//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.components;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * A colored circle, which contains one single letter
 * @author eProtectioneers
 */
public class LetterCircle extends JPanel {

	private JLabel _lbl;
	int _diameter;
	boolean _border;
	
	/**
	 * Create the panel.
	 */
	public LetterCircle(char letter,int diameter,Color bg,Color fg,boolean border) {
		this._diameter=diameter;
		this._border=border;
		this._lbl=new JLabel(Character.toString(letter));
		_lbl.setBorder(new EmptyBorder(3, 0, 0, 0));
		_lbl.setForeground(fg);
		_lbl.setFont(new Font("Calibri",Font.BOLD,diameter-5));
		_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		_lbl.setVerticalAlignment(SwingConstants.CENTER);
		setForeground(fg);
		setBackground(bg);
		setOpaque(false);
		setLayout(new BorderLayout());
		add(_lbl,BorderLayout.CENTER);
		Dimension size=new Dimension(diameter,diameter);
		setMaximumSize(size);
		setMinimumSize(size);
		setPreferredSize(size);
	}
	
	 @Override
	 protected void paintComponent(Graphics g) {
	    g.setColor(getBackground());
	    g.fillOval(0, 0,_diameter-1,_diameter-1);
	 }
	  
	  @Override
	  protected void paintBorder(Graphics g) {
		  if(_border){
		    g.setColor(getForeground());
		    g.drawOval(0, 0,_diameter-1,_diameter-1);
		  }
	  }


}
