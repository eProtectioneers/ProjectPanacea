package org.eprotectioneers.panacea.userinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

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
	private JTable tblEmail;

	private PPCA_PanaceaWindow window;
	private PPCA_EmailStore es;

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
		tblEmail.setRowHeight(tblEmail.getRowHeight() * 5);
		tblEmail.setDefaultRenderer(Object.class, new MultiLineCellRenderer());
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

	public void populateTable()
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
			String preview = getPreview(emails[i]);
			emailStore[i][0] = preview;
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
	private String getPreview(PPCA_PGPMail email)
	{
		String preview = removeMail(email.from) + "\n";
		preview += email.subject + "\n";
		//preview += snippet(email.payload);
		return preview;
	}
	
	private String removeMail(String removeFrom){
		try{
			int startInd = removeFrom.indexOf("<");
			int endInd = removeFrom.indexOf(">");
			String reString = "";
			String toBeReplaced = removeFrom.substring(startInd, endInd+1);
			return removeFrom.replace(toBeReplaced,reString);
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
		@Override
		public void mousePressed(MouseEvent e) 
		{
			if(e.getButton()==MouseEvent.BUTTON1){
				Object source = e.getSource();
				if (source == tblEmail)
				{
					int row = tblEmail.getSelectedRow();
					PPCA_PGPMail email = es.getEmail(row);
	
					window.setCenterPanel(window.getMainPanel());
					PPCA_MainPanel mp = window.getMainPanel();
					mp.show(email);
				}
			}
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
