import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class DataSet{
	private DataSample[] dataArray; 

//constructor
	public DataSet(String fileName){
		try {
			int size = getDataSetSize(fileName);

			File file = new File(fileName);
			Scanner dss = new Scanner(file);
			//reopen file and scanner as in getDataSetSize they were closed

			this.dataArray = new DataSample[size];

			/*buffer1 is for storing lines read from the file, 
			**every element in the array is a single line in the file 
			/*buffer2 is for storing array of strings splited from single lines in buffer1, 
			each element in buffer2 is an attribute represented as a string 
			*/
			String[] buffer1 = new String[size];
			String[][] buffer2 = new String[size][14];

			int a = 0;
			//to loop through all lines in the file and split them, store them in buffer2, set a as a counter
			while(a < size){
				buffer1[a] = dss.nextLine();
				for (int b = 0; b < 14; b++) {
					buffer2[a][b] = buffer1[a].split(",")[b];
				}
				a++;
			}

			//convert all the strings in buffer2 into labels(int) and attributes(double array)
			//set labelArr and attributeArr to store the results 
			int[] labelArr = new int[size];
			double[][] attributeArr = new double[size][13];

			for (int c = 0; c < size; c++) {
				labelArr[c] = Integer.parseInt(buffer2[c][0]);
				for (int d = 0; d < 13; d++) {
					attributeArr[c][d] = Double.parseDouble(buffer2[c][d+1]);
				}
			}

			//loop through all elements in dataArray to store its label and attributes
			for (int e = 0; e < size; e++) {
				this.dataArray[e] = new DataSample(labelArr[e], attributeArr[e]);
			}
			dss.close();
		} catch (NumberFormatException m) {
			System.out.println("invalid format: Error1: "+m);
		} catch (FileNotFoundException f) {
			System.out.println("target file not found: Error2"+f);
		} catch (Exception s) {
			System.out.println("an error has occurred: Error3: "+s);
		}
	}  

//methods
	public int getDataSetSize(String fileName) {		
		int size = 0;
		try{	
			File file = new File(fileName);
			Scanner dss = new Scanner(file);
			while(dss.hasNextLine()){
				size++;
				dss.nextLine();
			}
			dss.close();
		} catch (FileNotFoundException n) {
			System.out.println("target file not found: Error4: "+n);
		}	
		return size;	
	}


	public double[] getMean(int label){
		double[] avg = new double[13];

		try{
			double[] sum = new double[13];
			int numOfSample = 0;  
		
			for (int a = 0; a < 13; a++) { 				
				int b = 0;

				while(b < this.getDataSetSize("wine.txt")){ 
					if (this.dataArray[b].getLabel() == label) {
						sum[a] += this.dataArray[b].getAttributes()[a];
						numOfSample++;
					}
					b++;
				} 	
			}
			numOfSample /= 13;  //for all the 13 attributes, numOfSample was counted once, so divide 13 to get the true sample number

			for (int a = 0; a < 13; a++) {
				avg[a] = sum[a]/numOfSample;
			}
		} catch (Exception a) {
			System.out.println("an error has occurred: Error5: "+a);
		}
		return avg;
	} 
	

	public double[] getStd(int label){

		double[] std = new double[13];

		try{
			double[] mean = getMean(label);
			double[] sum = new double[13];

			for (int a = 0; a < 13; a++) {
				int b = 0;
			
				while(b < this.getDataSetSize("wine.txt")){
					if (this.dataArray[b].getLabel() == label) {
						sum[a] += Math.pow((mean[a] - this.dataArray[b].getAttributes()[a]), 2);	
					}
					b++;
				}
			}
			for (int a = 0; a < 13; a++) {
				std[a] = Math.sqrt(sum[a]);
			}
		} catch (Exception b) {
			System.out.println("an error has occurred: Error6: "+b);
		}
		return std;
	}
	
	public DataSample[] getDataSet(){
		return dataArray;
	}
}


class DataClassification extends DataSet{
	private DataSet newData; 

	//constructor 
	public DataClassification(String trainFileName, String newDataFileName){
		super(trainFileName);
		newData = new DataSet(newDataFileName);
	}

	//methods
	public DataSet getNewData(){
		return newData;
	} 

	public int nnClassification(DataSample datasp){
		//initilize theLabel to 1, because when finding the minimal distance, min is initialized to the first sample 
		//if datasp is closest to the first sample, directly its label will be 1
		//because in the if statement no other distance will be smaller than that
		int theLabel = 1;
		try{
			int b = 1; 
			double min = datasp.distance(this.getDataSet()[0]);	

			while(b < this.getDataSetSize("wine.txt")){

				if (datasp.distance(this.getDataSet()[b]) < min) {
					min = datasp.distance(this.getDataSet()[b]);
					theLabel = this.getDataSet()[b].getLabel();
				}
				b++;
			}
		} catch (Exception e) {
			System.out.println("An error has occurred: Error7: "+e);
		}
		return theLabel;		
	}
}