# Smart City Scheduling - Assignment 4
- **Name**: Balgatay Shynar
- **Course**: Design and Analysis of Algorithms 
- **Date**: 31 October 2025

## Project Overview
This project implements graph algorithms for smart city task scheduling, handling both cyclic and acyclic dependencies in service tasks (street cleaning, repairs, sensor maintenance). The system detects cyclic dependencies via SCC compression and optimizes scheduling via topological ordering and critical path analysis.

## Objectives
- **Detect cycles** in task dependencies using Strongly Connected Components (SCC)
- **Compress cycles** into single nodes in the condensation graph (DAG)
- **Schedule tasks** optimally using topological sorting
- **Identify critical paths** for project planning using longest path analysis


### Algorithms Implemented
1. **Strongly Connected Components (SCC)** - Tarjan's algorithm
2. **Topological Sorting** - Kahn's algorithm  
3. **Shortest/Longest Paths in DAG** - Dynamic programming over topological order

### Package Structure
```
src/main/java/
├── graph/               # Core graph model
│   ├── Edge.java       # Directed edge representation
│   ├── Graph.java      # Adjacency list graph
│   ├── GraphLoader.java # JSON deserialization
│   └── Metrics.java    # Performance tracking
├── graph/scc/          # Strongly Connected Components
│   ├── SCC.java        # Factory class
│   ├── TarjanSCC.java  # Tarjan's algorithm implementation
│   └── SCCResult.java  # SCC results container
├── graph/topo/         # Topological Sorting
│   ├── TopologicalSort.java      # Main interface
│   ├── KahnTopologicalSort.java  # Kahn's algorithm
│   └── TopologicalSortResult.java
├── graph/dagsp/        # DAG Shortest Paths
│   ├── DAGSP.java              # Factory class
│   ├── DAGShortestPath.java    # Algorithm implementation
│   ├── ShortestPathResult.java
│   └── CriticalPathResult.java
├── utils/
│   └── GraphGenerator.java     # Dataset generation
└── main/
    ├── Main.java              # Main demonstration
    └── GenerateDatasets.java  # Dataset generator
```

## Datasets

The project includes **10 datasets** in `/data/` folder:

| Category | Name | Vertices | Edges | Structure | Description |
|----------|------|----------|-------|-----------|-------------|
| **Small** | `small_dag.json` | 8 | 10 | DAG | Simple directed acyclic graph |
| **Small** | `small_cycle.json` | 7 | 12 | Cyclic | Graph with cycle structures |
| **Small** | `small_mixed.json` | 9 | 15 | Mixed | Combination of cycles and DAG |
| **Medium** | `medium_sparse_dag.json` | 15 | 18 | Sparse DAG | Larger acyclic graph |
| **Medium** | `medium_dense_scc.json` | 18 | 35 | Dense SCC | Multiple strongly connected components |
| **Medium** | `medium_balanced.json` | 20 | 30 | Balanced | Mixed structure with balanced density |
| **Large** | `large_sparse_dag.json` | 35 | 45 | Sparse DAG | Large-scale acyclic graph |
| **Large** | `large_complex_scc.json` | 45 | 80 | Complex SCC | Complex cyclic dependencies |
| **Large** | `large_dense_mixed.json` | 50 | 120 | Dense Mixed | High-density mixed graph |
| **Example** | `tasks.json` | 8 | 7 | Mixed | Original assignment example |

## Quick Start

### Prerequisites
- **Java**: 11 or higher
- **Maven**: 3.6 or higher
- **Git**: (optional) for version control

### Installation & Build
```bash
# Clone the repository
git clone <repository-url>
cd assignment4-smart-city-scheduling

# Compile the project
mvn compile

# Run tests to verify installation
mvn test
```

### Running the Application

**1. Main Demonstration (on tasks.json):**
```bash
mvn exec:java
```

**2. Generate New Datasets:**
```bash
mvn compile exec:java -Dexec.mainClass="main.GenerateDatasets"
```

**3. Run Specific Tests:**
```bash
# Run all tests
mvn test

# Run specific test suites
mvn test -Dtest=TarjanSCCTest
mvn test -Dtest=TopologicalSortTest  
mvn test -Dtest=DAGShortestPathTest
mvn test -Dtest=DatasetValidationTest
```

## Performance Metrics

The system tracks comprehensive performance metrics:

- **Execution Time**: Nanosecond precision timing
- **DFS Visits**: Node visits in SCC algorithm
- **Edge Relaxations**: Operations in shortest path algorithms
- **Queue Operations**: Enqueue/dequeue counts in topological sort

### Example Output:
```
SCC Metrics: Time: 49000 ns, DFS Visits: 15, Edge Relaxations: 0, Queue Operations: 0
Topo Sort Metrics: Time: 45100 ns, DFS Visits: 0, Edge Relaxations: 0, Queue Operations: 20
Shortest Path Metrics: Time: 38700 ns, DFS Visits: 0, Edge Relaxations: 3, Queue Operations: 0
```

## Algorithm Details

### Strongly Connected Components (Tarjan's Algorithm)
- **Complexity**: O(V + E)
- **Features**: 
  - Single-pass DFS for efficiency
  - Builds condensation graph automatically
  - Handles disconnected graphs

### Topological Sorting (Kahn's Algorithm)  
- **Complexity**: O(V + E)
- **Features**:
  - Works on original graphs and condensation graphs
  - Validates sort order completeness
  - Maps component order back to task order

### DAG Shortest/Longest Paths
- **Complexity**: O(V + E)
- **Features**:
  - Leverages topological order for efficiency
  - Finds critical path (longest path) for project planning
  - Reconstructs optimal paths

## Testing

The project includes comprehensive test coverage:

- **15 JUnit tests** across all modules
- **Unit tests** for individual algorithms
- **Integration tests** for complete pipeline
- **Dataset validation** for all generated graphs

```bash
# Test Results Summary
Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
```

## JSON Input Format

Example `tasks.json` structure:
```json
{
  "directed": true,
  "n": 8,
  "edges": [
    {"u": 0, "v": 1, "w": 3},
    {"u": 1, "v": 2, "w": 2},
    {"u": 2, "v": 3, "w": 4},
    {"u": 3, "v": 1, "w": 1}
  ],
  "source": 4,
  "weight_model": "edge"
}
```

## Results Analysis

### Key Findings:
1. **SCC Efficiency**: Tarjan's algorithm effectively compresses cyclic dependencies
2. **Scheduling Optimality**: Topological sort provides valid execution sequences
3. **Critical Path Insight**: Longest path analysis identifies project bottlenecks
4. **Scalability**: Linear time complexity maintained across graph sizes

### Performance Characteristics:
- **Small graphs** (6-10 nodes): < 0.1ms execution time
- **Medium graphs** (10-20 nodes): 0.1-0.5ms execution time  
- **Large graphs** (20-50 nodes): 0.5-2ms execution time