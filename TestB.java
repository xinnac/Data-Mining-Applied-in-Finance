/**
 * Created by xinnacai on 4/8/16.
 */
public class TestB {
    public static void main(String[] args) {
        KNNLabel knn = new KNNLabel(3);
        ProcessData train = new ProcessData("trainProdIntro.binary.arff");
//        System.out.println(train.getData());
        ProcessData test = new ProcessData("testProdIntro.binary.arff");
        SimilarityMatrix service_type = new SimilarityMatrix("service_typeSM.txt");
        SimilarityMatrix customer = new SimilarityMatrix("customerSM.txt");
        SimilarityMatrix size = new SimilarityMatrix("sizeSM.txt");

//        System.out.println(type.getMatrix());
        SimilarityMatrix promotion = new SimilarityMatrix("promotionSM.txt");
        knn.addMatrix(promotion.getName(),promotion);
        knn.addMatrix(service_type.getName(),service_type);
        knn.addMatrix(customer.getName(),customer);
        knn.addMatrix(size.getName(),size);
        knn.setAttribute(train.getAttributes());
        knn.setTrainData(train.getData());
//        knn.trainModel();
//        knn.setTrainData(train.getData());
//        double[] weight = {0.780,8.297,9.169,8.102,0.190,0.054,0.041,5.795};
        double[] weight = {1,1,1,1,1,1,1,1};
        knn.setWeight(weight);
        knn.baseLine();
        knn.setTestData(test.getData());
        knn.executeKNN();
        knn.printResult();

//        System.out.println(knn.computeAccuracy());
//        for(int i = 0; i<knn.weight.length;i++){
//            System.out.format("%.3f",knn.weight[i]);
//            System.out.print(",");
//        }

    }
}
