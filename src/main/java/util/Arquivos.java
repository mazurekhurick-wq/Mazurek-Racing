package util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Salva e remove as imagens enviadas pelos formulários (foto do piloto e imagem da equipe).
 *
 * As imagens enviadas ficam FORA do projeto, na pasta do usuário (~/mazurek-uploads).
 * Assim não existe caminho fixo do Windows no código e o projeto funciona em qualquer
 * computador depois do git clone. Quem entrega essas imagens ao navegador é o ImagemController.
 */
public final class Arquivos {

	private static final Logger LOGGER = Logger.getLogger(Arquivos.class.getName());

	/** Prefixo gravado no banco para imagens enviadas (as de exemplo usam assets/img/...). */
	public static final String PREFIXO = "assets/uploads/";

	public static final Path DIRETORIO_UPLOAD = Paths.get(System.getProperty("user.home"), "mazurek-uploads")
			.toAbsolutePath().normalize();

	private Arquivos() {
	}

	/** Salva a imagem e devolve o caminho para gravar no banco (ou null se nada foi enviado). */
	public static String salvarImagem(Part parte) throws IOException, ServletException {
		if (parte == null || parte.getSize() == 0) {
			return null;
		}

		String nomeOriginal = parte.getSubmittedFileName();
		int ponto = nomeOriginal == null ? -1 : nomeOriginal.lastIndexOf('.');
		String extensao = ponto >= 0 ? nomeOriginal.substring(ponto).toLowerCase() : "";

		if (!extensao.matches("\\.(jpg|jpeg|png|gif|webp)")) {
			throw new ServletException("A imagem deve ser JPG, PNG, GIF ou WEBP.");
		}

		Files.createDirectories(DIRETORIO_UPLOAD);
		String nome = UUID.randomUUID() + extensao;
		Path destino = DIRETORIO_UPLOAD.resolve(nome).normalize();

		if (!destino.startsWith(DIRETORIO_UPLOAD)) {
			throw new ServletException("Caminho de upload inválido.");
		}

		try (InputStream entrada = parte.getInputStream()) {
			Files.copy(entrada, destino, StandardCopyOption.REPLACE_EXISTING);
		}
		return PREFIXO + nome;
	}

	/** Apaga uma imagem enviada. As imagens de exemplo (assets/img/...) nunca são apagadas. */
	public static void excluirImagem(String imagem) {
		if (imagem == null || !imagem.startsWith(PREFIXO)) {
			return;
		}
		try {
			String nome = Paths.get(imagem).getFileName().toString();
			Path arquivo = DIRETORIO_UPLOAD.resolve(nome).normalize();
			if (arquivo.startsWith(DIRETORIO_UPLOAD)) {
				Files.deleteIfExists(arquivo);
			}
		} catch (IOException ex) {
			LOGGER.log(Level.WARNING, "Não foi possível excluir a imagem.", ex);
		}
	}
}
