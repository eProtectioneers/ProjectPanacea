package org.eprotectioneers.panacea.contactmanagement.models;

import java.io.File;

import javax.swing.*;

import org.eprotectioneers.panacea.contactmanagement.control.ImageFilter;

public class ChooseFile {
	private static JFileChooser _fc=new JFileChooser();

	/**
	 * @return the file, you chose with the JFileChooser
	 */
	public static File getPictoRead(){
		_fc.resetChoosableFileFilters();
		_fc.addChoosableFileFilter(new ImageFilter());
		_fc.setAcceptAllFileFilterUsed(false);
		int i = _fc.showOpenDialog(null);
		if (i == JFileChooser.APPROVE_OPTION){
			return _fc.getSelectedFile();
		}
		return null;
	}

}
