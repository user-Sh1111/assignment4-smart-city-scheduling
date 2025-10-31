package main;

import graph.*;
import graph.scc.*;
import graph.topo.*;
import graph.dagsp.*;
import java.io.IOException;

/**
 * Main class to demonstrate the complete pipeline of algorithms.
 */
public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== Smart City Scheduling Analysis ===\n");

            // Load graph from JSON
            String dataFile = "data/tasks.json";
            Graph graph = GraphLoader.loadFromJson(dataFile);
            GraphData graphData = GraphLoader.loadGraphData(dataFile);

            System.out.println("Graph loaded:");
            System.out.println("- Vertices: " + graph.n);
            System.out.println("- Edges: " + graph.getAllEdges().size());
            System.out.println("- Source: " + graphData.source);
            System.out.println("- Weight model: " + graphData.weightModel);
            System.out.println();

            // 1. Compute SCC and condensation graph
            System.out.println("1. STRONGLY CONNECTED COMPONENTS");
            Metrics sccMetrics = new Metrics();
            SCCResult sccResult = SCC.computeSCC(graph, sccMetrics);

            System.out.println("Components found: " + sccResult.getComponentCount());
            System.out.println("Component sizes: " + sccResult.getComponentSizes());
            for (int i = 0; i < sccResult.components.size(); i++) {
                System.out.println("  Component " + i + ": " + sccResult.components.get(i));
            }
            System.out.println("SCC Metrics: " + sccMetrics);
            System.out.println();

            // 2. Topological sort on condensation graph
            System.out.println("2. TOPOLOGICAL SORT");
            Metrics topoMetrics = new Metrics();
            TopologicalSortResult topoResult = TopologicalSort.sortCondensationGraph(sccResult, topoMetrics);

            System.out.println("Component order: " + topoResult.order);
            System.out.println("Task execution order: " + topoResult.taskOrder);
            System.out.println("Topo Sort Metrics: " + topoMetrics);
            System.out.println();

            // 3. Shortest paths in DAG (condensation graph)
            System.out.println("3. SHORTEST PATHS IN DAG");
            Metrics spMetrics = new Metrics();
            int sourceComponent = sccResult.componentId[graphData.source];
            ShortestPathResult shortestPath = DAGSP.shortestPath(
                    sccResult.condensationGraph, sourceComponent, spMetrics
            );

            System.out.println("Shortest paths from source component " + sourceComponent + ":");
            for (int i = 0; i < sccResult.condensationGraph.n; i++) {
                double dist = shortestPath.getDistance(i);
                if (dist != Double.POSITIVE_INFINITY) {
                    System.out.println("  to component " + i + ": " + dist);
                }
            }
            System.out.println("Shortest Path Metrics: " + spMetrics);
            System.out.println();

            // 4. Critical path (longest path) in DAG
            System.out.println("4. CRITICAL PATH ANALYSIS");
            Metrics criticalMetrics = new Metrics();
            CriticalPathResult criticalPath = DAGSP.findCriticalPath(
                    sccResult.condensationGraph, criticalMetrics
            );

            System.out.println(criticalPath);
            System.out.println("Critical Path Metrics: " + criticalMetrics);

        } catch (IOException e) {
            System.err.println("Error loading graph data: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}