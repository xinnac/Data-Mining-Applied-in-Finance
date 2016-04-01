import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;




public class SomeFunctions {

	private  class Node {




		//		= {"student":1,"engineer":2,"librarian":3,"professor":4,"doctor":5};
		public int type;
		public int lifestyle;
		public double 	vacation;
		public double 	ecredit;
		public double 	salary;
		public double 	property;
		public String 	label;
		public String flag;
		public double points;
		public Node(String line){
			HashMap<String , Integer> map1=new HashMap<String,Integer>();
			map1.put("student", 0);
			map1.put("engineer", 1);
			map1.put("librarian", 2);
			map1.put("professor", 3);
			map1.put("doctor", 4);
			HashMap<String , Integer> map2=new HashMap<String,Integer>();
			map2.put("spend<<saving", 0);
			map2.put("spend<saving", 1);
			map2.put("spend>saving", 2);
			map2.put("spend>>saving", 3);


			String[] parts=line.split(" ");
			type= map1.get(parts[0]);
			lifestyle= map2.get(parts[1]);
			vacation= Double.parseDouble(parts[2]);
			ecredit= Double.parseDouble(parts[3]);
			salary= Double.parseDouble(parts[4]);
			property= Double.parseDouble(parts[5]);
			if (parts[6]!=null){	label= parts[6];  }

		}
	}


	public static double distance (double[] w,Node a,Node b){

		/**Type Similarity matrix
		 * 			student engineer librarian professor doctor
		 * student-1
		 * engineer-2
		 * librarian-3
		 * professor-4
		 * doctor-5
		 * */
//		double[][] matrix1 = {
//				{1, (25/38+31/49)/2, (8/38+9/23)/2, (8/38+9/39)/2, (21/38+37/37)/2},
//				{(25/38+31/49)/2, 1, (12/49+9/23)/2, (30/49+27/39)/2, (12/49+9/37)/2},
//				{(8/38+9/23)/2, (12/49+9/23)/2, 1, (23/23+21/39)/2, (9/23+9/37)/2},
//				{(8/38+9/39)/2, (30/49+27/39)/2, (23/23+21/39)/2,1,(9/39+9/37)/2},
//				{(21/38+37/37)/2, (12/49+9/37)/2, (9/23+9/37)/2, (9/39+9/37)/2, 1}}; not the correct matrix...
		double[][] matrix1 = {
				{1, 0,0, 0, 0},
				{0, 1, 0, 0, 0},
				{0, 0, 1,0, 0},
				{0, 0, 0,1,0},
				{0, 0, 0, 0, 1}};



		/**LifeStyle Similarity matrix
		 * 	spend<<saving    spend<saving    spend>saving   spend>>saving
		 * spend<<saving    
		 * spend<saving    
		 * spend>saving   
		 * spend>>saving
		 * */
//		double[][] matrix2 = {
//				{1, (20/20+15/41)/2, (20/20+28/87)/2, (20/20+20/38)/2},
//				{(20/20+15/41)/2, 1, (41/41+69/87)/2, (15/41+20/38)/2},
//				{(20/20+28/87)/2, (41/41+69/87)/2, 1, (46/87+38/38)/2},
//				{(20/20+20/38)/2, (15/41+20/38)/2, (46/87+38/38)/2, 1}}; testing...
		double[][] matrix2 = {
				{1, 0, 0, 0},
				{0, 1, 0, 0},
				{0, 0, 1, 0},
				{0, 0,0, 1}};


		double dist_type = 1-matrix1[a.type][b.type];
		double dist_litestyle = 1-matrix2[a.lifestyle][b.lifestyle];
		double dist_vacation =Math.abs( a.vacation-b.vacation);
		double dist_ecredit = Math.abs( a.ecredit-b.ecredit);
		double dist_salary = Math.abs( a.salary-b.salary);
		double dist_property  =Math.abs( a.property-b.property);

		double distance = Math.sqrt((w[0]*dist_type*dist_type
				+w[1]*dist_litestyle*dist_litestyle
				+w[2]*dist_vacation*dist_vacation
				+w[3]*dist_ecredit*dist_ecredit
				+w[4]*dist_salary*dist_salary
				+w[5]*dist_property*dist_property));
		return distance;}
	public static String vote (ArrayList<Node> nodes){
		HashMap<String, Double> points = new HashMap<String, Double>();
		points.put("C1", (double) 0);
		points.put("C2", (double) 0);
		points.put("C3", (double) 0);
		points.put("C4", (double) 0);
		points.put("C5", (double) 0);

		for ( Node a :nodes){points.put(a.label, points.get(a.label)+a.points);}
		String flag = "C1";
		if( points.get("C2")>points.get(flag)) flag = "C2";
		if( points.get("C3")>points.get(flag)) flag = "C3";
		if( points.get("C4")>points.get(flag)) flag = "C4";
		if( points.get("C5")>points.get(flag)) flag = "C5";

		return flag;

	}
	public static void main( String[] args) throws IOException{
//		double[] weight = {1,1,8,50,6,1}; for testing...
		double[] weight = {1,1,1,1,1,1};

		int size_test = 40;
		int size_train = 146;
		int k = 3;
		ArrayList<Node> train_nodes = new ArrayList<Node>();
		ArrayList<Node> test_nodes = new ArrayList<Node>();
		ArrayList<Node> vote_nodes = new ArrayList<Node>();
		int[] checks = new int[size_test];
		Core core = new Core();

		BufferedReader bf = new BufferedReader( new FileReader("input.txt"));
		String line="";
		for(int i = 0 ; i < size_train ; i++){
			line =	bf.readLine();
			train_nodes.add(new Integer(i),core.new Node (line));
		}

		for(int i = 0 ; i < size_test ; i++){
			line =	bf.readLine();
			test_nodes.add(new Integer(i),core.new Node (line));
		}
		for ( int i = 0; i < size_test ;i ++){
			HashMap<Integer,Double> distances = new HashMap<Integer,Double>();

			for ( int j = 0; j< size_train; j++){
				double distance1 = distance(weight,test_nodes.get(i),train_nodes.get(j)); 

				train_nodes.get(j).points = 1/distance1;
			}
			Collections.sort(train_nodes, new Comparator<Node>() {

				public int compare(Node o1, Node o2) {
					if ( o1.points>o2.points){
						return -1;
					}
					else
						return 1;
				}
			});
			for ( int m = 0 ; m < k; m++){
			vote_nodes.add(train_nodes.get(m));}

			test_nodes.get(i).flag = vote(vote_nodes);
			
			System.out.println(test_nodes.get(i).label+":"+test_nodes.get(i).flag);
			checks[i]=0;
			 if (test_nodes.get(i).label.equals(test_nodes.get(i).flag)) checks[i]=1;
				System.out.println(""+checks[i]);

		}
		int sum = 0;
		for (int l = 0 ; l<size_test ; l++){
			sum = sum + checks[l];
		}
		System.out.println(""+sum);

		double accuracy = (double)sum/(double)size_test;
		System.out.println(accuracy);

  bf.close();

	}
}
