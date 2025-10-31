package graph;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents the graph data structure from JSON input.
 */
public class GraphData {
    @JsonProperty("directed")
    public boolean directed;

    @JsonProperty("n")
    public int n;

    @JsonProperty("edges")
    public List<EdgeData> edges;

    @JsonProperty("source")
    public int source;

    @JsonProperty("weight_model")
    public String weightModel;

    public static class EdgeData {
        @JsonProperty("u")
        public int u;

        @JsonProperty("v")
        public int v;

        @JsonProperty("w")
        public double w;
    }
}