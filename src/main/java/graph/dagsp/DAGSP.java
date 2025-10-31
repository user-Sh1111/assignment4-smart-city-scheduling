package graph.dagsp;

import graph.Graph;
import graph.Metrics;

/**
 * Factory class for DAG shortest/longest path computations.
 */
public class DAGSP {

    public static ShortestPathResult shortestPath(Graph dag, int source, Metrics metrics) {
        DAGShortestPath sp = new DAGShortestPath(dag, metrics);
        return sp.shortestPath(source);
    }

    public static ShortestPathResult longestPath(Graph dag, int source, Metrics metrics) {
        DAGShortestPath sp = new DAGShortestPath(dag, metrics);
        return sp.longestPath(source);
    }

    public static CriticalPathResult findCriticalPath(Graph dag, Metrics metrics) {
        DAGShortestPath sp = new DAGShortestPath(dag, metrics);
        return sp.findCriticalPath();
    }
}