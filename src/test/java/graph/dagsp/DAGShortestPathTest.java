package graph.dagsp;

import graph.Graph;
import graph.Metrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class DAGShortestPathTest {

    @Test
    public void testShortestPathSimpleDAG() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 2.0);
        graph.addEdge(0, 2, 1.0);
        graph.addEdge(1, 3, 3.0);
        graph.addEdge(2, 3, 5.0);

        ShortestPathResult result = DAGSP.shortestPath(graph, 0, new Metrics());

        assertEquals(0.0, result.getDistance(0), 0.001);
        assertEquals(2.0, result.getDistance(1), 0.001);
        assertEquals(1.0, result.getDistance(2), 0.001);
        assertEquals(5.0, result.getDistance(3), 0.001); // 0->1->3 = 2+3=5 is shorter than 0->2->3=1+5=6

        List<Integer> pathTo3 = result.reconstructPath(3);
        assertEquals(3, pathTo3.size());
        assertEquals(0, pathTo3.get(0));
        assertEquals(1, pathTo3.get(1));
        assertEquals(3, pathTo3.get(2));
    }

    @Test
    public void testLongestPathSimpleDAG() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 2.0);
        graph.addEdge(0, 2, 1.0);
        graph.addEdge(1, 3, 3.0);
        graph.addEdge(2, 3, 5.0);

        ShortestPathResult result = DAGSP.longestPath(graph, 0, new Metrics());

        assertEquals(0.0, result.getDistance(0), 0.001);
        assertEquals(2.0, result.getDistance(1), 0.001);
        assertEquals(1.0, result.getDistance(2), 0.001);
        assertEquals(6.0, result.getDistance(3), 0.001); // 0->2->3 = 1+5=6 is longer than 0->1->3=2+3=5

        List<Integer> pathTo3 = result.reconstructPath(3);
        assertEquals(3, pathTo3.size());
        assertEquals(0, pathTo3.get(0));
        assertEquals(2, pathTo3.get(1));
        assertEquals(3, pathTo3.get(2));
    }

    @Test
    public void testCriticalPath() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1, 3.0);
        graph.addEdge(0, 2, 2.0);
        graph.addEdge(1, 3, 4.0);
        graph.addEdge(2, 3, 1.0);
        graph.addEdge(3, 4, 2.0);

        CriticalPathResult result = DAGSP.findCriticalPath(graph, new Metrics());

        // Critical path should be 0->1->3->4 with length 3+4+2=9
        assertEquals(9.0, result.length, 0.001);
        assertEquals(4, result.path.size());
        assertEquals(0, result.path.get(0));
        assertEquals(1, result.path.get(1));
        assertEquals(3, result.path.get(2));
        assertEquals(4, result.path.get(3));
    }

    @Test
    public void testUnreachableVertices() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 2.0);
        graph.addEdge(2, 3, 1.0); // Separate component

        ShortestPathResult result = DAGSP.shortestPath(graph, 0, new Metrics());

        assertEquals(0.0, result.getDistance(0), 0.001);
        assertEquals(2.0, result.getDistance(1), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, result.getDistance(2), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, result.getDistance(3), 0.001);

        List<Integer> pathTo2 = result.reconstructPath(2);
        assertTrue(pathTo2.isEmpty(), "No path should exist to unreachable vertex");
    }

    @Test
    public void testSingleVertexGraph() {
        Graph graph = new Graph(1, true);

        ShortestPathResult shortest = DAGSP.shortestPath(graph, 0, new Metrics());
        ShortestPathResult longest = DAGSP.longestPath(graph, 0, new Metrics());
        CriticalPathResult critical = DAGSP.findCriticalPath(graph, new Metrics());

        assertEquals(0.0, shortest.getDistance(0), 0.001);
        assertEquals(0.0, longest.getDistance(0), 0.001);
        assertEquals(0.0, critical.length, 0.001);
        assertEquals(1, critical.path.size());
        assertEquals(0, critical.path.get(0));
    }
}