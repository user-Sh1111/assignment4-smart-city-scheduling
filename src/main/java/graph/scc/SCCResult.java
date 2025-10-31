package graph.scc;

import graph.Graph;
import java.util.*;

/**
 * Contains the results of SCC computation: list of components and condensation graph.
 */
public class SCCResult {
    public final List<List<Integer>> components;
    public final Graph condensationGraph;
    public final int[] componentId; // Maps vertex -> component ID

    public SCCResult(List<List<Integer>> components, Graph condensationGraph, int[] componentId) {
        this.components = components;
        this.condensationGraph = condensationGraph;
        this.componentId = componentId;
    }

    /**
     * Returns the number of strongly connected components.
     */
    public int getComponentCount() {
        return components.size();
    }

    /**
     * Returns the sizes of all components.
     */
    public List<Integer> getComponentSizes() {
        List<Integer> sizes = new ArrayList<>();
        for (List<Integer> component : components) {
            sizes.add(component.size());
        }
        return sizes;
    }
}