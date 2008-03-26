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

package ja.lingo.application.util.plaf.theme;

import ja.centre.gui.util.ColorUtil;
import ja.lingo.application.model.Preferences;
import ja.lingo.application.util.JaLingoColors;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

public class RainyTheme extends DefaultMetalTheme {
    private final static ColorUIResource WHITE = new ColorUIResource( 0xECE9D8 );

    private final static ColorUIResource PRIMARY_1 = new ColorUIResource( 0x666666 );
    private final static ColorUIResource PRIMARY_2 = new ColorUIResource( 0x999999 );
    private final static ColorUIResource PRIMARY_3 = new ColorUIResource( JaLingoColors.COLOR );

    private final static ColorUIResource SECONDARY_1 = new ColorUIResource( 0x666666 );
    private final static ColorUIResource SECONDARY_2 = new ColorUIResource( 0xAAAAAA ); // c5def5
    private final static ColorUIResource SECONDARY_3 = new ColorUIResource( WHITE );

    private FontUIResource font;
    private FontUIResource fontSmaller;

    public String getName() {
        return "JaLingo Rainy Theme";
    }

    public RainyTheme( int fontSize , String fontFace) {
        font = new FontUIResource( fontFace, 0, fontSize );
        fontSmaller = new FontUIResource( font.deriveFont( (float) fontSize - 1 ) );
    }

    protected ColorUIResource getPrimary1() {
        return PRIMARY_1;
    }
    protected ColorUIResource getPrimary2() {
        return PRIMARY_2;
    }
    protected ColorUIResource getPrimary3() {
        return PRIMARY_3;
    }

    protected ColorUIResource getSecondary1() {
        return SECONDARY_1;
    }
    protected ColorUIResource getSecondary2() {
        return SECONDARY_2;
    }
    protected ColorUIResource getSecondary3() {
        return SECONDARY_3;
    }

    public FontUIResource getControlTextFont() {
        return font;
    }
    public FontUIResource getSystemTextFont() {
        return font;
    }
    public FontUIResource getUserTextFont() {
        return font;
    }
    public FontUIResource getWindowTitleFont() {
        return font;
    }
    public FontUIResource getSubTextFont() {
        return font;
    }
    public FontUIResource getMenuTextFont() {
        return fontSmaller;
    }

    // NOTE: for menu
    public ColorUIResource getMenuSelectedForeground() {
        return WHITE;
    }
    public ColorUIResource getMenuSelectedBackground() {
        return getPrimary3();
    }
    public ColorUIResource getAcceleratorSelectedForeground() {
        return WHITE;
    }

    // NOTE: for progress bar
    public ColorUIResource getPrimaryControlShadow() {
        return getPrimary3();
    }

    public ColorUIResource getHighlightedTextColor() {
        return WHITE;
    }

    public void addCustomEntriesToTable( UIDefaults table ) {
        super.addCustomEntriesToTable( table );

        table.put( "OptionPane.errorIcon",          createImageIconUIResource( "icons/Error.png" ) );
        table.put( "OptionPane.informationIcon",    createImageIconUIResource( "icons/Inform.png" ) );
        table.put( "OptionPane.warningIcon",        createImageIconUIResource( "icons/Warn.png" ) );
        table.put( "OptionPane.questionIcon",       createImageIconUIResource( "icons/Question.png" ) );

        table.put( "SplitPane.dividerSize", 5 );
        table.put( "SplitPane.border", new BorderUIResource( BorderFactory.createEmptyBorder() ) );
        table.put( "SplitPaneDivider.border", new BorderUIResource( BorderFactory.createEmptyBorder() ) );

        ColorUIResource selectColorUiResource = new ColorUIResource(
                ColorUtil.deriveAlpha( JaLingoColors.COLOR, 0x80 ) );
        table.put( "ToggleButton.select", selectColorUiResource );
        table.put( "Button.select", selectColorUiResource );
        table.put( "ToolTip.hideAccelerator", true );
    }

    private IconUIResource createImageIconUIResource( String relativeResource ) {
        return new IconUIResource( new ImageIcon( getClass().getResource( relativeResource ) ) );
    }
}
