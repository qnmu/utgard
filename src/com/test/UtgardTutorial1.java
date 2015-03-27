package com.test;

import java.util.concurrent.Executors;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.AutoReconnectController;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.SyncAccess;
 
 public class UtgardTutorial1 {
 
    public static void main(String[] args) throws Exception {
        // create connection information
    	AutoReconnectController autos = null;
        final ConnectionInformation ci = new ConnectionInformation();
        /*
        ci.setHost("192.168.89.170");
        ci.setDomain("");
        ci.setUser("Administrator");
        ci.setPassword("supcondcs");
        ci.setProgId("ProcessIT.SimulationSvr");
        // ci.setClsid("680DFBF7-C92D-484D-84BE-06DC3DECCD68"); // if ProgId is not working, try it using the Clsid instead
        final String itemId = "23FC0020.OP";
        */

        ///*
        ci.setHost("10.10.11.11");
        ci.setDomain("");
        ci.setUser("Administrator");
        ci.setPassword("supcondcs");
        ci.setProgId("SUPCON.MISGateOPC.1");
        // ci.setClsid("680DFBF7-C92D-484D-84BE-06DC3DECCD68"); // if ProgId is not working, try it using the Clsid instead
        final String itemId = "S3.OPC01150\\SUPCON.SCRTCore.1.03011PT450101.PV";
        //*/
        
        // create a new server
        final Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        
        //autos = new AutoReconnectController(server);
        try {
            // connect to server
            server.connect();
            //autos.connect();
            Thread.sleep(100);
            // add sync access, poll every 500 ms
            final AccessBase access = new SyncAccess(server, 500);
            access.addItem(itemId, new DataCallback() {
            	
                @Override
                public void changed(Item item, ItemState state) {
                    //System.out.println("״̬:"+state);
    	        	try {
						//System.out.println(" ===============  " + item.read(true).getValue().getObjectAsUnsigned().getValue());
    	        		System.out.println("===============" + item.read(true).getValue());
					} catch (JIException e) {
						e.printStackTrace();
					}
                }
            });
            // start reading
            access.bind();
            // wait a little bit
            Thread.sleep(10 * 1000);
            // stop reading
            access.unbind();
        } catch (final JIException e) {
        	e.printStackTrace();
            System.out.println(String.format("%08X: %s", e.getErrorCode(), server.getErrorMessage(e.getErrorCode())));
        }
    }
}