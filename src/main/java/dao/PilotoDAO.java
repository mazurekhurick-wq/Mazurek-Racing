package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import model.Piloto;
import util.NegocioException;

/** CRUD da tabela piloto. */
public class PilotoDAO {

	private static final String SELECT = """
			SELECT p.id, p.nome, p.numero, p.nacionalidade, p.data_nascimento, p.vitorias,
			       p.titulos, p.foto, p.equipe_id, e.nome AS equipe_nome, e.cor AS equipe_cor
			FROM piloto p INNER JOIN equipe e ON e.id = p.equipe_id
			""";

	private static final String ERRO_INTEGRIDADE = "Não foi possível salvar: o número %d já é de outro piloto ou a equipe é inválida.";

	// CREATE
	public void cadastrar(Piloto p) throws Exception {
		String sql = """
				INSERT INTO piloto (nome, numero, nacionalidade, data_nascimento, vitorias, titulos, foto, equipe_id)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?)
				""";

		try (Connection c = ConexaoDB.getConexao(); PreparedStatement st = c.prepareStatement(sql)) {
			preencher(st, p);
			st.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException ex) {
			throw new NegocioException(String.format(ERRO_INTEGRIDADE, p.getNumero()));
		} catch (SQLException ex) {
			throw new Exception("Erro ao cadastrar o piloto!", ex);
		}
	}

	// READ (lista; equipeId = 0 traz todos os pilotos)
	public List<Piloto> listar(int equipeId) throws Exception {
		String sql = SELECT + (equipeId > 0 ? "WHERE p.equipe_id = ? " : "") + "ORDER BY e.nome, p.numero";
		List<Piloto> lista = new ArrayList<>();

		try (Connection c = ConexaoDB.getConexao(); PreparedStatement st = c.prepareStatement(sql)) {
			if (equipeId > 0) {
				st.setInt(1, equipeId);
			}
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					lista.add(criar(rs));
				}
			}
		} catch (SQLException ex) {
			throw new Exception("Erro ao listar os pilotos!", ex);
		}
		return lista;
	}

	// READ (um registro)
	public Piloto buscarPorId(int id) throws Exception {
		try (Connection c = ConexaoDB.getConexao(); PreparedStatement st = c.prepareStatement(SELECT + "WHERE p.id = ?")) {
			st.setInt(1, id);
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					return criar(rs);
				}
			}
		} catch (SQLException ex) {
			throw new Exception("Erro ao buscar o piloto!", ex);
		}
		return null;
	}

	// UPDATE
	public boolean atualizar(Piloto p) throws Exception {
		String sql = """
				UPDATE piloto SET nome = ?, numero = ?, nacionalidade = ?, data_nascimento = ?,
				       vitorias = ?, titulos = ?, foto = ?, equipe_id = ?
				WHERE id = ?
				""";

		try (Connection c = ConexaoDB.getConexao(); PreparedStatement st = c.prepareStatement(sql)) {
			preencher(st, p);
			st.setInt(9, p.getId());
			return st.executeUpdate() > 0;
		} catch (SQLIntegrityConstraintViolationException ex) {
			throw new NegocioException(String.format(ERRO_INTEGRIDADE, p.getNumero()));
		} catch (SQLException ex) {
			throw new Exception("Erro ao atualizar o piloto!", ex);
		}
	}

	// DELETE
	public boolean excluir(int id) throws Exception {
		try (Connection c = ConexaoDB.getConexao();
				PreparedStatement st = c.prepareStatement("DELETE FROM piloto WHERE id = ?")) {
			st.setInt(1, id);
			return st.executeUpdate() > 0;
		} catch (SQLException ex) {
			throw new Exception("Erro ao excluir o piloto!", ex);
		}
	}

	// Preenche os 8 primeiros parâmetros (iguais no INSERT e no UPDATE)
	private void preencher(PreparedStatement st, Piloto p) throws SQLException {
		st.setString(1, p.getNome());
		st.setInt(2, p.getNumero());
		st.setString(3, p.getNacionalidade());
		st.setDate(4, p.getDataNascimento());
		st.setInt(5, p.getVitorias());
		st.setInt(6, p.getTitulos());
		st.setString(7, p.getFoto());
		st.setInt(8, p.getEquipeId());
	}

	private Piloto criar(ResultSet rs) throws SQLException {
		Piloto p = new Piloto();
		p.setId(rs.getInt("id"));
		p.setNome(rs.getString("nome"));
		p.setNumero(rs.getInt("numero"));
		p.setNacionalidade(rs.getString("nacionalidade"));
		p.setDataNascimento(rs.getDate("data_nascimento"));
		p.setVitorias(rs.getInt("vitorias"));
		p.setTitulos(rs.getInt("titulos"));
		p.setFoto(rs.getString("foto"));
		p.setEquipeId(rs.getInt("equipe_id"));
		p.setEquipeNome(rs.getString("equipe_nome"));
		p.setEquipeCor(rs.getString("equipe_cor"));
		return p;
	}
}
