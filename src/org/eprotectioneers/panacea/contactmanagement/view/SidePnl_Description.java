package org.eprotectioneers.panacea.contactmanagement.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.eprotectioneers.panacea.contactmanagement.components.QuarterCirclePanel.CircularSector;

public class SidePnl_Description extends JPanel {
	
	private static org.eprotectioneers.panacea.contactmanagement.components.QuarterCirclePanel quarterCirclePanel;
	private static org.eprotectioneers.panacea.contactmanagement.components.QuarterCirclePanel quarterCirclePanel_1;
	private static JLabel lbl_text;
	private String _text;
	
	public void setText(String text){
		this._text=text;
		lbl_text.setText(getHtmlText());
	}
	
	/**
	 * Create the panel.
	 */
	public SidePnl_Description(String text) {
		this._text=text;
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setOpaque(false);
		setPreferredSize(new Dimension(59, 307));
		setBackground(Color.WHITE);
		setAlignmentY(Component.TOP_ALIGNMENT);
		setMinimumSize(new Dimension(30, 34));
		setMaximumSize(new Dimension(30, 250));
		setLayout(new BorderLayout(0, 0));
		inzialize();
	}
	
	private String getHtmlText(){
		String s="<HTML>";
		char c[]=_text.toCharArray();
		
		for(int i=0;i<c.length-1;i++){
			s+=c[i]+"<br>";
		}
		s+=c[c.length-1]+"</HTML>";
		
		return s;
	}

	private void inzialize(){
		lbl_text = new JLabel(getHtmlText());
		lbl_text.setForeground(Color.WHITE);
		lbl_text.setBorder(new EmptyBorder(0, 11, 20, 0));
		lbl_text.setBackground(Color.BLACK);
		lbl_text.setOpaque(true);
		lbl_text.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl_text.setHorizontalAlignment(SwingConstants.LEFT);
		add(lbl_text);
		
		quarterCirclePanel = new org.eprotectioneers.panacea.contactmanagement.components.QuarterCirclePanel(CircularSector.SECOND);
		quarterCirclePanel.setBackground(Color.BLACK);
		quarterCirclePanel.setPreferredSize(new Dimension(30, 27));
		add(quarterCirclePanel, BorderLayout.NORTH);
		quarterCirclePanel.setMinimumSize(new Dimension(30, 100));
		
		quarterCirclePanel_1 = new org.eprotectioneers.panacea.contactmanagement.components.QuarterCirclePanel(CircularSector.THIRD);
		quarterCirclePanel_1.setBackground(Color.BLACK);
		quarterCirclePanel_1.setPreferredSize(new Dimension(30, 40));
		quarterCirclePanel_1.setMinimumSize(new Dimension(30, 100));
		add(quarterCirclePanel_1, BorderLayout.SOUTH);
	}
}
