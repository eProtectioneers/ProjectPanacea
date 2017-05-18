//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.SecureRandom;

import javax.swing.JOptionPane;

/**
 * Database Eraser Thread
 * Deletes the local databasefile safely
 * @author eProtectioneers
 * Currently in Development!
 */
public class PPCA_DBEraser extends Thread{

		/**
		 * Database file
		 */
	    private File file;

	    /**
	     * Constructor
	     * @param f Database File to delete
	     */
	    public PPCA_DBEraser(File f) {
	    	
	    	this.file = f;
			
		}

	    /**
	     * Run the thread (Set thread as deamon before!)
	     */
	    public void run() {
	    	try{
	    			PPCA_DataRepo.closeConnection();
			        file.deleteOnExit();
			        System.exit(0);
	    			
	    	}catch(Exception ex)
	    	{
	    		ex.printStackTrace();
	    	}
	    }
	    
	    /**
	     * SecureDelete the file located on the harddrive
	     * @param file File to delete
	     * @throws IOException Throws if something doesnt work
	     */
	    public static void secureDelete(File file) throws IOException {
		    if (file.exists()) {
		        long length = file.length();
		        SecureRandom random = new SecureRandom();
		        RandomAccessFile raf = new RandomAccessFile(file, "rws");
		        raf.seek(0);
		        raf.getFilePointer();
		        byte[] data = new byte[64];
		        int pos = 0;
		        while (pos < length) {
		            random.nextBytes(data);
		            raf.write(data);
		            pos += data.length;
		        }
		        raf.close();
		        file.delete();
		    }
		}
}
