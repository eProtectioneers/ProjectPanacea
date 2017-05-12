package org.eprotectioneers.panacea.contactmanagement.view;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;

import org.eprotectioneers.panacea.contactmanagement.components.ImagePanel;
import org.eprotectioneers.panacea.contactmanagement.models.DatabaseC;

public class TwitterPanel extends JPanel {
	private Color _bg_color;
	private JLabel lblTweet;
	private JLabel lblname;
	private JLabel lblShownname;
	private JTextArea txtarea_tweet;
	private ImagePanel imagePanel;
	private JScrollPane scrollPane;
	private ImageIcon _bg_ic;
	private URL _url;
	private static final String defaultpicpath=DatabaseC.getUrlPath(TwitterPanel.class.getResource("/org/eprotectioneers/panacea/contactmanagement/view/PPNCA_Images/default_profile_twitter.png").getPath()).substring(1);
	private String tooltipdescr="";
	
	public TwitterPanel(URL url,String picpath,String shownname,String name,String tweet,String bgpicpath){
		this(url,picpath,shownname,name,tweet,(Color)null);
		this._bg_ic=new ImageIcon(bgpicpath);
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public TwitterPanel(URL url,String picpath,String shownname,String name,String tweet,Color bgcolor) {
		Dimension size=new Dimension(265,185);
		setPreferredSize(size);
		setMinimumSize(size);
		setLayout(null);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setBackground(Color.WHITE);
		
		this._bg_color=bgcolor;
		this._url=url;
		
		lblShownname = new JLabel(shownname);
		lblShownname.setFont(new Font("Arial", Font.BOLD, 16));
		lblShownname.setBounds(83, 98, 151, 22);
		add(lblShownname);
		
		lblname = new JLabel(name);
		lblname.setFont(new Font("Arial", Font.PLAIN, 14));
		lblname.setForeground(new Color(113,127,141));
		lblname.setBounds(83, 115, 151, 22);
		add(lblname);
		
		lblTweet = new JLabel("TWEET");
		lblTweet.setFont(new Font("Arial", Font.PLAIN, 11));
		lblTweet.setBounds(10, 145, 47, 14);
		lblTweet.setForeground(new Color(113,127,141));
		add(lblTweet);
		
		txtarea_tweet = new JTextArea("TEST");
		txtarea_tweet.setFont(new Font("Arial", Font.PLAIN, 14));
		txtarea_tweet.setEditable(false);
		txtarea_tweet.setFocusable(false);
		txtarea_tweet.setLineWrap(true);
		txtarea_tweet.setWrapStyleWord(true);
		if(tweet.length()>255){
			tweet=tweet.substring(0, 252);
			tweet+="...";
			tooltipdescr="Read more at: ";
		}
		txtarea_tweet.setText(tweet);
		txtarea_tweet.setSize(249, 242);
		txtarea_tweet.setOpaque(false);
		
		scrollPane=new JScrollPane(txtarea_tweet);
		scrollPane.setBounds(8, 158,249,txtarea_tweet.getPreferredSize().height);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(null);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane);
		
		if(!new File(picpath).exists())picpath=defaultpicpath;
		imagePanel = new ImagePanel(picpath);
		imagePanel.setRadius(10);
		imagePanel.setBackground(Color.WHITE);
		imagePanel.setDisabled();
		imagePanel.setBounds(10, 65, 67, 67);
		add(imagePanel);
		setMaximumSize(new Dimension(265, (int)(158+scrollPane.getSize().getHeight())));
		
		addMouseListener(new TwitterPanelMouseListener());
		if(url!=null)setToolTipText(url.toString());
	}
	
	@Override
	public void addMouseListener(MouseListener l){
		super.addMouseListener(l);
		txtarea_tweet.addMouseListener(l);
		imagePanel.addMouseListener(l);
	}
	
	@Override
	public void setToolTipText(String text){
		text=tooltipdescr+text;

		super.setToolTipText(text);
		txtarea_tweet.setToolTipText(text);
		imagePanel.setToolTipText(text);
	}
	
	@Override
	public void paintComponent(Graphics g){		
		g.setColor(getBackground());
		g.fillRoundRect(0, 0,getWidth(),getHeight(),10,10);
		g.setClip(new RoundRectangle2D.Double(0, 0,getWidth(),getHeight(),10,10));
		if(_bg_color==null){
			g.drawImage(_bg_ic.getImage(),0,0,getWidth(),93,null);
		}else{
			g.setColor(_bg_color);
			g.fillRect(0, 0, getWidth(), 93);
		}	
	}
	
	private class TwitterPanelMouseListener extends MouseAdapter{

		@Override
		public void mouseClicked(MouseEvent e) {
			if(_url!=null){
				 try {
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+_url);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			Color c=new Color(244,248,251);
			setBackground(c);
			imagePanel.setBackground(c);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setBackground(Color.WHITE);
			imagePanel.setBackground(Color.WHITE);
		}		
	}
}
