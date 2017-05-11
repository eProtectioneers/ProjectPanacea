package org.eprotectioneers.panacea.userinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.eprotectioneers.panacea.cs4235.PGPClient.email.PPCA_MailClient;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_DataRepo;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_Preferences;

import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;

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
	private JTextField txtInboundServer;
	private JPasswordField txtPassword;
	private JButton btnLogin;

	private JButton okButton;
	private JButton cancelButton;
	private JPanel exchangePanel;
	
	private PPCA_DataRepo or;
	private PPCA_Preferences pref;

	private final String outbound = "smtp.gmail.com";
	private final String inbound = "pop.gmail.com";
	
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
		this.setLocation(frameLocation.x + frame.getWidth() / 8, frameLocation.y + frame.getHeight() / 8);
		this.setPreferredSize(new Dimension(800, 600));

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
