package coinstraints;

import graph.IndexDomainVertex;
import graph.RelationshipEdge;
import org.jgrapht.Graph;
import org.jgrapht.alg.util.Pair;

import java.util.*;

public class FutoshikiConstraints {

    public static boolean checkDuplicates(List<Integer> filledFields) {
        filledFields.removeAll(Collections.singleton(-1));
        Set<Integer> set = new HashSet<Integer>(filledFields);

        return set.size() < filledFields.size();
    }

    public static boolean checkIfGreatest(List<Integer> grid, int index, Graph<IndexDomainVertex, RelationshipEdge> constraintGraph, List<Integer> domain) {
        boolean result = true;

        IndexDomainVertex vertex = new IndexDomainVertex(index, domain);
        if (!constraintGraph.containsVertex(vertex)) return true;
        Set<RelationshipEdge> edges = constraintGraph.outgoingEdgesOf(vertex);

        for (RelationshipEdge edge : edges) {
            switch (edge.getLabel()) {
                case ">" -> result = grid.get(edge.getTarget()) == -1 || grid.get(edge.getSource()) > grid.get(edge.getTarget());
                case "<" -> result = grid.get(edge.getTarget()) == -1 || grid.get(edge.getSource()) < grid.get(edge.getTarget());
            }
            if (!result) return false;
        }

        return true;
    }

    public static void deleteIndexesFromNeighboursDomain(int index, int value, Graph<IndexDomainVertex, RelationshipEdge> constraintGraph) {
        IndexDomainVertex vertex = new IndexDomainVertex(index, null);
        if (!constraintGraph.containsVertex(vertex)) return;
        Set<RelationshipEdge> edges = constraintGraph.outgoingEdgesOf(vertex);

        for (RelationshipEdge edge : edges) {
            switch (edge.getLabel()) {
                case ">" -> {
                    if (index != edge.getTarget()) {
                        var target = edge.getTargetDomain();
                        target.removeIf(i -> i >= value);
                    }

                }
                case "<" -> {
                    if (index != edge.getTarget()) {
                        var target = edge.getTargetDomain();
                        target.removeIf(i -> i <= value);
                    }
                }
            }
        }
    }

    public static void deleteIndexesFromRowAndColumnDomain(int index, int value, Graph<IndexDomainVertex, RelationshipEdge> constraintGraph, int size) {
        IndexDomainVertex vertex = new IndexDomainVertex(index, null);
        if (!constraintGraph.containsVertex(vertex)) return;
        var vertexes = constraintGraph.vertexSet();

        int row = index / size;
        int column = index % size;

        for (int i = row * size; i < (row + 1) * size; i++) {
            int finalI = i;
            vertexes.stream().filter(v -> v.index == finalI).forEach(v -> v.domain.removeIf(d -> d == value));
        }

        while (column < size * size) {
            int finalColumn = column;
            vertexes.stream().filter(v -> v.index == finalColumn).forEach(v -> v.domain.removeIf(d -> d == value));
            column = column + size;
        }
    }
}
