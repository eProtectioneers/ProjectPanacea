//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.view;

import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

/**
 * A reworked version of the PageItem, without edit Buttons
 * @author eProtectioneers
 */
public class PageItem_new extends JPanel {
	
	/**
	 * The page, the pi will be in
	 */
	private JPanel _page;
	/**
	 * The Item's textField
	 */
	private JTextField textField;
	/**
	 * The standard color
	 */
	private static Color colornormal=SystemColor.controlShadow;
	/**
	 * The Color, which is shown, when the textField is focused
	 */
	private static Color colorfocused=Color.WHITE;
	/**
	 * The standard font color
	 */
	private static Color fontcolor1=Color.BLACK;
	/**
	 * The font color of a focused PageItem
	 */
	private static Color fontcolor2=fontcolor1;
	/**
	 * The description label
	 */
	private JLabel lbl_itemdescription;

	/**
	 * Sets the given text as the textfield's text
	 * @param text
	 */
	public void setText(String text){
		if(text==null)text="";
		textField.setText(text);
	}
	
	/**
	 * @return the currently text of the textfield
	 */
	public String getText() {
		if(textField.getText()==null)textField.setText("");
		return textField.getText();
	}
	
	/**
	 * @return the item's textfield
	 */
	public JTextField getTextField() {
		return textField;
	}
	
	/**
	 * Create the panel.
	 * @param string 
	 */
	public PageItem_new(String itemtype, JPanel page){
		if(!itemtype.equals(""))itemtype+=": ";
		lbl_itemdescription = new JLabel(itemtype);
		lbl_itemdescription.setForeground(fontcolor2);
		lbl_itemdescription.setFont(new Font("Calibri Light", Font.PLAIN, 14));
		add(lbl_itemdescription,0);
		setBorder(new EmptyBorder(2, 3, 2, 3));
		this._page=page;
		setBackground(colornormal);
		this.setMaximumSize(new Dimension(1000,25));
		
		textField = new JTextField();
		textField.addFocusListener(new TextFieldFocusListener());
		textField.setOpaque(false);
		textField.setBorder(new EmptyBorder(0, 0, 0, 2));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		textField.setFont(new Font("Calibri", Font.PLAIN, 16));
		textField.setForeground(fontcolor1);
		add(textField);
		textField.setColumns(10);
	}
	
	
	@Override
	protected void paintComponent(Graphics g){
		g.setColor(this.getBackground());
		g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 15, 15);
		g.setClip(new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), 15, 15));
	}
	
	@Override
	public void requestFocus(){
		this.textField.requestFocus();
	}
	
	/**
	 * Reloads the page, the item is in, in order to make it visible in his full shape (Swing-Bug)
	 */
	private void reload(){
		_page.setVisible(false);
		_page.setVisible(true);	
	}

	/**
	 * @author eProtectioneers
	 */
	private class TextFieldFocusListener extends FocusAdapter {
		@Override
		public void focusGained(FocusEvent arg0) {
			setBackground(colorfocused);
			textField.selectAll();
		}
		@Override
		public void focusLost(FocusEvent arg0) {
			setBackground(colornormal);
			reload();
		}
	}
}
