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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.eprotectioneers.panacea.cs4235.PGPClient.email.PPCA_MailClient;
import org.eprotectioneers.panacea.cs4235.PGPClient.email.PPCA_PGPMail;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_DataRepo;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_Preferences;

import net.miginfocom.swing.MigLayout;
import javax.swing.JCheckBox;

/**
 * This class represent a dialog menu for Compose Email
 * @author eProtectioneers
 */
public class PPCA_ComposeWindow extends JFrame 
{
	private JTextField txtTo;
	private JTextField txtSubject;
	private JTextField txtFrom;
	private JTextArea txtBody;
	
	private JButton sendButton;
	private JButton cancelButton;
	private JCheckBox chckbxEncryptIt;

	private PPCA_MailClient ec;
	private PPCA_DataRepo or;
	private PPCA_Preferences pref;

	/**
	 * Create the dialog.
	 */
	public PPCA_ComposeWindow(JFrame frame) 
	{
		/* Set Properties */
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Project Panacea - Compose / [ACCOUNT]");
		Point frameLocation = frame.getLocation();
		this.setPreferredSize(new Dimension(1000, 700));
		this.setLocation((int) (frameLocation.x + frame.getWidth()/2-this.getPreferredSize().getWidth()/2),
				(int)(frameLocation.y + frame.getHeight()/2-this.getPreferredSize().getHeight()/2));

		/* Set Components */
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel panelHeader = new JPanel();
			getContentPane().add(panelHeader, BorderLayout.NORTH);
			panelHeader.setLayout(new MigLayout("", "[10%][][grow][10%]", "[30px][30px][]"));
			{
				JLabel lblTo = new JLabel("To:");
				panelHeader.add(lblTo, "cell 1 0,alignx right,growy");
			}
			{
				txtTo = new JTextField();
				txtTo.setMaximumSize(new Dimension(2147483647, 25));
				panelHeader.add(txtTo, "cell 2 0,grow");
				txtTo.setColumns(10);
			}
			{
				JLabel lblSubject = new JLabel("Subject:");
				panelHeader.add(lblSubject, "cell 1 1,alignx right,growy");
			}
			{
				txtSubject = new JTextField();
				txtSubject.setMaximumSize(new Dimension(2147483647, 25));
				panelHeader.add(txtSubject, "cell 2 1,grow");
				txtSubject.setColumns(10);
			}
			{
				JLabel lblFrom = new JLabel("From:");
				panelHeader.add(lblFrom, "cell 1 2,alignx right,growy");
			}
			{
				txtFrom = new JTextField();
				txtFrom.setMaximumSize(new Dimension(2147483647, 25));
				txtFrom.setEditable(false);
				panelHeader.add(txtFrom, "cell 2 2,grow");
				txtFrom.setColumns(10);
			}
		}
		{
			JPanel panelCompose = new JPanel();
			panelCompose.setBorder(null);
			getContentPane().add(panelCompose, BorderLayout.CENTER);
			panelCompose.setLayout(new MigLayout("", "[7.5%][grow][7.5%]", "[grow]"));
			{
				txtBody = new JTextArea();
				txtBody.setBorder(new EmptyBorder(7, 7, 7, 7));
				JScrollPane spnBody=new JScrollPane(txtBody);
				panelCompose.add(spnBody, "cell 1 0,grow");
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				chckbxEncryptIt = new JCheckBox("Encrypt it");
				buttonPane.add(chckbxEncryptIt);
			}
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
	
	/**
	 * Set a email to send to the given
	 * @param emailaddress
	 */
	public void setEmail(String emailaddress)
	{
		this.txtTo.setText(emailaddress);
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
					email.setEncrypted(chckbxEncryptIt.isSelected());
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
