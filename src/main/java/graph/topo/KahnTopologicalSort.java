package graph.topo;

import graph.Graph;
import graph.Metrics;
import java.util.*;

/**
 * Implementation of Kahn's algorithm for topological sorting.
 */
public class KahnTopologicalSort {
    private final Graph graph;
    private final Metrics metrics;

    public KahnTopologicalSort(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public TopologicalSortResult topologicalSort() {
        metrics.startTimer();

        // Calculate in-degrees for all vertices
        int[] inDegree = new int[graph.n];
        for (int u = 0; u < graph.n; u++) {
            for (graph.Edge edge : graph.getEdges(u)) {
                inDegree[edge.v]++;
                metrics.queueOperations++; // Count edge processing
            }
        }

        // Initialize queue with all vertices having in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < graph.n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
                metrics.queueOperations++;
            }
        }

        List<Integer> topoOrder = new ArrayList<>();

        // Process vertices
        while (!queue.isEmpty()) {
            int u = queue.poll();
            topoOrder.add(u);
            metrics.queueOperations++;

            // Decrease in-degree of neighbors
            for (graph.Edge edge : graph.getEdges(u)) {
                int v = edge.v;
                inDegree[v]--;
                metrics.queueOperations++;

                if (inDegree[v] == 0) {
                    queue.offer(v);
                    metrics.queueOperations++;
                }
            }
        }

        metrics.stopTimer();

        return new TopologicalSortResult(topoOrder, topoOrder); // For condensation graph, component order = task order
    }
}