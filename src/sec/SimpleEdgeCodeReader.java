package sec;

import graaf.Node;

import java.io.IOException;

/**
 * Created by domien on 30/10/2016.
 */
public interface SimpleEdgeCodeReader {

    /*
    Reads next graph
     */
    Node[] read() throws IOException;

    /*
    Close
     */
    void close() throws IOException;
}
