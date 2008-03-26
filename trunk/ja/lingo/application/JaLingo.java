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

package ja.lingo.application;

import ja.centre.gui.concurrent.ATask;
import ja.centre.gui.concurrent.IThrowableProcessor;
import ja.centre.gui.concurrent.TaskSequence;
import ja.centre.util.assertions.States;
import ja.centre.util.io.lock.LockedException;
import ja.centre.util.measurer.TimeMeasurer;
import ja.lingo.application.gui.splash.Splash;
import ja.lingo.application.model.Preferences;
import ja.lingo.application.model.Skeleton;
import ja.lingo.application.util.messages.Messages;
import ja.lingo.application.util.misc.LogInitializer;
import ja.lingo.application.util.plaf.JaLingoLookAndFeel;
import ja.lingo.engine.UnknownCacheVersionException;
import ja.lingo.engine.util.CssHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.*;
import java.io.IOException;
import java.io.StringReader;

public class JaLingo {
    private static final Log LOG = LogFactory.getLog( JaLingo.class );

    private Splash splash;
    private Skeleton skeleton;

    public JaLingo() {
        try {
            final TimeMeasurer measurer = new TimeMeasurer();

            splash = new Splash();

            TaskSequence taskSequence = new TaskSequence( new IThrowableProcessor() {
                public void process( Throwable t ) {
                    processThrowableAndExit( t );
                }
            } ).addGuiTask( new ATask( "show splash" ) {
                public void run() throws Exception {
                    splash.show();
                }
            } ).addTask( new ATask( "initialize skeleton" ) {
                public void run() throws Exception {
                    // few hacks
                    Preferences prefs = Preferences.load();
                    JaLingoLookAndFeel.install( prefs.getFontSize() , prefs.getFontFace());
                    applyJEditorPaneStyleHack( prefs.getFontSize() , prefs.getFontFace() );

                    skeleton = new Skeleton( prefs );
                }
            } ).addGuiTask( new ATask( "hide splash, show skeleton" ) {
                public void run() throws Exception {
                    splash.hide();
                    skeleton.show();
                }
            } );
            taskSequence.runAndWaint();

            LOG.info( "Started within " + measurer );
        } catch ( Throwable t ) {
            processThrowableAndExit( t );
        }
    }

    private void applyJEditorPaneStyleHack( int fontSize, String fontFace ) {
        StyleSheet ss = new StyleSheet();
        try {
            ss.loadRules( new StringReader( new CssHelper( fontSize, fontFace ).asString()), null );
        } catch ( IOException e ) {
            States.shouldNeverReachHere( e );
        }
        // set static style, so all JEditorPanes wil have new style
        new HTMLEditorKit().getStyleSheet().addStyleSheet( ss );
    }

    private void processThrowableAndExit( Throwable t ) {
        if ( t instanceof LockedException ) {
            splash.showAlreadyLockedErrorMessage();
        } else if ( t instanceof UnknownCacheVersionException ) {
            splash.showUnknownVersionErrorMessage( (UnknownCacheVersionException) t );
        } else {
            LOG.error( "Error occured during start" );
            Messages.internalError( splash == null ? null : splash.getDialog(), t );
        }
        System.exit( 1 );
    }

    public static void main( String[] args ) {
        System.getProperties().setProperty( "swing.aatext", "true" );

        // needed because of bug that was introduced by hack on Popup,
        // that is needed to show hand cursor for data tips on lists
        JPopupMenu.setDefaultLightWeightPopupEnabled( false );

        LogInitializer.initialize();

        new JaLingo();
    }
}
