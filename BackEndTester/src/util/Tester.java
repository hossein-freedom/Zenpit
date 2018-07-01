package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Tester {

	public static void main(String[] args) {
		try {
			Tester.testResponseCode();
			Tester.testStoreRetrievProcess();
			Tester.testUpdateProcess();
			Tester.testExistanceProcess();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method tests if server is returning correct server status code.
	 * @throws Exception
	 */
	public static void testResponseCode() throws Exception {
		URL url = new URL("http://ec2-52-21-174-224.compute-1.amazonaws.com/BackEnd/storeDeviceData?DeviceID=1234BB&DeviceName=Alcatel-G524&BatteryStatus=half-dead&Longitude=67.36565&Latitude=-112.32554");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		int responseCode = con.getResponseCode();
		if(HttpURLConnection.HTTP_OK == responseCode){
			System.out.println("First Test Passed.");
		}else{
			System.out.println("First Test Failed.");
		}
		
	}
	
	/**
	 * This method checks if data is stored correctly.
	 * @throws Exception
	 */
	public static void testStoreRetrievProcess() throws Exception {
		URL url = new URL("http://ec2-52-21-174-224.compute-1.amazonaws.com/BackEnd/storeDeviceData?"
				+ "DeviceID=1234AA&DeviceName=Samsung-S8&BatteryStatus=dead&Longitude=22.36565&Latitude=-88.322");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		int responseCode = con.getResponseCode();
		if(! (HttpURLConnection.HTTP_OK == responseCode) ){
			System.out.println("Second Test Failed.");
			return;
		}
		url = new URL("http://ec2-52-21-174-224.compute-1.amazonaws.com/BackEnd/retrievDeviceData?DeviceID=1234AA");
		con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		JSONParser parser = new JSONParser();
		JSONObject respObj = (JSONObject) parser.parse(response.toString());
		
		if( ! ((String) respObj.get("status") ).equals("OK") ) {
			System.out.println("Second Test Failed.");
			return;
		}
		boolean res = compareRetrive("1234AA","Samsung-S8","dead",22.36565,-88.322,(JSONObject)respObj.get("data"));
		if(res != true){
			System.out.println("Second Test Failed.");
			return;
		}
		System.out.println("Second Test Passed.");
	}
	
	/**
	 * This method just checks similarity of the given values with the 
	 * values stored in JSON object omparable.
	 * @param DeviceID
	 * @param DeviceName
	 * @param BatteryStatus
	 * @param Longitude
	 * @param Latitude
	 * @param comparable
	 * @return
	 */
	private static boolean compareRetrive(String DeviceID,String DeviceName,String BatteryStatus,Double Longitude,Double Latitude,JSONObject comparable){
		return ( DeviceID.equals((String) comparable.get("DeviceID")) &&
				DeviceName.equals((String) comparable.get("DeviceName"))&&
				BatteryStatus.equals( (String) comparable.get("BatteryStatus")) &&
				Longitude.equals((Double) comparable.get("Longitude") ) && 
				Latitude.equals((Double) comparable.get("Latitude") ) );
	}
	
	/**
	 * This method tests if the existing record of a device is correctly
	 * updated.
	 * @throws Exception
	 */
	public static void testUpdateProcess() throws Exception {
		URL url = new URL("http://ec2-52-21-174-224.compute-1.amazonaws.com/BackEnd/storeDeviceData?"
				+ "DeviceID=1234AA&DeviceName=Samsung-S99&BatteryStatus=almost-dead&Longitude=32.36565&Latitude=-123.322");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		int responseCode = con.getResponseCode();
		if(! (HttpURLConnection.HTTP_OK == responseCode) ){
			System.out.println("Third Test Failed.");
			return;
		}
		url = new URL("http://ec2-52-21-174-224.compute-1.amazonaws.com/BackEnd/retrievDeviceData?DeviceID=1234AA");
		con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		JSONParser parser = new JSONParser();
		JSONObject respObj = (JSONObject) parser.parse(response.toString());
		if( ! ((String) respObj.get("status") ).equals("OK") ) {
			System.out.println("Third Test Failed.");
			return;
		}
		boolean res = compareRetrive("1234AA","Samsung-S99","almost-dead",32.36565,-123.322,(JSONObject)respObj.get("data"));
		if(res != true){
			System.out.println("Third Test Failed.");
			return;
		}
		System.out.println("Third Test Passed.");
	}
	
	/**
	 * This method tests whether server returns appropriate
	 * action(returns error status) when the requested device
	 * does not exist in database.
	 * @throws Exception
	 */
    public static void testExistanceProcess() throws Exception {
		
		URL url = new URL("http://ec2-52-21-174-224.compute-1.amazonaws.com/BackEnd/retrievDeviceData?DeviceID=1234CC");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		JSONParser parser = new JSONParser();
		JSONObject respObj = (JSONObject) parser.parse(response.toString());
		if( ! ((String) respObj.get("status") ).equals("error") ) {
			System.out.println("Forth Test Failed.");
			return;
		}
		System.out.println("Forth Test Passed.");
		
	}
	
	
}
