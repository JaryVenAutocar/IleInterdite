import java.util.ArrayList;

enum role {ingenieur, navigateur, plongeur, messager} ;

public class Joueur {

	private int x; 
	private int y;
	public role r;
	public ArrayList<Key> keyList;
	public ArrayList<element> keyElementList;
	
	public Joueur(int x, int y) {
		this.x = x;
		this.y = y;
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
