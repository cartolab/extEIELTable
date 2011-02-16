package es.udc.cartolab.gvsig.eieltable.domain.generator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DBAux2 {

	private static Connection conn = null;

	public static Connection getConnection() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://193.144.59.201:5432/eiel_pontevedra_2009";
			conn = DriverManager.getConnection(url, "psanxiao", "i08carto");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

}
