/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package folder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Třída skládající obj soury na zaklade .txt souboru s pokyny
 *
 * @author Jakub Vašta
 */
public class DrivenComposition extends Thread {

	/** Reference na logger */
	public static final Logger dcLogger = LogManager.getLogger();
	
    /** Reference na soubor s postupem skladani - skriptovaci soubor */
    private final File script_file;
    /** Umisteni utility, script a obj souboru */
    private final String dir;
    
    /**
     * Konstruktor
     * @param f script file - pevne definovany soubor s postupem pro skladani
     */
    public DrivenComposition(File f) {
    	
    	super();
    	
        if (f == null) {
            throw new NullPointerException();
        }
        
        this.script_file = f;
        this.dir = script_file.getParent();
        
    };
    
    @Override
    public void run() {
    	compose();
    }
    
    /**
     * Metoda skladajici obj soubory podle skriptovaciho souboru
     */
    public void compose() {
        
        try (BufferedReader br = new BufferedReader(new FileReader(this.script_file))) {
            
            String result_name, info_line;
            ComposeInfo info;
            
            while ((result_name = br.readLine()) != null) {
            
                if ((info_line = br.readLine()) != null) {
                    info = parse_line(info_line);
                    // Predat ke skladani
                    Container temp = Composition.fold(info.first_f, info.second_f, 0, 0, info.iterations, dir);
                    ComposeUtility.composeFiles(temp.file, info.first_f, result_name, this.dir);
                }
                
            };
        
        }
        catch (IOException e) {
            //TODO
        }
        catch (ParseException e) {
            //TODO
        }
    }
    
    /**
     * Metoda parsujici radku ve scriptovacim souboru, ktera udava nformace o skladani
     * @param line string s udaji
     * @return kontajner s udaji pro skladani (nazev prvniho sbouru, nazev druheho souboru, pocet opakovani)
     * @throws ParseException v pripade spatneho formatu souboru
     */
    private ComposeInfo parse_line(String line) throws ParseException {
    
        String[] data = line.split(" ");
        
        String ffile_name, sfile_name;
        int iterations = 1;
  
            
        switch (data.length) {
        
            case 3 : iterations = Integer.parseInt(data[2]);
            case 2 : ffile_name = data[0];
                     sfile_name = data[1];
                     break;
            default : throw new ParseException("Line is not in format <file> <file> [itr] [ipc]", 0);
            
        }
        
        return new ComposeInfo(ffile_name, sfile_name, iterations);
    };
    
    public void close_log() {
        //TODO
    }

}
