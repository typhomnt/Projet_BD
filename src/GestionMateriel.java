import java.sql.*;

public class GestionMateriel {

	public static void inventaire(Connection c) throws SQLException {
		Savepoint sp = c.setSavepoint();
		System.out.println("Inventaire du materiel dans chaque centre");
		Statement stmt;
		ResultSet rset;
		try {
			stmt = c.createStatement();
			rset = stmt.executeQuery(	"SELECT m.* " +
										"FROM centre c, materiel m " +
										"WHERE c.CodeCentre = m.CodeCentre");
			Main.dumpResultSet(rset);
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
		}
	}
}
