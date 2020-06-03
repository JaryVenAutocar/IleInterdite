import java.awt.*;
import javax.swing.*;

/**
 * Une classe pour representer la zone d'affichage des Zones.
 *
 * JPanel est une classe d'elements graphiques, pouvant comme JFrame contenir
 * d'autres elements graphiques.
 *
 * Cette vue va etre un observateur du modele et sera mise a jour a chaque
 * nouvelle generation des Zones.
 */
class VueGrille extends JPanel implements Observer {
    /** On maintient une reference vers le modele. */
    private CModele modele;
    /** Definition d'une taille (en pixels) pour l'affichage des Zones. */
    private final static int TAILLE = 15;
    private JLabel j;
    private JLabel j2;

    
    /** Constructeur. */
    public VueGrille(CModele modele, JLabel j, JLabel j2) {
    	
		this.modele = modele;
		this.j = j;
		this.j2 = j2;
		/** On enregistre la vue [this] en tant qu'observateur de [modele]. */
		modele.addObserver(this);
		/**
		 * Definition et application d'une taille fixe pour cette zone de
		 * l'interface, calculee en fonction du nombre de Zones et de la
		 * taille d'affichage.
		 */
		Dimension dim = new Dimension(TAILLE*CModele.COTE,
					      TAILLE*CModele.COTE);
		this.setPreferredSize(dim);
    }
    

    /**
     * L'interface [Observer] demande de fournir une methode [update], qui
     * sera appelee lorsque la vue sera notifiee d'un changement dans le
     * modele. Ici on se content de reafficher toute la grille avec la methode
     * predefinie [repaint].
     */
    public void update() { 
    	repaint();
    	if(modele.isPerdue() == false && modele.getTour() != 0) {
	    	j.setText("il lui reste " + (3 - modele.getNbActions()) + " actions");
	    	j2.setText("L'ile s'inonde de plus en plus ! C'est le tour du joueur "+ (modele.getTour()%3+1) +" (en rouge), son role est " + this.modele.tabJoueurs[modele.getTour()%3].getRole() + ",");
    	}
    	else if(modele.isPerdue() == false && modele.getTour() == 0) {
	    	j.setText("il lui reste " + (3 - modele.getNbActions()) + " actions");
	    	j2.setText("Bienvenue dans cette nouvelle partie ! Inaugurons les hostilites avec le joueur 1 (en rouge), son role est " + this.modele.getJ().getRole() + ",");
    	}
    	else {
    		j.setText("Merci d'avoir joue ! :)");
	    	j2.setText("Quel dommage, vous avez perdu ! Vous pouvez retenter votre chance une prochaine fois. ");
    	}
    }

    
    /**
     * Les elements graphiques comme [JPanel] possedent une methode
     * [paintComponent] qui definit l'action a accomplir pour afficher cet
     * element. On la redefinit ici pour lui confier l'affichage des Zones.
     *
     * La classe [Graphics] regroupe les elements de style sur le dessin,
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
			 * coordonnees du coin en haut a gauche.
			 */
			paint(g, modele.getZone(i, j), (i-1)*TAILLE, (j-1)*TAILLE);
		    }
		}
    }
    
    
    /**
     * Fonction auxiliaire de dessin d'une Zone.
     * Ici, la classe [Zone] ne peut etre designee que par l'intermediaire
     * de la classe [CModele] a laquelle elle est interne, d'ou le type
     * [CModele.Zone].
     * Ceci serait impossible si [Zone] etait declaree privee dans [CModele].
     */
    private void paint(Graphics g, Zone c, int x, int y) {
        /** Selection d'une couleur. 
         *  On fait tous les cas possibles selon le typeZone de la Zone en parametre
         */
    	
     	if(c.getTypeZone() == typeZone.innonde) {
     		if(c.getX() == modele.getJ().getX() && c.getY() == modele.getJ().getY()) {
    			g.setColor(Color.RED);
        		g.fillRect(x,  y,  TAILLE, TAILLE);
     		} else {
     			g.setColor(Color.BLUE);
     			g.fillRect(x,  y,  TAILLE, TAILLE);
     		}
		} 
     	
     	else if(c.isJoueur() || c.getTypeZone() == typeZone.joueur) {
    		if(c.getX() == modele.getJ().getX() && c.getY() == modele.getJ().getY()) {
    			g.setColor(Color.RED);
        		g.fillRect(x,  y,  TAILLE, TAILLE);
    		} else {
    			g.setColor(Color.GRAY);
        		g.fillRect(x,  y,  TAILLE, TAILLE);
    		}
     	}
     	
     	else if(c.getTypeZone() == typeZone.terre) {
			Color terre = new Color(148,87,8);
			g.setColor(terre);
			g.fillRect(x,  y, TAILLE, TAILLE);
		}
     	
     	else if(c.getTypeZone() == typeZone.eau) {
			Color eau = new Color(166,221,230);
			g.setColor(eau);
			g.fillRect(x,  y, TAILLE, TAILLE);
		}
     	
     	else if(c.getTypeZone() == typeZone.feu) {
			Color feu = new Color(255,173,28);
			g.setColor(feu);
			g.fillRect(x,  y, TAILLE, TAILLE);
		} 
     	
     	else if(c.getTypeZone() == typeZone.air) {
			Color air = new Color(255,255,255);
			g.setColor(air);
			g.fillRect(x,  y, TAILLE, TAILLE);
		}
     	
     	else if(c.getTypeZone() == typeZone.normal) {
        	Color sable = new Color(245, 224, 158);
    		g.setColor(sable);
    		g.fillRect(x,  y,  TAILLE, TAILLE);
		}
     	
     	else if(c.getTypeZone() == typeZone.heliport) {
			g.setColor(Color.PINK);
			g.fillRect(x,  y, TAILLE, TAILLE);
		} 
     	
     	else {
			g.setColor(Color.BLACK);
			g.fillRect(x,  y,  TAILLE, TAILLE);
		}
    }

}