
/**
 * This class creates the Decision Tree and also has the cross validation method.
 * @author Shapan Dashore (sdashore).
 * @version 2.5.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DecisionTree {
    /**
     * root node of Tree.
     */
    private DecisionTreeNode root;
    /**
     * List of Test Data.
     */
    private List<Integer> testData = new ArrayList<Integer>();
    /**
     * list of attributes.
     */
    private List<Object> abstractAttributes;
    /**
     * combined data.
     */
    private List<DataGroup> combinedData;
    /**
     * List of training Data.
     */
    private List<Integer> trainingData = new ArrayList<Integer>();

    /**
     * Entropy value of root node, it is the max_limit of threshold.
     * 
     * @return the entropy of root
     */
    private double getEntropyOfRoot() {
        HashMap<String, Integer> labelCount = new HashMap<String, Integer>();

        for (int i = 0; i < this.combinedData.size(); i++) {

            String curClassification = combinedData.get(i).getValue(abstractAttributes.size() - 1);
            if (labelCount.containsKey(curClassification)) {

                labelCount.put(curClassification, labelCount.get(curClassification) + 1);
            } else {
                labelCount.put(curClassification, 1);
            }
        }
        double entropy = 0.0;

        for (Integer i : labelCount.values()) {
            entropy = entropy - (i * 1.0 / this.combinedData.size()) * Math.log(i * 1.0 / this.combinedData.size());
        }
        return entropy;

    }

    /**
     * Load all data to the tree.
     * 
     * @param filename
     *            name of file.
     */
    public void loadAllData(String filename) {
        try {
            this.abstractAttributes = new ArrayList<Object>();
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = in.readLine()) != null) {
                line.trim();
                if (line.contains("@attribute")) {
                    String[] res = line.split(" ");
                    if (res[2].charAt(0) == '{' && res[2].charAt(res[2].length() - 1) == '}') {
                        String[] values = res[2].substring(1, res[2].length() - 1).split(",");
                        NominalAttributes attribute = new NominalAttributes(res[1]);
                        for (int i = 0; i < values.length; i++) {
                            attribute.addValue(values[i]);
                        }
                        abstractAttributes.add(attribute);
                    } else {
                        NumericAttributes attribute = new NumericAttributes(res[1]);
                        abstractAttributes.add(attribute);
                    }
                } else if (line.contains("@data")) {
                    break;
                }
            }
            combinedData = new ArrayList<DataGroup>();
            while ((line = in.readLine()) != null) {
                String[] values = line.split(",");
                DataGroup rec = new DataGroup();
                rec.setValueOfAttributes(values);
                combinedData.add(rec);
            }

            in.close();
        } catch (FileNotFoundException e) {

            e.getMessage();
        } catch (IOException e) {

            e.getMessage();
        }
    }

    /**
     * generate a tree based on training data.
     * 
     * @param threshhold
     *            threshhold is used to pre-prune the tree.
     */
    public void createRootNode(double threshhold) {
        DecisionTreeNode root = new DecisionTreeNode(combinedData, abstractAttributes.size() - 1, abstractAttributes, 0,
                threshhold);
        for (int i : trainingData) {
            root.addDataGroup(i);
        }
        root.split();
        this.root = root;
    }

    /**
     * @param num
     *            cross validation based on fold number of data group.
     * @param threshhold
     *            when entropy of leaf node becomes smaller than the threshold
     *            value.
     * @return accuracy of this cross validation
     */
    public double crossValidation(int num, double threshhold) {

        HashMap<Integer, List<Integer>> foldNumGroup = new HashMap<Integer, List<Integer>>();
        for (int i = 0; i < combinedData.size(); i++) {

            if (!foldNumGroup.containsKey(i % num)) {
                foldNumGroup.put(i % num, new ArrayList<Integer>());
            }
            foldNumGroup.get((i % num)).add(i);
        }

        double avgAccuracy = 0;

        for (int i = 0; i < num; i++) {
            testData.clear();
            trainingData.clear();
            for (int j = 0; j < num; j++) {
                if (j == i) {
                    testData.addAll(foldNumGroup.get(j));
                } else {
                    trainingData.addAll(foldNumGroup.get(j));
                }
            }
            createRootNode(threshhold);
            double accuracy = 0;
            int correct = 0;
            int wrong = 0;
            for (int m : testData) {
                DataGroup rec = this.combinedData.get(m);
                if (rec.getValue(abstractAttributes.size() - 1).equals(root.prediction(rec))) {
                    correct++;
                } else {
                    wrong++;
                }
            }
            accuracy = correct * 1.0 / (correct + wrong);
            avgAccuracy += accuracy;
        }
        avgAccuracy /= num;
        return avgAccuracy;
    }

    /**
     * Cross Validation Result is printed here under the filename
     * CrossValidationResult.txt which has the same path as the input files.
     * 
     * @param num
     *            cross validation based on fold number of the group.
     * @param threshhold
     *            when entropy is smaller than threshold value, halt splitting.
     * @param filename
     *            name of input file.
     */
    public void printCrossValidationResult(int num, double threshhold, String filename) {

        HashMap<Integer, List<Integer>> folds = new HashMap<Integer, List<Integer>>();
        double avgAccuracy = 0.0;

        File directory = new File("");

        try {
            BufferedWriter bufwriter = new BufferedWriter(
                    new FileWriter(directory.getAbsolutePath() + "/CrossValidationResult.txt"));
            for (int i = 0; i < combinedData.size(); i++) {

                if (!folds.containsKey(i % num)) {
                    folds.put(i % num, new ArrayList<Integer>());
                }
                folds.get((i % num)).add(i);
            }

            for (int i = 0; i < num; i++) {
                bufwriter.write("\nFold #" + i + "\n");
                testData.clear();
                trainingData.clear();
                for (int j = 0; j < num; j++) {
                    if (j == i) {
                        testData.addAll(folds.get(j));
                    } else {
                        trainingData.addAll(folds.get(j));
                    }
                }
                for (int j = 0; j < this.abstractAttributes.size(); j++) {
                    if (abstractAttributes.get(j) instanceof NumericAttributes)
                        bufwriter.write(((NumericAttributes) this.abstractAttributes.get(j)).getName());
                    else
                        bufwriter.write(((NominalAttributes) this.abstractAttributes.get(j)).getName());
                    bufwriter.write(", ");
                }
                bufwriter.write("classification");
                bufwriter.write("\n");

                createRootNode(threshhold);
                double accuracy = 0;
                int correct = 0;
                int wrong = 0;

                for (int index : testData) {
                    DataGroup dataRecord = this.combinedData.get(index);
                    bufwriter.write("\n");
                    for (String value : dataRecord.getListOfValues()) {
                        bufwriter.write(value + ", ");
                    }
                    String result = root.prediction(dataRecord);
                    bufwriter.write(result);

                    if (dataRecord.getValue(abstractAttributes.size() - 1).equals(result)) {
                        correct++;
                    } else {
                        wrong++;
                    }
                }
                accuracy = correct * 1.0 / (correct + wrong);
                avgAccuracy += accuracy;
                bufwriter.write("\n" + "accuracy is " + accuracy + "\n");
            }
            avgAccuracy /= num;
            bufwriter.write("\naverage accuracy is " + avgAccuracy + "\n");
            bufwriter.close();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    /**
     * Creates optimal tree and prints in into CrossValidationResult.txt
     * 
     * @param filename
     *            filename of the data and also enter the path of file.
     * @param times
     *            steps used to optimize threshold.
     */
    public void createOptimalTree(String filename, int times) {
        this.loadAllData(filename);
        double optimalThreshHold = 0.0;
        double optimalAccuracy = 0.0;
        for (double threshhold = 0.0; threshhold <= getEntropyOfRoot(); threshhold += getEntropyOfRoot() / times) {
            double curAcc = this.crossValidation(10, threshhold);
            if (curAcc > optimalAccuracy) {
                optimalThreshHold = threshhold;
                optimalAccuracy = curAcc;
            }
        }
        printCrossValidationResult(10, optimalThreshHold, filename);
    }

    /**
     * Prints the result test data, in "your testData file path"
     * /RersultTest.txt. (It is the same folder as your input files.)
     * 
     * @param filename
     *            of testData
     */
    @SuppressWarnings("static-access")
    public void printResultClassification(String filename) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String line;
            boolean initData = false;
            File directory = new File("");

            BufferedWriter out = new BufferedWriter(new FileWriter(directory.getAbsolutePath() + "/ResultTest.txt"));
            for (int i = 0; i < abstractAttributes.size(); i++) {
                if (abstractAttributes.get(i) instanceof NumericAttributes)
                    out.write(((NumericAttributes) abstractAttributes.get(i)).getName());
                else
                    out.write(((NominalAttributes) abstractAttributes.get(i)).getName());
                out.write(", ");
            }

            while ((line = in.readLine()) != null) {
                line.trim();
                String[] output = line.split(" ");
                if (output[0].equals("@data")) {
                    initData = true;
                } else {
                    if (initData) {
                        out.newLine();
                        String[] values = line.split(",");
                        for (int i = 0; i < abstractAttributes.size() - 1; i++) {
                            out.write("%-20s".format(values[i], System.getProperty("line.separator")));
                            out.write(" , ");
                        }
                        DataGroup dSet = new DataGroup();
                        dSet.setValueOfAttributes(values);
                        out.write(root.prediction(dSet));
                    }
                }
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {

            e.getMessage();
        } catch (IOException e) {

            e.getMessage();
        }
    }
}
