/*
 * JaLingo, http://jalingo.sourceforge.net/
 *
 * Copyright (c) 2002-2006 Oleksandr Shyshko
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package ja.lingo.application.gui.main;


import ja.centre.gui.actionbinder.ActionBinder;
import ja.centre.gui.actionbinder.config.NListener;
import ja.centre.gui.actionbinder.config.NListenerGroup;
import ja.centre.gui.resources.Resources;
import ja.lingo.application.model.Model;
import ja.lingo.application.model.ModelAdapter;
import ja.lingo.application.model.Preferences;
import ja.lingo.application.gui.trayicon.TrayIcon;
import ja.lingo.application.gui.actions.Actions;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.awt.event.WindowListener;

public class MainGui {
    private Resources resources = Resources.forProperties( getClass() );

    @NListener(type = WindowListener.class, mappings = "windowClosing > windowClosing")
    private JFrame frame;

    private MainPanel mainPanel;
    private Model model;
    
    @NListenerGroup( {
    	@NListener( type = MouseListener.class, mappings = {
            "mouseClicked > trayIconMouseClicked"
    	} ),
        @NListener( property = "showOrHideMainItem",    type = ActionListener.class, mappings = "actionPerformed > showOrHideMain" ),
        @NListener( property = "exitItem",              type = ActionListener.class, mappings = "actionPerformed > exit" )
    } )
    private TrayIcon trayicon;

    public MainGui( Model model, MainPanel mainPanel,Actions actions ) {
        this.mainPanel = mainPanel;

        this.model = model;
        this.model.addApplicationModelListener( new ModelAdapter() {
            public void initialize( Preferences preferences ) {
                MainGui.this.mainPanel.setNavigatorDividerLocation( preferences.getNavigatorDividerLocation() );
                frame.setBounds( preferences.getMainWindowBounds() );
                if ( frame.getX() == -1 && frame.getY() == -1 ) {
                    frame.setLocationRelativeTo( null );
                }
            }
            public void main_showAtTop() {
                showAtTop();
            }
            public void main_showOrHide() {
                if ( frame.getState() == JFrame.ICONIFIED || !frame.isVisible()) {
                    showAtTop();
                } else {
                    frame.setState( JFrame.ICONIFIED );
                    frame.setVisible( false );
                }
            }
            public void settingsUpdated( Preferences preferences ) {
                MainGui.this.mainPanel.getMainToolBar().setMemoryBarVisibility( preferences.isMemoryBarVisible() );
            }
            public void dispose( Preferences preferences ) {
                preferences.setMainWindowBounds( getFrame().getBounds() );

                preferences.setNavigatorDividerLocation( MainGui.this.mainPanel.getNavigatorDividerLocation() );
            }
        } );

        frame = new JFrame();
        frame.setContentPane( mainPanel.getGui() );
        frame.setIconImage( resources.icon( "title" ).getImage() );
        frame.setTitle( resources.text( "title" ) );
        trayicon = new TrayIcon(resources.icon( "trayicon" ).getImage(),actions);
        trayicon.setVisible(true);
        ActionBinder.bind( this );
    }

    public JFrame getFrame() {
        return frame;
    }

    public void windowClosing() {
    	//Closing the window by clicking on the X will iconize it in the System Tray. 
    	frame.setState( JFrame.ICONIFIED ); //This will minimize it.
        frame.setVisible( false ); //This will Hide it.
    }
    
    public void trayIconMouseClicked( MouseEvent e ) {
        if ( SwingUtilities.isLeftMouseButton( e ) ) {
            showOrHideMain();
        }
    }

    public void showAtTop() {
        frame.setState( JFrame.NORMAL );
        frame.setVisible( true );
        frame.toFront();
        frame.requestFocus();
    }
    
    public void showOrHideMain() {
        model.main_showOrHide();
    }
    public void exit() {
        model.dispose();
    }
}
