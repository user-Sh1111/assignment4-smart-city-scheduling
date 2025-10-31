package graph;

import java.util.*;

/**
 * Represents a directed graph using an adjacency list.
 */
public class Graph {
    public final int n; // number of vertices
    public final List<List<Edge>> adj; // adjacency list
    private final boolean weighted;

    public Graph(int n, boolean weighted) {
        this.n = n;
        this.weighted = weighted;
        this.adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    }

    /**
     * Adds a directed edge from u to v with weight w.
     */
    public void addEdge(int u, int v, double w) {
        if (!weighted && w != 1.0) {
            throw new IllegalArgumentException("Unweighted graph must have weight 1.0");
        }
        adj.get(u).add(new Edge(u, v, w));
    }

    /**
     * Adds a directed edge from u to v with default weight 1.0.
     */
    public void addEdge(int u, int v) {
        addEdge(u, v, 1.0);
    }

    /**
     * Gets all edges from node u.
     */
    public List<Edge> getEdges(int u) {
        return adj.get(u);
    }

    /**
     * Gets all edges in the graph.
     */
    public List<Edge> getAllEdges() {
        List<Edge> edges = new ArrayList<>();
        for (List<Edge> list : adj) {
            edges.addAll(list);
        }
        return edges;
    }

    /**
     * Creates a reversed copy of this graph.
     */
    public Graph reverse() {
        Graph reversed = new Graph(n, weighted);
        for (int u = 0; u < n; u++) {
            for (Edge e : adj.get(u)) {
                reversed.addEdge(e.v, e.u, e.w);
            }
        }
        return reversed;
    }
}