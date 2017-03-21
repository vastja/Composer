package overlay;

/**
 * 
 * This class is for keeping coordinates of searching box
 * - max and min values in [x,y,z] coordinates
 * 
 * @author Jakub Vašta
 *
 */
public class Box {

	/** Minimal "X" coordinate of seraching box */
	public final double minX;
	/** Maximal "X" coordinate of searching box */
	public final double maxX;
	/** Minimal "Y" coordinate of seraching box */
	public final double minY;
	/** Maximal "Y" coordinate of searching box */
	public final double maxY;
	/** Minimal "Z" coordinate of seraching box */
	public final double minZ;
	/** Maximal "Z" coordinate of searching box */
	public final double maxZ;
	
	/**
	 * Constructor of seraching box
	 * 
	 * @param minX Minimal "X" coordinate of seraching box
	 * @param maxX Maximal "X" coordinate of searching box
	 * @param minY Minimal "Y" coordinate of seraching box
	 * @param maxY Maximal "Y" coordinate of searching bo
	 * @param minZ Minimal "Z" coordinate of seraching box
	 * @param maxZ Maximal "Z" coordinate of searching bo
	 */
	public Box(double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
		
		this.minX = minX;
		this.maxX = maxX;
		
		this.minY = minY;
		this.maxY = maxY;
		
		this.minZ = minZ;
		this.maxZ = maxZ;
	}
}
