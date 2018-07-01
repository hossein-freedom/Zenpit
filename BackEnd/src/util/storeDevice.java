package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.simple.JSONObject;

public class storeDevice extends HttpServlet{
    /**
     * This class is responsible to handle requests to store data for the given device.
     * If the device with the given DeviceID already exists in database, the values will 
     * get updated. 
     */
	private static final long serialVersionUID = 1L;
    
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			Connection con = this.getConnectin("jdbc/device_db");
			String deviceID = req.getParameter("DeviceID");
			String deviceName = req.getParameter("DeviceName");
			String batteryStatus= req.getParameter("BatteryStatus");
			Double lon = Double.parseDouble(req.getParameter("Longitude"));
			Double lat = Double.parseDouble(req.getParameter("Latitude"));
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("DeviceID",deviceID);
			jsonObj.put("DeviceName",deviceName);
			jsonObj.put("BatteryStatus",batteryStatus);
			jsonObj.put("Longitude",lon);
			jsonObj.put("Latitude",lat);
			this.handleStoringData(jsonObj, con);
			resp.setStatus(HttpServletResponse.SC_OK); //the operation was successful
		} catch (Exception e) {
			//e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); //the operation failed
		}
	}
	
	/**
	 * This method gets connection to the database with information
	 * specified in db_var_name variable in context.xml file. 
	 * @param db_var_name, name use to fetch data for database connection from context.xml
	 * @return, Connection object
	 * @throws Exception, if connection was not successful.
	 */
	private Connection getConnectin(String db_var_name) throws Exception{
		 Connection con;
		 Context ctx = new InitialContext();
		 Context envContext = (Context) ctx.lookup("java:comp/env");
		 DataSource ds = (DataSource) envContext.lookup(db_var_name);
		 con = ds.getConnection();
		 return con;
	}
	
	/**
	 * This method first will try to update the values in database. If it fails
	 * it means that this record did not exist in database. Then the values will
	 * inserted into database.
	 * @param jsonObj, JSON Object that contains data to be entered into database.
	 * @param con, Connection object
	 * @throws Exception
	 */
	private void handleStoringData(JSONObject jsonObj,Connection con) throws Exception{
		String insert_data = "INSERT INTO device_info(DeviceID,DeviceName,BatteryStatus,Longitude,Latitude) "
				       + " VALUES(?,?,?,?,?);";
		String update_data = "Update device_info Set DeviceName=?,BatteryStatus=?,Longitude=?,Latitude=?,TimeAdded=now() WHERE DeviceID = ?;";
	
		PreparedStatement ps = con.prepareStatement(update_data);
		ps.setString(5,(String) jsonObj.get("DeviceID"));
		ps.setString(1,(String) jsonObj.get("DeviceName"));
		ps.setString(2,(String) jsonObj.get("BatteryStatus"));
		ps.setDouble(3,(Double) jsonObj.get("Longitude"));
		ps.setDouble(4,(Double) jsonObj.get("Latitude"));
		int rows = ps.executeUpdate();
		if(rows <= 0){ // if there is no record with the given DeviceID.
			ps = con.prepareStatement(insert_data);
			ps.setString(1,(String) jsonObj.get("DeviceID"));
			ps.setString(2,(String) jsonObj.get("DeviceName"));
			ps.setString(3,(String) jsonObj.get("BatteryStatus"));
			ps.setDouble(4,(Double) jsonObj.get("Longitude"));
			ps.setDouble(5,(Double) jsonObj.get("Latitude"));
			rows = ps.executeUpdate();
	        if(rows <= 0){
	        	throw new Exception("The insert action was not successful.");
	        }
		}    
		con.close();
	}
}
