import java.util.*;

/**
 * Un lien entre vue et modèle : les informations montrées à l'utilisateur
 * reflètent l'état du modèle et doivent être maintenues à jour.
 * 
 * Pour réaliser cette synchronisation, on peut suivre le schéma de conception
 * observateur/observé, dont le principe est le suivant :
 *  - Un observateur (en l'occurrence la vue) est lié à un objet observé et se
 *    met à jour pour refléter les changement de l'observé.
 *  - Un observé est lié à un ensemble d'objets observateurs et les notifie de
 *    tout changement de son propre état.
 *
 * Java fournit une interface [Observer] (observateur) et une classe
 * [Observable] (observé) assurant cette jonction.
 * Voici une manière sommaire de les recoder.
 */

/**
 * Interface des objets observateurs.
 */ 
interface Observer {
    /**
     * Un observateur doit posséder une méthode [update] déclenchant la mise à
     * jour.
     */
    public void update();
    /**
     * La version officielle de Java possède des paramètres précisant le
     * changement qui a eu lieu.
     */
}

/**
 * Classe des objets pouvant être observés.
 */
abstract class Observable {
    /**
     * On a une liste [observers] d'observateurs, initialement vide, à laquelle
     * viennent s'inscrire les observateurs via la méthode [addObserver].
     */
    private ArrayList<Observer> observers;
    
    
    /** Constructeur */
    public Observable() {
    	this.observers = new ArrayList<Observer>();
    }
    
    /** Methode permettant d'ajouter un observer a la liste d'observateurs */
    public void addObserver(Observer o) {
    	observers.add(o);
    }

    /**
     * Lorsque l'état de l'objet observé change, il est convenu d'appeler la
     * méthode [notifyObservers] pour prévenir l'ensemble des observateurs
     * enregistrés.
     * On le fait ici concrètement en appelant la méthode [update] de chaque
     * observateur.
     */
    public void notifyObservers() {
		for(Observer o : observers) {
		    o.update();
		}
    }
}