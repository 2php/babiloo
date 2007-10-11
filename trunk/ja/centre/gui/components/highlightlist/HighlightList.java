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
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Vector;

public class HighlightList extends JList {
	private int highlightedIndex = -1;

	public HighlightList(ListModel dataModel) {
		super(dataModel);
		initialize();
	}

	public HighlightList(final Object[] listData) {
		super(listData);
		initialize();
	}

	public HighlightList(final Vector<?> listData) {
		super(listData);
		initialize();
	}

	public HighlightList() {
		initialize();
	}

	private void initialize() {
		setPrototypeCellValue("Index 1234567890qypjQYPJ");
		
		final MouseAdapter mouseAdapter =  new MouseAdapter() {

			@Override
			public void mouseExited(MouseEvent e) {
				highlight(-1);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				highlight(e.getPoint());
			}
				
			@Override
			public void mouseMoved(MouseEvent e) {
				highlight(e.getPoint());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				highlight(e.getPoint());
			}

		};
		
		addMouseListener(mouseAdapter);
		
		addMouseMotionListener(mouseAdapter);
		
		setCellRenderer(new GenericListCellRenderer());
		/*
		 * this code doesn't work properly because JList would take up mouse
		 * wheel events and JScrollPane has no oppotunities to be scrollable.
		 */

		/*
		 * addMouseWheelListener( new MouseWheelListener() { public void
		 * mouseWheelMoved( MouseWheelEvent e ) { highlight( e ); } } );
		 */
	}

	private JScrollPane scrollPane;

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	/**
	 * Register containing scroll pane with the list so that
	 * it can know when user move the mouse wheel on scroll pane.
	 * @param newScrollPane - containing scroll pane
	 */
	public void registerScrollPane(JScrollPane newScrollPane) {
		final MouseWheelListener mouseWheelListener = new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				assert scrollPane != null; // it certainly can not be null
				highlight( new Point(
						e.getX() + scrollPane.getHorizontalScrollBar().getValue(), 
						e.getY() + scrollPane.getVerticalScrollBar().getValue() ) );
			}
		};

		if (scrollPane != newScrollPane) {
			if (scrollPane != null) {
				scrollPane.removeMouseWheelListener(mouseWheelListener);
			}
			if (newScrollPane != null) {
				newScrollPane.addMouseWheelListener(mouseWheelListener);
			}
			this.scrollPane = newScrollPane;
		}
	}

	private void highlight(Point p) {
		highlight(p.y / getFixedCellHeight());
	}

	private void highlight(int newIndex) {
		if (this.highlightedIndex != newIndex) {
			int oldIndex = this.highlightedIndex;
			this.highlightedIndex = newIndex;

			if (oldIndex >= 0 && oldIndex < getModel().getSize()) {
				enqueCellRepaint(oldIndex);
			}

			if (newIndex >= 0 && newIndex < getModel().getSize()) {
				enqueCellRepaint(newIndex);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			} else {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	private void enqueCellRepaint(int cellIndex) {
		assert cellIndex >= 0 && cellIndex < getModel().getSize();
		repaint(0, cellIndex * getFixedCellHeight(), getWidth(), getFixedCellHeight());
	}

	private class GenericListCellRenderer extends DefaultListCellRenderer {
		public JLabel getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list,
					value, index, isSelected, cellHasFocus);

			if (highlightedIndex == index) {
				Color color = ColorUtil.middle(label.getBackground(), UIManager
						.getColor("MenuItem.selectionBackground"));

				// label.setForeground( Color.WHITE );
				label.setBackground(color); // TODO cache color results?;
				// label.setText( "<html><u>" + label.getText() + "</u></html>"
				// );
				// label.setBorder( BorderFactory.createLineBorder( Color.GRAY
				// ));
			}

			return label;
		}
	}

	public static void main(String[] args) {
		//JaLingoLookAndFeel.install(14);

		HighlightList list = new HighlightList(new Integer[] { 1, 2, 3, 4, 5,
				6, 7, 8, 9 });

		JFrame frame = new JFrame("HighlightList Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JScrollPane scrollPane = Components.scrollVertical(list);
		list.registerScrollPane(scrollPane);
		frame.setContentPane(scrollPane);
		frame.setSize(150, 100);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}
}
