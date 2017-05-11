package org.eprotectioneers.panacea.contactmanagement.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class AddContactToGroupActionListener implements ActionListener {
	
	private Group _g;
	private Contact _c;
	private static boolean finished=true;
	private boolean _notification=false;

	/**
	 * @return if it is finished
	 */
	public static boolean isFinished() {
		return finished;
	}
	
	public AddContactToGroupActionListener(Contact c,Group g){
		this._g=g;
		this._c=c;
	}
	public AddContactToGroupActionListener(Contact c,Group g,boolean notification){
		this(c,g);
		this._notification=notification;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		finished=false;
		DatabaseCG.AddContactToGroup(_c, _g);
		finished=true;
		if(_notification)JOptionPane.showMessageDialog(null, "Contact '"+_c.getShownname()+"' added to Group '"+_g.getName()+"'",
				"Added", JOptionPane.INFORMATION_MESSAGE, null);
	}

}
