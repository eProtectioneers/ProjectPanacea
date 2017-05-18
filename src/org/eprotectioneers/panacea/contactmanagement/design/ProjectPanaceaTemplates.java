//
// Copyright (c) eProtectioneers 2016/17. All rights reserved.  
// Licensed under the MIT License. See LICENSE file in the project root for full license information.
//
package org.eprotectioneers.panacea.contactmanagement.design;

import java.awt.*;
import java.io.File;
import java.io.Serializable;


/**
 * Coming soon
 * A design template, which job is, to be able to change nearly every Color of the program
 * @author eProtectioneers
 */
public class ProjectPanaceaTemplates implements Serializable {
	
	private static ProjectPanaceaTemplates tmplt=new ProjectPanaceaTemplates();

	private Color cbg_main;
	private Color cbg_menu;
	private Color cbg_email;
	private Color cbg_saveEmail;
	private Color cbg_contacts;
	private Color cbg_textfield;
	private Color cbg_item;
	private Color cbg_itemFocused;
	private Color cbg_button;
	private Color cbg_buttonClicked;
	private Color cbg_emailPreview;
	
	private Color cfnt_menu;
	private Color cfnt_emailPreview_subject;
	private Color cfnt_emailPreview_textPreview;
	private Color cfnt_item;
	private Color cfnt_itemFocused;
	
	private Font font_menu;
	private Font font_emailPreview_Subject;
	private Font font_emailPreview_TextPreview;	
	private Font font_item;
	
	/**
	 * @return the tmplt
	 */
	public static ProjectPanaceaTemplates getTemplate() {
		return tmplt;
	}

	/**
	 * @param tmplt the tmplt to set
	 */
	public static void setTemplate(ProjectPanaceaTemplates tmplt) {
		ProjectPanaceaTemplates.tmplt = tmplt;
	}

	/**
	 * @return the cbg_main
	 */
	public Color getCbg_main() {
		return cbg_main;
	}

	/**
	 * @param cbg_main the cbg_main to set
	 */
	public void setCbg_main(Color cbg_main) {
		this.cbg_main = cbg_main;
	}

	/**
	 * @return the cbg_menu
	 */
	public Color getCbg_menu() {
		return cbg_menu;
	}

	/**
	 * @param cbg_menu the cbg_menu to set
	 */
	public void setCbg_menu(Color cbg_menu) {
		this.cbg_menu = cbg_menu;
	}

	/**
	 * @return the cbg_email
	 */
	public Color getCbg_email() {
		return cbg_email;
	}

	/**
	 * @param cbg_email the cbg_email to set
	 */
	public void setCbg_email(Color cbg_email) {
		this.cbg_email = cbg_email;
	}

	/**
	 * @return the cbg_saveEmail
	 */
	public Color getCbg_saveEmail() {
		return cbg_saveEmail;
	}

	/**
	 * @param cbg_saveEmail the cbg_saveEmail to set
	 */
	public void setCbg_saveEmail(Color cbg_saveEmail) {
		this.cbg_saveEmail = cbg_saveEmail;
	}

	/**
	 * @return the cbg_contacts
	 */
	public Color getCbg_contacts() {
		return cbg_contacts;
	}

	/**
	 * @param cbg_contacts the cbg_contacts to set
	 */
	public void setCbg_contacts(Color cbg_contacts) {
		this.cbg_contacts = cbg_contacts;
	}

	/**
	 * @return the cbg_textfield
	 */
	public Color getCbg_textfield() {
		return cbg_textfield;
	}

	/**
	 * @param cbg_textfield the cbg_textfield to set
	 */
	public void setCbg_textfield(Color cbg_textfield) {
		this.cbg_textfield = cbg_textfield;
	}

	/**
	 * @return the cbg_item
	 */
	public Color getCbg_item() {
		return cbg_item;
	}

	/**
	 * @param cbg_item the cbg_item to set
	 */
	public void setCbg_item(Color cbg_item) {
		this.cbg_item = cbg_item;
	}

	/**
	 * @return the cbg_itemFocused
	 */
	public Color getCbg_itemFocused() {
		return cbg_itemFocused;
	}

	/**
	 * @param cbg_itemFocused the cbg_itemFocused to set
	 */
	public void setCbg_itemFocused(Color cbg_itemFocused) {
		this.cbg_itemFocused = cbg_itemFocused;
	}

	/**
	 * @return the cbg_button
	 */
	public Color getCbg_button() {
		return cbg_button;
	}

	/**
	 * @param cbg_button the cbg_button to set
	 */
	public void setCbg_button(Color cbg_button) {
		this.cbg_button = cbg_button;
	}

	/**
	 * @return the cbg_buttonClicked
	 */
	public Color getCbg_buttonClicked() {
		return cbg_buttonClicked;
	}

	/**
	 * @param cbg_buttonClicked the cbg_buttonClicked to set
	 */
	public void setCbg_buttonClicked(Color cbg_buttonClicked) {
		this.cbg_buttonClicked = cbg_buttonClicked;
	}

	/**
	 * @return the cbg_emailPreview
	 */
	public Color getCbg_emailPreview() {
		return cbg_emailPreview;
	}

	/**
	 * @param cbg_emailPreview the cbg_emailPreview to set
	 */
	public void setCbg_emailPreview(Color cbg_emailPreview) {
		this.cbg_emailPreview = cbg_emailPreview;
	}

	/**
	 * @return the cfnt_menu
	 */
	public Color getCfnt_menu() {
		return cfnt_menu;
	}

	/**
	 * @param cfnt_menu the cfnt_menu to set
	 */
	public void setCfnt_menu(Color cfnt_menu) {
		this.cfnt_menu = cfnt_menu;
	}

	/**
	 * @return the cfnt_emailPreview_subject
	 */
	public Color getCfnt_emailPreview_subject() {
		return cfnt_emailPreview_subject;
	}

	/**
	 * @param cfnt_emailPreview_subject the cfnt_emailPreview_subject to set
	 */
	public void setCfnt_emailPreview_subject(Color cfnt_emailPreview_subject) {
		this.cfnt_emailPreview_subject = cfnt_emailPreview_subject;
	}

	/**
	 * @return the cfnt_emailPreview_textPreview
	 */
	public Color getCfnt_emailPreview_textPreview() {
		return cfnt_emailPreview_textPreview;
	}

	/**
	 * @param cfnt_emailPreview_textPreview the cfnt_emailPreview_textPreview to set
	 */
	public void setCfnt_emailPreview_textPreview(Color cfnt_emailPreview_textPreview) {
		this.cfnt_emailPreview_textPreview = cfnt_emailPreview_textPreview;
	}

	/**
	 * @return the cfnt_item
	 */
	public Color getCfnt_item() {
		return cfnt_item;
	}

	/**
	 * @param cfnt_item the cfnt_item to set
	 */
	public void setCfnt_item(Color cfnt_item) {
		this.cfnt_item = cfnt_item;
	}

	/**
	 * @return the cfnt_itemFocused
	 */
	public Color getCfnt_itemFocused() {
		return cfnt_itemFocused;
	}

	/**
	 * @param cfnt_itemFocused the cfnt_itemFocused to set
	 */
	public void setCfnt_itemFocused(Color cfnt_itemFocused) {
		this.cfnt_itemFocused = cfnt_itemFocused;
	}

	/**
	 * @return the font_menu
	 */
	public Font getFont_menu() {
		return font_menu;
	}

	/**
	 * @param font_menu the font_menu to set
	 */
	public void setFont_menu(Font font_menu) {
		this.font_menu = font_menu;
	}

	/**
	 * @return the font_emailPreview_Subject
	 */
	public Font getFont_emailPreview_Subject() {
		return font_emailPreview_Subject;
	}

	/**
	 * @param font_emailPreview_Subject the font_emailPreview_Subject to set
	 */
	public void setFont_emailPreview_Subject(Font font_emailPreview_Subject) {
		this.font_emailPreview_Subject = font_emailPreview_Subject;
	}

	/**
	 * @return the font_emailPreview_TextPreview
	 */
	public Font getFont_emailPreview_TextPreview() {
		return font_emailPreview_TextPreview;
	}

	/**
	 * @param font_emailPreview_TextPreview the font_emailPreview_TextPreview to set
	 */
	public void setFont_emailPreview_TextPreview(Font font_emailPreview_TextPreview) {
		this.font_emailPreview_TextPreview = font_emailPreview_TextPreview;
	}

	/**
	 * @return the font_item
	 */
	public Font getFont_item() {
		return font_item;
	}

	/**
	 * @param font_item the font_item to set
	 */
	public void setFont_item(Font font_item) {
		this.font_item = font_item;
	}

		public static Color getContrastColor(Color bgColor){
		Color c;
		int brightness=(int)((double)bgColor.getRed()*0.299+
				(double)bgColor.getGreen()*0.587+
				(double)bgColor.getBlue()*0.114);
		if(brightness>(255/2)){
			c=Color.BLACK;
		}
		else{
			c=Color.WHITE;
		}
		return c;
	}
	
	/**
	 * Sets the current layout to the standard one
	 */
	public static void setStandardTemplate(){
		tmplt.cbg_main=Color.WHITE;
		tmplt.cbg_menu=new Color(250,250,250);
		tmplt.cbg_email=Color.WHITE;
		tmplt.cbg_saveEmail=new Color(245,245,245);
		tmplt.cbg_contacts=new Color(9,29,62);
		tmplt.cbg_textfield=new Color(230,230,230);
		tmplt.cbg_item=new Color(40,40,40);
		tmplt.cbg_itemFocused=new Color(150,150,150);
		tmplt.cbg_button=new Color(41,54,64);
		tmplt.cbg_buttonClicked=Color.LIGHT_GRAY;
		tmplt.cbg_emailPreview=new Color(250,250,250);
		
		tmplt.cfnt_menu=ProjectPanaceaTemplates.getContrastColor(tmplt.cbg_menu);
		tmplt.cfnt_emailPreview_subject=ProjectPanaceaTemplates.getContrastColor(tmplt.cbg_emailPreview);
		tmplt.cfnt_emailPreview_textPreview=ProjectPanaceaTemplates.getContrastColor(tmplt.cbg_emailPreview);
		tmplt.cfnt_item=ProjectPanaceaTemplates.getContrastColor(tmplt.cbg_menu);
		tmplt.cfnt_itemFocused=ProjectPanaceaTemplates.getContrastColor(tmplt.cbg_menu);
		
		tmplt.font_menu=new Font("Calibri", Font.PLAIN, 11);
		tmplt.font_emailPreview_Subject=new Font("Calibri", Font.BOLD, 13);
		tmplt.font_emailPreview_TextPreview=new Font("Calibri", Font.ITALIC, 11);
		tmplt.font_item=new Font("Calibri", Font.PLAIN, 11);
		
		DesignDatabase.saveDesign();
	}
}