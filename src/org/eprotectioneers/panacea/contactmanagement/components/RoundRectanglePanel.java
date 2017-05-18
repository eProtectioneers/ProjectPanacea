//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.components;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * A panel with a round rectangle shape
 * @author eProtectioneers
 */
public class RoundRectanglePanel extends JPanel {
	private int _radius;  
	
	/**
	 * Constructor, assigns
	 * @param radius
	 */
	public RoundRectanglePanel(int radius) {
		this._radius=radius;
	  }

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRoundRect(0, 0,getWidth()-1,getHeight()-1,_radius,_radius);
	}
}
