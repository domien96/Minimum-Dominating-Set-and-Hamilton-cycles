import graaf.Node;

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
}
