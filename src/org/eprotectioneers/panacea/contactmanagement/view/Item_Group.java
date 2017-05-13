package org.eprotectioneers.panacea.contactmanagement.view;

import javax.swing.*;

import org.eprotectioneers.panacea.contactmanagement.components.MenuScroller;
import org.eprotectioneers.panacea.contactmanagement.models.AddContactToGroupActionListener;
import org.eprotectioneers.panacea.contactmanagement.models.Contact;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseC;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseCG;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseG;
import org.eprotectioneers.panacea.contactmanagement.models.Group;
import org.eprotectioneers.panacea.contactmanagement.models.RemoveContactFromGroupActionListener;
import org.eprotectioneers.panacea.userinterface.PPCA_ComposeWindow;
import org.eprotectioneers.panacea.userinterface.PPCA_MainPanel;
import org.eprotectioneers.panacea.userinterface.PPCA_PanaceaWindow;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.awt.*;


public class Item_Group extends Item_Object {

	private Group _g;
	private JMenuItem mntmNewEmail;
	private JMenuItem mntmConversations;
	private JMenuItem mntmDeleteGroup;
	private JMenuItem mntmShowGroup;
	private JMenu mnContacts;
	
	public Group getGroup(){
		return _g;
	}
	
	/**
	 * Constructor from Item_group assigns the following values to the Group:
	 * @param group
	 * @param fromSpecificContact , if the Item_group isn't at a SpecificContact, please enter 'null'
	 */
	public Item_Group(Group group) {
		super(group.getName(), 5);
		this._g=group;
		new Thread(new Generator()).start();
	}
	
	@Override
	public String getShownText(){
		return this.getGroup().getName();
	}
	
	@Override
	protected void generatePopup() {
		super.popupMenu = new JPopupMenu();
		
		popupMenu.add(new JLabel(_g.getName()));

		mntmShowGroup = new JMenuItem("Open Group\r\n");
		mntmShowGroup.addActionListener(new OpenGroupListener());
		super.popupMenu.add(mntmShowGroup);
		
		mntmNewEmail = new JMenuItem("New Email");
		mntmNewEmail.addActionListener(new MntmNewEmailActionListener());
		super.popupMenu.add(mntmNewEmail);
		
		mntmConversations = new JMenuItem("Conversations");
		mntmConversations.addActionListener(new MntmConversationsActionListener());
		super.popupMenu.add(mntmConversations);
		
		mnContacts = new JMenu("Contacts");
		MenuScroller.setScrollerFor(mnContacts,10);
		super.popupMenu.add(mnContacts);
		
		mnContacts.addMenuListener(new GenerateOObjectMenuListener(mnContacts.getBackground(),Color.BLACK,true,mnContacts));
				
		mntmDeleteGroup = new JMenuItem("Delete Group\r\n");
		mntmDeleteGroup.addActionListener(new MntmDeleteGroupActionListener());
		super.popupMenu.add(mntmDeleteGroup);
	}
	
	@Override
	public void generatePopupOObject(Color bg, Color fg, boolean borderpainted,JComponent mnOObject,boolean tooltipset) {
		
		ArrayList<Contact> contacts=DatabaseC.getContacts();	
		ArrayList<Contact> contactsof_g=DatabaseC.getContacts(DatabaseCG.getContacts(_g));
		contactsof_g.sort(new Contact.ContactComparator());
		ArrayList<Integer> ids=DatabaseCG.getContacts(_g);
		
		mnOObject.removeAll();
		
		String s=mnOObject.getToolTipText();
		int amount=contacts.size();
		int progress=0;

		JLabel lbl=new JLabel("Members of Group:");
		lbl.setForeground(fg);
		mnOObject.add(lbl);	
		for(Contact cont:contactsof_g){
			JMenu mnContact=new JMenu(cont.getShownname());		
			
			ImageIcon ic=new ImageIcon(cont.getPicturepath());
			ic.setImage(ic.getImage().getScaledInstance(17,17,Image.SCALE_FAST));
			mnContact.setIcon(ic);
			mnContact.setForeground(fg);
			mnContact.getPopupMenu().setBackground(bg);
			mnContact.getPopupMenu().setBorderPainted(borderpainted);
			new Thread(new AddToolTipText(mnContact, cont.toString())).start();
			
			mnContact.add(getDefaultOObjectMI(cont, bg, fg,borderpainted)[0]);
				
			if(super.isEditable()){
				JMenuItem mntmExitGroup=new JMenuItem("Remove Contact from Group");
				mntmExitGroup.setForeground(fg);
				mntmExitGroup.setBackground(bg);
				mntmExitGroup.addActionListener(new RemoveContactFromGroupActionListener(cont,_g,true,this));
				mnContact.add(mntmExitGroup);
			}
			
			mnOObject.add(mnContact);
			if(tooltipset)mnOObject.setToolTipText("Generating... "+ ++progress+"/"+amount);
		}
		if(super.isEditable()){
			lbl=new JLabel("Other Contacts:");
			lbl.setForeground(fg);
			mnOObject.add(lbl);
			contacts.sort(new Contact.ContactComparator());
			for(Contact cont:contacts){
				if(!ids.contains(cont.getId())){
					JMenu mnContact=new JMenu(cont.getShownname());
					
					ImageIcon ic=new ImageIcon(cont.getPicturepath());
					ic.setImage(ic.getImage().getScaledInstance(17,17,Image.SCALE_FAST));
					mnContact.setIcon(ic);
					mnContact.setForeground(fg);
					mnContact.getPopupMenu().setBackground(bg);
					mnContact.getPopupMenu().setBorderPainted(borderpainted);
					new Thread(new AddToolTipText(mnContact, cont.toString())).start();
					
					mnContact.add(getDefaultOObjectMI(cont, bg, fg,borderpainted)[0]);
						
						JMenuItem mntmAddToGroup=new JMenuItem("Add Contact to Group");
						mntmAddToGroup.setForeground(fg);
						mntmAddToGroup.setBackground(bg);					
						mntmAddToGroup.addActionListener(new AddContactToGroupActionListener(cont,_g,true,this));
						mnContact.add(mntmAddToGroup);
					
					mnOObject.add(mnContact);
					if(tooltipset)mnOObject.setToolTipText("Generating... "+ ++progress+"/"+amount);
				}
			}	
		}
		if(tooltipset)mnOObject.setToolTipText(s);
	}
	
	@Override
	protected JMenuItem[] getDefaultOObjectMI(Object o, Color bg, Color fg, boolean borderpainted) {
		JMenuItem mi[]=new JMenuItem[1];
		
		JMenuItem mntmShowGroup=new JMenuItem("Open Contact");
		mntmShowGroup.setForeground(fg);
		mntmShowGroup.setBackground(bg);					
		mntmShowGroup.addActionListener(new ShowContactActionListener((Contact)o));
		mi[0]=mntmShowGroup;
		
		return mi;
	}
	
	@Override
	protected void doubleClickServiceRoutine() {
		PPCA_PanaceaWindow.setCenterPanel(new Page_Group(_g));
	}

	private class OpenGroupListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			doubleClickServiceRoutine();
		}
	}
	private class MntmNewEmailActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//MZ_Sojourner_cntl
		}
	}
	private class MntmDeleteGroupActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object options[]={"yes","no"};
			switch(JOptionPane.showOptionDialog(null, "Do you really want to Delete '"+_g.getName()+"'?", "Delete Group", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1])){
				case JOptionPane.YES_OPTION:
					setCursor(new Cursor(Cursor.WAIT_CURSOR));
					DatabaseG.removeGroup(_g);
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					JOptionPane.showMessageDialog(null, "'"+_g.getName()+"' Deleted", "Deleted", JOptionPane.INFORMATION_MESSAGE, null);
					break;
				default:
					break;
			}
		}
	}
	private class MntmConversationsActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//MZ_Sojourner_cntl
		}
	}	
	
	public class ShowContactActionListener implements ActionListener{
		private Contact _c;
		public ShowContactActionListener(Contact c){
			this._c=c;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			PPCA_PanaceaWindow.setCenterPanel(new Page_Contact(_c));
		}
	}
	
	public static class ItemGroupComparator implements Comparator<Item_Group>{
		@Override
		public int compare(Item_Group ig1, Item_Group ig2) {
			return ig1.getGroup().getName().toLowerCase().compareTo(ig2.getGroup().getName().toLowerCase());
		}
	}
	
	
	
	private class Generator implements Runnable{
		@Override
		public void run() {
			ImageIcon ic=new ImageIcon(_g.getPicturepath());
			ic.setImage(ic.getImage().getScaledInstance(23,23,Image.SCALE_FAST));
			setIcon(ic);
			setToolTipText(_g.toString());
		}
	}
}
