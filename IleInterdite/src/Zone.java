/**
 * Définition d'une classe pour les Zones.
 * Cette classe fait encore partie du modèle.
 */

/** L'état d'une Zone est donné par un enum. */

enum typeZone {joueur, heliport, air, eau, feu, terre, normal, innonde, submerge} ;

class Zone {
    /** On conserve un pointeur vers la classe principale du modèle. */
    private CModele modele;
    
    private boolean estJoueur;
    private boolean estInnonde;
    private boolean getKey;
    private typeZone z;
    /**
     * On stocke les coordonnées pour pouvoir les passer au modèle
     */
    private final int x, y;
		
	/** Constructeur : on initialise une zone */
	public Zone(CModele modele, boolean estJoueur, boolean getKey, int x, int y, typeZone z) {
		this.modele = modele;
		this.estJoueur = estJoueur;
		this.getKey = getKey;
		this.x = x; this.y = y;
		this.z = z;
	}
    
	
	/** Methode permettant de recuperer l'abscisse d'une zone */
    public int getX() {
		return x;
	}

    
    /** Methode permettant de recuperer l'ordonnee d'une zone */
	public int getY() {
		return y;
	}
	
	
	/** Methode permettant de savoir si un joueur est sur la zone */
    public boolean isJoueur() {
		return estJoueur;
	}


	/** Methode permettant de savoir si une zone est inondee */
	public boolean isInnonde() {
		return estInnonde;
	}

	
	/** Methode permettant de savoir si une zone contient une cle */
	public boolean isKey() {
		return getKey;
	}

	
	/** Methode permettant de recuperer le type d'une zone */
	public typeZone getTypeZone() {
		return z;
	}
	
	
	/** Methode permettant de modifier le type d'une zone */
	public void setTypeZone(typeZone z) {
		this.z = z;
	}

	
	/** Methode permettant de modifier la presence d'un joueur sur une zone */
	public void setEstJoueur(boolean estJoueur) {
		this.estJoueur = estJoueur;
	}
	
	
	/** Methode permettant de modifier la presence d'une cle sur une zone */
	public void setKey(boolean getKey) {
		this.getKey = getKey;
	}


	/** Un test a l'usage des autres classes (sera utilise par la vue). */
    public boolean estNormale() {
        return z == typeZone.normal;
    }
    
    
    /** Un autre test a l'usage des autres classes (sera utilise par la vue). */
    public boolean estInnonde() {
    	return z == typeZone.innonde;
    }
}