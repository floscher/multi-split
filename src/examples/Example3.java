/*
 * $Id: Example3.java,v 1.15 2005/10/26 14:29:54 hansmuller Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package examples;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JButton;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitPane;

/**
 *
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class Example3 extends Example {
    protected void initialize(String[] ignore) {
	super.initialize(ignore);

        String layoutDef = "(COLUMN (ROW weight=1.0 left (COLUMN middle.top middle middle.bottom) right) bottom)";
        MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);

        MultiSplitPane multiSplitPane = new MultiSplitPane();
	multiSplitPane.setDividerSize(5);
        multiSplitPane.getMultiSplitLayout().setModel(modelRoot);
       	multiSplitPane.add(new JButton("Left Column"), "left");
	multiSplitPane.add(new JButton("Right Column"), "right");
        multiSplitPane.add(new JButton("Bottom Row"), "bottom");
        multiSplitPane.add(new JButton("Middle Column Top"), "middle.top");
        multiSplitPane.add(new JButton("Middle"), "middle");
        multiSplitPane.add(new JButton("Middle Bottom"), "middle.bottom");
                
	Container cp = mainFrame.getContentPane();
	cp.add(multiSplitPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        launch(Example3.class, args);
    }
}
