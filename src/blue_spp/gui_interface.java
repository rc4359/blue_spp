/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blue_spp;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;


public class gui_interface {
	private static JFrame gframe;
	
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    
    private static JTextArea ReceivedText, SendText;
    private static btSppSock control_btSock = null;
    
    private static JButton button_stop;
    
	public gui_interface(){
	
		gframe = new JFrame("Blutooth SPP");
	    gframe.setBounds(200,200,500,400);


	    ReceivedText = new JTextArea(20, 20);
	    
	    ReceivedText.setLineWrap(true);
	


	   ReceivedText.setEditable(false);
	   ReceivedText.setCaretPosition(ReceivedText.getDocument()
				.getLength());
	   
	   DefaultCaret caret = (DefaultCaret)ReceivedText.getCaret();
	   caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	   
	   JScrollPane scrollPane = new JScrollPane(ReceivedText,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	   

	   scrollPane.setMinimumSize(new Dimension(200, 200));

	   
	    
	    JPanel pane = new JPanel ();
	    pane.setLayout(new GridBagLayout());
	
	    GridBagConstraints c = new GridBagConstraints();
	
	    JPanel ButtonlistPane = new JPanel();
	    ButtonlistPane.setLayout(new BoxLayout(ButtonlistPane, BoxLayout.PAGE_AXIS));
	    

        
	    
	    JButton button_clear = new JButton("Clear"); 
           // button_clear.setPreferredSize(new Dimension(40, 80));
	    ButtonlistPane.add(button_clear);
	    
	    JButton button_exit = new JButton("Exit");
	    ButtonlistPane.add(button_exit);
	    
	    
	    button_stop = new JButton("Stop");
	    ButtonlistPane.add(button_stop);
	    
	    /* re */
	    JLabel label_rev = new JLabel("Received data");
            
            JLabel label_snd = new JLabel("Input message to ..");
            SendText = new JTextArea(1, 20);
            SendText.setLineWrap( true );
            
            JButton button_send = new JButton("Send");
	   // c.weightx = 0.5;
	   // c.weighty = 0;
	   // c.fill = GridBagConstraints.HORIZONTAL;
	  //  c.fill = GridBagConstraints.VERTICAL;
            c.gridx = 0;
	    c.gridy = 0;
            pane.add(label_snd, c);
            c.gridx = 0;
	    c.gridy = 1;
            pane.add(SendText, c);
            c.gridx = 1;
	    c.gridy = 1;
            pane.add(button_send, c);
            
            
             
	    c.gridx = 0;
	    c.gridy = 2;
	   
	    pane.add(label_rev, c);
	 
	    c.gridx = 0;
	    c.gridy = 3;
	    pane.add(scrollPane, c);
	    
	   
	    c.gridx = 2;
	    c.gridy = 3;
	    pane.add(ButtonlistPane, c);
	    
	    gframe.add(pane);
	    gframe.setVisible(true);
            
            gframe.addWindowListener(new WindowAdapter() 
            {
                public void windowClosing(WindowEvent event) {
                System.exit(0);
              }});
	    
	    clearListener clr = new clearListener();
	    button_clear.addActionListener(clr);
	    
	    
	    exitListener ext = new exitListener();
	    button_exit.addActionListener(ext);
	    
	    button_stop.addActionListener(new stopLisener());
            
            button_send.addActionListener(new sendListener());
	}
	


	public void setRxText(String s){
		ReceivedText.setText(s);
	}
	public String getRxText(){
		return ReceivedText.getText();
	}
	public void set_control_bt(btSppSock btsock){
		control_btSock = btsock;
	}
	
	class stopLisener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			try{
				control_btSock.blueSppStop();
			}
			catch (Exception  e) {System.out.println("Exception Occured: " + e.toString());}
			
			if(control_btSock.thread_srunnung == true)
				button_stop.setText("Stop");
			else
				button_stop.setText("Start");
				
		}
		
	}
	class clearListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			ReceivedText.setText("");
			System.out.println("BListener");
		}
		
	}
	

	class exitListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
		
			System.out.println("exitListener");
			if (JOptionPane.showConfirmDialog(null, "Are you sure about exit?", "Exit", 
				    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
				    == JOptionPane.YES_OPTION)
				{
				 //Do the request
					System.exit(0);
				}
		}
		
	}
	
        class  sendListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("sendListener");
                try{
                   control_btSock.send_string(SendText.getText().toString());
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                
                
             
                
            }

            
        }
	
	
}
