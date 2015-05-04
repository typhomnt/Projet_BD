import java.sql.*;

public class VisualSeance {
	
	public static void visual(Connection c) throws SQLException {
		Savepoint sp = c.setSavepoint();
		System.out.println("Visualisation des séances planifiées");
		Statement stmt;
		ResultSet rset;
		try {
			stmt = c.createStatement();
			rset = stmt.executeQuery(	"SELECT * " +
										"FROM seance");
			Main.dumpResultSet(rset);
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
			Main.graphique.mainMenu();
		}
	}
}
