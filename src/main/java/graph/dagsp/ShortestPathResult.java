package graph.dagsp;

import java.util.*;

/**
 * Contains the results of shortest/longest path computation in a DAG.
 */
public class ShortestPathResult {
    public final double[] distances;
    public final int[] predecessors;
    public final int source;

    public ShortestPathResult(double[] distances, int[] predecessors, int source) {
        this.distances = distances;
        this.predecessors = predecessors;
        this.source = source;
    }

    /**
     * Reconstructs the shortest path from source to target.
     * Returns empty list if no path exists.
     */
    public List<Integer> reconstructPath(int target) {
        if (distances[target] == Double.POSITIVE_INFINITY) {
            return new ArrayList<>(); // No path exists
        }

        List<Integer> path = new ArrayList<>();
        for (int v = target; v != -1; v = predecessors[v]) {
            path.add(v);
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * Gets the distance to a target vertex.
     */
    public double getDistance(int target) {
        return distances[target];
    }

    /**
     * Finds the vertex with the maximum distance (for critical path).
     */
    public int getFarthestVertex() {
        int farthest = -1;
        double maxDist = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] != Double.POSITIVE_INFINITY && distances[i] > maxDist) {
                maxDist = distances[i];
                farthest = i;
            }
        }
        return farthest;
    }
}