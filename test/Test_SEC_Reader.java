import graaf.Node;
import sec.SecReader;
import sec.SimpleEdgeCodeReader;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by domien on 30/10/2016.
 */
public class Test_SEC_Reader {
    public static void main(String[] args) throws IOException {
        testReadAll();
    }

    public static void custom() throws IOException {
        SimpleEdgeCodeReader r = new SecReader("testgrafen/klein/handigraphed.sec");
        Node[] nodes = r.read();
        Common.printGraaf(nodes);
        System.out.println("Tweede graaf");
        Common.printGraaf(r.read());
        r.close();
    }

    public static void testReadAll() throws IOException {
        SimpleEdgeCodeReader r = new SecReader("testgrafen/klein/alle_5.sec");
        Node[] nodes = r.read();
        int aantalGrafen = 1;
        while(r.read()!=null)
            aantalGrafen++;
        assertTrue(aantalGrafen==25);
        r.close();
    }
}


