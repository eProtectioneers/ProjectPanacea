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
import org.eprotectioneers.panacea.contactmanagement.design.ProjectPanaceaTemplates;

public class ChooseFile {
	private static JFileChooser _picfc=new JFileChooser();
	private static JFileChooser _keyfc=new JFileChooser();
	
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
	
	public static class FileChoosePathDatabase implements Serializable{
				
		private static File fcfile=new File("filechooser/filechooserpaths");
		
		public static void saveFileChooser(){
			ArrayList<String> filechooserPaths = new ArrayList<String>();
			
			if(_picfc.getSelectedFile()!=null)filechooserPaths.add(0,_picfc.getCurrentDirectory().getAbsolutePath());
			if(_keyfc.getSelectedFile()!=null)filechooserPaths.add(1,_keyfc.getCurrentDirectory().getAbsolutePath());
			
			try {
				ObjectOutputStream os =new ObjectOutputStream(new FileOutputStream(fcfile));
				os.writeObject(filechooserPaths);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static void loadFileChooser(){
			ArrayList<String> filechooserPaths = new ArrayList<String>();
			try {
				ObjectInputStream is=new ObjectInputStream(new FileInputStream(fcfile));
				filechooserPaths=(ArrayList<String>)is.readObject();
				try{
					_picfc=new JFileChooser(filechooserPaths.get(0));
				}catch(Exception ex){}
				try{
					_keyfc=new JFileChooser(filechooserPaths.get(1));
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
