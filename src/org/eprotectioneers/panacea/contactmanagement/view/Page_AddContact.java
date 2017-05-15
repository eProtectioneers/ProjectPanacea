package org.eprotectioneers.panacea.contactmanagement.view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.eprotectioneers.panacea.contactmanagement.components.ImagePanel;
import org.eprotectioneers.panacea.contactmanagement.components.RoundRectangleButton;
import org.eprotectioneers.panacea.contactmanagement.models.ChooseFile;
import org.eprotectioneers.panacea.contactmanagement.models.Contact;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseC;
import org.eprotectioneers.panacea.contactmanagement.models.EmailValidator;
import org.eprotectioneers.panacea.userinterface.PPCA_PanaceaWindow;

import net.miginfocom.swing.MigLayout;

public class Page_AddContact extends JFrame {

	private JPanel contentPane;
	private ImagePanel pnl_image;
	private PageItem_new pi_shownname;
	private PageItem_new pi_firstname;
	private PageItem_new pi_lastname;
	private PageItem_new pi_emailaddress;
	private PageItem_new pi_phonenumber;
	private PageItem_new pi_address;
	private Page_AddContact pac=this;

	/**
	 * A list, which contains every pageitem 
	 */
	private ArrayList<PageItem_new> entryfields=new ArrayList<PageItem_new>();
	/**
	 * The button, which saves a new contact
	 */
	private JButton btnSave;
	private JButton btnCancel;
	
	/**
	 * Create the panel.
	 */
	public Page_AddContact(Component component) {
		super("New Contact");
		Point componentLocation = component.getLocation();
		Dimension componentDimension=component.getSize();
		this.setSize((int) (componentDimension.width-componentDimension.width/3),
				(int) (componentDimension.height-componentDimension.height/3));
		this.setLocation((int) (componentLocation.x + component.getWidth()/2-this.getWidth()/2),
				(int)(componentLocation.y + component.getHeight()/2-this.getHeight()/2));
		inizialize();
	}
	
	public Page_AddContact(Component component,String shownname,String emailaddress) {
		this(component);
		Object options[]={"yes","no"};
		int i=0;
		for(Contact c:DatabaseC.getContacts()){
			if(c.getEmailaddress().toLowerCase().equals(emailaddress.toLowerCase())){
				i=JOptionPane.showOptionDialog(PPCA_PanaceaWindow.getFrame(), "There's already a Contact with this Email-Address. Do you want to continue?", "Contact already exists",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,options,options[1]);
				break;
			}
		}
		switch(i){
			case JOptionPane.YES_OPTION:
				pi_shownname.setText(shownname);
				pi_emailaddress.setText(emailaddress);
				break;
			default:
				this.dispose();
				break;
		}
		
	}

	
	public void inizialize(){
		contentPane = new JPanel();

		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		contentPane.setLayout(new MigLayout("", "[5%][100px:25%:300px][40][30%,grow,fill][40.00][30%,grow,fill][5%]", "[15.00][25px:11%:75px][25px:11%:75px][25px:11%:75px][25px:11%:75px][25px:11%:75px][5.50%][11%][11%][15]"));
		
		pnl_image = new ImagePanel(Contact.getDefaultpicpath());
		pnl_image.setBackground(Color.BLACK);
		contentPane.add(pnl_image, "cell 1 1 1 5,grow");
		ChangeImageListener cil=new ChangeImageListener();
		pnl_image.getBtnChangePicture().addActionListener(cil);
		pnl_image.getMntmRemovePicture().addActionListener(cil);
		
		pi_shownname = new PageItem_new("Shown Name",contentPane);
		entryfields.add(pi_shownname);	
		
		pi_emailaddress = new PageItem_new("Email", contentPane);
		entryfields.add(pi_emailaddress);
		
		pi_firstname = new PageItem_new("Firstname",contentPane);
		entryfields.add(pi_firstname);	
		
		pi_lastname = new PageItem_new("Lastname", contentPane);
		entryfields.add(pi_lastname);

		pi_phonenumber = new PageItem_new("Phonenumber", contentPane);
		entryfields.add(pi_phonenumber);

		pi_address = new PageItem_new("Address", contentPane);
		entryfields.add(pi_address);

		for(PageItem_new pi:entryfields){
			pi.getTextField().setColumns(10);
			pi.getTextField().addActionListener(new PiActionListener());
		}
		
		contentPane.add(pi_shownname, "cell 3 2,grow");
		contentPane.add(pi_emailaddress, "cell 3 3 3 1,grow");
		contentPane.add(pi_firstname, "cell 3 4,grow");
		contentPane.add(pi_lastname, "cell 3 5,grow");
		contentPane.add(pi_phonenumber, "cell 5 4,grow");
		contentPane.add(pi_address, "cell 5 5,grow");
		
		btnSave = new RoundRectangleButton("Save",15);
		btnSave.setMaximumSize(new Dimension(75, 25));
		btnSave.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnSave.setBackground(Color.WHITE);
		btnSave.addActionListener(new BtnSaveActionListener());
		
		btnCancel = new RoundRectangleButton("Cancel", 15);
		btnCancel.setMaximumSize(new Dimension(70, 23));
		btnCancel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnCancel.setBackground(Color.WHITE);
		btnCancel.addActionListener(new BtnCancelActionListener());
		
		contentPane.add(btnCancel, "cell 3 8");
		contentPane.add(btnSave, "cell 5 8,alignx right");
	}
	
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
	 * 
	 * @author Simon Senoner
	 * @version 1.0
	 * The listener of every pageItem
	 *
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
	
	private void save(){
		Contact c=new Contact(DatabaseC.getNewIndex(), pi_shownname.getText(), pi_firstname.getText(), 
				pi_lastname.getText(), pi_emailaddress.getText(), pi_phonenumber.getText(), pi_address.getText(), 
				pnl_image.getPicturePath(), false);
		DatabaseC.addContact(c);
		JOptionPane.showMessageDialog(PPCA_PanaceaWindow.getFrame(), "Contact added", "", JOptionPane.INFORMATION_MESSAGE, null);
	}
	
	private class BtnSaveActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!new EmailValidator().validate(pi_emailaddress.getText())){
				JOptionPane.showMessageDialog(pac, "Please enter a valid Email-Address","", JOptionPane.ERROR_MESSAGE);
				pi_emailaddress.requestFocus();
			}
			else{
				Object options[]={"yes","no"};
				switch(JOptionPane.showOptionDialog(pac, "Do you really want to save this Contact?", "Save new Contact", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])){
					case JOptionPane.YES_OPTION:
						save();
						dispose();
						break;
					default:
						break;
				}	
			}
		}
	}
	
	private class BtnCancelActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object options[]={"yes","no","cancel"};
			if(lookForChanges()){
				switch(JOptionPane.showOptionDialog(pac, "Do you want to save this Contact?", "Save new Contact", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])){
					case JOptionPane.YES_OPTION:
						if(!new EmailValidator().validate(pi_emailaddress.getText())){
							JOptionPane.showMessageDialog(pac, "Please enter a valid Email-Address","", JOptionPane.ERROR_MESSAGE);
							pi_emailaddress.requestFocus();
							break;
						}
						save();
					case JOptionPane.NO_OPTION:
						dispose();
					default:
						break;
				}
			}else dispose();
		}
		
		private boolean lookForChanges(){
			if(!pnl_image.getPicturePath().equals(Contact.getDefaultpicpath()))return true;
			for(PageItem_new pi:entryfields){
				if(!pi.getText().equals(""))return true;
			}
			return false;
		}
	}
}
