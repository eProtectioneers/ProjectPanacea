package org.eprotectioneers.panacea.userinterface;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import org.eprotectioneers.panacea.cs4235.PGPClient.cryptex.EncryptionServiceEngine;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_DataRepo;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_FileEngine;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_Preferences;
import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_RemoteClientKey;

import net.miginfocom.swing.MigLayout;

/**
 * This class represents a Dialog for Crypto Setting
 * @author eProtectioneers
 */
public class PPCA_EncryptionConfigWindow extends JDialog 
{
	private JTabbedPane tabbedPane;
	private JPanel panelGeneral;
	private JPanel panelKeyManagement;
	private JPanel panelKeyStore;
	private JPanel panelUserKey;

	private JTextField txtKeydirectory;
	private JTextField txtPrivateKey;
	private JTextArea txtRSAPublicKey;
	private JTextArea txtRSAPrivateKey;
	private JTextField txtFingerprint;
	private JPasswordField pwdPassphrase;
	private JPasswordField pwdPassphraseConfirm;
	private JTextField txtUsername;
	private JTextField txtUserkey;
	private JTable tblUserKey;

	private JButton btnPrivateKeyBrowse;
	private JButton btnKeyDirectoryBrowse;
	private JButton btnGenerateRsaKey;
	private JButton btnUserKeyBrowse;
	private JButton btnUserKeyAdd;
	private JButton btnApply;
	private JButton okButton;
	private JButton cancelButton;

	private EncryptionServiceEngine ce;
	private PPCA_DataRepo or;
	private PPCA_Preferences pref;

	/**
	 * Create the dialog.
	 */
	public PPCA_EncryptionConfigWindow(JFrame frame)
	{
		/* Set Properties */
		setResizable(false);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Project Panacea - Privacy Configuration [ACCOUNT]");
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
				panelGeneral = new JPanel();
				tabbedPane.addTab("General", null, panelGeneral, null);
				panelGeneral.setLayout(new MigLayout("", "[190px][350px][50px]", "[30px][30px][600px]"));
				{
					JLabel lblKeyDirectory = new JLabel("Key Directory:");
					lblKeyDirectory.setHorizontalAlignment(SwingConstants.RIGHT);
					panelGeneral.add(lblKeyDirectory, "flowy,cell 0 0,grow");
				}
				{
					txtKeydirectory = new JTextField();
					panelGeneral.add(txtKeydirectory, "flowx,cell 1 0,grow");
					txtKeydirectory.setColumns(10);
				}
				{
					btnKeyDirectoryBrowse = new JButton("Browse...");
					panelGeneral.add(btnKeyDirectoryBrowse, "cell 2 0,grow");
				}
				{
					JLabel lblPrivateKey = new JLabel("Private Key:");
					lblPrivateKey.setHorizontalAlignment(SwingConstants.RIGHT);
					panelGeneral.add(lblPrivateKey, "cell 0 1,grow");
				}
				{
					txtPrivateKey = new JTextField();
					panelGeneral.add(txtPrivateKey, "cell 1 1,grow");
					txtPrivateKey.setColumns(10);
				}
				{
					btnPrivateKeyBrowse = new JButton("Browse...");
					panelGeneral.add(btnPrivateKeyBrowse, "flowx,cell 2 1,grow");
				}
			}
			{
				panelKeyManagement = new JPanel();
				tabbedPane.addTab("Key Management", null, panelKeyManagement, null);
				panelKeyManagement.setLayout(new MigLayout("", "[800px,grow]", "[29px][16px][280px][16px][200px][120px]"));
				{
					btnGenerateRsaKey = new JButton("Generate RSA Key");
					panelKeyManagement.add(btnGenerateRsaKey, "cell 0 0,alignx center,aligny center");
				}
				{
					JLabel lblPrivateKey_1 = new JLabel("Private Key:");
					panelKeyManagement.add(lblPrivateKey_1, "cell 0 1");
				}
				{
					txtRSAPrivateKey = new JTextArea();
					txtRSAPrivateKey.setRows(16);
					txtRSAPrivateKey.setLineWrap(true);
					txtRSAPrivateKey.setEditable(false);
					panelKeyManagement.add(txtRSAPrivateKey, "cell 0 2,grow");
				}
				{
					JLabel lblPublicKey = new JLabel("Public Key:");
					lblPublicKey.setHorizontalAlignment(SwingConstants.LEFT);
					panelKeyManagement.add(lblPublicKey, "cell 0 3,alignx left,aligny center");
				}
				{
					txtRSAPublicKey = new JTextArea();
					txtRSAPublicKey.setEditable(false);
					txtRSAPublicKey.setLineWrap(true);
					txtRSAPublicKey.setRows(5);
					txtRSAPublicKey.setColumns(80);
					panelKeyManagement.add(txtRSAPublicKey, "cell 0 4,alignx center,aligny top");
				}
				{
					JPanel panelKeyManagementPassphrase = new JPanel();
					panelKeyManagement.add(panelKeyManagementPassphrase, "cell 0 5,alignx center,aligny center");
					panelKeyManagementPassphrase.setLayout(new MigLayout("", "[20px][387px]", "[28px][328px][28px]"));
					{
						JLabel lblFingerprint = new JLabel("Fingerprint:");
						panelKeyManagementPassphrase.add(lblFingerprint, "cell 0 0,grow");
						lblFingerprint.setHorizontalAlignment(SwingConstants.RIGHT);
					}
					{
						txtFingerprint = new JTextField();
						txtFingerprint.setText("Fingerprint");
						panelKeyManagementPassphrase.add(txtFingerprint, "cell 1 0,grow");
						txtFingerprint.setColumns(100);
					}
					{
						JLabel lblPassphrase = new JLabel("Passphrase:");
						lblPassphrase.setHorizontalAlignment(SwingConstants.RIGHT);
						panelKeyManagementPassphrase.add(lblPassphrase, "cell 0 1,grow");
					}
					{
						pwdPassphrase = new JPasswordField();
						pwdPassphrase.setColumns(32);
						panelKeyManagementPassphrase.add(pwdPassphrase, "flowx,cell 1 1");
					}
					{
						JLabel lblPassphraseConfirm = new JLabel("Passphrase Confirm:");
						lblPassphraseConfirm.setHorizontalAlignment(SwingConstants.RIGHT);
						panelKeyManagementPassphrase.add(lblPassphraseConfirm, "cell 0 2,grow");
					}
					{
						pwdPassphraseConfirm = new JPasswordField();
						pwdPassphraseConfirm.setColumns(32);
						panelKeyManagementPassphrase.add(pwdPassphraseConfirm, "cell 1 2");
					}
				}
			}
			{
				panelKeyStore = new JPanel();
				tabbedPane.addTab("Key Store", null, panelKeyStore, null);
				panelKeyStore.setLayout(new BorderLayout(0, 0));
				{
					panelUserKey = new JPanel();
					panelKeyStore.add(panelUserKey, BorderLayout.NORTH);
					panelUserKey.setLayout(new MigLayout("", "[190px][350px][50px]", "[0px]"));
					{
						JLabel lblUsername = new JLabel("Username:");
						panelUserKey.add(lblUsername, "cell 0 0,alignx right,growy");
					}
					{
						txtUsername = new JTextField();
						panelUserKey.add(txtUsername, "cell 1 0,grow");
						txtUsername.setColumns(10);
					}
					{
						JLabel lblUserkey = new JLabel("Public Key:");
						panelUserKey.add(lblUserkey, "cell 0 1,alignx right,growy");
					}
					{
						txtUserkey = new JTextField();
						panelUserKey.add(txtUserkey, "cell 1 1,grow");
						txtUsername.setColumns(10);
					}
					{
						btnUserKeyBrowse = new JButton("Browse...");
						panelUserKey.add(btnUserKeyBrowse, "cell 2 1,grow");
					}
					{
						btnUserKeyAdd = new JButton("Add...");
						panelUserKey.add(btnUserKeyAdd, "cell 3 1,grow");
					}
				}
				{
					tblUserKey = new JTable();
					JScrollPane scrollKeyStore = new JScrollPane(tblUserKey);
					tblUserKey.setFillsViewportHeight(true);
					panelKeyStore.add(scrollKeyStore, BorderLayout.CENTER);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnApply = new JButton("Apply");
				buttonPane.add(btnApply);
			}
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

		/* Set event handler */
		ButtonListener blistener = new ButtonListener();
		btnPrivateKeyBrowse.addActionListener(blistener);
		btnKeyDirectoryBrowse.addActionListener(blistener);
		btnGenerateRsaKey.addActionListener(blistener);
		btnUserKeyBrowse.addActionListener(blistener);
		btnUserKeyAdd.addActionListener(blistener);
		btnApply.addActionListener(blistener);
		okButton.addActionListener(blistener);
		cancelButton.addActionListener(blistener);

		/* Preloading Event */
		ce = EncryptionServiceEngine.getInstance();
		or = PPCA_DataRepo.getInstance();
		pref = or.getPreferences();

		txtKeydirectory.setText(pref.getKeyDirectory());
		txtPrivateKey.setText(pref.getPrivateKeyFilePath());

		updateTable();

		/* Set visibility */
		pack();
		setVisible(true);
	}

	/* Keep it in sync with updateTable in ButtonListener and ButtonEditor */
	private void updateTable()
	{
		/* Populate data for the table */
		ArrayList<PPCA_RemoteClientKey> list = (ArrayList<PPCA_RemoteClientKey>) or.getKeyStore();
		String[] columnName = {"Username", "Public Key Fingerprint (MD5)"};
		String[][] keystore = new String[list.size()][columnName.length];

		for (int i = 0; i < list.size(); i++)
		{
			keystore[i][0] = list.get(i).getUsername();

			/* Construct Public Key */
			String publickeyraw = list.get(i).getKey();
			RSAPublicKey publickey = ce.parsePublicKey(publickeyraw);
			keystore[i][1] = ce.fingerprint(publickey);
		}

		/* Setup table model */
		DefaultTableModel model = new DefaultTableModel()
		{
			@Override
			public boolean isCellEditable(int row, int column) 
			{
				return false;
			}    
		};
		model.setDataVector(keystore, columnName);
		tblUserKey.setModel(model);
		tblUserKey.getColumn("Username").setMaxWidth(250);
		tblUserKey.getColumn("Username").setMinWidth(250);
	}

	private class ButtonListener implements ActionListener
	{
		private void updateTable(String username, String userPublicKey)
		{
			DefaultTableModel model = (DefaultTableModel) tblUserKey.getModel();
			String[] rowEntry = new String[2];

			/* Construct Public Key */
			RSAPublicKey publickey = ce.parsePublicKey(userPublicKey);
			if (publickey == null)
			{
				JOptionPane.showMessageDialog(PPCA_EncryptionConfigWindow.this, "Error: Unable to parse key!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			String fingerprint = ce.fingerprint(publickey);

			rowEntry[0] = username;
			rowEntry[1] = fingerprint;
			model.addRow(rowEntry);
		}

		private void saveGeneral(String keyDirectory, String privateKey)
		{
			pref.setKeyDirectory(keyDirectory);
			pref.setPrivateKeyFilePath(privateKey);
			pref.setPublicKeyFilePath(privateKey + ".pub");
		}

		private void saveKeyManagement(String privateKey, String publicKey, String passphrase)
		{
			/* Write both private key and public key to file */
			String filepath = pref.getKeyDirectory() + File.separator + pref.getPrivateKeyFilePath();
			boolean successful = PPCA_FileEngine.write(filepath, privateKey);
			if (!successful)
			{
				JOptionPane.showMessageDialog(PPCA_EncryptionConfigWindow.this, "Error: Unable to write Private Key to a file!", "Error", JOptionPane.ERROR_MESSAGE);
			}

			filepath = pref.getKeyDirectory() + File.separator + pref.getPublicKeyFilePath();
			successful = PPCA_FileEngine.write(filepath, publicKey);
			if (!successful)
			{
				JOptionPane.showMessageDialog(PPCA_EncryptionConfigWindow.this, "Error: Unable to write Public Key to a file!", "Error", JOptionPane.ERROR_MESSAGE);
			}

			/* Save the passphrase in user's preferences */
			pref.setPassphrase(passphrase);
		}

		private void saveKeyStore(String username, String publicKey)
		{
			or.savePublicKey(username, publicKey);
		}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			Object source = e.getSource();

			if (source == btnKeyDirectoryBrowse)
			{
				JFileChooser browser = new JFileChooser();
				browser.setDialogTitle("Key Directory");
				browser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (PPCA_FileEngine.isFileOrDirectoryExist(txtKeydirectory.getText()))
				{
					browser.setCurrentDirectory(new File (txtKeydirectory.getText()));
				}
				int returnVal = browser.showOpenDialog(PPCA_EncryptionConfigWindow.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) 
				{
					txtKeydirectory.setText(browser.getSelectedFile().getPath());
				} 
			}
			else if (source == btnPrivateKeyBrowse)
			{
				JFileChooser browser = new JFileChooser();
				browser.setDialogTitle("Private Key");
				browser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (PPCA_FileEngine.isFileOrDirectoryExist(txtKeydirectory.getText()))
				{
					browser.setCurrentDirectory(new File (txtKeydirectory.getText()));
				}
				int returnVal = browser.showOpenDialog(PPCA_EncryptionConfigWindow.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) 
				{
					txtPrivateKey.setText(browser.getSelectedFile().getName());
				} 
			}
			else if (source == btnGenerateRsaKey)
			{
				RSAKey[] keys = ce.generateRSAKeys();
				txtRSAPrivateKey.setText(ce.getEncoded(((RSAPrivateKey)keys[0])));
				txtRSAPublicKey.setText(ce.getEncoded(((RSAPublicKey)keys[1])));
				txtFingerprint.setText(ce.fingerprint(((RSAPublicKey)keys[1])));
			}
			else if (source == btnUserKeyBrowse)
			{
				JFileChooser browser = new JFileChooser();
				browser.setDialogTitle("Key Directory");
				browser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (PPCA_FileEngine.isFileOrDirectoryExist(txtKeydirectory.getText()))
				{
					browser.setCurrentDirectory(new File (txtKeydirectory.getText()));
				}
				int returnVal = browser.showOpenDialog(PPCA_EncryptionConfigWindow.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) 
				{
					txtUserkey.setText(browser.getSelectedFile().getPath());
				} 
			}
			else if (source == btnUserKeyAdd)
			{
				String username = txtUsername.getText();
				String userPublicKey = PPCA_FileEngine.read(txtUserkey.getText());
				if (userPublicKey == null)
				{
					JOptionPane.showMessageDialog(PPCA_EncryptionConfigWindow.this, "Error: Unable to read the file!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					saveKeyStore(username, userPublicKey);
					updateTable(username, userPublicKey);

					/* Clear textbox */
					txtUsername.setText("");
					txtUserkey.setText("");
				}
			}
			else if (source == btnApply)
			{
				Component component = tabbedPane.getSelectedComponent();
				if (component == panelGeneral)
				{
					String keyDirectory = txtKeydirectory.getText();
					String privateKey = txtPrivateKey.getText();
					saveGeneral(keyDirectory, privateKey);
				}
				else if (component == panelKeyManagement)
				{
					String passphrase = new String (pwdPassphrase.getPassword());
					String passphraseConfirm = new String (pwdPassphraseConfirm.getPassword());
					if (!passphrase.equals(passphraseConfirm))
					{
						JOptionPane.showMessageDialog(PPCA_EncryptionConfigWindow.this, "Error: Passphrases do not match!", "Error", JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						if (!passphrase.isEmpty())
						{
							String privateKey = txtRSAPrivateKey.getText();
							String publicKey = txtRSAPublicKey.getText();
							saveKeyManagement(privateKey, publicKey, passphrase);
						}
					}
				}
				else if (component == panelKeyStore)
				{
					/* nop */
				}
			}
			else if (source == okButton)
			{
				/* Write into database */
				or.savePreferences(pref);

				/* Close the dialog */
				PPCA_EncryptionConfigWindow.this.dispose();
			}
			else if (source == cancelButton)
			{
				/* Close the dialog */
				PPCA_EncryptionConfigWindow.this.dispose();
			}
		}	
	}	
}
