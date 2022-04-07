import backtracking.Backtracking;
import data_loader.DataLoader;
import forwardchecking.ForwardChecking;
import graph.IndexDomainVertex;
import graph.RelationshipEdge;
import org.jgrapht.Graph;
import org.jgrapht.alg.util.Pair;
import coinstraints.FutoshikiConstraints;
import coinstraints.BinaryConstraints;
import org.jgrapht.graph.SimpleGraph;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
//        System.out.println("Hello world");
//        System.out.println(DataLoader.loadBinary("6x6"));
//        System.out.println(DataLoader.loadFutoshiki("4x4").getFirst());
//        System.out.println(DataLoader.loadFutoshiki("4x4").getSecond());
//        var pair = DataLoader.loadFutoshiki("4x4");
        var binary = DataLoader.loadBinary("8x8");

//        System.out.println(DataLoader.printList(Arrays.asList(1, 1 ,0 , 1, 2, 2, - 1)));
//        pair.getFirst().set(0, 1);
//        pair.getFirst().set(1, 2);
//        System.out.println(FutoshikiConstraints.checkIfGreatest(pair.getFirst(), 0, pair.getSecond()));
        Backtracking.isHeuristicMostFrequent = true;
//        Backtracking.startBacktracking(pair.getFirst(), 0, Arrays.asList(1, 2, 3, 4), pair.getSecond(), 4);

        Backtracking.startBacktrackingBinary(binary, 0, Arrays.asList(0,1 ), 8);
//        ForwardChecking.startForwardChecking(pair.getFirst(), 0, Arrays.asList(1, 2, 3, 4), pair.getSecond(), 4);
//        ForwardChecking.startForwardCheckingBinary(binary, 0, Arrays.asList(0, 1), 8);
//        System.out.println(Backtracking.count);
//
//        int[] test = {4, 5, 6, 7, 8, 9, 1, 1, 1, 13, 14, 15, 16};
//        int[] pattern = {1, 1, 1, 1};
//        System.out.println(BinaryConstraints.isUniqueRows(Arrays.asList(4, 5, 1, 2), 2));

//        List<Integer> binaryGrid = DataLoader.loadBinary("10x10");

//        Backtracking.backtrackingBinary(binaryGrid, 0, Arrays.asList(0, 1), 10, 10);

    }


}
