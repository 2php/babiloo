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

package ja.lingo.application.util.progress;

import ja.centre.util.assertions.States;
import ja.lingo.application.util.misc.Threads;
import ja.lingo.application.util.plaf.JaLingoLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProgressUtil {
    /**
     * Should be called from EDT only
     * @param dialog
     */
    public static ITitledMonitor start( JDialog dialog ) {
        States.assertIsEDT();

        ProgressComponent content = new ProgressComponent();
        ProgressGlassPane.install( dialog, content.getGui() );
        return content.getController();
    }
    /**
     * Should be called from EDT only
     * @param frame
     */
    public static ITitledMonitor start( JFrame frame ) {
        States.assertIsEDT();

        ProgressComponent content = new ProgressComponent();
        ProgressGlassPane.install( frame, content.getGui() );
        return content.getController();
    }

    /**
     * May be called from any thread
     * @param dialog
     */
    public static void stop( JDialog dialog ) {
        ProgressGlassPane.restore( dialog );
    }
    /**
     * May be called from any thread
     * @param frame
     */
    public static void stop( JFrame frame ) {
        ProgressGlassPane.restore( frame );
    }

    public static void main( String[] args ) {
        JaLingoLookAndFeel.install( 14 , "SansSerif");

        Toolkit.getDefaultToolkit().setDynamicLayout( true );

        final JFrame frame = new JFrame( "ProgressUtil Demo" );

        JButton button = new JButton( "Start progress" );
        button.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                final int STEPS = 100;
                final ITitledMonitor controller = ProgressUtil.start( frame );
                controller.start( 0, 100 );

                Threads.startInBackground( new Runnable() {
                    public void run() {
                        try {
                            for ( int i = 0; i < STEPS; i++ ) {
                                final int i2 = i;
                                SwingUtilities.invokeLater( new Runnable() {
                                    public void run() {
                                        controller.setText( i2 + " of " + STEPS );
                                        controller.update( i2 );
                                    }
                                } );
                                Thread.sleep( 20 );
                            }
                        } catch ( InterruptedException e1 ) {
                            throw new RuntimeException( e1 );
                        }
                        ProgressUtil.stop( frame );
                    }
                } );
            }
        } );

        JEditorPane textPane = new JEditorPane( "text/html", "<html>" +
                "Type any text here. Press \"Start progress\". Resize the window." +
                "<br>" +
                "<br>" +
                "text text text text text text text text text text text text " +
                "text text text text text text text text text text text text " +
                "text text text text text text text text text text text text " +
                "text text text text text text text text text text text text " +
                "text text text text text text text text text text text text " +
                "text text text text text text</html>" );

        frame.getContentPane().setLayout( new BorderLayout() );
        frame.getContentPane().add( button, BorderLayout.NORTH );
        frame.getContentPane().add( textPane, BorderLayout.CENTER );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 400, 300 );
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
    }
}
