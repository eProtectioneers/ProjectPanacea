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
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.eprotectioneers.panacea.cs4235.PGPClient.email.PPCA_MailClient;
import org.eprotectioneers.panacea.cs4235.PGPClient.email.PPCA_PGPMail;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_DataRepo;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_Preferences;

import net.miginfocom.swing.MigLayout;

/**
 * This class represent a dialog menu for Compose Email
 * @author eProtectioneers
 */
public class PPCA_ComposeWindow extends JDialog 
{
	private JTextField txtTo;
	private JTextField txtSubject;
	private JTextField txtFrom;
	private JTextArea txtBody;

	private JButton sendButton;
	private JButton cancelButton;

	private PPCA_MailClient ec;
	private PPCA_DataRepo or;
	private PPCA_Preferences pref;

	/**
	 * Create the dialog.
	 */
	public PPCA_ComposeWindow(JFrame frame) 
	{
		/* Set Properties */
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Project Panacea - Compose / [ACCOUNT]");
		Point frameLocation = frame.getLocation();
		this.setLocation(frameLocation.x + frame.getWidth() / 8, frameLocation.y + frame.getHeight() / 8);
		this.setPreferredSize(new Dimension(1000, 700));

		/* Set Components */
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel panelHeader = new JPanel();
			getContentPane().add(panelHeader, BorderLayout.NORTH);
			panelHeader.setLayout(new MigLayout("", "[80px][850px]", "[30px][30px]"));
			{
				JLabel lblTo = new JLabel("To:");
				panelHeader.add(lblTo, "cell 0 0,alignx right,growy");
			}
			{
				txtTo = new JTextField();
				panelHeader.add(txtTo, "cell 1 0,grow");
				txtTo.setColumns(10);
			}
			{
				JLabel lblSubject = new JLabel("Subject:");
				panelHeader.add(lblSubject, "cell 0 1,alignx right,growy");
			}
			{
				txtSubject = new JTextField();
				panelHeader.add(txtSubject, "cell 1 1,grow");
				txtSubject.setColumns(10);
			}
			{
				JLabel lblFrom = new JLabel("From:");
				panelHeader.add(lblFrom, "cell 0 2,alignx right,growy");
			}
			{
				txtFrom = new JTextField();
				txtFrom.setEditable(false);
				panelHeader.add(txtFrom, "cell 1 2,grow");
				txtFrom.setColumns(10);
			}
		}
		{
			JPanel panelCompose = new JPanel();
			getContentPane().add(panelCompose, BorderLayout.CENTER);
			{
				txtBody = new JTextArea();
				txtBody.setRows(30);
				txtBody.setColumns(80);
				panelCompose.add(txtBody);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				sendButton = new JButton("Send");
				sendButton.setActionCommand("OK");
				buttonPane.add(sendButton);
				getRootPane().setDefaultButton(sendButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		/* Preloading event */
		ec = PPCA_MailClient.getInstance();
		or = PPCA_DataRepo.getInstance();
		pref = or.getPreferences();
		txtFrom.setText(pref.getUsername());

		/* Set event handler */
		ButtonListener listener = new ButtonListener();
		sendButton.addActionListener(listener);
		cancelButton.addActionListener(listener);

		/* Set visibility */
		pack();
		setVisible(true);
	}

	/**
	 * Set the email for REPLY and FORWARD protocol
	 * @param email the email
	 */
	public void setEmail(PPCA_PGPMail email, PPCA_PGPMail.Type type)
	{
		if (type == PPCA_PGPMail.Type.REPLY)
		{
			this.txtTo.setText(email.from);
			this.txtSubject.setText("RE: " + email.subject);
		}
		else if (type == PPCA_PGPMail.Type.FORWARD)
		{
			this.txtSubject.setText("FW: " + email.subject);
			this.txtBody.setText(email.payload);
		}
	}

	private class ButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			Object source = e.getSource();

			if (source == sendButton)
			{
				boolean successful = false;

				String to = txtTo.getText();
				String subject = txtSubject.getText();
				String from = txtFrom.getText();
				String body = txtBody.getText();

				/* Validation */
				if (to.isEmpty())
				{
					JOptionPane.showMessageDialog(PPCA_ComposeWindow.this, "Error: Recipient cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					PPCA_PGPMail email = new PPCA_PGPMail(to, subject, from, body);
					successful = ec.send(email);
					if (successful)
					{
						JOptionPane.showMessageDialog(PPCA_ComposeWindow.this, "Message has been delivered successfully.");
					}
					else
					{
						JOptionPane.showMessageDialog(PPCA_ComposeWindow.this, "Error: Unable to send message.", "Error", JOptionPane.ERROR_MESSAGE);
					}

				}

				/* Close the dialog */
				if (successful)
					PPCA_ComposeWindow.this.dispose();
			}
			else if (source == cancelButton)
			{
				/* Close the dialog */
				PPCA_ComposeWindow.this.dispose();
			}
		}
	}
}
