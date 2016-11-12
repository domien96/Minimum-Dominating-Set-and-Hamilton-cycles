import graaf.Node;
import gretig.Hamilton;
import sec.SecReader;
import sec.SimpleEdgeCodeReader;

import java.io.IOException;
import static org.junit.Assert.*;

/**
 * Created by domien on 2/11/2016.
 */
public class Test_hamilton {
    public static void main(String[] args) throws IOException {
        //debug();
        test();
    }

    private static void debug() throws IOException {
        String[] path= {"testgrafen/klein/graaf2opgave.sec"};
        SimpleEdgeCodeReader r = new SecReader(path[0]);
        Node[] graaf = r.read();
        Hamilton.zoekenprintHamiltonCykel(graaf);
        r.close();
    }

    private static void test() throws IOException {
        for (int i =5; i<10;i++) {
            String p = "testgrafen/klein/triang_alle_X.sec";
            p = p.replace("X",String.format("%02d",i));
            System.out.println("\nNow testing-->"+p);
            SimpleEdgeCodeReader r_donttouch = new SecReader(p); // gebruikt voor de originele graaf
            SimpleEdgeCodeReader r_touch = new SecReader(p); // gebruikt om cykel op te vinden
            Node[] graaf, cykel;
            int graafnr=1;
            while((graaf=r_donttouch.read()) != null) {
                cykel = Hamilton.zoekHamiltonCykel(r_touch.read());
                if(cykel != null) {
                    assertCykel(graaf, cykel);
                    System.out.println("V "+graafnr+++" Correcte cykel");
                } else
                    System.out.println("X "+graafnr+++" Geen cykel");
            }
            r_donttouch.close();
            r_touch.close();
        }
    }

    private static void assertCykel(Node[] graaf, Node[] cykel) {
        assertTrue("Cykel en graaf niet evenveel toppen",graaf.length == cykel.length);
        if(cykel.length==1)
            return;

        Node prev = cykel[cykel.length-1], cur;
        int next=0;
        do {
            cur = cykel[next++];
            assertNotNull(prev);
            assertNotNull(cur);

            // Controleren van buren op originele graaf, want de andere is gemuteerd.
            Node origPrev = graaf[prev.nummer], origCur = graaf[cur.nummer];
            assertTrue("Cykel bevat niet-buren",origPrev.buren.contains(origCur));
            assertTrue("Cykel bevat niet-buren",origCur.buren.contains(origPrev));
            prev = cur;
        } while (next<cykel.length);

        // Every node must be in cycle.
        boolean[] marked = new boolean[graaf.length];
        for (Node n : cykel)
            marked[n.nummer] = true;
        for (Node n : graaf)
            assertTrue("Niet gemarkeerd: "+n.nummer,marked[n.nummer]);
    }
}
