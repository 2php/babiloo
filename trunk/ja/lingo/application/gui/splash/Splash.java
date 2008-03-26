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

package ja.lingo.application.gui.splash;

import ja.centre.gui.resources.Resources;
import ja.lingo.application.util.Gaps;
import ja.lingo.application.util.JaLingoColors;
import ja.lingo.application.util.messages.Messages;
import ja.lingo.application.util.plaf.JaLingoLookAndFeel;
import ja.lingo.application.util.starlabel.StarLabel;
import ja.lingo.engine.UnknownCacheVersionException;

import javax.swing.*;
import java.awt.*;

public class Splash {
    private Resources resources = Resources.forProperties( Splash.class );

    private JDialog dialog;
    private StarLabel starLabel;

    //private BufferedImage back;
    //private static final float FADE_TIME = 2000f;

    public Splash() {
        //JLabel label = new JLabel( resources.icon( "splash" ) );

        // NOTE: workaround against JDialog icon
        JFrame dummyFrame = new JFrame();
        dummyFrame.setIconImage( resources.icon( "title" ).getImage() );

        JLabel copyLabel = new JLabel( resources.text( "copyright" ), JLabel.CENTER );
        copyLabel.setFont( new Font( "Tahoma", Font.PLAIN, 12 ) );
        copyLabel.setForeground( Color.WHITE );
        Gaps.applyBorder5( copyLabel );

        starLabel = new StarLabel( resources.text( "title" ), true );
        starLabel.setLayout( new BorderLayout() );
        starLabel.add( copyLabel, BorderLayout.SOUTH );
        starLabel.setBorder( BorderFactory.createLineBorder( JaLingoColors.COLOR ) );

        dialog = new JDialog( dummyFrame );
        dialog.setTitle( resources.text( "title" ) );
        dialog.setContentPane( starLabel );
        dialog.setSize( 300, 250 );
        dialog.setLocationRelativeTo( null );
        dialog.setUndecorated( true );

        /*try {
            Robot robot = new Robot( dummyFrame.getGraphicsConfiguration().getDevice() );
            back = robot.createScreenCapture( dialog.getBounds() );
        } catch ( AWTException e ) {
            LOG.warn( "Could not create robot", e );
        }

        final long started = System.currentTimeMillis();
        dialog.setGlassPane( new JComponent() {
            public void paint( Graphics g ) {
                if ( back != null ) {
                    long passed = System.currentTimeMillis() - started;
                    if ( passed <= FADE_TIME ) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1 - passed / FADE_TIME ) );
                        g2.drawImage( back, 0, 0, null );
                    }
                }
            }
        } );
        dialog.getGlassPane().setVisible( true );
        */
    }

    public void show() {
        dialog.setVisible( true );
        starLabel.start();
    }

    public void hide() {
        dialog.setVisible( false );
        starLabel.stop();
    }

    public JDialog getDialog() {
        return dialog;
    }

    public void showAlreadyLockedErrorMessage() {
        Messages.info( dialog,
                resources.text( "alreadyLocked" ),
                resources.text( "alreadyLockedTitle" ) );
    }

    public void showUnknownVersionErrorMessage( UnknownCacheVersionException e ) {
        Messages.info( dialog,
                resources.text( "unknownCacheVersion", e.getExistingCacheVersion(), e.getCurrentCacheVersion() ),
                resources.text( "unknownCacheVersionTitle" ) );
    }

    public static void main( String[] args ) {
        JaLingoLookAndFeel.install( 14 , "SansSerif");

        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                new Splash().show();
            }
        }
        );
    }
}
