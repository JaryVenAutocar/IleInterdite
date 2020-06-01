enum element {air, eau, feu, terre} ;

public class Key {
	
	public element e;
	
	public Key(element e) {
		this.e = e;
	}
	
	public Key(typeZone t) {
		if(t == typeZone.air) this.e = element.air;
		if(t == typeZone.eau) this.e = element.eau;
		if(t == typeZone.terre) this.e = element.terre;
		if(t == typeZone.feu) this.e = element.feu;
	}
	
}