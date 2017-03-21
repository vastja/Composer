package overlay;

/**
 * Bod
 * @author Jakub Vašta
 */
public class Point {
	
    /** X-ova souradnice bodu */
    public final Double x;
    /** Y-ova souradnice bodu */
    public final Double y;
    /** Z-ova souradnice bodu */
    public final Double z;
   

    /**
     * Konstruktor
     * @param x x-ova souradnice bodu
     * @param y y-ova souradnice bodu
     * @param z z-ova souradnice bodu
     */
    public Point(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
   
    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getZ() {
        return z;
    }
    
	/**
	 * Rozparsuje retezec reprezentujici vrchol a souradnice x, y, z
	 * @param line retezec reprezentujici vrchol (napr. v obj "v 1 2 3")
	 * @return triprvkove pole s [x,y,z]
	 */
	public Point(String line) {

		String[] s = line.split(" ");
		
		if (s.length > 3) {
			this.x = Double.parseDouble(s[1]);
			this.y = Double.parseDouble(s[2]);
			this.z = Double.parseDouble(s[3]);
		}
		else {
			throw new IllegalArgumentException();
		}
	}

}
