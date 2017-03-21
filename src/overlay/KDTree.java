package overlay;

/**
 * Trida s KD stromem sestavenym na mnozine bodu v prostoru (x, y, z)
 * @author Jakub Vašta
 */
public class KDTree {
	
    /** Pole x-ovych souradnic bodu */
    private final Double[] x;
    /** Pole y-ovych souradnic bodu */
    private final Double[] y;
    /** Pole z-ovych souradnic bodu */
    private final Double[] z;
    /** Kren stromu */
    private final Node root;
    /** Box container of points space */
    private final Box box;

    /**
     * Konstruktor
     * @param x pole x-ovych souradnic bodu
     * @param y pole y-ovych souradnic bodu
     * @param z pole z-ovych souradnic bodu
     */
    public KDTree(Double[] x, Double[] y, Double[] z) {
        this.x = x;
        this.y = y;
        this.z = z;
        
        this.root = build(0, x.length - 1, 'x');
        this.box = findSearchBox();
    }

    /**
     * Metoda vytvarejici KD strom 
     * @param bot pocatecni index v poli bodu
     * @param top koncovi index v poli bodu
     * @param dimension dimenze podle ktere se na dane urovni stavi strom
     * @return refernece na uzel
     */
    private Node build(int bot, int top, char dimension) {

        double med;
        int posPivot;
        char nextDimension = ' ';
        Node node, leftTree, rightTree;
        Point point;
        posPivot = 0;

        // Podle dimenze najdeme median, preskupime podle medianu
        switch (dimension) {
            case 'x':
                med = median(x, y, z, bot, top);
                posPivot = reorder(x, y, z, bot, top, med);
                nextDimension = 'y';
                break;

            case 'y':
                med = median(y, x, z, bot, top);
                posPivot = reorder(y, x, z, bot, top, med);
                nextDimension = 'z';
                break;

            case 'z':
                med = median(z, y, x, bot, top);
                posPivot = reorder(z, y, x, bot, top, med);
                nextDimension = 'x';
                break;

            default:
                System.out.println("ERROR not valid dimension <x, y, z>");
                break;
        }

        // Vytvorime novy bod na zaklade nalezeneho pivota
        point = new Point(x[posPivot], y[posPivot], z[posPivot]);

        // Sestrojime levy podstrom
        if (posPivot - bot > 1) {
            leftTree = build(bot, posPivot - 1, nextDimension);
        } else if (posPivot - bot == 1) {
            leftTree = new LeafNode(new Point(x[bot], y[bot], z[bot]));
        } else {
            leftTree = null;
        }

        // Sestrojime pravy podstrom
        if (top - posPivot > 1) {
            rightTree = build(posPivot + 1, top, nextDimension);
        } else if (top - posPivot == 1) {
            rightTree = new LeafNode(new Point(x[top], y[top], z[top]));
        } else {
            rightTree = null;
        }

        // Na novy uzel navazeme pravy a levy podstrom
        node = new TreeNode(leftTree, rightTree, point);

        return node;
    }

    /**
     * Metoda preskupujici pole podle daneho pivota
     * @param a pole x-ovych souradnic bodu
     * @param b pole y-ovych souradnic bodu
     * @param c pole z-ovych souradnic bodu
     * @param left pocatecni idnex pole, ktere radime
     * @param right koncovy index pole, ktere radime
     * @param pivot prvek podle ktereho rozdelime pole na vetsi a mensi cast
     * @return index prostredniho bodu
     */
    private int reorder(Double[] a, Double[] b, Double[] c, int left, int right, Double pivot) {

        int posPivot = -1;
        int posSwap;

        // Prohazime pole podle pivota
        while (left < right) {

            if (Double.compare(a[left], pivot) <= 0) {
                if (0 == Double.compare(a[left], pivot)) {
                    posPivot = left;
                }
                left++;
            } else if (Double.compare(a[right], pivot) >= 0) {
                if (0 == Double.compare(a[right], pivot)) {
                    posPivot = right;
                }
                right--;
            } else {
                pointsSwap(a, b, c, left, right);
            }

        }

        // Preradime pivota na misto kam patri
        if (posPivot == - 1) {
            return left;
        } else {
            if (posPivot > left) {
                if (a[left] > pivot) {
                    posSwap = left;
                } else {
                    posSwap = left - 1;
                }
            } else {
                if (a[left] > pivot) {
                    posSwap = left - 1;
                } else {
                    posSwap = left;
                }
            }

            pointsSwap(a, b, c, posPivot, posSwap);
            return posSwap;
        }
    }

    /**
     * Metoda prohazuje dva body na pozicich levy a pravy Souradnice X,Y,Z jsou
     * ulozeny ve trech polich, proto prohazuje ve trech polich
     *
     * @param left pozice prvniho bodu k prohozeni
     * @param right pozice druheho bodu k prohozeni
     */
    private void pointsSwap(Double[] a, Double[] b, Double[] c, int left, int right) {

        double t;

        t = a[left];
        a[left] = a[right];
        a[right] = t;

        t = b[left];
        b[left] = b[right];
        b[right] = t;

        t = c[left];
        c[left] = c[right];
        c[right] = t;

    }

    /**
     * Najde nejblizsi bod v KDTree
     * @param point bod k teremu hledame nejblizsi v KDTree
     * @return vzdalenost od nejblizsiho bodu
     */
    public Double findNearest(Point point) {
    	return findNearest(point, this.root, Double.MAX_VALUE, 'x');
    }
    
    /**
     * Metoda hledajici v KD strome nejblizsi bod
     *
     * @param point bod ke kteremu hledame nejblizsi bod v KD strome
     * @param node uzel KD stromu - na zacatku je to koren (root)
     * @param min zatim nejblizsi nalezeny bod
     * @param dimension v ktere dimenzi nyni hledame - x,y,z
     * @return vzdalenost od nejblizsiho bodu
     */
    private Double findNearest(Point point, Node node, Double min, char dimension) {
        
        // Ukoncovaci podmina
        if (node == null) {
            return min;
        }

        Point temp = node.getPoint();
        Double distance = Math.sqrt(x_square(temp.x - point.x) + x_square(temp.y - point.y) + x_square(temp.z - point.z));

        if (Double.compare(distance, min) < 0) {
            min = distance;
        }
        
        // Ukoncovaci podminka
        if (node.isLeaf()) {
            return min;
        }

        Double rs = 0.;
        char nextDimension = ' ';

        // Spocitame zda nemuze byt nejaky bod blize
        switch (dimension) {
            case 'x':
                rs = point.x - temp.x;
                nextDimension = 'y';
                break;
            case 'y':
                rs = point.y - temp.y;
                nextDimension = 'z';
                break;
            case 'z':
                rs = point.z - temp.z;
                nextDimension = 'x';
                break;
            default:
                System.out.println("ERROR not valid dimension");
                break;
        }

        // Pokud muze byt bliz, tak ho  vyhledame
        if (Double.compare(rs, 0.) <= 0) {
            min = findNearest(point, ((TreeNode) node).getLeft(), min, nextDimension);

            if (Double.compare(Math.abs(rs), min) <= 0) {
                min = findNearest(point, ((TreeNode) node).getRight(), min, nextDimension);
            }

        }

        if (Double.compare(rs, 0.) >= 0) {
            min = findNearest(point, ((TreeNode) node).getRight(), min, nextDimension);

            if (Double.compare(Math.abs(rs), min) <= 0) {
                min = findNearest(point, ((TreeNode) node).getLeft(), min, nextDimension);
            }

        }

        return min;
    }

    /**
     * Umocni promenou na druhou
     *
     * @param x umocnovane cislo
     * @return druha mocnina zadaneho cisla
     */
    private double x_square(Double x) {
        return x * x;
    }

    /**
     * Metoda vracejici median pole a 
     * @param a pole v kterem hledame median - bud x-ove, y-ove nebo z-ove souradnice 
     * @param b pole souradnic, bud x-ove, y-ove nebo z-ove - podle ostatnich poli 
     * @param c pole souradnic, bud x-ove, y-ove nebo z-ove - podle ostatnich poli 
     * @param bot pocatecni index pole, v kterem hledame
     * @param top koncovy index pole v kterem hledame
     * @return hledany mmedian
     */
    public double median(Double[] a, Double[] b, Double[] c, int bot, int top) {
        return Median.findMedian(a, b, c, bot, top, bot + (top - bot) / 2);
    }

    /**
     * Method search for minimal and maximal [X,Y,Z] coordinates of KDTree
     * - we can close the KDtree points space to 3D cube
     * @return Box container 
     */
    private Box findSearchBox() {
    	
    	double maxX = Double.MIN_VALUE;
    	double minX = Double.MAX_VALUE;
    	double maxY = Double.MIN_VALUE;
    	double minY = Double.MAX_VALUE;
    	double maxZ = Double.MIN_VALUE;
    	double minZ = Double.MAX_VALUE;
    	
    	for (int i = 0; i < x.length; i++) {
    		
    		if (maxX < x[i]) {
    			maxX = x[i];
    		}
    		
    		if (minX < x[i]) {
    			minX = x[i];
    		}
    		
    		if (maxY < y[i]) {
    			maxY = y[i];
    		}
    		
    		if (minY < y[i]) {
    			minY = y[i];
    		}
    		
    		if (maxZ < z[i]) {
    			maxZ = z[i];
    		}
    		
    		if (minZ < z[i]) {
    			minZ = z[i];
    		}
    	}
    	
    	return new Box(minX, maxX, minY, maxY, minZ, maxZ);
    }
    
    /**
     * Get method for box
     * @return box
     */
    public Box getSearchingBox() {
    	return this.box;
    }
    
    /**
     * Find out if given point is in seraching box of KDTree
     * @param point point for detection
     * @return true - point lays in seraching box, false - point doesnt lay ins earching box
     */
    public boolean isInSearchingBox(Point point) {
    	
    	boolean isValidX = (Double.compare(point.x, box.maxX) <= 0) && (Double.compare(point.x, box.minX) >= 0);
    	boolean isValidY = (Double.compare(point.y, box.maxY) <= 0) && (Double.compare(point.y, box.minY) >= 0);
    	boolean isValidZ = (Double.compare(point.z, box.maxZ) <= 0) && (Double.compare(point.z, box.minZ) >= 0);
    	
    	return isValidX && isValidY && isValidZ;
    			
    }
    
   
}
