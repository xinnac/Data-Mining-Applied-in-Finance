/**
 * Created by xinnacai on 4/4/16.
 */
public class TestA {
    public static void main(String[] args) {
        KNNLabel knn = new KNNLabel(3);
        ProcessData train = new ProcessData("trainProdSelection.arff");
        ProcessData test = new ProcessData("testProdSelection.arff");
        SimilarityMatrix type = new SimilarityMatrix("Type.txt");
        SimilarityMatrix lifeStyle = new SimilarityMatrix("LifeStyle.txt");
        knn.addMatrix(type.getName(),type);
        knn.addMatrix(lifeStyle.getName(),lifeStyle);
        knn.setAttribute(train.getAttributes());
        knn.setTrainData(train.getRet());
        knn.trainModel();
        knn.setTrainData(train.getRet());
        knn.baseLine();
        knn.setTrainData(train.getRet());
        double[] weight = {4.74,0.01,1.00,5.89,1.00,8.88};
        System.out.print("the weight vector we select: ");
        for(double d : weight){
            System.out.print(d+",");
        }
        System.out.println();
        knn.setWeight(weight);
        knn.crossValidation();
        knn.setTrainData(train.getRet());
        knn.setTestData(test.getRet());
        knn.executeKNN();
        knn.printResult();
    }
}
