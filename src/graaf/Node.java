package graaf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by domien on 30/10/2016.
 */
public class Node {
    public static int referencedNodes=0; // +1 for each node that is at least referenced once;

    public final int nummer;
    public List<Node> buren = new ArrayList<>();
    // dominantie : aantal node's die deze node domineren (inclusief zichzelf indien gemarkeerd==true)
    public int referenced;
    public boolean deleted=false; // true indien geen deel v/d gereduceerde graaf (zie reduceerfase)
    public int reducdeerverzamlingnummer = -1;// (zie reduceerfase)
    public boolean locked=false; // true indien deze node een zogezegde virtuele boog bevat. (zie reduceerfase)
    //
    //hamilton : hoe lager, hoe minder aantrekkelijk deze node wordt om gekozen te worden door anderen
    public int fitness = Integer.MAX_VALUE;
    //
    public int graad;
    public boolean gemarkeerd= false; // Gemarkeerd => hoort bij dominantieverzameling/hamilton cykel

    public Node(int i) {
        nummer = i;
    }


    public void nieuweBuur(Node buur) {
        graad++;
        buren.add(buur);
        buur.buren.add(this);
        buur.graad++;
    }

    public void addReference() {
        if(referenced==0)
            referencedNodes++;
        referenced++;
    }

    public void deleteReference() {
        referenced--;
        if(referenced==0)
            referencedNodes--;
    }

    /*
    De laagste fitnes zal steeds gekozen worden.
    Meegegeven Waarden hoger dan de huidige zullen genegeerd worden
     */
    public void suggestFitness(int newFitness) {
        if(this.graad>2) // graad dan 2 moet je altijd nemen
            fitness = Math.min(fitness,newFitness);
    }

    public String toString() {
        return String.valueOf(nummer);
    }
}
