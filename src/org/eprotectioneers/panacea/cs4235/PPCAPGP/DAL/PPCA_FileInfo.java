package org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eprotectioneers.panacea.cs4235.PPCAPGP.DAL.PPCA_Timefield;

/**
 * FileInformation Class
 * @author eProtectioneers
 */
public class PPCA_FileInfo {

	/**
	 * Dateformat
	 */
	private final DateFormat FORMATTER = new SimpleDateFormat(
			"dd/MM/yyyy  hh:mm");
	/**
	 * File
	 */
	private File file;
	/**
	 * Wether the file is loaded or not
	 */
	private boolean hasLoaded = false;
	/**
	 * Fileowner
	 */
	private String owner;
	/**
	 * Timemap
	 */
	private Map<PPCA_Timefield, Date> timefields = new HashMap<PPCA_Timefield, Date>();
	
	/**
	 * Constructor
	 * @param file
	 */
	public PPCA_FileInfo(File file) {
		this.file = file;
	}
	
	/**
	 * Get the Time switch
	 * @param field Timefield
	 * @return String timefield
	 */
	private String getTimefieldSwitch(PPCA_Timefield field) {
		switch (field) {
		case CREATED:
			return "C";
		case ACCESSED:
			return "A";
		default:
			return "W";
		}
	}
	
	/**
	 * Execute a system shell to get the informations of a file
	 * @param timefield Timefield
	 * @throws IOException Exception
	 * @throws ParseException Exception
	 */
	private void shellToDir(PPCA_Timefield timefield) throws IOException,
			ParseException {
		Runtime systemShell = Runtime.getRuntime();
		Process output = systemShell.exec(String.format("cmd /c dir /Q /R /T%s %s ", getTimefieldSwitch(timefield), file.getAbsolutePath()));
		BufferedReader reader = new BufferedReader(new InputStreamReader(output.getInputStream()));
		String outputLine = null;
		while ((outputLine = reader.readLine()) != null) {
			if (outputLine.contains(file.getName())) {
				//timefields.put(timefield,
					 	//FORMATTER.parse(outputLine.substring(0, 17)));
				owner = outputLine.substring(36, 59);
			}
		}
	}
	
	/**
	 * Load the file
	 * @throws IOException
	 * @throws ParseException
	 */
	private void load() throws IOException, ParseException {
		if (hasLoaded)
			return;
		shellToDir(PPCA_Timefield.CREATED);
		shellToDir(PPCA_Timefield.ACCESSED);
		shellToDir(PPCA_Timefield.WRITTEN);
	}
	
	/**
	 * Get the name
	 * @return String name
	 */
	public String getName() {
		return file.getName();
	}
	/**
	 * Get the absolute path
	 * @return String path
	 */
	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}
	/**
	 * Get the size in byte
	 * @return long ByteSize
	 */
	public long getSize() {
		return file.length();
	}
	/**
	 * Last modifed date
	 * @return Date last modified
	 */
	@Deprecated
	public Date getLastModified() {
		return new Date(file.lastModified());
	}
	/**
	 * Get the file owner
	 * @return String owner of the file
	 * @throws IOException
	 * @throws ParseException
	 */
	public String getOwner() throws IOException, ParseException {
		load();
		return owner;
	}
	/**
	 * Get the creation date
	 * @return Date created
	 * @throws IOException
	 * @throws ParseException
	 */
	@Deprecated
	public Date getCreated() throws IOException, ParseException {
		load();
		return timefields.get(PPCA_Timefield.CREATED);
	}
	/**
	 * Get the last acccess date
	 * @return Date accessed
	 * @throws IOException
	 * @throws ParseException
	 */
	public Date getAccessed() throws IOException, ParseException {
		load();
		return timefields.get(PPCA_Timefield.ACCESSED);
	}
	/**
	 * Get the last write Date
	 * @return Date written
	 * @throws IOException
	 * @throws ParseException
	 */
	public Date getWritten() throws IOException, ParseException {
		load();
		return timefields.get(PPCA_Timefield.WRITTEN);
	}
}
