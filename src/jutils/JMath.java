package jutils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
* A collection of useful methods for doing unique math operations
*/

public class JMath {
	
	/**
	* Counts the number of digits in an integer value
	*/
	
	public static int intDigits(int number) {
		return (number + "").length();
	}
	
	/**
	* Counts the number of digits in a double value
	*/
	
	public static int doubleDigits(double number) {
		return (number + "").length();
	}
	
	/**
	* Counts the number of digits in a long value
	*/
	
	public static int longDigits(long number) {
		return (number + "").length();
	}
	
	/**
	* Accepts a double and converts it into n number of significant digits
	*/
	
	public static String sigFigs(double value, int significantDigits) {
		if (significantDigits == -1) {
			return value + "";
		} else if (significantDigits < -1) {
			throw new IllegalArgumentException();
		}

	    BigDecimal bd = new BigDecimal(value, MathContext.DECIMAL64);
	    bd = bd.round(new MathContext(significantDigits, RoundingMode.HALF_UP));
	    final int precision = bd.precision();
	    
	    if (precision < significantDigits) {
	    	bd = bd.setScale(bd.scale() + (significantDigits - precision));
	    }
	    
	    return bd.toPlainString();
	}
	
	/**
	* Accepts the start and end time in nanoseconds and returns a string of the appropriate conversion in n significant digits
	* Set significantDigits to -1 to return as is
	*/
	
	public static String getTimeDifference(long start, long end, int significantDigits) {
		long time = end - start;
		
		if (JMath.longDigits(time) >= 4) {
			if (JMath.longDigits(time) >= 7) {	
				if (JMath.longDigits(time) >= 10) {
					return sigFigs(time / 1000000000.0, significantDigits) + " s";
				}
				
				return sigFigs(time / 1000000.0, significantDigits) + " ms";
			}
			
			return sigFigs(time / 1000.0, significantDigits) + " \u00B5s";
		}
		
		return sigFigs(time, significantDigits) + " ns";
	}
}