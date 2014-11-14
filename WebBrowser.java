import java.util.*;
import java.util.List;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class WebBrowser extends JFrame 
{
	public static void main(String [] args)
	{
		JFrame browser = new WebBrowser();
		browser.show();
	}
	private JTextField addressBar;
	private JEditorPane display;
	private JButton back;
	private JButton getURL;
	private Stack history = new Stack();
	private Stack history2 = new Stack();
	private JEditorPane display2;
	private JTextField addressbar2;
	private JTextArea message;
	private JTextArea message2;
	
	
	public WebBrowser()
	{
		super("Chanakan Charoenvichanchai s5050259@kmitl.ac.th - Java Web Browser (Network Programming Class Assignment)");
		
		setSize(1200,700);
		
		//adderssBar
		addressBar = new JTextField(40);
		//addressBar
		addressbar2 = new JTextField(40);
		
		getURL = new JButton ("Find The URL");
		
		//display2
		display2 = new JEditorPane();
		display2.setEditable(false);
		display2.setSize(600, 680);
		
		//display
		display = new JEditorPane();
		display.setEditable(false);
		display.setSize(600, 680);
	
		// back Button
		back = new JButton("BACK");
		getURL = new JButton("Get");
		message = new JTextArea(8, 20);
		message2 = new JTextArea(8,100);
		
		
		Runner trd = new Runner(addressBar,display,back,history,getURL,message,"t1");
		Thread t1 = new Thread(trd);
		t1.start();
		
		Runner trd2 = new Runner(addressbar2,display2,back,history2,getURL,message2,"t2");
		Thread t2 = new Thread(trd2);
		t2.start();
	
		Container contentPane = getContentPane();
		contentPane.add(new JScrollPane(display), "West");
		contentPane.add(new JScrollPane(display2), "East");
		contentPane.add(new JScrollPane(message), "North");
		
		
		//contentPane2.add(new JScrollPane(message2), "North");
		
		JPanel panel = new JPanel();
		panel.add(new JLabel("URL"));
		panel.add(addressBar);
		panel.add(addressbar2);
		panel.add(getURL);
		panel.add(back);
		panel.add(new JScrollPane(message2));
		
		contentPane.add(panel,"South");
	}
	
	// load crap to display on the screen

}


	class  Runner implements Runnable
	{
		private JTextField addressBared;
		private JEditorPane displayed;
		private Stack historyed = new Stack();
		private JButton backed;
		private JButton get;
		private JTextArea m;
		private String name;
		
		public Runner(JTextField a,JEditorPane b,JButton c,Stack d,JButton e,JTextArea f,String n) {
			this.addressBared = a;
			this.displayed = b;
			this.backed = c;
			this.historyed = d;
			this.get = e;
			this.m = f;
			this.name = n;
		}
		public void run()
		{
			get.addActionListener(
					new ActionListener(){
						public void actionPerformed(ActionEvent event)
						{
							//loadCrap(event.getActionCommand());
							//historyed.push(addressBared.getText());
							try{
								displayed.setPage(addressBared.getText());
								addressBared.setText(addressBared.getText());
								historyed.push(addressBared.getText());
								
								try 
								{
									 
									URL obj = new URL(addressBared.getText());
									URLConnection conn = obj.openConnection();
									Map<String, List<String>> map = conn.getHeaderFields();
								 
									m.append("Thread "+name+"  Printing Response Header...\n");
								 
									for (Map.Entry<String, List<String>> entry : map.entrySet()) 
									{
										m.append("Key : " + entry.getKey() 
								                           + " ,Value : " + entry.getValue()+"\n");
									}
								 
									m.append("\nGet Response Header By Key ...\n");
									String server = conn.getHeaderField("Server");
								 
									if (server == null) 
									{
										m.append("Key 'Server' is not found!\n");
									} 
									else 
									{
										m.append("Server - " + server+"\n");
									}
								 
									m.append("\n Done");
								 
								    } 
								catch (Exception e) 
								{
									e.printStackTrace();
							    }								
									
							}catch(Exception e){
								System.out.println("crap!");
							}
							
							
							
							
						}
					}
				);
		
			
			displayed.addHyperlinkListener
			(new HyperlinkListener()
				{
					public void hyperlinkUpdate(HyperlinkEvent event)
					{
						if(event.getEventType()==HyperlinkEvent.EventType.ACTIVATED)
						{
							try
							{
								displayed.setPage(event.getURL().toString());
		
							}
							catch(Exception e)
							{
								System.out.println("crap!");
							}
						}
					}
				}
			);
				
			backed.addActionListener
			(new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						if(historyed.size()<=1) return;
						try
						{
							historyed.pop();
							String histo = (String)historyed.peek();
							addressBared.setText(histo);
							displayed.setPage(histo);
							
							try 
							{
								 
								URL obj = new URL(addressBared.getText());
								URLConnection conn = obj.openConnection();
								Map<String, List<String>> map = conn.getHeaderFields();
							 
								System.out.println("Printing Response Header...\n");
							 
								for (Map.Entry<String, List<String>> entry : map.entrySet()) 
								{
									System.out.println("Key : " + entry.getKey() 
							                           + " ,Value : " + entry.getValue());
								}
							 
								System.out.println("\nGet Response Header By Key ...\n");
								String server = conn.getHeaderField("Server");
							 
								if (server == null) 
								{
									System.out.println("Key 'Server' is not found!");
								} 
								else 
								{
									System.out.println("Server - " + server);
								}
							 
								System.out.println("\n Done");
							 
							    } 
							catch (Exception e) 
							{
								e.printStackTrace();
						    }
						}
						catch(IOException e)
						{
							displayed.setText("Error : " +e);
						}
					}
				}
			);
			
		}
	
	}