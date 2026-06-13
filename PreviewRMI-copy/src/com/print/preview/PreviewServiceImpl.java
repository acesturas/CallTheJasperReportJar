package com.print.preview;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import rmicommons.rmi.commons.PreviewService;

public class PreviewServiceImpl extends UnicastRemoteObject implements PreviewService {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected PreviewServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void preview(String fromDate,
                        String toDate,
                        String custCd,
                        String custBr) throws RemoteException {

        System.out.println("RMI request received:");
        System.out.println(fromDate + " " + toDate + " " + custCd + " " + custCd);

        try {
        	new ProcessBuilder(
        		    "cmd",
        		    "/c",
        		    "start",
        		    "",
        		    "javaw",
        		    "-jar",
        		    "C:\\batchfiles\\CancelledOrdersSummaryJasperReportPreviewer.jar",
        		    fromDate,
        		    toDate,
        		    custCd,
        		    custBr
        		).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}