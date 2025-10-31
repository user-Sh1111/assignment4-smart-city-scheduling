package graph.dagsp;

import java.util.*;

/**
 * Contains the results of critical path analysis.
 */
public class CriticalPathResult {
    public final List<Integer> path;
    public final double length;
    public final int source;
    public final int target;

    public CriticalPathResult(List<Integer> path, double length, int source, int target) {
        this.path = path;
        this.length = length;
        this.source = source;
        this.target = target;
    }

    @Override
    public String toString() {
        return String.format(
                "Critical Path: %s\nLength: %.2f\nSource: %d, Target: %d",
                path, length, source, target
        );
    }
}