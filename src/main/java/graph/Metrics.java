package graph;

/**
 * Utility class to track algorithm performance metrics.
 */
public class Metrics {
    private long startTime;
    private long endTime;

    // Counters for different operations
    public int dfsVisits;
    public int edgeRelaxations;
    public int queueOperations;

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        endTime = System.nanoTime();
    }

    public long getElapsedTime() {
        return endTime - startTime;
    }

    public void reset() {
        dfsVisits = 0;
        edgeRelaxations = 0;
        queueOperations = 0;
    }

    @Override
    public String toString() {
        return String.format(
                "Time: %d ns, DFS Visits: %d, Edge Relaxations: %d, Queue Operations: %d",
                getElapsedTime(), dfsVisits, edgeRelaxations, queueOperations
        );
    }
}