import java.sql.*;
import java.util.Scanner;

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
	
	public static void ajout(Connection c) throws SQLException {
		Savepoint sp = c.setSavepoint();
		System.out.println("Ajout de materiel dans un centre donné");
		PreparedStatement stmt;
		ResultSet rset;
		Scanner sc = new Scanner(System.in);
		int centre;
		int materiel;
		int ajoutMat;
		try {
			stmt = c.prepareStatement("SELECT codecentre " +
										"FROM centre");
			rset = stmt.executeQuery();
			Main.dumpResultSet(rset);
			
			stmt.close();
			stmt = c.prepareStatement("SELECT * FROM materiel WHERE codecentre = ?");								
			System.out.println("Dans quel centre voulez-vous ajouter du materiel ?");
			centre = sc.nextInt();
			stmt.setInt(1, centre);
			rset.close();
			rset = stmt.executeQuery();
			/*while (!rset.next()) {
				System.out.println("Ce centre n'existe pas. Choisissez un autre ou tapez -1 pour sortir.");
				centre = sc.nextInt();
				if (centre < 0) {
					try {
						c.close();
						System.err.println("Connexion fermee");
					} catch (SQLException e) {
						System.err.println("Impossible de fermer la connexion");
						e.printStackTrace();
					}
				}
				stmt.close();
				stmt = c.prepareStatement("SELECT * FROM materiel WHERE codecentre = ?");
				stmt.setInt(1, centre);
				rset.close();
				rset = stmt.executeQuery();
			}*/
			Main.dumpResultSet(rset);

			stmt.close();
			stmt = c.prepareStatement("UPDATE materiel SET quantitemateriel = quantitemateriel + ? WHERE codecentre = ? and nummateriel = ?");
			System.out.println("Quel materiel voulez-vous ajouter ?");
			materiel = sc.nextInt();
			System.out.println("Quelle quantite voulez-vous ajouter ?");
			ajoutMat = sc.nextInt();
			stmt.setInt(1, ajoutMat);
			stmt.setInt(2, centre);
			stmt.setInt(3, materiel);
			rset.close();
			rset = stmt.executeQuery();
			rset.close();
			stmt.close();
			System.out.println("Ajout reussie");
			c.commit();
		} catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
		}
	}
}
