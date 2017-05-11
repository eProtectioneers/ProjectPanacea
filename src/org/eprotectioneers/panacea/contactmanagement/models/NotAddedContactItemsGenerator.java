package org.eprotectioneers.panacea.contactmanagement.models;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.eprotectioneers.panacea.contactmanagement.view.Item_Contact;
import org.eprotectioneers.panacea.contactmanagement.view.Item_Object;
import org.eprotectioneers.panacea.contactmanagement.view.Page_Group;


public class NotAddedContactItemsGenerator implements Runnable{
		private ArrayList<Integer> ids_contacts=new ArrayList<Integer>();
		private ArrayList<Integer> _ids_notadded=new ArrayList<Integer>();
		private ArrayList<Item_Contact> _ics_notadded=new ArrayList<Item_Contact>();
		private Group _g;
		private Page_Group _pg;
		private JPopupMenu item_selectedPopup;
		
		public Item_Contact getNotAddedItem_contact(int id){
			for(Item_Contact ic:_ics_notadded){
				if(ic.getContact().getId()==id)return ic;
			}
			return null;
		}
		
		public ArrayList<Item_Contact> getICs_NotAdded(){
			return _ics_notadded;
		}
		
		public NotAddedContactItemsGenerator(Group g, Page_Group pg){
			this._g=g;
			this._pg=pg;
			for(Contact c:DatabaseC.getContacts())ids_contacts.add(c.getId());
			item_selectedPopup=generateSelectedPopopMenu();
		}
		
		@Override
		public void run() {
			generateNotAddedICs();
		}
		
		public void generateNotAddedICs(){
			ArrayList<Integer> ids_group=DatabaseCG.getContacts(_g);
			ArrayList<Contact> contacts=DatabaseC.getContacts(ids_contacts);
			ArrayList<Integer> rem_ids=new ArrayList<Integer>();
			for(int id:ids_group){
				if(_ids_notadded.contains(id)){
					for(Item_Contact ic:_ics_notadded){
						if(ic.getContact().getId()==id){_ics_notadded.remove(ic);break;}
					}
					rem_ids.add(id);
				}
			}
			_ids_notadded.removeAll(rem_ids);
			Item_Object.setHidePopup(true);
			for(Contact c:contacts){
				if(!ids_group.contains(c.getId())&&!_ids_notadded.contains(c.getId())){
					ArrayList<Integer> ids=new ArrayList<Integer>();
					ids.add(c.getId());
					Item_Contact ic=new Item_Contact(c);
					ic.setSelectedPopup(item_selectedPopup);
					ic.setBackground(Color.gray);
					ic.setForeground(Color.WHITE);
					ic.setSelectable(false);
					_ics_notadded.add(ic);
					_ids_notadded.add(c.getId());
				}
			}
			Item_Object.setHidePopup(false);
		}
		
		private JPopupMenu generateSelectedPopopMenu(){
			item_selectedPopup=new JPopupMenu();
			JMenuItem mntm_add=new JMenuItem("Add selected Contacts to Group");
			mntm_add.addActionListener(_pg.new AddSelectedContactsActionListener());
			item_selectedPopup.add(mntm_add);
			return item_selectedPopup;
		}
	}