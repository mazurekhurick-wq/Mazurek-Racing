package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.Equipe;
import util.NegocioException;

/** CRUD da tabela equipe. */
public class EquipeDAO {

	// Traz também quantos pilotos a equipe tem (usado nos cartões e na exclusão)
	private static final String SELECT = """
			SELECT e.id, e.nome, e.pais, e.motor, e.ano_fundacao, e.cor, e.logo,
			       (SELECT COUNT(*) FROM piloto p WHERE p.equipe_id = e.id) AS total_pilotos
			FROM equipe e
			""";

	// CREATE
	public void cadastrar(Equipe e) throws Exception {
		String sql = "INSERT INTO equipe (nome, pais, motor, ano_fundacao, cor, logo) VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection c = ConexaoDB.getConexao(); PreparedStatement st = c.prepareStatement(sql)) {
			preencher(st, e);
			st.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException ex) {
			throw new NegocioException("Já existe uma equipe com o nome \"" + e.getNome() + "\".");
		} catch (SQLException ex) {
			throw new Exception("Erro ao cadastrar a equipe!", ex);
		}
	}

	// READ (lista)
	public List<Equipe> listar() throws Exception {
		List<Equipe> lista = new ArrayList<>();

		try (Connection c = ConexaoDB.getConexao();
				PreparedStatement st = c.prepareStatement(SELECT + "ORDER BY e.nome");
				ResultSet rs = st.executeQuery()) {
			while (rs.next()) {
				lista.add(criar(rs));
			}
		} catch (SQLException ex) {
			throw new Exception("Erro ao listar as equipes!", ex);
		}
		return lista;
	}

	// READ (um registro)
	public Equipe buscarPorId(int id) throws Exception {
		try (Connection c = ConexaoDB.getConexao(); PreparedStatement st = c.prepareStatement(SELECT + "WHERE e.id = ?")) {
			st.setInt(1, id);
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					return criar(rs);
				}
			}
		} catch (SQLException ex) {
			throw new Exception("Erro ao buscar a equipe!", ex);
		}
		return null;
	}

	// UPDATE
	public boolean atualizar(Equipe e) throws Exception {
		String sql = "UPDATE equipe SET nome = ?, pais = ?, motor = ?, ano_fundacao = ?, cor = ?, logo = ? WHERE id = ?";

		try (Connection c = ConexaoDB.getConexao(); PreparedStatement st = c.prepareStatement(sql)) {
			preencher(st, e);
			st.setInt(7, e.getId());
			return st.executeUpdate() > 0;
		} catch (SQLIntegrityConstraintViolationException ex) {
			throw new NegocioException("Já existe uma equipe com o nome \"" + e.getNome() + "\".");
		} catch (SQLException ex) {
			throw new Exception("Erro ao atualizar a equipe!", ex);
		}
	}

	// DELETE
	public boolean excluir(int id) throws Exception {
		try (Connection c = ConexaoDB.getConexao();
				PreparedStatement st = c.prepareStatement("DELETE FROM equipe WHERE id = ?")) {
			st.setInt(1, id);
			return st.executeUpdate() > 0;
		} catch (SQLIntegrityConstraintViolationException ex) {
			throw new NegocioException("Não é possível excluir: a equipe ainda tem pilotos. Exclua ou mude os pilotos antes.");
		} catch (SQLException ex) {
			throw new Exception("Erro ao excluir a equipe!", ex);
		}
	}

	// Preenche os 6 primeiros parâmetros (iguais no INSERT e no UPDATE)
	private void preencher(PreparedStatement st, Equipe e) throws SQLException {
		st.setString(1, e.getNome());
		st.setString(2, e.getPais());
		st.setString(3, e.getMotor());
		if (e.getAnoFundacao() == null) {
			st.setNull(4, Types.INTEGER);
		} else {
			st.setInt(4, e.getAnoFundacao());
		}
		st.setString(5, e.getCor());
		st.setString(6, e.getLogo());
	}

	private Equipe criar(ResultSet rs) throws SQLException {
		Equipe e = new Equipe();
		e.setId(rs.getInt("id"));
		e.setNome(rs.getString("nome"));
		e.setPais(rs.getString("pais"));
		e.setMotor(rs.getString("motor"));
		int ano = rs.getInt("ano_fundacao");
		e.setAnoFundacao(rs.wasNull() ? null : ano);
		e.setCor(rs.getString("cor"));
		e.setLogo(rs.getString("logo"));
		e.setTotalPilotos(rs.getInt("total_pilotos"));
		return e;
	}
}
