package org.eprotectioneers.panacea.cs4235.Main;

import javax.swing.UIManager;
import org.eprotectioneers.panacea.userinterface.PPCA_PanaceaWindow;

/**
 * PanaceaExecutable
 * @author eProtectioneers
 */
public class PanaceaExecutable 
{
	//DevelopmentLifeFeed -> Sojournercntl
	public static final String version = "Alpha Dev Branch | 0.1";
	
	public static void main(String[] args) 
	{
		try {
            //here you can put the selected theme class name in JTattoo
			//com.jtattoo.plaf.graphite.GraphiteLookAndFeel
			//"com.jtattoo.plaf.noire.NoireLookAndFeel"
            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
        } catch (ClassNotFoundException ex) {
            
        } catch (InstantiationException ex) {
            
        } catch (IllegalAccessException ex) {
           
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
           
        }
		
		
		PPCA_PanaceaWindow window = new PPCA_PanaceaWindow();
		
		window.run();
		
	}
}
