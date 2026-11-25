package controller;

import dao.EquipeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.Equipe;
import util.Arquivos;
import util.NegocioException;
import util.Web;

/**
 * Controller do CRUD de equipes.
 *
 *   GET  /equipes         -> READ   (lista as equipes)
 *   GET  /novaEquipe      -> abre o formulário vazio
 *   POST /novaEquipe      -> CREATE (grava a equipe e a imagem)
 *   GET  /editarEquipe    -> abre o formulário preenchido (?id=)
 *   POST /editarEquipe    -> UPDATE (a imagem antiga é mantida se nenhuma nova for enviada)
 *   POST /excluirEquipe   -> DELETE (equipe com pilotos não pode ser excluída)
 */
@WebServlet(name = "EquipeController", urlPatterns = { "/equipes", "/novaEquipe", "/editarEquipe", "/excluirEquipe" })
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class EquipeController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final EquipeDAO equipeDao = new EquipeDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String rota = request.getServletPath();

		try {
			if ("/equipes".equals(rota)) {
				request.setAttribute("equipes", equipeDao.listar());
				request.getRequestDispatcher("equipes.jsp").forward(request, response);

			} else if ("/novaEquipe".equals(rota)) {
				request.getRequestDispatcher("formEquipe.jsp").forward(request, response);

			} else if ("/editarEquipe".equals(rota)) {
				Equipe equipe = equipeDao.buscarPorId(Web.inteiro(request.getParameter("id"), 0));
				if (equipe == null) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Equipe não encontrada.");
					return;
				}
				request.setAttribute("equipe", equipe);
				request.getRequestDispatcher("formEquipe.jsp").forward(request, response);

			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (Exception ex) {
			throw new ServletException("Erro ao carregar as equipes.", ex);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String rota = request.getServletPath();

		// Se a imagem passar de 5 MB o Tomcat rejeita a requisição inteira
		try {
			request.getParts();
		} catch (IllegalStateException ex) {
			request.getSession().setAttribute("errorMessage", "A imagem enviada é grande demais (máximo 5 MB).");
			response.sendRedirect(request.getContextPath() + "/equipes");
			return;
		}

		try {
			if ("/novaEquipe".equals(rota)) {
				salvar(request, response, true);
			} else if ("/editarEquipe".equals(rota)) {
				salvar(request, response, false);
			} else if ("/excluirEquipe".equals(rota)) {
				excluir(request, response);
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (Exception ex) {
			throw new ServletException("Erro ao salvar a equipe.", ex);
		}
	}

	/** CREATE (novo = true) e UPDATE (novo = false). */
	private void salvar(HttpServletRequest request, HttpServletResponse response, boolean novo) throws Exception {
		Equipe e = new Equipe();
		e.setId(Web.inteiro(request.getParameter("id"), 0));
		e.setNome(Web.texto(request.getParameter("nome")));
		e.setPais(Web.texto(request.getParameter("pais")));
		e.setMotor(Web.texto(request.getParameter("motor")));
		e.setCor(Web.texto(request.getParameter("cor")));

		String ano = Web.texto(request.getParameter("anoFundacao"));
		e.setAnoFundacao(ano.isEmpty() ? null : Web.inteiro(ano, -1));

		// Equipe atual (na edição): serve para manter a imagem antiga se nenhuma nova for enviada
		Equipe atual = null;
		if (!novo) {
			atual = equipeDao.buscarPorId(e.getId());
			if (atual == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Equipe não encontrada.");
				return;
			}
			e.setLogo(atual.getLogo());
		}

		String erro = validar(e);

		String novaLogo = null;
		if (erro == null) {
			try {
				novaLogo = Arquivos.salvarImagem(request.getPart("logo"));
			} catch (ServletException ex) {
				erro = ex.getMessage();
			}
		}

		if (erro == null) {
			if (novaLogo != null) {
				e.setLogo(novaLogo);
			}
			try {
				if (novo) {
					equipeDao.cadastrar(e);
				} else if (!equipeDao.atualizar(e)) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Equipe não encontrada.");
					return;
				}
				if (!novo && novaLogo != null) {
					Arquivos.excluirImagem(atual.getLogo()); // apaga a imagem antiga
				}
			} catch (NegocioException ex) {
				erro = ex.getMessage();
				Arquivos.excluirImagem(novaLogo); // não deixa imagem órfã
			}
		}

		if (erro != null) {
			// Volta para o formulário mantendo o que a pessoa digitou
			if (novaLogo != null) {
				e.setLogo(atual != null ? atual.getLogo() : null);
			}
			request.setAttribute("erro", erro);
			request.setAttribute("equipe", e);
			request.getRequestDispatcher("formEquipe.jsp").forward(request, response);
			return;
		}

		request.getSession().setAttribute("successMessage",
				novo ? "Equipe cadastrada com sucesso!" : "Equipe atualizada com sucesso!");
		response.sendRedirect(request.getContextPath() + "/equipes");
	}

	/** DELETE. */
	private void excluir(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = Web.inteiro(request.getParameter("id"), 0);
		Equipe equipe = equipeDao.buscarPorId(id);

		if (equipe == null) {
			request.getSession().setAttribute("errorMessage", "Equipe não encontrada.");
		} else {
			try {
				if (equipeDao.excluir(id)) {
					Arquivos.excluirImagem(equipe.getLogo());
					request.getSession().setAttribute("successMessage", "Equipe excluída com sucesso!");
				} else {
					request.getSession().setAttribute("errorMessage", "Erro ao excluir a equipe!");
				}
			} catch (NegocioException ex) {
				request.getSession().setAttribute("errorMessage", ex.getMessage());
			}
		}
		response.sendRedirect(request.getContextPath() + "/equipes");
	}

	private String validar(Equipe e) {
		if (e.getNome().isEmpty() || e.getPais().isEmpty() || e.getMotor().isEmpty()) {
			return "Nome, país e motor são obrigatórios.";
		}
		if (e.getAnoFundacao() != null && (e.getAnoFundacao() < 1900 || e.getAnoFundacao() > 2100)) {
			return "O ano de fundação deve estar entre 1900 e 2100.";
		}
		// A cor é usada em atributos style="" das páginas, por isso só aceita o formato #RRGGBB
		if (!e.getCor().matches("#[0-9A-Fa-f]{6}")) {
			return "Cor inválida.";
		}
		return null;
	}
}
