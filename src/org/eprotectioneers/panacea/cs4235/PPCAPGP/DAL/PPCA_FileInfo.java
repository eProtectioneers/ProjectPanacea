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

public class PPCA_FileInfo {

	private final DateFormat FORMATTER = new SimpleDateFormat(
			"dd/MM/yyyy  hh:mm");
	private File file;
	private boolean hasLoaded = false;
	private String owner;
	private Map<PPCA_Timefield, Date> timefields = new HashMap<PPCA_Timefield, Date>();
	
	public PPCA_FileInfo(File file) {
		this.file = file;
	}
	
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
	private void load() throws IOException, ParseException {
		if (hasLoaded)
			return;
		shellToDir(PPCA_Timefield.CREATED);
		shellToDir(PPCA_Timefield.ACCESSED);
		shellToDir(PPCA_Timefield.WRITTEN);
	}
	public String getName() {
		return file.getName();
	}
	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}
	public long getSize() {
		return file.length();
	}
	public Date getLastModified() {
		return new Date(file.lastModified());
	}
	public String getOwner() throws IOException, ParseException {
		load();
		return owner;
	}
	public Date getCreated() throws IOException, ParseException {
		load();
		return timefields.get(PPCA_Timefield.CREATED);
	}
	public Date getAccessed() throws IOException, ParseException {
		load();
		return timefields.get(PPCA_Timefield.ACCESSED);
	}
	public Date getWritten() throws IOException, ParseException {
		load();
		return timefields.get(PPCA_Timefield.WRITTEN);
	}
}
