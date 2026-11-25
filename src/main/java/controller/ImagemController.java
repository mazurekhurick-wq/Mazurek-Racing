package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import util.Arquivos;

/**
 * Entrega ao navegador as imagens enviadas pelos formulários.
 * Elas ficam fora do projeto (pasta ~/mazurek-uploads), então este servlet faz a "ponte":
 * http://localhost:8080/MazurekRacing/assets/uploads/arquivo.png
 */
@WebServlet("/assets/uploads/*")
public class ImagemController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String caminho = request.getPathInfo();
		if (caminho == null || caminho.length() <= 1) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// Só o nome do arquivo é usado: impede caminhos como ../../senha.txt
		String nomeArquivo = Paths.get(caminho).getFileName().toString();
		Path arquivo = Arquivos.DIRETORIO_UPLOAD.resolve(nomeArquivo).normalize();

		if (!arquivo.startsWith(Arquivos.DIRETORIO_UPLOAD) || !Files.isRegularFile(arquivo)) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		response.setContentType(tipo(nomeArquivo));
		response.setHeader("X-Content-Type-Options", "nosniff");
		response.setContentLengthLong(Files.size(arquivo));
		Files.copy(arquivo, response.getOutputStream());
	}

	// Não depende do sistema operacional (probeContentType falha em alguns, principalmente com .webp)
	private String tipo(String nome) {
		String n = nome.toLowerCase();
		if (n.endsWith(".png")) {
			return "image/png";
		} else if (n.endsWith(".gif")) {
			return "image/gif";
		} else if (n.endsWith(".webp")) {
			return "image/webp";
		} else if (n.endsWith(".jpg") || n.endsWith(".jpeg")) {
			return "image/jpeg";
		}
		return "application/octet-stream";
	}
}
