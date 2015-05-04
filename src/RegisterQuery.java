import java.awt.font.GraphicAttribute;
import java.sql.*;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class RegisterQuery {
	
	public static void register(Connection c) throws SQLException {
		
		//Ici on va utiliser un scanner pour récupérer les differentes informations
		Scanner sc = new Scanner(System.in);
		String nom = new String();
		String prenom = new String();
		String dateNaiss = new String();
		Integer tel = 0;
		String mail = new String();
		Integer numAdr = 0;
		String rueAdr = new String();
		Integer codeAdr = 0;
		String villeAdr = new String();
		String dateDeb = new String();
		String dateFin = new String();
		Integer codeStag = 0;
		Integer codeCentr = 0;
		//On fait un SavePoint en cas d'exception
		Savepoint sp = c.setSavepoint();
		System.out.println("Enregistrement d'un stagiaire dans un centre");
		System.out.println("Voici la liste des stagiaires déjà existants");
		AffichageTable.affichageStagiaire(c);
		PreparedStatement stmt;
		ResultSet rset;
		try {
			// Ici on regarde si le stagiaire entré existe déjà dans la table stagiaire ou non
			stmt = c.prepareStatement("SELECT s.CodePersonne FROM Stagiaire s WHERE s.CodePersonne = ?");
			System.out.println("Entrez un numéro de stagiaire");
			Main.graphique.setInfoLab("Entrez un numéro de stagiaire");
			//codeStag = sc.nextInt();
			codeStag = Main.graphique.setVarInfoInt();
			//System.out.println(codeStag);
			stmt.setInt(1, codeStag);
			rset = stmt.executeQuery();
			//s'il n'existe pas dans la table stagiaire
			if(!rset.next()){
				//On regarde si le code existe dans Personne au quel cas on lève une exception
				stmt = c.prepareStatement("SELECT p.CodePersonne FROM Personne p WHERE p.CodePersonne = ?");
				stmt.setInt(1, codeStag);
				rset.close();
				rset = stmt.executeQuery();
				if(rset.next()){
					sc.close();
					throw new SQLException("Le statgiaire ne peut etre ni responsable ni moniteur");
				}
				else{
					// On va ajouter le stagiaire à la base et on récupère toutes les infos
					//sc.nextLine();
					System.out.println("Entrez le nom du Stagiaire :");
					Main.graphique.setInfoLab("Entrez le nom du Stagiaire");
					//nom = sc.nextLine();
					nom = Main.graphique.setVarInfoStr();
					System.out.println("Entrez le prenom du Stagiaire :");
					Main.graphique.setInfoLab("Entrez le prenom du Stagiaire");
					//prenom = sc.nextLine();
					prenom = Main.graphique.setVarInfoStr();
					System.out.println("Entrez la date de naissance du Stagiaire au format yyyy-mm-dd :");
					Main.graphique.setInfoLab("Entrez la date de naissance du Stagiaire au format yyyy-mm-dd :");
					//dateNaiss = sc.nextLine();
					dateNaiss = Main.graphique.setVarInfoStr();
					System.out.println("Entrez le numero de telephone du Stagiaire");
					Main.graphique.setInfoLab("Entrez le numero de telephone du Stagiaire");
					//tel = sc.nextInt();
					//sc.nextLine();
					tel = Main.graphique.setVarInfoInt();
					System.out.println("Entrez l'e-mail du Stagiaire :");
					Main.graphique.setInfoLab("Entrez l'e-mail du Stagiaire :");
					//mail = sc.nextLine();
					mail = Main.graphique.setVarInfoStr();
					System.out.println("Entrez le numero de rue du Stagiare :");
					Main.graphique.setInfoLab("Entrez le numero de rue du Stagiare :");
					//numAdr = sc.nextInt();
					//sc.nextLine();
					numAdr = Main.graphique.setVarInfoInt();
					System.out.println("Entrez le nom de rue du Stagiaire : ");
					Main.graphique.setInfoLab("Entrez le nom de rue du Stagiaire : ");
					//rueAdr = sc.nextLine();
					rueAdr = Main.graphique.setVarInfoStr();
					System.out.println("Entrez l'arrondissement du Stagiaire :");
					Main.graphique.setInfoLab("Entrez l'arrondissement du Stagiaire :");
					//codeAdr = sc.nextInt();
					//sc.nextLine();
					codeAdr = Main.graphique.setVarInfoInt();
					System.out.println("Entrez la ville du Stagiaire :");
					Main.graphique.setInfoLab("Entrez la ville du Stagiaire :");
					//villeAdr = sc.nextLine();
					villeAdr = Main.graphique.setVarInfoStr();
					stmt.close();
					//On ajoute son adresse à la table Adresse Postale
					try{
						stmt = c.prepareStatement("INSERT INTO Adresse_Postale VALUES (?,?,?,?)");
						stmt.setInt(1, numAdr);
						stmt.setString(2, rueAdr);
						stmt.setInt(3, codeAdr);
						stmt.setString(4, villeAdr);
						rset.close();
						rset = stmt.executeQuery();
					} 
					catch (SQLIntegrityConstraintViolationException e){
						System.out.println("Adresse deja existante");
					}
					stmt.close();
					System.out.println("Insertion dans addresse reussie");
					//On l'ajoute dans Personne
					stmt = c.prepareStatement("INSERT INTO Personne VALUES (?,?,?,to_date(?,'yyyy-mm-dd'),?,?,?,?,?,?)");
					stmt.setInt(1, codeStag);
					stmt.setString(2, nom);
					stmt.setString(3, prenom);
					stmt.setString(4, dateNaiss);
					stmt.setInt(5, tel);
					stmt.setString(6, mail);
					stmt.setInt(7, numAdr);
					stmt.setString(8, rueAdr);
					stmt.setInt(9, codeAdr);
					stmt.setString(10, villeAdr);
					rset.close();
					rset = stmt.executeQuery();
					stmt.close();
					System.out.println("Insertion dans personne reussie");
					//Et enfin dans la table stagiaire
					stmt = c.prepareStatement("INSERT INTO Stagiaire VALUES (?)");
					stmt.setInt(1, codeStag);
					rset.close();
					rset = stmt.executeQuery();
					System.out.println("Insertion dans stagiaire reussie");
				}
			}
			//Insertion dans un centre 
			// Ici on regarde si le centre existe
			System.out.println("Voici la liste des centres disponibles");
			AffichageTable.affichageCentre(c);
			stmt = c.prepareStatement("SELECT c.CodeCentre FROM Centre c WHERE c.CodeCentre = ?");
			//System.out.println("Entrez un numéro de centre");
			Main.graphique.setInfoLab("Entrez un numéro de centre");
			codeCentr = Main.graphique.setVarInfoInt();
			stmt.setInt(1, codeCentr);
			rset = stmt.executeQuery();
			//On pourrait demander la création d'un nouveau centre
			if(!rset.next()){
				sc.close();
				throw new SQLException("Le centre entré n'existe pas");
			}
			System.out.println("Voici les differentes inscriptions du stagiaire");
			AffichageTable.affichageInscriptionDansCentre(c, codeStag);
			//System.out.println("Entrez une date de debut d'inscription au format yyyy-mm-dd:");
			//sc.nextLine();
			//dateDeb = sc.nextLine();
			Main.graphique.setInfoLab("Entrez une date de debut d'inscription au format yyyy-mm-dd:");
			dateDeb = Main.graphique.setVarInfoStr();
			//System.out.println("Entrez une date de fin d'inscription au format yyyy-mm-dd:");
			//dateFin = sc.nextLine();
			Main.graphique.setInfoLab("Entrez une date de fin d'inscription au format yyyy-mm-dd:");
			dateFin = Main.graphique.setVarInfoStr();
			//On verifie que datefin > datedeb)
			Date deb = Date.valueOf(dateDeb);
			Date fin = Date.valueOf(dateFin);
			if(deb.after(fin)){
				sc.close();
				throw new SQLException("la date de début est plus grande que la date de fin");
			}
			//On verifie que le stagiaire n'est pas déjà dans un centre à cette période ci
			stmt = c.prepareStatement("SELECT e.* FROM EstInscritDansCentre e WHERE e.CodePersonne = ? AND ((e.Datedeb < to_date(?,'yyyy-mm-dd') AND e.Datefin > to_date(?,'yyyy-mm-dd')) OR (e.Datedeb < to_date(?,'yyyy-mm-dd') AND e.Datefin > to_date(?,'yyyy-mm-dd')) OR (e.Datedeb > to_date(?,'yyyy-mm-dd') AND e.Datefin < to_date(?,'yyyy-mm-dd'))) ");
			stmt.setInt(1, codeStag);
			stmt.setString(2, dateDeb);
			stmt.setString(3, dateDeb);
			stmt.setString(4, dateFin);
			stmt.setString(5, dateFin);
			stmt.setString(6, dateDeb);
			stmt.setString(7, dateFin);
			rset.close();
			rset = stmt.executeQuery();
			if(rset.next()){
				sc.close();
				throw new SQLException("Le stagiaire est déjà inscrit dans un centre à cette période ci");
			}
			else{
				//On insere le stagiaire dans estinscritdanscentre
				stmt = c.prepareStatement("INSERT INTO EstInscritDansCentre Values (?,?,to_date(?,'yyyy-mm-dd'),to_date(?,'yyyy-mm-dd'))");
				stmt.setInt(1, codeStag);
				stmt.setInt(2, codeCentr);
				stmt.setString(3,dateDeb);
				stmt.setString(4,dateFin);
				rset.close();
				rset = stmt.executeQuery();
			}
			rset.close();
			stmt.close();
			c.commit();
			Main.graphique.mainMenu();
		} catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
		}
		
	}
	public static void activity(Connection c) throws SQLException {
		//On fait un SavePoint en cas d'exception
			Savepoint sp = c.setSavepoint();
			System.out.println("Enregistrement d'un stagiaire à une activité");
			PreparedStatement stmt;
			ResultSet rset;
			Scanner sc = new Scanner(System.in);
			Integer codeA = 0;
			String nomAct = new String();
			String catAct = new String();
			String descrAct = new String();
			Integer codeStag = 0;
			Integer codeGroupe = 0;
			Integer codeCentre = 0;
			Date dateDeb;
			Date dateFin;
			int reply;
			try {
				// Ici on regarde si le stagiaire entré existe déjà dans la table stagiaire ou non
				stmt = c.prepareStatement("SELECT s.CodePersonne FROM Stagiaire s WHERE s.CodePersonne = ?");
				System.out.println("Voici la liste des stagiaires");
				AffichageTable.affichageStagiaire(c);
				//System.out.println("Entrez un numéro de stagiaire");
				//codeStag = sc.nextInt();
				Main.graphique.setInfoLab("Entrez un numéro de stagiaire");
				codeStag = Main.graphique.setVarInfoInt();
				stmt.setInt(1, codeStag);
				rset = stmt.executeQuery();
				if(!rset.next()){
					//On regarde si le code existe dans Personne au quel cas on lève une exception
					stmt = c.prepareStatement("SELECT p.CodePersonne FROM Personne p WHERE p.CodePersonne = ?");
					stmt.setInt(1, codeStag);
					rset.close();
					rset = stmt.executeQuery();
					if(rset.next()){
						sc.close();
						throw new SQLException("Le numero entré est un numéro de moniteur ou de responsable");
					}
					sc.nextLine();
					//On demande si on veut rajouter le stagiaire dans la base
					//System.out.println("Le stagiaire n'est pas dans la base voulez vous le rejouter [o/n]?");
					//if(sc.nextLine().equals("o"))
					Main.graphique.setInfoLab("Le stagiaire n'est pas dans la base voulez vous le rejouter [o/n]?");
					reply = Main.graphique.yesNoQuestion();
					if(reply == JOptionPane.YES_OPTION)
						register(c);
				}
				stmt.close();
				//On verifie que l'activite existe
				System.out.println("Voici la liste des activités disponibles");
				AffichageTable.affichageActivite(c);
				//System.out.println("Entrez le numéro d'une activité");
				stmt = c.prepareStatement("SELECT a.CodeAct FROM Activite a WHERE a.CodeAct = ?");
				//sc.nextLine();
				//codeA = sc.nextInt();
				//sc.nextLine();
				Main.graphique.setInfoLab("Entrez le numéro d'une activité");
				codeA = Main.graphique.setVarInfoInt();
				stmt.setInt(1, codeA);
				rset.close();
				rset = stmt.executeQuery();
				if(!rset.next()){
					//On demande si on veut créer l'activité
					//System.out.println("l'activité n'existe pas voulez vous la créer [o/n] ?");
					//if(sc.nextLine().equals("o")){
					Main.graphique.setInfoLab("l'activité n'existe pas voulez vous la créer [o/n] ?");
					reply = Main.graphique.yesNoQuestion();
					if(reply == JOptionPane.YES_OPTION){
						//System.out.println("Entrez le nom de l'activité :");
						//nomAct = sc.nextLine();
						Main.graphique.setInfoLab("Entrez le nom de l'activité :");
						nomAct = Main.graphique.setVarInfoStr();
						//System.out.println("Entrez la categorie de l'activité :");
						//catAct = sc.nextLine();
						Main.graphique.setInfoLab("Entrez la catégorie de l'activité :");
						catAct = Main.graphique.setVarInfoStr();
						//System.out.println("Entrez la description de l'activité :");
						//descrAct = sc.nextLine();
						Main.graphique.setInfoLab("Entrez la description de l'activité :");
						descrAct = Main.graphique.setVarInfoStr();
						stmt.close();
						stmt = c.prepareStatement("INSERT INTO Activite VALUES(?,?,?,?)");
						stmt.setInt(1, codeA);
						stmt.setString(2, nomAct);
						stmt.setString(3, catAct);
						stmt.setString(4, descrAct);
						rset.close();
						rset = stmt.executeQuery();
					}
				}
				//Enregistrement dans un groupe 
				System.out.println("Voici la liste des groupes disponibles");
				AffichageTable.affichageGroupe(c);
				//System.out.println("Entrez le numero du groupe auquel vous voulez inscrire le stagiaire :");
				//codeGroupe = sc.nextInt();
				Main.graphique.setInfoLab("Entrez le numero du groupe auquel vous voulez inscrire le stagiaire :");
				codeGroupe = Main.graphique.setVarInfoInt();
				stmt = c.prepareStatement("SELECT g.codeCentre,g.codeGroupe,g.DateDebutGroupe,g.DateFinGroupe FROM Groupe g WHERE g.CodeGroupe = ? AND g.CodeAct = ?");
				stmt.setInt(1, codeGroupe);
				stmt.setInt(2, codeA);
				rset.close();
				rset = stmt.executeQuery();
				if(!rset.next()){
					sc.close();
					throw new SQLException("Le groupe ne pratique pas l'activité désirée");
				}
				else{
					codeCentre = rset.getInt("codeCentre");
					dateDeb = rset.getDate("DateDebutGroupe");
					dateFin = rset.getDate("DateFinGroupe");
					//On verifie que le groupe fait bien parti du centre ou le stagiaire est inscrit et que les périodes d'inscription correspondent
					stmt = c.prepareStatement("SELECT c.* FROM EstInscritDansCentre c WHERE c.codeCentre = ? AND c.codePersonne = ? AND c.Datedeb <= ? AND c.Datefin >= ?");
					stmt.setInt(1, codeCentre);
					stmt.setInt(2, codeStag);
					stmt.setDate(3, dateDeb);
					stmt.setDate(4, dateFin);
					rset.close();
					rset = stmt.executeQuery();
					if(!rset.next()){
						sc.close();
						throw new SQLException("Le stagiaire n'est pas inscrit dans le centre du groupe durant la période du groupe");
					}
				}
				
				stmt = c.prepareStatement("INSERT INTO EstDansGroupe VALUES(?,?)");
				stmt.setInt(1, codeStag);
				stmt.setInt(2, codeGroupe);
				rset = stmt.executeQuery();
				rset.close();
				stmt.close();
				c.commit();
				Main.graphique.mainMenu();
			} 
			catch (SQLException e) {
				c.rollback(sp);
				e.printStackTrace();
			}
	}
	
}

