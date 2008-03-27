package ja.lingo.application.gui.trayicon;

import ja.centre.gui.resources.Resources;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import java.awt.*;
import ja.lingo.application.gui.actions.Actions;
import ja.lingo.application.util.Components;
import ja.lingo.application.util.MenuItems;
/**
* @author cumeo
*
*/
public class TrayIcon extends java.awt.TrayIcon {

    private JPopupMenu menu;

    private JMenuItem showOrHideMainItem;
    private JMenuItem exitItem;
    private JMenuItem pasteItem;
    private JDialog dialog;
    
       public TrayIcon(Image image,Actions actions) {
               super(image); // TODO i18n
               this.setImageAutoSize(true);
               this.dialog = new JDialog((Frame) null, "BabilooTrayIcon");
               dialog.setUndecorated(true);
               dialog.setAlwaysOnTop(true);
               initComponents(actions);
       }
       
       private void initComponents(Actions actions) {
    	   
    	   showOrHideMainItem = MenuItems.showOrHideMain();
           exitItem = MenuItems.exit();
           pasteItem = actions.getPasteAndTranslateAction().item();

           menu = Components.popupMenu();
           menu.add( showOrHideMainItem );
           menu.addSeparator();
           menu.add( pasteItem );
           menu.add( actions.getSettingsShowAction().item() );
           menu.addSeparator();
           menu.add( exitItem );
           
           //This fixes the bug in JDK6 with the awt TrayIcon DisposeEvent
           Toolkit.getDefaultToolkit().getSystemEventQueue().push( new PopupFixQueue(menu) );
           
           addMouseListener(new MouseAdapter() {
               public void mousePressed(MouseEvent e) {
                   showJPopupMenu(e);
               }
               public void mouseReleased(MouseEvent e) {
                   dialog.dispose();
               }
           });
       }
       
       private void showJPopupMenu(MouseEvent e) {
               if (e.isPopupTrigger() && menu != null) {
                   Dimension size = menu.getPreferredSize();
                   dialog.setLocation(e.getX(), e.getY() - size.height);
                   dialog.setVisible(true);
                   menu.show(dialog.getContentPane(), 0, 0);
                   // popup works only for focused windows
                   dialog.toFront();
               }else{
            	   dialog.dispose();
               }
           }
                       
       public void setVisible(boolean visible) {
               SystemTray tray = SystemTray.getSystemTray();
               if (visible) {
                       try {
                               tray.add(this);
                       } catch (AWTException e) {
                               e.printStackTrace();
                       }
               } else {
                       tray.remove(this);
               }
       }


}


