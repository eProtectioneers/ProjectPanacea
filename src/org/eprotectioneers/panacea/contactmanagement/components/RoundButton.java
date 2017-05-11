package org.eprotectioneers.panacea.contactmanagement.components;

import java.awt.*;
import javax.swing.JButton;

public class RoundButton extends JButton {
	
	  public RoundButton(String text, Dimension prefsize) {
		  this(text);
		  this.setPreferredSize(prefsize);
	  }
	  
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
