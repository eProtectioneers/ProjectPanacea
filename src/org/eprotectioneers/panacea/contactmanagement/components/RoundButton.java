//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.components;

import java.awt.*;
import javax.swing.JButton;

/**
 * A round Button
 * @author eProtectioneers
 */
public class RoundButton extends JButton {
	
	/**
	 * Constructor, assigns the
	 * @param text
	 * @param prefsize
	 */
	public RoundButton(String text, Dimension prefsize) {
		this(text);
		this.setPreferredSize(prefsize);
	}

	/**
	 * Constructor, assigns the
	 * @param text
	 */
	public RoundButton(String text) {
		super(text);
	  	super.setFocusPainted(false);
	    setContentAreaFilled(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (getModel().isArmed()) g.setColor(Color.lightGray);
		else g.setColor(getBackground());
	  	g.fillOval(0, 0,getWidth()-1,getHeight()-1);
	
	  	super.paintComponent(g);
	}
	
	@Override
	protected void paintBorder(Graphics g) {
		g.setColor(getForeground());
		g.drawOval(0, 0,getWidth()-1,getHeight()-1);
	}
}
