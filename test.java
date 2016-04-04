/**
 * Created by xinnacai on 4/4/16.
 */
public class Test {
    public static void main(String[] args) {
//        double[] weight = new double[6];
//        for(int i=0;i < weight.length;i++) {
//            if(i == 0) weight[i] = 100;
//            else weight[i] = 1;
//        }
        KNNLabel knn = new KNNLabel(3,0.7,1,100,10);
        ProcessData train = new ProcessData("trainProdSelection.arff");
//        System.out.println(train.getData());
        ProcessData test = new ProcessData("testProdSelection.arff");
        SimilarityMatrix type = new SimilarityMatrix("TypeSM.txt");
//        System.out.println(type.getMatrix());
        SimilarityMatrix lifeStyle = new SimilarityMatrix("LifeStyleSM.txt");
        knn.addMatrix(type.getName(),type);
        knn.addMatrix(lifeStyle.getName(),lifeStyle);
        knn.setAttribute(train.getAttributes());
        knn.setTrainData(train.getData());
        knn.trainModel();
        knn.setTrainData(train.getData());
        knn.setTestData(test.getData());
//        System.out.println(train.getAttributes());

        knn.executeKNN();
        for(int i = 0; i<knn.weight.length;i++){
            System.out.print(knn.weight[i]+",");
        }
        System.out.println();
        System.out.println(knn.computeAccuracy());
        System.out.println(knn.getPredictLable().size());
        for(int i = 0; i< knn.getPredictLable().size();i++){
            System.out.println(i+1+" "+knn.getPredictLable().get(i)+" "+knn.getOringinalLabel().get(i));
        }
    }
}