import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class PlanificationSeance {
	public static void register(Connection c) throws SQLException {

		// Ici on va utiliser un scanner pour récupérer les differentes
		// informations
		Scanner sc = new Scanner(System.in);
		Savepoint sp = c.setSavepoint();
		System.out.println("Enregistrement d'une seance");
		PreparedStatement stmt;
		ResultSet rset;
		int CodeGroupe;
		try {
			int t = 0;
			// On selectionne un groupe
			do {
				stmt = c.prepareStatement("SELECT g.CodeGroupe FROM Groupe g WHERE g.CodeGroupe = ?");
				if (t == 0) {
					System.out.println("Entrez le numéro du groupe");
				} else {
					System.out.println("Numéro invalide, réessayez");
				}
				t++;
				CodeGroupe = sc.nextInt();
				sc.nextLine();
				stmt.setInt(1, CodeGroupe);
				rset = stmt.executeQuery();
				// Tant que le numéro de groupe est invalide
			} while (!rset.next());
			stmt = c.prepareStatement("SELECT Count(distinct edg.CodePersonne) as Participants"
					+ "FROM EstDansGroupe edg" + "WHERE edg.CodeGroupe = ?");
			stmt.setInt(1, CodeGroupe);
			rset = stmt.executeQuery();
			rset.next();
			int participants = rset.getInt(1);

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

			stmt = c.prepareStatement("SELECT g.DateDebutGroupe g.DateFinGroupe FROM Groupe g WHERE g.CodeGroupe = ?");
			stmt.setInt(1, CodeGroupe);
			rset = stmt.executeQuery();
			rset.next();
			Date DebutG = rset.getDate(1);
			Date FinG = rset.getDate(1);

			System.out.println("Entrez l'année de début (yyyy): ");
			int annee = sc.nextInt();
			sc.nextLine();
			System.out.println("Entrez le mois de début (mm): ");
			int mois = sc.nextInt();
			sc.nextLine();
			System.out.println("Entrez le jour de début (dd): ");
			int jour = sc.nextInt();
			sc.nextLine();

			Calendar DebutS = new java.util.GregorianCalendar(annee, mois - 1,
					jour);

			System.out.println("Entrez l'heure de début: ");
			int heure = sc.nextInt();
			sc.nextLine();
			System.out.println("Entrez la durée de la séance en heure: ");
			int duree = sc.nextInt();
			sc.nextLine();

			DebutS.add(Calendar.HOUR_OF_DAY, heure);
			Calendar FinS = (Calendar) DebutS.clone();
			FinS.add(Calendar.HOUR_OF_DAY, duree);

			// /!\ les comparaison sont strictes, on change donc le nombre de
			// milliseconde pour changer ce comportement
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
				stmt = c.prepareStatement("SELECT s.NumSeance FROM Groupe g Seance s WHERE g.CodeGroupe = ?"
						+ "and s.NumSeance = ? and s.CodeGroupe = g.CodeGroupe");
				if (t == 0) {
					System.out.println("Entrez le numéro de la seance");
				} else {
					System.out.println("Numéro déjà attribué, réessayez");
				}
				t++;
				CodeSeance = sc.nextInt();
				sc.nextLine();
				stmt.setInt(1, CodeGroupe);
				stmt.setInt(2, CodeSeance);
				rset = stmt.executeQuery();
				// Tant que le numéro de groupe est invalide
			} while (!rset.next());

			// on ajoute la seance, on verifiera ensuite si cela crée un
			// problème avec le materiel
			stmt = c.prepareStatement("INSERT INTO Seance"
					+ "Values (?, ?, ?, ?, ?)");
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
			stmt = c.prepareStatement("SELECT distinct N.Type"
					+ "FROM Necessite N, Seance S, Groupe G"
					+ "WHERE S.NumSeance = NumSeance and S.CodeGroupe = CodeGroupe"
					+ "and G.CodeGroupe = S.CodeGroupe and G.CodeAct = S.CodeAct and N.CodeCentre = G.CodeCentre;");
			stmt.setInt(1, CodeSeance);
			stmt.setInt(2, CodeGroupe);
			rset = stmt.executeQuery();

			if (rset.next()) {
				//L'activité demande du materiel, on doit vérifier

				//on recupere le type de materiel necessaire
				String type = rset.getString(1);
				
				//On affiche le materiel disponible
				System.out.println("Materiel compatible disponible:");
				stmt = c.prepareStatement("SELECT *"+
				"FROM Materiel M, Groupe g"+
				"where M.CodeCentre = g.CodeCentre and g.CodeGroupe = ? and M.Type = ?");
				stmt.setInt(1, CodeGroupe);
				stmt.setString(2, type);
				rset = stmt.executeQuery();
				Main.dumpResultSet(rset);

				t=0;
				while (t != -1) {
					System.out.println("Entrez le code du matériel que vous souhaitez (-1 pour quitter):");
					int code = sc.nextInt();
					t=code;
					sc.nextLine();
					if(t!=-1) {
						System.out.println("Entrez la quantitée de matériel que vous souhaitez");
						int quant = sc.nextInt();
						stmt = c.prepareStatement("INSERT INTO Utilise"
							+ "Values (CodeGroupe, NumSeance, CodeCentre, NumMateriel, Quantitée)");
						stmt.setInt(1, CodeGroupe);
						stmt.setInt(2, CodeSeance);
						stmt.setInt(3, CodeCentre);
						stmt.setInt(4, code);
						stmt.setInt(5, quant);
						rset = stmt.executeQuery();
						
						//On cherche la quantité max de ce materiel qu'on utilisera desormais pendant les seance
						stmt = c.prepareStatement("SELECT Max(Quantite)"+
						"FROM"+
						"("+
						//	--on fait la somme de materiel utilisé a chaque commencement de seance
						"	SELECT Dates.MomentTest, Sum(U.QuantiteNecessaire) as Quantite"+
						"	FROM Utilise U, Seance S, Groupe G, "+
						//	--on cherche a obtenir chaque nouveau commencement de seance entre le debut et la fin de notre seance
						"	(SELECT (S.DateSeance + ?) as MomentTest"+
						"	    FROM Utilise U, Seance S, Groupe G"+
						"	        WHERE U.NumMateriel = ? and U.CodeCentre = g.CodeCentre and"+
						"			g.CodeCentre = ? and"+
						"		    U.CodeGroupe = G.CodeGroupe and U.NumSeance = S.NumSeance and"+
						"   		    S.DateSeance + TO_DATE(TO_CHAR(S.HeureDebutSeance,'99'),'HH24') < TO_DATE(?,'DD/MM/YYYY:HH24:MI:SS') and"+
						"   		    S.DateSeance + TO_DATE(TO_CHAR(S.HeureDebutSeance,'99'),'HH24') >= TO_DATE(?,'DD/MM/YYYY:HH24:MI:SS')"+
						"	       ) as Dates"+
						"	WHERE U.NumMateriel = ? and U.CodeCentre = g.CodeCentre and g.CodeCentre = ?"+
						"	and U.CodeGroupe = G.CodeGroupe and U.NumSeance = S.NumSeance"+
						"	and S.DateSeance+S.HeureDebutSeance IN Dates.MomentTest"+
						"	GROUP BY Dates.MomentTest)");
						stmt.setInt(1, heure);
						stmt.setInt(2, code);
						stmt.setInt(3, CodeCentre);
						
						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy:hh:mm:ss");
						
						java.util.Date dateDate = FinS.getTime();
						String retour = dateFormat.format(dateDate);
						
						//On met dans DebutS la Date avec l'heure de debut
						DebutS.add(Calendar.HOUR_OF_DAY, heure);
						dateDate = DebutS.getTime();
						String retour2 = dateFormat.format(dateDate);

						stmt.setString(4, retour);
						stmt.setString(5, retour2);
						stmt.setInt(6, code);
						stmt.setInt(7, CodeCentre);						
						rset = stmt.executeQuery();
						rset.next();
						int maxUtil = rset.getInt(1);
						
						//On cherche maintenant la quantité dont dispose le centre
						stmt = c.prepareStatement("SELECT M.QuantiteMateriel"+
								"FROM Materiel M, Groupe G"+
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
				System.out.println("Cette séance ne demande aucun matériel, enregistrement terminé");
			}

			stmt.close();
			rset.close();
			c.commit();

		} catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
		}
	}
}
