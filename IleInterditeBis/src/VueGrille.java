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
    private final static int TAILLE = 15;
    private JLabel j, j2, j3, j4, j5, j6, j7, j8;

    /** Constructeur. */
    public VueGrille(CModele modele, JLabel j, JLabel j2, JLabel j3, JLabel j4, JLabel j5,JLabel j6, JLabel j7,JLabel j8) { //
    	
		this.modele = modele;
		this.j = j;
		this.j2 = j2;
		this.j3 = j3;
		this.j4 = j4;
		this.j5 = j5;
		this.j6 = j6;
		this.j7 = j7;
		this.j8 = j8;
		
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
    	//Joueur tabJ [] = {modele.j1, modele.j2, modele.j3}; 
    	if(modele.partiePerdue == false && modele.tour != 0) {
	    	j.setText("<html> il lui reste " + (3 - modele.nbActions) + " actions. </html>");
	    	j2.setText("<html> L'ile s'inonde de plus en plus ! C'est le tour du joueur </html> " + (modele.tour%3+1) + "<html> (en rouge), son role est </html>" + this.modele.tabJoueurs[modele.tour%3].r + "<br> </html>");
	    	
	    	if(modele.j1.keyElementList.isEmpty()) j3.setText( "<html> 																				J1 ne possede pas de cles <br> </html>");
	    	else {
	    		j3.setText("										J1 possede les cles suivantes : "); 
	    		int nbKeyAir = modele.j1.nbKeyOfArtefact(element.air);
	    		int nbKeyTerre = modele.j1.nbKeyOfArtefact(element.terre);
	    		int nbKeyFeu = modele.j1.nbKeyOfArtefact(element.feu);
	    		int nbKeyEau = modele.j1.nbKeyOfArtefact(element.eau);
	    		j4.setText(nbKeyAir + " " + element.air +", " + nbKeyTerre + " " + element.terre +", " + nbKeyFeu + " " + element.feu +", " + nbKeyEau + " " + element.eau);
	    	
	    	}
	    	
	    	if(modele.j2.keyElementList.isEmpty()) j5.setText( "J2 ne possede pas de cles");
	    	else {
	    		j5.setText("J2 possede les cles suivantes : "); 
	    		int nbKeyAir = modele.j2.nbKeyOfArtefact(element.air);
	    		int nbKeyTerre = modele.j2.nbKeyOfArtefact(element.terre);
	    		int nbKeyFeu = modele.j2.nbKeyOfArtefact(element.feu);
	    		int nbKeyEau = modele.j2.nbKeyOfArtefact(element.eau);
	    		j6.setText("<html>" + nbKeyAir + " " + element.air +", " + nbKeyTerre + " " + element.terre +", " + nbKeyFeu + " " + element.feu +", " + nbKeyEau + " " + element.eau +", </html> ");
	    	
	    	}
	    	
	    	if(modele.j3.keyElementList.isEmpty()) j7.setText( "J3 ne possede pas de cles");
	    	else {
	    		j7.setText("J3 possede les cles suivantes : "); 
	    		int nbKeyAir = modele.j3.nbKeyOfArtefact(element.air);
	    		int nbKeyTerre = modele.j3.nbKeyOfArtefact(element.terre);
	    		int nbKeyFeu = modele.j3.nbKeyOfArtefact(element.feu);
	    		int nbKeyEau = modele.j3.nbKeyOfArtefact(element.eau);
	    		j8.setText("<html>" + nbKeyAir + " " + element.air +", " + nbKeyTerre + " " + element.terre +", " + nbKeyFeu + " " + element.feu +", " + nbKeyEau + " " + element.eau +", </html> ");
	    	
	    	}
	    	
	    	
    	
    	}
    	else if(modele.partiePerdue == false && modele.tour == 0) {
	    	j.setText("il lui reste " + (3 - modele.nbActions) + " actions");
	    	j2.setText("Bienvenue dans cette nouvelle partie ! Inaugurons les hostilites avec le joueur 1 (en rouge), son role est " + this.modele.j.r + ",");
    	}
    	else {
    		j.setText("Merci d'avoir joue ! :)");
	    	j2.setText("Quel dommage, vous avez perdu ! Vous pouvez retenter votre chance une prochaine fois. ");
    	}
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
			Color eau = new Color(166,221,230);
			g.setColor(eau);
			g.fillRect(x,  y, TAILLE, TAILLE);
		}
     	
     	else if(c.z == typeZone.feu) {
			Color feu = new Color(255,173,28);
			g.setColor(feu);
			g.fillRect(x,  y, TAILLE, TAILLE);
		} 
     	
     	else if(c.z == typeZone.air) {
			Color air = new Color(255,255,255);
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