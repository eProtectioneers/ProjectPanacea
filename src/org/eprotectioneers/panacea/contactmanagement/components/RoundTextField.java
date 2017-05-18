//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.components;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.JTextField;

/**
 * A unused TextField with a round side
 * @author eProtectioneers
 */
@Deprecated
public class RoundTextField extends JTextField {	    

	/**
	 * Constructor, assigns
	 * @param text
	 */
	public RoundTextField(String text){
		super(text);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setClip(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
		super.paintComponent(g);
	}
}
