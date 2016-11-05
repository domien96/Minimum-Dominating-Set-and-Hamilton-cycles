import graaf.Node;
import sec.SecReader;
import sec.SimpleEdgeCodeReader;

import java.io.IOException;

import static graaf.Sorteerder.sorteerOpGraad;

/**
 * Created by domien on 30/10/2016.
 */
public class Test_sorteerder {
    public static void main(String[] args) throws IOException {
        SimpleEdgeCodeReader r = new SecReader("testgrafen/klein/graaf4.sec");
        Node[] unsorted = r.read();
        Node[] sorted = sorteerOpGraad(unsorted);
        Node prev= sorted[0];
        for(Node j : sorted) {
            if(j.graad<prev.graad)
                System.err.println("ASSERTION FAILED");
            assert (j.graad>= prev.graad);
            prev=j;
        }
    }
}
