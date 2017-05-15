package org.eprotectioneers.panacea.userinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.eprotectioneers.panacea.cs4235.Main.PanaceaExecutable;
import org.eprotectioneers.panacea.cs4235.PGPClient.email.PPCA_MailClient;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_FileInfo;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_DBEraser;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_DataRepo;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_Preferences;

import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;

/**
 * This class represents a Dialog for Settings
 * @author eProtectioneers
 */
public class PPCA_SettingDialog extends JDialog 
{
	private JTabbedPane tabbedPane;
	private JPanel loginPanel;

	private JTextField txtUsername;
	private JTextField txtOutboundServer;
	private JTextField txtDatabaseFileLocation;
	private JTextField txtInboundServer;
	private JPasswordField txtPassword;
	private JButton btnLogin;

	private JButton okButton;
	private JButton cancelButton;
	private JPanel exchangePanel;
	private JPanel databasepanel;
	
	private PPCA_DataRepo or;
	private PPCA_Preferences pref;

	private final String outbound = "smtp.gmail.com";
	private final String inbound = "pop.gmail.com";
	private JLabel lblFilename;
	private JLabel lblLastEdited;
	private JLabel lblPanaceaVersion;
	private JLabel lblFileSize;
	private JLabel lblOwner;
	private JButton btnDeleteDatabase;
	private JButton btnScanForDatabase;
	private JLabel lblCreated;
	private JLabel lblAccessed;
	private JLabel lblWritten;
	private JLabel lbl_name;
	private JLabel lbl_path;
	private JLabel lbl_PanaceaVersion;
	private JLabel lblSize;
	private JLabel lbl_owner;
	private JLabel lbl_created;
	private JLabel lbl_acessed;
	private JLabel lbl_written;
	private JButton btnOpenFileIn;
	
	/**
	 * Create the dialog.
	 */
	public PPCA_SettingDialog(JFrame frame) 
	{
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Project Panacea - Settings | [ACCOUNT]");
		Point frameLocation = frame.getLocation();
		this.setPreferredSize(new Dimension(800, 600));
		this.setLocation((int) (frameLocation.x + frame.getWidth()/2-this.getPreferredSize().getWidth()/2),
				(int)(frameLocation.y + frame.getHeight()/2-this.getPreferredSize().getHeight()/2));

		/* Set Components */
		getContentPane().setLayout(new BorderLayout());
		{
			tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			getContentPane().add(tabbedPane, BorderLayout.CENTER);
			{
				loginPanel = new JPanel();
				tabbedPane.addTab("User Login", null, loginPanel, null);
				loginPanel.setLayout(new MigLayout("", "[190px][350px][50px]", "[30px][30px][600px]"));
				{
					JLabel lblUsername = new JLabel("Username:");
					lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
					loginPanel.add(lblUsername, "flowy,cell 0 0,grow");
				}
				{
					txtUsername = new JTextField();
					loginPanel.add(txtUsername, "flowx,cell 1 0,grow");
					txtUsername.setColumns(10);
				}
				{
					JLabel lblPassword = new JLabel("Password:");
					lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
					loginPanel.add(lblPassword, "cell 0 1,grow");
				}
				{
					txtPassword = new JPasswordField();
					loginPanel.add(txtPassword, "cell 1 1,grow");
					txtPassword.setColumns(10);
				}
				{
					btnLogin = new JButton("Login...");
					loginPanel.add(btnLogin, "cell 2 1,grow");
				}
			}
			{
				databasepanel = new JPanel();
				tabbedPane.addTab("Data Management", null, databasepanel, null);
				databasepanel.setLayout(new MigLayout("", "[190px][350px][50px]", "[30px][30px][][][][][][][][][][][][][][][][][][600px]"));
				{
					JLabel lblDBFile = new JLabel("Database File:");
					lblDBFile.setHorizontalAlignment(SwingConstants.RIGHT);
					databasepanel.add(lblDBFile, "flowy,cell 0 0,grow");
				}
				{
					txtDatabaseFileLocation = new JTextField();
					txtDatabaseFileLocation.setEditable(false);
					txtDatabaseFileLocation.setText(outbound);
					databasepanel.add(txtDatabaseFileLocation, "flowx,cell 1 0,grow");
					txtDatabaseFileLocation.setColumns(10);
				}
				{
					JLabel lblFileInfo = new JLabel("File Information:\r\n");
					lblFileInfo.setFont(new Font("Dialog", Font.BOLD, 13));
					lblFileInfo.setHorizontalAlignment(SwingConstants.RIGHT);
					databasepanel.add(lblFileInfo, "flowy,cell 0 1 2 1,alignx leading,growy");
				}
				{
					lblFilename = new JLabel("Name:");
					databasepanel.add(lblFilename, "cell 0 2,alignx right");
				}
				{
					lbl_name = new JLabel("Name:");
					databasepanel.add(lbl_name, "cell 1 2");
				}
				{
					lblLastEdited = new JLabel("Absolute Path:");
					databasepanel.add(lblLastEdited, "cell 0 3,alignx right");
				}
				{
					lbl_path = new JLabel("Name:");
					databasepanel.add(lbl_path, "cell 1 3");
				}
				{
					lblPanaceaVersion = new JLabel("Panacea Version:");
					databasepanel.add(lblPanaceaVersion, "cell 0 4,alignx right");
				}
				{
					lbl_PanaceaVersion = new JLabel("Name:");
					databasepanel.add(lbl_PanaceaVersion, "cell 1 4");
				}
				{
					lblFileSize = new JLabel("File Size:");
					databasepanel.add(lblFileSize, "cell 0 6,alignx right");
				}
				{
					lblSize = new JLabel("Name:");
					databasepanel.add(lblSize, "cell 1 6");
				}
				{
					{
						lblOwner = new JLabel("Owner:\r\n");
						databasepanel.add(lblOwner, "cell 0 8,alignx right");
					}
					{
						lbl_owner = new JLabel("Name:");
						databasepanel.add(lbl_owner, "cell 1 8");
					}
					{
						lblCreated = new JLabel("Created:");
						databasepanel.add(lblCreated, "cell 0 9,alignx right");
					}
					{
						lbl_created = new JLabel("//Usupported");
						lbl_created.setForeground(Color.LIGHT_GRAY);
						databasepanel.add(lbl_created, "cell 1 9");
					}
					{
						lblAccessed = new JLabel("Accessed:");
						databasepanel.add(lblAccessed, "cell 0 10,alignx right");
					}
					{
						lbl_acessed = new JLabel("//Usupported");
						lbl_acessed.setForeground(Color.LIGHT_GRAY);
						databasepanel.add(lbl_acessed, "cell 1 10");
					}
					{
						lblWritten = new JLabel("Written:");
						databasepanel.add(lblWritten, "cell 0 11,alignx right");
					}
				}
				btnScanForDatabase = new JButton("Scan for Database (Unsupported)");
				btnScanForDatabase.setEnabled(false);
				btnScanForDatabase.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//Currently not working
					}
				});
				{
					lbl_written = new JLabel("//Usupported");
					lbl_written.setForeground(Color.LIGHT_GRAY);
					databasepanel.add(lbl_written, "cell 1 11");
				}
				{
					btnOpenFileIn = new JButton("Open File in Explorer");
					btnOpenFileIn.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							File f = new File("PPCA_Storage");
							File folder = new File(f.getAbsolutePath());
							try {
								Desktop.getDesktop().open(folder);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					databasepanel.add(btnOpenFileIn, "cell 1 16,growx");
				}
				databasepanel.add(btnScanForDatabase, "cell 1 17,growx");
				{
					btnDeleteDatabase = new JButton("Delete Database safely\r\n");
					btnDeleteDatabase.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
								PPCA_DBEraser xDeb = new PPCA_DBEraser(new File("PPCA_Storage"));
								xDeb.setDaemon(true);
								xDeb.run();
						}
					});
					btnDeleteDatabase.setForeground(Color.WHITE);
					btnDeleteDatabase.setBackground(Color.RED);
					databasepanel.add(btnDeleteDatabase, "cell 1 18,growx");
				}
			}
			{
				exchangePanel = new JPanel();
				tabbedPane.addTab("Exchange Settings", null, exchangePanel, null);
				exchangePanel.setLayout(new MigLayout("", "[190px][350px][50px]", "[30px][30px][600px]"));
				{
					JLabel lblOutboundConnection = new JLabel("Outbound Connection:");
					lblOutboundConnection.setHorizontalAlignment(SwingConstants.RIGHT);
					exchangePanel.add(lblOutboundConnection, "flowy,cell 0 0,grow");
				}
				{
					txtOutboundServer = new JTextField();
					txtOutboundServer.setEditable(false);
					txtOutboundServer.setText(outbound);
					exchangePanel.add(txtOutboundServer, "flowx,cell 1 0,grow");
					txtOutboundServer.setColumns(10);
				}
				{
					JLabel lblInboundConnection = new JLabel("Inbound Connection:");
					lblInboundConnection.setHorizontalAlignment(SwingConstants.RIGHT);
					exchangePanel.add(lblInboundConnection, "flowy,cell 0 1,grow");
				}
				{
					txtInboundServer = new JTextField();
					txtInboundServer.setEditable(false);
					txtInboundServer.setText(inbound);
					exchangePanel.add(txtInboundServer, "flowx,cell 1 1,grow");
					txtInboundServer.setColumns(10);
				}
			}
		}
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		/* Preloading event */
		or = PPCA_DataRepo.getInstance();
		pref = or.getPreferences();
		txtUsername.setText(pref.getUsername());
		txtPassword.setText(pref.getPassword());

		/* Set event handler */
		ButtonListener listener = new ButtonListener();
		btnLogin.addActionListener(listener);
		okButton.addActionListener(listener);
		cancelButton.addActionListener(listener);

		/* Set visibility */
		pack();
		setVisible(true);
		try {
			databaseInformation();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void databaseInformation() throws IOException, ParseException	{
		
		File f = new File("PPCA_Storage");
		PPCA_FileInfo file = new PPCA_FileInfo(f);
		
		txtDatabaseFileLocation.setText(file.getAbsolutePath());
		lbl_name.setText(file.getName());
		lbl_path.setText(file.getAbsolutePath());
		lblSize.setText(String.valueOf(file.getSize()));
		lbl_owner.setText(file.getOwner());
		lbl_PanaceaVersion.setText(PanaceaExecutable.version);
		//lbl_modified.setText(file.getLastModified().toString());
		//System.out.println("Created: " + file.getCreated());
		//System.out.println("Accessed: " + file.getAccessed());
		//System.out.println("Written: " + file.getWritten());
	}
	

	private class ButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			Object source = e.getSource();

			if (source == btnLogin)
			{
				String username = txtUsername.getText();
				String password = new String(txtPassword.getPassword());
				PPCA_MailClient ec = PPCA_MailClient.getInstance();

				/* Validation */
				if (username.isEmpty() || password.isEmpty())
				{
					JOptionPane.showMessageDialog(PPCA_SettingDialog.this, "Error: Username and Password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					if (ec.login(username, password))
					{
						/* nop */
					}
					else
					{
						JOptionPane.showMessageDialog(PPCA_SettingDialog.this, "Invalid username and/or password.");
					}
				}
			}
			else if (source == okButton)
			{ 
				/* Write to Database */
				or.savePreferences(pref);
				
				/* Close the dialog */
				PPCA_SettingDialog.this.dispose();
			}
			else if (source == cancelButton)
			{
				/* Close the dialog */
				PPCA_SettingDialog.this.dispose();
			}
		}
	}
	
	
	
	
	
}
