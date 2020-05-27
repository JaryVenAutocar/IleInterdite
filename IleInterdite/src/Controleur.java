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
    	else
    		modele.bas();
    	
    	//Maximum 3 actions par tour
    	if(modele.nbActions > 2) {
    	    commandes.boutonGauche.setEnabled(false);
    	    commandes.boutonDroite.setEnabled(false);
    	    commandes.boutonHaut.setEnabled(false);
    	    commandes.boutonBas.setEnabled(false);
    	    commandes.boutonAsseche.setEnabled(false);
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
    	}
    	
    	//Les tours alternent, 3 joueurs donc modulo 3
    	if( (modele.tour)%3 == 0) modele.j = modele.j1;
    	else if( (modele.tour)%3 == 1) modele.j = modele.j2;
    	else modele.j = modele.j3;
    }
}