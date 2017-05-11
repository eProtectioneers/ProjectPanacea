package org.eprotectioneers.panacea.contactmanagement.components;

import java.awt.*;
import javax.swing.JPanel;

public class QuarterCirclePanel extends JPanel {

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
