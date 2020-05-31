import java.awt.event.*;

import javax.swing.JButton;

/**
 * Classe pour notre contrôleur rudimentaire.
 * 
 * Le contrôleur implémente l'interface [ActionListener] qui demande
 * uniquement de fournir une méthode [actionPerformed] indiquant la
 * réponse du contrôleur à la réception d'un événement.
 */
class Controleur implements ActionListener {
    /**
     * On garde un pointeur vers le modèle, car le contrôleur doit
     * provoquer un appel de méthode du modèle.
     * Remarque : comme cette classe est interne, cette inscription
     * explicite du modèle est inutile. On pourrait se contenter de
     * faire directement référence au modèle enregistré pour la classe
     * englobante [VueCommandes].
     */
    CModele modele;
    JButton bouton;
    VueCommandes commandes;
    public Controleur(CModele modele, VueCommandes commandes, JButton bouton) { 
    	this.modele = modele;
    	this.commandes = commandes;
    	this.bouton = bouton;
    	}
    /**
     * Action effectuée à réception d'un événement : appeler la
     * méthode [avance] du modèle.
     */
    public void actionPerformed(ActionEvent e) {
    	
    	
    	if(!modele.partiePerdue && bouton == commandes.boutonAvance) {
	    	System.out.println("");
	    	System.out.println("Nouveau tour !");
	    	System.out.println("");
    	}
    	
    	if(bouton == commandes.boutonAvance) {
    		modele.nbActions = 0;
    		modele.avance();
    	}
    	else if(bouton == commandes.boutonAsseche)
    		modele.asseche();
    	else if(bouton == commandes.boutonGauche)
    		modele.gauche();
    	else if(bouton == commandes.boutonDroite)
    		modele.droite();
    	else if(bouton == commandes.boutonHaut)
    		modele.haut();
    	else if(bouton == commandes.boutonArtefact)
    		modele.recupArtefact();
    	else if(bouton == commandes.boutonRecupKey)
    		modele.recupKey();
    	else if(bouton == commandes.boutonGiveKey)
    		modele.giveKey();
    	else
    		modele.bas();
    	
    	//Maximum 3 actions par tour
    	if(modele.nbActions > 2) {
    	    commandes.boutonGauche.setEnabled(false);
    	    commandes.boutonDroite.setEnabled(false);
    	    commandes.boutonHaut.setEnabled(false);
    	    commandes.boutonBas.setEnabled(false);
    	    commandes.boutonAsseche.setEnabled(false);
    	    commandes.boutonArtefact.setEnabled(false);
    	    commandes.boutonRecupKey.setEnabled(false);
    	    commandes.boutonGiveKey.setEnabled(false);
    	    commandes.boutonAvance.setEnabled(true);	    
    		}
    	
    	//A chaque debut de tour, toutes les cases sont à nouveau disponibles
    	if(modele.nbActions == 0) {
    		commandes.boutonGauche.setEnabled(true);
    	    commandes.boutonDroite.setEnabled(true);
    	    commandes.boutonHaut.setEnabled(true);
    	    commandes.boutonBas.setEnabled(true);
    	    commandes.boutonAsseche.setEnabled(true);
    	    commandes.boutonAvance.setEnabled(true);
    	    commandes.boutonArtefact.setEnabled(true);
    	    commandes.boutonRecupKey.setEnabled(true);
    	    commandes.boutonGiveKey.setEnabled(true);
    	}
    	
    	if(modele.partieGagnee) {
			System.out.println("Vous avez gagne la partie");
    		commandes.boutonGauche.setVisible(false);
    	    commandes.boutonDroite.setVisible(false);
    	    commandes.boutonHaut.setVisible(false);
    	    commandes.boutonBas.setVisible(false);
    	    commandes.boutonAsseche.setVisible(false);
    	    commandes.boutonAvance.setVisible(false);
    	    commandes.boutonArtefact.setVisible(false);
    	    commandes.boutonRecupKey.setVisible(false);
    	    commandes.boutonGiveKey.setVisible(false);
    	}
    	
    	if(modele.partiePerdue) {
    		System.out.println("Vous avez perdu la partie");
    		commandes.boutonGauche.setVisible(false);
    	    commandes.boutonDroite.setVisible(false);
    	    commandes.boutonHaut.setVisible(false);
    	    commandes.boutonBas.setVisible(false);
    	    commandes.boutonAsseche.setVisible(false);
    	    commandes.boutonAvance.setVisible(false);
    	    commandes.boutonArtefact.setVisible(false);
    	    commandes.boutonRecupKey.setVisible(false);
    	    commandes.boutonGiveKey.setVisible(false);
    	}
    	
    	
    	
    	//Les tours alternent, 3 joueurs donc modulo 3
    	if( (modele.tour)%3 == 0) modele.j = modele.j1;
    	else if( (modele.tour)%3 == 1) modele.j = modele.j2;
    	else modele.j = modele.j3;
    	
    	Zone[] zoneSpeciale = {modele.heliport, modele.air, modele.eau, modele.feu, modele.terre};
    	typeZone[] typeZoneSpeciale = {typeZone.heliport, typeZone.air, typeZone.eau, typeZone.feu, typeZone.terre};
    	
    	for(int i = 0; i < zoneSpeciale.length; i++) {
    		
    		if(modele.partiePerdue == false) {
	    		if(zoneSpeciale[i].z == typeZone.innonde && bouton == commandes.boutonAvance)
	    			System.out.println("Cette zone speciale est innondee faites attention : " + typeZoneSpeciale[i]);
	
	    		if(zoneSpeciale[i].z == typeZone.submerge && bouton == commandes.boutonAvance)
	    			System.out.println("Cette zone speciale est submergee c'est perdu : " + typeZoneSpeciale[i]);
	    		
	    		if(zoneSpeciale[i].z == typeZone.submerge)
					modele.partiePerdue = true;
    		}
    	}
    	
    	
    	//Si le joueur est sur le contour visible des bordures du jeu	
    	if(modele.j.getX() == 1 || modele.j.getX() == modele.COTE || modele.j.getY() == 1 || modele.j.getY() == modele.COTE) {
    		
    		//Si le joueur est sur un des 4 recoins du jeu
    		if(modele.j.getX() == modele.j.getY() || (modele.j.getX() == 1 && modele.j.getY() == modele.COTE) || (modele.j.getX() == modele.COTE && modele.j.getY() == 1)) {
    			if(modele.compteZoneSubmerge(modele.j.getX(), modele.j.getY()) == 2) {
    				System.out.println("Le joueur " + ((modele.tour)%3 + 1) + " s'est noye");
    				modele.partiePerdue = true;
    			}
    		}
    		
    		if(modele.compteZoneSubmerge(modele.j.getX(), modele.j.getY()) == 3) {
    			System.out.println("Le joueur " + ((modele.tour)%3 + 1) + " s'est noye");
    			modele.partiePerdue = true;
    		}
    	}	
    	
    	
    	//Si le joueur est n'importe ou sauf sur les bordures
    	else if(modele.compteZoneSubmerge(modele.j.getX(), modele.j.getY()) == 4) {
    		System.out.println("Le joueur " + ((modele.tour)%3 + 1) + " s'est noye");
    		modele.partiePerdue = true;
    	}
    
    	//Si tous les artefacts sont en la possession des joueurs
    	//Et si tous les joueurs sont sur la zone de l'heliport
    	if(modele.nbArtefacts == 4) {
    		if((modele.j1.getX() == modele.j2.getX()) && (modele.j2.getX() == modele.j3.getX()) && (modele.j3.getX() == modele.heliport.getX())) {
    			if((modele.j1.getY() == modele.j2.getY()) && (modele.j2.getY() == modele.j3.getY()) && (modele.j3.getY() == modele.heliport.getY()))
    				modele.partieGagnee = true;
    		}
    	}
    	
    }
}