//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.control;

import java.io.File;

/**
 * @author eProtectioneers
 */
public class Utils {
	
	public final static String jpeg = "jpeg";
	public final static String jpg = "jpg";
	public final static String gif = "gif";
	public final static String tiff = "tiff";
	public final static String tif = "tif";
	public final static String png = "png";

	/**
	 * @param file
	 * @return the Extension of the given file
	 */
	public static String getExtension(File f) {
    	String ext = null;
    	String s = f.getName();
    	int i = s.lastIndexOf('.');

    	if (i > 0 &&  i < s.length() - 1) {
    		ext = s.substring(i+1).toLowerCase();
    	}
    	return ext;
	}
}
