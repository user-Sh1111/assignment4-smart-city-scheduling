package graph;

/**
 * Represents a directed edge in a graph from node 'u' to node 'v' with weight 'w'.
 */
public class Edge {
    public final int u;
    public final int v;
    public final double w;

    public Edge(int u, int v, double w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    @Override
    public String toString() {
        return "(" + u + " -> " + v + ", w=" + w + ")";
    }
}