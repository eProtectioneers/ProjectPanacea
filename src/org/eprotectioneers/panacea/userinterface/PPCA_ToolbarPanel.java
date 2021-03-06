//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.userinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.mail.Flags.Flag;
import javax.mail.MessagingException;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.eprotectioneers.panacea.cs4235.PGPClient.email.PPCA_MailClient;
import org.eprotectioneers.panacea.cs4235.PGPClient.email.PPCA_PGPMail;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import javax.swing.Icon;

/**
 * This class represents a toolbar panel.
 * @author eProtectioneers
 */
public class PPCA_ToolbarPanel extends JPanel 
{
	//Visual components
	private JFrame frame;
	private JButton btnSetting;
	private JButton btnCryptoConfig;
	private JButton btnEmailSync;
	private JButton btnEmailCompose;
	private JButton btnEmailReply;
	private JButton btnEmailDelete;
	private JButton btnEmailForward;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JButton btnWorkspace;
	private JButton btnHelp;
	private JButton btnShare;
	
	/**
	 * PanaceaWindow
	 */
	private PPCA_PanaceaWindow window;
	/**
	 * MailClient
	 */
	private PPCA_MailClient ec;
	


	/**
	 * Constructor
	 */
	public PPCA_ToolbarPanel(JFrame frame, PPCA_PanaceaWindow window)
	{
		this.frame = frame;
		this.setPreferredSize(new Dimension (1200, 40));
		this.setBackground (Color.WHITE);
		initializeComponent();

		this.window = window;
		
		
	}

	/**
	 * Initialize components
	 */
	private void initializeComponent()
	{
		//Initialize components
		ButtonListener bl = new ButtonListener();

		ImageIcon imgSetting = new ImageIcon ("images/setting.png");

		ImageIcon imgCryptoConfig = new ImageIcon ("images/lock.png");

		ImageIcon imgEmailSync = new ImageIcon ("images/email_download.png");

		ImageIcon imgEmailCompose = new ImageIcon ("images/email_new.png");

		ImageIcon imgEmailReply = new ImageIcon ("images/email_reply.png");

		ImageIcon imgEmailForward = new ImageIcon ("images/email_forward.png");

		ImageIcon imgEmailDelete = new ImageIcon ("images/email_delete.png");

		ImageIcon imgHelp = new ImageIcon ("images/help.png");
		
		ImageIcon imgShare = new ImageIcon("images/share.png");
		

		setLayout(new BorderLayout(0, 0));
		
		panel_2 = new JPanel();
		panel_2.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		add(panel_2,BorderLayout.WEST);
		btnEmailCompose = new JButton (imgEmailCompose);
		btnEmailCompose.setText("Compose new Mail");
		btnEmailCompose.setMinimumSize(new Dimension(295,40));
		btnEmailCompose.setPreferredSize(new Dimension(290, 33));
		btnEmailCompose.setSize(new Dimension(295, 40));
		
		panel_2.add(btnEmailCompose);
		btnEmailCompose.setToolTipText("Compose new message");
		btnEmailCompose.addActionListener(bl);
		
		panel_1 = new JPanel();
		add(panel_1,BorderLayout.CENTER);
		btnEmailSync = new JButton (imgEmailSync);
		panel_1.add(btnEmailSync);
		btnEmailSync.setSize(new Dimension(350, 60));
		btnEmailSync.setToolTipText("Get new messages");
		btnEmailReply = new JButton (imgEmailReply);
		panel_1.add(btnEmailReply);
		btnEmailReply.setToolTipText("Reply to sender of selected message");
		btnEmailForward = new JButton (imgEmailForward);
		panel_1.add(btnEmailForward);
		btnEmailForward.setToolTipText("Forward selected message");
		btnEmailDelete = new JButton (imgEmailDelete);
		panel_1.add(btnEmailDelete);
		btnEmailDelete.setToolTipText("Delete selected message");
		btnEmailDelete.addActionListener(bl);
		btnEmailForward.addActionListener(bl);
		btnEmailReply.addActionListener(bl);
		btnEmailSync.addActionListener(bl);

		panel = new JPanel();
		add(panel,BorderLayout.EAST);
		
		btnWorkspace = new JButton("Workspace");
		btnWorkspace.setToolTipText("Workspace");
		btnWorkspace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Set the Workspace Panel as Center Display
				window.setCenterPanel(window.getMainPanel());
				window.getMainPanel().resetWorkspace();
				window.getNavigationPanel().populateTable();
			}
		});
		btnWorkspace.setIcon(new ImageIcon("images/workspace.png"));
		panel.add(btnWorkspace);
		btnSetting = new JButton (imgSetting);
		btnSetting.setText("Settings");
		panel.add(btnSetting);
		btnSetting.setToolTipText("Settings");
		btnCryptoConfig = new JButton (imgCryptoConfig);
		btnCryptoConfig.setText("Security");
		panel.add(btnCryptoConfig);
		btnCryptoConfig.setToolTipText("Crypto Config");
		
		btnHelp = new JButton(imgHelp);
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//Set the Help Panel as Center Display
				window.setCenterPanel(window.getMainPanel());
				window.getMainPanel().resetHelp();
				window.getNavigationPanel().populateTable();
				
			}
		});
		btnHelp.setToolTipText("Help Page");
		panel.add(btnHelp);
		btnCryptoConfig.addActionListener(bl);
		btnSetting.addActionListener(bl);
		
		btnShare = new JButton(imgShare);
		btnShare.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//Set the Share Panel as Center Display
				window.setCenterPanel(window.getMainPanel());
				window.getMainPanel().resetShare();
				window.getNavigationPanel().populateTable();
			}
		});
		btnShare.setToolTipText("Social");
		panel.add(btnShare);
		
		//Preloading
		ec = PPCA_MailClient.getInstance();
	}

	/**
	 * ButtonListener (Relic)
	 * @author eProtectioneers
	 */
	private class ButtonListener implements ActionListener
	{
		/**
		 * actionPerformed
		 */
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			Object source = e.getSource();

			if (source == btnSetting)
			{
				new PPCA_SettingDialog(frame);
			}
			else if (source == btnCryptoConfig)
			{
				new PPCA_EncryptionConfigWindow(frame);
			}
			else if (source == btnEmailSync)
			{
				PPCA_PGPMail[] emails  = ec.getMessages();
				if (emails == null)
				{
					JOptionPane.showMessageDialog(frame, "Error: Unable to retrieve emails!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (emails.length == 0)
				{
					JOptionPane.showMessageDialog(frame, "No new messages in your inbox.");
				}
				else
				{
					PPCA_NavigationPanel np = window.getNavigationPanel();
					np.show(emails);
				}
				PPCA_NavigationPanel.populateTable();
			}
			else if (source == btnEmailCompose)
			{
				new PPCA_ComposeWindow(frame);
			}
			else if (source == btnEmailReply)
			{
				PPCA_MainPanel mp = window.getMainPanel();
				PPCA_PGPMail email = mp.getEmail();

				if (email == null)
				{
					JOptionPane.showMessageDialog(null, "Select an email to reply first.");
				}
				else
				{
					PPCA_ComposeWindow cd = new PPCA_ComposeWindow(frame);
					cd.setEmail(email, PPCA_PGPMail.Type.REPLY);
				}
			}
			else if (source == btnEmailForward)
			{
				PPCA_MainPanel mp = window.getMainPanel();
				PPCA_PGPMail email = mp.getEmail();

				if (email == null)
				{
					JOptionPane.showMessageDialog(null, "Select an email to forward first.");
				}
				else
				{
					PPCA_ComposeWindow cd = new PPCA_ComposeWindow(frame);
					cd.setEmail(email, PPCA_PGPMail.Type.FORWARD);
				}
			}
			else if (source == btnEmailDelete)
			{
				JOptionPane.showMessageDialog(null, "Currently unsupported...!");
			}
		}
	}
}
