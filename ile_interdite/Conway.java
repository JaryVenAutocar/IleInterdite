import java.awt.EventQueue;

/**
 * Nous allons commencer à construire notre application, en voici la classe
 * principale.
 */
public class Conway {
    /**
     * L'amorçage est fait en creant le modèle et la vue, par un simple appel
     * à chaque constructeur.
     * Ici, le modèle est cree independamment (il s'agit d'une partie autonome
     * de l'application), et la vue prend le modèle comme paramètre (son
     * objectif est de faire le lien entre modèle et utilisateur).
     */
    public static void main(String[] args) {
	/**
	 * Pour les besoins du jour on considère la ligne EvenQueue... comme une
	 * incantation qu'on pourra expliquer plus tard.
	 */
	EventQueue.invokeLater(() -> {
		/** Voici le contenu qui nous interesse. */
                CModele modele = new CModele();
                CVue vue = new CVue(modele);
	    });
    }
}
/** Fin de la classe principale. */

