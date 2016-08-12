/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blue_spp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.swing.JOptionPane;


public class btSppSock {
	public final UUID uuid = new UUID(                              //the uid of the service, it has to be unique,
			"27012f0c68af4fbf8dbe6bbaf7aa432a", false); //it can be generated randomly
    public final String name = "Echo Server";                       //the name of the service
    public final String url  =  "btspp://localhost:" + uuid         //the service url
                                + ";name=" + name 
                                + ";authenticate=false;encrypt=false;";
    LocalDevice local = null;
    StreamConnectionNotifier server = null;
    StreamConnection conn = null;
    
    int connected = 0;
    boolean thread_srunnung = false;
    private static String read_in = "";
    OutputStream outStream;
    PrintWriter pWriter;
    
    Thread btRxpool;
    RxWork rxThead;
	public String get_recieve_string(){
    	String out = read_in;
    	read_in = "";
    	return out;
    }
    
    
    public void clear_recieve_string(){
    	 read_in = "";
    }
    
    public int get_connect_status(){
    	return connected;
    }
    
    public btSppSock() {
        try {
            System.out.println("Setting device to be discoverable...");
            local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);
            System.out.println("Start advertising service...");
            server = (StreamConnectionNotifier)Connector.open(url);
            System.out.println("xxWaiting for incoming connection...");
            conn = server.acceptAndOpen();
            System.out.println("Client Connected...");
            InputStream inStream=conn.openInputStream();
            DataInputStream din   = new DataInputStream(inStream);
        
            connected = 1;
            
            rxThead = new RxWork();
            rxThead.set_input_stream(inStream);
            
            btRxpool = new Thread(rxThead, "buletooth_spp_rx_thread");
            btRxpool.start();
            
            thread_srunnung = true;
            
            /* for send string to clinet */
            outStream = conn.openOutputStream();
            pWriter = new PrintWriter(new OutputStreamWriter(outStream));
            
        } catch (Exception  e) {
        	System.out.println("Exception Occured: " + e.toString());
        	 JOptionPane.showMessageDialog(null, e.toString(), "Error",
                     JOptionPane.ERROR_MESSAGE);	
        }
    }    
    
    
    public void blueSppStop(){
    	
    	if(btRxpool != null){
	    	try {
	    		btRxpool.interrupt();
				btRxpool.join();
				btRxpool = null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	System.out.println("Blue tooth socket alive put it into stop !!");
    	}
    	else{
    		try{
                btRxpool = new Thread(rxThead, "buletooth_spp_rx_thread");
                btRxpool.start();
                
                thread_srunnung = true;
    		}
    		 catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
    		System.out.println("Blue tooth socket die call it start !!");
    	}
    	
    
    }
    
    public void blueSppStart(){
    	btRxpool.start();
    }
    
    public void send_string(String s){
        try{
            pWriter.write(s+"\r\n");
            pWriter.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }
		
		
		
    }
    class RxWork implements Runnable { 
    	
    	private  InputStream inStream;
    	
    	public void set_input_stream(InputStream in){
    		inStream = in;
    	}
    	
    	public void run() {
    		
            while(thread_srunnung == true){
            	try{
	                BufferedReader bReader=new BufferedReader(new InputStreamReader(inStream));
	                String lineRead=bReader.readLine();
	                
	                if( lineRead != null){
	                	System.out.println(lineRead);
	                	read_in += lineRead;
	                }
            	}
            	catch (Exception  e) {
            		System.out.println("Exception Occured: " + e.toString());
            		thread_srunnung = false;
            	}
            	
            	try {
					Thread.sleep(1 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
            }
    	}
    }
    
    
}
