import java.sql.*;


public class ClassementCentres {
	
	public static void classement(Connection c) throws SQLException {
		Savepoint sp = c.setSavepoint();
		System.out.println("Pour chaque activité, classement des centres en fonction" +
							" du nombre d'inscrits");
		Statement stmt;
		ResultSet rset;
		try {
			stmt = c.createStatement();
			rset = stmt.executeQuery(	"SELECT DISTINCT a.NomAct, c.NomCentre as Centre, count(*) as Nb_Inscrits " +
										"FROM Activite a, EstDansGroupe edg, Centre c, Groupe g " +
										"WHERE edg.CodeGroupe = g.CodeGroupe AND g.CodeAct = a.CodeAct AND g.CodeCentre = c.CodeCentre " +
										"GROUP BY a.NomAct, c.NomCentre " +
										"ORDER BY a.NomAct, Nb_Inscrits"	);
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
