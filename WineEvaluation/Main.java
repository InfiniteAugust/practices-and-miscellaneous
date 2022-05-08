import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.Arrays;

class Main{
	public static void main(String[] args) {
		try{
			//Task 1
			DataSet train = new DataSet("wine.txt");

			//Task 2
			String taskOutputString = "Mean of class 1: " + Arrays.toString(train.getMean(1)) + "\n" + "Std of class 1: " + Arrays.toString(train.getStd(1)) + "\n" + "Mean of class 2: " + Arrays.toString(train.getMean(2)) + "\n" + "Std of class 2: " + Arrays.toString(train.getStd(2)) + "\n" + "Mean of class 3: " + Arrays.toString(train.getMean(3)) + "\n" + "Std of class 3: " + Arrays.toString(train.getStd(3)) + "\n";
			
			PrintWriter tsk2op = new PrintWriter("task2Result.txt");
			tsk2op.print(taskOutputString);
			
			//Task 3
			DataClassification newtest = new DataClassification("wine.txt", "testwine.txt");

			//Task 4
			int sampleIndex = 0;

			PrintWriter tsk4op = new PrintWriter("task4Result.txt");
			int sampleSize = newtest.getDataSetSize("testwine.txt");

			while(sampleIndex < sampleSize) {

				int testSampleLabel = newtest.nnClassification(newtest.getDataSet()[sampleIndex]);
				taskOutputString = "The " + Integer.toString(sampleIndex+1) + "-th new sample belongs to class " + Integer.toString(testSampleLabel) + "\n";
				tsk4op.print(taskOutputString);
				sampleIndex++;				
			}
			
			tsk2op.close();
			tsk4op.close();		
		} catch (Exception e) {
			System.out.println("An error has occurred: in Main function: " + e);
		}	
	}
}