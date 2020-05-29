import java.awt.*;
import javax.swing.*;

/**
 * Une classe pour représenter la zone d'affichage des Zones.
 *
 * JPanel est une classe d'éléments graphiques, pouvant comme JFrame contenir
 * d'autres éléments graphiques.
 *
 * Cette vue va être un observateur du modèle et sera mise à jour à chaque
 * nouvelle génération des Zones.
 */
class VueGrille extends JPanel implements Observer {
    /** On maintient une référence vers le modèle. */
    private CModele modele;
    /** Définition d'une taille (en pixels) pour l'affichage des Zones. */
    private final static int TAILLE = 12;
    private JLabel j;

    /** Constructeur. */
    public VueGrille(CModele modele, JLabel j) {
    	
		this.modele = modele;
		this.j = j;
		/** On enregistre la vue [this] en tant qu'observateur de [modele]. */
		modele.addObserver(this);
		/**
		 * Définition et application d'une taille fixe pour cette zone de
		 * l'interface, calculée en fonction du nombre de Zones et de la
		 * taille d'affichage.
		 */
		Dimension dim = new Dimension(TAILLE*CModele.COTE,
					      TAILLE*CModele.COTE);
		this.setPreferredSize(dim);
    }

    /**
     * L'interface [Observer] demande de fournir une méthode [update], qui
     * sera appelée lorsque la vue sera notifiée d'un changement dans le
     * modèle. Ici on se content de réafficher toute la grille avec la méthode
     * prédéfinie [repaint].
     */
    public void update() { 
    	repaint();
    	j.setText("              Il reste " + (3 - modele.nbActions) + " actions");
    	}

    /**
     * Les éléments graphiques comme [JPanel] possèdent une méthode
     * [paintComponent] qui définit l'action à accomplir pour afficher cet
     * élément. On la redéfinit ici pour lui confier l'affichage des Zones.
     *
     * La classe [Graphics] regroupe les éléments de style sur le dessin,
     * comme la couleur actuelle.
     */
    public void paintComponent(Graphics g) {
	super.repaint();
	/** Pour chaque Zone... */
	for(int i=1; i<=CModele.COTE; i++) {
	    for(int j=1; j<=CModele.COTE; j++) {
		/**
		 * ... Appeler une fonction d'affichage auxiliaire.
		 * On lui fournit les informations de dessin [g] et les
		 * coordonnées du coin en haut à gauche.
		 */
		paint(g, modele.getZone(i, j), (i-1)*TAILLE, (j-1)*TAILLE);
	    }
	}
    }
    /**
     * Fonction auxiliaire de dessin d'une Zone.
     * Ici, la classe [Zone] ne peut être désignée que par l'intermédiaire
     * de la classe [CModele] à laquelle elle est interne, d'où le type
     * [CModele.Zone].
     * Ceci serait impossible si [Zone] était déclarée privée dans [CModele].
     */
    private void paint(Graphics g, Zone c, int x, int y) {
        /** Sélection d'une couleur. */
    	
     	if(c.z == typeZone.innonde) {
     		if(c.getX() == modele.j.getX() && c.getY() == modele.j.getY()) {
    			g.setColor(Color.RED);
        		g.fillRect(x,  y,  TAILLE, TAILLE);
     		} else {
			g.setColor(Color.BLUE);
			g.fillRect(x,  y,  TAILLE, TAILLE);
     		}
		} 
     	
     	else if(c.estJoueur || c.z == typeZone.joueur) {
    		if(c.getX() == modele.j.getX() && c.getY() == modele.j.getY()) {
    			g.setColor(Color.RED);
        		g.fillRect(x,  y,  TAILLE, TAILLE);
    		} else {
    			g.setColor(Color.GRAY);
        		g.fillRect(x,  y,  TAILLE, TAILLE);
    		}
     	}
     	
     	else if(c.z == typeZone.terre) {
			Color terre = new Color(148,87,8);
			g.setColor(terre);
			g.fillRect(x,  y, TAILLE, TAILLE);
		}
     	
     	else if(c.z == typeZone.eau) {
			Color eau = new Color(38,127,154);
			g.setColor(eau);
			g.fillRect(x,  y, TAILLE, TAILLE);
		}
     	
     	else if(c.z == typeZone.feu) {
			Color feu = new Color(255,173,28);
			g.setColor(feu);
			g.fillRect(x,  y, TAILLE, TAILLE);
		} 
     	
     	else if(c.z == typeZone.air) {
			Color air = new Color(181,236,245);
			g.setColor(air);
			g.fillRect(x,  y, TAILLE, TAILLE);
		}
     	
     	else if(c.z == typeZone.normal) {
        	Color sable = new Color(245, 224, 158);
    		g.setColor(sable);
    		g.fillRect(x,  y,  TAILLE, TAILLE);
		}
     	
     	else if(c.z == typeZone.heliport) {
			g.setColor(Color.PINK);
			g.fillRect(x,  y, TAILLE, TAILLE);
		} 
     	
     	else {
			g.setColor(Color.BLACK);
			g.fillRect(x,  y,  TAILLE, TAILLE);
		}
     	
    }

}