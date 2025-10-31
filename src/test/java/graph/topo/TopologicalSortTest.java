package graph.topo;

import graph.Graph;
import graph.Metrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TopologicalSortTest {

    @Test
    public void testSimpleDAG() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        TopologicalSortResult result = TopologicalSort.sortDAG(graph, new Metrics());

        assertEquals(3, result.order.size());
        assertEquals(0, result.order.get(0));
        assertEquals(1, result.order.get(1));
        assertEquals(2, result.order.get(2));
    }

    @Test
    public void testMultipleValidOrders() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 2);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        TopologicalSortResult result = TopologicalSort.sortDAG(graph, new Metrics());

        assertEquals(4, result.order.size());
        // 0 and 1 can be in any order, but both must come before 2
        assertTrue(result.order.indexOf(0) < result.order.indexOf(2));
        assertTrue(result.order.indexOf(1) < result.order.indexOf(2));
        assertTrue(result.order.indexOf(2) < result.order.indexOf(3));
    }

    @Test
    public void testTopologicalSortValidatesAllVertices() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        // Vertices 3 and 4 are isolated

        TopologicalSortResult result = TopologicalSort.sortDAG(graph, new Metrics());

        assertTrue(result.isValid(graph.n), "Topological sort should include all vertices");
        assertEquals(5, result.order.size());
    }
}
