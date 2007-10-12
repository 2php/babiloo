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
import ja.centre.gui.resources.Resources;
import ja.lingo.application.model.Model;
import ja.lingo.application.model.ModelAdapter;
import ja.lingo.application.model.Preferences;
import ja.lingo.application.gui.trayicon.TrayIcon;

import javax.swing.*;
import java.awt.event.WindowListener;

public class MainGui {
    private Resources resources = Resources.forProperties( getClass() );

    @NListener(type = WindowListener.class, mappings = "windowClosing > windowClosing")
    private JFrame frame;

    private MainPanel mainPanel;
    private Model model;

    public MainGui( Model model, MainPanel mainPanel ) {
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
                if ( frame.getState() == JFrame.ICONIFIED ) {
                    showAtTop();
                } else {
                    frame.setState( JFrame.ICONIFIED );
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
        TrayIcon trayicon = new TrayIcon(resources.icon( "trayicon" ).getImage());
        trayicon.setVisible(true);
        ActionBinder.bind( this );
    }

    public JFrame getFrame() {
        return frame;
    }

    public void windowClosing() {
        frame.setVisible( false );

        model.dispose();
    }

    public void showAtTop() {
        frame.setVisible( true );
        frame.setState( JFrame.NORMAL );
        frame.toFront();
    }
}
