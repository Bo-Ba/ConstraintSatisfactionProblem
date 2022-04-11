import backtracking.Backtracking;
import data_loader.DataLoader;
import forwardchecking.BinaryForwardChecking;
import forwardchecking.ForwardChecking;
import forwardchecking.ForwardCheckingBase;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        var futoshiki = DataLoader.loadFutoshiki("6x6");
        var binary = DataLoader.loadBinary("10x10");

        Backtracking.isHeuristicMostFrequent = false;
        Backtracking.isHeuristicMostFilled = true;

//        BacktrackingBase futoshikiBacktracking = new FutoshikiBacktracking(futoshiki.getFirst(), futoshiki.getSecond(), 0, 4);
//        Backtracking.startBacktracking(futoshikiBacktracking);
//
//        FutoshikiForwardChecking futoshikiForwardChecking = new FutoshikiForwardChecking(futoshiki.getFirst(), futoshiki.getSecond(), 0 , 6);
//        ForwardChecking.startForwardChecking(futoshikiForwardChecking);

//        BacktrackingBase binaryBacktracking = new BinaryBacktracking(binary, 0, 8);
//        Backtracking.startBacktracking(binaryBacktracking);

        ForwardCheckingBase binaryForwardChecking = new BinaryForwardChecking(binary, 0, 10);
        ForwardChecking.startForwardChecking(binaryForwardChecking);
    }
}
