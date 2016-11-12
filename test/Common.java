import graaf.Node;
import sec.SecReader;
import sec.SimpleEdgeCodeReader;

import java.io.IOException;

/**
 * Created by domien on 30/10/2016.
 */
public class Common {
    public static void printGraaf(Node[] nodes) {
        for (Node j : nodes) {
            System.out.println("node: "+j.nummer);
            for(Node b : j.buren) {
                System.out.println("    buur: "+b.nummer);
            }
        }
    }

//    public static void main(String[] args) throws IOException {
//        SimpleEdgeCodeReader r;
//        for (int i =1;i<=20;i++) {
//            r = new SecReader("testgrafen/testset/graaf"+i+".sec");
//            System.out.println(i+":"+r.read().length);
//            if(r.read()!=null)
//                System.err.println("dubbel graaf ontdekt");
//            r.close();
//        }
//    }
}
