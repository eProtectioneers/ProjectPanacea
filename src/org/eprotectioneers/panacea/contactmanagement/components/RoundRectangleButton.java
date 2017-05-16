//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.components;

import java.awt.*;
import javax.swing.JButton;
import javax.swing.JMenuItem;

/**
 * A button with a round rectangle shape
 * @author eProtectioneers
 */
public class RoundRectangleButton extends JButton {
	private int _radius;  
	
	/**
	 * Constructor, assigns
	 * @param text
	 * @param radius
	 */
	public RoundRectangleButton(String text, int radius) {
		super(text);
		this._radius=radius;
		setContentAreaFilled(false);
		setFocusPainted(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (getModel().isArmed())g.setColor(Color.BLACK);
		else g.setColor(getBackground());
		g.fillRoundRect(0, 0,getWidth()-1,getHeight()-1,_radius,_radius);
		super.paintComponent(g);
	}

	@Override
	protected void paintBorder(Graphics g) {
		g.setColor(getForeground());
		g.drawRoundRect(0, 0,getWidth()-1,getHeight()-1,_radius,_radius);
	}
}
