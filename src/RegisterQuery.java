import java.sql.*;
import java.util.Scanner;

public class RegisterQuery {
	
	public static void register(Connection c) throws SQLException {
		
		//Ici on va utiliser un scanner pour récupérer les differentes informations
		Scanner sc = new Scanner(System.in);
		String nom;
		String prenom;
		String dateNaiss;
		Integer tel;
		String mail;
		Integer numAdr;
		String rueAdr;
		Integer codeAdr;
		String villeAdr;
		String dateDeb;
		String dateFin;
		//On fait un SavePoint en cas d'exception
		Savepoint sp = c.setSavepoint();
		System.out.println("Enregistrement d'un stagiaire dans un centre");
		PreparedStatement stmt;
		ResultSet rset;
		try {
			// Ici on regarde si le stagiaire entré existe déjà dans la table stagiaire ou non
			stmt = c.prepareStatement("SELECT s.CodePersonne FROM Stagiaire s WHERE s.CodePersonne = ?");
			System.out.println("Entrez un numéro de stagiaire");
			Integer codeStag = sc.nextInt();
			stmt.setInt(1, codeStag);
			rset = stmt.executeQuery();
			Main.dumpResultSet(rset);
			//s'il n'existe pas dans la table stagiaire
			if(!rset.next()){
				//On regarde si le code existe dans Personne au quel cas on lève une exception
				stmt = c.prepareStatement("SELECT p.CodePersonne FROM Personne p WHERE p.CodePersonne = ?");
				stmt.setInt(1, codeStag);
				System.out.println(codeStag);
				rset.close();
				rset = stmt.executeQuery();
				if(rset.next()){
					throw new SQLException("Le statgiaire ne peut etre ni responsable ni moniteur");
				}
				else{
					// On va ajouter le stagiaire à la base et on récupère toutes les infos
					sc.nextLine();
					System.out.println("Entrez le nom du Stagiaire :");
					nom = sc.nextLine();
					System.out.println("Entrez le prenom du Stagiaire :");
					prenom = sc.nextLine();
					System.out.println("Entrez la date de naissance du Stagiaire au format yyyy-mm-dd :");
					dateNaiss = sc.nextLine();
					System.out.println("Entrez le numero de telephone du Staigiare");
					tel = sc.nextInt();
					sc.nextLine();
					System.out.println("Entrez l'e-mail du Stagiaire :");
					mail = sc.nextLine();
					System.out.println("Entrez le numero de rue du Stagiare :");
					numAdr = sc.nextInt();
					sc.nextLine();
					System.out.println("Entrez le nom de rue du Stagiaire : ");
					rueAdr = sc.nextLine();
					System.out.println("Entrez l'arrondissement du Stagiaire :");
					codeAdr = sc.nextInt();
					sc.nextLine();
					System.out.println("Entrez la ville du Stagiaire :");
					villeAdr = sc.nextLine();
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
			stmt = c.prepareStatement("SELECT c.CodeCentre FROM Centre c WHERE c.CodeCentre = ?");
			System.out.println("Entrez un numéro de centre");
			Integer codeCentr = sc.nextInt();
			stmt.setInt(1, codeCentr);
			rset = stmt.executeQuery();
			Main.dumpResultSet(rset);
			if(!rset.next()){
				
			}
			System.out.println("Entrez une date de debut d'inscription au format yyyy-mm-dd:");
			sc.nextLine();
			dateDeb = sc.nextLine();
			System.out.println("Entrez une date de fin d'inscription au format yyyy-mm-dd:");
			dateFin = sc.nextLine();
			//On verifie que datefin > datedeb)
			Date deb = Date.valueOf(dateDeb);
			Date fin = Date.valueOf(dateFin);
			if(deb.after(fin))
				throw new SQLException("la date de début est plus grande que la date de fin");
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
			if(rset.next())
				throw new SQLException("Le stagiaire est déjà inscrit dans un centre à cette période ci");
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
		} catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
		}
		
	}
	
}

