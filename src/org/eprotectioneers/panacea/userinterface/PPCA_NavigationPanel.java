package org.eprotectioneers.panacea.userinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.eprotectioneers.panacea.contactmanagement.models.Contact;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseC;
import org.eprotectioneers.panacea.contactmanagement.view.Page_AddContact;
import org.eprotectioneers.panacea.contactmanagement.view.Page_Contact;
import org.eprotectioneers.panacea.cs4235.PGPClient.email.PPCA_PGPMail;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_DataRepo;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_EmailStore;

/**
 * This class represents a navigation panel where list of available
 * emails will be displayed.
 * @author eProtectioneers
 */
public class PPCA_NavigationPanel extends JPanel 
{
	private static JTable tblEmail;

	private PPCA_PanaceaWindow window;
	private static PPCA_EmailStore es;
	
	private JPopupMenu pum;
	private JMenuItem mni_reply;
	private JMenuItem mni_forward;
	private JMenuItem mni_delete;
	private JMenuItem mni_addtoc;
	private JMenuItem mni_openc;
	private JMenuItem mni_changespam;

	/**
	 * Constructor
	 */
	public PPCA_NavigationPanel(JFrame frame, PPCA_PanaceaWindow window)
	{
		this.es = PPCA_DataRepo.getInstance().getEmailStore();

		this.setPreferredSize(new Dimension (300, 750));
		this.setBackground (new Color(238, 222, 213));
		this.setLayout(new BorderLayout());
		initializeComponent();
		this.window = window;
	}

	/**
	 * Initialize components
	 */
	private void initializeComponent()
	{
		/* Initialize components */
		tblEmail = new JTable();
		tblEmail.setFillsViewportHeight(true);
		tblEmail.setRowHeight(tblEmail.getRowHeight() * 3);
		tblEmail.setDefaultRenderer(Object.class, new TooltipCellRenderer());
		tblEmail.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollEmail = new JScrollPane(tblEmail);	

		/* Register event handlers */
		MouseListener ml = new MouseListener();
		tblEmail.addMouseListener(ml);

		/* Add components */
		this.add(scrollEmail, BorderLayout.CENTER);

		/* Preloading events */
		populateTable();
	}

	/**
	 * Create a snippet of an message.
	 * @param message the message
	 */
	private String snippet(String message)
	{
		String result = "";
		int maxCount = 100;

		if (message.length() <= maxCount)
		{
			result = message;
		}
		else
		{
			result = message.substring(0, maxCount);
			if (message.length() > maxCount)
				result += "...";
		}

		return result;
	}

	public static void populateTable()
	{
		tblEmail.removeAll();
		/* Retrieve emails */
		PPCA_PGPMail[] emails = es.getEmails();

		/* Populate data for the table */
		String[] columnName = {"INBOX"};
		String[][] emailStore = new String[emails.length][columnName.length];
		
		for (int i = 0; i < emails.length; i++)
		{
			/* Construct Email Preview */
			//String preview = getPreview(emails[i]);
			Contact c=DatabaseC.checkContact(emails[i].from);
			boolean b=c!=null;
			String html = "<html><div>"
					+ "<a style='color:#FF4F00'><strong><em>Sender: </em></strong></a><a style='color:#191919'><strong>";
			if(b)html+=c.getShownname()+" </strong>";
			else html+=emails[i].from+"</strong>";
			if(b){
				html+="<img src='file:///" +
						c.getPicturepath() +
						"' width=15 height=15 />";
			}
			html+= "</a><p align='justify' style='color:#191919word-wrap:break-word'>"+getPreview(emails[i])+"</p>"
					+ "</div></html>"
					+ ""
					+ "t0oLTPt€xT:"+removeName(emails[i].from);

			emailStore[i][0] = html;
		}
		
		/* Set table model */
		DefaultTableModel model = new DefaultTableModel()
		{
			@Override
			public boolean isCellEditable(int row, int column) 
			{
				return false;
			}    
		};
		model.setDataVector(emailStore, columnName);
		tblEmail.setModel(model);
	}

	/**
	 * Update email table
	 */
	private void updateTable(PPCA_PGPMail[] emails)
	{
		// Emails are in chronological order, i.e. newer email
		// is on the top.
		DefaultTableModel model = (DefaultTableModel) tblEmail.getModel();
		String[] rowEntry = new String[1];
		for (int i = 0; i < emails.length; i++)
		{
			rowEntry[0] = getPreview(emails[i]);
			model.insertRow(i, rowEntry);
		}	

	}

	/**
	 * Get a preview of an email.
	 * @param email the email
	 * @return the preview of an email
	 */
	private static String getPreview(PPCA_PGPMail email)
	{
		String preview = email.subject + "\n" +email.payload;
		//preview += snippet(email.payload);
		if(preview.length()>42){
			preview = preview.substring(0,41);
			preview+="...";
		}
		
		return preview;
	}
	
	private static String removeMail(String removeFrom){
		try{
			int startInd = removeFrom.indexOf("<");
			int endInd = removeFrom.indexOf(">");
			String reString = "";
			String toBeReplaced = removeFrom.substring(startInd-1, endInd+1);
			return removeFrom.replace(toBeReplaced,reString);
		}
		catch(Exception ex){
			ex.printStackTrace();
			return "Failed to remove mail information!";
		}
	}
	
	private static String removeName(String removeFrom){
		try{
			int startInd = removeFrom.indexOf("<");
			int endInd = removeFrom.indexOf(">");
			return removeFrom.substring(startInd+1, endInd);
		}
		catch(Exception ex){
			ex.printStackTrace();
			return "Failed to remove mail information!";
		}
	}	
	
	/**
	 * Display email metadata.
	 * @param metadata the email metadata
	 */
	public void show(PPCA_PGPMail[] emails)
	{
		updateTable(emails);
	}

	private class MouseListener extends MouseAdapter
	{
		private Thread t1;
		private String _sender;
		private String _subject;
		
		@Override
		public void mousePressed(MouseEvent e) 
		{
			Object source = e.getSource();
			if (source == tblEmail)
			{
				int row = tblEmail.rowAtPoint(new Point(e.getX(),e.getY()));
				PPCA_PGPMail email = es.getEmail(row);
				_sender=email.from;
				_subject=email.subject;
				
				if(e.getButton()==MouseEvent.BUTTON1){	
					window.setCenterPanel(window.getMainPanel());
					PPCA_MainPanel mp = window.getMainPanel();
					mp.show(email);
				}
				
				if(e.isPopupTrigger())showPopup(e);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Object source = e.getSource();
			if (source == tblEmail)
			{
				if(e.isPopupTrigger())showPopup(e);
			}
		}	
		
		private void showPopup(MouseEvent e){
			if(t1!=null&&t1.isAlive())t1.stop();
			t1=new Thread(new PopupGenerator(_sender,_subject));
			t1.start();

			while(t1.isAlive()){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			pum.show(tblEmail, e.getX(), e.getY());
		}
	}

	private class PopupGenerator implements Runnable{

		private Contact _c;
		private String _sender;
		private String _subject;
		
		public PopupGenerator(String sender, String subject){
			_sender=sender;
			_subject=subject;
		}
		@Override
		public void run() {
			
			_c=DatabaseC.checkContact(_sender);
			
			pum=new JPopupMenu();
			if(_subject.length()>18){
				_subject=_subject.substring(0, 15);
				_subject+="...";
			}
			pum.add(new JLabel(_subject));
			mni_reply=new JMenuItem("Reply to Message");
			mni_reply.addActionListener(new ReplyActionListener());
			pum.add(mni_reply);
			mni_forward=new JMenuItem("Forward Message");
			mni_forward.addActionListener(new ForwardActionListener());
			pum.add(mni_forward);
			mni_delete=new JMenuItem("Delete Message");
			mni_delete.addActionListener(new DeleteMessageActionListener());
			pum.add(mni_delete);
			if(_c==null){
				mni_addtoc=new JMenuItem("Add Sender to Contacts");
				mni_addtoc.addActionListener(new AddToContactsActionListener(_sender));
				pum.add(mni_addtoc);
			}
			if(_c!=null){
				mni_openc=new JMenuItem("Open Contact");
				mni_openc.setToolTipText(_c.toString());
				mni_openc.addActionListener(new OpenContactActionListener(_c));
				pum.add(mni_openc);
				if(_c.isSpam()){
					mni_changespam=new JMenuItem("Remove sender from Spam");
					mni_changespam.addActionListener(new ChangeSpamStateActionListener(_c));
				}else{
					mni_changespam=new JMenuItem("Add sender to Spam");
					mni_changespam.addActionListener(new ChangeSpamStateActionListener(_c));
				}
				pum.add(mni_changespam);
			}
		}
	}
	
	private class ReplyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//MZ
		}
	}
	
	private class ForwardActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//MZ
		}
	}
	
	private class DeleteMessageActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//MZ
		}
	}
	
	private class AddToContactsActionListener implements ActionListener{
		private String _s;
		public AddToContactsActionListener(String sender){
			_s=sender;
		}
		@Override
		public void actionPerformed(ActionEvent e) {	
			new Page_AddContact(PPCA_PanaceaWindow.getFrame(),removeMail(_s),removeName(_s)).setVisible(true);
		}
	}
	
	private class OpenContactActionListener implements ActionListener {
		private Contact _c;
		public OpenContactActionListener(Contact c){
			_c=c;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			PPCA_PanaceaWindow.setCenterPanel(new Page_Contact(_c));
		}
	}
	
	private class ChangeSpamStateActionListener implements ActionListener {
		private Contact _c;
		public ChangeSpamStateActionListener(Contact c){
			_c=c;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			_c.setSpam(!_c.isSpam(), true);
			setToolTipText(_c.toString());
		}
	}
	
	private class TooltipCellRenderer extends DefaultTableCellRenderer {
	    public Component getTableCellRendererComponent(
	                        JTable table, Object value,
	                        boolean isSelected, boolean hasFocus,
	                        int row, int column) {
	        JLabel c = (JLabel)super.getTableCellRendererComponent(table, value, hasFocus, hasFocus, column, column );
	        String[] s=c.getText().split("t0oLTPt€xT:");
	        c.setText(s[0]);
	        c.setToolTipText(s[1]);
	        return c;
	    }
	}
	
	private class MultiLineCellRenderer extends JTextArea implements TableCellRenderer
	{
		public MultiLineCellRenderer()
		{
			setLineWrap(true);
			setWrapStyleWord(true);
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row, int column) 
		{
			if (isSelected) 
			{
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} 
			else 
			{
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			setFont(table.getFont());

			if (hasFocus) 
			{
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				if (table.isCellEditable(row, column)) 
				{
					setForeground(UIManager.getColor("Table.focusCellForeground"));
					setBackground(UIManager.getColor("Table.focusCellBackground"));
				}
			} 
			else 
			{
				Border paddingBorder = BorderFactory.createEmptyBorder(1, 2, 1, 2);
				Border border = BorderFactory.createRaisedBevelBorder();
				setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));
			}

			setText((value == null) ? "" : value.toString());
			return this;
		}
	}
}
