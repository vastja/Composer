package folder;

import java.util.Arrays;

/**
 * Trida pro skladani a porovnavani vysledku obj souboru
 * @author Jakub Vašta
 */
public class NotDrivenComposition extends Thread {

    /** Pole porovnavanych souboru */
    private final String[] files;
    /** Umisteni souboru */
    private final String dir;
    /** Pocet opakovani pri skladani dvou souboru */
    private final int treshold;

    /**
     * Konstruktor
     * @param files soubory ktere chceme slozit
     * @param dir umisteni souboru
     * @param treshold pocet opakovani pri skladani dvou souboru
     */
    public NotDrivenComposition(String[] files, String dir, int treshold) {
    	super();
    	
        this.files = files;
        this.dir = dir;
        this.treshold = treshold;
    }
    
    /**
     * Metoda skladajici soubory obj
     */
	@Override 
	public void run() {
		
		no_guide_fold();
	}
        
        private void no_guide_fold() {
            
            boolean first = true;
            int cont = this.files.length;
            int compose = 0;
            Container temp, r = null;
                        
            while (cont > 1) {

                System.out.println(Arrays.toString(this.files));
                int g_max = Integer.MIN_VALUE;

                // Poprve zkousime vsechny se vsemi
                if (first) {

                    for (int i = 0; i < this.files.length - 1; i++) {
                        for (int j = i + 1; j < this.files.length; j++) {

                            temp = Composition.fold(this.files[i], this.files[j], i, j, this.treshold, this.dir);
                            // Zapamatujeme si nelepsi vysledek
                            if (temp != null && temp.points > g_max) {
                                r = temp;
                                compose = temp.first;
                            }
                        }
                    }
                    first = false;
                } // Pokud prochazime podruhe, tak zkousime pouze ostatni s poslednim slozenym
                else {

                    for (int i = 0; i < this.files.length; i++) {
                        if (i != compose && this.files[i] != null) {
                            temp = Composition.fold(this.files[compose], this.files[i], compose, i, this.treshold, this.dir);

                            // Zapamatujeme si nelepsi vysledek
                            if (temp != null && temp.points > g_max) {
                                r = temp;
                                compose = temp.first;
                            }
                        }
                    }
                }

                System.out.println("We will combine:" + this.files[r.first] + " + " + this.files[r.second]);

                // Slozeni nejlepsich kandidatu
                String result_name = ComposeUtility.composeFiles(r.file, this.files[r.first], null, dir);

                // Slozeny soubor nahradi prvni ze skladanych souboru a druhy vymazeme
                this.files[r.first] = result_name;
                this.files[r.second] = null;

                // Snizime pocet souboru, ktere zbyvaji ke slozeni
                cont--;

            }
        }
        
}