package org.eprotectioneers.panacea.userinterface;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.BoxLayout;
import net.miginfocom.swing.MigLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import org.eprotectioneers.panacea.contactmanagement.components.ImagePanel;
import org.eprotectioneers.panacea.contactmanagement.models.Contact;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseC;
import org.eprotectioneers.panacea.contactmanagement.view.Page_AddContact;
import org.eprotectioneers.panacea.contactmanagement.view.Page_Contact;
import org.eprotectioneers.panacea.cs4235.PGPClient.email.PPCA_PGPMail;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JMenuItem;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.MatteBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * PPCA_EmailView (JPanel)
 * unused and deprecated view class
 * @author eProtectioneers
 * (Deletion within next few releases)
 */
@Deprecated
public class PPCA_EmailView extends JPanel{
	
	/**
	 * MainPanel instance
	 */
	private PPCA_MainPanel mainpanel;
	/**
	 * Email to bind
	 */
	private PPCA_PGPMail email;
	
	/**
	 * jfxPanel to show HTML content
	 */
	private JFXPanel jfxPanel = new JFXPanel();
	/**
	 * JavaFX webComponent
	 */
	private WebView webComponent;
	
	private String _from;
	private String _to;
	private String _subject;
	private String _payload;
	
	private JPanel pnl_header;
	private JPanel pnlMain;
	private ImagePanel imagePanel;
	private JLabel lblFrom_content;
	private JLabel lblSubject;
	private JPanel pnl_descrition;
	private JPanel pnl_text;
	private JLabel lblFrom;
	private JLabel lblTo;
	private JLabel lblTo_content;
	private JLabel lblSubject_content;
	private Contact _c;
	
	/**
	 * Constructor
	 */
	public PPCA_EmailView(PPCA_PGPMail email,PPCA_MainPanel mainpanel) {
			this._from=email.from;
			this._to=email.to;
			this._subject=email.subject;
			this._payload=email.payload;
			this.mainpanel = mainpanel;
			this.email = email;
			this._c=DatabaseC.checkContact(_from);
			
			initialize();
	}
	
	/**
	 * initialises the panel
	 */
	private void initialize() {
		Dimension d=new Dimension(200, 100);
		setMaximumSize(d);
		setMinimumSize(d);
		setPreferredSize(new Dimension(473, 316));
		setBackground(Color.WHITE);
		setBorder(new EmptyBorder(0, 5, 0, 5));
		setLayout(new BorderLayout());

		pnl_header = new JPanel();
		pnl_header.setOpaque(false);
		pnl_header.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		pnl_header.setPreferredSize(new Dimension(10, 50));
		pnl_header.setMinimumSize(new Dimension(10, 50));
		pnl_header.setMaximumSize(new Dimension(32767, 50));
		add(pnl_header,BorderLayout.NORTH);
		pnl_header.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnl_header.setLayout(new BoxLayout(pnl_header, BoxLayout.X_AXIS));
		
		pnl_descrition = new JPanel();
		pnl_descrition.setOpaque(false);
		pnl_descrition.setBorder(new EmptyBorder(0, 5, 0, 0));
		pnl_header.add(pnl_descrition);		
		pnl_descrition.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pnl_descrition.setLayout(new BoxLayout(pnl_descrition, BoxLayout.Y_AXIS));
		
		lblFrom = new JLabel("From:");
		lblFrom.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lblFrom.setFont(new Font("Calibri", Font.PLAIN, 13));
		pnl_descrition.add(lblFrom);
		
		lblTo = new JLabel("To:\r\n");
		lblTo.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lblTo.setFont(new Font("Calibri", Font.PLAIN, 13));
		pnl_descrition.add(lblTo);
		
		lblSubject = new JLabel("Subject:");
		lblSubject.setBorder(null);
		lblSubject.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lblSubject.setFont(new Font("Calibri", Font.PLAIN, 13));
		pnl_descrition.add(lblSubject);
		
		pnl_text = new JPanel();
		pnl_text.setOpaque(false);
		pnl_text.setBorder(new EmptyBorder(0, 5, 0, 5));
		pnl_header.add(pnl_text);
		pnl_text.setLayout(new BoxLayout(pnl_text, BoxLayout.Y_AXIS));
		
		lblFrom_content = new JLabel(_from);
		pnl_text.add(lblFrom_content);
		lblFrom_content.setBorder(null);
		lblFrom_content.setFont(new Font("Calibri", Font.BOLD, 13));
		
		lblTo_content = new JLabel(_to);
		lblTo_content.setFont(new Font("Calibri", Font.BOLD, 13));
		lblTo_content.setBorder(null);
		pnl_text.add(lblTo_content);
		
		lblSubject_content = new JLabel(_subject);
		lblSubject_content.setFont(new Font("Calibri", Font.BOLD, 13));
		lblSubject_content.setBorder(null);
		pnl_text.add(lblSubject_content);
		
		if(_c!=null){
			imagePanel = new ImagePanel(_c.getPicturepath());
			imagePanel.setOpaque(false);
			imagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			imagePanel.setBorder(null);
			pnl_header.add(imagePanel);
			imagePanel.setRadius(5);
			imagePanel.setDisabled();
		}
		
		pnlMain = new JPanel();
		pnlMain.setOpaque(false);
		pnlMain.setBorder(new EmptyBorder(5, 5, 5, 5));
		pnlMain.setLayout(new BorderLayout());
		add(pnlMain, BorderLayout.CENTER);
		if(_payload.length()>255){
			_payload=_payload.substring(0, 252);
			_payload+="...";
		}
		
		this.pnlMain.add(jfxPanel, BorderLayout.CENTER);
		setToolTipText(_from);
		show();
	}
	
	/**
	 * Display email content.
	 * @param email the email
	 */
	public void show()
	{						
		loadJavaFXScene();
		
		this.repaint();
	}
	
	/**
	 * Load the email HTML content in the JavaFX Webview
	 */
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
				Scene scene = new Scene(borderPane);
				jfxPanel.setScene(scene);
			}
		});
	}
	
	/**
	 * Return the HTML Content of a mail
	 * @param base
	 * @return
	 */
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
	
	
	@Override
	public void addMouseListener(MouseListener ml){
		super.addMouseListener(ml);
		for(Component c:this.getComponents()){
			c.addMouseListener(ml);
		}
		imagePanel.addMouseListener(ml);
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
}
