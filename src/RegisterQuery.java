import java.sql.*;
import java.util.Scanner;

public class RegisterQuery {
	
	public static void register(Connection c) throws SQLException {
		
		/* DIFFERENCES AVEC LES REQUETES DEJA ECRITES :
		 *
		 * utiliser des PreparedStatement, car l'utilisateur devra entrer des données
		 * cf. ReqParam.java ou SimpleTrans.java sur la page du projet bd sur le kiosk,
		 * ou cf. les sources de Kévin P. */
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
		
		Savepoint sp = c.setSavepoint();
		System.out.println("Enregistrement d'un stagiaire dans un centre");
		PreparedStatement stmt;
		ResultSet rset;
		try {
			stmt = c.prepareStatement("SELECT s.CodePersonne FROM Stagiaire s WHERE s.CodePersonne = ?");
			System.out.println("Entrez un numéro de stagiaire");
			Integer codeStag = sc.nextInt();
			stmt.setInt(1, codeStag);
			rset = stmt.executeQuery();
			Main.dumpResultSet(rset);
			if(!rset.next()){
				stmt = c.prepareStatement("SELECT p.CodePersonne FROM Personne p WHERE p.CodePersonne = ?");
				stmt.setInt(1, codeStag);
				System.out.println(codeStag);
				rset.close();
				rset = stmt.executeQuery();
				if(rset.next()){
					throw new SQLException("Le statgiaire ne peut etre ni responsable ni moniteur");
				}
				else{
					System.out.println("Insertion");
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
					stmt = c.prepareStatement("INSERT INTO Adresse_Postale VALUES (?,?,?,?)");
					stmt.setInt(1, numAdr);
					stmt.setString(2, rueAdr);
					stmt.setInt(3, codeAdr);
					stmt.setString(4, villeAdr);
					rset.close();
					rset = stmt.executeQuery();
					stmt.close();
					System.out.println("Insertion dans addresse reussie");
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
					stmt = c.prepareStatement("INSERT INTO Stagiaire VALUES (?)");
					stmt.setInt(1, codeStag);
					rset.close();
					rset = stmt.executeQuery();
					System.out.println("Insertion dans stagiaire reussie");
				}
				//Main.dumpResultSet(rset);
				
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

