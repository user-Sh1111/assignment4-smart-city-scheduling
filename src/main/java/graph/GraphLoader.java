package graph;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 * Utility class for loading graph data from JSON files.
 */
public class GraphLoader {

    public static Graph loadFromJson(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GraphData graphData = mapper.readValue(new File(filePath), GraphData.class);

        // Validate the graph data
        if (graphData.n <= 0) {
            throw new IllegalArgumentException("Number of vertices must be positive");
        }

        // Create graph (always weighted based on the input)
        Graph graph = new Graph(graphData.n, true);

        // Add edges
        for (GraphData.EdgeData edge : graphData.edges) {
            graph.addEdge(edge.u, edge.v, edge.w);
        }

        return graph;
    }

    public static GraphData loadGraphData(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filePath), GraphData.class);
    }
}