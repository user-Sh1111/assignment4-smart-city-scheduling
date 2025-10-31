import graph.Graph;
import graph.Metrics;
import graph.scc.SCC;
import graph.scc.SCCResult;
import graph.topo.TopologicalSort;
import graph.topo.TopologicalSortResult;
import graph.dagsp.DAGSP;
import graph.dagsp.CriticalPathResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {

    @Test
    public void testCompletePipeline() {
        // Test the complete pipeline on a known graph
        Graph graph = new Graph(6, true);
        // Component 1: 0->1->2->0 (cycle)
        graph.addEdge(0, 1, 1.0);
        graph.addEdge(1, 2, 2.0);
        graph.addEdge(2, 0, 3.0);
        // Component 2: 3->4->5 (linear)
        graph.addEdge(3, 4, 4.0);
        graph.addEdge(4, 5, 5.0);
        // Connection between components
        graph.addEdge(2, 3, 1.0);

        // 1. SCC
        SCCResult sccResult = SCC.computeSCC(graph);
        assertEquals(4, sccResult.getComponentCount());

        // 2. Topological Sort
        TopologicalSortResult topoResult = TopologicalSort.sortCondensationGraph(sccResult, new Metrics());
        assertTrue(topoResult.isValid(sccResult.condensationGraph.n));

        // 3. Critical Path
        CriticalPathResult criticalPath = DAGSP.findCriticalPath(sccResult.condensationGraph, new Metrics());
        assertNotNull(criticalPath);
        assertTrue(criticalPath.length > 0);

        System.out.println("Integration test passed - all algorithms work together correctly");
    }
}