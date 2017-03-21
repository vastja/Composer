/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package folder;

import overlay.Point;

/**
 * Tridimenzionalni vektor
 * @author Jakub Vašta
 */
public class Vector {
    
    /** X-ova souradnice voktoru */
    public final Double x;
    /** Y-ova souradnice voktoru */
    public final Double y;
    /** Z-ova souradnice voktoru */
    public final Double z;
    
    /**
     * Konstruktor vytvarejici z dvou bodu vektor
     * @param a pocatecni bod vektoru
     * @param b koncovy bod vektoru
     */
    public Vector(Point a, Point b) {
        
        this.x = b.x - a.x;
        this.y = b.y - a.y;
        this.z = b.z - a.z;
        
    }
    
    /**
	 * Vraci uhela svirajici dvemi vektory
	 * @param f prvni vektor
	 * @param s druhy vektor
	 * @return uhel, ktery sviraji vektory "f" a "s"
	 */
	public static Double getAngle(Vector f, Vector s) {

		Double size_f = Math.sqrt(x_square(f.x) + x_square(f.y) + x_square(f.z));
		Double size_s = Math.sqrt(x_square(s.x) + x_square(s.y) + x_square(s.z));

		return scalar_product(f,s)/(size_f*size_s);

	}
	
	/**
	 * Metoda vraci skalarini soucin dvou vektoru
	 * @param f prvni vektor
	 * @param s druhy vektor
	 * @return skalarni soucin vektoru "s" a "f"
	 */
	public static Double scalar_product(Vector f, Vector s) {
		return f.x * s.x + f.y * s.y + f.z * s.z;
	}
	
	/**
	 * Umocni promenou na druhou
	 * @param x umocnovane cislo
	 * @return druha mocnina zadaneho cisla
	 */
	private static double x_square(Double x) {
		return x * x;
	}
}
