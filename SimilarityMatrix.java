import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by xinnacai on 4/3/16. This class reads similarity matrix file and
 * initialize the similarity matrix.
 */
public class SimilarityMatrix {
	private String name; // Name of the matrix
	private double[][] matrix; // Matrix
	private HashMap<String, Integer> hm = new HashMap<>(); // column name -> column number

	/**
	 * construct the similarity matrix by reading a file.
	 * @param fileName the file name
     */
	SimilarityMatrix(String fileName) {
		File file = new File(fileName);
		try {
			Scanner scanner = new Scanner(file);
			if(scanner.hasNextLine()){
				String firstLine = scanner.nextLine();
				String[] array = firstLine.split(",");
				int index = 0;
				for(int i =0;i < array.length;i++){
					if(i == 0){
						this.name = array[i].toLowerCase().trim();
						continue;
					}
					hm.put(array[i],index++);
				}
			}
			matrix = new double[hm.size()][hm.size()];
			int indexr = 0;
			int indexc= 0;
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				String[] value = line.split(",");
				for(int i =0;i < value.length;i++){
					try {
						double d = Double.parseDouble(value[i].trim());
						this.matrix[indexr][indexc++] = d;
					} catch(NumberFormatException e ){

					}
				}
				indexr++;
				indexc = 0;
			}
			scanner.close();
		} catch(FileNotFoundException e){
			System.out.println("can not find file");
		}
	}

	/**
	 * get the name of the matrix.
	 * @return the name
     */
	public String getName(){

		return this.name;

	}

	/**
	 * get similarity value according to index.
	 * @param indexr row index
	 * @param indexc column index
     * @return similarity value
     */
	public double getSimilarity(int indexr, int indexc) {
		// return the similarity to the 2 inputs string
		return matrix[indexr][indexc];
	}

	/**
	 * get index according to the name of attribute.
	 * @param attribute the name of attribute
	 * @return the index
     */
	public int getIndex(String attribute){
		//System.out.println(str);
		return hm.get(attribute.toLowerCase());
	}

}
