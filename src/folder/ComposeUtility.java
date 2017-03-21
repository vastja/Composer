package folder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import overlay.KDTree;
import overlay.Point;

/**
 * Skladani dvou obj souboru do jednoho + odstraneni prekryvu
 * @author Jakub Vašta
 */
public class ComposeUtility {

	/** Reference na logger */
	public static final Logger composeUtilityLogger = LogManager.getLogger();
	
    /** Nazev vysledneho souboru */
	private static final String RESULT = "/result";
    
	/** Pocet slozenych souboru */
	private static int count = 0;
	
	/** Znak pro spatny bod  */
	private static final int B_SIGN = 1;

	private static final Double TRESHOLD = 0.01;
    /**
     * Metoda skladajici dva obj soubory 
     * @param moved obj soubor, ktery byl natoceny aby pasoval na "fixed" obj soubor
     * @param fixed nazev obj souboru na ktery byl napasovany "moved" obj soubor
     * @param dir cesta k umisteni souboru
     * @return nazev slozeneho souboru - nazev bude "result + cislo"
     */
	public static String composeFiles(File moved, String fixed, String result_name, String dir) {
		
		try {
			
			// Zvysime citac
			count++;
                        
			File file; 
			
			// Pojmenujeme vysledny soubor
			if (result_name == null) {
				file = new File(dir + RESULT + count + ".obj");
				composeUtilityLogger.debug("Composed filename" + dir + RESULT + count + ".obj");
			} else {
				file = new File(dir + "/" + result_name);
				composeUtilityLogger.debug("Composed filename" + dir + RESULT + count + ".obj");
			}
                        
			file.createNewFile();
                        
			BufferedReader firstf = new BufferedReader(new FileReader(moved));                     
			composeUtilityLogger.info("First file <" + moved.getName() + "> buffered");
			          
			BufferedReader secondf = new BufferedReader(new FileReader(dir + "/" + fixed));
			composeUtilityLogger.info("Second file <" + dir + "/" + fixed + "> buffered");
			
			BufferedWriter result = new BufferedWriter(new FileWriter(file));
			composeUtilityLogger.info("Output file <" + file.getPath() + "> created");
			
			int countpoints;
			
			List<String> fVertexs = new ArrayList<String>();
			List<String> fTriangles = new ArrayList<String>();
			List<String> sVertexs = new ArrayList<String>();
			List<String> sTriangles = new ArrayList<String>();
			
			EditFile.readObjFile(firstf, fVertexs, fTriangles);
			EditFile.readObjFile(secondf, sVertexs, sTriangles);
			
			
            // Prepiseme vrcholy z prvniho souboru s spocitame kolik jich bylo
            countpoints = rewriteVertexToFile(fVertexs, result);
            
            int size = fVertexs.size();
            Double[] x = new Double[size];
            Double[] y = new Double[size];
            Double[] z = new Double[size];
            
            fillPointsCoordinate(fVertexs, x, y, z);
            KDTree kdt = new KDTree(x, y, z);
            
            Integer[] mapfunc = getMapfunc(sVertexs, TRESHOLD , kdt);
            EditFile.rewriteValidVertexs(mapfunc, sVertexs, result);
            rewriteTrianglesToFile(fTriangles, result);
            rewriteValidTrianglesToFile(mapfunc, sTriangles, result, countpoints);
                        
                        
			composeUtilityLogger.info("Step 1/4 completed");
			composeUtilityLogger.debug("Count of points form first file <" + moved.getName() + "> : " + countpoints);
                        
            rewriteVertexToFile(secondf, result);
                        
            composeUtilityLogger.info("Step 2/4 completed");
                        
			
            // Prepiseme trojuhelniky z prvniho souboru
			rewriteTriangleToFile(firstf, result, 0);
                        
			composeUtilityLogger.info("Step 3/4 completed");
			
			// Prepiseme trojuhelniky z druheho souboru a k vrcholum pricteme pocet vrcholu v prvnim souboru
			rewriteTriangleToFile(secondf, result, countpoints); 
			composeUtilityLogger.info("Step 4/4 completed");
			
			
			firstf.close();
			secondf.close();
                        
            result.flush();
			result.close();
			
			return file.getName();
		}
		catch (FileNotFoundException e) {
			composeUtilityLogger.error("File not found");
			return null;
		}
		catch (IOException e) {	
			composeUtilityLogger.error("Composition failed");
			return null;
		}

	}
    
	/**
	 * Prepise podle mapfunkce trojuhelniky do vysledneho souboru + pripocte posun
	 * @param mapfunc mapovaci funkce
	 * @param triangles  seznam trojuhelniku k prepsani
	 * @param result reference na bufferedwriter souboru pro vysledky
	 * @param countpoints posun
	 * @throws IOException
	 */
	private static void rewriteValidTrianglesToFile(Integer[] mapfunc, List<String> triangles, BufferedWriter result, int countpoints) throws IOException {
		
		int[] pos;
		int a, b, c;
		
		for (String t : triangles) {
			
			pos = EditFile.getTrianglePos(t);
			
			if (pos != null) {
				
				if (EditFile.evaluateMapfunc(pos[0] - 1, pos[1] - 1, pos[2] - 1, mapfunc)) {
					a = mapfunc[pos[0] - 1] + countpoints;
					b = mapfunc[pos[1] - 1] + countpoints;
					c = mapfunc[pos[2] - 1] + countpoints;
					
					result.write(EditFile.updateTriangle(t, a, b, c));
					result.newLine();
				}
			}
			
		}
		
	}
	
	/**
	 * Prespise trojuhelniky do souboru
	 * @param triangles seznam trojuhelniku
	 * @param result reference na bufferedwriter souboru pro zapis
	 * @throws IOException
	 */
	private static void rewriteTrianglesToFile(List<String> triangles, BufferedWriter result) throws IOException {
		
		for (String t : triangles) {
			result.write(t);
			result.newLine();
		}
		
	}
	
	/**
	 * Zapise trojuhelnik do souboru a v pripade nenuloveho countpoints i pred zapisem trojuhelnik prepocita 
	 * @param br reference na bufferedreader seouboru z ktereho prepisujeme
	 * @param result refernece na bufferedwriter souboru do ktereho zapisujeme
	 * @param countpoints pocet bodu na posun
	 * @throws IOException
	 */
	private static void rewriteTriangleToFile(BufferedReader br, BufferedWriter result, int countpoints) throws IOException {
		String line;
		
		while((line = br.readLine()) != null) {
			if (line.charAt(0) == 'f') {
				
				if (countpoints != 0) {
					line = recountObjTriangle(line, countpoints);
				}
										
				if (line != null) {
					writeToFile(line, result);
				}
			}
		}
	}
	
	/**
	 * Metoda prepisujici vrcholy do daneho souboru
	 * @param source reference na bufferedreader pro soubor z ktereho se kopiruji trojuhelniky 
	 * @param result reference na buffereader souboru pro prepis vrcholu
	 * @return pocet prepsanych vrcholu
	 * @throws IOException
	 */
	private static int rewriteVertexToFile(BufferedReader source, BufferedWriter result) throws IOException {

		String line;
		int vertexCount = 0;

		while ((line = source.readLine()) != null) {

			if (line.charAt(0) == 'v') {
				writeToFile(line, result);
				// Pripocitavam pocet vrholu do staticke promene
				vertexCount++;
				source.mark(100);
			} else if (line.charAt(0) == 'f') {
				source.reset();
				break;
			}
		}

		return vertexCount;

	}
	
	
	/**
	 * Vutvari mapovaci funkci vzhledem ke kvalite vrcholu - pokud je vrchol nekvalitni bude na pozici vrcholu - 1
	 * (vrcholy v obj souboru od 1 ale v poli indexovany od 0) B_SIGN, jinak zde bude cislo udavajici minus pocet vrcholu ktere jsme vyradili
	 * (pozor cislo je zaporne)
	 * @param vertexs seznam vrcholu
	 * @param treshold prah nad ktery nesmi byt kvalita
	 * @return pole s mapovaci funkci
	 */
	private static Integer[] getMapfunc(List<String> vertexs, Double treshold, KDTree kdt) {

		List<Integer> mapfunc = new ArrayList<Integer>();
		int bad = 0;
		Double distance;
		Point point;

		for (String v : vertexs) {
		
			// TODO
			// osetreni null
			point = new Point(v);
			
			distance = kdt.findNearest(point);
			
			if(distance < treshold) {//|| Double.compare(quality, 0.) == 0) {							//Pokud je kvalita mensi nez zvoleny prah
				mapfunc.add(B_SIGN);
				bad--;
			}
			else {
				mapfunc.add(bad);
			}
		}

		/*
		 * Prepsani mopovaci funkce do pole, aby se s ni lepe pracovalo
		 */
		Integer[] mapf =  new Integer[mapfunc.size()]; 	
		mapfunc.toArray(mapf);

		return mapf;
	}
	
	/**
	 * Metoda prepisujici vrcholy do daneho souboru
	 * @param vertexs seznam z ktereho se kopiruji trojuhelniky 
	 * @param result reference na buffereader souboru pro prepis vrcholu
	 * @return pocet prepsanych vrcholu
	 * @throws IOException
	 */
	private static int rewriteVertexToFile(List<String> vertexs, BufferedWriter result) throws IOException {

		int vertexCount = 0;

		for (String v : vertexs) {
			
			result.write(v);
			result.newLine();
			vertexCount++;
			
		}

		return vertexCount;

	}
        
	/**
	 * Metoda zapisujici do daneho souboru
	 * @param line retezec pro zapsani
	 * @param target refernece na bufferedwriter do souboru kam se bude zapisovat
	 * @throws IOException
	 */
	private static void writeToFile(String line, BufferedWriter target) throws IOException {
		target.write(line);
		target.newLine();
	}
    
	/**
	 * Metoda pripocita k vrcholum trojuhelniku dane cislo
	 * @param line textovy retezec s trojuhelnikem ve formatu obj
	 * @param countpoints pocet ktery pricitame k vrcholum trojuhelniku
	 * @return textovy retezec s upravenym trojuhelnikem ve formatu obj
	 */
	private static String recountObjTriangle(String line, int countpoints) {

		String temp = "f";
		int n;

		for (String split : line.substring(2).split(" ")) {
			try {
				n = Integer.parseInt(split);
				temp += " " + (n + countpoints);
			} catch (ParseException e) {
				composeUtilityLogger.error("Error during parsing triangle line");
				return "null";
			}
		}

		return temp;
	}
	
	/**
	 * Naplni tri pole pro x,y,z souradnice
	 * @param vertexs seznam vrcholu v retezci
	 * @param x pole x-ovych souradnice
	 * @param y pole y-ovych souradnic
	 * @param z pole z-ovych souradnic
	 */
	private static void fillPointsCoordinate(List<String> vertexs, Double[] x, Double[] y, Double[] z) {
		
		Point point;
		int i = 0;
		
		for (String v : vertexs) {
			
			point = new Point(v);
			
			if (point != null) {
				x[i] = point.x;
				y[i] = point.y;
				z[i] = point.z;
				i++;
			}
			
		}
		
	}

}
