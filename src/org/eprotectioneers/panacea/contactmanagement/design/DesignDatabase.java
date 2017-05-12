package org.eprotectioneers.panacea.contactmanagement.design;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * @version 0.1
 * 
 * Coming soon
 *
 */
public class DesignDatabase {
	private static File tmpltfile=new File(org.eprotectioneers.panacea.contactmanagement.models.DatabaseC.getUrlPath(DesignDatabase.class.getResource("/org/eprotectioneers/panacea/contactmanagement/design/").getPath())+"templates/template");
	
	public static void saveDesign(){
		try {
			ObjectOutputStream os =new ObjectOutputStream(new FileOutputStream(tmpltfile));
			os.writeObject(ProjectPanaceaTemplates.getTmplt());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
