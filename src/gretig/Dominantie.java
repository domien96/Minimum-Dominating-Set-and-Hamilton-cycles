package gretig;

import graaf.Node;
import sec.SecReader;
import sec.SimpleEdgeCodeReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static graaf.Sorteerder.sorteerOpGraad;
import static graaf.Sorteerder.sorteerOpNummer;

/**
 * Created by domien on 30/10/2016.
 */
public class Dominantie {
    public static void main(String[] args) {
        main(System.in);
    }

    public static void main(InputStream in) {
        //System.out.println("gretig.Dominantie");
        Node[] nodes;
        try {
            SimpleEdgeCodeReader r = new SecReader(in);
            nodes=r.read();
            if(nodes != null)
                zoekenprintDominanteSet(nodes);
            while((nodes=r.read()) != null) {
                System.out.print("\n");
                zoekenprintDominanteSet(nodes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("fout bij het inlezen. misschien corrupte invoer?");
            return;
        }
    }

    public static void zoekenprintDominanteSet(Node[] graaf) {
        Node[] nodes = zoekDominanteSet(graaf);
        nodes = sorteerOpNummer(nodes);
        for (Node j: nodes) {
            if(j.gemarkeerd)
                System.out.print(j.nummer+1+" "); // +1 want opgave telt vanaf 1 ipv 0.
        }
    }

    /**
     * Heuristiek: niet gegarandeerd de optimale oplossing.
     * Zoekt de dominante set voor 1 graaf.
     * Returnt array met evenveel nodes als graaf, maar wel met gemarkeerde velden voor de dominante-toppen.
     * @param graaf : invoer graaf.
     */
    public static Node[] zoekDominanteSet(Node[] graaf) {
        Node.referencedNodes = 0;
        Node[] nodes = null;
        // fase 1 : voeg zo weinig mogelijk toe tot je alle toppen kan bereiken.
        for (int i=0; i<1;i++) {
            nodes = sorteerOpGraad(graaf);
            // Voeg toe op gretige manier.
            for (Node j : nodes) {
                if(Node.referencedNodes== nodes.length)
                    // alle nodes zijn al bereikbaar, dus kunnen we hier al stoppen.
                    break;
                probeerToeTeVoegen(j);
            }
        }
        // fase 2 : probeer zoveel mogelijk te verwijderen
        for (int run=0;run<1;run++) {
            // fase 2 : verwijder zoveel mogelijk uit D.
            for (int i = nodes.length - 1; i >= 0; i--) {
                Node j = nodes[i];
                if (j.gemarkeerd)
                    probeerTeVerwijderen(j);

            }
        }
        return nodes;
    }

    private static void probeerToeTeVoegen(Node j) {
        //Gebruik om te Kijken of het nuttig is deze toe te voegen. Vanaf we een verandering in referencednodes
        //ondervinden kunnen we doorgaan.
        int referencedVoor = Node.referencedNodes;

        // 1 We kijken of de top zelf al dan niet bereikbaar was, indien niet hoort hij zowiezo bij de oplossing.
        j.addReference();
        if(Node.referencedNodes > referencedVoor)
            j.gemarkeerd = true;
        else
            j.deleteReference();
        // 2 Kijken of er eerder onbereikbare buren waren
        for(Node buur: j.buren) {
            buur.addReference();
            if( (! j.gemarkeerd) && referencedVoor < Node.referencedNodes) {
                // Voeg deze node toe, want er is verandering in het aantal te bereiken toppen.
                j.gemarkeerd = true;
                j.addReference();
            }
        }
        if(!j.gemarkeerd) {
            // deze top zorgt niet voor extra referenties naar toppen die voordien nog nit gerefereerd waren.
            // undo dus de vorige operatie
            for(Node buur: j.buren)
                buur.deleteReference();
        }
    }

    /**
     * Gebruik deze functie alleen voor gemarkeerde toppen.
     * Algoritme:
     * We bekijken elke buur:
     *      We kijken eerst of hij nog tot de graaf behoort dmv het "gemarkeerd" veld.
     *      Indien zo kijken we of er nog andere referenties zijn. Indien dit zo is, kunnen we deze
     *      top verwijderen.
     * @param node
     * @return
     */
    public static boolean probeerTeVerwijderen(Node node) {
        if(!node.gemarkeerd)
            return false;
        int n = Node.referencedNodes; // aantal te bereiken toppen
        int count=0;
        node.deleteReference(); // VERGEET NIET TERUG TE ADDEN INDIEN LATER BLIJKT DAT VERWIJDEREN NIET LUKT!
        if(Node.referencedNodes < n) {
            node.addReference();
            return false;
        }
        for(Node buur : node.buren) {
            buur.deleteReference();
            count++;
            if(Node.referencedNodes < n)
                break; // stop onmiddelijk en start de undo
        }

        if(Node.referencedNodes < n ) {
            for(Node buur: node.buren) {
                if(count==0)
                    break;
                buur.addReference();
                count--;
            }
            // gemarkeerd = true;
            node.addReference(); // we zijn het niet vergeten :) (zie hierboven)
            return false;
        }
        node.gemarkeerd = false;
        return true;
    }
}

