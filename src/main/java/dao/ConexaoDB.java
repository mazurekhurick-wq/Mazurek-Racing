package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexão com o MariaDB do XAMPP.
 * Se o seu banco usar outra porta, usuário ou senha, altere SOMENTE os campos abaixo.
 */
public class ConexaoDB {

	public static String db = "mazurek_racing";
	public static String url = "jdbc:mysql://localhost:3306/" + db
			+ "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
	public static String usuario = "root";
	public static String senha = "";

	public static Connection getConexao() throws SQLException {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new SQLException("Driver JDBC não encontrado", e);
		}

		try {
			return DriverManager.getConnection(url, usuario, senha);
		} catch (SQLException e) {
			throw new SQLException("Erro ao conectar com o banco de dados (o MySQL do XAMPP está ligado?)", e);
		}
	}
}
