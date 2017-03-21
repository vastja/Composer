/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package folder;

/**
 *
 * @author Jakub Vasta
 */
public class ComposeInfo {
    
    /** Nazev prvniho souboru */
    public final String first_f;
    /** Nazev druheho souboru */
    public final String second_f;
    /** Pocet iteraci */
    public final int iterations;
    
    /**
     * Konstruktor
     * @param first_f nazev prvniho souboru
     * @param second_f nazev druheho souboru
     * @param iterations pocet opakovani
     */
    public ComposeInfo(String first_f, String second_f, int iterations) {
        
        if (first_f == null || second_f == null) {
            throw new NullPointerException();
        }
        
        if (iterations < 0) {
            throw new IllegalArgumentException();
        }
        
        this.first_f = first_f;
        this.second_f = second_f;
        this.iterations = iterations;
        
    }
   
}
