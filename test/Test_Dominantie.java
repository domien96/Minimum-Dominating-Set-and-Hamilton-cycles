import graaf.Node;
import graaf.Sorteerder;
import gretig.Dominantie;
import sec.SecReader;
import sec.SimpleEdgeCodeReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * "Speelbox" voor het testen.
 * Created by domien on 30/10/2016.
 */
public class Test_Dominantie {
    public static void main(String[] args) throws IOException {
        debug();
        //testFile();
    }

    private static void debug() throws IOException {
        String[] x = {
                "testgrafen/klein/alle_5.sec", // directory klein
                "testgrafen/testset/graaf2.sec" // directory testset
        };
        SimpleEdgeCodeReader r = new SecReader(x[1]);
        Node[] graaf ;
        //Common.printGraaf(graaf);
        while((graaf=r.read()) != null) {
            Node[] opl = Dominantie.zoekDominanteSet(graaf);
            System.out.println(isDominant(graaf, opl));
            System.out.println(referentiesKloppen(graaf, opl));
            printDominantie(opl);
        }
        r.close();
        //Common.printGraaf(graaf);
    }

    public static void testFile() throws IOException {
        String path = "testgrafen/klein/alle_5.sec";
        InputStream s = new FileInputStream(path);
        Dominantie.main(s);
        s.close();
    }

    public static boolean isDominant(Node[] graaf, Node[] oplossing) {
        boolean[] visited = new boolean[graaf.length];
        for (Node j : oplossing) {
            if(j.gemarkeerd) {
                visited[j.nummer] = true;
                for(Node buur : j.buren) {
                    visited[buur.nummer] = true;
                }
            }
        }

        for(Node j : graaf) {
            if(!visited[j.nummer])
                return false;
        }
        return true;
    }

    public static boolean referentiesKloppen(Node[] graaf, Node[] oplossing) {
        boolean correct = true;
        if(graaf.length != Node.referencedNodes) {
            System.err.println("Totaal gerefereerde aantal nodes zijn niet gelijk aan aantal toppen.");
            correct = false;
        }
        for(Node j : oplossing) {
            int ref = j.referenced;
            int refwaargenomen= 0;
            if(j.gemarkeerd)
                refwaargenomen=1; // 1 vanwege zichzelf
            for(Node buur : j.buren) {
                if(buur.gemarkeerd)
                    refwaargenomen++;
            }
            if(ref != refwaargenomen) {
                System.err.println("Top met index "+j.nummer+" heeft ref: "+ref+" maar er zijn er " +refwaargenomen);
                return false;
            }
        }
        return correct;
    }

    public static void printDominantie(Node[] nodes) {
        Node[] opl = Sorteerder.sorteerOpNummer(nodes);
        int count=0; // aantal elementen in dominantieverzameling
        for (Node j: opl) {
            if(j.gemarkeerd) {
                System.out.print(j.nummer + " ");
                count++;
            }
        }
        System.out.print("\n");
        System.out.println("Aantal dominantietoppen: "+count);
    }
}
