package model;

/** Equipe (construtor) de Fórmula 1. */
public class Equipe {

	public static final String LOGO_PADRAO = "assets/img/equipe-padrao.svg";

	private int id;
	private String nome;
	private String pais;
	private String motor;
	private Integer anoFundacao;
	private String cor = "#E10600";
	private String logo;
	private int totalPilotos;

	public Equipe() {
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	public String getPais() { return pais; }
	public void setPais(String pais) { this.pais = pais; }
	public String getMotor() { return motor; }
	public void setMotor(String motor) { this.motor = motor; }
	public Integer getAnoFundacao() { return anoFundacao; }
	public void setAnoFundacao(Integer anoFundacao) { this.anoFundacao = anoFundacao; }
	public String getCor() { return cor; }
	public void setCor(String cor) { this.cor = cor; }
	public String getLogo() { return logo; }
	public void setLogo(String logo) { this.logo = logo; }
	public int getTotalPilotos() { return totalPilotos; }
	public void setTotalPilotos(int totalPilotos) { this.totalPilotos = totalPilotos; }

	/** Caminho da imagem para usar no JSP: a da equipe ou, se não houver, a imagem padrão. */
	public String getLogoUrl() {
		return (logo == null || logo.isBlank()) ? LOGO_PADRAO : logo;
	}
}
