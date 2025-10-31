package graph.dagsp;

import graph.Graph;
import graph.Metrics;
import graph.topo.TopologicalSort;
import graph.topo.TopologicalSortResult;
import java.util.*;

/**
 * Computes shortest and longest paths in a Directed Acyclic Graph (DAG).
 */
public class DAGShortestPath {
    private final Graph dag;
    private final Metrics metrics;

    public DAGShortestPath(Graph dag, Metrics metrics) {
        this.dag = dag;
        this.metrics = metrics;
    }

    /**
     * Computes single-source shortest paths from the given source vertex.
     */
    public ShortestPathResult shortestPath(int source) {
        return computePaths(source, false);
    }

    /**
     * Computes single-source longest paths from the given source vertex.
     * This finds the critical path in project scheduling.
     */
    public ShortestPathResult longestPath(int source) {
        return computePaths(source, true);
    }

    private ShortestPathResult computePaths(int source, boolean longestPath) {
        metrics.startTimer();

        // Initialize distances and predecessors
        double[] dist = new double[dag.n];
        int[] pred = new int[dag.n];
        Arrays.fill(pred, -1);

        if (longestPath) {
            // For longest path, initialize to negative infinity
            Arrays.fill(dist, Double.NEGATIVE_INFINITY);
            dist[source] = 0.0;
        } else {
            // For shortest path, initialize to positive infinity
            Arrays.fill(dist, Double.POSITIVE_INFINITY);
            dist[source] = 0.0;
        }

        // Get topological order of the DAG
        TopologicalSortResult topo = TopologicalSort.sortDAG(dag, new Metrics());
        List<Integer> order = topo.order;

        // Process vertices in topological order
        for (int u : order) {
            if (dist[u] != (longestPath ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY)) {
                for (graph.Edge edge : dag.getEdges(u)) {
                    metrics.edgeRelaxations++;

                    double newDist;
                    if (longestPath) {
                        // For longest path, we invert the logic
                        newDist = dist[u] + edge.w;
                    } else {
                        // For shortest path, standard relaxation
                        newDist = dist[u] + edge.w;
                    }

                    // Relax the edge
                    if (longestPath ? (newDist > dist[edge.v]) : (newDist < dist[edge.v])) {
                        dist[edge.v] = newDist;
                        pred[edge.v] = u;
                    }
                }
            }
        }

        metrics.stopTimer();
        return new ShortestPathResult(dist, pred, source);
    }

    /**
     * Finds the critical path (longest path) in the entire DAG.
     * Returns the path and its length.
     */
    public CriticalPathResult findCriticalPath() {
        // We need to find the longest path from any source to any target
        // This requires running longest path from every vertex that has in-degree 0

        double maxLength = Double.NEGATIVE_INFINITY;
        List<Integer> criticalPath = new ArrayList<>();
        int bestSource = -1;
        int bestTarget = -1;

        // Find potential sources (vertices with in-degree 0)
        List<Integer> sources = new ArrayList<>();
        int[] inDegree = new int[dag.n];
        for (int u = 0; u < dag.n; u++) {
            for (graph.Edge edge : dag.getEdges(u)) {
                inDegree[edge.v]++;
            }
        }
        for (int i = 0; i < dag.n; i++) {
            if (inDegree[i] == 0) {
                sources.add(i);
            }
        }

        // If no obvious sources, try all vertices
        if (sources.isEmpty()) {
            for (int i = 0; i < dag.n; i++) {
                sources.add(i);
            }
        }

        // Find longest path from each source
        for (int source : sources) {
            ShortestPathResult result = longestPath(source);
            int target = result.getFarthestVertex();

            if (target != -1 && result.getDistance(target) > maxLength) {
                maxLength = result.getDistance(target);
                criticalPath = result.reconstructPath(target);
                bestSource = source;
                bestTarget = target;
            }
        }

        return new CriticalPathResult(criticalPath, maxLength, bestSource, bestTarget);
    }
}