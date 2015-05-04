import java.sql.*;


public class ClassementVilles {

	public static void classement(Connection c) throws SQLException {
		Savepoint sp = c.setSavepoint();
		System.out.println("Classement des villes en fonction du nombre d'inscrits");
		Statement stmt;
		ResultSet rset;
		try {
			stmt = c.createStatement();
			rset = stmt.executeQuery(	"SELECT DISTINCT ap.VilleAdr as Ville, count(i.CodePersonne) as Nb_Inscrits " +
										"FROM Centre c, EstInscritDansCentre i, Adresse_Postale ap " +
										"WHERE ap.VilleAdr = c.VilleAdr AND ap.NumAdr = c.NumAdr AND ap.RueAdr = c.RueAdr " +
										"AND ap.CodeAdr = c.CodeAdr AND i.CodeCentre = c.CodeCentre " +											
										"GROUP BY ap.VilleAdr " +
										"UNION " +
										"SELECT DISTINCT ap.VilleAdr as Ville, 0 " +											
										"FROM Adresse_Postale ap, Centre c " +
										"WHERE ap.VilleAdr NOT IN (	SELECT c.VilleAdr " +
										"FROM Centre c, EstInscritDansCentre i " +
										"WHERE c.CodeCentre = i.CodeCentre	) " +
										"ORDER BY Nb_Inscrits"	);
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
