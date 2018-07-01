package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.simple.JSONObject;

public class retrievDevice extends HttpServlet {

	/**
	 * This class is responsible to handle requests to retrieve data for
	 * a given DeviceID. This class uses context parameter "jdbc/device_db" 
	 * (which is set in context.xml file for tomcat) 
	 * to look up required information for database connection.
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String deviceID = req.getParameter("DeviceID");
		try {
			resp.setContentType("application/json");
			Connection con = this.getConnectin("jdbc/device_db");
			JSONObject tmp_res = this.handleRetrievData(deviceID, con);
			JSONObject final_res = new JSONObject();
			// if the result was found for the given DeviceID
			if(tmp_res != null){ 
				final_res.put("status","OK");
				final_res.put("data",tmp_res);
				resp.getWriter().write(final_res.toString());
			// if the result was noy found for the given DeviceID
			}else{
				final_res.put("status","error");
				final_res.put("data",null);
				resp.getWriter().write(final_res.toString());
			}
		} catch (Exception e) {
			//if something went wrong send server error code.
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
	 * This method retrieves data for the device with the given DeviceId from database.
	 * If the device info exist, the status value returned object will be set to 'OK'. 
	 * @param deviceID, the id for the device whose info is requested.
	 * @param con, Connection object
	 * @return, JSONObject, contains {data:{},status:,timeStamp:}
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private  JSONObject handleRetrievData(String deviceID,Connection con) throws Exception{
		String get_data = "SELECT DeviceID,DeviceName,BatteryStatus,Longitude,Latitude,TimeAdded FROM  device_info WHERE DeviceID = ?;";
		PreparedStatement ps = con.prepareStatement(get_data);
		ps.setString(1,deviceID);
		ResultSet rs  = ps.executeQuery();
		JSONObject result = new JSONObject();
		if(rs.next()){
			String deviceId = rs.getString(1);
			String deviceName = rs.getString(2);
			String batteryStatus = rs.getString(3);
			Double lon = rs.getDouble(4);
			Double lat = rs.getDouble(5);
			String time = rs.getTimestamp(6).toString();
			result.put("DeviceID",deviceId);
			result.put("DeviceName",deviceName);
			result.put("BatteryStatus",batteryStatus);
			result.put("Longitude",lon);
			result.put("Latitude",lat);
			result.put("TimeAdded",time);
			
			return result;
		}
	    con.close();
		return null;
	}
}
