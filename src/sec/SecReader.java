package sec;

import graaf.Node;

import java.io.*;
import java.io.EOFException;

/**
 * Created by domien on 30/10/2016.
 */
public class SecReader implements SimpleEdgeCodeReader {

    private final InputStream r;
    private int numberSize;

    public SecReader(String filename) throws IOException {
        r = new FileInputStream(filename);
        // read header
        for(int i =0;i<7;i++)
            r.read();
    }

    public SecReader(InputStream in) throws IOException {
        r = in;
        // read header
        for(int i =0;i<7;i++)
            r.read();
    }

    @Override
    public Node[] read() throws IOException {
        Node[] alleNodes = null;
        try {
            // read node size in bytes
            numberSize = readNextnumber(1);
            int n = readNextnumber(); // aantal toppen
            int e = readNextnumber(); // aantal bogen

            if(e > Integer.MAX_VALUE -2)
                System.err.println("limiet overschreden: corruptie mogelijk");

            alleNodes = new Node[n];
            Node[] mapEdgeToNode = new Node[e]; // zo kunnen buren Node objecten elkaar terugvinden

            for (int nodenr=0;nodenr<n;nodenr++) {
                Node cur = new Node(nodenr);
                alleNodes[nodenr] = cur;
                int boognr= readNextnumber();
                while(boognr != 0) {
                    boognr -= 1;  // bogen in file hebben index vanaf 1
                    if(mapEdgeToNode[boognr] == null) {
                        //de boog is nog niet gezien
                        mapEdgeToNode[boognr] = cur;
                    } else {
                        // de buren kunnen elkaar nu erkennen
                        mapEdgeToNode[boognr].nieuweBuur(cur);
                    }
                    boognr = readNextnumber();
                }
            }
        } catch (IOException e1) {
            return null;
        }


        return alleNodes;
    }

    @Override
    public void close() throws IOException {
        this.r.close();
    }

    /**
     * Reads next number according to the size in bytes (@field numberSize).
     * Little endian will be used to calculate total number
     * Throws EOFexception if EOF reached.
     */
    public int readNextnumber(int size) throws IOException {
        int res = 0;
        int deel = 0;
        int factor= 1;
        for(int i =0;i<size;i++) {
            deel = r.read();
            if(deel==-1)
                throw new EOFException();
            res += deel * factor;
            factor *= 256;
        }
        return res;
    }

    public int readNextnumber() throws IOException {
        return readNextnumber(numberSize);
    }
}
