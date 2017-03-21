/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package folder;

import overlay.Point;

/**
 * Trojuhelnik
 * @author Jakub Vašta
 */
public class Triangle {
    
	/** Minimalni prijatelny uhel */
	private static final Double MIN_COS_ANGLE = Math.cos(0.17);
	
    /** Bod A trojuhelniku */
    public final Point a;
    /** Bod B trojuhelniku */
    public final Point b;
    /** Bod C trojuhelniku */
    public final Point c;
    
    /**
     * Konstruktor trojuhelniku ze tri bodu
     * @param a bod A trojuhelniku
     * @param b bod B trojuhelniku
     * @param c bod C trojuhelniku
     */
    public Triangle(Point a, Point b, Point c) {
        
        this.a = a;
        this.b = b;
        this.c = c;
        
    }
    
	/**
	 * Metoda ktera vraci zda nema trojuhelnik ostrejsi uhly nez pozadujeme
	 * @param t zkoumany trojuhelnik
	 * @return nema ostrejsi uhly TRUE jinak FALSE
	 */
	public static boolean isValidTriangle(Triangle t) {

		Vector a = new Vector(t.a, t.b);
		Vector b = new Vector(t.a, t.c);

		Double f_a = Vector.getAngle(a, b);

		// Musim porovnavat cosinus
		if (f_a > MIN_COS_ANGLE) {
			return false;
		}

		Vector c = new Vector(t.b, t.a);
		Vector d = new Vector(t.b, t.c);

		Double s_a = Vector.getAngle(c, d);

		if (s_a > MIN_COS_ANGLE) {
			return false;
		}

		Vector e = new Vector(t.c, t.a);
		Vector f = new Vector(t.c, t.b);

		Double t_a = Vector.getAngle(e, f);

		return t_a < MIN_COS_ANGLE;

	}
    
}
