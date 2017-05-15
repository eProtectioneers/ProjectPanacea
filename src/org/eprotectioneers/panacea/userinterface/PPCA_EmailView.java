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

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JMenuItem;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.border.MatteBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PPCA_EmailView extends JPanel{
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
	private JScrollPane scrollPane;
	private JTextArea taContent;
	private Contact _c;
	
	/**
	 * Create the panel.
	 */
	public PPCA_EmailView(PPCA_PGPMail email) {
			this._from=email.from;
			this._to=email.to;
			this._subject=email.subject;
			this._payload=email.payload;
			
			this._c=DatabaseC.checkContact(_from);
			
			initialize();
	}
	
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
		
		taContent = new JTextArea("");
		taContent.setFont(new Font("Arial", Font.PLAIN, 14));
		taContent.setEditable(false);
		taContent.setFocusable(false);
		taContent.setLineWrap(true);
		taContent.setWrapStyleWord(true);
		if(_payload.length()>255){
			_payload=_payload.substring(0, 252);
			_payload+="...";
		}
		taContent.setText(_payload);
		taContent.setOpaque(false);
		
		scrollPane=new JScrollPane(taContent);
		pnlMain.add(scrollPane,BorderLayout.CENTER);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(null);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		setToolTipText(_from);
	}
	
	@Override
	public void addMouseListener(MouseListener ml){
		super.addMouseListener(ml);
		for(Component c:this.getComponents()){
			c.addMouseListener(ml);
		}
		imagePanel.addMouseListener(ml);
		taContent.addMouseListener(ml);
	}
}
