//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.view;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

import javax.swing.border.*;

import org.eprotectioneers.panacea.contactmanagement.components.ImagePanel;
import org.eprotectioneers.panacea.contactmanagement.components.RoundRectangleButton;
import org.eprotectioneers.panacea.contactmanagement.components.RoundRectanglePanel;
import org.eprotectioneers.panacea.contactmanagement.models.AddContactToGroupActionListener;
import org.eprotectioneers.panacea.contactmanagement.models.ChooseFile;
import org.eprotectioneers.panacea.contactmanagement.models.Contact;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseC;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseG;
import org.eprotectioneers.panacea.contactmanagement.models.Group;
import org.eprotectioneers.panacea.userinterface.PPCA_PanaceaWindow;

/**
 * A Page to add a new Group
 * @author eProtectioneers
 */
public class Page_AddGroup extends JFrame{
	
	private JPanel contentPane;
	private ImagePanel pnl_image;
	private PageItem_new pi_groupname;
	private PageItem_new pi_description;
	private JButton btnSave;
	private JButton btnCancel;
	private ArrayList<PageItem_new> entryfields=new ArrayList<PageItem_new>();
	private JPanel pnl_scrollPane;
	private JScrollPane scrollPane;
	private JPanel pnl_contactsUp;
	private JPanel pnl_contacts;	
	private ArrayList<Item_Contact> _ics=new ArrayList<Item_Contact>();
	private Page_AddGroup pag=this;
	
	/**
	 * Create the panel.
	 */
	public Page_AddGroup(Component component) {
		super("New Group");
		Point componentLocation = component.getLocation();
		Dimension componentDimension=component.getSize();
		this.setSize((int) (componentDimension.width-componentDimension.width/3),
				(int) (componentDimension.height-componentDimension.height/3));
		this.setLocation((int) (componentLocation.x + component.getWidth()/2-this.getWidth()/2),
				(int)(componentLocation.y + component.getHeight()/2-this.getHeight()/2));

		initialize();
	}
	
	/**
	 * Initializes
	 */
	private void initialize() {
		contentPane = new JPanel();

		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		contentPane.setLayout(new MigLayout("", "[5%][100px:25%:300px,grow][40][30%,grow,fill][40.00][10%,grow,fill][20%,grow,fill][5%]", "[15.00][25px:11%:75px][25px:11%:75px][25px:11%:75px][13px:5.5%:38px,grow][12px:5.5%:37px,grow][27%,grow][12%][15]"));
		
		pnl_image = new ImagePanel(Group.getDefaultpicpath());
		pnl_image.setBackground(Color.BLACK);
		ChangeImageListener cil=new ChangeImageListener();
		pnl_image.getBtnChangePicture().addActionListener(cil);
		pnl_image.getMntmRemovePicture().addActionListener(cil);
		contentPane.add(pnl_image, "cell 1 1 1 6,grow");	
		
		pi_groupname = new PageItem_new("Group Name", contentPane);
		pi_groupname.setOpaque(true);
		pi_groupname.setFont(new Font("Calibri", Font.PLAIN, 16));
		pi_groupname.getTextField().addActionListener(new PiActionListener());
		contentPane.add(pi_groupname, "cell 3 2,grow");
		entryfields.add(pi_groupname);
		
		pi_description = new PageItem_new("Description",  contentPane);
		pi_description.setOpaque(true);
		pi_description.setFont(new Font("Calibri", Font.PLAIN, 16));
		pi_description.getTextField().addActionListener(new PiActionListener());
		contentPane.add(pi_description, "cell 3 3 4 1,grow");
		entryfields.add(pi_description);
		
		btnCancel = new RoundRectangleButton("Cancel", 15);
		btnCancel.setMaximumSize(new Dimension(70, 23));
		btnCancel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnCancel.setBackground(Color.WHITE);
		btnCancel.addActionListener(new BtnCancelActionListener());
		
		generateContactView();
		
		contentPane.add(btnCancel, "cell 3 7");
		
		btnSave = new RoundRectangleButton("Save",15);
		btnSave.setMaximumSize(new Dimension(70, 23));
		btnSave.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnSave.setBackground(Color.WHITE);
		btnSave.addActionListener(new BtnSaveActionListener());
		contentPane.add(btnSave, "cell 6 7,alignx right");
	}
	
	/**
	 * Generate the Contact view
	 */
	private void generateContactView(){
		pnl_scrollPane = new RoundRectanglePanel(10);
		pnl_scrollPane.setToolTipText("Select the Contacts, you want to add to your Group");
		pnl_scrollPane.setBackground(new Color(9,29,62));
		pnl_scrollPane.setBorder(new EmptyBorder(2, 5, 5, 5));
		pnl_scrollPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(pnl_scrollPane,"cell 3 5 3 2,grow");
		pnl_scrollPane.setPreferredSize(new Dimension(pnl_scrollPane.getWidth(),pnl_scrollPane.getHeight()));
		pnl_scrollPane.setMaximumSize(new Dimension(500,99999));

		pnl_contacts=new JPanel();
		pnl_contacts.setOpaque(false);
		
		scrollPane = new JScrollPane(pnl_contacts);
		scrollPane.setToolTipText("Select the Contacts, you want to add to your Group");
		pnl_contacts.setLayout(new BoxLayout(pnl_contacts, BoxLayout.Y_AXIS));
		
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		pnl_scrollPane.add(scrollPane);
		
		addSrollPanePnl_Up();

		generateContactItems();
	}

	/**
	 * Add the Panel above the ScrollPane
	 */
	private void addSrollPanePnl_Up(){
		pnl_contactsUp = new JPanel();
		pnl_contactsUp.setOpaque(false);
		pnl_contactsUp.setBorder(new MatteBorder(0, 0, 1, 0,Color.WHITE));
		pnl_scrollPane.add(pnl_contactsUp, BorderLayout.NORTH);
		
		JLabel lbl_members = new JLabel("Select Contacts, you want to add:");
		lbl_members.setBorder(new EmptyBorder(6, 0, 0, 0));
		lbl_members.setForeground(new Color(255, 255, 255));
		lbl_members.setBorder(new EmptyBorder(4,9,0,0));
		pnl_contactsUp.add(lbl_members);
		
		pnl_contactsUp.setLayout(new BoxLayout(pnl_contactsUp,BoxLayout.X_AXIS));
	}
	
	/**
	 * Generate the ContactItems
	 */
	private void generateContactItems(){
		ArrayList<Contact> contacts=DatabaseC.getContacts();
		
		for(Contact c:contacts){
			Item_Contact ic=new Item_Contact(c);
			ic.setBackground(Color.gray);
			ic.setForeground(Color.WHITE);
			ic.setSelectable(true);
			_ics.add(ic);
		}
		addContactItems();
	}	
	
	/**
	 * Add the ContactItems
	 */
	private void addContactItems(){
		String s="[]";
		for(int i=0;i<_ics.size();i++)s+="[]";
		
		pnl_contacts.removeAll();
		pnl_contacts.setLayout(new MigLayout("", "[grow]", s));
		
		int i=0;
		_ics.sort(new Item_Contact.ItemContactComparator());
		for(Item_Contact ic:_ics){		
			pnl_contacts.add(ic,"cell 0 "+i+",grow");			
			i++;
		}
		if(i==0){
			JLabel lbl_noMember=new JLabel("No Members");
			lbl_noMember.setBackground(Color.WHITE);
			lbl_noMember.setFont(new Font("Calibri",Font.ITALIC,20));
		}
		contentPane.setVisible(false);
		contentPane.setVisible(true);
	}
	
	/**
	 * @return the ItemContacts, which are selected
	 */
	private ArrayList<Item_Contact> getSelectedICs(){
		ArrayList<Item_Contact> ret=new ArrayList<Item_Contact>();
		for(Item_Contact ic:_ics){
			System.out.println(ic.getContact().getId());
			if(ic.isSelected())ret.add(ic);
		}
		return ret;
	}
	
	/**
	 * Add the selected Contacts to the Group
	 * @param g
	 */
	public void addSelectedCsToGroup(Group g) {
		for(Item_Contact c:getSelectedICs()){
			new AddContactToGroupActionListener(c.getContact(), g).actionPerformed(null);
		}
	}	

	/**
	 * ActionListener to change the Image
	 * @author eProtectioneers
	 */
	private class ChangeImageListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(pnl_image.getBtnChangePicture()))pnl_image.setPicturePath(readFilePath());
			contentPane.setVisible(false);
			contentPane.setVisible(true);
		}
		private String readFilePath(){
			File file=ChooseFile.getPictoRead();
			if(file!=null&&(new ImageIcon(file.getAbsolutePath()).getImage()) instanceof Image)return file.getAbsolutePath();
			else return pnl_image.getPicturePath();
		}
	}
	
	/**
	 * The listener of every PageItem
	 * @author eProtectioneers
	 */
	private class PiActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			int i=getSelectedPageItem();
			if(i!=entryfields.size()-1){
				entryfields.get(i+1).requestFocus();
			}
			else {
				btnSave.requestFocus();
				btnSave.doClick(100);
			}
		}
		/**
		 * @return the number of the selected PageItem
		 */
		private int getSelectedPageItem(){
			for(int i=0; i<entryfields.size();i++){
				if(entryfields.get(i).getTextField().isFocusOwner())return i;
			}
			return (Integer)null;
		}
	}
	
	/**
	 * save the new Group
	 */
	private void save(){
		Group g=new Group(DatabaseG.getNewIndex(), pi_groupname.getText(), pi_description.getText(), pnl_image.getPicturePath());
		DatabaseG.addGroup(g);
		addSelectedCsToGroup(g);
		JOptionPane.showMessageDialog(PPCA_PanaceaWindow.getFrame(), "Group added", "", JOptionPane.INFORMATION_MESSAGE, null);
	}

	/**
	 * ActionListener to save the Group
	 * @author eProtectioneers
	 */
	private class BtnSaveActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			Object options[]={"yes","no"};
			switch(JOptionPane.showOptionDialog(pag, "Do you really want to save this Group?", "Save new Group", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])){
				case JOptionPane.YES_OPTION:
					save();
					dispose();
					break;
				default:
					break;
			}			
		}
	}

	/**
	 * ActionListener to cancel
	 * @author eProtectioneers
	 */
	private class BtnCancelActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(lookForChanges()){
				Object options[]={"yes","no","cancel"};
				switch(JOptionPane.showOptionDialog(pag, "Do you want to save this Group?", "Save new Group", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])){
					case JOptionPane.YES_OPTION:
							save();
					  case JOptionPane.NO_OPTION:
						  dispose();
						  break;
					default:
						break;
				}	
			}else dispose();
		}
	}
	/**
	 * @return false if there are no changes
	 */
	private boolean lookForChanges(){
		if(!pnl_image.getPicturePath().equals(Group.getDefaultpicpath()))return true;
		for(PageItem_new pi:entryfields){
			if(!pi.getText().equals(""))return true;
		}
		return false;
	}
	
	
}