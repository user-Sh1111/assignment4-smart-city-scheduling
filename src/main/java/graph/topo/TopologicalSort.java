package graph.topo;

import graph.Graph;
import graph.Metrics;
import graph.scc.SCCResult;
import java.util.*;

/**
 * Utility class for topological sorting that handles both regular DAGs and SCC condensation graphs.
 */
public class TopologicalSort {

    /**
     * Performs topological sort on a condensation graph and maps it back to original tasks.
     */
    public static TopologicalSortResult sortCondensationGraph(SCCResult sccResult, Metrics metrics) {
        // Get the condensation DAG
        Graph condensation = sccResult.condensationGraph;

        // Perform topological sort on the condensation graph
        KahnTopologicalSort kahn = new KahnTopologicalSort(condensation, metrics);
        TopologicalSortResult componentOrder = kahn.topologicalSort();

        // Map component order back to original task order
        List<Integer> taskOrder = new ArrayList<>();
        for (int compId : componentOrder.order) {
            // Add all tasks from this component to the final order
            taskOrder.addAll(sccResult.components.get(compId));
        }

        return new TopologicalSortResult(componentOrder.order, taskOrder);
    }

    /**
     * Performs topological sort on a regular DAG.
     */
    public static TopologicalSortResult sortDAG(Graph dag, Metrics metrics) {
        KahnTopologicalSort kahn = new KahnTopologicalSort(dag, metrics);
        return kahn.topologicalSort();
    }
}