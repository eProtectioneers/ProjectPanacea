//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.control;

import java.io.File;
import javax.swing.filechooser.*;

/**
 * A FileFilter, to show only pictures
 * @author eProtectioneers
 */
public class ImageFilter extends FileFilter {
	
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		
		String extension = Utils.getExtension(f);
		if (extension != null) {
			if (extension.equals(Utils.tiff) ||
					extension.equals(Utils.tif) ||
					extension.equals(Utils.gif) ||
					extension.equals(Utils.jpeg) ||
					extension.equals(Utils.jpg) ||
					extension.equals(Utils.png)) {
				return true;
			} else return false;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "Images";
	}
}
