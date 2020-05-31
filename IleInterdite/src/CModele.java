import java.util.*;
import java.lang.Math;

/**
 * Le modèle : le coeur de l'application.
 *
 * Le modèle étend la classe [Observable] : il va posséder un certain nombre
 * d'observateurs (ici, un : la partie de la vue responsable de l'affichage)
 * et devra les prévenir avec [notifyObservers] lors des modifications.
 * Voir la méthode [avance()] pour cela.
 */
class CModele extends Observable {
	/** On fixe la taille de la grille. */
    // static final int COTE=20, COTE=20;
	public VueCommandes commandes;
	public Controleur ctrl;
	public int nbActions = 0;
	public int nbArtefacts = 0;
	public int tour = 0;
	public boolean partieGagnee = false;
	public boolean partiePerdue = false;
	static final int COTE = 21;
    /** On stocke un tableau de Zones. */
    private Zone[][] Zones;
    public Joueur j1 = new Joueur((COTE+1)/2, 1);
    public Joueur j2 = new Joueur(COTE, (COTE+1)/2);
    public Joueur j3 = new Joueur(1, (COTE+1)/2);
    public Zone heliport = new Zone(this, false, (COTE+1)/2, COTE, typeZone.heliport);
    
    private int xair = 1+new Random().nextInt(((COTE+1)/2)-1);
    private int yair = 1+new Random().nextInt(((COTE+1)/2)-1);
    
    private int xterre = ((COTE+1)/2)+1 + new Random().nextInt(((COTE+1)/2)-2);
    private int yterre = 1+new Random().nextInt(((COTE+1)/2)-2);
    
    private int xeau = 1+new Random().nextInt(((COTE+1)/2)-1);
    private int yeau = ((COTE+1)/2)+1 + new Random().nextInt(((COTE+1)/2)-2);
    
    private int xfeu = ((COTE+1)/2)+1 + new Random().nextInt(((COTE+1)/2)-2);
    private int yfeu = ((COTE+1)/2)+1 + new Random().nextInt(((COTE+1)/2)-2);
    
    public Zone air = new Zone(this, false, xair, yair , typeZone.air);
    public Zone terre = new Zone(this, false, xterre, yterre , typeZone.terre);
    public Zone eau = new Zone(this, false, xeau, yeau , typeZone.eau);
    public Zone feu = new Zone(this, false, xfeu, yfeu, typeZone.feu);
    //Permet d'alterner les 3 joueurs, initialisé à J1 pour dire que le J1 commence le jeu (celui en haut)
    public Joueur j = j1;
    public typeZone ancienneZone, prochaineZone;

    
    
    
    /** Construction : on initialise un tableau de Zones. */
    public CModele() {
	/**
	 * Pour éviter les problèmes aux bords, on ajoute une ligne et une
	 * colonne, dont les Zones n'évolueront pas.
	 */ 
		Zones = new Zone[COTE+2][COTE+2];
		for(int i=0; i<COTE+2; i++) {
			for(int j=0; j<COTE+2; j++) {
	    	Zones[i][j] = new Zone(this, false, i, j, typeZone.normal);
	    	}
		}
		Zones[j1.getX()][j1.getY()] = new Zone(this, true, j1.getX(), j1.getY(), typeZone.normal);
		Zones[j2.getX()][j2.getY()] = new Zone(this, true, j2.getX(), j2.getY(), typeZone.normal);
		Zones[j3.getX()][j3.getY()] = new Zone(this, true, j3.getX(), j3.getY(), typeZone.normal);
		Zones[(COTE+1)/2][COTE] = heliport;
		Zones[xair][yair] = air;
		Zones[xterre][yterre] = terre;
		Zones[xeau][yeau] = eau;
		Zones[xfeu][yfeu] = feu;
    }

    
    
   /**  
    * Pour tester la fonction evalue
    * 
     public void compteTypeZone() {	
    	int resInnonde = 0;
    	int resSubmerge = 0;
    	int resNormale = 0;
    	for(int i=0; i<COTE+2; i++) {
			for(int j=0; j<COTE+2; j++) {
				if (Zones[i][j].z == typeZone.normal) resNormale +=1;
				if (Zones[i][j].z == typeZone.innonde) resInnonde +=1;
				if (Zones[i][j].z == typeZone.submerge) resSubmerge +=1;
			}
    	}
    	System.out.println("il y a " + resNormale + " cases normales ");
    	System.out.println("il y a " + resInnonde + " cases innondees ");
    	System.out.println("il y a " + resSubmerge + " cases submergees ");
    } **/
    
    
    
    protected Zone[][] evalue() {  	
        
    	int k = 0;
    	
    	while(k < 3) {
        	int a = new Random().nextInt(COTE+1); if (a == 0) a = 1; // =1 Pour éviter que les cases aléatoires soient celles des bordures hors cadre
        	int b = new Random().nextInt(COTE+1); if (b == 0) b = 1;
        	
        	if(Zones[a][b].z == typeZone.submerge) k+= 0;
        	else {
        		if(Zones[a][b].z == typeZone.normal) Zones[a][b].z = typeZone.innonde;		
        		else if(Zones[a][b].estJoueur) Zones[a][b].z = typeZone.innonde;
        		else if(Zones[a][b].z == typeZone.air) Zones[a][b].z = typeZone.innonde;
        		else if(Zones[a][b].z == typeZone.eau) Zones[a][b].z = typeZone.innonde;
        		else if(Zones[a][b].z == typeZone.feu) Zones[a][b].z = typeZone.innonde;
        		else if(Zones[a][b].z == typeZone.terre) Zones[a][b].z = typeZone.innonde;
        		else if(Zones[a][b].z == typeZone.heliport) Zones[a][b].z = typeZone.innonde;
        		else if(Zones[a][b].z == typeZone.innonde) Zones[a][b].z = typeZone.submerge;
            	else Zones[a][b].z = typeZone.submerge;
        		k++;
        	}	      	
        }
        return Zones;
    }
    
    /**
     * Calcul de la génération suivante.
     */
    public void avance() {
	/**
	 * On procède en deux étapes.
	 *  - D'abord, pour chaque Zone on évalue ce que sera son état à la
	 *    prochaine génération.
	 *  - Ensuite, on applique les évolutions qui ont été calculées.
	 */ 
		evalue();
		addKey(j);
		//compteTypeZone();
		tour+=1;
	/**
	 * Pour finir, le modèle ayant changé, on signale aux observateurs
	 * qu'ils doivent se mettre à jour.
	 */
		notifyObservers();
    }
    
    public element getRandomElement() {
    	Random random = new Random();
    	    element randomElement = element.values()[random.nextInt(element.values().length)];
    	    System.out.println("Le joueur " + ((this.tour)%3 + 1) + " recoit la cle " + randomElement);
    	    return randomElement;
    	}
    
    public void addKey(Joueur j) {
    	if(Math.random() <= 0.35 && partiePerdue == false) {
    		Key k = new Key(getRandomElement());
    		j.addKey(k);
    		/**for(int i = 0; i < j.keyList.size(); i++)
    			System.out.println(" " + j.keyList.get(i).e); **/
    	}
    }
    
    
    
    public void MoveGauche(int x, int y) {
    	
    	//L'ancienne case qui était celle d'un joueur ne l'est plus mtn et devient normale
    	//(à changer avec ancien état : si ancienne case était innondée, mettre innondé)
    	
    	Zones[x][y] = new Zone(this, false, x, y, ancienneZone);

    	if(x == 1) x+=1;
    	
    	//La nouvelle case où se trouve le joueur devient une case joueur
    	Zones[x-1][y] = new Zone(this, true, x-1, y, prochaineZone);
    	j.setX(x-1);
	}
    
    public void gauche() {
    	ancienneZone = Zones[j.getX()][j.getY()].z;
    	prochaineZone = Zones[j.getX() - 1][j.getY()].z;
    	
    	if(Zones[j.getX() - 1][j.getY()].z == typeZone.submerge) 
    		System.out.println("la zone est submergée et est donc bloquée");
    	else
    		MoveGauche(j.getX(), j.getY());
    	nbActions+=1;
    	notifyObservers();
    }
    
    
    public void MoveDroite(int x, int y) {
    	Zones[x][y] = new Zone(this, false, x, y, ancienneZone);
    	
    	if(x == COTE) x-=1;
    	
    	Zones[x+1][y] = new Zone(this, true, x+1, y, prochaineZone);
    	j.setX(x+1);
	}
    
    public void droite() {
    	ancienneZone = Zones[j.getX()][j.getY()].z;
    	prochaineZone = Zones[j.getX() + 1][j.getY()].z;
    	
    	if(Zones[j.getX() + 1][j.getY()].z == typeZone.submerge) 
    		System.out.println("la zone est submergée et est donc bloquée");
    	else
    		MoveDroite(j.getX(), j.getY());
    	nbActions+=1;
    	notifyObservers();
    }
    
    
    public void MoveBas(int x, int y) {
    	Zones[x][y] = new Zone(this, false, x, y, ancienneZone);
    	
    	if(y == COTE) y-=1;
    	
    	Zones[x][y+1] = new Zone(this, true, x, y+1, prochaineZone);
    	j.setY(y+1);
	}
    
    public void bas() {
    	ancienneZone = Zones[j.getX()][j.getY()].z;
    	prochaineZone = Zones[j.getX()][j.getY() + 1].z;
    	
    	if(Zones[j.getX()][j.getY()+1].z == typeZone.submerge) 
    		System.out.println("la zone est submergée et est donc bloquée");
    	else
    		MoveBas(j.getX(), j.getY());
    	nbActions+=1;
    	notifyObservers();
    }
    
    
    public void MoveHaut(int x, int y) {
    	Zones[x][y] = new Zone(this, false, x, y, ancienneZone);
    	
    	if(y == 1) y+=1;
    	
    	Zones[x][y-1] = new Zone(this, true, x, y-1, prochaineZone);
    	j.setY(y-1);
	}
    
    public void haut() {
    	ancienneZone = Zones[j.getX()][j.getY()].z;
    	prochaineZone = Zones[j.getX()][j.getY() - 1].z;
    	
    	if(Zones[j.getX()][j.getY()-1].z == typeZone.submerge) 
    		System.out.println("la zone est submergée et est donc bloquée");
    	else
    		MoveHaut(j.getX(), j.getY());
    	nbActions+=1;
    	notifyObservers();
    }
    
    
    
    public int compteZoneInnonde(int x, int y) {
    	int res = 0;
    	if(getZone(x + 1, j.getY()).z == typeZone.innonde) res +=1;
    	if(getZone(x - 1, j.getY()).z == typeZone.innonde) res +=1;
    	if(getZone(x, y + 1).z == typeZone.innonde) res +=1;
    	if(getZone(x, y - 1).z == typeZone.innonde) res +=1;
    	return res;
    }
    
    public int compteZoneSubmerge(int x, int y) {
    	int res = 0;
    	if(getZone(x + 1, j.getY()).z == typeZone.submerge) res +=1;
    	if(getZone(x - 1, j.getY()).z == typeZone.submerge) res +=1;
    	if(getZone(x, y + 1).z == typeZone.submerge) res +=1;
    	if(getZone(x, y - 1).z == typeZone.submerge) res +=1;
    	return res;
    }
    
    
    public void asseche() {
    	
    	Zone[] ensZones = {air, eau, terre, feu};
    	typeZone[] ensTypeZone = {air.z, eau.z, terre.z, feu.z};
    	
    	typeZone tempAir = typeZone.air; typeZone tempFeu = typeZone.feu;
    	typeZone tempEau = typeZone.eau; typeZone tempTerre = typeZone.terre;
    	
    	int voisinsInnondesAvantBoucle = compteZoneInnonde(j.getX(), j.getY());
    	
    	for(int i = 0; i < ensZones.length; i++) {
    		
    		if(i == 0) ensTypeZone[i] = tempAir;
    		else if(i == 1) ensTypeZone[i] = tempEau;
    		else if(i == 2) ensTypeZone[i] = tempTerre;
    		else if(i == 3) ensTypeZone[i] = tempFeu;
    		
    		if(getZone(j.getX(), j.getY()).z == typeZone.innonde) {
    			if(j.getX() == ensZones[i].getX() && j.getY() == ensZones[i].getY())
    				getZone(j.getX(), j.getY()).z = ensTypeZone[i]; 
    		}
    			
    		else if(getZone(j.getX() + 1, j.getY()).z == typeZone.innonde) {
    			if((j.getX() + 1) == ensZones[i].getX() && j.getY() == ensZones[i].getY())
    				getZone(j.getX() + 1, j.getY()).z = ensTypeZone[i];
    		}    	    
    		
	    	else if(getZone(j.getX() - 1, j.getY()).z == typeZone.innonde) {
	    		if((j.getX() - 1) == ensZones[i].getX() && j.getY() == ensZones[i].getY())
	    			getZone(j.getX() - 1, j.getY()).z = ensTypeZone[i];
	    	}	    		
	    	
	    	else if(getZone(j.getX(), j.getY() + 1).z == typeZone.innonde) {
	    		if(j.getX() == ensZones[i].getX() && (j.getY() + 1) == ensZones[i].getY())
	    			getZone(j.getX(), j.getY() + 1).z = ensTypeZone[i];
	    	}	    		
	    	
	    	else if(getZone(j.getX(), j.getY() - 1).z == typeZone.innonde) {
	    		if(j.getX() == ensZones[i].getX() && (j.getY() - 1) == ensZones[i].getY())
	    			getZone(j.getX(), j.getY() - 1).z = ensTypeZone[i];
	    	}	
    	}
    	int voisinsInnondesApresBoucle = compteZoneInnonde(j.getX(), j.getY());
  	
    	if(voisinsInnondesAvantBoucle == voisinsInnondesApresBoucle) {
	    	if(getZone(j.getX(), j.getY()).z == typeZone.innonde)
				getZone(j.getX(), j.getY()).z = typeZone.normal;
	    	
	    	else if(getZone(j.getX() + 1, j.getY()).z == typeZone.innonde) 
	        	getZone(j.getX() + 1, j.getY()).z = typeZone.normal;
	    	
	        else if(getZone(j.getX() - 1, j.getY()).z == typeZone.innonde)
	        	getZone(j.getX() - 1, j.getY()).z = typeZone.normal;
	    	
	        else if(getZone(j.getX(), j.getY() + 1).z == typeZone.innonde)
	        	getZone(j.getX(), j.getY() + 1).z = typeZone.normal;
	    	
	        else if(getZone(j.getX(), j.getY() - 1).z == typeZone.innonde)
	        	getZone(j.getX(), j.getY() - 1).z = typeZone.normal;
    	}
    	
    	nbActions+=1;
    	notifyObservers();
    }
  
    public void recupArtefact() {
    	
    	for(int i = 0; i < typeZone.values().length; i++) {
    		
    		typeZone temp = typeZone.values()[i];
    		Key k = new Key(temp);
    		
    		if(getZone(j.getX() + 1, j.getY()).z == temp && j.getKeyElement(k.e)) {
        	    getZone(j.getX() + 1, j.getY()).z = typeZone.normal; 
        	    nbArtefacts+=1;
    		}
    		
	    	else if(getZone(j.getX() - 1, j.getY()).z == temp && j.getKeyElement(k.e)) {
	    		getZone(j.getX() - 1, j.getY()).z = typeZone.normal;
	    		nbArtefacts+=1;
	    	}
	    	
	    	else if(getZone(j.getX(), j.getY() + 1).z == temp && j.getKeyElement(k.e)) {
	    		getZone(j.getX(), j.getY() + 1).z = typeZone.normal;
	    		nbArtefacts+=1;
	    	}
	    	
	    	else if(getZone(j.getX(), j.getY() - 1).z == temp && j.getKeyElement(k.e)) {
	    		getZone(j.getX(), j.getY() - 1).z = typeZone.normal;
	    		nbArtefacts+=1;
	    	}
    	}
    	
    	nbActions+=1;
    	System.out.println("le nb d'artefacts est : " + nbArtefacts);
		notifyObservers();
    }
    
    


    /**
     * Méthode auxiliaire : compte le nombre de voisines vivantes d'une
     * Zone désignée par ses coordonnées.
     */
    protected int compteVoisines(int x, int y) {
    	int res=0;
	/**
	 * Stratégie simple à écrire : on compte les Zones vivantes dans le
	 * carré 3x3 centré autour des coordonnées (x, y), puis on retire 1
	 * si la Zone centrale est elle-même vivante.
	 * On n'a pas besoin de traiter à part les bords du tableau de Zones
	 * grâce aux lignes et colonnes supplémentaires qui ont été ajoutées
	 * de chaque côté (dont les Zones sont mortes et n'évolueront pas).
	 */
		for(int i=x-1; i<=x+1; i++) {
		    for(int j=y-1; j<=y+1; j++) {
		    	if (Zones[i][j].z == typeZone.normal) { 
		    		res++; 
		    	}
		    }
		}
		return res;
	/**
	 * L'expression [(c)?e1:e2] prend la valeur de [e1] si [c] vaut [true]
	 * et celle de [e2] si [c] vaut [false].
	 * Cette dernière ligne est donc équivalente à
	 *     int v;
	 *     if (Zones[x][y].etat) { v = res - 1; }
	 *     else { v = res - 0; }
	 *     return v;
	 */
    }

    /**
     * Une méthode pour renvoyer la Zone aux coordonnées choisies (sera
     * utilisée par la vue).
     */
    public Zone getZone(int x, int y) {
    	return Zones[x][y];
    }
}