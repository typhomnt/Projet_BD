import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class GestionMateriel {

	public static void inventaire(Connection c) throws SQLException {
		Savepoint sp = c.setSavepoint();
		System.out.println("Inventaire du materiel dans chaque centre");
		Statement stmt;
		ResultSet rset;
		try {
			stmt = c.createStatement();
			rset = stmt.executeQuery(	"SELECT * " +
										"FROM materiel");
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
	
	public static void suppression(Connection c) throws SQLException {
		Savepoint sp = c.setSavepoint();
		System.out.println("Suppression de materiel dans un centre donné");
		PreparedStatement stmt;
		ResultSet rset;
		Scanner sc = new Scanner(System.in);
		int centre;
		int materiel;
		int supprMat;
		try {
			Timestamp dateCour = new Timestamp(System.currentTimeMillis());
			stmt = c.prepareStatement("SELECT codecentre " +
										"FROM centre");
			rset = stmt.executeQuery();
			Main.dumpResultSet(rset);
			
			//dateCour.setTime(dateCour.getTime() + 5*3600*1000);
			//System.out.println("datecour + 5 heures = " + dateCour);
			stmt.close();
			stmt = c.prepareStatement("SELECT * FROM materiel WHERE codecentre = ?");								
			System.out.println("Dans quel centre voulez-vous ajouter du materiel ?");
			centre = sc.nextInt();
			stmt.setInt(1, centre);
			rset.close();
			rset = stmt.executeQuery();
			Main.dumpResultSet(rset);
			
			System.out.println("Quel matériel voulez-vous enlever ?");
			materiel = sc.nextInt();
			
			stmt.close();
			stmt = c.prepareStatement("SELECT QuantiteMateriel FROM Materiel WHERE NumMateriel = ? AND CodeCentre = ?");
			stmt.setInt(1, materiel);
			stmt.setInt(2, centre);
			rset.close();
			rset = stmt.executeQuery();
			int quantiteCentre = 0;
			if (rset.next()) {
				quantiteCentre = rset.getInt(1);
			} else {
				System.out.println("No element retrieved");
			}
			Main.dumpResultSet(rset);
			
			stmt.close();
			stmt = c.prepareStatement("SELECT s.DateSeance, s.HeureDebutSeance, s.Duree, u.QuantiteNecessaire FROM seance s, utilise u WHERE u.nummateriel = ? and u.codecentre = ? and u.NumSeance = s.NumSeance and u.CodeGroupe = s.CodeGroupe");
			stmt.setInt(1, materiel);
			stmt.setInt(2, centre);
			rset.close();
			rset = stmt.executeQuery();
			int quantiteUtilisee = 0;
			while (rset.next()) {
				Timestamp debutSeance = new Timestamp(rset.getDate(1).getTime());
				int heure = rset.getInt(2);
				int duree = rset.getInt(3);
				int quantiteSeance = rset.getInt(4);
				debutSeance.setTime(debutSeance.getTime() + heure*3600*1000);
				Timestamp finSeance = new Timestamp(debutSeance.getTime());
				finSeance.setTime(finSeance.getTime() + duree*3600*1000);
				if (debutSeance.before(dateCour) && dateCour.before(finSeance))
					quantiteUtilisee += quantiteSeance;
			}
			
			System.out.println("Quelle quantite, inferieur a " + (quantiteCentre-quantiteUtilisee) + ", voulez-vous supprimer ?");
			supprMat = sc.nextInt();
			if (supprMat <= (quantiteCentre-quantiteUtilisee)) {
				stmt.close();
				stmt = c.prepareStatement("UPDATE materiel SET quantitemateriel = quantitemateriel - ? WHERE codecentre = ? and nummateriel = ?");
				stmt.setInt(1, supprMat);
				stmt.setInt(2, centre);
				stmt.setInt(3, materiel);
				rset = stmt.executeQuery();
				rset.close();
				stmt.close();
				System.out.println("Suppression reussie");
				c.commit();
			} else {
				System.out.println("Quantite trop grande");
				rset.close();
				stmt.close();
			}
		} catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
		}
	}
}
