
/** Tous les elements possibles d'une cle */
enum element {air, eau, feu, terre} ;

public class Key {
	
	private element e;
	
	/** Premier constructeur pour initialiser une cle avec un element */
	public Key(element e) {
		this.e = e;
	}
	
	/** Autre constructeur pour initialiser une cle avec un typeZone */
	public Key(typeZone t) {
		if(t == typeZone.air) this.e = element.air;
		if(t == typeZone.eau) this.e = element.eau;
		if(t == typeZone.terre) this.e = element.terre;
		if(t == typeZone.feu) this.e = element.feu;
	}

	
	/** Methode permettant d'obtenir l'element de la cle */
	public element getElement() {
		return e;
	}
}