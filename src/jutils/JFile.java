package jutils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * A collection of useful methods for dealing with files
 */

public class JFile {

	/**
	 * Converts bytes from a long into a more appropriate and readable string format
	 */

	public static String convertFileSize(long bytes) {
		long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);

		if (absB < 1024) {
			return bytes + " B";
		}

		long value = absB;
		CharacterIterator ci = new StringCharacterIterator("KMGTPE");

		for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
			value >>= 10;
			ci.next();
		}

		value *= Long.signum(bytes);
		return String.format("%.1f %cB", value / 1024.0, ci.current());
	}

	/**
	 * Accepts a file path string and returns the file name including the extension
	 */

	public static String getFile(String filePath) {
		return new StringBuilder(filePath).delete(0, filePath.lastIndexOf("\\") + 1).toString();
	}

	/**
	 * Accepts a file path string and returns just the file name
	 */

	public static String getFileName(String filePath) {
		return new StringBuilder(filePath).delete(filePath.lastIndexOf("."), filePath.length())
				.delete(0, filePath.lastIndexOf("\\") + 1).toString();
	}

	/**
	 * Accepts a file path string and returns just the file path excluding the file
	 * itself
	 */

	public static String getFilePath(String filePath) {
		return new StringBuilder(filePath).delete(filePath.lastIndexOf("\\") + 1, filePath.length()).toString();
	}

	/**
	 * Accepts a file path string and returns the file extension
	 */

	public static String getExtension(String filePath) {
		int lastPeriod = filePath.lastIndexOf(".");

		if (lastPeriod < filePath.lastIndexOf("\\") || lastPeriod < filePath.lastIndexOf("/")) {
			return "";
		}

		return new StringBuilder(filePath).delete(0, lastPeriod + 1).toString();
	}
}
