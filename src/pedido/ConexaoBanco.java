package pedido;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConexaoBanco {

	private Connection connection;
	private Statement statement;
	
	public ConexaoBanco() {}

	public ConexaoBanco(Connection connection, Statement statement) {
		this.connection = connection;
		this.statement = statement;
	}

	public Connection conectar() {
		String servidor="jdbc:mysql://localhost:3306/db_pedido";
		String usuario="root";
		String senha="r";
		String driver="com.mysql.cj.jdbc.Driver";
		try {
			Class.forName(driver);
			this.connection=DriverManager.getConnection(servidor, usuario, senha);
			this.setStatement(this.connection.createStatement());
		} catch (Exception e) {
			connection = null;
		}
		
		return connection;
	}
	
	public Statement getStatement() {
		return statement;
	}
	public void setStatement(Statement statement) {
		this.statement = statement;
	}
	
}
