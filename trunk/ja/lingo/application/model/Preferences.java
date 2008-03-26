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

package ja.lingo.application.model;

import ja.centre.util.beans.BeanPersister;
import ja.lingo.engine.util.EngineFiles;

import java.awt.*;
import java.io.Serializable;

public class Preferences implements Serializable {
    // preinitialized with defaults
    private Rectangle mainWindowBounds = new Rectangle( -1, -1, 600, 500 );

    private int navigatorDividerLocation = 160;

    private int fontSize = 14;
    private String fontFace = "SansSerif";

    private boolean dropZoneVisible = true;
    private Point dropZoneLocation = new Point( 300, 300 );

    private boolean memoryBarVisible;
    private boolean searchCaseSensetive;

    public Rectangle getMainWindowBounds() {
        return mainWindowBounds;
    }
    public void setMainWindowBounds( Rectangle mainWindowBounds ) {
        this.mainWindowBounds = mainWindowBounds;
    }

    public int getNavigatorDividerLocation() {
        return navigatorDividerLocation;
    }
    public void setNavigatorDividerLocation( int navigatorDividerLocation ) {
        this.navigatorDividerLocation = navigatorDividerLocation;
    }

    public int getFontSize() {
        return fontSize;
    }
    public void setFontSize( int fontSize ) {
        this.fontSize = fontSize;
    }

    public String getFontFace() {
        return fontFace;
    }
    public void setFontFace( String fontFace ) {
        this.fontFace = fontFace;
    }

    public boolean isDropZoneVisible() {
        return dropZoneVisible;
    }
    public void setDropZoneVisible( boolean dropZoneVisible ) {
        this.dropZoneVisible = dropZoneVisible;
    }

    public Point getDropZoneLocation() {
        return dropZoneLocation;
    }
    public void setDropZoneLocation( Point dropZoneLocation ) {
        this.dropZoneLocation = dropZoneLocation;
    }

    public boolean isMemoryBarVisible() {
        return memoryBarVisible;
    }
    public void setMemoryBarVisible( boolean memoryBarVisible ) {
        this.memoryBarVisible = memoryBarVisible;
    }

    public boolean isSearchCaseSensetive() {
        return searchCaseSensetive;
    }
    public void setSearchCaseSensetive( boolean searchCaseSensetive ) {
        this.searchCaseSensetive = searchCaseSensetive;
    }

    public static Preferences load() {
        return BeanPersister.load( Preferences.class, calculatePreferencesFileName() );
    }
    public static void save( Preferences preferences ) {
        BeanPersister.save( preferences, calculatePreferencesFileName() );
    }

    private static String calculatePreferencesFileName() {
        return EngineFiles.calculateInWorking( "preferences.xml" );
    }
}
