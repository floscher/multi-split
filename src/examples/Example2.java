/*
 * $Id: Example2.java,v 1.15 2005/10/26 14:29:54 hansmuller Exp $
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
import org.jdesktop.swingx.MultiSplitLayout.Node;
import org.jdesktop.swingx.MultiSplitLayout.Leaf;
import org.jdesktop.swingx.MultiSplitLayout.Divider;
import org.jdesktop.swingx.MultiSplitLayout.Split;
import org.jdesktop.swingx.MultiSplitPane;



/**
 *
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class Example2 extends Example {
    protected void initialize(String[] ignore) {
	super.initialize(ignore);

        /*
        Leaf left = new Leaf("left");
        Leaf right = new Leaf("right");
        left.setWeight(0.5);
        right.setWeight(0.5);
        List children = Arrays.asList(left, new Divider(), right);
        Split modelRoot = new Split();
        modelRoot.setChildren(children);
         */
        String layoutDef = "(ROW (LEAF name=left weight=0.5) (LEAF name=right weight=0.5))";
        Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
                
        MultiSplitPane multiSplitPane = new MultiSplitPane();
        multiSplitPane.getMultiSplitLayout().setModel(modelRoot);
       	multiSplitPane.add(new JButton("Left Component"), "left");
	multiSplitPane.add(new JButton("Right Component"), "right");

	Container cp = mainFrame.getContentPane();
	cp.add(multiSplitPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        launch(Example2.class, args);
    }
}
