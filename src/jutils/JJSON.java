package jutils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Useful methods for dealing with JSON files
 */

public class JJSON {

	/**
	 * Attempts to retrieve and read a JSON file from the specified link
	 * The method will retry up to 5 times if it is unsuccessful
	 */

	public static String retrieveJSON(String link) throws IOException {
		for (int i = 1; i <= 5; i++) {
			System.out.println("\nRetrieving and reading... (" + (i) + ")");

			try {
				URL url = new URL(link);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				String responseCode = connection.getResponseCode() + "";

				if (responseCode.charAt(0) == 4) {
					return responseCode;
				} else if (responseCode.charAt(0) == 5) {
					return responseCode;
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line, JSON = "";

				while ((line = br.readLine()) != null) {
					JSON += line;
				}

				br.close();
				connection.disconnect();

				return JSON;
			} catch (IOException ex) {}
		}

		return "";
	}
}