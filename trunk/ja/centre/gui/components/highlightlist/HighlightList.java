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

package ja.centre.gui.components.highlightlist;

import ja.centre.gui.util.ColorUtil;
import ja.lingo.application.util.Components;
import ja.lingo.application.util.plaf.JaLingoLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

/**
 * Test comment
 */
public class HighlightList extends JList {
    private int lastHighLightedIndex = -1;

    public HighlightList( ListModel dataModel ) {
        super( dataModel );
        initialize();
    }

    public HighlightList( final Object[] listData ) {
        super( listData );
        initialize();
    }

    public HighlightList( final Vector<?> listData ) {
        super( listData );
        initialize();
    }

    public HighlightList() {
        initialize();
    }

    private void initialize() {
        setPrototypeCellValue( "Index 1234567890qypjQYPJ" );

        addMouseListener( new MouseAdapter() {
            public void mouseExited( MouseEvent e ) {
                removeLastHighlighted();
            }
        } );

        addMouseMotionListener( new MouseMotionListener() {
            public void mouseDragged( MouseEvent e ) {
                highlight( e );
            }

            public void mouseMoved( MouseEvent e ) {
                highlight( e );
            }
        } );
/*
        addMouseWheelListener( new MouseWheelListener() {
            public void mouseWheelMoved( MouseWheelEvent e ) {
                highlight( e );
            }
        } );
*/

        setCellRenderer( new GenericListCellRenderer() );
    }

    private void removeLastHighlighted() {
        int backHighlightIndex = lastHighLightedIndex;
        lastHighLightedIndex = -1;

        if ( backHighlightIndex >= 0 ) {
            enqueCellRepaint( backHighlightIndex );
        }
    }

    private void highlight( MouseEvent e ) {
        int index = e.getY() / getFixedCellHeight();

        if ( lastHighLightedIndex == index ) {
            return;
        }

        if ( index < getModel().getSize() ) {
            // enque redraw of old and new highlighted cells
            if ( lastHighLightedIndex >= 0 ) {
                enqueCellRepaint( lastHighLightedIndex );
            }

            enqueCellRepaint( index );

            lastHighLightedIndex = index;

            setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
        } else {
            if ( lastHighLightedIndex >= 0 ) {
                enqueCellRepaint( lastHighLightedIndex );

                lastHighLightedIndex = -1;
            }

            setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
        }
    }

    private void enqueCellRepaint( int cellIndex ) {
        if ( cellIndex < 0 ) {
            throw new IndexOutOfBoundsException( "Can't render call with index " + cellIndex );
        }

        repaint( 0, cellIndex * getFixedCellHeight(),
                getWidth(), getFixedCellHeight() );
    }

    private class GenericListCellRenderer extends DefaultListCellRenderer {
        public JLabel getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
            JLabel label = (JLabel) super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );

            if ( lastHighLightedIndex == index ) {
                Color color = ColorUtil.middle(
                        label.getBackground(),
                        UIManager.getColor( "MenuItem.selectionBackground" ) );

                //label.setForeground( Color.WHITE );
                label.setBackground( color ); // TODO cache color results?;
                //label.setText( "<html><u>" + label.getText() + "</u></html>" );
                //label.setBorder( BorderFactory.createLineBorder( Color.GRAY ));
            }

            return label;
        }
    }

    public static void main( String[] args ) {
        JaLingoLookAndFeel.install( 14 );

        HighlightList list = new HighlightList( new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 } );

        JFrame frame = new JFrame( "HighlightList Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( Components.scrollVertical( list ) );
        frame.setSize( 150, 300 );
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );

    }
}
