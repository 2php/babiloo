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

import ja.centre.util.io.lock.LockedException;
import ja.centre.util.measurer.TimeMeasurer;
import ja.lingo.application.gui.actions.Actions;
import ja.lingo.application.gui.drophandler.DropHandler;
import ja.lingo.application.gui.dropzone.DropZoneGui;
import ja.lingo.application.gui.main.MainGui;
import ja.lingo.application.gui.main.MainPanel;
import ja.lingo.application.gui.main.describer.DescriberGui;
import ja.lingo.application.gui.main.export.ExportGui;
import ja.lingo.application.gui.main.help.HelpGui;
import ja.lingo.application.gui.main.history.HistoryGui;
import ja.lingo.application.gui.main.navigator.NavigatorGui;
import ja.lingo.application.gui.main.popout.PopOut;
import ja.lingo.application.gui.main.search.SearchGui;
import ja.lingo.application.gui.main.settings.SettingsGui;
import ja.lingo.application.gui.main.settings.appearance.AppearanceGui;
import ja.lingo.application.gui.main.settings.dictionaries.DictionariesGui;
import ja.lingo.application.gui.main.settings.dictionaries.add.AddGui;
import ja.lingo.application.gui.main.suggest.SuggestGui;
import ja.lingo.engine.Engine;
import ja.lingo.engine.IEngine;
import ja.lingo.engine.UnknownCacheVersionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class Skeleton {
    private static final Log LOG = LogFactory.getLog( Skeleton.class );

    private Model model;

    public Skeleton( Preferences preferences ) throws LockedException, UnknownCacheVersionException, IOException {
        LOG.info( "Initializing engine..." );
        TimeMeasurer measurer = new TimeMeasurer();
        IEngine engine = new Engine();
        LOG.info( "Initializing engine: done within " + measurer );

        LOG.info( "Initializing UI: model and history..." );
        model = new Model( engine, preferences );
        History history = new History();

        LOG.info( "Initializing UI..." );
        measurer = new TimeMeasurer();

        LOG.info( "Initializing UI: actions..." );
        Actions actions = new Actions( preferences, model, history );

        // translation handler (common listener for droppings)
        LOG.info( "Initializing UI: drop handler..." );
        DropHandler dropHandler = new DropHandler( engine, model );

        // main
        LOG.info( "Initializing UI: main..." );
        DescriberGui describerGui = new DescriberGui( model, actions, engine, dropHandler );
        NavigatorGui navigatorGui = new NavigatorGui( model, engine, dropHandler );
        SearchGui searchGui = new SearchGui( model, engine, preferences );

        HistoryGui historyGui = new HistoryGui( model, engine, history );

        MainPanel mainPanel = new MainPanel( actions, describerGui, navigatorGui, historyGui, searchGui );
        MainGui mainGui = new MainGui( model, mainPanel,actions );

        // settings
        LOG.info( "Initializing UI: settings..." );
        DictionariesGui dictionariesGui = new DictionariesGui( model, engine );
        AppearanceGui appearanceGui = new AppearanceGui( model );
        SettingsGui settingsGui = new SettingsGui( actions, model, engine, preferences, mainGui.getFrame(), dictionariesGui, appearanceGui );

        new AddGui( model, engine, settingsGui.getDialog() );

        // other
        LOG.info( "Initializing UI: other..." );
        new HelpGui( model, mainGui.getFrame() );
        new ExportGui( engine, model, mainGui.getFrame() );

        new DropZoneGui( model, actions, dropHandler );

        new SuggestGui( model, engine, describerGui.getGui() );

        new PopOut( model, engine, describerGui.getGui() );
        LOG.info( "Initializing UI: done within " + measurer );

        LOG.info( "Updating UI..." );
        measurer = new TimeMeasurer();
        start();
        LOG.info( "Updating UI: done within " + measurer );
    }

    private void start() {
        model.intialize();
        model.settingsUpdated();
    }

    public void show() {
        model.main_showAtTop();
    }
}
