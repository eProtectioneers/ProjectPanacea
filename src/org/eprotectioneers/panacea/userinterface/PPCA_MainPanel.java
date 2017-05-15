package org.eprotectioneers.panacea.userinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument.Content;

import org.eprotectioneers.panacea.cs4235.PGPClient.email.PPCA_PGPMail;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

import javax.swing.JEditorPane;

/**
 * This class represents a main panel where email will be displayed.
 * @author eProtectioneers
 */
public class PPCA_MainPanel extends JPanel 
{
	//Bringing HTML5 Rendering
	private final JFXPanel jfxPanel = new JFXPanel();
	
	private WebView webComponent;
	private JTextArea txtEmail;
	private PPCA_PanaceaWindow window;
	private PPCA_PGPMail email;
	
	private double height;
	private double width;
	
	JEditorPane paneMail = new JEditorPane();
	
	/**
	 * Constructor
	 */
	public PPCA_MainPanel(JFrame frame, PPCA_PanaceaWindow window)
	{
		this.setBackground (Color.WHITE);
		this.setLayout(new BorderLayout());
		initializeComponent();
		
		this.window = window;
	}
	
	/**
	 * Initialize components
	 */
	private void initializeComponent()
	{
		this.setBackground(new Color(243, 241, 239));
		//this.setBackground(new Color(242, 107, 58));
		/* Initialize components */
		txtEmail = new JTextArea();
		txtEmail.setText("No message.");
		txtEmail.setPreferredSize(new Dimension (886, 737));
		txtEmail.setLineWrap(true);
		txtEmail.setEditable(false);
		txtEmail.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		//JScrollPane scrollEmail = new JScrollPane(txtEmail);
		this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		JScrollPane scrollEmail = new JScrollPane(jfxPanel);
		/* Register event handlers */
		
		this.height = this.getHeight();
		this.width = this.getWidth();
		
		/* Add components */
		this.add(jfxPanel, BorderLayout.CENTER);
		
		scrollEmail.setColumnHeaderView(paneMail);
		
		paneMail.setContentType("text/html");
		paneMail.setText("<h1>NO MAILS TO DISPLAY (Please select one)</h1>");
		

	}
	
	public void resetWorkspace(){
		this.height = this.getHeight();
		this.width = this.getWidth();
		//Could create problems
		this.jfxPanel.setPreferredSize(this.getSize());
		runWorkspace();
	}
	
	public void resetHelp(){
		this.height = this.getHeight();
		this.width = this.getWidth();
		//Could create problems
		this.jfxPanel.setPreferredSize(this.getSize());
		runHelp();
	}
	
	public void resetShare(){
		this.height = this.getHeight();
		this.width = this.getWidth();
		//Could create problems
		this.jfxPanel.setPreferredSize(this.getSize());
		runShare();
	}
	
	/**
	 * Display email content.
	 * @param email the email
	 */
	public void show(PPCA_PGPMail email)
	{
		this.email = email;
		
		String display = "Sender:\t" + email.from + "\n";
		display += "Subject:\t" + email.subject + "\n\n";
		
		if (!email.isAunthentic)
			display += "WARNING: Unable to verify the signature!" + "\n\n";
		
		display += email.payload;
		
		//txtEmail.setText(display);
		paneMail.setText(email.payload);
		
		this.height = this.getHeight();
		this.width = this.getWidth();
		
		loadJavaFXScene();
		
		this.repaint();
	}
	
	/**
	 * Get the currently displayed email.
	 * @return the currently displayed email
	 */
	public PPCA_PGPMail getEmail()
	{
		return email;
	}
	
	private void loadJavaFXScene(){
		Platform.setImplicitExit(false);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BorderPane borderPane = new BorderPane();
				webComponent = new WebView();
				webComponent.getEngine().loadContent(getHTML(email.payload));
				borderPane.setCenter(webComponent);
				Scene scene = new Scene(borderPane,width-15,height-15);
				jfxPanel.setScene(scene);
			}
		});
	}
	
	private static String getHTML(String base){
		try{
			int startInd = 0;
			int endInd = base.indexOf("<");
			String reString = "";
			String toBeReplaced = base.substring(startInd, endInd);
			return base.replace(toBeReplaced,reString);
		}
		catch(Exception ex){
			ex.printStackTrace();
			return "Failed to remove mail information!";
		}
	}
	
	private void runWorkspace(){
		Platform.setImplicitExit(false);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BorderPane borderPane = new BorderPane();
				borderPane.setPrefSize(width-20, height-20);
				webComponent = new WebView();
				webComponent.getEngine().load("file:///"+new File("content/PPCA_Workspace/welcome_offline.html").getAbsolutePath());
				webComponent.setPrefSize(width-20, height-20);
				borderPane.setCenter(webComponent);
				Scene scene = new Scene(borderPane,width-20,height-20);
				jfxPanel.setScene(scene);
				jfxPanel.setBackground(new Color(191, 168, 140));
			}
		});
	}
	
	private void runHelp(){
		Platform.setImplicitExit(false);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BorderPane borderPane = new BorderPane();
				borderPane.setPrefSize(width-20, height-20);
				webComponent = new WebView();
				webComponent.getEngine().load("file:///"+new File("content/PPCA_Workspace/index.html").getAbsolutePath());
				webComponent.setPrefSize(width-20, height-20);
				borderPane.setCenter(webComponent);
				Scene scene = new Scene(borderPane,width-20,height-20);
				jfxPanel.setScene(scene);
				jfxPanel.setBackground(new Color(191, 168, 140));
			}
		});
	}
	
	private void runShare(){
		Platform.setImplicitExit(false);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				BorderPane borderPane = new BorderPane();
				borderPane.setPrefSize(width-20, height-20);
				webComponent = new WebView();
				webComponent.getEngine().load("file:///"+new File("content/PPCA_Workspace/share.html").getAbsolutePath());
				webComponent.setPrefSize(width-20, height-20);
				borderPane.setCenter(webComponent);
				Scene scene = new Scene(borderPane,width-20,height-20);
				jfxPanel.setScene(scene);
				jfxPanel.setBackground(new Color(191, 168, 140));
			}
		});
	}
	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		jfxPanel.setSize(this.getSize());
	}
	
}
