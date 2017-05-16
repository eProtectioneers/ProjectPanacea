//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.cs4235.Main;

import javax.swing.UIManager;
import org.eprotectioneers.panacea.userinterface.PPCA_PanaceaWindow;

/**
 * PanaceaExecutable (Main Program)
 * @author eProtectioneers
 */
public class PanaceaExecutable 
{
	/**
	 * Version String
	 */
	public static final String version = "Alpha Dev Branch | 0.1";
	
	/**
	 * main method
	 * @param args unused
	 */
	public static void main(String[] args) 
	{
		try {
			//com.jtattoo.plaf.graphite.GraphiteLookAndFeel
			//"com.jtattoo.plaf.noire.NoireLookAndFeel"
            UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
        	ex.printStackTrace();
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        	ex.printStackTrace();
        }
		
		//initialise the new PPCA_PanaceaWindow
		PPCA_PanaceaWindow window = new PPCA_PanaceaWindow();
		
		//run the main window
		window.run();	
	}
}
