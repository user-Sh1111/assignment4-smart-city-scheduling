package graph.scc;

import graph.Graph;
import graph.Metrics;
import java.util.*;

/**
 * Implementation of Tarjan's algorithm for Strongly Connected Components.
 */
public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics;

    // Tarjan algorithm state
    private int index;
    private int[] indices;
    private int[] lowLinks;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private List<List<Integer>> components;

    public TarjanSCC(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public SCCResult findSCCs() {
        // Initialize algorithm state
        index = 0;
        indices = new int[graph.n];
        Arrays.fill(indices, -1);
        lowLinks = new int[graph.n];
        onStack = new boolean[graph.n];
        stack = new Stack<>();
        components = new ArrayList<>();

        metrics.startTimer();

        // Run DFS for each unvisited vertex
        for (int v = 0; v < graph.n; v++) {
            if (indices[v] == -1) {
                strongConnect(v);
            }
        }

        metrics.stopTimer();

        // Build condensation graph and component mapping
        return buildCondensationGraph();
    }

    private void strongConnect(int v) {
        metrics.dfsVisits++;

        // Set the depth index for v to the smallest unused index
        indices[v] = index;
        lowLinks[v] = index;
        index++;
        stack.push(v);
        onStack[v] = true;

        // Consider successors of v
        for (graph.Edge edge : graph.getEdges(v)) {
            metrics.dfsVisits++; // Count edge exploration

            int w = edge.v;
            if (indices[w] == -1) {
                // Successor w has not yet been visited; recurse on it
                strongConnect(w);
                lowLinks[v] = Math.min(lowLinks[v], lowLinks[w]);
            } else if (onStack[w]) {
                // Successor w is in stack and hence in the current SCC
                lowLinks[v] = Math.min(lowLinks[v], indices[w]);
            }
        }

        // If v is a root node, pop the stack and generate an SCC
        if (lowLinks[v] == indices[v]) {
            List<Integer> component = new ArrayList<>();
            int w;
            do {
                w = stack.pop();
                onStack[w] = false;
                component.add(w);
            } while (w != v);
            components.add(component);
        }
    }

    private SCCResult buildCondensationGraph() {
        // Map each vertex to its component ID
        int[] componentId = new int[graph.n];
        for (int i = 0; i < components.size(); i++) {
            for (int vertex : components.get(i)) {
                componentId[vertex] = i;
            }
        }

        // Build condensation graph (DAG of components)
        Graph condensation = new Graph(components.size(), true);

        // Use a set to avoid duplicate edges between components
        Set<String> edgesAdded = new HashSet<>();

        for (int u = 0; u < graph.n; u++) {
            for (graph.Edge edge : graph.getEdges(u)) {
                int v = edge.v;
                int compU = componentId[u];
                int compV = componentId[v];

                // Add edge between different components
                if (compU != compV) {
                    String edgeKey = compU + "->" + compV;
                    if (!edgesAdded.contains(edgeKey)) {
                        condensation.addEdge(compU, compV, edge.w);
                        edgesAdded.add(edgeKey);
                    }
                }
            }
        }

        return new SCCResult(components, condensation, componentId);
    }
}