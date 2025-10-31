package graph.scc;

import graph.Graph;
import graph.Metrics;

/**
 * Factory class for computing Strongly Connected Components.
 */
public class SCC {

    public static SCCResult computeSCC(Graph graph, Metrics metrics) {
        TarjanSCC tarjan = new TarjanSCC(graph, metrics);
        return tarjan.findSCCs();
    }

    public static SCCResult computeSCC(Graph graph) {
        return computeSCC(graph, new Metrics());
    }
}