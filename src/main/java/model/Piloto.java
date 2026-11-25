package model;

import java.sql.Date;

/** Piloto de Fórmula 1. Os campos equipeNome/equipeCor vêm do JOIN com a tabela equipe. */
public class Piloto {

	public static final String FOTO_PADRAO = "assets/img/piloto-padrao.svg";

	private int id;
	private String nome;
	private int numero;
	private String nacionalidade;
	private Date dataNascimento;
	private int vitorias;
	private int titulos;
	private String foto;
	private int equipeId;
	private String equipeNome;
	private String equipeCor;

	public Piloto() {
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	public int getNumero() { return numero; }
	public void setNumero(int numero) { this.numero = numero; }
	public String getNacionalidade() { return nacionalidade; }
	public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }
	public Date getDataNascimento() { return dataNascimento; }
	public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }
	public int getVitorias() { return vitorias; }
	public void setVitorias(int vitorias) { this.vitorias = vitorias; }
	public int getTitulos() { return titulos; }
	public void setTitulos(int titulos) { this.titulos = titulos; }
	public String getFoto() { return foto; }
	public void setFoto(String foto) { this.foto = foto; }
	public int getEquipeId() { return equipeId; }
	public void setEquipeId(int equipeId) { this.equipeId = equipeId; }
	public String getEquipeNome() { return equipeNome; }
	public void setEquipeNome(String equipeNome) { this.equipeNome = equipeNome; }
	public String getEquipeCor() { return equipeCor; }
	public void setEquipeCor(String equipeCor) { this.equipeCor = equipeCor; }

	/** Caminho da foto para usar no JSP: a do piloto ou, se não houver, a imagem padrão. */
	public String getFotoUrl() {
		return (foto == null || foto.isBlank()) ? FOTO_PADRAO : foto;
	}
}
