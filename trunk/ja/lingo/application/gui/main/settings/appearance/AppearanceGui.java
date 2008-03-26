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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307  USA
 */

package ja.lingo.application.gui.main.settings.appearance;

import java.awt.*;
import javax.swing.*;
import java.lang.String;
import info.clearthought.layout.TableLayout;
import ja.centre.gui.actionbinder.ActionBinder;
import ja.centre.gui.util.BaseGui;
import ja.lingo.application.model.Model;
import ja.lingo.application.model.ModelAdapter;
import ja.lingo.application.model.Preferences;
import ja.lingo.application.util.Gaps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AppearanceGui extends BaseGui {
    private static final Log LOG =
LogFactory.getLog( AppearanceGui.class );

    private static final int DEFAULT_FONT_SIZE = 14;

    private JPanel gui;

    private JCheckBox dropZoneCheckBox;
    private JComboBox fontSizeComboBox;
    private JCheckBox memoryBarCheckBox;
    private JComboBox fontFaceComboBox;

    public AppearanceGui( Model model ) {
        model.addApplicationModelListener( new ModelAdapter() {
            public void settingsUpdated( Preferences preferences ) {
                setSelectedFontSize( preferences.getFontSize() );
                setSelectedFontFace( preferences.getFontFace() );
                dropZoneCheckBox.setSelected( preferences.isDropZoneVisible() );
                memoryBarCheckBox.setSelected( preferences.isMemoryBarVisible() );
            }
        } );

        dropZoneCheckBox  = new JCheckBox( resources.text( "showDropZone" ) );
        fontSizeComboBox  = new JComboBox( new Integer[] { 12, 13, DEFAULT_FONT_SIZE, 15, 16, 17 } );
        memoryBarCheckBox = new JCheckBox( resources.text( "showMemoryBar" ) );

        JPanel fontSizePanel = new JPanel( new FlowLayout( FlowLayout.LEFT, Gaps.GAP5, 0 ) );
        fontSizePanel.add( resources.label( "fontSize" ) );
        fontSizePanel.add( fontSizeComboBox );
        fontSizePanel.add( resources.label( "requiresRestart" ) );

        gui = new JPanel( new TableLayout( new double[][] {
                { TableLayout.PREFERRED },
                {
	                    TableLayout.PREFERRED,  // 0: font face
	                    Gaps.GAP5,
	                    TableLayout.PREFERRED,  // 2: font size
	                    Gaps.GAP5,
                        TableLayout.PREFERRED,  // 4: drop-zone
                        Gaps.GAP5,
                        TableLayout.PREFERRED,  // 6: memory bar
                }
        } ) );



        JPanel fontFacePanel = new JPanel( new FlowLayout( FlowLayout.LEFT, Gaps.GAP5, 0 ) );
        fontFacePanel.add( resources.label( "fontFace" ) );
        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fontFaceComboBox = new JComboBox(gEnv.getAvailableFontFamilyNames());
        fontFaceComboBox.setMaximumRowCount(10);
        fontFacePanel.add( fontFaceComboBox );
        fontFacePanel.add( resources.label( "requiresRestart" ) );

        gui.add( fontFacePanel,  "0, 0" );
        gui.add( fontSizePanel,     "0, 2" );
        gui.add( dropZoneCheckBox,  "0, 4" );
        gui.add( memoryBarCheckBox, "0, 6" );
        Gaps.applyBorder5( gui );

        ActionBinder.bind( this );
    }

    public JComponent getGui() {
        return gui;
    }

    private void setSelectedFontSize( int fontSize ) {
        ComboBoxModel model = fontSizeComboBox.getModel();
        for ( int i = 0; i < model.getSize(); i++ ) {
            Integer value = (Integer) model.getElementAt( i );
            if ( value.equals( fontSize ) ) {
                fontSizeComboBox.setSelectedItem( value );
                return;
            }
        }
        // TODO refactoring needed: move out default size + allowed sizes
        LOG.warn( "Incorrect font size: " + fontSize + ", setting size to default" );
        setSelectedFontSize( DEFAULT_FONT_SIZE ); // NOTE potential recursion appearance on re-factoring
    }

    public int getFontSize() {
        return (Integer) fontSizeComboBox.getSelectedItem();
    }

    private void setSelectedFontFace( String fontFace ) {
        ComboBoxModel model = fontFaceComboBox.getModel();
        for ( int i = 0; i < model.getSize(); i++ ) {
            String value = (String) model.getElementAt( i );
            System.out.println(value);
            System.out.println(fontFace);
            if ( value.equals( fontFace ) ) {
                fontFaceComboBox.setSelectedItem( value );
                return;
            }
        }
        // TODO refactoring needed: move out default size + allowed sizes
        LOG.warn( "Incorrect font face: " + fontFace + ", setting font to default" );
        setSelectedFontFace( "SansSerif" ); // NOTE potential recursion appearance on re-factoring
    }

    public String getFontFace() {
        return (String) fontFaceComboBox.getSelectedItem();
    }
    public boolean isDropZoneVisible() {
        return dropZoneCheckBox.isSelected();
    }
    public boolean isMemoryBarVisible() {
        return memoryBarCheckBox.isSelected();
    }
}
