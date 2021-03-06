//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.view;

import javax.swing.*;

import org.eprotectioneers.panacea.contactmanagement.components.ImagePanel;
import org.eprotectioneers.panacea.contactmanagement.components.MenuScroller;
import org.eprotectioneers.panacea.contactmanagement.models.ChooseFile;
import org.eprotectioneers.panacea.contactmanagement.models.Contact;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseC;
import org.eprotectioneers.panacea.contactmanagement.models.EmailValidator;
import org.eprotectioneers.panacea.userinterface.PPCA_PanaceaWindow;

import net.miginfocom.swing.MigLayout;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 * A Page to visualize and edit a Contact
 * @author eProtectioneers
 */
public class Page_Contact extends JPanel {

	private Contact _c;
	private ImagePanel pnl_image;
	private PageItem_new pin_groups;
	private JCheckBox chckbxInSpamFolder;
	private PageItem pi_shownname;
	private PageItem pi_emailaddress;
	private PageItem pi_address;
	private PageItem pi_lastname;
	private PageItem pi_firstname;
	private PageItem pi_phonenumber;
	private ArrayList<PageItem> contactpi=new ArrayList<PageItem>();
	private SaveContactActionListener scal=new SaveContactActionListener();	
	private JPopupMenu popupMenuGroups;
	private JButton btnDeleteContact;
	private static ImageIcon ic_delete;
	private static ImageIcon ic_delete_pressed;
	private static ImageIcon ic_delete_rollover;
	
	/**
	 * @return the Contact
	 */
	public Contact getContact() {
		return _c;
	}
	
	/**
	 * Create the panel.
	 */
	public Page_Contact(Contact contact) {
		this._c=contact;
		this.getParent();
		
		this.setBackground(Color.WHITE);
		setLayout(new MigLayout("", "[5%][100px:25%:300px][40][30%,grow,fill][40.00][30%,grow,fill][5%]", "[15.00][25px:11%:75px][25px:11%:75px][25px:11%:75px][25px:11%:75px][25px:11%:75px][5.50%][11%,grow][11%][15]"));
		
		pnl_image = new ImagePanel(_c.getPicturepath(),Contact.getDefaultpicpath());
		pnl_image.setBackground(Color.BLACK);
		add(pnl_image, "cell 1 1 1 7,grow");
		
		ChangeImageListener cil=new ChangeImageListener();
		pnl_image.getBtnChangePicture().addActionListener(cil);
		pnl_image.getMntmRemovePicture().addActionListener(cil);
		
		pi_shownname = new PageItem(_c.getShownname(), "Shown Name", this);
		contactpi.add(pi_shownname);
		
		pi_emailaddress = new PageItem(_c.getEmailaddress(), "Email", this);
		contactpi.add(pi_emailaddress);
		
		pi_firstname = new PageItem(_c.getFirstname(), "Firstname", this);
		contactpi.add(pi_firstname);
		
		pi_lastname = new PageItem(_c.getLastname(), "Lastname", this);
		contactpi.add(pi_lastname);
		
		pi_phonenumber = new PageItem(_c.getPhonenumber(), "Phonenumber", this);
		contactpi.add(pi_phonenumber);
		
		pi_address = new PageItem(_c.getAddress(),"Address",this);
		contactpi.add(pi_address);
		
		for(PageItem pi:contactpi){
			pi.setOpaque(true);
			pi.setFont(new Font("Calibri", Font.PLAIN, 16));
			pi.getBtnSave().addActionListener(scal);
		}
		
		ic_delete=new ImageIcon("images/icon_delete.png");
		ic_delete.setImage(ic_delete.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		
		ic_delete_pressed=new ImageIcon("images/icon_delete_pressed.png");
		ic_delete_pressed.setImage(ic_delete_pressed.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		
		ic_delete_rollover=new ImageIcon("images/icon_delete_rollover.png");
		ic_delete_rollover.setImage(ic_delete_rollover.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		
		btnDeleteContact = new JButton();
		btnDeleteContact.setToolTipText("Delete Contact");
		btnDeleteContact.setFocusPainted(false);
		btnDeleteContact.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnDeleteContact.setOpaque(false);
		btnDeleteContact.setContentAreaFilled(false);
		btnDeleteContact.setBorderPainted(false);
		btnDeleteContact.setMaximumSize(new Dimension(30,30));
		btnDeleteContact.setMinimumSize(new Dimension(30,30));
		btnDeleteContact.setIcon(ic_delete);
		btnDeleteContact.setPressedIcon(ic_delete_pressed);
		btnDeleteContact.setRolloverIcon(ic_delete_rollover);
		btnDeleteContact.addActionListener(new BtnDeleteContactActionListener());
		add(btnDeleteContact, "cell 5 1,alignx right,aligny top");
		
		add(pi_shownname, "cell 3 2,grow");
		add(pi_emailaddress, "cell 3 3 3 1,grow");
		add(pi_firstname, "cell 3 4,grow");
		add(pi_lastname, "cell 3 5,grow");
		add(pi_phonenumber, "cell 5 4,grow");
		add(pi_address, "cell 5 5,grow");
		
		pin_groups = new PageItem_new("",this);
		pin_groups.getTextField().addMouseListener(new Pin_groupsTextFieldMouseListener());
		pin_groups.setMaximumSize(new Dimension(100, 25));
		pin_groups.getTextField().setText("Groups");
		pin_groups.getTextField().setFocusable(false);
		pin_groups.setCursor(new Cursor(Cursor.HAND_CURSOR));
		pin_groups.getTextField().setCursor(new Cursor(Cursor.HAND_CURSOR));
		pin_groups.getTextField().setHorizontalAlignment(JTextField.CENTER);
		pin_groups.setBackground(Color.BLACK);
		pin_groups.getTextField().setForeground(Color.WHITE);
		pin_groups.setFont(new Font("Calibri", Font.PLAIN, 16));
		pin_groups.setOpaque(true);
		pin_groups.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(pin_groups, "cell 3 7");
				
		chckbxInSpamFolder = new JCheckBox("Contact is Spam",_c.isSpam());
		chckbxInSpamFolder.addActionListener(new ChckbxInSpamFolderActionListener());
		chckbxInSpamFolder.setOpaque(false);
		chckbxInSpamFolder.setFocusPainted(false);
		add(chckbxInSpamFolder, "cell 3 8");
	}	
	
	/**
	 * Add the GroupPopup
	 */
	public void addGPopup(){
		popupMenuGroups = new JPopupMenu();
		popupMenuGroups.setBackground(Color.BLACK);
		popupMenuGroups.setBorderPainted(false);
		MenuScroller.setScrollerFor(popupMenuGroups, 6);
		new Thread(new GroupPopupGenerator()).start();
	}
	
	/**
	 * @return the Contact which is currently shown
	 */
	public Contact getCurrentlyContact(){
		return new Contact(_c.getId(), pi_shownname.getText(), pi_firstname.getText(), pi_lastname.getText(), pi_emailaddress.getText(), 
				pi_phonenumber.getText(), pi_address.getText(), _c.getPicturepath(), chckbxInSpamFolder.isSelected());
	}

	/**
	 * Generator, to generate the GroupPopup
	 * @author eProtectioneers
	 */
	private class GroupPopupGenerator implements Runnable{
		private boolean generating=true;
		@Override
		public void run() {
			pin_groups.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			pin_groups.getTextField().setCursor(new Cursor(Cursor.WAIT_CURSOR));
			startToolTipSet();
			new Item_Contact(_c).generatePopupOObject(Color.WHITE,Color.BLACK,false,popupMenuGroups,true);
			generating=false;
			pin_groups.setCursor(new Cursor(Cursor.HAND_CURSOR));
			pin_groups.getTextField().setCursor(new Cursor(Cursor.HAND_CURSOR));
			popupMenuGroups.setVisible(false);
			popupMenuGroups.setVisible(true);
			popupMenuGroups.show(pin_groups.getTextField(), 3, pin_groups.getTextField().getHeight()+2);
		}

		private void startToolTipSet(){
			new Thread(new Runnable(){
				@Override
				public void run() {
					String s=pin_groups.getTextField().getToolTipText();
					while(generating){
						pin_groups.getTextField().setToolTipText(popupMenuGroups.getToolTipText());
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {}
					}
					pin_groups.getTextField().setToolTipText(s);
				}
			}).start();
		}
	}

	/**
	 * ActionListener to change the Image
	 * @author eProtectioneers
	 */
	private class ChangeImageListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String s;
			if(e.getSource().equals(pnl_image.getBtnChangePicture()))s=readFilePath();
			else s=Contact.getDefaultpicpath();
			_c.setPicturepath(s);
			pnl_image.setPicturePath(s);
			setVisible(false);
			setVisible(true);
			new SaveContactActionListener().saveContact();
		}
		private String readFilePath(){
			File file=ChooseFile.getPictoRead();
			if(file!=null&&(new ImageIcon(file.getAbsolutePath()).getImage()) instanceof Image)return file.getAbsolutePath();
			else return pnl_image.getPicturePath();
		}
	}

	/**
	 * ActionListener to save the Contact
	 * @author eProtectioneers
	 */
	private class SaveContactActionListener implements ActionListener, Runnable{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==pi_emailaddress.getBtnSave()&&!new EmailValidator().validate(pi_emailaddress.getTextField().getText())){
				pi_emailaddress.reject();
				JOptionPane.showMessageDialog(null, "Please enter a valid Email-Address","", JOptionPane.ERROR_MESSAGE);
			}else{
				Thread t=new Thread(this);
				t.start();
			}
		}
		public void saveContact(){
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			_c=getCurrentlyContact();
			//Reload _c in main ContactList
			DatabaseC.updateContact(_c);
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		@Override
		public void run() {
			try {
				Thread.sleep(5);
				while(PageItem.isSaving()){}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			saveContact();
		}
	}

	/**
	 * ActionListener to check the Contact's spam state
	 * @author eProtectioneers
	 */
	private class ChckbxInSpamFolderActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			chckbxInSpamFolder.setSelected(_c.setSpam(chckbxInSpamFolder.isSelected(), true));
			new SaveContactActionListener().saveContact();		
		}
	}

	/**
	 * MouseListener which shows the GroupPopup
	 * @author eProtectioneers
	 */
	private class Pin_groupsTextFieldMouseListener extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			addGPopup();
		}
	}

	/**
	 * ActionListener to delete the Contact
	 * @author eProtectioneers
	 */
	private class BtnDeleteContactActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object options[]={"yes", "no"};
			switch(JOptionPane.showOptionDialog(PPCA_PanaceaWindow.getFrame(), "Do you really want to Delete this Contact?", "Delete Contact", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1])){
			case JOptionPane.YES_OPTION:
				DatabaseC.removeContact(_c);
				JOptionPane.showMessageDialog(null, "Contact Deleted", "", JOptionPane.INFORMATION_MESSAGE, null);
				//EXIT PAGE
				PPCA_PanaceaWindow.setCenterPanel(PPCA_PanaceaWindow.getMainPanel());
			default:
				break;
			}
		}
	}
}
