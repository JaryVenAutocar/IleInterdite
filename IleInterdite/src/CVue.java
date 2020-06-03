import java.awt.*;
import javax.swing.*;

/**
 * La vue : l'interface avec l'utilisateur.
 *
 * On définit une classe chapeau [CVue] qui crée la fenêtre principale de 
 * l'application et contient les deux parties principales de notre vue :
 *  - Une zone d'affichage où on voit l'ensemble des Zones.
 *  - Une zone de commande avec un bouton pour passer à la génération suivante.
 */
class CVue{
    /**
     * JFrame est une classe fournie pas Swing. Elle représente la fenêtre
     * de l'application graphique.
     */
    private JFrame frame;
    /**
     * VueGrille et VueCommandes sont deux classes définies plus loin, pour
     * nos deux parties de l'interface graphique.
     */
    private VueGrille grille;
    private VueCommandes commandes;

    /** Construction d'une vue attachée à un modèle. */
    public CVue(CModele modele) {
	/** Définition de la fenêtre principale. */
	frame = new JFrame();
	JLabel tourJoueur = new JLabel("Bienvenue sur l'ile. Veuillez effectuer une action pour debuter la partie. Votre personnage est le Joueur 1,", JLabel.CENTER);
	JLabel actions = new JLabel("il lui reste " + (3 - modele.getNbActions()) + " actions", JLabel.LEFT);
	frame.add(tourJoueur);
	frame.add(actions);
	frame.setTitle("Ile Interdite, by Jary Vallimamode Damien Ouzillou et Marius Seve");
	
	/**
	 * On précise un mode pour disposer les différents éléments à
	 * l'intérieur de la fenêtre.
	 *    FlowLayout (défaut pour un JPanel) : les éléments sont disposés
	 *    l'un à la suite de l'autre, dans l'ordre de leur ajout, les lignes
	 *    se formant de gauche à droite et de haut en bas. Un élément peut
	 *    passer à la ligne lorsque l'on redimensionne la fenêtre.
	 */
	frame.setLayout(new FlowLayout());

	/** Définition des deux vues et ajout à la fenêtre. */
	grille = new VueGrille(modele, actions, tourJoueur);
	frame.add(grille);
	commandes = new VueCommandes(modele);
	frame.add(commandes);

	/**
	 * Fin de la plomberie :
	 *  - Ajustement de la taille de la fenêtre en fonction du contenu.
	 *  - Indiquer qu'on quitte l'application si la fenêtre est fermée.
	 *  - Préciser que la fenêtre doit bien apparaître à l'écran.
	 */
	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(900, 450);
	frame.setVisible(true);
    }
}