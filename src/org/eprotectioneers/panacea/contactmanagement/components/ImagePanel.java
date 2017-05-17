package org.eprotectioneers.panacea.contactmanagement.components;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.eprotectioneers.panacea.contactmanagement.models.Contact;

import java.awt.event.*;

public class ImagePanel extends JPanel{
	private GroupLayout gl_pnl_image;
	private JButton btn_changepicture;
	private String _picturepath;
	private final String _defaultpicturepath;	
	private JPopupMenu popupMenu;
	private JMenuItem mntmChangePicture;
	private JMenuItem mntmRemovePicture;
	private static ImageIcon ic_add;
	private static ImageIcon ic_add_pressed;
	private static ImageIcon ic_add_rollover;
	private double _radius=30;
	private boolean disabled=false;
	
	/**
	 * @return the btn_changepicture
	 */
	public JButton getBtnChangePicture() {
		return btn_changepicture;
	}
	
	public void setDisabled(){
		disabled=true;
		btn_changepicture.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		btn_changepicture.setVisible(false);
		this.setToolTipText("");
	}
	/**
	 * @return the mntmRemovePicture
	 */
	public JMenuItem getMntmRemovePicture() {
		return mntmRemovePicture;
	}
	
	public String getPicturePath() {
		return this._picturepath;
	}

	public void setPicturePath(String picturepath) {
		if(picturepath==null||picturepath.equals(""))this._picturepath=_defaultpicturepath;
		else this._picturepath = picturepath;
	}

	public void setRadius(double radius){
		this._radius=radius;
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public ImagePanel(String picture, String defaultpicturepath){
		this._picturepath=picture;
		this._defaultpicturepath=defaultpicturepath;
		initialize();
	}
	private void initialize() {
		
		popupMenu = new JPopupMenu();
		addPopup(this, popupMenu);
		
		mntmChangePicture = new JMenuItem("Change picture");
		mntmChangePicture.addActionListener(new MntmChangePictureActionListener());
		popupMenu.add(mntmChangePicture);
		
		mntmRemovePicture = new JMenuItem("Remove picture");
		popupMenu.add(mntmRemovePicture);
		
		ic_add=new ImageIcon("images/icon_plus_white.png");
		ic_add.setImage(ic_add.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		
		ic_add_pressed=new ImageIcon("images/icon_plus_black.png");
		ic_add_pressed.setImage(ic_add_pressed.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		
		ic_add_rollover=new ImageIcon("images/icon_plus_gray.png");
		ic_add_rollover.setImage(ic_add_rollover.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		
		btn_changepicture = new RoundRectangleButton("",20);
		btn_changepicture.setToolTipText("Change picture");
		btn_changepicture.setForeground(Color.WHITE);
		btn_changepicture.setIcon(ic_add);
		btn_changepicture.setPressedIcon(ic_add_pressed);
		btn_changepicture.setRolloverIcon(ic_add_rollover);
		btn_changepicture.setBackground(Color.BLACK);
		btn_changepicture.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		this.setLayout(null);
	}
	
	public ImagePanel(String picturepath){
		this(picturepath,picturepath);
	}

	@Override
	public void paintComponent(Graphics g){	
		if(!disabled){
			if(_picturepath.equals(_defaultpicturepath))setToolTipText("No Profile Picture");
			else setToolTipText(_picturepath);
		}
		
		if(this.getWidth()>this.getHeight()){
			setSize(new Dimension(this.getHeight(),this.getHeight()));
		}
		else{
			setSize(new Dimension(this.getWidth(),this.getWidth()));
		}
		
		g.setColor(getBackground());
		g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(),(int)_radius,(int)_radius);
		g.setClip(new RoundRectangle2D.Double(2,2,this.getWidth()-4,this.getHeight()-4,_radius,_radius));
		super.paintComponent(g);
		g.setClip(new RoundRectangle2D.Double(2,2,this.getWidth()-4,this.getHeight()-4,_radius,_radius));
		g.drawImage(new ImageIcon(_picturepath).getImage(), 1, 1, this.getWidth()-2,this.getHeight()-2,null);
		
		btn_changepicture.setBounds(getWidth()-33, getHeight()-33,30,30);
		add(btn_changepicture);
	}
	
	private void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()&&!disabled) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()&&!disabled) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
				popup.setVisible(true);
			}
		});
	}
	
	private class MntmChangePictureActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			btn_changepicture.requestFocus();
			btn_changepicture.doClick();
		}
	}
}
