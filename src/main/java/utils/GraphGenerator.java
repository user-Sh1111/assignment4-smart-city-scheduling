package utils;

import graph.Graph;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Utility class for generating different types of graphs for testing.
 */
public class GraphGenerator {
    private static final Random random = new Random(42); // Fixed seed for reproducibility

    public static void generateDatasets() throws IOException {
        // Small graphs (6-10 nodes)
        generateSmallDatasets();

        // Medium graphs (10-20 nodes)
        generateMediumDatasets();

        // Large graphs (20-50 nodes)
        generateLargeDatasets();

        System.out.println("All datasets generated successfully in /data/ folder");
    }

    private static void generateSmallDatasets() throws IOException {
        // Dataset 1: Simple DAG
        generateGraph("small_dag", 8, 10, 0.0, 1, 5, "edge");

        // Dataset 2: Graph with one cycle
        generateGraph("small_cycle", 7, 12, 0.2, 0, 4, "edge");

        // Dataset 3: Mixed structure
        generateGraph("small_mixed", 9, 15, 0.3, 2, 6, "edge");
    }

    private static void generateMediumDatasets() throws IOException {
        // Dataset 4: Sparse DAG
        generateGraph("medium_sparse_dag", 15, 18, 0.0, 5, 8, "edge");

        // Dataset 5: Dense with multiple SCCs
        generateGraph("medium_dense_scc", 18, 35, 0.4, 8, 12, "edge");

        // Dataset 6: Balanced mixed
        generateGraph("medium_balanced", 20, 30, 0.25, 10, 15, "edge");
    }

    private static void generateLargeDatasets() throws IOException {
        // Dataset 7: Large sparse DAG
        generateGraph("large_sparse_dag", 35, 45, 0.0, 15, 20, "edge");

        // Dataset 8: Large with complex SCC structure
        generateGraph("large_complex_scc", 45, 80, 0.3, 20, 25, "edge");

        // Dataset 9: Large dense mixed
        generateGraph("large_dense_mixed", 50, 120, 0.2, 25, 30, "edge");
    }

    private static void generateGraph(String name, int n, int targetEdges, double cycleProbability,
                                      int minWeight, int maxWeight, String weightModel) throws IOException {
        Graph graph = createGraph(n, targetEdges, cycleProbability, minWeight, maxWeight);
        saveGraphToJson(graph, name, weightModel);
    }

    private static Graph createGraph(int n, int targetEdges, double cycleProbability,
                                     int minWeight, int maxWeight) {
        Graph graph = new Graph(n, true);
        Set<String> existingEdges = new HashSet<>();
        int edgesAdded = 0;

        // First, ensure the graph is weakly connected
        makeConnected(graph, existingEdges, minWeight, maxWeight);
        edgesAdded = graph.getAllEdges().size();

        // Add remaining edges randomly
        while (edgesAdded < targetEdges) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);

            // Avoid self-loops and duplicate edges
            if (u != v && !existingEdges.contains(u + "->" + v)) {
                // With some probability, create cycles
                if (random.nextDouble() < cycleProbability && edgesAdded > n) {
                    // Try to create a cycle by adding reverse edge if it doesn't exist
                    if (!existingEdges.contains(v + "->" + u)) {
                        graph.addEdge(v, u, randomWeight(minWeight, maxWeight));
                        existingEdges.add(v + "->" + u);
                        edgesAdded++;
                    }
                }

                graph.addEdge(u, v, randomWeight(minWeight, maxWeight));
                existingEdges.add(u + "->" + v);
                edgesAdded++;
            }
        }

        return graph;
    }

    private static void makeConnected(Graph graph, Set<String> existingEdges, int minWeight, int maxWeight) {
        int n = graph.n;
        List<Integer> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodes.add(i);
        }
        Collections.shuffle(nodes, random);

        // Create a random spanning tree to ensure weak connectivity
        for (int i = 0; i < n - 1; i++) {
            int u = nodes.get(i);
            int v = nodes.get(i + 1);
            graph.addEdge(u, v, randomWeight(minWeight, maxWeight));
            existingEdges.add(u + "->" + v);
        }
    }

    private static double randomWeight(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    private static void saveGraphToJson(Graph graph, String name, String weightModel) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        root.put("directed", true);
        root.put("n", graph.n);
        root.put("source", 0); // Default source
        root.put("weight_model", weightModel);

        ArrayNode edges = root.putArray("edges");
        for (graph.Edge edge : graph.getAllEdges()) {
            ObjectNode edgeNode = edges.addObject();
            edgeNode.put("u", edge.u);
            edgeNode.put("v", edge.v);
            edgeNode.put("w", edge.w);
        }

        File outputFile = new File("data/" + name + ".json");
        mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, root);
    }

    public static void main(String[] args) {
        try {
            generateDatasets();
        } catch (IOException e) {
            System.err.println("Error generating datasets: " + e.getMessage());
            e.printStackTrace();
        }
    }
}