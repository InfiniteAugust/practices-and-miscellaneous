//Jingyu LUO (6521724)
 
public class InsertionSort {
 
	public static void main(String[] args) {

		InsertionSort sorter = new InsertionSort();

		/** generate 18 arrays from the smallest size to the largest
		 *  to reduce the error caused by process, it'd be better not to run the 2 for loops at one time
		 *  so comment one when running the other one
		 */ 

		for (int i = 1; i <= 18; i++) {
			sorter.sort(generateArrayRvs(i));
		} 

		/**
		for (int j = 1; j <= 18; j++) {
			sorter.sort(generateArrayRdm(j));
		}*/
	
	}

	public static void sort(int[] arr) {
		int temp, j;
		int i = 1;

		long startTime = System.nanoTime(); //record the starting time
		if (arr == null || arr.length < 2) {
			return;
		}	
		
		//for the ith element in the array(zero-based), compare it with the all elements on its left start from the nearest one 
		while(i < arr.length){
			j = i;
			/** if the comparing element a.k.a. the jth element is smaller then 
			 *  the current compared element a.k.a the (j-1)th element in the ith loop
			 */
			while(j > 0 && arr[j-1] > arr[j]){

				//swap the position of the two since the element on the left is should be smaller than the element on the right
				temp = arr[j];
				arr[j] = arr[j-1];
				arr[j-1] = temp;
				//re-index the comparing element as it makes one move to the left
				j--;
			}
			//go to the next comparing element
			i++;
		}
		long endTime = System.nanoTime(); //record the ending time 

		long elapsed = endTime - startTime; 

		//display(arr);
		System.out.println(elapsed);
	}


	public static int generateSize(int index){
		return (int)Math.pow(2, index);
	}

	//this method generates a reversely-sorted array.
	public static int[] generateArrayRvs(int index){
		int size = generateSize(index);
		int[] sample = new int[size];
		for (int i = size; i > 0; i--) {
			sample[size - i] = i;
		}
		return sample;
	}

	//generate a random number array, the range of random number is the size of the array
	//this guarantees the degree of the disorder of the arrays to be the same
	public static int[] generateArrayRdm(int index){
		int size = generateSize(index);
		int[] sample = new int[size];
		for (int i = 0; i < sample.length; i++) {
			sample[i] = (int)(Math.random()*size);
		}
		return sample;
	}
	
	//print the array
	public static void display(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println("");
	}
}