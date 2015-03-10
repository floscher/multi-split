/*
 * $Id: Example4.java,v 1.15 2005/10/26 14:29:54 hansmuller Exp $
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
import java.awt.Rectangle;
import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.ExceptionListener;
import java.beans.Expression;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import javax.swing.JButton;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitLayout.Node;
import org.jdesktop.swingx.MultiSplitPane;

/**
 *
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class Example4 extends Example {
    MultiSplitPane multiSplitPane = null;
    File file = null;

    /* By default, if an exception occurs in XMLEncoder.writeObject(),
     * or XMLDecoder.readObject(), an error message is printed and the
     * system carries on.  We punt instead.
     */
    private static class AbortExceptionListener implements ExceptionListener {
	public void exceptionThrown(Exception e) {
	    throw new Error(e);
	}
    }

    protected void initialize(String[] ignoreArgs) {
	super.initialize(ignoreArgs);

        multiSplitPane = new MultiSplitPane();
	MultiSplitLayout mspLayout = multiSplitPane.getMultiSplitLayout();

	Node model = null;
	XMLDecoder d = null;
	try {
	    d = new XMLDecoder(openResourceInput("Example4.xml"));
	    d.setExceptionListener(new AbortExceptionListener());
	    model = (Node)(d.readObject());
	}
	catch (Throwable ignore) { }
	finally {
	    if (d != null) { d.close(); }
	}

	if (model == null) {
	    String layoutDef = "(COLUMN (ROW weight=1.0 left (COLUMN middle.top middle middle.bottom) right) bottom)";
	    model = MultiSplitLayout.parseModel(layoutDef);
	}
	else {
	    mspLayout.setFloatingDividers(false);

	    /* This is just a hack.  It fails to restore
	     * the mainFrame to its original location and
	     * we probably don't really want to change the 
	     * multiSplitPane's preferred size permanently.
	     * A general solution must also contend with 
	     * having the mainFrame restored on a different 
	     * display configuration as well as enabling the
	     * developer to constrain the initial position 
	     * of the mainFrame, e.g. "center it".
	     */
	    multiSplitPane.setPreferredSize(model.getBounds().getSize());
	}

        mspLayout.setModel(model);
       	multiSplitPane.add(new JButton("Left Column"), "left");
	multiSplitPane.add(new JButton("Right Column"), "right");
        multiSplitPane.add(new JButton("Bottom Row"), "bottom");
        multiSplitPane.add(new JButton("Middle Column Top"), "middle.top");
        multiSplitPane.add(new JButton("Middle"), "middle");
        multiSplitPane.add(new JButton("Middle Bottom"), "middle.bottom");
                
	Container cp = mainFrame.getContentPane();
	cp.add(multiSplitPane, BorderLayout.CENTER);
    }
    
    protected void quit() {
	super.quit();
	XMLEncoder e = null;
	try {
	    e = new XMLEncoder(openResourceOutput("Example4.xml"));
	    e.setPersistenceDelegate(Rectangle.class, new RectanglePD());
	    e.setExceptionListener(new AbortExceptionListener());
	    Node model = multiSplitPane.getMultiSplitLayout().getModel();
	    e.writeObject(model);
	}
	catch (Exception ignore) { }
	finally {
	    if (e != null) { e.close(); }
	}
    }

    /* There are some (old) Java classes that aren't proper beans.  Rectangle
     * is one of these.  When running within the secure sandbox, writing a 
     * Rectangle with XMLEncoder causes a security exception because 
     * DefaultPersistenceDelegate calls Field.setAccessible(true) to gain
     * access to private fields.  This is a workaround for that problem.
     * A bug has been filed, see http://monaco.sfbay/detail.jsf?cr=4741757  
     */
    private static class RectanglePD extends DefaultPersistenceDelegate {
	public RectanglePD() {
	    super(new String[]{"x", "y", "width", "height"});
	}
	protected Expression instantiate(Object oldInstance, Encoder out) {
	    Rectangle oldR = (Rectangle)oldInstance;
	    Object[] constructorArgs = new Object[]{
		oldR.x, oldR.y, oldR.width, oldR.height
	    };
	    return new Expression(oldInstance, oldInstance.getClass(), "new", constructorArgs);
	}
    }

    public static void main(String[] args) {
        launch(Example4.class, args);
    }    
}
