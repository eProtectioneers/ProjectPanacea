package org.eprotectioneers.panacea.contactmanagement.view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.eprotectioneers.panacea.contactmanagement.components.RoundRectangleButton;

public abstract class Item_Object extends RoundRectangleButton {

	protected JPopupMenu popupMenu;
	private boolean selectable=false;
	private static boolean hidepopup=false;
	private boolean selected=false;
	private boolean editable=true;
	private static Color bg_selected=Color.BLACK;
	private static Color fg_selected=Color.WHITE;
	private JPopupMenu selectedPopup;
	private Container _c;
	private static SelectionListener sl=new SelectionListener();
	private static Thread t1;
	
	/**
	 * @return the bg_selected
	 */
	public static Color getBg_selected() {
		return bg_selected;
	}

	/**
	 * sets the
	 * @param bg_selected
	 */
	public static void setBg_selected(Color bg_selected) {
		Item_Object.bg_selected = bg_selected;
	}

	/**
	 * @return the fg_selected
	 */
	public static Color getFg_selected() {
		return fg_selected;
	}

	/**
	 * sets the
	 * @param fg_selected
	 */
	public static void setFg_selected(Color fg_selected) {
		Item_Object.fg_selected = fg_selected;
	}	
	
	/**
	 * @return if it is selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		if(selected)new Item_ObjectMouseListener().selectItem();
		else new Item_ObjectMouseListener().deselectItem();
	}

	/**
	 * @return the hidePopup
	 */
	public static boolean isHidePopup() {
		return hidepopup;
	}

	/**
	 * @param hidePopup the hidePopup to set
	 */
	public static void setHidePopup(boolean hidePopup) {
		hidepopup = hidePopup;
	}
	
	/**
	 * @return the selectable
	 */
	public boolean isSelectable() {
		return selectable;
	}

	/**
	 * @param selectable the selectable to set
	 */
	public void setSelectable(boolean selectable) {
		if(selectable)setCursor(new Cursor(Cursor.HAND_CURSOR));
		else setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		this.selectable = selectable;
	}
	
	/**
	 * @return the editable
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * @param editable the editable to set
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	/**
	 * @return the selectedPopup
	 */
	public JPopupMenu getSelectedPopup() {
		return selectedPopup;
	}

	/**
	 * @param selectedPopup the selectedPopup to set
	 */
	public void setSelectedPopup(JPopupMenu selectedPopup) {
		this.selectedPopup = selectedPopup;
	}
	
	public Item_Object(String text, int radius) {
		super(text, radius);
		super.setHorizontalAlignment(SwingConstants.LEFT);
		super.setFont(new Font("Calibri", Font.PLAIN, 18));
		super.addActionListener(new DoubleClickListener());
		super.setFocusPainted(false);
		super.setMaximumSize(new Dimension(99999,27));
		super.setMinimumSize(new Dimension(0,27));
		super.setIconTextGap(15);
		this.addMouseListener(new Item_ObjectMouseListener());
		this.addMouseListener(sl);
	}
	
	public abstract String getShownText();
	abstract protected void generatePopup();
	abstract public void generatePopupOObject(Color bg, Color fg, boolean borderpainted, JComponent mnOObject,boolean tooltipset);
	abstract protected JMenuItem[] getDefaultOObjectMI(Object o,Color bg, Color fg, boolean borderpainted);
	abstract protected void doubleClickServiceRoutine();

	protected class GenerateOObjectMenuListener implements MenuListener,Runnable{

		private boolean generated;
		private Color _bg,_fg;
		private boolean _borderpainted;
		private JComponent _mnOObject;
		protected GenerateOObjectMenuListener(Color bg, Color fg, boolean borderpainted, JComponent mnOObject){
			this._bg=bg;
			this._fg=fg;
			this._borderpainted=borderpainted;
			this._mnOObject=mnOObject;
			generated=false;
		}
		@Override
		public void run() {
			_mnOObject.setEnabled(false);
			generatePopupOObject(_bg,_fg,_borderpainted,_mnOObject,true);
			_mnOObject.setEnabled(true);
		}
		@Override
		public void menuCanceled(MenuEvent arg0) {			
		}

		@Override
		public void menuDeselected(MenuEvent arg0) {
		}

		@Override
		public void menuSelected(MenuEvent arg0) {
			System.out.println(generated);
			if(!generated){
				if(t1!=null&&t1.isAlive())t1.stop();
				t1=new Thread(this);
				t1.start();
				generated=true;
			}
		}
		
	}
	protected class DoubleClickListener implements ActionListener, Runnable{
		private int doubleclickcounter;
		private Thread threadwait=new Thread(this);
		@Override
		public void run() {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if(doubleClickHappened()){
				doubleClickServiceRoutine();
			}
		}
		public boolean doubleClickHappened(){
			if(selected){
				doubleclickcounter=0;
				return false;
			}
			if(!threadwait.isAlive()){
				threadwait=new Thread(this);
				threadwait.start();
				doubleclickcounter=0;
			}
			doubleclickcounter++;
			if(doubleclickcounter>1){doubleclickcounter=0; return true;}
			return false;
		}
	}
	
	protected class Item_ObjectMouseListener extends MouseAdapter {
		private PopUpGenerator pug;
		private Color _bg,_fg;

		public Item_ObjectMouseListener(){
			this._bg=getBackground();
			this._fg=getForeground();
		}
		public void mousePressed(MouseEvent e) {
			if((e.isControlDown()||selectable)&&e.getButton()==MouseEvent.BUTTON1){
				if(selected){
					deselectItem();
				}else{
					selectItem();
				}
			}
			popupTriggered(e);			
		}
		public void mouseReleased(MouseEvent e) {
			popupTriggered(e);
		}
		
		public void popupTriggered(MouseEvent e){
			if(e.isPopupTrigger()){
				if(pug==null)pug=new PopUpGenerator(e);
				if(!selected&&!hidepopup){
					new Thread(pug).start();
				}else if(selected&&selectedPopup!=null){
					showMenu(selectedPopup,e);
				}
			}
		}
		
		public void selectItem(){
			selected=true;
			_bg=getBackground();
			_fg=getForeground();
			setBackground(bg_selected);
			setForeground(fg_selected);
		}
		public void deselectItem(){
			selected=false;
			setBackground(_bg);
			setForeground(_fg);
		}
		public void showMenu(JPopupMenu popumenu,MouseEvent e) {
			popumenu.show(e.getComponent(), e.getX(),e.getY());
		}		
	}
	
	protected class AddToolTipText implements Runnable{
		JComponent _c;
		String _s;
		
		public AddToolTipText(JComponent c, String text) {
			this._c=c;
			this._s=text;
		}
		@Override
		public void run() {
			_c.setToolTipText(_s);
		}
	}
		
	private class PopUpGenerator implements Runnable{
		private boolean generated=false;
		MouseEvent _me;
		public PopUpGenerator(MouseEvent me){
			this._me=me;
		}
		@Override
		public void run() {
			Cursor c=getCursor();
			if(!generated){
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				generatePopup();
				setCursor(c);
				generated=true;
			}
			new Item_ObjectMouseListener().showMenu(popupMenu,_me);
		}
	}
	
	private static class SelectionListener extends MouseAdapter{

		private static boolean handCursor=false;
	    private static KeyStroke keyStrokeP = KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, InputEvent.CTRL_MASK,false);
	    private static KeyStroke keyStrokeR = KeyStroke.getKeyStroke(KeyEvent.VK_CONTROL, 0,true);
	    private static Item_Object io;

		@Override
		public void mouseEntered(MouseEvent e) {
			io=(Item_Object)e.getSource();
			if(!io.selectable){
				io.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStrokeP, "hand");
				io.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStrokeR, "default");
				io.getActionMap().put("hand", actionSetHand);
				io.getActionMap().put("default", actionSetDefault);
			}
		}
		
		private static Action actionSetHand = new AbstractAction() {
		    @Override
		    public void actionPerformed(ActionEvent ae) {
		    	if(!handCursor){
		    		handCursor=true;
		    		io.setCursor(new Cursor(Cursor.HAND_CURSOR));
		    	}		
		    }
		};
		private static Action actionSetDefault = new AbstractAction() {
		    @Override
		    public void actionPerformed(ActionEvent ae) {
		    	if(handCursor){
			    	handCursor=false;
					io.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));		    	
				}
		    }
		};

		@Override
		public void mouseExited(MouseEvent e) {
			if(!io.selectable){
				if(handCursor){
					handCursor=false;
					io.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
				io.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStrokeP, "none");
				io.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStrokeR, "none");
			}
		}		
	}
}
