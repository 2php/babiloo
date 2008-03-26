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

package ja.lingo.engine.util;

public class CssHelper {
    private String style;

    public CssHelper() {
        this( 14 );
    }

    public CssHelper( int fontSize ) {
    	//TODO get font from preferences
        this( fontSize, "Dialog", "Lucida Sans Unicode" );
    }

    public CssHelper( int fontSize, String fontFace ) {
    	//TODO get font from preferences
        this( fontSize, fontFace, "Lucida Sans Unicode" );
    }

    public CssHelper( int fontSize, String baseFont, String transcriptionFont ) {
        // NOTE: Lucida Sans Unicode has transcription symbols
        // NOTE: span.black is a workaraund to make LI grayed, but its text contents black

        String baseColor = "black";
        String transcriptionColor = "blue";
        String keywordColor = "green";

        style = "body, td, a, span  { font-family: " + baseFont + "; font-size: " + fontSize + "pt; color: " + baseColor +" }\n" +
                "\n" +
                "body { margin: 0px; padding: 0px }\n" +
                "\n" +
                "ol      { margin-left: 25; color: gray; }\n" +
                "ol.1    { list-style-type: upper-roman; }\n" +
                "ol.2    { list-style-type: decimal; }\n" +
                "ol.3    { list-style-type: decimal; }\n" +
                "ol.4    { list-style-type: decimal; }\n" +
                "\n" +
                "\n" +
                "td.section  { background-color: #cccccc; }\n" +
                "\n" +
                "span.black  { color: " + baseColor + "; }\n" +
                "span.trans  { color: " + transcriptionColor + "; font-family: " + transcriptionFont + "; }\n" +
                "span.keywd  { color: " + keywordColor + "; font-style: italic }" +
                "\n" +
                // DSL-specific
                "div         { margin: 0 5 5 5; }\n" +
                "div.m1      { margin-left: 10; }\n" +
                "div.m2      { margin-left: 20; }\n" +
                "div.m3      { margin-left: 30; }";
    }

    public String asString() {
        return style;
    }
}
