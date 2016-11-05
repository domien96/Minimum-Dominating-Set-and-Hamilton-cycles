import graaf.Node;
import gretig.Hamilton;
import sec.SecReader;
import sec.SimpleEdgeCodeReader;

import java.io.IOException;

/**
 * Created by domien on 2/11/2016.
 */
public class Test_hamilton {
    public static void main(String[] args) throws IOException {
        debug();
    }

    private static void debug() throws IOException {
        String[] path= {"testgrafen/klein/triang2.sec"};
        SimpleEdgeCodeReader r = new SecReader(path[0]);
        Node[] graaf = r.read();
        Hamilton.zoekenprintHamiltonCykel(graaf);
    }
}
