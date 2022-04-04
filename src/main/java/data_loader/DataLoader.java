package data_loader;

import graph.IndexDomainVertex;
import graph.RelationshipEdge;
import org.jgrapht.Graph;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.SimpleGraph;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataLoader {

    public static List<Integer> loadBinary(String size) {
        File file = new File("src/main/resources/binary_" + size);
        List<String> charResult = new ArrayList<>();
        List<Integer> intResult = new ArrayList<>();

        try (FileReader fr = new FileReader(file)) {
            int content;
            while ((content = fr.read()) != -1) {
                if (content != '\r' && content != '\n')
                    charResult.add(String.valueOf((char) content));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        charResult = charResult.stream()
                .map(character -> Objects.equals(character, "x") ? "-1" : character)
                .collect(Collectors.toList());
        intResult = charResult.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        return intResult;
    }

    public static Pair<List<Integer>, Graph<IndexDomainVertex, RelationshipEdge>> loadFutoshiki(String size) throws IOException {
        int gridLength = Character.getNumericValue(size.charAt(0));
        var domain = IntStream.range(1, gridLength + 1)
                .boxed()
                .collect(Collectors.toList());

        var graph = new SimpleGraph<IndexDomainVertex, RelationshipEdge>(RelationshipEdge.class);
        File file = new File("src/main/resources/futoshiki_" + size);

        var lines = Files.lines(Path.of("src/main/resources/futoshiki_" + size)).collect(Collectors.toList());
        var charResult = new ArrayList<Character>();
        var intResult = new ArrayList<Integer>();

        int rowNum = 0;
        int numbersRowNum = 0;
        int index = 0;

        for (String line : lines) {
            if (rowNum % 2 == 0) {
                for (int i = 0; i < line.length(); i++) {
                    switch ((Character) line.charAt(i)) {
                        case Character c && (c == '>' || c == '<') -> {
                            int vertex1Index = numbersRowNum * (gridLength) + index;
                            int vertex2Index = numbersRowNum * (gridLength) + index + 1;
                            addVertexes(vertex1Index, vertex2Index, graph, domain, c);
                            index++;
                        }
                        case 'x' -> {
                            intResult.add(-1);
                            addVertex(intResult.size() - 1, graph, domain);
                        }
                        case Character c && Character.getNumericValue(c) > 0 && Character.getNumericValue(c) <= gridLength ->{
                            intResult.add(Character.getNumericValue(c));
                            addVertex(intResult.size() - 1, graph, domain);
                        }
                        default -> index++;

                    }
                }
                numbersRowNum++;
                index = 0;
            } else {
                for (int i = 0; i < line.length(); i++) {
                    switch ((Character) line.charAt(i)) {
                        case Character c && (c == '>' || c == '<') -> {
                            int vertex1Index = (numbersRowNum - 1) * (gridLength) + i;
                            int vertex2Index = (numbersRowNum) * (gridLength) + i;
                            addVertexes(vertex1Index, vertex2Index, graph, domain, c);
                        }
                        default -> {}
                    }
                }
            }
            rowNum++;
        }

        return new Pair<>(intResult, graph);
    }

    private static void addVertexes(int vertex1Index, int vertex2Index, SimpleGraph<IndexDomainVertex, RelationshipEdge> graph, List<Integer> domain, Character c) {
        IndexDomainVertex vertex1 = new IndexDomainVertex(vertex1Index, new ArrayList<>(domain));
        IndexDomainVertex vertex2 = new IndexDomainVertex(vertex2Index, new ArrayList<>(domain));
        graph.addVertex(vertex1);
        graph.addVertex(vertex2);
        graph.addEdge(vertex1, vertex2, new RelationshipEdge(String.valueOf(c)));
    }

    private static void addVertex(int vertexIndex, SimpleGraph<IndexDomainVertex, RelationshipEdge> graph, List<Integer> domain) {
        IndexDomainVertex vertex = new IndexDomainVertex(vertexIndex, new ArrayList<>(domain));
        graph.addVertex(vertex);

    }

    public static Graph<IndexDomainVertex, RelationshipEdge> deepCopyGraph(Graph<IndexDomainVertex, RelationshipEdge> orig) {
        Graph<IndexDomainVertex, RelationshipEdge> copy = new SimpleGraph<>(RelationshipEdge.class);

        var vertexes = orig.vertexSet();
        Set<RelationshipEdge> edges = new HashSet<>();

        var newVertexes = new HashSet<IndexDomainVertex>();

        for (var vertex : vertexes) {
            var newVertex = new IndexDomainVertex(vertex);
            edges.addAll(orig.outgoingEdgesOf(newVertex));
            copy.addVertex(newVertex);

            for(var edge : edges) {
                var newTarget = new IndexDomainVertex(orig.getEdgeTarget(edge));
                copy.addVertex(newTarget);
                try {
                    copy.addEdge(newVertex, newTarget, new RelationshipEdge(edge.getLabel()));
                } catch (IllegalArgumentException e) {
                    //swallow exception
                }
            }
            edges.clear();
        }

        return copy;
    }
}
