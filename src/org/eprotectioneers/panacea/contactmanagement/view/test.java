package org.eprotectioneers.panacea.contactmanagement.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.eprotectioneers.panacea.contactmanagement.components.RoundTextField;
import org.eprotectioneers.panacea.contactmanagement.design.DesignDatabase;
import org.eprotectioneers.panacea.contactmanagement.design.ProjectPanaceaTemplates;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JToolTip;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.JMenuBar;
import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class test extends JFrame {

	//DevelopmentLifeFeed Simsen3142
	
	private JPanel contentPane;
	static test frame;
	private RoundTextField rndtxtfldTest;
	private Contactbar contactbar;
	private Component item_group;
	private Contactbar contactbar_1;
	private static JPanel panel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {		
				try {
				new Thread(new Runnable(){
						public void run() {	
							DesignDatabase.loadDesign();
							System.out.println(ProjectPanaceaTemplates.getCbg_contacts());
							/*for(int i=1;i<658;i++){
								DatabaseC.addContact(new Contact(i, "Contact "+i, "", "", "Email.c"+i+"@Eprotection.com", i+""+i+""+i+""+i, i+" Street", "", false));
								DatabaseG.addGroup(new Group(i, "Group "+i, "The "+i+" Group", ""));
								System.out.println(i);
							}*/
						}
					}).start();
					frame=new test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		
	}

	public static void setPanel(JPanel pnl){
		frame.remove(panel);
		panel=pnl;
		frame.add(panel,BorderLayout.CENTER);
		frame.setVisible(true);
	}
	/**
	 * Create the frame.
	 */
	public test() {
		initialize();
	}
	private void initialize(){
	    try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 450);
		UIManager.put("ToolTip.background",new Color(10,10,10));
		UIManager.put("ToolTip.foreground",Color.WHITE);
		Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
		UIManager.put("ToolTip.border", border);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		

		//Contact c=new Contact(2, "", "Simon", "Simon", "Simon", "Simon", "Simon", "s", false);
		//Group g=new Group(1, "GROUP1", "MY FIRST CAPS GROUP", "");
		//panel = new Page_Group(DatabaseG.getGroups().get(0)); 
		//panel =new Page_AddContact();
		//panel = new Page_Contact(DatabaseC.getContacts().get(4)); 
				
		//item_group= new Item_contact(DatabaseC.getContacts().get(2));
		//contentPane.add(item_group, BorderLayout.NORTH);
		contactbar_1 = new Contactbar();
		contactbar_1.setPreferredSize(new Dimension(300, 84));
		contactbar_1.setMinimumSize(new Dimension(300, 70));
		contentPane.add(contactbar_1, BorderLayout.EAST);	
		
		panel = new JPanel();
		panel.setBackground(ProjectPanaceaTemplates.getCbg_contacts());
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		contentPane.add(panel, BorderLayout.CENTER);
		try {
		TwitterPanel twitterPanel1 = new TwitterPanel(new URL("https://twitter.com"),
				"C:\\Users\\Katschtaler\\Desktop\\PPNCA_Images\\Twitter-icon.png", "Twitter", "@Twitter", 
				"Hello, my name is Twitter,\nPossibly, you ask yourself, why the person who wrote this wrote this... UND WIESO DER TYP DES NIT IN SEINER MUTTERSPRACHE GSCHRIEBN HAT, but there is no reason why...\nHe just wanted to write random Bullshit.\nSome of you will think, that this guy isn't normal into his brain. These people are possibly right.\nBut the writer of this tricked you. He told you, that there's no reason for writing this, but there is.\nThe real reason for writing this is, that the writer wanted to test, if you can read this anymore, or if there are already the 3 dots, the writer thinks, that there are already, because he is a great programmer.",
				"C:\\Users\\Katschtaler\\Desktop\\PPNCA_Images\\typo-twitter-background.jpg");
		panel.add(twitterPanel1);
		
		
		TwitterPanel twitterPanel2 = new TwitterPanel(null,
				"C:\\Users\\Katschtaler\\Desktop\\PPNCA_Images\\ep.jpg", "eProtectioneers", "@eProtectioneers", 
				"We protect your electronic communication",
				new Color(234,101,65));
		panel.add(twitterPanel2);
		
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
