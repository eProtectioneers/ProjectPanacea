//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.view;

import javax.swing.*;
import java.awt.*;
import net.miginfocom.swing.MigLayout;

import org.eprotectioneers.panacea.contactmanagement.models.*;
import javax.swing.border.*;

import org.eprotectioneers.panacea.contactmanagement.components.LetterCircle;
import org.eprotectioneers.panacea.contactmanagement.components.MenuScroller;
import org.eprotectioneers.panacea.contactmanagement.components.SearchField;
import org.eprotectioneers.panacea.contactmanagement.models.Contact;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseC;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseG;
import org.eprotectioneers.panacea.contactmanagement.models.Group;
import org.eprotectioneers.panacea.contactmanagement.view.Item_Contact.ShownText;
import org.eprotectioneers.panacea.userinterface.PPCA_PanaceaWindow;

import java.awt.event.*;
import java.util.*;

/**
 * A Bar, to show the added Contacts and Groups
 * @author eProtectioneers
 */
public class Contactbar extends JPanel {
	
	/**
	 * A enum, which contains the options, to get shown
	 * @author eProtectioneers
	 */
	public enum Selected{
		CONTACT_SHOWNNAME, CONTACT_EMAILADDRESS, CONTACT_FIRSTNAME, CONTACT_LASTNAME, CONTACT_ADDRESS, CONTACT_PHONENUMBER,
		GROUP
	}
	
	private static Selected selected=Selected.CONTACT_SHOWNNAME;
	private static JPanel pnl_scrollpane;
	private static JScrollPane scrollPane;
	private static SearchField searchField;
	private static JButton btn_add;
	private static JPopupMenu ppmn_add;
	private static JMenuItem mntm_addContact;
	private static JMenuItem mntm_addGroup;
	private static JPanel pnl_items;
	private static JPopupMenu ppmn_ic_selected;
	private static JPopupMenu ppmn_ig_selected;
	private static ArrayList<Item_Contact> _ics=new ArrayList<Item_Contact>();
	private static ArrayList<Item_Contact> _ics_visible=new ArrayList<Item_Contact>();
	private static ArrayList<Item_Group> _igs=new ArrayList<Item_Group>();
	private static ArrayList<Item_Group> _igs_visible=new ArrayList<Item_Group>();
	private static JButton btnRefresh;
	private static ImageIcon ic_refresh;
	private static ImageIcon ic_refresh_pressed;
	private static ImageIcon ic_refresh_rollover;
	private static ImageIcon ic_add;
	private static ImageIcon ic_add_pressed;
	private static ImageIcon ic_add_rollover;
	private static JComboBox<Selected> cbox_type;
	private static boolean letter1_used;
	private static char[] letter;
	private Object options[]={"yes", "no"};
	private ScrollBarVisibilityMouseListener sbvml=new ScrollBarVisibilityMouseListener();

	/**
	 * @return the selected parameter, to show
	 */
	public Selected getSelected(){
		return selected;
	}
	
	/**
	 * @return the ItemContacts
	 */
	public ArrayList<Item_Contact> getICs(){
		return _ics;
	}
	
	/**
	 * @return the visible ItemContacts
	 */
	public ArrayList<Item_Contact> getICs_visible(){
		return _ics_visible;
	}
	
	/**
	 * @return the ItemGroups
	 */
	public ArrayList<Item_Group> getIGs(){
		return _igs;
	}
	
	/**
	 * @return the visible ItemGroups
	 */
	public ArrayList<Item_Group> getIGs_visible(){
		return _igs_visible;
	}
	
	/**
	 * Create the panel.
	 */
	public Contactbar() {
		initialize();
	}
	
	/**
	 * Inizializes
	 */
	private void initialize() {
		setPreferredSize(new Dimension(300, 400));
		setMaximumSize(new Dimension(350, 32767));
		setBorder(new LineBorder(new Color(0, 0, 0), 2, true));
		setBackground(new Color(9,29,62));
		setLayout(new MigLayout("", "[25:25:25][90%,grow][25:25:25]", "[37:37.00:37][grow]"));
		
		ic_add=new ImageIcon("images/icon_plus_white.png");
		ic_add.setImage(ic_add.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		
		ic_add_pressed=new ImageIcon("images/icon_plus_black.png");
		ic_add_pressed.setImage(ic_add_pressed.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		
		ic_add_rollover=new ImageIcon("images/icon_plus_green.png");
		ic_add_rollover.setImage(ic_add_rollover.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		
		btn_add = new JButton();
		btn_add.setToolTipText("Add");
		btn_add.setFocusPainted(false);
		btn_add.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn_add.setOpaque(false);
		btn_add.setContentAreaFilled(false);
		btn_add.setBorderPainted(false);
		btn_add.setMaximumSize(new Dimension(25,25));
		btn_add.setMinimumSize(new Dimension(25,25));
		btn_add.setIcon(ic_add);
		btn_add.setPressedIcon(ic_add_pressed);
		btn_add.setRolloverIcon(ic_add_rollover);
		btn_add.addActionListener(new Btn_addActionListener());
		add(btn_add, "cell 0 0");
		
		generatePopupAdd();
		
		searchField = new SearchField(this);
		add(searchField, "cell 1 0,grow");
		
		ic_refresh=new ImageIcon("images/icon_refresh.png");
		ic_refresh.setImage(ic_refresh.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		
		ic_refresh_pressed=new ImageIcon("images/icon_refresh_pressed.png");
		ic_refresh_pressed.setImage(ic_refresh_pressed.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		
		ic_refresh_rollover=new ImageIcon("images/icon_refresh_rollover.png");
		ic_refresh_rollover.setImage(ic_refresh_rollover.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		
		btnRefresh = new JButton();
		btnRefresh.setToolTipText("Refresh");
		btnRefresh.setFocusPainted(false);
		btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnRefresh.setOpaque(false);
		btnRefresh.setContentAreaFilled(false);
		btnRefresh.setBorderPainted(false);
		btnRefresh.setMaximumSize(new Dimension(25,25));
		btnRefresh.setMinimumSize(new Dimension(25,25));
		btnRefresh.setIcon(ic_refresh);
		btnRefresh.setPressedIcon(ic_refresh_pressed);
		btnRefresh.setRolloverIcon(ic_refresh_rollover);
		btnRefresh.addActionListener(new BtnRefreshActionListener());
		add(btnRefresh, "cell 2 0");
		
		pnl_scrollpane=new JPanel();
		pnl_scrollpane.setLayout(new BorderLayout());
		pnl_scrollpane.setOpaque(false);
		add(pnl_scrollpane, "cell 0 1 3 1,grow");

		pnl_items = new JPanel();
		pnl_items.setOpaque(false);
		
		scrollPane = new JScrollPane(pnl_items);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		changeScrollBarPolicy(false);
		pnl_scrollpane.add(scrollPane, BorderLayout.CENTER);
		
		cbox_type = new JComboBox<Selected>();
		cbox_type.setToolTipText("Filter");
		cbox_type.addItem(Selected.GROUP);
		cbox_type.addItem(Selected.CONTACT_SHOWNNAME);
		cbox_type.addItem(Selected.CONTACT_EMAILADDRESS);
		cbox_type.addItem(Selected.CONTACT_FIRSTNAME);
		cbox_type.addItem(Selected.CONTACT_LASTNAME);
		cbox_type.addItem(Selected.CONTACT_PHONENUMBER);
		cbox_type.addItem(Selected.CONTACT_ADDRESS);
		cbox_type.addActionListener(new CBoxTypeActionListener());
		cbox_type.setSelectedItem(Selected.CONTACT_SHOWNNAME);
		pnl_scrollpane.add(cbox_type, BorderLayout.NORTH);
		
		addScrollBarVisibilityMouseListener();
		
		new Thread(new ItemGenerator()).start();	
	}
	
	/**
	 * Adds the Scrollbarvisibility MouseListener to every Component, the bar contains
	 */
	private void addScrollBarVisibilityMouseListener(){
		addMouseListener(sbvml);
		for(int i=0;i<this.getComponentCount();i++){
			this.getComponent(i).addMouseListener(sbvml);
		}
		searchField.getComponent(0).addMouseListener(sbvml);
		scrollPane.addMouseListener(sbvml);
		scrollPane.getHorizontalScrollBar().addMouseListener(sbvml);
		for(int i=0;i<scrollPane.getHorizontalScrollBar().getComponentCount();i++){
			scrollPane.getHorizontalScrollBar().getComponent(i).addMouseListener(sbvml);
		}
		scrollPane.getVerticalScrollBar().addMouseListener(sbvml);
		for(int i=0;i<scrollPane.getVerticalScrollBar().getComponentCount();i++){
			scrollPane.getVerticalScrollBar().getComponent(i).addMouseListener(sbvml);
		}
		cbox_type.addMouseListener(sbvml);
		for(int i=0;i<cbox_type.getComponentCount();i++){
			cbox_type.getComponent(i).addMouseListener(sbvml);
		}
	}
	
	/**
	 * Changes, if
	 * @param scrollbarvisible
	 */
	private void changeScrollBarPolicy(boolean scrollbarvisible){
		if(scrollbarvisible){
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}else{
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		}
	}
	
	/**
	 * Generate the popup, to add a new Contact/Group
	 */
	private void generatePopupAdd(){
		MntmAddActionListener mntmaal=new MntmAddActionListener();

		ppmn_add=new JPopupMenu();	
		
		ImageIcon ic_contact=new ImageIcon(Contact.getDefaultpicpath());
		ic_contact.setImage(ic_contact.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		
		mntm_addContact=new JMenuItem("New Contact");
		mntm_addContact.setIcon(ic_contact);
		mntm_addContact.addActionListener(mntmaal);
		ppmn_add.add(mntm_addContact);
		
		ImageIcon ic_group=new ImageIcon(Group.getDefaultpicpath());
		ic_group.setImage(ic_group.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		
		mntm_addGroup=new JMenuItem("New Group");
		mntm_addGroup.setIcon(ic_group);
		mntm_addGroup.addActionListener(mntmaal);
		ppmn_add.add(mntm_addGroup);
	}
	
	/**
	 * Generate the Items
	 */
	private void generateItems(){
		new Thread(new SelectedPopUpGenerator_Contact()).start();
		new Thread(new SelectedPopUpGenerator_Group()).start();;

		_ics=new ArrayList<Item_Contact>();
		_ics_visible=new ArrayList<Item_Contact>();
		_igs=new ArrayList<Item_Group>();
		_igs_visible=new ArrayList<Item_Group>();

		if(selected==Selected.GROUP){
			generateIGs();
			generateICs();
		}else{
			generateICs();
			generateIGs();
		}
		addItems();
	}
	
	/**
	 * Generate the ItemContacts
	 */
	private void generateICs(){
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		ArrayList<Contact> cs=DatabaseC.getContacts();
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		for(Contact c:cs){
			Item_Contact ic=new Item_Contact(c);
			generateItem(ic);
		}
	}
	
	/**
	 * Generate the ItemGroups
	 */
	private void generateIGs(){
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		ArrayList<Group> gs=DatabaseG.getGroups();
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		for(Group g:gs){
			Item_Group ig=new Item_Group(g);
			generateItem(ig);
		}
	}

	/**
	 * Generate a new Item
	 * @param io
	 */
	private void generateItem(Item_Object io){
		if(io instanceof Item_Contact){
			Item_Contact ic=(Item_Contact)io;
			ic.setBackground(Color.gray);
			ic.setForeground(Color.WHITE);
			ic.setSelectedPopup(ppmn_ic_selected);
			_ics.add(ic);
			_ics_visible.add(ic);
		}
		if(io instanceof Item_Group){
			Item_Group ig=(Item_Group)io;
			ig.setBackground(Color.gray);
			ig.setForeground(Color.WHITE);
			ig.setSelectedPopup(ppmn_ig_selected);
			_igs.add(ig);
			_igs_visible.add(ig);
		}
		io.addMouseListener(sbvml);
	}
	
	/**
	 * Add the Visible, selected Item to the bar
	 */
	public void addItems(){
		letter=new char[40];
		letter1_used=false;
		
		ShownText st=getShownText();
		
		String s="[]";
		pnl_items.removeAll();
		if(selected==Selected.GROUP){
			for(int i=0;i<_igs_visible.size();i++)s+="[]";
			pnl_items.setLayout(new MigLayout("", "[][grow]", s));
			_igs_visible.sort(new Item_Group.ItemGroupComparator());
			
			int intc=0;
			int i=0;
			for(Item_Group  ig:_igs_visible){
				if(!ig.getGroup().getName().equals("")){
					char c=Character.toUpperCase(ig.getGroup().getName().toCharArray()[0]);
					if(c<'A'&&!letter1_used){
						pnl_items.add(new LetterCircle('#', 22, Color.ORANGE, new Color(20,20,20), true),"cell 0 "+i);
						letter1_used=true;
					}
					if(c>letter[intc]&&c>='A'){
						letter[++intc]=c;
						pnl_items.add(new LetterCircle(letter[intc], 22, Color.ORANGE, new Color(20,20,20), true),"cell 0 "+i);
					}
				}
				
				pnl_items.add(ig,"cell 1 "+i+",grow");
				i++;
			}
			
			if(_igs_visible.size()==0){
				JLabel lbl_noGroup=new JLabel("No Groups");
				lbl_noGroup.setForeground(Color.WHITE);
				lbl_noGroup.setFont(new Font("Calibri",Font.ITALIC,16));
				pnl_items.add(lbl_noGroup);
			}
			
		}else{
			for(int i=0;i<_ics_visible.size();i++)s+="[]";
			pnl_items.setLayout(new MigLayout("", "[][grow]", s));
			for(Item_Contact ic:_ics_visible){
				ic.setShownText(st);
			}
			_ics_visible.sort(new Item_Contact.ItemContactComparator());
			
			int intc=0;
			int i=0;
			for(Item_Contact ic:_ics_visible){
				if(!ic.getShownText().equals("")){
					char c=Character.toUpperCase(ic.getShownText().toCharArray()[0]);
					if(c<'A'&&!letter1_used){
						pnl_items.add(new LetterCircle('#', 22, Color.ORANGE, new Color(20,20,20), true),"cell 0 "+i);
						letter1_used=true;
					}
					if(c>letter[intc]&&c>='A'){
						letter[++intc]=c;
						pnl_items.add(new LetterCircle(letter[intc], 22, Color.ORANGE, new Color(20,20,20), true),"cell 0 "+i);
					}
				}
				
				pnl_items.add(ic,"cell 1 "+i+",grow");
				i++;
			}
			
			if(_ics_visible.size()==0){
				JLabel lbl_noContact=new JLabel("No Contacts");
				lbl_noContact.setForeground(Color.WHITE);
				lbl_noContact.setFont(new Font("Calibri",Font.ITALIC,16));
				pnl_items.add(lbl_noContact);
			}
		}
		boolean b=searchField.isFocusOwner();
		setVisible(false);
		setVisible(true);
		if(b){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
			searchField.requestFocus();
		}
	}
	
	/**
	 * @return the selected ShownText of ItemContacts
	 */
	private static ShownText getShownText(){
		ShownText st=ShownText.SHOWNNAME;
		switch(selected){
			case CONTACT_ADDRESS:
				st=ShownText.ADDRESS;
				break;
			case CONTACT_EMAILADDRESS:
				st=ShownText.EMAILADDRESS;
				break;
			case CONTACT_FIRSTNAME:
				st=ShownText.FIRSTNAME;
				break;
			case CONTACT_LASTNAME:
				st=ShownText.LASTNAME;
				break;
			case CONTACT_PHONENUMBER:
				st=ShownText.PHONENUMBER;
				break;
			case CONTACT_SHOWNNAME:
				st=ShownText.SHOWNNAME;
				break;
			default:
				break;
		}
		return st;
	}
	
	/**
	 * Generate the PuMn for the selected ICs
	 */
	private void generatePopupMenuSelected_Contact(){
		ppmn_ic_selected=new JPopupMenu();
		
		ppmn_ic_selected.add(new JLabel("Selected Contacts:"));

		ArrayList<Group> gs=DatabaseG.getGroups();
		gs.sort(new Group.GroupComparator());
		
		JMenuItem mntm_newEmail=new JMenuItem("New Email");
		mntm_newEmail.addActionListener(new NewEmailToSelectedContactsActionlistener());
		ppmn_ic_selected.add(mntm_newEmail);
		
		JMenu mn_spam=new JMenu("Spam");
			JMenuItem mntm_addtospam=new JMenuItem("Add to Spam");
				mntm_addtospam.addActionListener(new AddSelectedContactsToSpamActionListener());
			mn_spam.add(mntm_addtospam);
			JMenuItem mntm_removefromspam=new JMenuItem("Remove from Spam");
				mntm_removefromspam.addActionListener(new RemoveSelectedContactsFromSpamActionListener());
			mn_spam.add(mntm_removefromspam);
		ppmn_ic_selected.add(mn_spam);
		
		JMenuItem mntm_deleteContacts=new JMenuItem("Delete");
			mntm_deleteContacts.addActionListener(new DeleteSelectedContactsActionListener());
		ppmn_ic_selected.add(mntm_deleteContacts);
		
		JMenu mn_groups=new JMenu("Groups");
		ppmn_ic_selected.add(mn_groups);
		
		JMenu mn_addtogroup=new JMenu("Add to Group");
		mn_groups.add(mn_addtogroup);
		mn_addtogroup.setEnabled(false);
		MenuScroller.setScrollerFor(mn_addtogroup,10);
		
		JMenu mn_removefromgroup=new JMenu("Remove from Group");
		mn_groups.add(mn_removefromgroup);
		mn_removefromgroup.setEnabled(false);
		MenuScroller.setScrollerFor(mn_removefromgroup,10);
		
		String s1=mn_addtogroup.getToolTipText();
		String s2=mn_removefromgroup.getToolTipText();
		int amount=gs.size();
		int progress=0;
		
		for(Group g:gs){
			progress++;
			ImageIcon ic=new ImageIcon(g.getPicturepath());
			ic.setImage(ic.getImage().getScaledInstance(17,17,Image.SCALE_FAST));
			
			JMenuItem mntm_group_a=new JMenuItem(g.getName());
				mntm_group_a.setToolTipText(g.toString());
				mntm_group_a.setIcon(ic);
				mntm_group_a.addActionListener(new AddSelectedContactsToGroupActionListener(g));
			mn_addtogroup.add(mntm_group_a);
			mn_addtogroup.setToolTipText("Generating... "+progress+"/"+amount);
			
			JMenuItem mntm_group_r=new JMenuItem(g.getName());
				mntm_group_r.setToolTipText(g.toString());
				mntm_group_r.setIcon(ic);
				mntm_group_r.addActionListener(new RemoveSelectedContactsFromGroupActionListener(g));
			mn_removefromgroup.add(mntm_group_r);
			mn_removefromgroup.setToolTipText("Generating... "+progress+"/"+amount);
		}
		mn_addtogroup.setToolTipText(s1);
		mn_addtogroup.setEnabled(true);
		mn_removefromgroup.setToolTipText(s2);
		mn_removefromgroup.setEnabled(true);
	}
	
	/**
	 * Generate the PuMn for the selected IGs
	 */
	private void generatePopupMenuSelected_Group(){
		ppmn_ig_selected=new JPopupMenu();

		ppmn_ig_selected.add(new JLabel("Selected Groups:"));
		
		ArrayList<Contact> cs=DatabaseC.getContacts();
		cs.sort(new Contact.ContactComparator());
		
		JMenuItem mntm_newEmail=new JMenuItem("New Email");
		mntm_newEmail.addActionListener(new NewEmailToSelectedGroupsActionlistener());
		ppmn_ig_selected.add(mntm_newEmail);
		
		JMenuItem mntm_deleteGroups=new JMenuItem("Delete");
			mntm_deleteGroups.addActionListener(new DeleteSelectedGroupsActionListener());
		ppmn_ig_selected.add(mntm_deleteGroups);
		
		JMenu mn_contacts=new JMenu("Contacts");
		ppmn_ig_selected.add(mn_contacts);
		
		JMenu mn_addcontacts=new JMenu("Add Contact");
		mn_contacts.add(mn_addcontacts);
		mn_addcontacts.setEnabled(false);
		MenuScroller.setScrollerFor(mn_addcontacts,10);
		
		JMenu mn_removecontacts=new JMenu("Remove Contact");
		mn_contacts.add(mn_removecontacts);
		mn_removecontacts.setEnabled(false);
		MenuScroller.setScrollerFor(mn_removecontacts,10);
		
		String s1=mn_addcontacts.getToolTipText();
		String s2=mn_removecontacts.getToolTipText();
		int amount=cs.size();
		int progress=0;
		
		for(Contact c:cs){
			progress++;
			ImageIcon ic=new ImageIcon(c.getPicturepath());
			ic.setImage(ic.getImage().getScaledInstance(17,17,Image.SCALE_FAST));
			
			JMenuItem mntm_contact_a=new JMenuItem(c.getShownname());
				mntm_contact_a.setToolTipText(c.toString());
				mntm_contact_a.setIcon(ic);
				mntm_contact_a.addActionListener(new AddContactToSelectedGroupsActionListener(c));
			mn_addcontacts.add(mntm_contact_a);
			mn_addcontacts.setToolTipText("Generating... "+progress+"/"+amount);
			
			JMenuItem mntm_contact_r=new JMenuItem(c.getShownname());
				mntm_contact_r.setToolTipText(c.toString());
				mntm_contact_r.setIcon(ic);
				mntm_contact_r.addActionListener(new RemoveContactFromSelectedGroupsActionListener(c));
			mn_removecontacts.add(mntm_contact_r);
			mn_removecontacts.setToolTipText("Generating... "+progress+"/"+amount);
		}
		
		mn_addcontacts.setToolTipText(s1);
		mn_addcontacts.setEnabled(true);
		mn_removecontacts.setToolTipText(s2);
		mn_removecontacts.setEnabled(true);
	}
	
	/**
	 * @return the selected ics
	 */
	private ArrayList<Item_Contact> getSelectedICs(){
		ArrayList<Item_Contact> ics_selected=new ArrayList<Item_Contact>();
		for(Item_Contact ic:_ics_visible){
			if(ic.isSelected())ics_selected.add(ic);
		}
		return ics_selected;
	}
	
	/**
	 * @return the selected igs
	 */
	private ArrayList<Item_Group> getSelectedIGs(){
		ArrayList<Item_Group> igs_selected=new ArrayList<Item_Group>();
		for(Item_Group ig:_igs_visible){
			if(ig.isSelected())igs_selected.add(ig);
		}
		return igs_selected;
	}
	
	private static boolean generatingItems=false;
	/**
	 * Generator, to generate the Items
	 * @author eProtectioneers
	 */
	private class ItemGenerator implements Runnable{
		@Override
		public void run() {
			if(generatingItems){
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {}
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}else{
				generatingItems=true;
				generateItems();
				generatingItems=false;
			}
		}
	}
	
	private static boolean generatingPopup_c=false;
	/**
	 * Generator, to generate the PopupMn for the Contacts
	 * @author eProtectioneers
	 */
	private class SelectedPopUpGenerator_Contact implements Runnable{
		@Override
		public void run() {
			if(!generatingPopup_c){
				generatingPopup_c=true;
				generatePopupMenuSelected_Contact();
				generatingPopup_c=false;
			}
		}
	}
	private static boolean generatingPopup_g=false;
	/**
	 * Generator, to generate the PopupMn for the Groups
	 * @author eProtectioneers
	 */
	private class SelectedPopUpGenerator_Group implements Runnable{
		@Override
		public void run() {
			if(!generatingPopup_g){
				generatingPopup_g=true;
				generatePopupMenuSelected_Group();
				generatingPopup_g=false;
			}
		}
	}

	/**
	 * ActionListener to write a new Email so the selected cs
	 * @author eProtectioneers
	 */
	private class NewEmailToSelectedContactsActionlistener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			for(Item_Contact ic:getSelectedICs()){
				//MZ_Sojourner_cntl
			}		
		}
	}

	/**
	 * ActionListener to write a new Email so the selected gs
	 * @author eProtectioneers
	 */
	private class NewEmailToSelectedGroupsActionlistener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			for(Item_Group ig:getSelectedIGs()){
				//MZ_Sojourner_cntl
			}		
		}
	}

	/**
	 * ActionListener to add the selected cs to a Group
	 * @author eProtectioneers
	 */
	private class AddSelectedContactsToGroupActionListener implements ActionListener{
		Group _g;
		
		public AddSelectedContactsToGroupActionListener(Group g){
			this._g=g;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			switch(JOptionPane.showOptionDialog(PPCA_PanaceaWindow.getFrame(), "Do you really want to Add the selected Contacts to '"+_g.getName()+"'?", "Add to Group", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1])){
				case JOptionPane.YES_OPTION:
					for(Item_Contact ic:getSelectedICs()){
						ic.setPUOOGenerated(false);
						new AddContactToGroupActionListener(ic.getContact(), _g).actionPerformed(e);
					}
					JOptionPane.showMessageDialog(PPCA_PanaceaWindow.getFrame(), "Contacts Added", "", JOptionPane.INFORMATION_MESSAGE, null);
				default:
					break;
			}
		}	
	}

	/**
	 * ActionListener to remove the selected cs from a Group
	 * @author eProtectioneers
	 */
	private class RemoveSelectedContactsFromGroupActionListener implements ActionListener{
		Group _g;
		
		public RemoveSelectedContactsFromGroupActionListener(Group g){
			this._g=g;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			switch(JOptionPane.showOptionDialog(PPCA_PanaceaWindow.getFrame(), "Do you really want to Remove the selected Contacts from '"+_g.getName()+"'?", "Remove from Group", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1])){
				case JOptionPane.YES_OPTION:
					for(Item_Contact ic:getSelectedICs()){
						ic.setPUOOGenerated(false);
						new RemoveContactFromGroupActionListener(ic.getContact(), _g).actionPerformed(e);
					}
					JOptionPane.showMessageDialog(PPCA_PanaceaWindow.getFrame(), "Contacts Removed", "", JOptionPane.INFORMATION_MESSAGE, null);
				default:
					break;
			}
		}	
	}

	/**
	 * ActionListener to add the selected cs to spam
	 * @author eProtectioneers
	 */
	private class AddSelectedContactsToSpamActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			switch(JOptionPane.showOptionDialog(PPCA_PanaceaWindow.getFrame(), "Do you really want to Add the selected Contacts to Spam?", "Add to Spam", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1])){
				case JOptionPane.YES_OPTION:
					for(Item_Contact ic:getSelectedICs()){
						ic.getContact().setSpam(true, false);
						ic.setToolTipText(ic.getContact().toString());
					}
					JOptionPane.showMessageDialog(PPCA_PanaceaWindow.getFrame(), "Contacts Added to Spam", "", JOptionPane.WARNING_MESSAGE, null);
				default:
					break;
			}
		}
	}

	/**
	 * ActionListener to remove the selected cs from spam
	 * @author eProtectioneers
	 */
	private class RemoveSelectedContactsFromSpamActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			switch(JOptionPane.showOptionDialog(PPCA_PanaceaWindow.getFrame(), "Do you really want to Remove the selected Contacts from Spam?", "Remove from Spam", JOptionPane.WARNING_MESSAGE, JOptionPane.QUESTION_MESSAGE, null, options, options[1])){
				case JOptionPane.YES_OPTION:
					for(Item_Contact ic:getSelectedICs()){
						ic.getContact().setSpam(false, false);
						ic.setToolTipText(ic.getContact().toString());
					}
					JOptionPane.showMessageDialog(PPCA_PanaceaWindow.getFrame(), "Contacts Removed from Spam", "", JOptionPane.INFORMATION_MESSAGE, null);
				default:
					break;
			}
		}
	}

	/**
	 * ActionListener to delete the selected cs
	 * @author eProtectioneers
	 */
	private class DeleteSelectedContactsActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			switch(JOptionPane.showOptionDialog(PPCA_PanaceaWindow.getFrame(), "Do you really want to Delete the selected Contacts?", "Delete Contacts", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1])){
				case JOptionPane.YES_OPTION:
					for(Item_Contact ic:getSelectedICs()){
						DatabaseC.removeContact(ic.getContact());
					}
					JOptionPane.showMessageDialog(PPCA_PanaceaWindow.getFrame(), "Contacts Deleted", "", JOptionPane.INFORMATION_MESSAGE, null);
					new BtnRefreshActionListener().actionPerformed(e);
				default:
					break;
			}
		}
	}
	

	/**
	 * ActionListener to add a c to the selected gs
	 * @author eProtectioneers
	 */
	private class AddContactToSelectedGroupsActionListener implements ActionListener{
		Contact _c;
		
		public AddContactToSelectedGroupsActionListener(Contact c){
			this._c=c;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			switch(JOptionPane.showOptionDialog(PPCA_PanaceaWindow.getFrame(), "Do you really want to Add '"+_c.getShownname()+"' to the selected Groups?", "Add Contact", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1])){
				case JOptionPane.YES_OPTION:
					for(Item_Group ig:getSelectedIGs()){
						ig.setPUOOGenerated(false);
						new AddContactToGroupActionListener(_c, ig.getGroup()).actionPerformed(e);
					}
					JOptionPane.showMessageDialog(PPCA_PanaceaWindow.getFrame(), "Contact Added", "", JOptionPane.INFORMATION_MESSAGE, null);
				default:
					break;
			}
		}	
	}

	/**
	 * ActionListener to remove a c from the selected gs
	 * @author eProtectioneers
	 */
	private class RemoveContactFromSelectedGroupsActionListener implements ActionListener{
		Contact _c;
		
		public RemoveContactFromSelectedGroupsActionListener(Contact c){
			this._c=c;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			switch(JOptionPane.showOptionDialog(PPCA_PanaceaWindow.getFrame(), "Do you really want to Remove '"+_c.getShownname()+"' from the selected Groups?", "Remove Contact", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1])){
			case JOptionPane.YES_OPTION:
				for(Item_Group ig:getSelectedIGs()){
					ig.setPUOOGenerated(false);
					new RemoveContactFromGroupActionListener(_c, ig.getGroup()).actionPerformed(e);
				}
				JOptionPane.showMessageDialog(PPCA_PanaceaWindow.getFrame(), "Contact Removed", "", JOptionPane.INFORMATION_MESSAGE, null);
			default:
				break;
			}
		}	
	}

	/**
	 * ActionListener to delete the selected cs
	 * @author eProtectioneers
	 */
	private class DeleteSelectedGroupsActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			switch(JOptionPane.showOptionDialog(PPCA_PanaceaWindow.getFrame(), "Do you really want to Delete the selected Groups?", "Delete Groups", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1])){
				case JOptionPane.YES_OPTION:
					for(Item_Group ig:getSelectedIGs()){
						DatabaseG.removeGroup(ig.getGroup());
					}
					JOptionPane.showMessageDialog(PPCA_PanaceaWindow.getFrame(), "Group Deleted", "", JOptionPane.INFORMATION_MESSAGE, null);
					new BtnRefreshActionListener().actionPerformed(e);
				default:
					break;
			}
		}
	}

	/**
	 * ActionListener to set the 'selected' of the CBar
	 * @author eProtectioneers
	 */
	private class CBoxTypeActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			selected=(Selected)cbox_type.getSelectedItem();
			addItems();
		}
	}

	/**
	 * ActionListener to refresh the ios
	 * @author eProtectioneers
	 */
	private class BtnRefreshActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			new Thread(new ItemGenerator()).start();
			searchField.setFirst();
		}
	}

	/**
	 * ActionListener to show the addPopup
	 * @author eProtectioneers
	 */
	private class Btn_addActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ppmn_add.show(btn_add, 3, btn_add.getHeight());
		}
	}

	/**
	 * ActionListener to add the selected Object (Contact/Group)
	 * @author eProtectioneers
	 */
	private class MntmAddActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(mntm_addContact)){
				new Page_AddContact(PPCA_PanaceaWindow.getFrame()).setVisible(true);
			}else if(e.getSource().equals(mntm_addGroup)){
				new Page_AddGroup(PPCA_PanaceaWindow.getFrame()).setVisible(true);
			}
		}
	}

	/**
	 * MouseListener, to change the ScrollBars visibility state
	 * @author eProtectioneers
	 */
	private class ScrollBarVisibilityMouseListener extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			changeScrollBarPolicy(true);
		}
		@Override
		public void mouseExited(MouseEvent e) {
			changeScrollBarPolicy(false);
		}
	}
}
