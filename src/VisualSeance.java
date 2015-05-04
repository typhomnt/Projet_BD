import java.sql.*;

import javax.swing.JOptionPane;

public class VisualSeance {
	
	public static void visual(Connection c) throws SQLException {
		Savepoint sp = c.setSavepoint();
		System.out.println("Visualisation des séances planifiées");
		Main.graphique.setInfReq("Visualisation des séances planifiées");
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
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
			Main.graphique.mainMenu();
		}
	}
}
