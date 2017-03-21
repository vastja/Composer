package folder;

import java.io.File;

/**
 * Kontajner ukladajici informace o slozeni dvou obj souboru
 * @author Jakub Vašta
 */
public class Container {
        
    /** Prvni porovnavany soubor */
    public final int first;
    /** Druhy porovnavnay osubor */
    public final int second;
    /** Pocet dosazenych bodu shody*/
    public final int points;
    /** Matice, kterou byl upraven */
    public final String matrix;
    /** Vysledny soubor */
    public final File file;
    /** Jestli je afterICP nebo neni */
    public final String type;

    /**
     * Konstruktor pro kontajner ukladajici infomace o slozeni dvou obj souboru
     * @param first prvni skladany soubor
     * @param second druhy skladany soubor
     * @param points ohodnoceni vysledku
     * @param matrix transformacni matice
     * @param file nazev souboru s vysledkem
     * @param type jaky typ - afterICP nebo beforeICP
     */
    public Container(int first, int second, int points, String matrix, File file, String type) {
        this.first = first;
        this.second = second;
        this.points = points;
        this.matrix = matrix;
        this.file = file;
        this.type = type;
    }
;      
}
