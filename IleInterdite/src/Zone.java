/**
 * Définition d'une classe pour les Zones.
 * Cette classe fait encore partie du modèle.
 */

/** L'état d'une Zone est donné par un enum. */
//enum etat {normal, innonde, submerge} ;
enum typeZone {joueur, heliport, air, eau, feu, terre, normal, innonde, submerge} ;

class Zone {
    /** On conserve un pointeur vers la classe principale du modèle. */
    private CModele modele;
    public boolean estJoueur;
    public boolean estInnonde;
    public boolean getKey;
    //public etat e;
    public typeZone z;
    /**
     * On stocke les coordonnées pour pouvoir les passer au modèle lors
     * de l'appel à [compteVoisines].
     */
    private final int x, y;
    
    public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	/** public Zone(CModele modele, int x, int y, etat e, typeZone z) {
        this.modele = modele;
        this.e = e;
        this.x = x; this.y = y;
        this.z = z;
    } **/
	
	public Zone(CModele modele, boolean estJoueur, boolean getKey, int x, int y, typeZone z) {
    this.modele = modele;
    this.estJoueur = estJoueur;
    this.getKey = getKey;
    this.x = x; this.y = y;
    this.z = z;
	}
	
	
    /**
     * Le passage à la génération suivante se fait en deux étapes :
     *  - D'abord on calcule pour chaque Zone ce que sera son état à la
     *    génération suivante (méthode [evalue]). On stocke le résultat
     *    dans un attribut supplémentaire [prochainEtat].
     *  - Ensuite on met à jour l'ensemble des Zones (méthode [evolue]).
     * Objectif : éviter qu'une évolution immédiate d'une Zone pollue
     * la décision prise pour une Zone voisine.
     */
    
    /** Un test à l'usage des autres classes (sera utilisé par la vue). */
    public boolean estNormale() {
        return z == typeZone.normal;
    }
    
    public boolean estInnonde() {
    	return z == typeZone.innonde;
    }
}    
/** Fin de la classe Zone, et du modèle en général. */