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
					stmt.close();
					stmt = c.prepareStatement("INSERT INTO Personne VALUES (?)");
					stmt.setInt(1, codeStag);
					rset.close();
					rset = stmt.executeQuery();
					stmt.close();
					stmt = c.prepareStatement("INSERT INTO Stagiaire VALUES (?)");
					stmt.setInt(1, codeStag);
					rset.close();
					rset = stmt.executeQuery();
				}
				Main.dumpResultSet(rset);
				
			}
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
		}
		
	}
	
}

