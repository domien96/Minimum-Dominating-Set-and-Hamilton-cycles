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
        zoekDominanteSet(graaf);
        //nodes = sorteerOpNummer(nodes);
        for (Node j: graaf) {
            if(j.gemarkeerd)
                System.out.print(j.nummer+1+" "); // +1 want opgave telt vanaf 1 ipv 0.
        }
    }

    /**
     * Heuristiek: niet gegarandeerd de optimale oplossing.
     * Zoekt de dominante set voor 1 graaf.
     * Returnt array met evenveel nodes als graaf, maar wel met gemarkeerde velden voor de dominante-toppen.
     * @param graafIN : invoer graaf.
     */
    public static Node[] zoekDominanteSet(Node[] graafIN) {
        Node.referencedNodes = 0;
        Node[] nodes = null;
        // fase 1 : Reduceren van de graaf.
        Node[] graaf = reduceergraaf(graafIN);
        // fase 2 : voeg zo weinig mogelijk toe tot je alle toppen kan bereiken.
        for (int i=0; i<1;i++) {
            nodes = sorteerOpGraad(graaf);
            // Voeg toe op gretige manier.
            for (Node j : nodes) {
                if(!j.deleted && (Node.referencedNodes!= nodes.length || j.locked)) {
                    // alle nodes zijn al bereikbaar, dus kunnen we hier al stoppen.
                    probeerToeTeVoegen(j);
                }
            }
        }
        // fase 3 : probeer zoveel mogelijk te verwijderen
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

    /**
     * PRECONDITIE: graaf bevat precies 1 node met nummer tussen 0 en graaf.length-1
     * @param graaf
     * @return
     */
    private static Node[] reduceergraaf(Node[] graaf) {
        List<Node> n1= new LinkedList<>(),n2 = new LinkedList<>(),n3 = new LinkedList<>();
        int aantalverwijderd = 0;
        boolean[] buurnummers; // true voor alle nodes element van N[v].
        for(Node v : graaf) {
            if(! v.deleted) {
                buurnummers = new boolean[graaf.length];
                buurnummers[v.nummer] = true;
                for(Node b : v.buren) {
                    if (! b.deleted) {
                        buurnummers[b.nummer] = true;
                        b.reducdeerverzamlingnummer = -1;
                    }
                }
                // Opstellen van N1
                for(Node b : v.buren){
                    if(b.locked) {
                        assert (!b.deleted);
                        n1.add(b); // O(1)
                        b.reducdeerverzamlingnummer = 1;
                        continue;
                    }
                    if (! b.deleted) {
                        for(Node bb: b.buren) {
                            if(!bb.deleted &&! buurnummers[bb.nummer]) {
                                n1.add(b); // O(1)
                                b.reducdeerverzamlingnummer = 1;
                                break;
                            }
                        }
                    }
                }
                // Opstellen van N2
                for (Node b : v.buren) {
                    if(!b.deleted && b.reducdeerverzamlingnummer <0) {
                        // b hoort nog tot geen enkele verzameling.
                        for(Node bb : b.buren){
                            if (! bb.deleted && bb.reducdeerverzamlingnummer == 1) {
                                b.reducdeerverzamlingnummer = 2;
                                n2.add(b);
                                break;
                            }
                        }
                    }
                }// Opstellen van N3
                for (Node b : v.buren) {
                    if(! b.deleted && b.reducdeerverzamlingnummer <0) {
                        // b hoort nog tot geen enkele verzameling.
                        b.reducdeerverzamlingnummer = 3;
                        n3.add(b);
                    }
                }
                // Toepassen van de reduceerregel
                if(n3.size() >0) {
                    for(Node n : n2) {
                        if(!n.deleted)
                            aantalverwijderd++;
                        n.deleted = true;
                        assert (!n.locked);
                    }
                    for(Node n : n3) {
                        if(!n.deleted)
                            aantalverwijderd++;
                        n.deleted = true;
                        assert (!n.locked);
                    }
                    v.locked=true;
                }
            }
        }
        Node[] gereduceerd = new Node[graaf.length-aantalverwijderd];
        int idx = 0;
        for(Node n : graaf) {
            if(!n.deleted)
                gereduceerd[idx++] = n;
        }

        return gereduceerd;
    }

    /**
     * Gebruik alleen voor niet verwijderde toppen.
     * Gelockte toppen worden steeds toegevoegd.
     * @param j
     */
    private static void probeerToeTeVoegen(Node j) {
        if(j.deleted)
            return;
        if (! j.locked) {
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
                if (! buur.deleted) {
                    buur.addReference();
                    if( (! j.gemarkeerd) && referencedVoor < Node.referencedNodes) {
                        // Voeg deze node toe, want er is verandering in het aantal te bereiken toppen.
                        j.gemarkeerd = true;
                        j.addReference();
                    }
                }
            }
            if(!j.gemarkeerd) {
                // deze top zorgt niet voor extra referenties naar toppen die voordien nog nit gerefereerd waren.
                // undo dus de vorige operatie
                for(Node buur: j.buren)
                    if(!buur.deleted)
                        buur.deleteReference();
            }
        } else {
            j.gemarkeerd = true;
            j.addReference();
            for(Node b : j.buren) {
                if ( ! b.deleted)
                    b.addReference();
            }
        }

    }

    /**
     * Gebruik deze functie alleen voor gemarkeerde, niet verwijderde toppen.
     * Gelockte toppen worden in geen geval verwijderd.
     * Algoritme:
     * We bekijken elke buur:
     *      We kijken eerst of hij nog tot de graaf behoort dmv het "gemarkeerd" veld.
     *      Indien zo kijken we of er nog andere referenties zijn. Indien dit zo is, kunnen we deze
     *      top verwijderen.
     * @param node
     * @return
     */
    public static boolean probeerTeVerwijderen(Node node) {
        if(!node.gemarkeerd || node.locked || node.deleted)
            return false;
        int n = Node.referencedNodes; // aantal te bereiken toppen
        int count=0;
        node.deleteReference(); // VERGEET NIET TERUG TE ADDEN INDIEN LATER BLIJKT DAT VERWIJDEREN NIET LUKT!
        if(Node.referencedNodes < n) {
            node.addReference();
            return false;
        }
        for(Node buur : node.buren) {
            if (!buur.deleted) {
                buur.deleteReference();
                count++;
                if(Node.referencedNodes < n)
                    break; // stop onmiddelijk en start de undo
            }
        }

        if(Node.referencedNodes < n ) {
            for(Node buur: node.buren) {
                if (!buur.deleted) {
                    if(count==0)
                        break;
                    buur.addReference();
                    count--;
                }
            }
            // gemarkeerd = true;
            node.addReference(); // we zijn het niet vergeten :) (zie hierboven)
            return false;
        }
        node.gemarkeerd = false;
        return true;
    }
}

