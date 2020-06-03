import java.util.ArrayList;

/** tous les roles que le joueur peut prendre */
enum role {normal, ingenieur, plongeur, messager} ;

public class Joueur {

	/** coordonnees et role du joueur */
	private int x; 
	private int y;
	private role r;
	
	/** Cles que possede le joueur */
	public ArrayList<Key> keyList;
	public ArrayList<element> keyElementList;
	
	/** Construction : on initialise un joueur avec ses coordonnes et un role, et aucune cle */
	public Joueur(int x, int y, role r) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.keyList = new ArrayList<Key>();
		this.keyElementList = new ArrayList<element>();
	}

	
	/**
	 * Methode permettant d'ajouter une cle au joueur
	 * @param k la cle qu'on ajoute
	 */
	public void addKey(Key k) {
		keyList.add(k);
		keyElementList.add(k.getElement());
	}
	
	
	/**
	 * Methode permettant de savoir si un joueur possede une cle passee en parametre
	 * @param k la cle dont on veut savoir si le joueur la possede
	 * @return true si le joueur possede la cle k, false sinon
	 */
	public boolean getKey(Key k) {
		return keyList.contains(k);
	}
	
	
	/**
	 * Methode permettant de savoir si un joueur possede un element passe en parametre
	 * @param e l'element dont on veut savoir si le joueur possede une cle de cet element
	 * @return true si le joueur possede la cle de l'element e, false sinon
	 */
	public boolean getKeyElement(element e) {
		return keyElementList.contains(e);
	}
	
	
	/**
     * Methode permettant d'obtenir le nombre de cles d'un element qu'un joueur possede
     * @param e L'element dont on veut connaitre la quantite de cles de cet element que le joueur possede
     * @return res un entier representant le nombre de cles de l'element e que le joueur possede
     */
	public int nbKeyOfArtefact(element e) {
    	int res = 0;
    	for(Key k : this.keyList)
    		if(k.getElement() == e) res+=1;
    	return res;
    }
	
	
	/** Methode permettant d'afficher les cles qu'un joueur possede dans le terminal */
	public void afficheCles() {
		if(this.keyList.isEmpty())
			System.out.print("Le joueur ne possede pas de cles");
		else {
			System.out.println("Il possede les cles suivantes: ");
			for(element e: element.values()) {
				int nbKey = nbKeyOfArtefact(e);
				System.out.print(nbKey + " " + e +" ");
			}
		}
	}
	
	
	/** Methode permettant de recuperer l'abscisse du joueur */
	public int getX() {
		return x;
	}
	
	
	/** Methode permettant de recuperer l'ordonnee du joueur */
	public int getY() {
		return y;
	}
	
	
	/** Methode permettant de recuperer le role du joueur */
	public role getRole() {
		return r;
	}

	
	/** Methode permettant de modifier l'abscisse du joueur */
	public void setX(int x) {
		this.x = x;
	}

	
	/** Methode permettant de modifier l'ordonnee du joueur */
	public void setY(int y) {
		this.y = y;
	}
	
	/** Methode permettant de modifier le role du joueur */
	public void setRole(role r) {
		this.r = r;
	}
	
}