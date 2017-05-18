//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.components;

import java.awt.*;
import javax.swing.JPanel;

/**
 * a Panel, which shows a colored quarter circle
 * @author eProtectioneers
 */
@Deprecated
public class QuarterCirclePanel extends JPanel {

	/**
	 * enum, to decide, which circular sector the QuarterCirclePanel should display
	 * @author eProtectioneers
	 */
	public static enum CircularSector{
		FIRST,SECOND,THIRD,FOURTH
	}

	private CircularSector _sector=CircularSector.FIRST;
	
	/**
	 * Create the panel.
	 */
	public QuarterCirclePanel(CircularSector sector) {
		this._sector=sector;
		setOpaque(false);
	}

	@Override
	public void paintComponent(Graphics g){
		int x=0, y=0;
		switch(_sector){
		case FIRST: x=getWidth(); y=0;
			break;
		case SECOND: x=y=0;
			break;
		case THIRD: x=0; y=-getHeight();
			break;
		case FOURTH: x=getWidth(); y=getHeight();
			break;
		}
		g.setColor(getBackground());
		g.fillOval(x,y,2*getWidth(),2*getHeight());
	}
}
