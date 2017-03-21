package overlay;

import java.util.Random;

/**
 * Trida pro hledani medianu
 * @author Jakub Vašta
 */
public class Median {

    /** Objekt pro ziskavani pseudonahodnych cisel */
    private static final Random RND = new Random();

    
    /**
     * Metoda vracejici pozici prvku, ktery je median (respektive prvku na k-te pozici)
     * @param a pole x-ovych souradnic bodu
     * @param b pole y-ovych souradnic bodu
     * @param c pole z-ovych souradnic bodu
     * @param bot dolni index - brany jako pocatek pole v kterem hladame prvek na urcite pozici
     * @param top horni index  - brany jako konec pole v kterem hledame prvek na urcite pozici
     * @param pos kolikaty nejvetší prvek (index prvku ve vzestupne serazenem poli)
     * @return hledany prvek
     */
    public static double findMedian(Double[] a, Double[] b, Double[] c, int bot, int top, double pos) {

        // Je-li prohledavany usek mensi nebo roven petiprvkovemu, tak jej seradime a vybereme hledany prvek
        if (top - bot < 5) {
           
            // Razeni pomoci bubble sort
            for (int i = 0; i < top - bot; i++) {
                for (int j = bot; j < top - i; j++) {
                    if (a[j] > a[j + 1]) {
                        swap(a, b, c, j, j + 1);
                    }
                }
            }
            
            // Pokud hledame prvek, ktery je primo v poli (tzn. neni to prumer dvou prvku)
            if ((pos * 10) % 10 == 0) {
                return a[(int) pos];
            // Pokud je to prumer dvou sousednich prvku 
            } 
            else {
                return (a[(int) pos] + a[(int) pos + 1]) / 2.;
            }
            
        } 
        else {
            
            // Nahodne vybereme pivot
            double pivot = a[RND.nextInt(top - bot) + bot];
            
            // Prvky stejne jako pivot hazime na kraje pole a posouváme hranice cisel kde nejsou cisla rovna pivotum
            int pright = top;
            int pleft = bot;

            int left = bot;
            int right = top;

            // Jako Quick sort prohazujeme pole podle pivota
            while (left < right) {
                if (a[left] <= pivot) {
                    // Pokud je prvek roven pivotu, tak ho presuneme na kraj pole
                    if (Double.compare(a[left], pivot) == 0) {
                        swap(a, b, c, left, pleft);
                        pleft++;
                    }
                    left++;
                } else if (a[right] >= pivot) {
                    // Pokud je prvek roven pivotu, tak ho presuneme na kraj pole
                    if (Double.compare(a[right], pivot) == 0) {
                        swap(a, b, c, pright, right);
                        pright--;
                    }
                    right--;
                } else {
                    swap(a, b, c, left, right);
                }
            }

            double botmed = left - (pleft - bot);
            double topmed = left + top - pright - 1;

            // Podle toho v jake skupine se prvek nachazi - mensi, vetsi, nebo roven pivotu - tak hladame v dane skupine
            if (pos > topmed) {
                return findMedian(a, b, c, right, pright, pos - (top - pright));
            } else if (pos < botmed) {
                return findMedian(a, b, c, pleft, left - 1, pos + (pleft - bot));
            } else {
                return pivot;
            }

        }

    }

    /**
     * Metoda prohodi dva prvky na urcitych indexech ve trech polich, tzn. prohodi bod ve trech souradnicich (x, y, z)
     * @param a pole x-ovych souradnic bodu
     * @param b pole y-ovych souradnic bodu
     * @param c pole z-ovych souradnic bodu
     * @param left index prvniho prohazovaneho bodu
     * @param right index druheho prohazovaneho bodu
     */
    private static void swap(Double a[], Double b[], Double c[], int left, int right) {

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
}
