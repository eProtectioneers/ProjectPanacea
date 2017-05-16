package org.eprotectioneers.panacea.userinterface;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.eprotectioneers.panacea.contactmanagement.view.*;

/**
 * ProjectPanacea Mail Client
 * @author eProtectioneers
 */
public class PPCA_PanaceaWindow 
{
	/**
	 * Window title
	 */
	public final String TITLE = "Project Panacea - ALPHA - DEV/1.0";
	private int width;
	private int height;
	
	//Visual components
	private static JFrame frame;
	private static Container container;
	
	/**
	 * ToolbarPanel
	 */
	private PPCA_ToolbarPanel toolbarPanel;
	/**
	 * NavigationPanel
	 */
	private PPCA_NavigationPanel navigationPanel;
	
	private static JPanel centerPanel;
	private static PPCA_MainPanel mainPanel;
	private JPanel xpanel;
	
	/**
	 * Contactbar
	 */
	private Contactbar cbar;
	
	private static JPanel rightPanel;
	
	/**
	 * Default constructor. Create a 1200 x 1000 window
	 */
	public PPCA_PanaceaWindow ()
	{
		this (800, 450);
		
	}
	
	/**
	 * Constructor
	 * @param width the width of the window
	 * @param height the height of the window
	 */
	public PPCA_PanaceaWindow (int width, int height)
	{
		this.width = width;
		this.height = height;
		initializeWindow();
	}
	
	/**
	 * Initialize window
	 */
	private void initializeWindow()
	{
		Image image;
		
		
		//Set the properties
		frame = new JFrame(TITLE);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(width, height));
		frame.setPreferredSize(new Dimension(width,height));
		
		try {
		    File pathToFile = new File("images/eprotlogo_main.png");
		    image = ImageIO.read(pathToFile);
		    frame.setIconImage(image);
		} catch (IOException ex) {
		    ex.printStackTrace();
		}
		
		// Set layout
		container = frame.getContentPane();
		container.setLayout(new BorderLayout());
		
		// Add comps
		frame.setLocationRelativeTo(null);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
		
		navigationPanel = new PPCA_NavigationPanel(frame, this);
				
		//Initialising the main Panel
		mainPanel = new PPCA_MainPanel(frame, this);
		centerPanel=mainPanel;

		toolbarPanel = new PPCA_ToolbarPanel(frame, this);
		
		//sidePanel = new PPCA_SidePanelRight(this);
		cbar=new Contactbar();
		rightPanel=cbar;
		
		container.add(navigationPanel, BorderLayout.WEST);
		container.add(toolbarPanel, BorderLayout.NORTH);
		container.add(centerPanel, BorderLayout.CENTER);
		container.add(rightPanel, BorderLayout.EAST);
		
		//Show window
		
		frame.pack();
		frame.setVisible(true);
		
		this.setCenterPanel(this.getMainPanel());
		this.getMainPanel().resetWorkspace();
		
	}

	/**
	 * @return the Frame
	 */
	public static JFrame getFrame(){
		return frame;
	}
	/**
	 * Set the mainPanel of the ProjectPanacea window to MailDisplay
	 */
	public static void setCenterPanel(JPanel jpanel){
		container.remove(centerPanel);
		centerPanel=jpanel;
		container.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setVisible(false);
		centerPanel.setVisible(true);
	}
	
	public static void setRightPanel(JPanel jpanel){
		container.remove(rightPanel);
		rightPanel=jpanel;
		container.add(rightPanel, BorderLayout.EAST);
		rightPanel.setVisible(false);
		rightPanel.setVisible(true);
	}
	
	/**
	 * Set the mainPanel with another panel
	 */
	public void setMailPanel(PPCA_MainPanel xmain){
		mainPanel = xmain;
	}
	
	/**
	 * @return the toolbarPanel
	 */
	public PPCA_ToolbarPanel getToolbarPanel() 
	{
		return toolbarPanel;
	}

	/**
	 * @return the navigationPanel
	 */
	public PPCA_NavigationPanel getNavigationPanel() 
	{
		return navigationPanel;
	}

	/**
	 * @return the mainPanel
	 */
	public static PPCA_MainPanel getMainPanel() 
	{
		return mainPanel;
	}

	/**
	 * @return the width
	 */
	public int getWidth() 
	{
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() 
	{
		return height;
	}

	/**
	 * Run the program
	 */
	public void run()
	{
		
	}
}
