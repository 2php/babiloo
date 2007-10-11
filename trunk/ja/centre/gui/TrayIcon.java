/**
 * 
 */
package ja.centre.gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

/**
 * @author cumeo
 *
 */
public class TrayIcon extends java.awt.TrayIcon {

	private static final Image image = Toolkit.getDefaultToolkit().getImage("d:\\home.png"); // TODO change it
	
	private static final TrayIcon trayIcon = new TrayIcon(image);

	private TrayIcon(Image image) {
		super(image, "Babilooo Opensource Dictionary"); // TODO i18n
		initComponents();
	}
	
	private void initComponents() {
		setImageAutoSize(true);

		PopupMenu popup = new PopupMenu();
		
		MenuItem showMenuItem = new MenuItem("Show Babillo");
		showMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO show program
		
			}
		
		});
		popup.add(showMenuItem);
		
		popup.addSeparator();
		
		MenuItem settingsMenuItem = new MenuItem("Settings...");
		settingsMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO show settings dialog
			}
		
		});
		popup.add(settingsMenuItem); 
		
		popup.addSeparator();
		
		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO cause program to exit
		
			}
		
		});
		popup.add(exitMenuItem);
		
		setPopupMenu(popup);
		
		addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO show the program
			}
		
		});
	}
		
	public static TrayIcon getTrayIcon() {
		return trayIcon;
	}
	
	public void setVisible(boolean visible) {
		SystemTray tray = SystemTray.getSystemTray();
		if (visible) {
			try {
				tray.add(this);
			} catch (AWTException e) {
				// TODO what to do???
				e.printStackTrace();
			}
		} else {
			tray.remove(this);
		}
	}

	// TODO move it to test part
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeLater(new Runnable() {
			
				@Override
				public void run() {
					JFrame jFrame = new JFrame("Babiloo TrayIcon test.");
					jFrame.setLocationRelativeTo(null);
					jFrame.add(new JLabel("Can you see it?"));
					jFrame.pack();
					jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					jFrame.setVisible(true);
					TrayIcon.getTrayIcon().setVisible(true);
				}
			
			});
		} catch (UnsupportedOperationException ex) {
			System.out.println("System tray is not supported.");
		}
	}

}
