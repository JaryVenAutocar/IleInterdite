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
	
	private int nbActions = 0;
	private int nbArtefacts = 0;
	private int tour = 0;
	
	/** Permettent de finir la partie (gagner ou perdre) des qu'ils passent a true */
	private boolean partieGagnee = false;
	private boolean partiePerdue = false;
	
	/** On fixe la taille de la grille. */
	static final int COTE = 21;
	
    /** On stocke un tableau de Zones. */
    private Zone[][] Zones;
    
    /** Coordonnees des zones speciales pour les artefacts */
    private int xAir = 1+new Random().nextInt(((COTE+1)/2)-1);
    private int yAir = 1+new Random().nextInt(((COTE+1)/2)-1);
    private int xTerre = ((COTE+1)/2)+1 + new Random().nextInt(((COTE+1)/2)-2);
    private int yTerre = 1+new Random().nextInt(((COTE+1)/2)-2);
    private int xEau = 1+new Random().nextInt(((COTE+1)/2)-1);
    private int yEau = ((COTE+1)/2)+1 + new Random().nextInt(((COTE+1)/2)-2);
    private int xFeu = ((COTE+1)/2)+1 + new Random().nextInt(((COTE+1)/2)-2);
    private int yFeu = ((COTE+1)/2)+1 + new Random().nextInt(((COTE+1)/2)-2);
    
    /** Declaration des zones speciales */
    private Zone air = new Zone(this, false, false, xAir, yAir , typeZone.air);
    private Zone terre = new Zone(this, false, false, xTerre, yTerre , typeZone.terre);
    private Zone eau = new Zone(this, false, false, xEau, yEau , typeZone.eau);
    private Zone feu = new Zone(this, false, false, xFeu, yFeu, typeZone.feu);
    private Zone heliport = new Zone(this, false, false, (COTE+1)/2, COTE, typeZone.heliport);

    /** Declaration des trois joueurs de la partie */
    private Joueur j1 = new Joueur((COTE+1)/2, 1, role.normal);
    private Joueur j2 = new Joueur(COTE, (COTE+1)/2, role.normal);
    private Joueur j3 = new Joueur(1, (COTE+1)/2, role.normal);
    /** Permet d'alterner les 3 joueurs, initialise a J1 pour dire que le J1 commence le jeu (celui en haut) */
    private Joueur j = j1;
    
    private typeZone ancienneZone, prochaineZone;
    Random getKey = new Random();
    Joueur[] tabJoueurs = {j1, j2, j3, j1, j2};
    
    
    /** Construction : on initialise un tableau de Zones
     * puis on affecte un role special different a chaque joueur 
     */
    public CModele() {
	/**
	 * Pour éviter les problèmes aux bords, on ajoute une ligne et une
	 * colonne, dont les Zones n'évolueront pas.
	 */ 
		Zones = new Zone[COTE+2][COTE+2];
		for(int i=0; i<COTE+2; i++) {
			for(int j=0; j<COTE+2; j++) {
	    	Zones[i][j] = new Zone(this, false, getKey.nextBoolean(), i, j, typeZone.normal);
	    	}
		}
		Zones[j1.getX()][j1.getY()] = new Zone(this, true, getKey.nextBoolean(), j1.getX(), j1.getY(), typeZone.normal);
		Zones[j2.getX()][j2.getY()] = new Zone(this, true, getKey.nextBoolean(), j2.getX(), j2.getY(), typeZone.normal);
		Zones[j3.getX()][j3.getY()] = new Zone(this, true, getKey.nextBoolean(), j3.getX(), j3.getY(), typeZone.normal);
		Zones[(COTE+1)/2][COTE] = heliport;
		Zones[xAir][yAir] = air;
		Zones[xTerre][yTerre] = terre;
		Zones[xEau][yEau] = eau;
		Zones[xFeu][yFeu] = feu;
		choixRole();
    }
    
    /**
     * Methode permettant d'initialiser un role aleatoire a chaque joueur
     * Chaque role n'est represente qu'une seule fois (= chaque joueur a un role different)
     */
    public void choixRole() {
    	final ArrayList<role> roles = new ArrayList<role>();
    	roles.add(role.ingenieur); roles.add(role.messager); roles.add(role.plongeur);
    	Random rand = new Random();
    	int randomInt = rand.nextInt(roles.size());
    	
    	for(int i = 0; i < 2; i++) {
    		tabJoueurs[i].setRole(roles.get(randomInt));
    		roles.remove(randomInt);
    		randomInt = rand.nextInt(roles.size());
    	}
    	//en dehors de la boucle car roles.size() doit etre strictement positif et roles serait vide a la
    	//fin du troisieme tour de boucle si nous avions fait le cas du j3 dedans, roles.size() serait nul
    	j3.setRole(roles.get(0));
    }
    
    
    /**
     * Methode permettant d'innonder ou de submerger 3 zones aleatoires a la fin d'un tour
     * @return le tableau des Zones dont 3 cases ont ete modifiees
     */
    protected Zone[][] evalue() {
    	int k = 0;
    	while(k < 3) {
    		// a=1 et b=1 pour éviter que les cases aléatoires soient celles des bordures hors cadre
        	int a = new Random().nextInt(COTE+1); if (a == 0) a = 1; 
        	int b = new Random().nextInt(COTE+1); if (b == 0) b = 1;
        	if(Zones[a][b].getTypeZone() == typeZone.submerge) k+= 0;
        	else {
        		if(Zones[a][b].getTypeZone() == typeZone.normal) Zones[a][b].setTypeZone(typeZone.innonde);		
        		else if(Zones[a][b].isJoueur() || Zones[a][b].getTypeZone() == typeZone.joueur) Zones[a][b].setTypeZone(typeZone.innonde);
        		else if(Zones[a][b].getTypeZone() == typeZone.air) Zones[a][b].setTypeZone(typeZone.innonde);
        		else if(Zones[a][b].getTypeZone() == typeZone.eau) Zones[a][b].setTypeZone(typeZone.innonde);
        		else if(Zones[a][b].getTypeZone() == typeZone.feu) Zones[a][b].setTypeZone(typeZone.innonde);
        		else if(Zones[a][b].getTypeZone() == typeZone.terre) Zones[a][b].setTypeZone(typeZone.innonde);
        		else if(Zones[a][b].getTypeZone() == typeZone.heliport) Zones[a][b].setTypeZone(typeZone.innonde);
        		else if(Zones[a][b].getTypeZone() == typeZone.innonde) Zones[a][b].setTypeZone(typeZone.submerge);
            	else Zones[a][b].setTypeZone(typeZone.submerge);
        		k++;
        	}	      	
        }
        return Zones;
    }
    
    
    /**
     * Methode permettant d'avancer le jeu d'un tour en actualisant la partie
     */
    public void avance() {
		evalue();
		addKey(j);
		tour+=1;
	/** 
	 Pour finir, le modèle ayant changé, 
	 on signale aux observateurs qu'ils doivent se mettre à jour.
	**/
		notifyObservers();
    }
    
    
    /**
     * Methode permettant d'obtenir un element aleatoire parmis tous ceux dans l'enum element
     * @return un element aleatoire
     */
    public element getRandomElement() {
    	Random random = new Random();
    	element randomElement = element.values()[random.nextInt(element.values().length)];
    	System.out.println("Le joueur " + ((this.tour)%3 + 1) + " recoit la cle " + randomElement + " !");
    	return randomElement;
    	}
    
    
    /**
     * Methode permettant d'ajouter, avec 35% de chances, une cle d'un element aleatoire a un joueur
     * @param j le joueur auquel on veut rajouter une cle
     */
    public void addKey(Joueur j) {
    	if(Math.random() <= 0.35 && partiePerdue == false) {
    		Key k = new Key(getRandomElement());
    		j.addKey(k);
    	}
    }
    
    
    /**
     * Methode permettant reellement de deplacer un joueur d'une zone a gauche
     * Celle ci doit etre appelee par la methode gauche() pour que le deplacement ait lieu
     * @param x abscisse du joueur qu'on va deplacer
     * @param y ordonnee du joueur qu'on va deplacer
     */
    public void MoveGauche(int x, int y) {
    	
    	/** L'ancienne zone ou se trouvait le joueur redevient la zone qu'elle etait avant son arrivee */
    	Zones[x][y] = new Zone(this, false, getKey.nextBoolean(), x, y, ancienneZone);

    	if(x == 1) x+=1;
    	
    	/** La nouvelle zone où se trouve le joueur devient une zone joueur */
    	Zones[x-1][y] = new Zone(this, true, getKey.nextBoolean(), x-1, y, prochaineZone);
    	j.setX(x-1);
	}
    
    
    /**
     * Cette methode permet de déplacer un joueur d'une zone a gauche en appelant moveGauche
     * tout en respectant les contraintes de role et de deplacements si une zone est submergee
     **/
    public void gauche() {
    	ancienneZone = Zones[j.getX()][j.getY()].getTypeZone();
    	prochaineZone = Zones[j.getX() - 1][j.getY()].getTypeZone();
    	
	    if(Zones[j.getX() - 1][j.getY()].getTypeZone() == typeZone.submerge) {
	    	if(j.getRole() != role.plongeur)
	    		System.out.println("la zone est submergée et est donc bloquée");
	    	else {
	    		MoveGauche(j.getX(), j.getY());
	    		System.out.println("la zone est submergée mais vous etes plongeur, la voie vous est libre !");
	    	}
	    } else {
    		MoveGauche(j.getX(), j.getY());
    		if(compteJoueurSurZone(j.getX() + 1, j.getY()) >= 1)
    			Zones[j.getX() + 1][j.getY()].setEstJoueur(true);
    	}
    	nbActions+=1;
    	notifyObservers();
    }
    
    
    /**
     * Methode permettant reellement de deplacer un joueur d'une zone a droite
     * Celle ci doit etre appelee par la methode droite() pour que le deplacement ait lieu
     * @param x abscisse du joueur qu'on va deplacer
     * @param y ordonnee du joueur qu'on va deplacer
     */
    public void MoveDroite(int x, int y) {
    	Zones[x][y] = new Zone(this, false, getKey.nextBoolean(), x, y, ancienneZone);
    	
    	if(x == COTE) x-=1;
    	
    	Zones[x+1][y] = new Zone(this, true, getKey.nextBoolean(), x+1, y, prochaineZone);
    	j.setX(x+1);
	}
    
    
    /**
     * Cette methode permet de déplacer un joueur d'une zone a droite en appelant moveDroite
     * tout en respectant les contraintes de role et de deplacements si une zone est submergee
     **/
    public void droite() {
    	ancienneZone = Zones[j.getX()][j.getY()].getTypeZone();
    	prochaineZone = Zones[j.getX() + 1][j.getY()].getTypeZone();
    	
	    if(Zones[j.getX() + 1][j.getY()].getTypeZone() == typeZone.submerge) {
	    	if(j.getRole() != role.plongeur)
	    		System.out.println("la zone est submergée et est donc bloquée");
	    	else {
	    		MoveDroite(j.getX(), j.getY());
	    		System.out.println("la zone est submergée mais vous etes plongeur, la voie vous est libre !");
	    	}
	    } else {
    		MoveDroite(j.getX(), j.getY());
    		if(compteJoueurSurZone(j.getX() - 1, j.getY()) >= 1)
    			Zones[j.getX() - 1][j.getY()].setEstJoueur(true);
    	}
    	nbActions+=1;
    	notifyObservers();
    }
    
    
    /**
     * Methode permettant reellement de deplacer un joueur d'une zone en bas
     * Celle ci doit etre appelee par la methode bas() pour que le deplacement ait lieu
     * @param x abscisse du joueur qu'on va deplacer
     * @param y ordonnee du joueur qu'on va deplacer
     */
    public void MoveBas(int x, int y) {
    	Zones[x][y] = new Zone(this, false, getKey.nextBoolean(), x, y, ancienneZone);
    	
    	if(y == COTE) y-=1;
    	
    	Zones[x][y+1] = new Zone(this, true, getKey.nextBoolean(), x, y+1, prochaineZone);
    	j.setY(y+1);
	}
    
    
    /**
     * Cette methode permet de déplacer un joueur d'une zone en bas en appelant moveBas
     * tout en respectant les contraintes de role et de deplacements si une zone est submergee
     **/
    public void bas() {
    	ancienneZone = Zones[j.getX()][j.getY()].getTypeZone();
    	prochaineZone = Zones[j.getX()][j.getY() + 1].getTypeZone();
    	
	    if(Zones[j.getX()][j.getY()+1].getTypeZone() == typeZone.submerge) {
	    	if(j.getRole() != role.plongeur)
	    		System.out.println("la zone est submergée et est donc bloquée");
	    	else {
	    		MoveBas(j.getX(), j.getY());
	    		System.out.println("la zone est submergée mais vous etes plongeur, la voie vous est libre !");	
	    	}
	    } else {
    		MoveBas(j.getX(), j.getY());
    		if(compteJoueurSurZone(j.getX(), j.getY() - 1) >= 1)
    			Zones[j.getX()][j.getY() - 1].setEstJoueur(true);
    	}
    	nbActions+=1;
    	notifyObservers();
    }
    
    
    /**
     * Methode permettant reellement de deplacer un joueur d'une zone en haut
     * Celle ci doit etre appelee par la methode haut() pour que le deplacement ait lieu
     * @param x abscisse du joueur qu'on va deplacer
     * @param y ordonnee du joueur qu'on va deplacer
     */
    public void MoveHaut(int x, int y) {
    	Zones[x][y] = new Zone(this, false, getKey.nextBoolean(), x, y, ancienneZone);
    	
    	if(y == 1) y+=1;
    	
    	Zones[x][y-1] = new Zone(this, true, getKey.nextBoolean(), x, y-1, prochaineZone);
    	j.setY(y-1);
	}
    
    
    /**
     * Cette methode permet de déplacer un joueur d'une zone en haut en appelant moveHaut
     * tout en respectant les contraintes de role et de deplacements si une zone est submergee
     **/
    public void haut() {
    	ancienneZone = Zones[j.getX()][j.getY()].getTypeZone();
    	prochaineZone = Zones[j.getX()][j.getY() - 1].getTypeZone();
    	
	    if(Zones[j.getX()][j.getY()-1].getTypeZone() == typeZone.submerge) {
	    	if(j.getRole() != role.plongeur)
	    		System.out.println("la zone est submergée et est donc bloquée");
	    	else {
	    		MoveHaut(j.getX(), j.getY());
	    		System.out.println("la zone est submergée mais vous etes plongeur, la voie vous est libre !");
	    	}
	    } else {
    		MoveHaut(j.getX(), j.getY());
    		if(compteJoueurSurZone(j.getX(), j.getY() + 1) >= 1)
    			Zones[j.getX()][j.getY() + 1].setEstJoueur(true);
    	}
    	nbActions+=1;
    	notifyObservers();
    }
    
    
    /**
     * Une methode pour compter le nombre de Zones inondees adjacentes
     * a la Zone aux coordonnees x et y en parametres 
     * @param x abscisse de la Zone
     * @param y ordonnee de la Zone
     * @return res un entier representant le nombre de Zones inondees adjacentes
     */
    public int compteZoneInnonde(int x, int y) {
    	int res = 0;
    	if(getZone(x + 1, y).getTypeZone() == typeZone.innonde) res +=1;
    	if(getZone(x - 1, y).getTypeZone() == typeZone.innonde) res +=1;
    	if(getZone(x, y + 1).getTypeZone() == typeZone.innonde) res +=1;
    	if(getZone(x, y - 1).getTypeZone() == typeZone.innonde) res +=1;
    	return res;
    }
    
    /**
     * Une methode pour compter le nombre de Zones submergees adjacentes
     * a la Zone aux coordonnees x et y en parametres 
     * @param x abscisse de la Zone
     * @param y ordonnee de la Zone
     * @return res un entier representant le nombre de Zones submergees adjacentes
     */
    public int compteZoneSubmerge(int x, int y) {
    	int res = 0;
    	if(getZone(x + 1, y).getTypeZone() == typeZone.submerge) res +=1;
    	if(getZone(x - 1, y).getTypeZone() == typeZone.submerge) res +=1;
    	if(getZone(x, y + 1).getTypeZone() == typeZone.submerge) res +=1;
    	if(getZone(x, y - 1).getTypeZone() == typeZone.submerge) res +=1;
    	return res;
    }
    
    
    /**
     * Methode permettant d'assecher une zone adjacente au joueur
     * C'est a dire qu'une zone inondee redevient ce qu'elle etait avant d'etre inondee
     * Si le joueur a le role special "ingenieur", il peut en assecher 2 en meme temps
     */
    public void asseche() {
    	
    	/** 
     	Deux tableaux pour pouvoir acceder a notre guise aux Zone et aux typeZone
    	selon nos besoins et selon les types des parametres des fonctions utilisees
    	Ceux ci sont volontairement disposes dans le meme ordre 	
    	**/
    	Zone[] ensZones = {heliport, air, eau, terre, feu};
    	typeZone[] ensTypeZone = {heliport.getTypeZone(), air.getTypeZone(), eau.getTypeZone(), terre.getTypeZone(), feu.getTypeZone()};
    	
    	/**stockage des typeZone de depart des zones speciales pour y acceder meme si leur typeZone change */
    	typeZone tempAir = typeZone.air; typeZone tempFeu = typeZone.feu;
    	typeZone tempEau = typeZone.eau; typeZone tempTerre = typeZone.terre; typeZone tempHeliport = typeZone.heliport;
    	
    	int voisinsInnondesAvantBoucle = compteZoneInnonde(j.getX(), j.getY());
    	
    	for(int i = 0; i < ensZones.length; i++) {
    		if(i == 0) ensTypeZone[i] = tempHeliport;
    		else if(i == 1) ensTypeZone[i] = tempAir;
    		else if(i == 2) ensTypeZone[i] = tempEau;
    		else if(i == 3) ensTypeZone[i] = tempTerre;
    		else if(i == 4) ensTypeZone[i] = tempFeu;
    		
    		/** On fait tous les cas avec les zones speciales pour les zones adjacentes */
    		if(getZone(j.getX(), j.getY()).getTypeZone() == typeZone.innonde) {
    			if(j.getX() == ensZones[i].getX() && j.getY() == ensZones[i].getY())
    				getZone(j.getX(), j.getY()).setTypeZone(ensTypeZone[i]); 
    		}
    			
    		else if(getZone(j.getX() + 1, j.getY()).getTypeZone() == typeZone.innonde) {
    			if((j.getX() + 1) == ensZones[i].getX() && j.getY() == ensZones[i].getY())
    				getZone(j.getX() + 1, j.getY()).setTypeZone(ensTypeZone[i]);
    		}    	    
    		
	    	else if(getZone(j.getX() - 1, j.getY()).getTypeZone() == typeZone.innonde) {
	    		if((j.getX() - 1) == ensZones[i].getX() && j.getY() == ensZones[i].getY())
	    			getZone(j.getX() - 1, j.getY()).setTypeZone(ensTypeZone[i]);
	    	}	    		
	    	
	    	else if(getZone(j.getX(), j.getY() + 1).getTypeZone() == typeZone.innonde) {
	    		if(j.getX() == ensZones[i].getX() && (j.getY() + 1) == ensZones[i].getY())
	    			getZone(j.getX(), j.getY() + 1).setTypeZone(ensTypeZone[i]);
	    	}	    		
	    	
	    	else if(getZone(j.getX(), j.getY() - 1).getTypeZone() == typeZone.innonde) {
	    		if(j.getX() == ensZones[i].getX() && (j.getY() - 1) == ensZones[i].getY())
	    			getZone(j.getX(), j.getY() - 1).setTypeZone(ensTypeZone[i]);
	    	}
    	}
    	int voisinsInnondesApresBoucle = compteZoneInnonde(j.getX(), j.getY());
    	
    	/** on rentre dans cette boucle que s'il ne s'est rien passe avec le code ci dessus 
    	 * et on refait tous les cas avec les zones normales pour les zones adjacentes 
    	 */
    	if(voisinsInnondesAvantBoucle == voisinsInnondesApresBoucle) {
	    	if(getZone(j.getX(), j.getY()).getTypeZone() == typeZone.innonde) {
	    			getZone(j.getX(), j.getY()).setTypeZone(typeZone.normal);
	    	}
	    	else if(getZone(j.getX() + 1, j.getY()).getTypeZone() == typeZone.innonde) 
	        	getZone(j.getX() + 1, j.getY()).setTypeZone(typeZone.normal);
	        else if(getZone(j.getX() - 1, j.getY()).getTypeZone() == typeZone.innonde)
	        	getZone(j.getX() - 1, j.getY()).setTypeZone(typeZone.normal);
	        else if(getZone(j.getX(), j.getY() + 1).getTypeZone() == typeZone.innonde)
	        	getZone(j.getX(), j.getY() + 1).setTypeZone(typeZone.normal);  	
	        else if(getZone(j.getX(), j.getY() - 1).getTypeZone() == typeZone.innonde)
	        	getZone(j.getX(), j.getY() - 1).setTypeZone(typeZone.normal);
    	}
    	nbActions+=1;
    	notifyObservers();
    }
    
    
    /**
     * Methode permettant de recuperer un artefact si le joueur est situe sur une zone adjacente
     * a l'artefact et s'il possede au moins 4 cles de celui ci
     */
    public void recupArtefact() {
    	
    	/** 
     	Deux tableaux pour pouvoir acceder a notre guise aux typeZone et aux elements
    	selon nos besoins et selon les types des parametres des fonctions utilisees
    	Ceux ci sont volontairement disposes dans le meme ordre 	
    	**/
    	typeZone[] ensTypeZone = {typeZone.air, typeZone.eau, typeZone.terre, typeZone.feu};
    	element[] ensElement = {element.air, element.eau, element.terre, element.feu};
    	
    	for(int i = 0; i < ensTypeZone.length; i++) {
    		
    		/** On fait tous les cas pour chaque zone adjacente au joueur */
    		if(getZone(j.getX() + 1, j.getY()).getTypeZone() == ensTypeZone[i] && j.nbKeyOfArtefact(ensElement[i]) >= 4) {
        	    getZone(j.getX() + 1, j.getY()).setTypeZone(typeZone.normal); 
        	    nbArtefacts+=1;
    		}
    		
	    	else if(getZone(j.getX() - 1, j.getY()).getTypeZone() == ensTypeZone[i] && j.nbKeyOfArtefact(ensElement[i]) >= 4) {
	    		getZone(j.getX() - 1, j.getY()).setTypeZone(typeZone.normal);
	    		nbArtefacts+=1;
	    	}
	    	
	    	else if(getZone(j.getX(), j.getY() + 1).getTypeZone() == ensTypeZone[i] && j.nbKeyOfArtefact(ensElement[i]) >= 4) {
	    		getZone(j.getX(), j.getY() + 1).setTypeZone(typeZone.normal);
	    		nbArtefacts+=1;
	    	}
	    	
	    	else if(getZone(j.getX(), j.getY() - 1).getTypeZone() == ensTypeZone[i] && j.nbKeyOfArtefact(ensElement[i]) >= 4) {
	    		getZone(j.getX(), j.getY() - 1).setTypeZone(typeZone.normal);
	    		nbArtefacts+=1;
	    	}
    	}
    	nbActions+=1;
    	System.out.println("le nb d'artefacts est : " + nbArtefacts);
		notifyObservers();
    }
    
    
    /**
     * Methode qui compte le nombre de joueurs presents sur la Zone aux coordonnees x et y en parametres
     * @param x abscisse de la Zone
     * @param y ordonnee de la Zone
     * @return res l'entier indiquant le nombre de joueurs sur la Zone de coordonnees (x, y)
     */
    public int compteJoueurSurZone(int x, int y) {
    	int res = 0;
    	if(j1.getX() == x && j1.getY() == y) res+=1;
    	if(j2.getX() == x && j2.getY() == y) res+=1;
    	if(j3.getX() == x && j3.getY() == y) res+=1;
    	return res;
    }
    
    
    /**
     * Une methode pour recuperer une cle si le joueur appuie sur le bouton "Recuperer une cle"
     * Si la zone du joueur ne possede pas de cle, soit il ne se passe rien soit la zone s'innonde
     * Si la zone possede une cle, le joueur a 90% de chance de la recuperer
     */
    public void recupKey() {
    	
    	/** Si joueur n'est pas sur une zone qui contient une cle
    	Cette variable va permettre de trancher sur le fait de declencher la montee des eaux ou ne rien faire */
    	double innondeOuNormal = Math.random();
    	
    	/** Si le joueur est sur une zone qui contient une cle, nous ne voulons pas qu'il soit sur a 100%
    	d'obtenir une cle d'ou le random. Il a 90% de chance d'obtenir une cle si la zone en contient une */
    	if(Math.random() <= 0.90 && getZone(j.getX(), j.getY()).isKey()) {
    		Key k = new Key(getRandomElement());
    		j.addKey(k);
    		getZone(j.getX(), j.getY()).setKey(false);
    		System.out.println("Elle a maintenant disparu de la zone qui ne possede donc plus de cle pour le moment. Revenez plus tard ! :)");
    	}
    	else if(innondeOuNormal <= 0.50){
    		getZone(j.getX(), j.getY()).setTypeZone(typeZone.innonde);
    		System.out.println("Le joueur " + ((this.tour)%3 + 1) + " a cherche une cle et n'aurait pas du ! Sa zone s'innonde.");
    	}
    	else if(innondeOuNormal > 0.50)
    		System.out.println("Le joueur " + ((this.tour)%3 + 1) + " a cherche une cle et n'a rien trouve, il ne se passe rien.");
    	
    	nbActions+=1;
    	notifyObservers();
    }
    
    
    /**
     * Methode permettant de donner une cle a un joueur sur la meme zone
     * Si le role du joueur qui joue est messager, il peut donner une cle a un autre joueur qui
     * est sur une autre zone n'importe ou sur la carte
     */
    public void giveKey() {
    	Random rand = new Random();
    	for(int i = 0; i < 3; i++) {
    		
    		/** 
    		 * Pour chaque joueur, on verifie si son role est messager et s'il possede des cles
    		 * Si c'est le cas, il peut donner une cle a un des deux autres joueurs, aleatoirement
    		 * La cle qu'il donne est retiree de son inventaire de cles
    		 */
    		if((this.tour)%3 == i && tabJoueurs[i].getRole() == role.messager && !tabJoueurs[i].keyList.isEmpty()) {
    			System.out.println("Vous etes le messager, vous avez envoye une cle a un de vos coequipiers ! Quel altruisme ;)");
    			int randomInt = rand.nextInt(j.keyList.size());
    			Key temp = tabJoueurs[i].keyList.get(randomInt);
    			if(Math.random() <= 0.50) {
    				tabJoueurs[i].keyList.remove(randomInt);
	    			tabJoueurs[i+1].keyList.add(temp);
	    			if(i < 2)
	    				System.out.println("Le joueur " + (i+2) + " a recu la cle " + temp.getElement() + " du joueur " + (i+1));
	    			else
	    				System.out.println("Le joueur 1 a recu la cle " + temp.getElement() + " du joueur 3");
    			} else {
    				tabJoueurs[i].keyList.remove(randomInt);
	    			tabJoueurs[i+2].keyList.add(temp);
	    			if(i == 0)
	    				System.out.println("Le joueur 3 a recu la cle " + temp.getElement() + " du joueur 1");
	    			else if(i >= 1)
	    				System.out.println("Le joueur " + i + " a recu la cle " + temp.getElement() + " du joueur " + (i+1));
    			}
    		}
    	
    		
    		/**
    		 * Pour chaque joueur, on verifie s'il y a plusieurs joueurs sur la zone ou il se situe
    		 * Si c'est le cas, il peut donner une cle au joueur sur sa zone
    		 */
    		else if((this.tour)%3 == i && compteJoueurSurZone(tabJoueurs[i].getX(), tabJoueurs[i].getY()) > 1) {
		    	
    			/** Possible de donner que si le joueur possede au moins une cle */
	    		if(!tabJoueurs[i].keyList.isEmpty()) {
	    			int randomInt = rand.nextInt(j.keyList.size());
	    			Key temp = tabJoueurs[i].keyList.get(randomInt);
		    		if(getZone(tabJoueurs[i].getX(), tabJoueurs[i].getY()) == getZone(tabJoueurs[i+1].getX(), tabJoueurs[i+1].getY())) {
		    			tabJoueurs[i].keyList.remove(randomInt);
		    			tabJoueurs[i+1].keyList.add(temp);
		    			if(i < 2)
		    				System.out.println("Le joueur " + (i+2) + " a recu la cle " + temp.getElement() + " du joueur " + (i+1));
		    			else
		    				System.out.println("Le joueur 1 a recu la cle " + temp.getElement() + " du joueur 3");
		    		}
		    		else if(getZone(tabJoueurs[i].getX(), tabJoueurs[i].getY()) == getZone(tabJoueurs[i+2].getX(), tabJoueurs[i+2].getY())) {
		    			tabJoueurs[i].keyList.remove(randomInt);
		    			tabJoueurs[i+2].keyList.add(temp);
		    			if(i == 0)
		    				System.out.println("Le joueur 3 a recu la cle " + temp.getElement() + " du joueur 1");
		    			else if(i >= 1)
		    				System.out.println("Le joueur " + i + " a recu la cle " + temp.getElement() + " du joueur " + (i+1));
		    		}
	    		}
	    		else
	    			System.out.println("Le joueur " + (i+1) + " n'a pas de cle a donner");
	    	}
    	}
    	nbActions+=1;
    	notifyObservers();
    }

    
    /**
     * Une methode pour renvoyer la Zone aux coordonnees x et y en parametres (sera
     * utilisee par la vue).
     * @param x abscisse de la Zone
     * @param y ordonnee de la Zone
     * @return la Zone de coordonnes (x,y)
     */
    public Zone getZone(int x, int y) {
    	return Zones[x][y];
    }

    /** Methode permettant de recuperer la zone air */
	public Zone getAir() {
		return air;
	}
	
	
	/** Methode permettant de recuperer la zone terre */
	public Zone getTerre() {
		return terre;
	}

	
	/** Methode permettant de recuperer la zone eau */
	public Zone getEau() {
		return eau;
	}

	
	/** Methode permettant de recuperer la zone feu */
	public Zone getFeu() {
		return feu;
	}

	
	/** Methode permettant de recuperer la zone heliport */
	public Zone getHeliport() {
		return heliport;
	}

	
	/** Methode permettant de savoir si la partie est gagnee */
	public boolean isGagnee() {
		return partieGagnee;
	}

	
	/** Methode permettant de savoir si la partie est perdue */
	public boolean isPerdue() {
		return partiePerdue;
	}

	
	/** Methode permettant de modifier l'etat de la variable partieGagnee */
	public void setPartieGagnee(boolean partieGagnee) {
		this.partieGagnee = partieGagnee;
	}
	
	
	/** Methode permettant de modifier l'etat de la variable partiePerdue */
	public void setPartiePerdue(boolean partiePerdue) {
		this.partiePerdue = partiePerdue;
	}

	
	/** Methode permettant de recuperer le nombre d'actions disponibles pour le joueur */
	public int getNbActions() {
		return nbActions;
	}

	
	/** Methode permettant de modifier le nombre d'actions disponibles pour le joueur */
	public void setNbActions(int nbActions) {
		this.nbActions = nbActions;
	}

	
	/** Methode permettant de recuperer le nombre d'artefacts en la possession des joueurs */
	public int getNbArtefacts() {
		return nbArtefacts;
	}

	
	/** Methode permettant de recuperer le tour actuel de la partie */
	public int getTour() {
		return tour;
	}

	
	/** Methode permettant de recuperer le Joueur 1 */
	public Joueur getJ1() {
		return j1;
	}


	/** Methode permettant de recuperer le Joueur 2 */
	public Joueur getJ2() {
		return j2;
	}


	/** Methode permettant de recuperer le Joueur 3 */
	public Joueur getJ3() {
		return j3;
	}

	
	/** Methode permettant de recuperer le Joueur actuel */
	public Joueur getJ() {
		return j;
	}

	
	/** Methode permettant de modifier le Joueur actuel */
	public void setJ(Joueur j) {
		this.j = j;
	}	
}