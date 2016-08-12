/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blue_spp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author richard
 */
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import javax.bluetooth.*;
import javax.microedition.io.*;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Blue_spp {
	
	private static btSppSock echoserver;
	private static gui_interface main_frame;
	
	 public static void xmain (String args[]){
		
		 int ij = 0;
		 main_frame = new gui_interface();
		 
		 while(true){
			 String text = "";
			 String Show_string = "";
			  text = Integer.toString(ij);
			  
			 
			  Show_string = main_frame.getRxText();
			  
			  main_frame.setRxText(Show_string + text);
			  ij++;
			  try {
					Thread.sleep(1 * 10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			  
		 }
	 }
	
    public static void main (String args[]){
        JFrame frame_wait_conn = new JFrame();
        frame_wait_conn.setBounds(50, 50, 400, 100);
        
        
        JLabel lable_wait_conn = new JLabel();
        lable_wait_conn.setText("Waiting bluetooth client connection...");
       
        
        frame_wait_conn.add(lable_wait_conn);
        
        frame_wait_conn.addWindowListener(new WindowAdapter() 
        {
                public void windowClosing(WindowEvent event) {
                System.exit(0);
         }});
        
        frame_wait_conn.setVisible(true);
    	echoserver = new btSppSock(); 
    	frame_wait_conn.setVisible(false);
    	
    	frame_wait_conn.dispose();
    	
    	if(echoserver.get_connect_status() == 0)
    		System.exit(0);
    	
    	main_frame = new gui_interface();
    	
    	main_frame.set_control_bt(echoserver);
    	main_frame.setRxText("Start");
    	
    	data_task rcv_tas = new data_task(echoserver, main_frame);
    	Thread guiupdate = new Thread(rcv_tas, "");
    	guiupdate.start();
    
    }

    
    
}
class data_task implements Runnable{
  	 private btSppSock btsock;
  	 private gui_interface frame;
  	 private static String Receive_log = "";
  	 
  	 public data_task(btSppSock s, gui_interface f){
  		btsock = s;
  		frame = f;
  	 }
		@Override
		public void run() {
			// TODO Auto-generated method stub
		
			
			while(true){
				try{
					String in = "";
					String tmp = "";
					in = btsock.get_recieve_string();
					
					//if(in.compareTo("") != 0){
						//System.out.println(" new message !!");
						//Receive_log += in;
						//frame.setRxText(Receive_log);
						//in = "";
						tmp = frame.getRxText();
						frame.setRxText(in + tmp);
					//}
	
				}
			   catch (Exception  e) {System.out.println("Exception Occured: " + e.toString());}
				try {
					Thread.sleep(1 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}
  	
  }