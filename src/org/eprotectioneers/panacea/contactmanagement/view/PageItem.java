//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.view;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

/**
 * A Item with a round rectangle shape, to display a text and edit it
 * @author eProtectioneers
 */
public class PageItem extends JPanel {
	private JPanel _page;
	private JTextField textField;
	private String text;
	private JButton btnEdit;
	private JButton btnSave;
	private JButton btnReject;
	private static boolean saving=false;
	private static Color colornormal=Color.BLACK;
	private static Color colorfocused=SystemColor.controlShadow;
	private static Color fontcolor1=Color.WHITE;
	private static Color fontcolor2=new Color(230,230,230);
	private ImageIcon ic_edit=new ImageIcon("images/icon_edit.png");
	private ImageIcon ic_save=new ImageIcon("images/icon_accept.png");
	private ImageIcon ic_reject=new ImageIcon("images/icon_deny.png");
	private JLabel lbl_itemdescription;
	
	/**
	 * @return the saving button
	 */
	public JButton getBtnSave() {
		return btnSave;
	}
	
	/**
	 * Set the
	 * @param text
	 */
	public void setText(String text){
		if(text==null)text="";
		setToolTipText(text);
		this.text=text;
		textField.setText(text);
	}
	
	/**
	 * @return the text
	 */
	public String getText() {
		if(text==null)setText("");
		return text;
	}
	
	@Override 
	public void setToolTipText(String text){
		super.setToolTipText(text);
		textField.setToolTipText(text);
	}
	
	/**
	 * @return the textField
	 */
	public JTextField getTextField() {
		return textField;
	}
	
	/**
	 * Constructor, assigns
	 * @param text
	 * @param itemtype
	 * @param bgcolor
	 * @param page
	 */
	public PageItem(String text, String itemtype, Color bgcolor,JPanel page){
		this(text,itemtype,page);
		setBackground(bgcolor);
	}

	/**
	 * Constructor, assigns
	 * @param text
	 * @param itemtype
	 * @param page
	 */
	public PageItem(String text, String itemtype,JPanel page){
		this(text,page);
		setToolTipText(text);
		lbl_itemdescription = new JLabel(itemtype+": ");
		lbl_itemdescription.setForeground(fontcolor2);
		lbl_itemdescription.setFont(new Font("Calibri Light", Font.PLAIN, 14));
		add(lbl_itemdescription,0);
	}

	/**
	 * Constructor, assigns
	 * @param text
	 * @param page
	 */
	public PageItem(String text,JPanel page) {
		setBorder(new EmptyBorder(2, 3, 2, 2));
		this.text=text;
		this._page=page;
		setBackground(SystemColor.controlShadow);
		this.setMaximumSize(new Dimension(100000,25));
		
		textField = new JTextField(this.text);
		textField.addKeyListener(new TextFieldKeyListener());
		textField.setOpaque(false);
		textField.setBorder(new EmptyBorder(0, 0, 0, 2));
		setItemstate(false);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		textField.setFont(new Font("Calibri", Font.PLAIN, 16));
		textField.setForeground(fontcolor1);
		add(textField);
		textField.setColumns(10);
		
		btnEdit = new org.eprotectioneers.panacea.contactmanagement.components.RoundButton("",new Dimension(21,21));
		btnEdit.setFocusPainted(false);
		btnEdit.setBackground(Color.WHITE);
		ic_edit.setImage(ic_edit.getImage().getScaledInstance(17,18,Image.SCALE_SMOOTH));
		add(btnEdit);
		btnEdit.setIcon(ic_edit);
		btnEdit.addActionListener(new BtnEditActionListener());
		btnEdit.setToolTipText("edit");
		btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnEdit.setMaximumSize(new Dimension(21,21));
		btnEdit.setMinimumSize(new Dimension(21,21));
		
		btnSave = new org.eprotectioneers.panacea.contactmanagement.components.RoundButton("",new Dimension(21,21));
		btnSave.setFocusPainted(false);
		btnSave.setBackground(Color.WHITE);
		btnSave.setBorder(null);
		ic_save.setImage(ic_save.getImage().getScaledInstance(21,21,Image.SCALE_SMOOTH));
		btnSave.setIcon(ic_save);
		btnSave.addActionListener(new BtnSaveActionListener());
		btnSave.setToolTipText("save");
		btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		btnReject = new org.eprotectioneers.panacea.contactmanagement.components.RoundButton("",new Dimension(21,21));
		btnReject.setFocusPainted(false);
		btnReject.setBackground(Color.WHITE);
		btnReject.setBorder(null);
		ic_reject.setImage(ic_reject.getImage().getScaledInstance(21,21,Image.SCALE_SMOOTH));
		btnReject.setIcon(ic_reject);
		btnReject.addActionListener(new BtnRejectActionListener());
		btnReject.setToolTipText("reject");
		btnReject.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	@Override
	protected void paintComponent(Graphics g){
		g.setColor(this.getBackground());
		g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 15, 15);
	}
	
	/**
	 * Set the itemstate
	 * @param state
	 */
	private void setItemstate(boolean state){
		textField.setEditable(state);
		if(state)setBackground(colorfocused);
		else setBackground(colornormal);
	}
	
	/**
	 * finish the entry
	 */
	private void finishEntry(){
		remove(btnSave);
		remove(btnReject);
		add(btnEdit);
		textField.nextFocus();
		reload();
		this.requestFocus();
	}
	
	/**
	 * reload the parent, to visualize it correct
	 */
	private void reload(){
		_page.repaint();
		_page.setVisible(false);
		_page.setVisible(true);	
	}
	
	/**
	 * save the text
	 */
	public void save(){
		setItemstate(false);
		setText(textField.getText());
		finishEntry();
	}
	
	/**
	 * reject the changes
	 */
	public void reject(){
		setItemstate(false);
		textField.setText(text);
		finishEntry();
	}

	/**
	 * @return if the PageItem is saving
	 */
	public static boolean isSaving() {
		return saving;
	}

	/**
	 * ActionListener to edit the text
	 * @author eProtectioneers
	 */
	private class BtnEditActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setItemstate(true);
			textField.requestFocus();
			textField.selectAll();
			remove(btnEdit);
			add(btnSave);
			add(btnReject);	
			reload();
		}
	}

	/**
	 * ActionListener to edit save the changes
	 * @author eProtectioneers
	 */
	private class BtnSaveActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			saving=true;
			save();
			saving=false;
		}
	}

	/**
	 * ActionListener to reject the changes
	 * @author eProtectioneers
	 */
	private class BtnRejectActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			reject();
		}
	}

	/**
	 * KeyListener to replace the Buttons
	 * @author eProtectioneers
	 */
	private class TextFieldKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				btnSave.requestFocus();
				btnSave.doClick(200);
			}
			if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
				btnReject.requestFocus();
				btnReject.doClick();
			}
		}
	}
}
