package com.print.preview;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {

	public static void main(String[] args) {

	    try {

	        String userId = System.getProperty("user.name");

	        System.out.println("Windows User: " + userId);
	        
	        CredentialsDAO dao = new CredentialsDAO();
	        
	        dao.connectToDB();

	        UserConfig config = dao.getUserConfig(userId);

	        if (config == null) {
	            System.out.println("No configuration found for user: " + userId);
	            return;
	        }

	        String host = config.getIp();
	        int port = config.getPort();

	        System.out.println("IP: " + host);
	        System.out.println("PORT: " + port);

	        System.setProperty("java.rmi.server.hostname", host);

	        Registry registry = LocateRegistry.createRegistry(port);

	        registry.rebind("PreviewService", new PreviewServiceImpl());

	        System.out.println(
	            "RMI Server running on " + host + ":" + port
	        );

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
    
}