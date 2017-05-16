//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * This class handles writing into and reading from a file.
 * @author eProtectioneers
 */
public class PPCA_FileEngine 
{
	/**
	 * Write a payload to a file
	 * @param filepath the file path
	 * @param payload the payload
	 * @return true on success or false otherwise
	 */
	public static boolean write(String filepath, String payload)
	{
		boolean result = true;

		try
		{
			FileWriter outFile = new FileWriter(filepath);
			PrintWriter out = new PrintWriter(outFile);
			out.print(payload);
			out.close();
		}
		catch (IOException e)
		{
			result = false;
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * Read content of a file
	 * @param filepath the file path
	 * @return the content of a file. It returns null if such file cannot be read.
	 */
	public static String read(String filepath)
	{
		String result = null;

		try
		{
			FileInputStream fstream = new FileInputStream(filepath);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			result = "";
			String temp = "";
			while ((temp = br.readLine()) != null)
			{
				result += temp;
			}
			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * Check if the file or directory exists in the filesystem.
	 * @param filepath the filepath
	 * @return true if the file or directory exists or false otherwise
	 */
	public static boolean isFileOrDirectoryExist(String filepath)
	{
		File file = new File(filepath);
		return file.exists();
	}
}
