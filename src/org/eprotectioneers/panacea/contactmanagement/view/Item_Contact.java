//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.eprotectioneers.panacea.contactmanagement.components.MenuScroller;
import org.eprotectioneers.panacea.contactmanagement.models.AddContactToGroupActionListener;
import org.eprotectioneers.panacea.contactmanagement.models.Contact;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseC;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseCG;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseG;
import org.eprotectioneers.panacea.contactmanagement.models.Group;
import org.eprotectioneers.panacea.contactmanagement.models.RemoveContactFromGroupActionListener;
import org.eprotectioneers.panacea.userinterface.PPCA_ComposeWindow;
import org.eprotectioneers.panacea.userinterface.PPCA_PanaceaWindow;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.awt.*;

/**
 * A Item, to visualize a Contact
 * @author eProtectioneers
 */
public class Item_Contact extends Item_Object {
	
	/**
	 * Enum, to decide, what to show
	 * @author eProtectioneers
	 */
	public static enum ShownText{
		 SHOWNNAME, FIRSTNAME, LASTNAME, EMAILADDRESS, PHONENUMBER, ADDRESS
	}
	
	private ShownText _st=ShownText.SHOWNNAME;
	private Contact _c;
	private JMenuItem mntmNewEmail;
	private JMenuItem mntmConversations;
	private JMenuItem mntmChat;
	private JMenuItem mntmChangeSpamState;
	private JMenuItem mntmDeleteContact;
	private JMenuItem mntmShowContact;
	private JMenu mnGroups;

	/**
	 * @return the Contact
	 */
	public Contact getContact(){
		return _c;
	}
	
	/**
	 * Set the
	 * @param showntext
	 */
	public void setShownText(ShownText showntext){
		_st=showntext;
	}
	
	/**
	 * @return the showntext, as it is
	 */
	public String getShownText(){
		switch(_st){
		case SHOWNNAME: 
			return _c.getShownname();
		case FIRSTNAME: 
			if(_c.getFirstname()!=null)return _c.getFirstname(); 
			else return _c.getShownname();
		case LASTNAME: 
			if(_c.getLastname()!=null)return _c.getLastname(); 
			else return _c.getShownname();
		case EMAILADDRESS: 
			return _c.getEmailaddress(); 
		case PHONENUMBER:
			if(_c.getPhonenumber()!=null)return _c.getPhonenumber(); 
			else return _c.getShownname();
		case ADDRESS: 
			if(_c.getAddress()!=null)return _c.getAddress(); 
			else return _c.getShownname();
		}
		return "";
	}
	
	/**
	 * Create the panel.
	 */
	public Item_Contact(Contact contact) {
		super("", 5);
		this._c=contact;
		new Thread(new Generator()).start();
	}
	
	@Override 
	protected void generatePopup(){
		popupMenu = new JPopupMenu();
		
		popupMenu.add(new JLabel(_c.getShownname()));
		
		mntmShowContact = new JMenuItem("Open Contact");
		mntmShowContact.addActionListener(new OpenContactListener());
		popupMenu.add(mntmShowContact);
		
		mntmNewEmail = new JMenuItem("New Email");
		mntmNewEmail.addActionListener(new MntmNewEmailActionListener());
		popupMenu.add(mntmNewEmail);
		
		mntmConversations = new JMenuItem("Conversations");
		mntmConversations.addActionListener(new MntmConversationsActionListener());
		popupMenu.add(mntmConversations);
		
		mntmChat = new JMenuItem("Chat");
		mntmChat.addActionListener(new MntmChatActionListener());
		popupMenu.add(mntmChat);
		
		mnGroups = new JMenu("Groups");
		MenuScroller.setScrollerFor(mnGroups,10);
		popupMenu.add(mnGroups);
				
		mnGroups.addMenuListener(new GenerateOObjectMenuListener(mnGroups.getBackground(),Color.BLACK,true,mnGroups));

		if(_c.isSpam())mntmChangeSpamState = new JMenuItem("Remove from Spam");
		else mntmChangeSpamState = new JMenuItem("Add to Spam");
		mntmChangeSpamState.addActionListener(new MntmChangeSpamStateActionListener());
		popupMenu.add(mntmChangeSpamState);
		
		mntmDeleteContact = new JMenuItem("Delete Contact");
		mntmDeleteContact.addActionListener(new MntmDeleteContactActionListener());
		popupMenu.add(mntmDeleteContact);
	}
	
	@Override
	public void generatePopupOObject(Color bg, Color fg, boolean borderpainted, JComponent mnOObject,boolean tooltipset) {
		
		ArrayList<Group> groups=DatabaseG.getGroups();
		ArrayList<Group> groupsof_c=DatabaseG.getGroups(DatabaseCG.getGroups(_c));
		groupsof_c.sort(new Group.GroupComparator());
		ArrayList<Integer> ids=DatabaseCG.getGroups(_c);

		mnOObject.removeAll();

		String s=mnOObject.getToolTipText();
		int amount=groups.size();
		int progress=0;

		JLabel lbl=new JLabel("Added to:");
		lbl.setForeground(fg);
		mnOObject.add(lbl);	
		
		for(Group g:groupsof_c){
			
			JMenu mnGroup=new JMenu(g.getName());

			ImageIcon ic=new ImageIcon(g.getPicturepath());
			ic.setImage(ic.getImage().getScaledInstance(17,17,Image.SCALE_FAST));
			mnGroup.setIcon(ic);
			mnGroup.setForeground(fg);
			mnGroup.getPopupMenu().setBackground(bg);
			mnGroup.getPopupMenu().setBorderPainted(borderpainted);
			new Thread(new AddToolTipText(mnGroup, g.toString())).start();
			
			mnGroup.add(getDefaultOObjectMI(g, bg, fg, borderpainted)[0]);
			mnGroup.add(getDefaultOObjectMI(g, bg, fg, borderpainted)[1]);
			
			if(super.isEditable()){
				JMenuItem mntmExitGroup=new JMenuItem("Remove Contact from Group");
				mntmExitGroup.setForeground(fg);
				mntmExitGroup.setBackground(bg);
				mntmExitGroup.addActionListener(new RemoveContactFromGroupActionListener(_c,g,true,this));
				mnGroup.add(mntmExitGroup);
			}	
			
			mnOObject.add(mnGroup);
			if(tooltipset)mnOObject.setToolTipText("Generating... "+ ++progress+"/"+amount);
		}
		if(super.isEditable()){
			lbl=new JLabel("Other Groups:");
			lbl.setForeground(fg);
			mnOObject.add(lbl);
			groups.sort(new Group.GroupComparator());
			
			for(Group g:groups){
				if(!ids.contains(g.getId())){
					JMenu mnGroup=new JMenu(g.getName());
					
					ImageIcon ic=new ImageIcon(g.getPicturepath());
					ic.setImage(ic.getImage().getScaledInstance(17,17,Image.SCALE_FAST));
					mnGroup.setIcon(ic);
					mnGroup.setForeground(fg);
					mnGroup.getPopupMenu().setBackground(bg);
					mnGroup.getPopupMenu().setBorderPainted(borderpainted);
					new Thread(new AddToolTipText(mnGroup, g.toString())).start();
					
					mnGroup.add(getDefaultOObjectMI(g, bg, fg, borderpainted)[0]);
					mnGroup.add(getDefaultOObjectMI(g, bg, fg, borderpainted)[1]);
						
						JMenuItem mntmAddToGroup=new JMenuItem("Add Contact to Group");
						mntmAddToGroup.setForeground(fg);
						mntmAddToGroup.setBackground(bg);					
						mntmAddToGroup.addActionListener(new AddContactToGroupActionListener(_c,g,true,this));
						mnGroup.add(mntmAddToGroup);
					
					mnOObject.add(mnGroup);
					if(tooltipset)mnOObject.setToolTipText("Generating... "+ ++progress+"/"+amount);

				}
			}
		}
		if(tooltipset)mnOObject.setToolTipText(s);
	}
	
	@Override
	public JMenuItem[] getDefaultOObjectMI(Object o, Color bg, Color fg, boolean borderpainted) {
		JMenuItem mi[]=new JMenuItem[2];
		
		JMenuItem mntmShowGroup=new JMenuItem("Open Group");
		mntmShowGroup.setForeground(fg);
		mntmShowGroup.setBackground(bg);					
		mntmShowGroup.addActionListener(new ShowGroupActionListener((Group)o));
		mi[0]=mntmShowGroup;
		
		JMenu mnGroupmembers=new JMenu("Groupmembers");
		mnGroupmembers.setForeground(fg);
		mnGroupmembers.getPopupMenu().setBackground(bg);
		mnGroupmembers.getPopupMenu().setBorderPainted(borderpainted);
		boolean b=true;
		ArrayList<Contact> contacts=DatabaseC.getContacts(DatabaseCG.getContacts((Group)o));
		contacts.sort(new Contact.ContactComparator());
		for(Contact c:contacts){
			b=false;
			JLabel lbl_c=new JLabel(c.getShownname());
			lbl_c.setBorder(new EmptyBorder(2, 5, 2, 5));
			lbl_c.setForeground(fg);
			ImageIcon ic=new ImageIcon(c.getPicturepath());
			ic.setImage(ic.getImage().getScaledInstance(17,17,Image.SCALE_FAST));
			lbl_c.setIcon(ic);
			new Thread(new AddToolTipText(lbl_c, c.toString())).start();
			mnGroupmembers.add(lbl_c);
		}
		
		if(b){
			JLabel lbl=new JLabel("No Members");
			lbl.setBorder(new EmptyBorder(2, 5, 2, 5));
			lbl.setForeground(fg);
			mnGroupmembers.add(lbl);
		}
		
		MenuScroller.setScrollerFor(mnGroupmembers,6);
		
		mi[1]=mnGroupmembers;
		
		return mi;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		super.setText(getShownText());
	}
	
	@Override
	protected void doubleClickServiceRoutine() {
		PPCA_PanaceaWindow.setCenterPanel(new Page_Contact(_c));
	}

	/**
	 * @author eProtectioneers
	 */
	private class OpenContactListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			doubleClickServiceRoutine();
		}
	}

	/**
	 * ActionListener to delete this Contact
	 * @author eProtectioneers
	 */
	private class MntmDeleteContactActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object options[]={"yes","no"};
			switch(JOptionPane.showOptionDialog(PPCA_PanaceaWindow.getFrame(), "Do you really want to Delete '"+_c.getShownname()+"'?", "Delete Contact", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1])){
				case JOptionPane.YES_OPTION:
					setCursor(new Cursor(Cursor.WAIT_CURSOR));
					//Remove Contact from LIST
					DatabaseC.removeContact(_c);
					//Abfrage, ob Panel Kontaktseite entspricht 
					//ContactItem entfernen
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					JOptionPane.showMessageDialog(PPCA_PanaceaWindow.getFrame(), "'"+_c.getShownname()+"' Deleted", "Deleted", JOptionPane.INFORMATION_MESSAGE, null);
					break;
				default:
					break;
			}
		}
	}

	/**
	 * ActionListener to view the chat with this Contact
	 * @author eProtectioneers
	 */
	private class MntmChatActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//MZ_Sojourner_cntl
		}
	}
	
	/**
	 * ActionListener to Write a new Email to the Contact
	 * @author eProtectioneers
	 */
	private class MntmNewEmailActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			PPCA_ComposeWindow cd = new PPCA_ComposeWindow(PPCA_PanaceaWindow.getFrame());
			cd.setEmail(_c.getEmailaddress());
		}
	}
	
	/**
	 * ActionListener to view the recent Emails between you and this Contact
	 * @author eProtectioneers
	 */
	private class MntmConversationsActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//MZ_Sojourner_cntl
		}
	}

	/**
	 * ActionListener to change the spam state of this Contact
	 * @author eProtectioneers
	 */
	private class MntmChangeSpamStateActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			_c.setSpam(!_c.isSpam(), true);
			setToolTipText(_c.toString());
		}
	}	

	/**
	 * ActionListener to show this Contact
	 * @author eProtectioneers
	 */
	public class ShowGroupActionListener implements ActionListener{
		private Group _g;
		public ShowGroupActionListener(Group g){
			this._g=g;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			PPCA_PanaceaWindow.setCenterPanel(new Page_Group(_g));
		}
	}

	/**
	 * Comparator, which compares the ShownText of the ICs
	 * @author eProtectioneers
	 */
	public static class ItemContactComparator implements Comparator<Item_Contact>{
		@Override
		public int compare(Item_Contact ic1, Item_Contact ic2) {
			return ic1.getShownText().toLowerCase().compareTo(ic2.getShownText().toLowerCase());
		}
	}


	/**
	 * Generator, to generate the Picture and the ToolTipText
	 * @author eProtectioneers
	 */
	private class Generator implements Runnable{
		@Override
		public void run() {
			ImageIcon ic=new ImageIcon(_c.getPicturepath());
			ic.setImage(ic.getImage().getScaledInstance(23,23,Image.SCALE_FAST));
			setIcon(ic);
			setToolTipText(_c.toString());
		}
	}
}
