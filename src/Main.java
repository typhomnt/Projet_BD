import java.sql.*;
import java.util.Scanner;

public class Main {
	
	static String id = "ghorreso";
	static String pwd = "ghorreso";
	static String url = "jdbc:oracle:thin:@ensioracle1.imag.fr:1521:ensioracle1";
	static Connection cnct;

	public static void main(String args[]) {
		
		try {
			// Enregistrement du driver Oracle
			System.out.print("Loading Oracle driver... "); 
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			System.out.println("loaded");
			// Etablissement de la connexion
			System.out.print("Connecting to the database... ");
			cnct = DriverManager.getConnection(url, id, pwd);
			cnct.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int i = 9;
		Scanner lecteur = new Scanner(System.in);
		try {
			if (cnct.isValid(1)) 
				i = 1;
		} catch(SQLException e){
			i = 9;
		}
		while (i != 9) {
			System.out.println("Que souhaitez-vous faire ? : \n" +
					"1) Enregistrement d'un stagiaire à un centre et à une activité\n" + // RegisterQuery.java
					"2) Création d'un groupe\n" +
					"3) Suppression d'un groupe\n" +
					"4) Planification d'une séance pour un groupe\n" +
					"5) Visualisation des séances planifiées\n" +
					"6) Gestion du matériel (inventaire, ajout, suppression)\n" +
					"7) Pour chaque activité, classement des centres en fonction du nombre d'inscrits\n" +
					"8) Classement des villes par nombre de stagiaires inscrits\n" +
					"9) Sortir ?\n");
			i = lecteur.nextInt();
			
			try {
			if(i==1){ // Enregistrement d'un stagiaire
				RegisterQuery.register(cnct);
			}
			if(i==2){ // Creation d'un groupe
			    // TODO
			}
			if(i==3){ // Suppression d'un groupe
				// TODO
			}
			if(i==4){ // Planification d'une seance pour un groupe
				// TODO
			}
			if(i==5){ // Visualisation des seances planifiees
				// TODO
			}
			if(i==6){ // Gestion du materiel
				// TODO
			}
			if(i==7){ // Pour chaque activite, classement des centres
				ClassementCentres.classement(cnct);
			}
			if(i==8){ // Classement des villes
				ClassementVilles.classement(cnct);
			}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} // fin du while
		
		lecteur.close();

		try {
			cnct.close();
			System.err.println("Connexion fermee");
		} catch (SQLException e) {
			System.err.println("Impossible de fermer la connexion");
			e.printStackTrace();
		}
	}
	
	public static void dumpResultSet(ResultSet rset) throws SQLException {
		ResultSetMetaData rsetmd = rset.getMetaData();
		int i = rsetmd.getColumnCount();
		for (int k=1;k<=i;k++)
			System.out.print(rsetmd.getColumnName(k) + "\t");
		System.out.println();
		while (rset.next()) {
			for (int j = 1; j <= i; j++) {
				System.out.print(rset.getString(j) + "\t");
			}
			System.out.println();
		}
	}


}
