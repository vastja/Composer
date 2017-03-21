package folder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Composition {
	
	private static int counter = 0;
	
	/**
     * Metoda skladajici dva soubory
     * @param i index prvniho souboru v poli souboru ke slozeni
     * @param j index druheho souboru v poli souboru ke slozeni
     * @return  conatainer s vysledky
     */
    public static Container fold(String first_f, String second_f, int i, int j, int iterations, String dir) {
        
        Process p;
        Container result;
        Container fin = null;
        
        
        File first = new File(dir + "/" + first_f);
        File second = new File(dir + "/" + second_f);
        
        if (!(first.exists() && second.exists())) {
            // TODO
        }
        
        try {
            int max = Integer.MIN_VALUE;
            
            // Opakujeme podle nastvaeneho poctu
            for (int k = 0; k < iterations; k++) {
                p = Runtime.getRuntime().exec("cmd /c match " + first_f + " " + second_f + " -i", null, new File(dir));
                result = match(i, j, p, dir);

                if (result.points > max) {
                    // Mazat soubory, ktere nevyuzijeme dale
                    fin = result;
                    max = result.points;
                }
                p.destroy();
            }
            
            return fin;
            
        }
        catch (Exception e) {
            System.out.println("Error in fold");
            System.out.println(dir);
            return null;
        }
    }
    
    /**
     * Metoda skladajici dva soubory 
     * @param first index prvniho souboru v poli souboru ke slozeni
     * @param second index druheho souboru v poli souboru ke slozeni
     * @param p process ke skladani souboru
     * @param dir umisteni souboru
     * @return conatainer s vyslekdy o slozeni
     * @throws IOException 
     */
private static Container match(int first, int second, Process p, String dir) throws IOException {
	
	try(BufferedReader in = new BufferedReader(new InputStreamReader(
	        p.getInputStream()));) {
		  
	
		String line;
		String matrix1 = "";
		String matrix2 = "";
		int matching[] = new int[2];
		int i , mark, n ;
	
		i = n = 0;
	
		while ((line = in.readLine()) != null) {
			 
			System.out.println(line); 

			i++;

			if(i >= 21 && i <= 24) {
		    	matrix1 += line + "\n"; 
		    }
		    
		    if(i == 28 || i == 37) { 
				mark = 0;
				for(int j = 24; j < line.length() - 1; j++)
					if(line.charAt(j) >= '0' && line.charAt(j) <= '9') 
						mark = mark*10 + (line.charAt(j) - '0'); 
	    		
				
				matching[n] = mark;
	    		n++;
			}
		    
		    if(i >= 40 && i <= 43) {
		    	matrix2 += line + "\n"; 
		    }
		}
		
		File temp_file;
		int points;
		String matrix;
		String type;
		
		if (matching[0] < matching[1]) {
			temp_file = new File(dir + "/afterICP.obj");
			points = matching[1];
			matrix = matrix2;
			type = "afterICP";
		}
		else {
			temp_file = new File(dir + "/result.obj");
			points = matching[0];
			matrix = matrix1;
			type = "beforeICP";
		}
		
		File f = new File(dir + "/res" + counter++ + ".obj");
		temp_file.renameTo(f);
		
		return  new Container(first, second, points, matrix, f, type);
	} 
}


}
