package org.eprotectioneers.panacea.contactmanagement.components;

import java.awt.Graphics;

import javax.swing.JPanel;

public class RoundRectanglePanel extends JPanel {
	private int _radius;  
	
	  public RoundRectanglePanel(int radius) {
	    this._radius=radius;
	  }
	  
	  @Override
	  protected void paintComponent(Graphics g) {
		  g.setColor(getBackground());
		  g.fillRoundRect(0, 0,getWidth()-1,getHeight()-1,_radius,_radius);
	  }

}
