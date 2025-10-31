package graph.scc;

import graph.Graph;
import graph.Metrics;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCCTest {

    @Test
    public void testSingleComponent() {
        // Graph: 0->1->2->0 (single cycle)
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);

        SCCResult result = SCC.computeSCC(graph);

        assertEquals(1, result.getComponentCount());
        assertEquals(3, result.getComponentSizes().get(0));
        assertTrue(result.components.get(0).contains(0));
        assertTrue(result.components.get(0).contains(1));
        assertTrue(result.components.get(0).contains(2));
    }

    @Test
    public void testMultipleComponents() {
        // Graph: 0->1->2->1 (cycle) and 3->4 (separate component)
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 1); // Cycle: 1->2->1
        graph.addEdge(3, 4);

        SCCResult result = SCC.computeSCC(graph);

        assertEquals(4, result.getComponentCount());

        // Find the cycle component (size 2)
        boolean foundCycle = false;
        for (java.util.List<Integer> component : result.components) {
            if (component.size() == 2) {
                assertTrue(component.contains(1));
                assertTrue(component.contains(2));
                foundCycle = true;
            }
        }
        assertTrue(foundCycle, "Should find cycle component of size 2");
    }

    @Test
    public void testDAGHasAllSingleComponents() {
        // DAG: 0->1->2 (no cycles)
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);

        SCCResult result = SCC.computeSCC(graph);

        assertEquals(3, result.getComponentCount());
        for (java.util.List<Integer> component : result.components) {
            assertEquals(1, component.size());
        }
    }

    @Test
    public void testCondensationGraphIsDAG() {
        // Graph with one cycle and separate nodes
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1);
        graph.addEdge(1, 0); // Cycle: 0<->1
        graph.addEdge(2, 3);

        SCCResult result = SCC.computeSCC(graph);

        // Condensation graph should have vertices = number of components
        assertEquals(result.getComponentCount(), result.condensationGraph.n);

        // Verify no self-loops in condensation graph
        for (int i = 0; i < result.condensationGraph.n; i++) {
            for (graph.Edge edge : result.condensationGraph.getEdges(i)) {
                assertNotEquals(edge.u, edge.v, "Condensation graph should not have self-loops");
            }
        }
    }
}