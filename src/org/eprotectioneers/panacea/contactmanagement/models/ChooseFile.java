//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.*;

import org.eprotectioneers.panacea.contactmanagement.control.ImageFilter;

/**
 * A class, to store and use FileChoosers
 * @author eProtectioneers
 */
public class ChooseFile {
	private static JFileChooser _picfc=new JFileChooser();
	private static JFileChooser _keyfc=new JFileChooser();
	
	/**
	 * @return The FileChosser for the key-handling
	 */
	public static JFileChooser getKeyFileChooser(){
		return _keyfc;
	}
	
	/**
	 * @return the file, you chose with the JFileChooser
	 */
	public static File getPictoRead(){
		_picfc.setDialogTitle("Change Picture");

		_picfc.resetChoosableFileFilters();
		_picfc.addChoosableFileFilter(new ImageFilter());
		_picfc.setAcceptAllFileFilterUsed(false);
		int i = _picfc.showOpenDialog(null);
		if (i == JFileChooser.APPROVE_OPTION){
			FileChoosePathDatabase.saveFileChooser();
			return _picfc.getSelectedFile();
		}
		return null;
	}

	/**
	 * Database application to save and read the previous paths of the filechoosers
	 * @author eProtectioneers
	 */
	public static class FileChoosePathDatabase implements Serializable{
				
		private static File fcfile=new File("filechooser/filechooserpaths");
		
		/**
		 * save the filechoosers
		 */
		public static void saveFileChooser(){
			String[] filechooserPaths = new String[2];
			
			if(_picfc.getSelectedFile()!=null)filechooserPaths[0]=_picfc.getCurrentDirectory().getAbsolutePath();
			if(_keyfc.getSelectedFile()!=null)filechooserPaths[1]=_keyfc.getCurrentDirectory().getAbsolutePath();
			
			try {
				ObjectOutputStream os =new ObjectOutputStream(new FileOutputStream(fcfile));
				os.writeObject(filechooserPaths);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * load the filechoosers from the file
		 */
		public static void loadFileChooser(){
			String[] filechooserPaths = new String[2];
			try {
				ObjectInputStream is=new ObjectInputStream(new FileInputStream(fcfile));
				filechooserPaths=(String[])is.readObject();
				try{
					_picfc=new JFileChooser(filechooserPaths[0]);
				}catch(Exception ex){}
				try{
					_keyfc=new JFileChooser(filechooserPaths[1]);
				}catch(Exception ex){}
			} catch (Exception e) {
				e.printStackTrace();
				try{
					_picfc=new JFileChooser();
					_keyfc=new JFileChooser();
				}catch (Exception e1) {}
			}
		}
	}

}
