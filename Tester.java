/**
 * Please read UpdatedReadMe.txt file to see description and details how to run
 * this C4.5 Algorithm.
 * 
 * @author Shapan Dashore (sdashore).
 *
 */
public class Tester {
    public static void main(String[] args) {

        DecisionTree decisionTree = new DecisionTree();
        if (args.length == 2) {
            String dataFile = args[0];
            String testFile = args[1];
            decisionTree.createOptimalTree(dataFile, 100);
            decisionTree.printResultClassification(testFile);
        } else {
            System.out.println("Please Enter valid arguments");
        }

    }
}
