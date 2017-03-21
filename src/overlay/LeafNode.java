package overlay;

/**
 * List stromu - dedi tridu Node (Uzel)
 * @author Jakub Vašta
 */
public class LeafNode extends Node {

    /**
     * Konstruktor
     * @param point reference na bod, ktery je v tomto listu
     */
    public LeafNode(Point point) {
        super(point);
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
    
}
