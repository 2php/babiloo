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

package ja.lingo.application.gui.main.settings;

import info.clearthought.layout.TableLayout;
import ja.centre.gui.actionbinder.ActionBinder;
import ja.centre.gui.actionbinder.config.NListener;
import ja.centre.gui.resources.Resources;
import ja.lingo.application.gui.actions.Actions;
import ja.lingo.application.gui.main.settings.appearance.AppearanceGui;
import ja.lingo.application.gui.main.settings.dictionaries.DictionariesGui;
import ja.lingo.application.model.Model;
import ja.lingo.application.model.ModelAdapter;
import ja.lingo.application.model.Preferences;
import ja.lingo.application.util.Buttons;
import ja.lingo.application.util.Components;
import ja.lingo.application.util.Gaps;
import static ja.lingo.application.util.Gaps.GAP5;
import ja.lingo.application.util.messages.Messages;
import ja.lingo.application.util.misc.Threads;
import ja.lingo.application.util.progress.ProgressUtil;
import ja.lingo.application.util.progress.ITitledMonitor;
import ja.lingo.application.util.progress.engine.Monitors;
import ja.lingo.engine.IEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

public class SettingsGui {
    private Resources resources = Resources.forProperties( getClass() );

    private JFrame parentFrame;

    @NListener ( type = WindowListener.class, mappings = "windowClosing > hide" )
    private JDialog dialog;

    @NListener ( type = ActionListener.class, mappings = "actionPerformed > hide" )
    private JButton closeButton;
    private JTabbedPane tabbedPane;
    private Model model;
    private Preferences preferences;
    private AppearanceGui appearanceGui;
    private IEngine engine;

    public SettingsGui( Actions actions, final Model model, IEngine engine, Preferences preferences, JFrame parentFrame, DictionariesGui dictionariesGui, AppearanceGui appearanceGui ) {
        this.parentFrame = parentFrame;

        this.appearanceGui = appearanceGui;
        this.engine = engine;
        this.model = model;
        this.preferences = preferences;

        this.model.addApplicationModelListener( new ModelAdapter() {
            public void settings_show() {
                model.dropzone_hideTemporary();
                showDialogWithDefaults();
            }
        } );

        closeButton = Buttons.close();

        JPanel southPanel = new JPanel( new TableLayout( new double[][] {
            { Gaps.GAP7, TableLayout.FILL, Gaps.GAP7, TableLayout.PREFERRED },
            { TableLayout.PREFERRED }
        } ) );
        southPanel.add( actions.getDownloadDictionariesAction().rollover(), "1, 0");
        southPanel.add( closeButton, "3, 0" );

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab( resources.text( "dictionaries" ), dictionariesGui.getGui() );
        tabbedPane.addTab( resources.text( "appearance" ),  appearanceGui.getGui() );

        JPanel panel = new JPanel( new BorderLayout( GAP5, GAP5 * 2 ) );
        panel.add( tabbedPane, BorderLayout.CENTER );
        panel.add( southPanel, BorderLayout.SOUTH );

        Gaps.applyBorder7( panel );

        dialog = Components.dialogModal( parentFrame );
        dialog.setTitle( resources.text( "title" ) );
        dialog.setContentPane( panel );
        dialog.getRootPane().setDefaultButton( closeButton );

        ActionBinder.bind( this );
    }

    public void showDialogWithDefaults() {
        tabbedPane.setSelectedIndex( 0 );

        closeButton.requestFocusInWindow();

        dialog.setSize( 590, 450 );
        dialog.setLocationRelativeTo( parentFrame );
        dialog.setVisible( true );
    }

    public JDialog getDialog() {
        return dialog;
    }

    public void hide() {
        if ( engine.isCompiled() ) {
            hideAndUpdatePrefs();
            return;
        }

        final ITitledMonitor controller = ProgressUtil.start( dialog );

        Threads.startInBackground( new Runnable() {
            public void run() {
                try {
                    engine.compile( Monitors.compile( controller )  );

                    SwingUtilities.invokeLater( new Runnable() {
                        public void run() {
                            hideAndUpdatePrefs();
                        }
                    } );

                } catch ( Throwable t ) {
                    Messages.internalError( getDialog(), t );
                } finally {
                    ProgressUtil.stop( dialog );
                }
            }
        } );
    }

    private void hideAndUpdatePrefs() {
        dialog.setVisible( false );

        preferences.setFontSize( appearanceGui.getFontSize() );
        preferences.setFontFace( appearanceGui.getFontFace() );
        preferences.setDropZoneVisible( appearanceGui.isDropZoneVisible());
        preferences.setMemoryBarVisible( appearanceGui.isMemoryBarVisible() );

        model.settingsUpdated();
    }
}
