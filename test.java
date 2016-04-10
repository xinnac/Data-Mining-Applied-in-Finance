/**
 * Created by xinnacai on 4/4/16.
 */
public class Test {
    public static void main(String[] args) {
        KNNLabel knn = new KNNLabel(3);
        ProcessData train = new ProcessData("trainProdSelection.arff");
//        System.out.println(train.getData());
        ProcessData test = new ProcessData("testProdSelection.arff");
        SimilarityMatrix type = new SimilarityMatrix("TypeSM.txt");
//        System.out.println(type.getMatrix());
        SimilarityMatrix lifeStyle = new SimilarityMatrix("LifeStyleSM.txt");
        knn.addMatrix(type.getName(),type);
        knn.addMatrix(lifeStyle.getName(),lifeStyle);
        knn.setAttribute(train.getAttributes());
        knn.setTrainData(train.getRet());
//        knn.trainModel();
//        knn.setTrainData(train.getData());
        knn.setTestData(test.getRet());
        double[] weight = {1,1,1,1,1,1};
        knn.setWeight(weight);
        knn.baseLine();
//        knn.executeKNN();
//        knn.printResult();
//        knn.baseLine();
//        System.out.println(knn.computeAccuracy());
//        for(int i = 0; i<knn.weight.length;i++){
//            System.out.format("%.3f",knn.weight[i]);
//            System.out.print(",");
//        }

    }
}
