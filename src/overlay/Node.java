package overlay;

/**
 * Abstraktni trida - uzel stromu
 * @author Jakub Vašta
 */
public abstract class Node {
    
    /** Ulozeny bod v uzlu stromu */
    private final Point point;

    /**
     * Konstruktor
     * @param point reference na bod v uzlu
     */
    public Node(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return this.point;
    }

    /**
     * Abstraktni metoda vracejici zda je uzel listem
     * @return pokud je uzel listem TRUE jinak FALSE
     */
    public abstract boolean isLeaf();
}
