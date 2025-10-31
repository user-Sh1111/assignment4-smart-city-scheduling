package graph;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    @Test
    public void testGraphCreation() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 2.5);
        graph.addEdge(1, 2, 1.0);

        assertEquals(3, graph.n);
        assertEquals(2, graph.getAllEdges().size());
        assertEquals(1, graph.getEdges(0).size());
        assertEquals(2.5, graph.getEdges(0).get(0).w);
    }

    @Test
    public void testGraphReverse() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 2.5);
        graph.addEdge(1, 2, 1.0);

        Graph reversed = graph.reverse();

        assertEquals(3, reversed.n);
        assertEquals(2, reversed.getAllEdges().size());
        assertEquals(1, reversed.getEdges(1).size());
        assertEquals(0, reversed.getEdges(1).get(0).v); // Check reverse edge
    }
}