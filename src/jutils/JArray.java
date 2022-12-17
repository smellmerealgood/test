package jutils;

/**
* A collection of useful methods for dealing with arrays
*/

public class JArray {
	
	/**
	* Finds and replaces said numbers in an integer array
	*/
	
	public static int [] replaceInt(int [] numbers, int find, int replace) {
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] == find) {
				numbers[i] = replace;
			}
		}
		
		return numbers;
	}
	
	/**
	* Finds and replaces said numbers in an double array
	*/
	
	public static Double [] replaceDouble(Double [] numbers, Double find, Double replace) {
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] == find) {
				numbers[i] = replace;
			}
		}
		
		return numbers;
	}
	
	/**
	* Finds and replaces said string in a string array
	*/
	
	public static String [] replaceString(String [] strings, String find, String replace) {
		for (int i = 0; i < strings.length; i++) {
			if (strings[i] == find) {
				strings[i] = replace;
			}
		}
		
		return strings;
	}
	
	/**
	* Shifts the values of an int array right one space
	*/
	
	public static int [] singleShiftRight(int [] numbers) {
		int last = numbers[numbers.length - 1];

        for (int i = numbers.length - 2; i >= 0; i-- ) {
        	numbers[i + 1] = numbers[i];
        }
        
        numbers[0] = last;
		
		return numbers;
	}
	
	/**
	* Shifts the values of an int array left one space
	*/
	
	public static int [] singleShiftLeft(int [] numbers) {
		int first = numbers[0];
		
		for (int i = 1; i < numbers.length; i++) {
			numbers[i - 1] = numbers[i];
		}
		
		numbers[numbers.length - 1] = first;
		
		return numbers;
	}
	
	/**
	* Shifts the values of an int array right n spaces
	*/
	
	public static int [] shiftRight(int [] numbers, int spaces) {
		for (int i = 1; i < spaces; i++) {
			singleShiftRight(numbers);
		}
		
		return numbers;
	}
	
	/**
	* Shifts the values of an int array left n spaces
	*/
	
	public static int [] shiftLeft(int [] numbers, int spaces) {
		for (int i = 1; i < spaces; i++) {
			singleShiftLeft(numbers);
		}
		
		return numbers;
	}
	
	/**
	* Shifts the values of a double array right one space
	*/
	
	public static double [] singleShiftRight(double [] numbers) {
		double last = numbers[numbers.length - 1];

        for (int i = numbers.length - 2; i >= 0; i-- ) {
        	numbers[i + 1] = numbers[i];
        }
        
        numbers[0] = last;
		
		return numbers;
	}
	
	/**
	* Shifts the values of a double array left one space
	*/
	
	public static double [] singleShiftLeft(double [] numbers) {
		double first = numbers[0];
		
		for (int i = 1; i < numbers.length; i++) {
			numbers[i - 1] = numbers[i];
		}
		
		numbers[numbers.length - 1] = first;
		
		return numbers;
	}
	
	/**
	* Shifts the values of a double array right n spaces
	*/
	
	public static double [] shiftRight(double [] numbers, int spaces) {
		for (int i = 1; i < spaces; i++) {
			singleShiftRight(numbers);
		}
		
		return numbers;
	}
	
	/**
	* Shifts the values of a double array left n spaces
	*/
	
	public static double [] shiftLeft(double [] numbers, int spaces) {
		for (int i = 1; i < spaces; i++) {
			singleShiftLeft(numbers);
		}
		
		return numbers;
	}
	
	/**
	* Shifts the values of a string array right one space
	*/
	
	public static String [] singleShiftRight(String [] strings) {
		String last = strings[strings.length - 1];

        for (int i = strings.length - 2; i >= 0; i-- ) {
        	strings[i + 1] = strings[i];
        }
        
        strings[0] = last;
		
		return strings;
	}
	
	/**
	* Shifts the values of a string array left one space
	*/
	
	public static String [] singleShiftLeft(String [] strings) {
		String first = strings[0];
		
		for (int i = 1; i < strings.length; i++) {
			strings[i - 1] = strings[i];
		}
		
		strings[strings.length - 1] = first;
		
		return strings;
	}
	
	/**
	* Shifts the values of a string array right n spaces
	*/
	
	public static String [] shiftRight(String [] strings, int spaces) {
		for (int i = 1; i < spaces; i++) {
			singleShiftRight(strings);
		}
		
		return strings;
	}
	
	/**
	* Shifts the values of a string array left n spaces
	*/
	
	public static String [] shiftLeft(String [] strings, int spaces) {
		for (int i = 1; i < spaces; i++) {
			singleShiftLeft(strings);
		}
		
		return strings;
	}
}