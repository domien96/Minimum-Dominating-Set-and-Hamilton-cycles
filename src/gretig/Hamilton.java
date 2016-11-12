package gretig;

import graaf.Node;
import sec.SecReader;
import sec.SimpleEdgeCodeReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by domien on 30/10/2016.
 */
public class Hamilton {

    public static void main(String[] args) {
        //System.out.println("hamilton");
        main(System.in);

    }

    public static void main(InputStream in) {
        //System.out.println("gretig.Hamilton");
        Node[] nodes;
        try {
            SimpleEdgeCodeReader r = new SecReader(in);
            nodes=r.read();
            if(nodes != null)
                zoekenprintHamiltonCykel(nodes);
            while((nodes=r.read()) != null) {
                System.out.print("\n");
                zoekenprintHamiltonCykel(nodes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("fout bij het inlezen. misschien corrupte invoer?");
            return;
        }
    }

    public static void zoekenprintHamiltonCykel(Node[] graaf) {
        Node[] pad = zoekHamiltonCykel(graaf);
        if(pad != null) {// O(1) only 0 or 1 node in list left normally.
            for(Node n : pad)
                System.out.print(n.nummer+1+" "); // + 1 want opgave telt met nodeindices vanaf 1.
        } else
            System.out.print("Geen cykel gevonden.");

    }

    /**
     * Retourneert null indien geen pad gevonden.
     * Messes up de graaf by the way, buren enzo worden verwijderd, markeervelden aangepast, ... .
     * PRECONDITIE: alle nodes hebben gemarkeerd op false en rij bevat geen null-pointers.
     * samenhangend, triangulair
     * alle fitnesssen op INT.MAX
     * @param nodes
     */
    public static Node[] zoekHamiltonCykel(Node[] nodes) {
        Node[] pad = new Node[nodes.length];
        // starten bij degene met laagste graad, idealiter graad 2 zodat er geen kans is op het kiezen van een verkeerd pad.
        Node start = kiesStartNode(nodes),
                cur = start; // kop van het pad
        // De meest aantrekklijkste buur/richting om het pad voor te zetten.
        Node prefNode = null;

        //
        pad[0]=cur;
        cur.fitness=0;
        int i;
        for (i = 1;i<nodes.length && !(cur.buren.isEmpty());i++) {
            //cur.suggestFitness(cur.fitness+1); // of misschien ? cur.fitness++;
            for(Node buur: cur.buren) {
                if(buur==start)
                    continue;
                buur.suggestFitness(cur.fitness+1);
                if(prefNode == null)
                    prefNode = buur;
                else if(buur.fitness > prefNode.fitness || (buur.fitness==prefNode.fitness && buur.graad<prefNode.graad)){
                    // Zelfde fitness, dan kiezen we voor lagere graad => minder paden => minder kans op foute paden.
                    prefNode = buur;
                }
                if(cur != start) // om final check correct te kunnen uitvoeren, wordt start als buur behouden
                    buur.buren.remove(cur);
            }
            if(prefNode==null)
                break;
            pad[i]= prefNode;
            cur=prefNode;
            prefNode= null;
        }
        // Final check if path is closed.
        if(i == nodes.length && cur.buren.contains(start)) // O(1) only 0 or 1 node in list left normally.
            return pad;
        else
            return null;
    }

    /**
     * Hoogste voorkeur aan graad 2.
     * @param nodes
     * @return
     */
    public static Node kiesStartNode(Node[] nodes) {
        // Used for hamilton
        int hoogsteGraad = -1;
        Node hoogsteGraadNode = null;
        //
        for (Node cur : nodes) {
            if(cur.graad == 2)
                return cur;
            if(cur.graad > hoogsteGraad) {
                hoogsteGraadNode = cur;
                hoogsteGraad = cur.graad;
            }
        }
        return hoogsteGraadNode;
    }
}
