package util;

/** Erro "esperado" (ex.: número de piloto repetido). A mensagem é mostrada ao usuário. */
public class NegocioException extends Exception {

	private static final long serialVersionUID = 1L;

	public NegocioException(String mensagem) {
		super(mensagem);
	}
}
