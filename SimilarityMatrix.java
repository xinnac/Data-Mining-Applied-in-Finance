import java.util.HashMap;

/**
 * Created by xinnacai on 4/3/16.
 * This class reads similarity matrix file and initialize the similarity matrix.
 */
public class SimilarityMatrix {
    private String matrixName;
    private double[][] matrix;
    private HashMap<String, Integer> indexMap = new HashMap<>();// Map attribute name to matrix index number

    /**
     * initialize similarity matrix.
     * @param fileName
     */
    SimilarityMatrix(String fileName) {

    }

    /**
     * get matrix name.
     * @return matrix name
     */
    public String getMatrixName() {
        return matrixName;
    }

    public void setMatrixName(String matrixName) {
        this.matrixName = matrixName;
    }

    /**
     * get column or row index according to the value of attribute.
     * @param attribute
     * @return column or row index
     */
    public int getIndex(String attribute) {
        return indexMap.get(attribute.toLowerCase());
    }

    /**
     * get similarity according the attribute values of two different instance
     * @param attribute1
     * @param attribute2
     * @return similarity score
     */
    public double getSimilarity(String attribute1, String attribute2){
        return matrix[getIndex(attribute1)][getIndex(attribute2)];
    }
}
