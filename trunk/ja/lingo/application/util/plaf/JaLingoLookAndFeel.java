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

package ja.lingo.application.util.plaf;

import com.incors.plaf.kunststoff.KunststoffLookAndFeel;
import ja.lingo.application.util.plaf.theme.RainyGradientTheme;
import ja.lingo.application.util.plaf.theme.RainyTheme;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;

public class JaLingoLookAndFeel extends KunststoffLookAndFeel {
    private static final Log LOG = LogFactory.getLog( JaLingoLookAndFeel.class );

    public static void install( int fontSize , String fontFace) {
        // children dynamic re-layout on resize
        Toolkit.getDefaultToolkit().setDynamicLayout( true );

        // kunstoff

        JaLingoLookAndFeel laf = new JaLingoLookAndFeel();
        setCurrentTheme( new RainyTheme( fontSize , fontFace) );
        setCurrentGradientTheme( new RainyGradientTheme() );

        //LookAndFeel laf = new WindowsLookAndFeel();

        // children LF
        //Plastic3DLookAndFeel laf = new Plastic3DLookAndFeel();
        //Plastic3DLookAndFeel.setMyCurrentTheme( new ExperienceBlue() );

        try {
            //UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
            UIManager.setLookAndFeel( laf );
            //Wrapper.wrap();
        } catch ( Exception e ) {
            LOG.warn( "Could not initialize LF", e );
        }
    }

    protected void initClassDefaults( UIDefaults table ) {
        super.initClassDefaults( table );

        table.put( "EditorPaneUI",      JaLingoEditorPaneUI.class.getName() );
        table.put( "ListUI",            JaLingoListUI.class.getName() );
        table.put( "ToggleButtonUI",    JaLingoToggleButtonUI.class.getName() );
    }

    protected void initSystemColorDefaults( UIDefaults table ) {
        super.initSystemColorDefaults( table );

        table.put( "textHighlight", getCurrentTheme().getTextHighlightColor() );
        table.put( "info",          getCurrentTheme().getControlHighlight() );
    }
}
