package com.print.preview;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;

import com.federalhq.common.connectionmanager.oracle.dbconn.Database;
import com.federalhq.common.connectionmanager.oracle.dbconn.DatabaseConfig;
import com.federalhq.common.connectionmanager.oracle.dbconn.DatabaseConfigManager;

public class CredentialsDAO {
	
	public void connectToDB() {

		try {
			DatabaseConfig dbConfig = new DatabaseConfig();
	        dbConfig.setXmlConfigFilename("C:\\batchfiles\\config\\timmy.xml");
//	        dbConfig.setXmlConfigFilename("C:\\batchfiles\\config\\dbconfig_hq.xml");
			dbConfig.loadConfiguration();
			DatabaseConfigManager dm = DatabaseConfigManager.getInstance();
			dm.add(dbConfig);
			System.out.println("Database configuration loaded successfully for Android.");
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public UserConfig getUserConfig(String userId) throws Exception {

	    String sql =
	        "SELECT USER_IP, USER_PORT FROM USER_IP WHERE WINDOWS_USER = ? and USER_IP = ?";

	    try (Connection conn = Database.getInstance().getConnection("HQ");
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, userId);
	        ps.setString(2, getLocalIp());
	        try (ResultSet rs = ps.executeQuery()) {

	            if (rs.next()) {

	                return new UserConfig(
	                    rs.getString("USER_IP"),
	                    rs.getInt("USER_PORT")
	                );
	            }
	        }
	    }

	    return null;
	}
	

	private static String getLocalIp() throws Exception {

	    Enumeration<NetworkInterface> interfaces =
	            NetworkInterface.getNetworkInterfaces();

	    while (interfaces.hasMoreElements()) {

	        NetworkInterface ni = interfaces.nextElement();

	        if (!ni.isUp() || ni.isLoopback())
	            continue;

	        Enumeration<InetAddress> addresses = ni.getInetAddresses();

	        while (addresses.hasMoreElements()) {

	            InetAddress addr = addresses.nextElement();

	            if (addr instanceof Inet4Address
	                    && !addr.isLoopbackAddress()) {

	                String ip = addr.getHostAddress();

	                // Skip VirtualBox
	                if (ni.getDisplayName().contains("VirtualBox"))
	                    continue;

	                // Skip WiFi Direct
	                if (ni.getDisplayName().contains("Wi-Fi Direct"))
	                    continue;

	                // Skip hotspot adapter
	                if (ip.startsWith("192.168.137."))
	                    continue;

	                System.out.println("Selected IP: " + ip);

	                return ip;
	            }
	        }
	    }

	    throw new RuntimeException("No valid IP found");
	}
	
	
	
}
