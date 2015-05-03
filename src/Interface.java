import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

public class Interface extends JFrame implements ActionListener, Runnable{
	
	private int request;
	private int valideMode;
	private JPanel container;
	private JButton stagCentButton;
	private JButton stagActButton;
	private JButton addGroupButton;
	private JButton delGroupButton;
	private JButton planGroupBUtton;
	private JButton allPlanButton;
	private JButton allStuff;
	private JButton addStuff;
	private JButton delStuff;
	private JButton classCenter;
	private JButton classTown;
	private JButton close;
	private JLabel mainLab;
	private JLabel requestLab;
	private JLabel getInfoLab;
	private JTextField getInfoText;
	private JButton validateButton;
	private Thread action = new Thread(this);
	public Interface(){
		this.container = new JPanel();
		this.stagCentButton = new JButton("Ajouter un stagiaire à un centre");
		this.stagActButton = new JButton("Ajouter un stagiaire à une activité");
		this.addGroupButton = new JButton("Ajouter un groupe");
		this.delGroupButton = new JButton("Supprimer un groupe");
		this.planGroupBUtton = new JButton("Planifier une séance pour un groupe");
		this.allPlanButton = new JButton("Voir toutes les planifications de séance");
		this.allStuff = new JButton("Voir tout le matériel disponible");
		this.addStuff = new JButton("Rajouter du matériel dans un centre");
		this.delStuff = new JButton("Supprimer du matériel");
		this.classCenter = new JButton("Classemenent des centres pour chaque activité");
		this.classTown = new JButton("Classement des villes en fonction du nombre d'inscrits");
		this.close = new JButton("Quitter");
		this.mainLab = new JLabel("<html><pre>VLV Manager</pre></html>");
		this.requestLab = new JLabel();
		this.getInfoLab = new JLabel();
		this.getInfoText = new JTextField();
		this.validateButton = new JButton("Suivant");
		//Parametrage de chaque composant
		this.mainLab.setFont(new Font("Arial", Font.BOLD, 15));
		this.mainLab.setForeground(Color.black);
		this.requestLab.setFont(new Font("Arial", Font.BOLD, 15));
		this.requestLab.setForeground(Color.black);
		this.getInfoLab.setFont(new Font("Arial", Font.BOLD, 15));
		this.getInfoLab.setForeground(Color.black);
		//this.mainLab.setVisible(true);
		//Caractéristiques de la fenetre en elle même
		this.setTitle("VLV Manager");
		this.setSize(600, 600);
		//Important pour terminer le processus de l'appication
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		//Permet d'ajouter une action à un clic sur les boutons
		this.stagCentButton.addActionListener(this);
		this.stagActButton.addActionListener(this);
		this.addGroupButton.addActionListener(this);
		this.delGroupButton.addActionListener(this);
		this.planGroupBUtton.addActionListener(this);
		this.allPlanButton.addActionListener(this);
		this.allStuff.addActionListener(this);
		this.addStuff.addActionListener(this);
		this.delStuff.addActionListener(this);
		this.classCenter.addActionListener(this);
		this.classTown.addActionListener(this);
		this.close.addActionListener(this);
		this.validateButton.addActionListener(this);
		//On ajoute tous les composants au container
		//Important car permet le placement des différents composants
		this.container.setLayout(null);
		this.container.add(stagCentButton);
		this.container.add(stagActButton);
		this.container.add(addGroupButton);
		this.container.add(delGroupButton);
		this.container.add(planGroupBUtton);
		this.container.add(allPlanButton);
		this.container.add(allStuff);
		this.container.add(addStuff);
		this.container.add(delStuff);
		this.container.add(classCenter);
		this.container.add(classTown);
		this.container.add(close);
		this.container.add(mainLab);
		this.container.add(requestLab);
		this.container.add(getInfoLab);
		this.container.add(getInfoText);
		this.container.add(validateButton);
		//Placement des differents composants
		this.stagCentButton.setBounds(50, 100, 400, 30);
		this.stagActButton.setBounds(50, 150, 400, 30);
		this.addGroupButton.setBounds(50, 200, 400, 30);
		this.delGroupButton.setBounds(50, 250, 400, 30);
		this.planGroupBUtton.setBounds(50,300, 400, 30);
		this.allPlanButton.setBounds(50, 350, 400, 30);
		this.allStuff.setBounds(50, 400, 400, 30);
		this.addStuff.setBounds(50, 450, 400, 30);
		this.delStuff.setBounds(50, 500, 400, 30);
		this.classCenter.setBounds(50, 550, 400, 30);
		this.classTown.setBounds(50, 600, 400, 30);
		this.close.setBounds(50, 650, 400, 30);
		this.mainLab.setBounds(600, 25, 400, 50);
		this.requestLab.setBounds(50, 160, 1200, 500);
		this.getInfoLab.setBounds(300, 50, 400, 50);
		this.getInfoText.setBounds(300, 100, 400, 50);
		this.validateButton.setBounds(300, 500, 400, 50);
		this.requestLab.setVisible(false);
		this.getInfoLab.setVisible(false);
		this.getInfoText.setVisible(false);
		this.validateButton.setVisible(false);
		this.setContentPane(this.container);
		this.setVisible(true);
		
	}
	/**
	 * Fonction permettant de lier une action à un clic de bouton
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == this.stagCentButton){
			request = 1;
			refreshThread();
			this.action.start();
			hideMenu();
		}
		if (arg0.getSource() == this.stagActButton){
			request = 2;
			refreshThread();
			this.action.start();
			hideMenu();
		}
		if (arg0.getSource() == this.addGroupButton){
			request = 3;
			refreshThread();
			this.action.start();
			hideMenu();
		}
		if (arg0.getSource() == this.delGroupButton){
			request = 4;
			refreshThread();
			this.action.start();
			hideMenu();
		}
		if (arg0.getSource() == this.planGroupBUtton){
			request = 5;
			refreshThread();
			this.action.start();
			hideMenu();
		}
		if (arg0.getSource() == this.allPlanButton){
			request = 6;
			refreshThread();
			this.action.start();
			hideMenu();
		}
		if (arg0.getSource() == this.allStuff){
			request = 7;
			refreshThread();
			this.action.start();
			hideMenu();
		}
		if (arg0.getSource() == this.addStuff){
			request = 8;
			refreshThread();
			this.action.start();
			hideMenu();
		}
		if (arg0.getSource() == this.delStuff){
			request = 9;
			refreshThread();
			this.action.start();
			hideMenu();
		}
		if (arg0.getSource() == this.classCenter){
			request = 10;
			refreshThread();
			this.action.start();
			hideMenu();
		}
		if (arg0.getSource() == this.classTown){
			request = 11;
			refreshThread();
			this.action.start();
			hideMenu();
		}
		if (arg0.getSource() == this.validateButton){
		}
		if (arg0.getSource() == this.close){
			try {
				Main.cnct.close();
				System.err.println("Connexion fermee");
			} catch (SQLException e) {
				System.err.println("Impossible de fermer la connexion");
				e.printStackTrace();
			}
			this.dispose();
		}
	}
	
	public void mainMenu(){
		this.stagCentButton.setVisible(true);
		this.stagActButton.setVisible(true);
		this.addGroupButton.setVisible(true);
		this.delGroupButton.setVisible(true);
		this.planGroupBUtton.setVisible(true);
		this.allPlanButton.setVisible(true);
		this.allStuff.setVisible(true);
		this.addStuff.setVisible(true);
		this.delStuff.setVisible(true);
		this.classCenter.setVisible(true);
		this.classTown.setVisible(true);
		this.close.setVisible(true);
		this.mainLab.setVisible(true);
		this.requestLab.setVisible(false);
		this.getInfoLab.setVisible(false);
		this.getInfoText.setVisible(false);
		this.validateButton.setVisible(false);
	}
	
	public void hideMenu(){
		this.stagCentButton.setVisible(false);
		this.stagActButton.setVisible(false);
		this.addGroupButton.setVisible(false);
		this.delGroupButton.setVisible(false);
		this.planGroupBUtton.setVisible(false);
		this.allPlanButton.setVisible(false);
		this.allStuff.setVisible(false);
		this.addStuff.setVisible(false);
		this.delStuff.setVisible(false);
		this.classCenter.setVisible(false);
		this.classTown.setVisible(false);
		this.close.setVisible(false);
		this.mainLab.setVisible(false);
		this.requestLab.setVisible(true);
		this.getInfoLab.setVisible(true);
		this.getInfoText.setVisible(true);
		this.validateButton.setVisible(true);
	}
	public void run() {
		try {
			if(request==1){ // Enregistrement d'un stagiaire dans un centre
				RegisterQuery.register(Main.cnct);
			}
			if(request==2){ // Enregistrement d'un stagiaire à une activité
				RegisterQuery.activity(Main.cnct);
			}
			if(request==3){ // Creation d'un groupe
			    GestionGroupe.creation(Main.cnct);
			}
			if(request==4){ // Suppression d'un groupe
				GestionGroupe.suppression(Main.cnct);
			}
			if(request==5){ // Planification d'une seance pour un groupe
			    PlanificationSeance.register(Main.cnct);
			}
			if(request==6){ // Visualisation des seances planifiees
				VisualSeance.visual(Main.cnct);
			}
			if(request==7){ // Gestion du materiel : inventaire
				GestionMateriel.inventaire(Main.cnct);
			}
	
			if(request==8){ // Gestion du materiel : ajout
				GestionMateriel.ajout(Main.cnct);
			}
			if(request==9){ // Gestion du materiel : suppression
				// TODO
			}
			if(request==10){ // Pour chaque activite, classement des centres
				ClassementCentres.classement(Main.cnct);
			}
			if(request==11){ // Classement des villes
				ClassementVilles.classement(Main.cnct);
			}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public void refreshThread(){
		try {
			this.action.join();
			this.action = new Thread(this);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setInfoLab(String text){
		this.getInfoLab.setText(text);
	}
	public void setMode(int i){
		valideMode = i;
	}
	public void setReqLab(String text){
		this.requestLab.setText(text);
	}
	
	public Integer setVarInfoInt(){
		//i = Integer.valueOf(this.getInfoText.getText());
		return Integer.valueOf(JOptionPane.showInputDialog(this, getInfoLab.getText(), "", JOptionPane.PLAIN_MESSAGE));
		
	}
	
	public String setVarInfoStr(){
		//s = this.getInfoText.getText();
		return JOptionPane.showInputDialog(this, getInfoLab.getText(), "", JOptionPane.QUESTION_MESSAGE);
	}
	
	public Integer yesNoQuestion(){
		return JOptionPane.showConfirmDialog(this, getInfoLab.getText(), "", JOptionPane.YES_NO_OPTION);
	}
	
	public JTextField getText(){
		return getInfoText;
	}
}
