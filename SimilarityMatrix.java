import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by xinnacai on 4/3/16.
 * This class reads similarity matrix file and initialize the similarity matrix.
 */
public class SimilarityMatrix {

	private String matrixName; // Name of the matrix
	private double[][] matrix; // Matrix
	private HashMap<String, Integer> hm; // column name -> column number

	SimilarityMatrix(String filename) {
		Scanner scanner = null;
		File file = new File(filename);

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
				for (int i = 0; i < temp.length; i++) {

					try {
						Double d = Double.parseDouble(temp[i]);
						this.matrix[index][indey++] = d;
					} catch (Exception e) {

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

	public String getName(){

		return this.matrixName;

	}


	public double getSimilarity(int str1, int str2) {
		// return the similarity to the 2 inputs string
		return matrix[str1][str2];
	}

	public int getIndex(String str){
		//System.out.println(str);
		return hm.get(str.toLowerCase());
	}
}
