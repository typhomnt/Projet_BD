import java.sql.*;
import java.util.Scanner;

public class Main {
	
	static String id = "garcia1";
	static String pwd = "garcia1";
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
		
		int i = 11;
		Scanner lecteur = new Scanner(System.in);
		try {
			if (cnct.isValid(1)) 
				i = 1;
		} catch(SQLException e){
			i = 11;
		}
		while (i != 11) {
			System.out.println("Que souhaitez-vous faire ? : \n" +
					"1) Enregistrement d'un stagiaire à un centre et à une activité\n" + // RegisterQuery.java
					"2) Création d'un groupe\n" +
					"3) Suppression d'un groupe\n" +
					"4) Planification d'une séance pour un groupe\n" +
					"5) Visualisation des séances planifiées\n" +
					"6) Inventaire du materiel dans chaque centre\n" +
					"7) Ajout de materiel dans un centre\n" +
					"8) Suppression de materiel dans un centre\n" +
					"9) Pour chaque activité, classement des centres en fonction du nombre d'inscrits\n" +
					"10) Classement des villes par nombre de stagiaires inscrits\n" +
					"11) Sortir ?\n");
			i = lecteur.nextInt();
			
			try {
			if(i==1){ // Enregistrement d'un stagiaire dans un centre
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
				VisualSeance.visual(cnct);
			}
			if(i==6){ // Gestion du materiel : inventaire
				GestionMateriel.inventaire(cnct);
			}
			if(i==7){ // Gestion du materiel : ajout
				// TODO
			}
			if(i==8){ // Gestion du materiel : suppression
				// TODO
			}
			if(i==9){ // Pour chaque activite, classement des centres
				ClassementCentres.classement(cnct);
			}
			if(i==10){ // Classement des villes
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
