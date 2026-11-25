package util;

/** Pequenos utilitários compartilhados pelos controllers. */
public final class Web {

	private Web() {
	}

	/** Remove espaços das pontas; null vira texto vazio. */
	public static String texto(String valor) {
		return valor == null ? "" : valor.trim();
	}

	/** Converte para int; devolve o padrão se estiver vazio ou inválido. */
	public static int inteiro(String valor, int padrao) {
		try {
			return Integer.parseInt(valor.trim());
		} catch (Exception e) {
			return padrao;
		}
	}
}
