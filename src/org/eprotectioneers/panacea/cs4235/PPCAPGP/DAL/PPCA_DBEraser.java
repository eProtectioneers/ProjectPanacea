package org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.SecureRandom;

import javax.swing.JOptionPane;

public class PPCA_DBEraser extends Thread{

	    private File file;

	    public PPCA_DBEraser(File f) {
	    	
	    	this.file = f;
			
		}

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
