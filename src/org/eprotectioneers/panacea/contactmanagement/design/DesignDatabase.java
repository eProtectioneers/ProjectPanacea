//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.design;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Coming soon
 * A Database, to save and read templates into/from a file
 * @author eProtectioneers
 */
public class DesignDatabase {
	private static File tmpltfile=new File("templates/template");
	
	/**
	 * saves the current design
	 */
	public static void saveDesign(){
		try {
			ObjectOutputStream os =new ObjectOutputStream(new FileOutputStream(tmpltfile));
			os.writeObject(ProjectPanaceaTemplates.getTemplate());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * loads the design from the design file
	 */
	public static void loadDesign(){
		try {
			ObjectInputStream is=new ObjectInputStream(new FileInputStream(tmpltfile));
			ProjectPanaceaTemplates.setTemplate((ProjectPanaceaTemplates)is.readObject());
		} catch (Exception e) {
			e.printStackTrace();
			try{
				ProjectPanaceaTemplates.setStandardTemplate();
				saveDesign();
				ObjectInputStream is=new ObjectInputStream(new FileInputStream(tmpltfile));
				ProjectPanaceaTemplates.setTemplate((ProjectPanaceaTemplates)is.readObject());
			}catch (Exception e1) {}
		}
	}
}
