package controller;

import dao.EquipeDAO;
import dao.PilotoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import model.Piloto;
import util.Arquivos;
import util.NegocioException;
import util.Web;

/**
 * Controller do CRUD de pilotos.
 *
 *   GET  /pilotos         -> READ   (lista os pilotos, com filtro por equipe)
 *   GET  /novoPiloto      -> abre o formulário vazio
 *   POST /novoPiloto      -> CREATE (grava o piloto e a foto)
 *   GET  /editarPiloto    -> abre o formulário preenchido (?id=)
 *   POST /editarPiloto    -> UPDATE (a foto antiga é mantida se nenhuma nova for enviada)
 *   POST /excluirPiloto   -> DELETE
 */
@WebServlet(name = "PilotoController", urlPatterns = { "/pilotos", "/novoPiloto", "/editarPiloto", "/excluirPiloto" })
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class PilotoController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final PilotoDAO pilotoDao = new PilotoDAO();
	private final EquipeDAO equipeDao = new EquipeDAO();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String rota = request.getServletPath();

		try {
			if ("/pilotos".equals(rota)) {
				int equipeId = Web.inteiro(request.getParameter("equipe"), 0);
				request.setAttribute("pilotos", pilotoDao.listar(equipeId));
				request.setAttribute("equipes", equipeDao.listar());
				request.setAttribute("equipeFiltro", equipeId);
				request.getRequestDispatcher("pilotos.jsp").forward(request, response);

			} else if ("/novoPiloto".equals(rota)) {
				request.setAttribute("equipes", equipeDao.listar());
				request.getRequestDispatcher("formPiloto.jsp").forward(request, response);

			} else if ("/editarPiloto".equals(rota)) {
				Piloto piloto = pilotoDao.buscarPorId(Web.inteiro(request.getParameter("id"), 0));
				if (piloto == null) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Piloto não encontrado.");
					return;
				}
				request.setAttribute("piloto", piloto);
				request.setAttribute("equipes", equipeDao.listar());
				request.getRequestDispatcher("formPiloto.jsp").forward(request, response);

			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (Exception ex) {
			throw new ServletException("Erro ao carregar os pilotos.", ex);
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
			response.sendRedirect(request.getContextPath() + "/pilotos");
			return;
		}

		try {
			if ("/novoPiloto".equals(rota)) {
				salvar(request, response, true);
			} else if ("/editarPiloto".equals(rota)) {
				salvar(request, response, false);
			} else if ("/excluirPiloto".equals(rota)) {
				excluir(request, response);
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (Exception ex) {
			throw new ServletException("Erro ao salvar o piloto.", ex);
		}
	}

	/** CREATE (novo = true) e UPDATE (novo = false). */
	private void salvar(HttpServletRequest request, HttpServletResponse response, boolean novo) throws Exception {
		Piloto p = new Piloto();
		p.setId(Web.inteiro(request.getParameter("id"), 0));
		p.setNome(Web.texto(request.getParameter("nome")));
		p.setNumero(Web.inteiro(request.getParameter("numero"), 0));
		p.setNacionalidade(Web.texto(request.getParameter("nacionalidade")));
		p.setVitorias(Web.inteiro(request.getParameter("vitorias"), 0));
		p.setTitulos(Web.inteiro(request.getParameter("titulos"), 0));
		p.setEquipeId(Web.inteiro(request.getParameter("equipeId"), 0));

		String nascimento = Web.texto(request.getParameter("dataNascimento"));
		String erro = null;
		try {
			p.setDataNascimento(nascimento.isEmpty() ? null : Date.valueOf(nascimento));
		} catch (IllegalArgumentException ex) {
			erro = "Data de nascimento inválida.";
		}

		// Piloto atual (na edição): serve para manter a foto antiga se nenhuma nova for enviada
		Piloto atual = null;
		if (!novo) {
			atual = pilotoDao.buscarPorId(p.getId());
			if (atual == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Piloto não encontrado.");
				return;
			}
			p.setFoto(atual.getFoto());
		}

		if (erro == null) {
			erro = validar(p);
		}

		String novaFoto = null;
		if (erro == null) {
			try {
				novaFoto = Arquivos.salvarImagem(request.getPart("foto"));
			} catch (ServletException ex) {
				erro = ex.getMessage();
			}
		}

		if (erro == null) {
			if (novaFoto != null) {
				p.setFoto(novaFoto);
			}
			try {
				if (novo) {
					pilotoDao.cadastrar(p);
				} else if (!pilotoDao.atualizar(p)) {
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Piloto não encontrado.");
					return;
				}
				if (!novo && novaFoto != null) {
					Arquivos.excluirImagem(atual.getFoto()); // apaga a foto antiga
				}
			} catch (NegocioException ex) {
				erro = ex.getMessage();
				Arquivos.excluirImagem(novaFoto); // não deixa foto órfã
			}
		}

		if (erro != null) {
			// Volta para o formulário mantendo o que a pessoa digitou
			if (novaFoto != null && atual != null) {
				p.setFoto(atual.getFoto());
			} else if (novaFoto != null) {
				p.setFoto(null);
			}
			request.setAttribute("erro", erro);
			request.setAttribute("piloto", p);
			request.setAttribute("equipes", equipeDao.listar());
			request.getRequestDispatcher("formPiloto.jsp").forward(request, response);
			return;
		}

		request.getSession().setAttribute("successMessage",
				novo ? "Piloto cadastrado com sucesso!" : "Piloto atualizado com sucesso!");
		response.sendRedirect(request.getContextPath() + "/pilotos");
	}

	/** DELETE. */
	private void excluir(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = Web.inteiro(request.getParameter("id"), 0);
		Piloto piloto = pilotoDao.buscarPorId(id);

		if (piloto == null) {
			request.getSession().setAttribute("errorMessage", "Piloto não encontrado.");
		} else if (pilotoDao.excluir(id)) {
			Arquivos.excluirImagem(piloto.getFoto());
			request.getSession().setAttribute("successMessage", "Piloto excluído com sucesso!");
		} else {
			request.getSession().setAttribute("errorMessage", "Erro ao excluir o piloto!");
		}
		response.sendRedirect(request.getContextPath() + "/pilotos");
	}

	private String validar(Piloto p) {
		if (p.getNome().isEmpty() || p.getNacionalidade().isEmpty()) {
			return "Nome e nacionalidade são obrigatórios.";
		}
		if (p.getNumero() < 1 || p.getNumero() > 99) {
			return "O número do piloto deve estar entre 1 e 99.";
		}
		if (p.getEquipeId() <= 0) {
			return "Selecione uma equipe.";
		}
		if (p.getVitorias() < 0 || p.getTitulos() < 0) {
			return "Vitórias e títulos não podem ser negativos.";
		}
		return null;
	}
}
