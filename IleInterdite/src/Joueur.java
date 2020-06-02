import java.util.ArrayList;

enum role {normal, ingenieur, plongeur, messager} ;

public class Joueur {

	private int x; 
	private int y;
	public role r;
	public ArrayList<Key> keyList;
	public ArrayList<element> keyElementList;
	
	public Joueur(int x, int y, role r) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.keyList = new ArrayList<Key>();
		this.keyElementList = new ArrayList<element>();
	}

	
	public void addKey(Key k) {
		keyList.add(k);
		keyElementList.add(k.e);
	}
	
	public Key getKey(int indice) {
		return keyList.get(indice);
	}
	
	
	public boolean getKey(Key k) {
		return keyList.contains(k);
	}
	
	public boolean getKeyElement(element e) {
		return keyElementList.contains(e);
	}
	
	
	public int nbKeyOfArtefact(element e) {
    	int res = 0;
    	for(Key k : this.keyList)
    		if(k.e == e) res+=1;
    	return res;
    }
	
	public void afficheClesTer() {
		if(this.keyElementList.isEmpty()) 
			System.out.print("Le joueur ne possede pas de cles");
		else {
			System.out.println("Il possede les cles suivantes: ");
			for(element e: element.values()) {
				int nbKey = nbKeyOfArtefact(e);
				System.out.print(nbKey + " " + e +" ");
			}
		}
	}
	
	
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
