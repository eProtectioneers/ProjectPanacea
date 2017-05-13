package org.eprotectioneers.panacea.contactmanagement.view;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.border.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.*;

import org.eprotectioneers.panacea.contactmanagement.components.ImagePanel;
import org.eprotectioneers.panacea.contactmanagement.components.RoundRectangleButton;
import org.eprotectioneers.panacea.contactmanagement.components.RoundRectanglePanel;
import org.eprotectioneers.panacea.contactmanagement.models.AddContactToGroupActionListener;
import org.eprotectioneers.panacea.contactmanagement.models.ChooseFile;
import org.eprotectioneers.panacea.contactmanagement.models.Contact;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseC;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseCG;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseG;
import org.eprotectioneers.panacea.contactmanagement.models.Group;
import org.eprotectioneers.panacea.contactmanagement.models.NotAddedContactItemsGenerator;
import org.eprotectioneers.panacea.contactmanagement.models.RemoveContactFromGroupActionListener;
import org.eprotectioneers.panacea.userinterface.PPCA_PanaceaWindow;

public class Page_Group extends JPanel{
	
	private Group _g;
	private ImagePanel pnl_image;
	private PageItem pi_groupname;
	private PageItem pi_description;
	private JScrollPane scrollPane;
	private JPanel pnl_contacts;
	private JPanel pnl_scrollPane;
	private JPanel pnl_contactsUp;
	private JLabel lbl_members;
	private JButton btnEditMembers;
	private static ImageIcon ic_editGroup;
	private static ImageIcon ic_removeContact;
	private static ImageIcon ic_removeContact_pressed;
	private static ImageIcon ic_removeContact_rollover;
	private boolean reload=false;
	private boolean reloading;
	private JPopupMenu item_selectedPopup;
	private ArrayList<Integer> _cids=new ArrayList<Integer>();
	private ArrayList<Item_Contact> _ics=new ArrayList<Item_Contact>();
	private NotAddedContactItemsGenerator naicg;
	private boolean _first=true;
	private Thread t1;
	private JButton btnDeleteGroup;
	private static ImageIcon ic_delete;
	private static ImageIcon ic_delete_pressed;
	private static ImageIcon ic_delete_rollover;
	
	/**
	 * Create the panel.
	 */
	public Page_Group(Group g) {
		this._g=g;
		naicg=new NotAddedContactItemsGenerator(_g,this);
		initialize();
	}
	
	private void initialize() {
		setLayout(new MigLayout("", "[5%][100px:25%:300px,grow][40][30%,grow,fill][40.00][10%,grow,fill][20%,grow,fill][5%]", "[15.00][25px:11%:75px][25px:11%:75px][25px:11%:75px][13px:5.5%:38px,grow][12px:5.5%:37px,grow][39%,grow][15]"));
		setBackground(Color.WHITE);

		pnl_image = new ImagePanel(_g.getPicturepath(),Group.getDefaultpicpath());
		pnl_image.setBackground(Color.BLACK);
		add(pnl_image, "cell 1 1 1 6,grow");	
		
		ChangeImageListener cil=new ChangeImageListener();
		pnl_image.getBtnChangePicture().addActionListener(cil);
		pnl_image.getMntmRemovePicture().addActionListener(cil);
		
		ic_delete=new ImageIcon("images/icon_delete.png");
		ic_delete.setImage(ic_delete.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		
		ic_delete_pressed=new ImageIcon("images/icon_delete_pressed.png");
		ic_delete_pressed.setImage(ic_delete_pressed.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		
		ic_delete_rollover=new ImageIcon("images/icon_delete_rollover.png");
		ic_delete_rollover.setImage(ic_delete_rollover.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		
		btnDeleteGroup = new JButton();
		btnDeleteGroup.setToolTipText("Delete Group");
		btnDeleteGroup.setFocusPainted(false);
		btnDeleteGroup.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnDeleteGroup.setOpaque(false);
		btnDeleteGroup.setContentAreaFilled(false);
		btnDeleteGroup.setBorderPainted(false);
		btnDeleteGroup.setMaximumSize(new Dimension(30,30));
		btnDeleteGroup.setMinimumSize(new Dimension(30,30));
		btnDeleteGroup.setIcon(ic_delete);
		btnDeleteGroup.setPressedIcon(ic_delete_pressed);
		btnDeleteGroup.setRolloverIcon(ic_delete_rollover);
		btnDeleteGroup.addActionListener(new BtnDeleteGroupActionListener());
		add(btnDeleteGroup, "cell 6 1,alignx right,aligny top");
		
		pi_groupname = new PageItem(_g.getName(), "Group Name", this);
		pi_groupname.setOpaque(true);
		pi_groupname.setFont(new Font("Calibri", Font.PLAIN, 16));
		add(pi_groupname, "cell 3 2,grow");
		
		pi_description = new PageItem(_g.getDescription(), "Description",  this);
		pi_description.setOpaque(true);
		pi_description.setFont(new Font("Calibri", Font.PLAIN, 16));
		add(pi_description, "cell 3 3 4 1,grow");
		
		SaveGroupActionListener sgal=new SaveGroupActionListener();
		pi_groupname.getBtnSave().addActionListener(sgal);
		pi_description.getBtnSave().addActionListener(sgal);
		generateContactView();
	}
	
	private void generateContactView(){
		pnl_scrollPane = new RoundRectanglePanel(10);
		pnl_scrollPane.setBackground(new Color(9,29,62));
		pnl_scrollPane.setBorder(new EmptyBorder(2, 5, 5, 5));
		pnl_scrollPane.setLayout(new BorderLayout(0, 0));
		add(pnl_scrollPane,"cell 3 5 3 2,grow");
		pnl_scrollPane.setPreferredSize(new Dimension(pnl_scrollPane.getWidth(),pnl_scrollPane.getHeight()));
		pnl_scrollPane.setMaximumSize(new Dimension(500,99999));

		pnl_contacts=new JPanel();
		pnl_contacts.setOpaque(false);
		
		scrollPane = new JScrollPane(pnl_contacts);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.setBorder(null);
		pnl_scrollPane.add(scrollPane);
		
		addSrollPanePnl_Up();

		ic_removeContact=new ImageIcon("images/icon_remove.png");
		ic_removeContact.setImage(ic_removeContact.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		
		ic_removeContact_pressed=new ImageIcon("images/icon_remove_pressed.png");
		ic_removeContact_pressed.setImage(ic_removeContact_pressed.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		
		ic_removeContact_rollover=new ImageIcon("images/icon_remove_rollover.png");
		ic_removeContact_rollover.setImage(ic_removeContact_rollover.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		
		generateSelectedPopopMenu();
		generateContactItems();
	}

	private void addSrollPanePnl_Up(){
		pnl_contactsUp = new JPanel();
		pnl_contactsUp.setOpaque(false);
		pnl_contactsUp.setBorder(new MatteBorder(0, 0, 1, 0,Color.WHITE));
		pnl_scrollPane.add(pnl_contactsUp, BorderLayout.NORTH);
		
		lbl_members = new JLabel("Group Members:");
		lbl_members.setBorder(new EmptyBorder(6, 0, 0, 0));
		lbl_members.setForeground(new Color(255, 255, 255));
		
		ic_editGroup=new ImageIcon("images/icon_edit2.png");
		ic_editGroup.setImage(ic_editGroup.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		
		btnEditMembers = new RoundRectangleButton("",5);
		btnEditMembers.setToolTipText("Edit Group Members\r\n");
		btnEditMembers.setAlignmentY(Component.TOP_ALIGNMENT);
		btnEditMembers.setBackground(new Color(255, 140, 0));
		btnEditMembers.setMinimumSize(new Dimension(25, 25));
		btnEditMembers.setMaximumSize(new Dimension(25, 25));
		btnEditMembers.setIcon(ic_editGroup);
		btnEditMembers.setBorderPainted(false);
		btnEditMembers.setOpaque(false);
		btnEditMembers.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEditMembers.addActionListener(new BtnEditMembersActionListener());
	
		GroupLayout gl_pnl_contactsUp = new GroupLayout(pnl_contactsUp);
		gl_pnl_contactsUp.setHorizontalGroup(
			gl_pnl_contactsUp.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnl_contactsUp.createSequentialGroup()
					.addGap(9)
					.addComponent(lbl_members)
					.addPreferredGap(ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
					.addComponent(btnEditMembers)
					.addGap(5))
		);
		gl_pnl_contactsUp.setVerticalGroup(
			gl_pnl_contactsUp.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnl_contactsUp.createParallelGroup(Alignment.BASELINE)
					.addComponent(lbl_members)
					.addComponent(btnEditMembers))
		);
		pnl_contactsUp.setLayout(gl_pnl_contactsUp);
	}
	
	private void generateContactItems(){
		ArrayList<Integer> ids_database=DatabaseCG.getContacts(_g);
		ArrayList<Contact> contacts_database=DatabaseC.getContacts(ids_database);
		ArrayList<Integer> rem_ids=new ArrayList<Integer>();
		for(int id:_cids){
			if(!ids_database.contains(id)){
				for(Item_Contact ic:_ics){
					if(ic.getContact().getId()==id){_ics.remove(ic);break;}
				}
				rem_ids.add(id);
			}
		}
		_cids.removeAll(rem_ids);
		
		t1=new Thread(naicg);
		t1.start();
		for(Contact c:contacts_database){
			if(!_cids.contains(c.getId())){
				Item_Contact ic;
				if(_first){
					ArrayList<Integer> ids=new ArrayList<Integer>();
					ids.add(c.getId());
					ic=new Item_Contact(c);
				}else{ic=naicg.getNotAddedItem_contact(c.getId());}
				ic.setSelectedPopup(item_selectedPopup);
				ic.setBackground(Color.gray);
				ic.setForeground(Color.WHITE);
				_ics.add(ic);
				_cids.add(c.getId());
			}
		}
		_first=false;
		addContactItems();
	}	
	
	private void addContactItems(){
		lbl_members.setText("Group Members:");
		String s="[]";
		for(int i=0;i<_ics.size();i++)s+="[]";
		pnl_contacts.removeAll();
		pnl_contacts.setLayout(new MigLayout("", "[grow][]", s));
		
		reloading=true;
		this.btnEditMembers.setEnabled(false);
		int i=0;
		
		_ics.sort(new Item_Contact.ItemContactComparator());
		for(Item_Contact  ic:_ics){
			ic.setSelectable(false);
			ic.setSelected(false);
			ic.setEditable(false);
			pnl_contacts.add(ic,"cell 0 "+i+",grow");
			
			JButton btn_remove=new JButton();
			btn_remove.setFocusPainted(false);
			btn_remove.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btn_remove.setOpaque(false);
			btn_remove.setContentAreaFilled(false);
			btn_remove.setBorderPainted(false);
			btn_remove.setMaximumSize(new Dimension(25,25));
			btn_remove.setMinimumSize(new Dimension(25,25));
			btn_remove.setIcon(ic_removeContact);
			btn_remove.setPressedIcon(ic_removeContact_pressed);
			btn_remove.setRolloverIcon(ic_removeContact_rollover);
			btn_remove.setToolTipText("Remove Contact from Group");
			btn_remove.addActionListener(new RemoveContactFromGroupActionListener(ic.getContact(),_g));
			btn_remove.addActionListener(new UpdateContactViewActionListener());
			
			pnl_contacts.add(btn_remove,"cell 1 "+i);
			i++;
			if(reload){reload=false;break;}
		}
		
		if(_ics.size()==0){
			JLabel lbl_noMember=new JLabel("No Members");
			lbl_noMember.setForeground(Color.WHITE);
			lbl_noMember.setFont(new Font("Calibri",Font.ITALIC,16));
			pnl_contacts.add(lbl_noMember);
		}
		
		setVisible(false);
		setVisible(true);
		this.btnEditMembers.setEnabled(true);
		reloading=false;
	}
	
	private void addPageEdit(){
		lbl_members.setText("No Group Members:");
		ArrayList<Item_Contact> ics_na=naicg.getICs_NotAdded();
		String s="[]";
		for(int i=0;i<ics_na.size();i++)s+="[]";
		pnl_contacts.removeAll();
		pnl_contacts.setLayout(new MigLayout("", "[grow]", s));
		
		reloading=true;
		this.btnEditMembers.setEnabled(false);
		int i=0;
		ics_na.sort(new Item_Contact.ItemContactComparator());
		
		for(Item_Contact  ic:ics_na){
			ic.setSelectable(true);
			pnl_contacts.add(ic,"cell 0 "+i+",grow");
			i++;
			if(reload){reload=false;break;}
		}
		
		if(ics_na.size()==0){
			JLabel lbl_noMember=new JLabel("Every Contact is Added to this Group");
			lbl_noMember.setForeground(Color.WHITE);
			lbl_noMember.setFont(new Font("Calibri",Font.ITALIC,16));
			pnl_contacts.add(lbl_noMember);
		}
		
		setVisible(false);
		setVisible(true);
		this.btnEditMembers.setEnabled(true);
		reloading=false;
	}
	
	public Group getCurrentlyGroup(){
		Group g=new Group(_g.getId(), pi_groupname.getText(), pi_description.getText(), _g.getPicturepath());
		return g;
	}
	
	private JPopupMenu generateSelectedPopopMenu(){
		item_selectedPopup=new JPopupMenu();
		JMenuItem mntm_remove=new JMenuItem("Remove selected Contacts from Group");
		mntm_remove.addActionListener(new RemoveSelectedContactsActionListener());
		item_selectedPopup.add(mntm_remove);
		return item_selectedPopup;
	}
	
	private ArrayList<Item_Contact> getSelectedICs(){
		ArrayList<Item_Contact> ret=new ArrayList<Item_Contact>();
		for(Item_Contact ic:_ics){
			if(ic.isSelected())ret.add(ic);
		}
		for(Item_Contact ic:naicg.getICs_NotAdded()){
			if(ic.isSelected())ret.add(ic);
		}
		return ret;
	}
	
	private class RemoveSelectedContactsActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<Item_Contact> _ics_selected=getSelectedICs();
			for(Item_Contact c:_ics_selected){
				new RemoveContactFromGroupActionListener(c.getContact(), _g).actionPerformed(e);
			}
			new UpdateContactViewActionListener().actionPerformed(null);
		}	
	}
	
	public class AddSelectedContactsActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			for(Item_Contact c:getSelectedICs()){
				new AddContactToGroupActionListener(c.getContact(), _g).actionPerformed(e);
			}
			new UpdateContactViewActionListener().actionPerformed(null);
		}	
	}
	
	private class SaveGroupActionListener implements ActionListener, Runnable{
		@Override
		public void actionPerformed(ActionEvent e) {
			Thread t=new Thread(this);
			t.start();
		}
		public void saveGroup(){
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			_g=getCurrentlyGroup();
			DatabaseG.updateGroup(_g);
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
			saveGroup();
		}
	}
		
	private class ChangeImageListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String s;
			if(e.getSource().equals(pnl_image.getBtnChangePicture()))s=readFilePath();
			else s=Group.getDefaultpicpath();
			_g.setPicturepath(s);
			pnl_image.setPicturePath(s);
			setVisible(false);
			setVisible(true);
			new SaveGroupActionListener().saveGroup();
		}
		private String readFilePath(){
			File file=ChooseFile.getPictoRead();
			if(file!=null&&(new ImageIcon(file.getAbsolutePath()).getImage()) instanceof Image)return file.getAbsolutePath();
			else return pnl_image.getPicturePath();
		}
	}
	
	private class UpdateContactViewActionListener implements ActionListener,Runnable{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(reloading)reload=true;
			new Thread(this).start();
		}
		@Override
		public void run() {
			setCursor(new Cursor(Cursor.WAIT_CURSOR));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while(!RemoveContactFromGroupActionListener.isFinished()){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			while(!AddContactToGroupActionListener.isFinished()){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			generateContactItems();
		}
	}
	
	private class BtnEditMembersActionListener implements ActionListener {
		boolean edit=true;
		@Override
		public void actionPerformed(ActionEvent e) {
			if(edit)addPageEdit();
			else new UpdateContactViewActionListener().actionPerformed(null);
			edit=!edit;
		}
	}
	
	private class BtnDeleteGroupActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object options[]={"yes", "no"};
			switch(JOptionPane.showOptionDialog(PPCA_PanaceaWindow.getFrame(), "Do you really want to DELETE this Group?", "DELETE Group", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1])){
			case JOptionPane.YES_OPTION:
				DatabaseG.removeGroup(_g);
				JOptionPane.showMessageDialog(null, "Group Deleted", "", JOptionPane.INFORMATION_MESSAGE, null);
				//EXIT PAGE
				PPCA_PanaceaWindow.setCenterPanel(PPCA_PanaceaWindow.getMainPanel());
			default:
				break;
			}
		}
	}
}