package org.eprotectioneers.panacea.contactmanagement.design;

import java.awt.*;
import java.io.File;
import java.io.Serializable;


/**
 * 
 * @version 0.1
 * 
 * Coming soon
 *
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
	
	public static ProjectPanaceaTemplates getTmplt(){
		return tmplt;
	}
	public static void setTemplate(ProjectPanaceaTemplates tmplt){
		ProjectPanaceaTemplates.tmplt=tmplt;
	}
	
	
	public static Color getCbg_main() {
		return tmplt.cbg_main;
	}
	public static void setCbg_main(Color cbg_main) {
		tmplt.cbg_main = cbg_main;
	}
	
	public static Color getCbg_menu() {
		return tmplt.cbg_menu;
	}
	public static void setCbg_menu(Color cbg_menu) {
		tmplt.cbg_menu = cbg_menu;
	}
	
	public static Color getCbg_email() {
		return tmplt.cbg_email;
	}
	public static void setCbg_email(Color cbg_email) {
		tmplt.cbg_email = cbg_email;
	}
	
	public static Color getCbg_saveEmail() {
		return tmplt.cbg_saveEmail;
	}
	public static void setCbg_saveEmail(Color cbg_saveEmail) {
		tmplt.cbg_saveEmail = cbg_saveEmail;
	}

	public static Color getCbg_contacts() {
		return tmplt.cbg_contacts;
	}
	public static void setCbg_contacts(Color cbg_contacts) {
		tmplt.cbg_contacts = cbg_contacts;
	}
	
	public static Color getCbg_textfield() {
		return tmplt.cbg_textfield;
	}
	public static void setCbg_textfield(Color cbg_textfield) {
		tmplt.cbg_textfield = cbg_textfield;
	}

	public static Color getCbg_item() {
		return tmplt.cbg_item;
	}
	public static void setCbg_item(Color cbg_item) {
		tmplt.cbg_item = cbg_item;
	}
	
	public static Color getCbg_itemFocused() {
		return tmplt.cbg_itemFocused;
	}
	public static void setCbg_itemFocused(Color cbg_itemFocused) {
		tmplt.cbg_itemFocused = cbg_itemFocused;
	}
	
	public static Color getCbg_button() {
		return tmplt.cbg_button;
	}
	public static void setCbg_button(Color cbg_button) {
		tmplt.cbg_button = cbg_button;
	}
	
	public static Color getCbg_buttonClicked() {
		return tmplt.cbg_buttonClicked;
	}
	public static void setCbg_buttonClicked(Color cbg_buttonClicked) {
		tmplt.cbg_buttonClicked = cbg_buttonClicked;
	}
	
	public static Color getCbg_cbg_emailPreview() {
		return tmplt.cbg_emailPreview;
	}
	public static void setCbg_emailPreview(Color cbg_emailPreview) {
		tmplt.cbg_emailPreview = cbg_emailPreview;
	}
	
	
	public static Color getCfnt_menu() {
		return tmplt.cfnt_menu;
	}
	public static void setCfnt_menu(Color cfnt_menu) {
		tmplt.cfnt_menu = cfnt_menu;
	}
	
	public static Color getCfnt_emailPreview_subject() {
		return tmplt.cfnt_emailPreview_subject;
	}
	public static void setCfnt_emailSubject(Color cfnt_emailPreview_emailSubject) {
		tmplt.cfnt_emailPreview_subject = cfnt_emailPreview_emailSubject;
	}
	
	public static Color getCfnt_emailTextPreview() {
		return tmplt.cfnt_emailPreview_textPreview;
	}
	public static void setCfnt_emailTextPreview(Color cfnt_emailPreview_textPreview) {
		tmplt.cfnt_emailPreview_textPreview = cfnt_emailPreview_textPreview;
	}
	
	public static Color getCfnt_item() {
		return tmplt.cfnt_item;
	}
	public static void setCfnt_item(Color cfnt_item) {
		tmplt.cfnt_item = cfnt_item;
	}
	
	public static Color getCfnt_itemFocused() {
		return tmplt.cfnt_itemFocused;
	}
	public static void setCfnt_itemFocused(Color cfnt_itemFocused) {
		tmplt.cfnt_itemFocused = cfnt_itemFocused;
	}
	
	public static Font getFont_menu() {
		return tmplt.font_menu;
	}
	public static void setFont_menu(Font font_menu) {
		tmplt.font_menu = font_menu;
	}
	
	public static Font getFont_emailSubject() {
		return tmplt.font_emailPreview_Subject;
	}
	public static void setFont_emailSubject(Font font_emailSubject) {
		tmplt.font_emailPreview_Subject = font_emailSubject;
	}
	
	public static Font getFont_emailTextPreview() {
		return tmplt.font_emailPreview_TextPreview;
	}
	public static void setFont_emailTextPreview(Font font_emailTextPreview) {
		tmplt.font_emailPreview_TextPreview = font_emailTextPreview;
	}
	
	public static Font getFont_item() {
		return tmplt.font_item;
	}
	public static void setFont_item(Font font_item) {
		tmplt.font_item = font_item;
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