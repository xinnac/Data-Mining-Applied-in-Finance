import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Created by xinnacai on 4/3/16. This class reads similarity matrix file and
 * initialize the similarity matrix.
 */
public class SimilarityMatrix {
	private String matrixName;
	private double[][] matrixArr;
	private Hashtable<String, Integer> map = new Hashtable<String, Integer>();// Map
																				// attribute
																				// name
																				// to
																				// matrix
																				// index
																				// number

	/**
	 * initialize similarity matrix.
	 * 
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
				populateMatrix(temp);
			}
			int mapSize = map.size();
			matrixArr = new double[mapSize][mapSize];

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] temp = line.split(",");
				populateMat(temp);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("No Such File!");
		} finally {
			if (scanner != null)
				scanner.close();
		}

	}

	public void populateMatrix(String[] temp) {
		int index = 0;
		map = new Hashtable<String, Integer>();
		for (int i = 0; i < temp.length; i++) {
			temp[i] = temp[i].toLowerCase().trim();

			if ("".equals(temp[i])) {
				continue;
			} else if (this.matrixName == null) {
				this.matrixName = temp[i];
			} else {
				map.put(temp[i], index++);
			}
		}
	}

	public void populateMat(String[] temp) {
		int row = 0;
		int column = 0;
		for (int i = 0; i < temp.length; i++) {
			try {
				Double d = Double.parseDouble(temp[i]);
				this.matrixArr[row][column++] = d;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		row++;
		column = 0;
	}

	/**
	 * get matrix name.
	 * 
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
	 * 
	 * @param attribute
	 * @return column or row index
	 */
	public int getIndex(String attribute) {
		return map.get(attribute.toLowerCase());
	}

	/**
	 * get similarity according the attribute values of two different instance
	 * 
	 * @param attribute1
	 * @param attribute2
	 * @return similarity score
	 */
	public double getSimilarity(String attribute1, String attribute2) {
		return matrixArr[getIndex(attribute1)][getIndex(attribute2)];
	}
}
