package folder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import overlay.Point;



/**
 * Trida upravujici obj soubor s body, tak aby zde zbyly pouze kvalitni body a trojuhelniky
 * @author Jakub Vašta
 */
public class EditFile {

	/** Pocet mezer pred cislem udavajicim kvalitu bodu */
	private static final int QUALITY_POS = 7;
	/** Znak pro spatny bod  */
	private static final int B_SIGN = 1;

	/**
	 * Metoda odstranujici spatne vrcholy 
	 * @param file soubor z ktereho odstaranime spatne vrcholy
	 * @param treshold mez kdy ma byt bod odstranen
	 */
	public static void editFile(File file, double treshold) {
		try {

			File rf = new File(file.getParentFile() + "/e_" + file.getName());
			rf.createNewFile();

			System.out.println(treshold);
			
			BufferedReader f = new BufferedReader(new FileReader(file));
			BufferedWriter result = new BufferedWriter(new FileWriter(rf));

			List<String> vertexs = new ArrayList<String>();
			List<String> triangles = new ArrayList<String>();
			Integer[] mapfunc;

			readObjFile(f, vertexs, triangles);
			mapfunc = getMapfunc(vertexs, treshold);
			rewriteValidVertexs(mapfunc, vertexs, result);
			rewriteValidTriangles(mapfunc, triangles, vertexs, result);


			result.flush();
			f.close();
			result.close();	

		}
		catch (IOException e) {
			System.out.println("Edit failed");
		}
	}

	/**
	 * Metoda nacte do dvou seznamu vrcholy a trojuhelniky
	 * @param br reference na bufferedreader souboru s vrcholy a trojuhelniky
	 * @param vertexs seznam vrcholu
	 * @param triangles seznam trojuhelniku
	 * @throws IOException
	 */
	public static void readObjFile(BufferedReader br, List<String> vertexs, List<String> triangles) throws IOException {

		String line;

		while((line = br.readLine()) != null) {

			if (line.compareTo("") != 0) {
				if (line.charAt(0) == 'v') {
	
					vertexs.add(line);
				}
				else if (line.charAt(0) == 'f') {
	
					triangles.add(line);
				}
			}

		}

	}

	/**
	 * Metoda vrati cislo ktere oznacuje kvalitu daneho vrcholu
	 * @param vertex retezcovy zapis vrcholu
	 * @return cislo udavajici kvalitu vrcholu
	 */
	private static Double getVertexQuality(String vertex) {

		String s[] = vertex.split(" ");

		Double quality = Double.parseDouble(toValidFormat(s[QUALITY_POS]));

		return quality;

	}

	/**
	 * Metoda osetrujici carku jako oddelovac tisicu
	 * @param number cislo ve forme retezce
	 * @return cislo bez carky jako oddelovace tisicu ve forme retezce
	 */
	private static String toValidFormat(String number) {

		String num = "";

		// Osetreni carky mezi tisici 
		for (int i = 0; i < number.length(); i++) {
			if (number.charAt(i) != ',') {
				num += number.charAt(i);
			}
		}

		return num;

	}

	/**
	 * Vutvari mapovaci funkci vzhledem ke kvalite vrcholu - pokud je vrchol nekvalitni bude na pozici vrcholu - 1
	 * (vrcholy v obj souboru od 1 ale v poli indexovany od 0) B_SIGN, jinak zde bude cislo udavajici minus pocet vrcholu ktere jsme vyradili
	 * (pozor cislo je zaporne)
	 * @param vertexs seznam vrcholu
	 * @param treshold prah nad ktery nesmi byt kvalita
	 * @return pole s mapovaci funkci
	 */
	private static Integer[] getMapfunc(List<String> vertexs, Double treshold) {

		List<Integer> mapfunc = new ArrayList<Integer>();
		int bad = 0;
		Double quality;

		for (String v : vertexs) {
		
			quality = getVertexQuality(v);
			
			if(quality > treshold) {//|| Double.compare(quality, 0.) == 0) {							//Pokud je kvalita mensi nez zvoleny prah
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
	 * prepise na zaklade mapovaci funkce trojuhelniky do vysledneho souboru
	 * @param mapfunc mapovaci funkce
	 * @param vertexs seznam vrcholu
	 * @param bw refernece na bufferedwriter souboru pro zapis
	 * @throws IOException
	 */
	public static void rewriteValidVertexs(Integer[] mapfunc, List<String> vertexs, BufferedWriter bw) throws IOException {

		int i = 0;

		for (String v : vertexs) {

			if (mapfunc[i] != B_SIGN) {
				bw.write(v);
				bw.newLine();
			}

			i++;
		}

	}

	/**
	 * Prepise trojuhelniky do vysledneho souboru na zaklade mapovaci funkce
	 * @param mapfunc mapovaci funkce
	 * @param triangles seznam trojuhelniku
	 * @param vertexs seznam vrcholu
	 * @param bw reference na bufferedwriter do ktereho se zapisuje vysledek
	 * @throws IOException
	 */
	private static void rewriteValidTriangles(Integer[] mapfunc, List<String> triangles, List<String> vertexs, BufferedWriter bw) throws IOException {

		Triangle t;

		String[] v =  new String[vertexs.size()]; 	
		vertexs.toArray(v);

		Point x, y, z;

		int[] vertexPos;
		int xpos, ypos, zpos;
		String rLine;
		
		for (String s : triangles) {

			vertexPos = getTrianglePos(s);

			xpos = vertexPos[0] - 1;
			ypos = vertexPos[1] - 1;
			zpos = vertexPos[2] - 1;
			
			x = new Point(v[xpos]);
			y = new Point(v[ypos]);
			z = new Point(v[zpos]);

			t = new Triangle(x, y, z);
			
			if (Triangle.isValidTriangle(t) && evaluateMapfunc(xpos, ypos, zpos, mapfunc)) {
				rLine = updateTriangle(s, mapfunc[xpos], mapfunc[ypos], mapfunc[zpos]);
				if (rLine != null) {
					bw.write(rLine);
					bw.newLine();
				}
			}
			
		}
	}

	/**
	 * Pricte k trojuhelniku ve forme retezce posun ke kazdemu vrcholu
	 * @param line trojuhelnik ve forme retezce (tzn. v obj je to napr. "f 10 1 5")
	 * @param a cislo pro pricteni k vrcholu a
	 * @param b cislo pro pricteni k vrcholu b
	 * @param c cislo pro pricteni k vrcholu c
	 * @return upraveny trojuhelnik ve forme retezce
	 */
	public static String updateTriangle(String line, int a, int b, int c) {
		
		String[] temp = line.split(" ");
		
		if (temp.length > 3) {
			// ParseException
			temp[1] = Integer.toString(Integer.parseInt(temp[1]) + a);
			temp[2] = Integer.toString(Integer.parseInt(temp[2]) + b);
			temp[3] = Integer.toString(Integer.parseInt(temp[3]) + c);
		}
		else {
			return null;
		}
		
		String result = "";
		
		for(int i = 0; i < temp.length - 1; i++) {
			result += temp[i] + " ";
		}
		
		return result + temp[temp.length - 1];
	}
	
	/**
	 * Na zaklade mapovaci funkce a pozic v ni rozhodne zda je trojuhelnik kvalitni (nepouziva nekvalitni body - ani jeden)
	 * @param a index prvniho vrcholu v mapovaci funkci
	 * @param b index druheho vrcholu v mapovaci funkci
	 * @param c index tretiho vrcholu v mapovaci funkci
	 * @param mapfunc mapovaci funkce
	 * @return true - je kvalitni cely, false - alespon jeden bod je nekvalitni
	 */
	public static boolean evaluateMapfunc(int a, int b, int c, Integer[] mapfunc) {
		return mapfunc[a] != B_SIGN && mapfunc[b] != B_SIGN && mapfunc[c] != B_SIGN;
	}
	
	/**
	 * Rozparsuje retezec reprezentujici trojuhelnik a najde vrcholy na ktere odkazuje
	 * @param line retezec reprezentujici trojuhelnik (napr. v obj "f 1 2 3")
	 * @return triprvkove pole s indexy vrcholu z kterych se sklada trojuhelnik (POZOR vrcholy jsou v obj indexovany od 1 a ne od 0)
	 */
	public static int[] getTrianglePos(String line) {

		int[] pos = new int[3];

		String[] s = line.split(" ");
		
		if (s.length > 3) {
			pos[0] = Integer.parseInt(s[1]);
			pos[1] = Integer.parseInt(s[2]);
			pos[2] = Integer.parseInt(s[3]);
	
			return pos;
		}
		
		return null;
	}
}
