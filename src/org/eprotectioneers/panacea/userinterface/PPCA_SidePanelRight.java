package org.eprotectioneers.panacea.userinterface;

import javax.swing.JPanel;
import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.List;

import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.Dimension;

import twitter4j.*;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.ScrollPaneConstants;
import java.awt.Component;

/**
 * Old right Sidebar to show Twitter feeds (Jpanel)(Relic)
 * @author eProtectioneers
 */
@Deprecated
public class PPCA_SidePanelRight extends JPanel {

	PPCA_PanaceaWindow window;
	JTextPane twitterText = new JTextPane();
	/**
	 * Create the panel.
	 */
	public PPCA_SidePanelRight(PPCA_PanaceaWindow wnd) {
		setBorder(null);
		setAlignmentY(0.0f);
		setAlignmentX(0.0f);
		setBackground(new Color(255, 127, 80));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setAlignmentY(0.0f);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setBackground(new Color(255, 99, 71));
		add(panel,BorderLayout.WEST);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setMinimumSize(new Dimension(290, 40));
		scrollPane.setPreferredSize(new Dimension(290, 2));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		add(scrollPane,BorderLayout.EAST);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 127, 80));
		panel_1.setBorder(null);
		scrollPane.setViewportView(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon("images/twitter_tweet_eProt.png"));
		panel_1.add(label_1);
		
		JLabel label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon("images/twitter_tweet_eProt2.png"));
		panel_1.add(label_2);
		
		JLabel lblPartnerNews = new JLabel("Partner news:");
		lblPartnerNews.setFont(new Font("Tahoma", Font.PLAIN, 9));
		panel_1.add(lblPartnerNews);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("images/twitter_scntl_tweet.png"));
		label.setFont(new Font("Tahoma", Font.PLAIN, 9));
		panel_1.add(label);
		
		JLabel lblAdvertisement = new JLabel("Advertisement:");
		lblAdvertisement.setFont(new Font("Tahoma", Font.PLAIN, 9));
		panel_1.add(lblAdvertisement);
		
		JLabel label_3 = new JLabel("");
		label_3.setIcon(new ImageIcon("images/advertisement_pex.png"));
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 9));
		panel_1.add(label_3);
		
	}
	

}
