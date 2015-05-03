import java.sql.*;
import java.util.Scanner;

public class Main {
	
	static String id = "garcia1";
	static String pwd = "garcia1";
	static String url = "jdbc:oracle:thin:@ensioracle1.imag.fr:1521:ensioracle1";
	static Connection cnct;
	static Interface graphique;

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
		graphique = new Interface();
		/*int i = 12;
		Scanner lecteur = new Scanner(System.in);
		try {
			if (cnct.isValid(1)) 
				i = 1;
		} catch(SQLException e){
			i = 12;
		}
		while (i != 12) {
			System.out.println("Que souhaitez-vous faire ? : \n" +
					"1) Enregistrement d'un stagiaire à un centre\n" + // RegisterQuery.java
					"2) Enregistrement d'un stagiaire à une activité\n"+
					"3) Création d'un groupe\n" +
					"4) Suppression d'un groupe\n" +
					"5) Planification d'une séance pour un groupe\n" +
					"6) Visualisation des séances planifiées\n" +
					"7) Inventaire du materiel dans chaque centre\n" +
					"8) Ajout de materiel dans un centre\n" +
					"9) Suppression de materiel dans un centre\n" +
					"10) Pour chaque activité, classement des centres en fonction du nombre d'inscrits\n" +
					"11) Classement des villes par nombre de stagiaires inscrits\n" +
					"12) Sortir ?\n");
			i = lecteur.nextInt();
			try {
			if(i==1){ // Enregistrement d'un stagiaire dans un centre
				RegisterQuery.register(cnct);
			}
			if(i==2){ // Enregistrement d'un stagiaire à une activité
				RegisterQuery.activity(cnct);
			}
			if(i==3){ // Creation d'un groupe
			    GestionGroupe.creation(cnct);
			}
			if(i==4){ // Suppression d'un groupe
				GestionGroupe.suppression(cnct);
			}
			if(i==5){ // Planification d'une seance pour un groupe
			    PlanificationSeance.register(cnct);
			}
			if(i==6){ // Visualisation des seances planifiees
				VisualSeance.visual(cnct);
			}
			if(i==7){ // Gestion du materiel : inventaire
				GestionMateriel.inventaire(cnct);
			}

			if(i==8){ // Gestion du materiel : ajout
				GestionMateriel.ajout(cnct);
			}
			if(i==9){ // Gestion du materiel : suppression
				// TODO
			}
			if(i==10){ // Pour chaque activite, classement des centres
				ClassementCentres.classement(cnct);
			}
			if(i==11){ // Classement des villes
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
		}*/
	}
	
	public static void dumpResultSet(ResultSet rset) throws SQLException {
		ResultSetMetaData rsetmd = rset.getMetaData();
		String res = new String("<html><pre>");
		int i = rsetmd.getColumnCount();
		for (int k=1;k<=i;k++){
			System.out.print(rsetmd.getColumnName(k) + "\t");
			res += rsetmd.getColumnName(k) +  "&#9;" ;
		}
		res += "\n";
		System.out.println();
		while (rset.next()) {
			for (int j = 1; j <= i; j++) {
				System.out.print(rset.getString(j) + "\t");
				res += rset.getString(j) + "&#9;";
			}
			res += "\n";
			System.out.println();
		}
		res += "</pre></html>";
		graphique.setReqLab(res);
	}


}
