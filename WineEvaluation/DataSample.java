import java.io.File;
import java.util.Scanner;

//represents each data sample
public class DataSample{
	private int label;
	private int numOfAttributes;
	private double[] attributes;

	public DataSample(int lb, double[] atr){
		setLabel(lb);
		this.attributes = atr;
		this.numOfAttributes = attributes.length;
	}
	
	public void setLabel(int lb){
		this.label = lb;
	}

	public int getLabel(){
		return this.label;
	}

	public int getnumOfAttributes(){
		return numOfAttributes;
	}

	public double[] getAttributes(){
		return attributes;
	}

	public double distance(DataSample dat){
		double dist2 = 0.0;
		try{
			for (int c = 0; c < numOfAttributes; c++) {
				dist2 += Math.pow((this.attributes[c] - dat.attributes[c]), 2);
			}
		} catch (Exception e) {
			System.out.println("an error has occurred: Error8: "+e);
		}
		return Math.sqrt(dist2);
	}
}