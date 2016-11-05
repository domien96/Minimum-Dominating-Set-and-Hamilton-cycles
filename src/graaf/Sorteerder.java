package graaf;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by domien on 30/10/2016.
 */
public class Sorteerder {

    /*
    Hoogste naar kleinste graad.
    Volgens hetprincipe van counting sort.
    Complexiteit O(n+k) met k hoogste graad, maar in vlakke grafen is k= O(n). Hieruit volgt dat de
    complexiteit van deze functie O(n) is voor vlakke grafen.
     */
    public static Node[] sorteerOpGraad(Node[] ongesorteerd) {
        // 1 vind maximum graad
        int maxgraad=0;
        for(Node j : ongesorteerd) {
            if (j.graad > maxgraad)
                maxgraad = j.graad;
        }
        List<Node>[] map = new List[maxgraad+1]; // +1 om leesbaarder te werken, bovendient ondersteunt dit ook toppen
        // zonder buren.

        // Groepeer toppen adhv hun graad
        for(Node j : ongesorteerd) {
            if(map[j.graad] == null)
                map[j.graad] = new LinkedList<>();
            map[j.graad].add(j);
        }
        // Bouw gesorteerde op adhv de map.
        Node[] gesorteerd = new Node[ongesorteerd.length];
        int idx=0;
        for(int i =maxgraad;i>=0;i--) {
            if(map[i] != null) {
                // alle nodes met dezelfde graad worden hieronder overlopen.
                for(Node j : map[i])
                    gesorteerd[idx++] = j;
            }
        }
        return gesorteerd;
    }

    /*
    Voor een array van lengte n zijn is er precies 1 node voor elk getal tussen 1 en n.
     */
    public static Node[] sorteerOpNummer(Node[] nodes) {
        Node[] gesorteerd = new Node[nodes.length];
        for (Node j : nodes)
            gesorteerd[j.nummer] = j;
        return gesorteerd;
    }
}
