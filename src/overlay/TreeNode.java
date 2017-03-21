package overlay;

/**
 * Uzel stromu
 * @author Jakub Vašta
 */
public class TreeNode extends Node {

    /** Odkaz na leveho potomka */
    private final Node left;
    /**  Odkaz na praveho potomka */
    private final Node right;

    /**
     * Konstruktor
     * @param left odkaz na leveho potomka
     * @param right odkaz na praveho potomka
     * @param point bod, ktery nalezi tomuto uzlu stromu
     */
    public TreeNode(Node left, Node right, Point point) {
        super(point);
        this.left = left;
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

}
