package graph.topo;

import java.util.*;

/**
 * Contains the results of topological sorting.
 */
public class TopologicalSortResult {
    public final List<Integer> order; // Order of component IDs
    public final List<Integer> taskOrder; // Order of original task vertices

    public TopologicalSortResult(List<Integer> order, List<Integer> taskOrder) {
        this.order = order;
        this.taskOrder = taskOrder;
    }

    /**
     * Returns true if the sort order is valid (contains all vertices).
     */
    public boolean isValid(int totalVertices) {
        return order.size() == totalVertices;
    }

    @Override
    public String toString() {
        return "Component Order: " + order + "\nTask Order: " + taskOrder;
    }
}