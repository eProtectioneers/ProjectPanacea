//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.components;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

import org.eprotectioneers.panacea.contactmanagement.view.*;

/**
 * A searchfield, which only shows the Item_Objects in the Contactbar, which contain the text
 * @author eProtectioneers
 */
public class SearchField extends JPanel{
	private Contactbar _cbar;
	private JTextField textField;
	private String text;
	private ArrayList<Item_Object> _ios=new ArrayList<Item_Object>();
	private ArrayList<Item_Object> _ios_found=new ArrayList<Item_Object>();
	private boolean first=true;
	private boolean b=true;

	/**
	 * set, that this has gained the focus for the first time
	 */
	public void setFirst(){
		setEmptyTf();
		b=true;
		this.first=true;
	}
	@Override
	public boolean isFocusOwner(){
		return textField.isFocusOwner();
	}
	
	@Override
	public void requestFocus(){
		textField.requestFocus();
	}
	
	/**
	 * Sets the
	 * @param ics
	 */
	public void setICs(ArrayList<Item_Contact> ics){
		_ios=new ArrayList<Item_Object>();
		for(Item_Contact ic:ics){
			_ios.add(ic);
		}
	}
	
	/**
	 * Sets the
	 * @param igs
	 */
	public void setIGs(ArrayList<Item_Group> igs){
		_ios=new ArrayList<Item_Object>();
		for(Item_Group ig:igs){
			_ios.add(ig);
		}
	}

	/**
	 * Constructor, assigns
	 * @param cbar
	 */
	public SearchField(Contactbar cbar) {
		setBorder(new EmptyBorder(2, 3, 2, 3));
		this._cbar=cbar;
		this.setMaximumSize(new Dimension(100000,25));
		
		textField = new JTextField();
		setEmptyTf();
		textField.addFocusListener(new TextFieldFocusListener());
		textField.addKeyListener(new TextFieldKeyListener());
		textField.setBorder(new EmptyBorder(0, 0, 0, 2));
		textField.setToolTipText(text);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		add(textField);
		textField.setColumns(10);
	}

	@Override
	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 5, 5);
	}
	
	/**
	 * Set the textField to the empty state
	 */
	public final void setEmptyTf(){
		textField.setText("");
		textField.setFont(new Font("Calibri Light", Font.ITALIC, 14));
		textField.setForeground(Color.DARK_GRAY);
		textField.setText("  SEARCH:");
	}
	
	/**
	 * Reload the SearchFields view
	 */
	private void reload(){
		_cbar.setVisible(false);
		_cbar.setVisible(true);	
	}

	/**
	 * KeyListener of the SearchField's textField
	 * @author eProtectioneers
	 */
	private class TextFieldKeyListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_ESCAPE||e.getKeyCode()==KeyEvent.VK_ENTER){
				textField.nextFocus();
			}
			if(!(e.getKeyChar()==27||e.getKeyChar()==65535))
            {
				search();
            }
		}
	}
	
	/**
	 * FocusListener of the SearchField's textField
	 * @author eProtectioneers
	 */
	private class TextFieldFocusListener extends FocusAdapter {
		@Override
		public void focusGained(FocusEvent arg0) {
			if(b){
				textField.setText("");
				textField.setFont(new Font("Calibri", Font.PLAIN, 16));
				textField.setForeground(Color.BLACK);
				b=false;
			}	
		}
		@Override
		public void focusLost(FocusEvent e) {
			reload();
			if(textField.getText().equals("")){
				b=true;
				setEmptyTf();
			}
		}
	}
	
	/**
	 * Searching sequence
	 */
	private void search(){
		boolean isgroup;
		if(isgroup=(_cbar.getSelected()==org.eprotectioneers.panacea.contactmanagement.view.Contactbar.Selected.GROUP)){
			setIGs(_cbar.getIGs());
		}
		else{
			setICs(_cbar.getICs());
		}
		_ios.removeAll(_ios_found);
		if(first){_ios_found.addAll(_ios);first=false;}
		
		ArrayList<Item_Object> no_matches=new ArrayList<Item_Object>();
		for(Item_Object io:_ios_found){
			if(!io.getShownText().toLowerCase().contains(textField.getText().toLowerCase()))no_matches.add(io);
		}
		_ios_found.removeAll(no_matches);
		if(isgroup) _cbar.getIGs_visible().removeAll(no_matches);
		else _cbar.getICs_visible().removeAll(no_matches);
		
		for(Item_Object io:_ios){
			if(io.getShownText().toLowerCase().contains(textField.getText().toLowerCase())){
				_ios_found.add(io);
				if(isgroup){
					if(!_cbar.getIGs_visible().contains(io))_cbar.getIGs_visible().add((Item_Group)io);
				}else{
					if(!_cbar.getICs_visible().contains(io))_cbar.getICs_visible().add((Item_Contact)io);
				}
			}
		}

		_ios.removeAll(_ios_found);
		_cbar.addItems();
	}
}
