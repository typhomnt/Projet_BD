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
			Main.graphique.setInfoLab("Dans quel centre voulez-vous ajouter du materiel ?");
//			System.out.println("Dans quel centre voulez-vous ajouter du materiel ?");
//			centre = sc.nextInt();
			centre = Main.graphique.setVarInfoInt();
			stmt.setInt(1, centre);
			rset.close();
			rset = stmt.executeQuery();
			if(!rset.next()){
				throw new SQLException("Le centre entré n'existe pas");
			}
			Main.dumpResultSet(rset);

			stmt.close();
			stmt = c.prepareStatement("UPDATE materiel SET quantitemateriel = quantitemateriel + ? WHERE codecentre = ? and nummateriel = ?");
			//System.out.println("Quel materiel voulez-vous ajouter ?");
			Main.graphique.setInfoLab("Quel materiel voulez-vous ajouter ?");
			//materiel = sc.nextInt();
			materiel = Main.graphique.setVarInfoInt();
			//System.out.println("Quelle quantite voulez-vous ajouter ?");
			Main.graphique.setInfoLab("Quelle quantite voulez-vous ajouter ?");
			//ajoutMat = sc.nextInt();
			ajoutMat = Main.graphique.setVarInfoInt();
			if(ajoutMat < 0){
				throw new SQLException("Vous devez taper un nombre positif");
			}
			stmt.setInt(1, ajoutMat);
			stmt.setInt(2, centre);
			stmt.setInt(3, materiel);
			rset.close();
			rset = stmt.executeQuery();
			if(!rset.next()){
				throw new SQLException("Le matériel entré n'est pas dans le centre");
			}
			rset.close();
			stmt.close();
			System.out.println("Ajout reussie");
			c.commit();
			Main.graphique.mainMenu();
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
			Main.graphique.setInfoLab("Dans quel centre voulez-vous ajouter du materiel ?");
			//System.out.println("Dans quel centre voulez-vous ajouter du materiel ?");
			//centre = sc.nextInt();
			centre = Main.graphique.setVarInfoInt();
			stmt.setInt(1, centre);
			rset.close();
			rset = stmt.executeQuery();
			if(!rset.next()){
				throw new SQLException("Le centre entré n'existe pas");
			}
			Main.dumpResultSet(rset);
			
			//System.out.println("Quel matériel voulez-vous enlever ?");
			Main.graphique.setInfoLab("Quel matériel voulez-vous enlever ?");
			//materiel = sc.nextInt();
			materiel = Main.graphique.setVarInfoInt();
			stmt.close();
			stmt = c.prepareStatement("SELECT QuantiteMateriel FROM Materiel WHERE NumMateriel = ? AND CodeCentre = ?");
			stmt.setInt(1, materiel);
			stmt.setInt(2, centre);
			rset.close();
			rset = stmt.executeQuery();
			if(!rset.next()){
				throw new SQLException("Le matériel entré n'existe pas dans le centre");
			}
			int quantiteCentre = rset.getInt(1);
			
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
			
			stmt.close();
			//On cherche la quantité max de ce materiel qu'on utilisera pendant les seances apres la date courante
			stmt = c.prepareStatement("SELECT Max(Q.Quantite) "+
			"FROM "+
			"( "+
			//	--on fait la somme de materiel utilisé a chaque commencement de seance
			"	SELECT Dates.MomentTest, Sum(U.QuantiteNecessaire) as Quantite "+
			"	FROM Utilise U, Seance S, Groupe G, "+
			//	--on cherche a obtenir chaque nouveau commencement de seance entre le debut et la fin de notre seance
			"	(SELECT (S.DateSeance+S.HeureDebutSeance/24) as MomentTest "+
			"	    FROM Utilise U, Seance S, Groupe G "+
			"	        WHERE U.NumMateriel = ? and U.CodeCentre = g.CodeCentre and "+
			"			g.CodeCentre = ? and "+
			"		    U.CodeGroupe = G.CodeGroupe and U.NumSeance = S.NumSeance and "+
			"   		    (S.DateSeance+S.HeureDebutSeance/24) >= SYSDATE "+
			"	       ) Dates "+
			"	WHERE U.NumMateriel = ? and U.CodeCentre = g.CodeCentre and g.CodeCentre = ? "+
			"	and U.CodeGroupe = G.CodeGroupe and U.NumSeance = S.NumSeance "+
			"	and (S.DateSeance+S.HeureDebutSeance/24) IN Dates.MomentTest "+
			"	GROUP BY Dates.MomentTest) Q");
			stmt.setInt(1, materiel);
			stmt.setInt(2, centre);
			stmt.setInt(3, materiel);
			stmt.setInt(4, centre);
			rset.close();
			rset = stmt.executeQuery();
			int maxUtil = 0;
			if (rset.next()) {
				// Si on utilise plus de materiel
				maxUtil = rset.getInt(1);
				if (quantiteUtilisee <= maxUtil)
					quantiteUtilisee = maxUtil;
			}
			//System.out.println("Quelle quantite, inferieur a " + (quantiteCentre-quantiteUtilisee) + ", voulez-vous supprimer ?");
			Main.graphique.setInfoLab("Quelle quantite, inferieur a " + (quantiteCentre-quantiteUtilisee) + ", voulez-vous supprimer ?");
			//supprMat = sc.nextInt();
			supprMat = Main.graphique.setVarInfoInt();
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
				Main.graphique.mainMenu();
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
