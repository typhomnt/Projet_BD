import java.sql.*;


public class AffichageTable {
	public static void affichageGroupe(Connection c) throws SQLException {
		//On fait un SavePoint en cas d'exception
		Savepoint sp = c.setSavepoint();
		Statement stmt;
		ResultSet rset;
		try {
			stmt = c.createStatement();
			rset = stmt.executeQuery("SELECT * FROM GROUPE");
			Main.dumpResultSet(rset);
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
			Main.graphique.mainMenu();
		}
	}
	
	public static void affichageCentre(Connection c) throws SQLException {
		//On fait un SavePoint en cas d'exception
		Savepoint sp = c.setSavepoint();
		Statement stmt;
		ResultSet rset;
		try {
			stmt = c.createStatement();
			rset = stmt.executeQuery("SELECT * FROM Centre");
			Main.dumpResultSet(rset);
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
			Main.graphique.mainMenu();
		}
	}
	

	public static void affichageActivite(Connection c) throws SQLException {
		//On fait un SavePoint en cas d'exception
		Savepoint sp = c.setSavepoint();
		Statement stmt;
		ResultSet rset;
		try {
			stmt = c.createStatement();
			rset = stmt.executeQuery("SELECT * FROM Activite");
			Main.dumpResultSet(rset);
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
			Main.graphique.mainMenu();
		}
	}
	

	public static void affichageStagiaire(Connection c) throws SQLException {
		//On fait un SavePoint en cas d'exception
		Savepoint sp = c.setSavepoint();
		Statement stmt;
		ResultSet rset;
		try {
			stmt = c.createStatement();
			rset = stmt.executeQuery("SELECT p.* FROM Personne p, Stagiaire s WHERE p.codePersonne = s.codePersonne");
			Main.dumpResultSet(rset);
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
			Main.graphique.mainMenu();
		}
	}
	

	public static void affichageMateriel(Connection c) throws SQLException {
		//On fait un SavePoint en cas d'exception
		Savepoint sp = c.setSavepoint();
		Statement stmt;
		ResultSet rset;
		try {
			stmt = c.createStatement();
			rset = stmt.executeQuery("SELECT * FROM Materiel");
			Main.dumpResultSet(rset);
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
			Main.graphique.mainMenu();
		}
	}
	
	public static void affichageInscriptionDansCentre(Connection c,Integer stag) throws SQLException {
		//On fait un SavePoint en cas d'exception
		Savepoint sp = c.setSavepoint();
		PreparedStatement stmt;
		ResultSet rset;
		try {
			stmt = c.prepareStatement("SELECT * FROM EstInscritDansCentre WHERE codePersonne = ?");
			stmt.setInt(1, stag);
			rset = stmt.executeQuery();
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
