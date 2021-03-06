import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class PlanificationSeance {
	public static void register(Connection c) throws SQLException {

		// Ici on va utiliser un scanner pour récupérer les differentes
		// informations
		Scanner sc = new Scanner(System.in);
		Savepoint sp = c.setSavepoint();
		System.out.println("Enregistrement d'une seance");
		Main.graphique.setTitle("Enregistrement d'une seance");
		PreparedStatement stmt;
		ResultSet rset;
		int CodeGroupe;
		try {
			int t = 0;
			// On selectionne un groupe
			do {
				stmt = c.prepareStatement("SELECT g.CodeGroupe FROM Groupe g WHERE g.CodeGroupe = ?");
				if (t == 0) {
					//System.out.println("Entrez le numéro du groupe");
					Main.graphique.setInfoLab("Entrez le numéro du groupe");
				} else {
					//System.out.println("Numéro invalide, réessayez");
					Main.graphique.setInfoLab("Entrez le numéro du groupe");
				}
				t++;
				CodeGroupe = Main.graphique.setVarInfoInt();
				//CodeGroupe = sc.nextInt();
				//sc.nextLine();
				stmt.setInt(1, CodeGroupe);
				rset = stmt.executeQuery();
				// Tant que le numéro de groupe est invalide
			} while (!rset.next());
			stmt.close();
			stmt = c.prepareStatement("SELECT COUNT(DISTINCT edg.CodePersonne) "
					+ "FROM EstDansGroupe edg WHERE edg.CodeGroupe = ?");
			stmt.setInt(1, CodeGroupe);
			rset = stmt.executeQuery();
			rset.next();
			int participants = rset.getInt(1);

			stmt.close();
			stmt = c.prepareStatement("SELECT g.NbMinStagGroupe FROM Groupe g WHERE g.CodeGroupe = ?");
			stmt.setInt(1, CodeGroupe);
			rset = stmt.executeQuery();
			rset.next();
			int NbMin = rset.getInt(1);

			if (NbMin > participants) {
				c.rollback(sp);
				throw new SQLException(
						"Il n'y a pas assez de participants dans ce groupe");
			}

			stmt.close();
			stmt = c.prepareStatement("SELECT g.DateDebutGroupe FROM Groupe g WHERE g.CodeGroupe = ?");
			stmt.setInt(1, CodeGroupe);
			rset = stmt.executeQuery();
			rset.next();
			Date DebutG = rset.getDate(1);

			stmt.close();
			stmt = c.prepareStatement("SELECT g.DateFinGroupe FROM Groupe g WHERE g.CodeGroupe = ?");
			stmt.setInt(1, CodeGroupe);
			rset = stmt.executeQuery();
			rset.next();
			Date FinG = rset.getDate(1);
			Main.graphique.setInfoLab("Entrez l'année de début (yyyy): ");
			//System.out.println("Entrez l'année de début (yyyy): ");
			//int annee = sc.nextInt();
			int annee = Main.graphique.setVarInfoInt();
			//sc.nextLine();
			//System.out.println("Entrez le mois de début (mm): ");
			Main.graphique.setInfoLab("Entrez le mois de début (mm): ");
			//int mois = sc.nextInt();
			int mois = Main.graphique.setVarInfoInt();
			//sc.nextLine();
			//System.out.println("Entrez le jour de début (dd): ");
			Main.graphique.setInfoLab("Entrez le jour de début (dd): ");
			//int jour = sc.nextInt();
			int jour = Main.graphique.setVarInfoInt();
			//sc.nextLine();

			Calendar DebutS = new java.util.GregorianCalendar(annee, mois - 1,
					jour);

			//System.out.println("Entrez l'heure de début: ");
			Main.graphique.setInfoLab("Entrez l'heure de début: ");
			//int heure = sc.nextInt();
			int heure = Main.graphique.setVarInfoInt();
			//sc.nextLine();
			//System.out.println("Entrez la durée de la séance en heure: ");
			Main.graphique.setInfoLab("Entrez la durée de la séance en heure: ");
			//int duree = sc.nextInt();
			int duree = Main.graphique.setVarInfoInt();
			//sc.nextLine();

			DebutS.add(Calendar.HOUR_OF_DAY, heure);
			Calendar FinS = (Calendar) DebutS.clone();
			FinS.add(Calendar.HOUR_OF_DAY, duree);

			// /!\ les comparaison sont strictes, on change donc le nombre de
			// milliseconde pour changer ce comportement
			System.out.println(DebutS.getTime());
			System.out.println(FinS.getTime());
			if (DebutG.after(new Date(DebutS.getTime().getTime() + 1))
					|| FinG.before(new Date(FinS.getTime().getTime() - 1))) {
				c.rollback(sp);
				throw new SQLException(
						"L'horaire de la séance est incompatible avec l'horaire du groupe");
			}

			int CodeSeance;
			t = 0;
			// On choisit le numéro de séance
			do {
				stmt.close();
				rset.close();
				stmt = c.prepareStatement("SELECT s.NumSeance FROM Groupe g, Seance s WHERE g.CodeGroupe = ? "
						+ "and s.NumSeance = ? and s.CodeGroupe = g.CodeGroupe");
				if (t == 0) {
					//System.out.println("Entrez le numéro de la seance");
					Main.graphique.setInfoLab("Entrez le numéro de la seance");
				} else {
					//System.out.println("Numéro déjà attribué, réessayez");
					Main.graphique.setInfoLab("Numéro déjà attribué, réessayez");
				}
				t++;
				//CodeSeance = sc.nextInt();
				CodeSeance = Main.graphique.setVarInfoInt();
				//sc.nextLine();
				stmt.setInt(1, CodeGroupe);
				stmt.setInt(2, CodeSeance);
				rset = stmt.executeQuery();
				// Tant que le numéro de groupe est invalide
			} while (rset.next());

			// on ajoute la seance, on verifiera ensuite si cela crée un
			// problème avec le materiel
			stmt.close();
			stmt = c.prepareStatement("INSERT INTO Seance"
					+ " Values (?, ?, ?, ?, ?)");
			stmt.setInt(1, CodeGroupe);
			stmt.setInt(2, CodeSeance);
			// on remet la date dans DebutS, et non plus la date avec l'heure de
			// debut
			DebutS.add(Calendar.HOUR_OF_DAY, -heure);
			stmt.setDate(3, new Date(DebutS.getTime().getTime()));
			stmt.setInt(4, heure);
			stmt.setInt(5, duree);
			rset = stmt.executeQuery();

			// on recupere le code du centre
			stmt = c.prepareStatement("SELECT g.CodeCentre FROM Groupe g WHERE g.CodeGroupe = ?");
			stmt.setInt(1, CodeGroupe);
			rset = stmt.executeQuery();
			rset.next();
			int CodeCentre = rset.getInt(1);

			// On recupere le type de materiel requis
			stmt = c.prepareStatement("SELECT distinct N.Type "
					+ "FROM Necessite N, Groupe G "
					+ "WHERE G.CodeGroupe = ? "
					+ "and N.CodeAct = G.CodeAct");
			stmt.setInt(1, CodeGroupe);
			rset = stmt.executeQuery();

			if (rset.next()) {
				//L'activité demande du materiel, on doit vérifier

				//on recupere le type de materiel necessaire
				String type = rset.getString(1);
				
				//On affiche le materiel disponible
				System.out.println("Materiel compatible disponible:");
				stmt = c.prepareStatement("SELECT M.* "+
				"FROM Materiel M, Groupe g "+
				"where M.CodeCentre = g.CodeCentre and g.CodeGroupe = ? and M.Type = ?");
				stmt.setInt(1, CodeGroupe);
				stmt.setString(2, type);
				rset = stmt.executeQuery();
				Main.dumpResultSet(rset);

				t=0;
				while (t != -1) {
					//System.out.println("");
					//System.out.println("Entrez le code du matériel que vous souhaitez (-1 pour quitter):");
					Main.graphique.setInfoLab("Entrez le code du matériel que vous souhaitez (-1 pour terminer):");
					//int code = sc.nextInt();
					int code = Main.graphique.setVarInfoInt();
					t=code;
					//sc.nextLine();
					if(t!=-1) {
						//System.out.println("Entrez la quantitée de matériel que vous souhaitez");
						Main.graphique.setInfoLab("Entrez la quantitée de matériel que vous souhaitez");
						//int quant = sc.nextInt();
						int quant = Main.graphique.setVarInfoInt();
						stmt = c.prepareStatement("INSERT INTO Utilise "
							+ "Values (?, ?, ?, ?, ?)");
						stmt.setInt(1, CodeGroupe);
						stmt.setInt(2, CodeSeance);
						stmt.setInt(3, CodeCentre);
						stmt.setInt(4, code);
						stmt.setInt(5, quant);
						rset = stmt.executeQuery();
						
						//On cherche la quantité max de ce materiel qu'on utilisera desormais pendant les seance
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
						"   		    (S.DateSeance+S.HeureDebutSeance/24) < (TO_DATE(?,'DD/MM/YYYY:HH24:MI:SS')) and "+
						"   		    (S.DateSeance+S.HeureDebutSeance/24) >= (TO_DATE(?,'DD/MM/YYYY:HH24:MI:SS')) "+
						"	       ) Dates "+
						"	WHERE U.NumMateriel = ? and U.CodeCentre = g.CodeCentre and g.CodeCentre = ? "+
						"	and U.CodeGroupe = G.CodeGroupe and U.NumSeance = S.NumSeance "+
						"	and (S.DateSeance+S.HeureDebutSeance/24) IN Dates.MomentTest "+
						"	GROUP BY Dates.MomentTest) Q");
//						stmt.setInt(1, heure);
						stmt.setInt(1, code);
						stmt.setInt(2, CodeCentre);
						
						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy:hh:mm:ss");
						
						java.util.Date dateDate = FinS.getTime();
						String retour = dateFormat.format(dateDate);
						
						//On met dans DebutS la Date avec l'heure de debut
						DebutS.add(Calendar.HOUR_OF_DAY, heure);
						dateDate = DebutS.getTime();
						String retour2 = dateFormat.format(dateDate);

						stmt.setString(3, retour);
						stmt.setString(4, retour2);
						stmt.setInt(5, code);
						stmt.setInt(6, CodeCentre);						
						rset = stmt.executeQuery();
						rset.next();
						int maxUtil = rset.getInt(1);
						
						//On cherche maintenant la quantité dont dispose le centre
						stmt = c.prepareStatement("SELECT M.QuantiteMateriel "+
								"FROM Materiel M, Groupe G "+
								"WHERE M.CodeCentre = G.CodeCentre and G.CodeCentre = ? and M.NumMateriel = ?");
						stmt.setInt(1,CodeCentre);
						stmt.setInt(2, code);
						rset = stmt.executeQuery();
						rset.next();
						int maxDispo = rset.getInt(1);

						if (maxUtil > maxDispo) {
							c.rollback(sp);
							throw new SQLException("Ce matériel n'est pas disponible en quantité suffisante");
						}
						
					}
				}
			} else {
				// L'activité ne demande pas de matériel
				//System.out.println("Cette séance ne demande aucun matériel, enregistrement terminé");
				Main.graphique.setInfoLab("Cette séance ne demande aucun matériel, enregistrement terminé");
			}

			stmt.close();
			rset.close();
			c.commit();
			Main.graphique.mainMenu();

		} catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
			Main.graphique.mainMenu();
		}
	}
}
