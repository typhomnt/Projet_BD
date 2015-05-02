import java.sql.*;
import java.util.Scanner;

public class GestionGroupe {
	public static void creation(Connection c) throws SQLException {
		//On fait un SavePoint en cas d'exception
		Savepoint sp = c.setSavepoint();
		System.out.println("Enregistrement d'un stagiaire à une activité");
		PreparedStatement stmt;
		ResultSet rset;
		Scanner sc = new Scanner(System.in);
		Integer codeGroupe;
		Integer codeAct;
		Integer codeCentre;
		String dateDebGroupe;
		String dateFinGroupe;
		Integer nbMinStGpr;
		Integer nbMaxStGpr;
		String nomNiveau;
		try {
			
			System.out.println("Creation d'un groupe");
			stmt = c.prepareStatement("INSERT INTO Groupe VALUES (?,?,?,to_date(?,'yyyy-mm-dd'),to_date(?,'yyyy-mm-dd'),?,?,?)");
			System.out.println("Entrez le numero du groupe");
			codeGroupe = sc.nextInt();
			sc.nextLine();
			System.out.println("Entrez le numero de l'activité");
			codeAct = sc.nextInt();
			sc.nextLine();
			System.out.println("Entrez le numero du centre");
			codeCentre = sc.nextInt();
			sc.nextLine();
			System.out.println("Entrez la date du début du groupe");
			dateDebGroupe = sc.nextLine();
			System.out.println("Entrez le numero de fin de groupe");
			dateFinGroupe = sc.nextLine();
			System.out.println("Entrez le nombre minimal de stagiaire dans le groupe");
			nbMinStGpr = sc.nextInt();
			sc.nextLine();
			System.out.println("Entrez le nombre maximal de stagiaire dans le groupe");
			nbMaxStGpr = sc.nextInt();
			sc.nextLine();
			System.out.println("Entrez le niveau du groupe");
			nomNiveau = sc.nextLine();
			stmt.setInt(1,codeGroupe);
			stmt.setInt(2,codeAct);
			stmt.setInt(3,codeCentre);
			stmt.setString(4,dateDebGroupe);
			stmt.setString(5,dateFinGroupe);
			stmt.setInt(6,nbMinStGpr);
			stmt.setInt(7,nbMaxStGpr);
			stmt.setString(8,nomNiveau);
			if(Date.valueOf(dateDebGroupe).after(Date.valueOf(dateFinGroupe))){
				sc.close();
				throw new SQLException("Date deb groupe plus grand que date fin groupe");
			}
			if(nbMaxStGpr < nbMinStGpr || nbMinStGpr <= 0){
				sc.close();
				throw new SQLException("Nombre max de stagiaire < Nombre min de stagiaire ou Nombre min <= 0");
			}
			try{
				rset = stmt.executeQuery();
				rset.close();
			}
			catch (SQLIntegrityConstraintViolationException e){
				System.out.println("Groupe déjà existant");
			}
			stmt.close();
			sc.close();
			c.commit();
		} 
		catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
		}
	}
	
	public static void suppression(Connection c) throws SQLException {
		//On fait un SavePoint en cas d'exception
		Savepoint sp = c.setSavepoint();
		System.out.println("Enregistrement d'un stagiaire à une activité");
		PreparedStatement stmt;
		ResultSet rset;
		Scanner sc = new Scanner(System.in);
		Integer codeGroupe;
		try{
			System.out.println("Suppression d'un groupe");
			System.out.println("Entrez le numero du groupe à supprimer");
			codeGroupe = sc.nextInt();
			sc.nextLine();
			stmt = c.prepareStatement("DELETE FROM EstEncadreePar e WHERE e.CodeGroupe = ?");
			stmt.setInt(1, codeGroupe);
			rset = stmt.executeQuery();
			stmt.close();
			rset.close();
			stmt = c.prepareStatement("DELETE FROM Seance s WHERE s.CodeGroupe = ?");
			stmt.setInt(1, codeGroupe);
			rset = stmt.executeQuery();
			stmt.close();
			rset.close();
			stmt = c.prepareStatement("DELETE FROM utilise u WHERE u.CodeGroupe = ?");
			stmt.setInt(1, codeGroupe);
			rset = stmt.executeQuery();
			stmt.close();
			rset.close();
			stmt = c.prepareStatement("DELETE FROM EstDansGroupe e WHERE e.CodeGroupe = ?");
			stmt.setInt(1, codeGroupe);
			rset = stmt.executeQuery();
			stmt.close();
			rset.close();
			stmt = c.prepareStatement("DELETE FROM Groupe g WHERE g.CodeGroupe = ?");
			stmt.setInt(1, codeGroupe);
			rset = stmt.executeQuery();
			stmt.close();
			rset.close();
			sc.close();
			c.commit();
		} 
		catch (SQLException e) {
			c.rollback(sp);
			e.printStackTrace();
		}	
		
	}
}
