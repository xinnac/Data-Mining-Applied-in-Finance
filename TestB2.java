/**
 * Created by xinnacai on 4/8/16.
 */
public class TestB2 {
    public static void main(String[] args) {
        KNNReal knn = new KNNReal(3);
        ProcessData train = new ProcessData("trainProdIntro.real.arff");
//        System.out.println(train.getData());
        ProcessData test = new ProcessData("testProdIntro.real.arff");
        SimilarityMatrix service_type = new SimilarityMatrix("service_typeSM.txt");
        SimilarityMatrix customer = new SimilarityMatrix("customerSM.txt");
        SimilarityMatrix size = new SimilarityMatrix("sizeSM.txt");
        SimilarityMatrix promotion = new SimilarityMatrix("promotionSM.txt");
        knn.addMatrix(promotion.getName(),promotion);
        knn.addMatrix(service_type.getName(),service_type);
        knn.addMatrix(customer.getName(),customer);
        knn.addMatrix(size.getName(),size);
        knn.setAttribute(train.getAttributes());
        knn.setTrainData(train.getRet());
//        knn.trainModel();
        double[] weight = {7.883,1.000,3.721,1.000,0.271,0.767,1.000,8.093};
        knn.setWeight(weight);
        knn.baseLine();
        knn.setTestData(test.getRet());
        knn.executeKNNReal();
        knn.printResult();

//        knn.executeKNN();

        for(int i = 0; i<knn.weight.length;i++){
            System.out.format("%.3f",knn.weight[i]);
            System.out.print(",");
        }

    }
}
