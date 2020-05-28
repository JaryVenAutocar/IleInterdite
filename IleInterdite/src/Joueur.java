import java.util.ArrayList;

public class Joueur {

	private int x; 
	private int y;
	public ArrayList<Key> keyList;
	
	public Joueur(int x, int y) {
		this.x = x;
		this.y = y;
		this.keyList = new ArrayList<Key>();
	}

	
	public void addKey(Key k) {
		keyList.add(k);
	}
	
	public Key getKey(int indice) {
		return keyList.get(indice);
	}
	
	
	public boolean getKey(Key k) {
		return keyList.contains(k);
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
