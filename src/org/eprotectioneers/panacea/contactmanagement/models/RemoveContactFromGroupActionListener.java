package org.eprotectioneers.panacea.contactmanagement.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import org.eprotectioneers.panacea.contactmanagement.view.Item_Object;
import org.eprotectioneers.panacea.userinterface.PPCA_PanaceaWindow;

public class RemoveContactFromGroupActionListener implements ActionListener {

	private Group _g;
	private Contact _c;
	private static boolean finished=true;
	private boolean _notification=false;
	private Item_Object _io=null;
	
	/**
	 * @return if it is finished
	 */
	public static boolean isFinished() {
		return finished;
	}
	
	public RemoveContactFromGroupActionListener(Contact c,Group g){
		this._g=g;
		this._c=c;
	}
	public RemoveContactFromGroupActionListener(Contact c,Group g,boolean notification){
		this(c,g);
		this._notification=notification;
	}
	public RemoveContactFromGroupActionListener(Contact c,Group g,boolean notification,Item_Object io){
		this(c,g);
		this._notification=notification;
		this._io=io;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		finished=false;
		DatabaseCG.RemoveContactFromGroup(_c, _g);
		finished=true;
		if(_notification)JOptionPane.showMessageDialog(PPCA_PanaceaWindow.getFrame(), "Contact '"+_c.getShownname()+"' removed from Group '"+_g.getName()+"'",
				"Removed", JOptionPane.INFORMATION_MESSAGE, null);
		if(_io!=null)_io.setPUOOGenerated(false);
	}
	
}
