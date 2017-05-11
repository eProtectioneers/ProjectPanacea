package org.eprotectioneers.panacea.contactmanagement.components;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.JTextField;

public class RoundTextField extends JTextField {	    
	        
	public RoundTextField(String text){
		super(text);
	}
    @Override
    protected void paintComponent(Graphics g) {
        g.setClip(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
        super.paintComponent(g);
    }
}
