import javax.swing.*;

/**
 * Une classe pour représenter la zone contenant le bouton.
 *
 * Cette zone n'aura pas à être mise à jour et ne sera donc pas un observateur.
 * En revanche, comme la zone précédente, celle-ci est un panneau [JPanel].
 */
class VueCommandes extends JPanel {
    /**
     * Pour que le bouton puisse transmettre ses ordres, on garde une
     * référence au modèle.
     */
    private CModele modele;
    public JButton boutonAvance, boutonAsseche, boutonGauche, boutonDroite, boutonHaut, boutonBas;

    /** Constructeur. */
    public VueCommandes(CModele modele) {
    	this.modele = modele;
	/**
	 * On crée un nouveau bouton, de classe [JButton], en précisant le
	 * texte qui doit l'étiqueter.
	 * Puis on ajoute ce bouton au panneau [this].
	 */
    	boutonAvance = new JButton("fin de tour");
    	boutonHaut = new JButton("^");
    	boutonAsseche = new JButton("Assécher");
    	boutonGauche = new JButton("<");
    	boutonBas = new JButton("v");
    	boutonDroite = new JButton(">");
    	
    	
    	this.add(boutonAvance);
    	this.add(boutonAsseche);
    	this.add(boutonGauche);
    	this.add(boutonHaut);
    	this.add(boutonBas);
    	this.add(boutonDroite);
    	
    	
	/**
	 * Le bouton, lorsqu'il est cliqué par l'utilisateur, produit un
	 * événement, de classe [ActionEvent].
	 *
	 * On a ici une variante du schéma observateur/observé : un objet
	 * implémentant une interface [ActionListener] va s'inscrire pour
	 * "écouter" les événements produits par le bouton, et recevoir
	 * automatiquements des notifications.
	 * D'autres variantes d'auditeurs pour des événements particuliers :
	 * [MouseListener], [KeyboardListener], [WindowListener].
	 *
	 * Cet observateur va enrichir notre schéma Modèle-Vue d'une couche
	 * intermédiaire Contrôleur, dont l'objectif est de récupérer les
	 * événements produits par la vue et de les traduire en instructions
	 * pour le modèle.
	 * Cette strate intermédiaire est potentiellement riche, et peut
	 * notamment traduire les mêmes événements de différentes façons en
	 * fonction d'un état de l'application.
	 * Ici nous avons un seul bouton réalisant une seule action, notre
	 * contrôleur sera donc particulièrement simple. Cela nécessite
	 * néanmoins la création d'une classe dédiée.
	 */	
    	Controleur ctrl = new Controleur(modele, this, boutonAvance);
    	Controleur Gauche = new Controleur(modele, this, boutonGauche);
    	Controleur Droit = new Controleur(modele, this, boutonDroite);
    	Controleur Bas = new Controleur(modele, this, boutonBas);
    	Controleur Haut = new Controleur(modele, this, boutonHaut);
    	Controleur Asseche = new Controleur(modele, this, boutonAsseche);
	/** Enregistrement du contrôleur comme auditeur du bouton. */
    	boutonAvance.addActionListener(ctrl);
    	boutonDroite.addActionListener(Droit);
    	boutonGauche.addActionListener(Gauche);
    	boutonBas.addActionListener(Bas);
    	boutonHaut.addActionListener(Haut);
    	boutonAsseche.addActionListener(Asseche);
    	
	
	/**
	 * Variante : une lambda-expression qui évite de créer une classe
         * spécifique pour un contrôleur simplissime.
         *
         JButton boutonAvance = new JButton(">");
         this.add(boutonAvance);
         boutonAvance.addActionListener(e -> { modele.avance(); });
         *
         */

    }
}