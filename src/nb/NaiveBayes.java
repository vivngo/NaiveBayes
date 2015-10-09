package nb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class NaiveBayes {
	
	static ArrayList<ArrayList<Boolean>> trainData = new ArrayList<ArrayList<Boolean>>();
	static ArrayList<ArrayList<Boolean>> testData = new ArrayList<ArrayList<Boolean>>();
	static ArrayList<String> attributeNames = new ArrayList<String>();
	static double[] condProbClass1 = new double[attributeNames.size()];
	static double[] condProbClass2 = new double[attributeNames.size()];
	static double probClass1 = 0, probClass2 = 0;
	
	public static void main(String[] args) throws IOException {
		// Terminate program with error message if not 2 command line arguments
		if (args.length != 2) {
			System.out.println("This program accepts exactly 2 arguments: a training file, and a test file.");
			System.out.println("Please try again. Program terminating.");
			System.exit(0);
		}
		
		//Store attribute names in attributeNames;
		//Store binary data in trainData:
		parseTrainData(args[0]);
		
		for (int i = 0; i < trainData.size(); i++) {
			if (trainData.get(i).get(attributeNames.size())) {
				probClass1++;
			} 
		}
		probClass1 = probClass1 * 1.0 / trainData.size();
		probClass2 = 1.0 - probClass1;
		
		condProbClass1 = calculateConditionalProbabilities(true);
		condProbClass2 = calculateConditionalProbabilities(false);
		
		printClassifier();
		
	}
	
	public static double[] calculateConditionalProbabilities(boolean classValue) {
		double[] condProbs = new double[attributeNames.size()];
		double numMatch=0;
		double numTrue=0;
		
		for (int k=0; k<attributeNames.size()-1; k++) {
			
			for (int i=0; i<trainData.size(); i++) {
				
				if(trainData.get(i).get(attributeNames.size())==classValue) {
					numMatch++;
					if (trainData.get(i).get(k))
						numTrue++;
				}
			}
			
			condProbs[k] = (double)numTrue / numMatch;
			numMatch = 0;
			numTrue = 0;
		}

		return condProbs;
	}
	
	//method to print classifier
	public static void printClassifier() {
		
		DecimalFormat df = new DecimalFormat("0.00");
		
		System.out.print("P(C=c1)=" + df.format(probClass1) + " ");
		for (int k=0; k<attributeNames.size(); k++) {
			System.out.print("P(");
			System.out.print(attributeNames.get(k));
			System.out.print("=1|c1)=");
			System.out.print(df.format(condProbClass1[k]) + " ");
			
			System.out.print("P(");
			System.out.print(attributeNames.get(k));
			System.out.print("=0|c1)=");
			System.out.print(df.format(1-condProbClass1[k]) + " ");
		}
		System.out.println();
		
		System.out.print("P(C=c2)=" + df.format(probClass2) + " ");
		for (int k=0; k<attributeNames.size(); k++) {
			System.out.print("P(");
			System.out.print(attributeNames.get(k));
			System.out.print("=1|c2)=");
			System.out.print(df.format(condProbClass2[k]) + " ");
			
			System.out.print("P(");
			System.out.print(attributeNames.get(k));
			System.out.print("=0|c2)=");
			System.out.print(df.format(1-condProbClass2[k]) + " ");
		}
		
	}
	
	//method to read training data file and store in two dimensional boolean arraylist
		public static void parseTrainData(String fileName) throws IOException {
			//open training file
				File trainFile = new File(fileName);
				Scanner scan = new Scanner(trainFile);
				//first, read names of attributes, store in arraylist
				String name = scan.next();
				while (!name.equals("class")) {
					attributeNames.add(name);
					name = scan.next();
				}
				
				//DEBUGGING:
				System.out.println("The names are:");
				System.out.println(attributeNames);
				
				//Now we have the number and names of attributes stored properly.
				//Next we populate the data array.
				
				//arraylist to hold each line of data
				ArrayList<Boolean> temp = new ArrayList<Boolean>();
				//move to first line after attribute names
				scan.nextLine();
//				Scanner scanLine = new Scanner(scan.nextLine());
				String current = scan.nextLine();
				while (current.isEmpty()) {
					current = scan.nextLine();
				}
				char curr;
				//traverse through each line:
				while(scan.hasNextLine()) {
					temp = new ArrayList<Boolean>();
					//traverse through each 0/1 on the line:
					
					for (int k=0;k<attributeNames.size()+1;k++) {
//						curr = scanLine.next();
						curr = current.charAt(2*k);
						if(curr == '1')
							temp.add(true);
						else if(curr == '0')
							temp.add(false);
					}
					trainData.add(temp); //add the line of data to 2-dimensional array
					//skip over blank lines:
					
					do {
						current = scan.nextLine();
					} while (current.isEmpty() && scan.hasNextLine());
					
				}
				
				if(!current.isEmpty()){
					//Add in very last line/instance:
					temp = new ArrayList<Boolean>();
					//traverse through each 0/1 on the line:
					for (int k=0;k<attributeNames.size()+1;k++) {
						curr = current.charAt(2*k);
						if(curr == '1')
							temp.add(true);
						else if(curr == '0')
							temp.add(false);
					}	
				
					trainData.add(temp); //add the line of data to 2-dimensional array
				}
				scan.close();
				//Now we have successfully created a two-dimensional boolean array with all of the data!!
				
		}
		
		public static void parseTestData(String fileName) throws FileNotFoundException {
			//open test file
			File testFile = new File(fileName);
			Scanner scan = new Scanner(testFile);
			
			//arraylist to hold each line of data
			ArrayList<Boolean> temp = new ArrayList<Boolean>();
			scan.nextLine(); //move to first line after attribute names
			Scanner scanLine = new Scanner(scan.nextLine());
			String curr;
			//traverse through each line:
			while(scan.hasNextLine()) {
				temp = new ArrayList<Boolean>();
				//traverse through each 0/1 on the line:
				for (int k=0;k<attributeNames.size()+1;k++) {
					curr = scanLine.next();
					if(curr.equals("1"))
						temp.add(true);
					else if(curr.equals("0"))
						temp.add(false);
				}
				testData.add(temp); //add the line of data to 2-dimensional array
				//numInstances++;
				scanLine = new Scanner(scan.nextLine());
			}
			
			//Add in very last line/instance:
			//scanLine = new Scanner(scan.nextLine());
			temp = new ArrayList<Boolean>();
			//traverse through each 0/1 on the line:
			for (int k=0;k<attributeNames.size()+1;k++) {
				curr = scanLine.next();
				if(curr.equals("1"))
					temp.add(true);
				else if(curr.equals("0"))
					temp.add(false);
			}
			testData.add(temp); //add the line of data to 2-dimensional array
			
			scanLine.close();
			scan.close();
			//Now we have successfully created a two-dimensional boolean array with all of the data!!
		}
}
