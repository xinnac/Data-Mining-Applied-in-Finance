import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by xinnacai on 4/3/16.
 * This class reads similarity matrix file and initialize the similarity matrix.
 */
public class SimilarityMatrix {
    private String matrixName;
    private double[][] matrix;
    private HashMap<String, Integer> hm = new HashMap<>();// Map attribute name to matrix index number

    /**
     * initialize similarity matrix.
     * @param fileName
     */
    SimilarityMatrix(String fileName) {

		Scanner scanner = null;
		File file = new File(fileName);

		try {
			scanner = new Scanner(file);

			if (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] temp = line.split(",");
				
				int index = 0;
				hm = new HashMap<String, Integer>();
				for (int i = 0; i < temp.length; i++) {
					temp[i] = temp[i].toLowerCase().trim();
					
					if ("".equals(temp[i])) {
						continue;
					} else if (this.matrixName == null) {
						this.matrixName = temp[i];
					} else {
						hm.put(temp[i], index++);
					}

				}

			}
			int size = hm.size();
			matrix = new double[size][size];
			int index = 0;
			int indey = 0;

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] temp = line.split(",");
				for (int i = 1; i < temp.length; i++) {

					try {
						Double d = Double.parseDouble(temp[i]);
						this.matrix[index][indey++] = d;
					} catch (Exception e) {
                        e.printStackTrace();
					}

				}

				index++;
				indey = 0;

			}

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("No Such File!");
		}  finally {
			if (scanner != null)
				scanner.close();
		}
		

	
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
        return hm.get(attribute.toLowerCase());
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
