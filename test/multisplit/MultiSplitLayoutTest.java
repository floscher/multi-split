/*
 * MultiSplitLayoutTest.java
 * JUnit based test
 *
 * Created on February 10, 2006, 2:06 PM
 */

package multisplit;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import junit.framework.*;
import org.jdesktop.swingx.MultiSplitLayout.Leaf;
import org.jdesktop.swingx.MultiSplitLayout.Divider;
import org.jdesktop.swingx.MultiSplitLayout.Split;
import org.jdesktop.swingx.MultiSplitLayout.InvalidLayoutException;
import org.jdesktop.swingx.MultiSplitLayout.Node;
import org.jdesktop.swingx.MultiSplitLayout;



/**
 *
 * @author Hans Muller
 */
public class MultiSplitLayoutTest extends TestCase {
    
    public MultiSplitLayoutTest(String testName) {
        super(testName);
    }
    
    public void testSetModelIllegalArgument() {
	MultiSplitLayout msl = new MultiSplitLayout();
        try {
	    msl.setModel(new Divider());
            fail("Should throw IllegalArgumentException [1]");
        } 
	catch(IllegalArgumentException e) { }
        try {
	    msl.setModel(null);
            fail("Should throw IllegalArgumentException [2]");
        } 
	catch(IllegalArgumentException e) { }
    }


    public void testSetDividerSizeIllegalArgument() {
	MultiSplitLayout msl = new MultiSplitLayout();
	msl.setDividerSize(0); // OK
        try {
	    msl.setDividerSize(-1);
            fail("Should throw IllegalArgumentException");
        } 
	catch(IllegalArgumentException e) { }
    }



    private void checkNodeBasics(Node node) {
	Rectangle zeroRectangle = new Rectangle(0, 0, 0, 0);
	assertEquals("default weight is 0.0", node.getWeight(), 0.0);
	assertEquals("default bounds are 0,0 0x0", node.getBounds(), zeroRectangle);
	assertNull("default parent is null", node.getParent());
	try {
	    node.setBounds(null);
	    fail("null bounds Rectangle, should throw IllegalArgumentException");
	}
	catch (IllegalArgumentException e) { }

    }

    private void checkNodeWeight(Node node) {
	try {
	    node.setWeight(1.1);
	    fail("should throw IllegalArgument exception 1");
	}
	catch(IllegalArgumentException e) { }
	try {
	    node.setWeight(1.1);
	    fail("should throw IllegalArgument exception 2");
	}
	catch(IllegalArgumentException e) { }
	node.setWeight(0.0);  // OK
	node.setWeight(1.0);  // OK
    }

    public void testSplitBasics() {
	Split split = new Split();
	checkNodeBasics(split);
	checkNodeWeight(split);
	assertTrue("rowLayout should be true by default", split.isRowLayout());
	assertEquals("no children by default", split.getChildren().size(), 0);

	/* Check the children property.  Setter should
	 * defensively copy children list, getter should return
	 * a copy (could be read-only) that doesn't change when 
	 * the Split's children property is reset.  Setter 
	 * should update old/new children's parent property.
	 */
        Leaf one = new Leaf("one");
        Leaf two = new Leaf("two");
	Divider divider12 = new Divider();
	List<Node> children1 = new ArrayList<Node>(Arrays.asList(one, divider12, two));
	split.setChildren(children1);
	for(Node child : split.getChildren()) {
	    assertEquals("Split child's parent should be Split", child.getParent(), split);
	}
	children1.clear();  // shouldn't effect Split's children
	assertEquals("getChildren should defensively copy [1]", split.getChildren().size(), 3);
	List<Node> children2 = Arrays.asList(one, divider12, two);
	int i = 0;
	for(Node child : split.getChildren()) {
	    assertEquals("getChildren should defensively copy [2]", child, children2.get(i++));
	}
        Leaf foo = new Leaf("foo");
        Leaf bar = new Leaf("bar");
	Divider dividerfb = new Divider();
	List<Node> children3 = Arrays.asList(foo, dividerfb, bar);	
	split.setChildren(children3);
	for(Node child : children2) {
	    assertNull("old Split child parent should be null", child.getParent());
	}
	for(Node child : children3) {
	    assertEquals("new Split child parent should be Split", child.getParent(), split);
	}

	/* setChildren should throw IllegalArgumentException if 
	 * incoming List is null.
	 */
	try {
	    split.setChildren(null);
	    fail("null children List, should throw IllegalArgumentException");
	}
	catch (IllegalArgumentException e) { }
    }

    public void testLeafBasics() {
	Rectangle zeroRectangle = new Rectangle(0, 0, 0, 0);
	Leaf leaf = new Leaf();
	checkNodeBasics(leaf);
	checkNodeWeight(leaf);
	assertEquals("default name should be \"\"", leaf.getName(), "");
	try {
	    leaf.setName(null);
	    fail("setName: null, should throw IllegalArgumentException");
	}
	catch (IllegalArgumentException e) { }
	try {
	    new Leaf(null);
	    fail("Constructor: null Leaf name, should throw IllegalArgumentException");
	}
	catch (IllegalArgumentException e) { }
    }

    public void testDividerBasics() {
	Divider divider = new Divider();
	checkNodeBasics(divider);
	try {
	    divider.setWeight(1.0);
	    fail("should throw UnsupportedOperation exception");
	}
	catch(UnsupportedOperationException e) {
	}
    }

    /**
     * MultiSplitLayout model is just a Leaf node.
     */
    public void testLeafOnly() {
	MultiSplitLayout msl = new MultiSplitLayout();
	Leaf leaf = new Leaf("leaf");
	Component leafComponent = new JLabel("leafComponent");
	msl.setModel(leaf);
	JPanel panel = new JPanel(msl);
	panel.add(leafComponent, "leaf");
	panel.setBounds(50, 60, 100, 200);
	panel.doLayout();

	assertEquals(panel.getSize(), leafComponent.getSize());
	assertEquals(new Point(0, 0), leafComponent.getLocation());
	assertEquals(leafComponent.getBounds(), leaf.getBounds());

	Border border = new EmptyBorder(10, 20, 30, 40); // top left bottom right
	panel.setBorder(border);
	panel.doLayout();
	
	Insets insets = border.getBorderInsets(panel);
	int x = insets.left;
	int y = insets.top;
	int width = panel.getWidth() - (insets.left + insets.right);
	int height = panel.getHeight() - (insets.top + insets.bottom);

	assertEquals(new Rectangle(x, y, width, height), leafComponent.getBounds());
	assertEquals(leafComponent.getBounds(), leaf.getBounds());
    }


    /**
     * See private method MultiSplitLayout.checkLayout()
     */
    public void testCheckModelValidity() {
        Leaf one = new Leaf("one");
        Leaf two = new Leaf("two");
        Leaf three = new Leaf("three");
	Divider divider12 = new Divider();
	Divider divider23 = new Divider();
	Split split = new Split();
	MultiSplitLayout msl = new MultiSplitLayout();
	msl.setModel(split);

	JPanel panel = new JPanel(msl);
	panel.add(new JLabel("one"), "one");
	panel.add(new JLabel("two"), "two");
	panel.add(new JLabel("three"), "three");

	try {
	    // Split node must have at least 3 children
	    split.setChildren((List)(Arrays.asList(one)));
	    panel.doLayout();
            fail("Should throw InvalidLayoutException [1]");
	}
	catch(InvalidLayoutException e) {
	}
	// with three children, no exception
	split.setChildren(Arrays.asList(one, divider12, two));
	panel.doLayout();


	try {
	    // No Divider in between nodes 2,3 - invalid
	    split.setChildren((List)(Arrays.asList(one, divider12, two, three)));
	    panel.doLayout();
            fail("Should throw InvalidLayoutException [2]");
	}
	catch(InvalidLayoutException e) {
	}
	// with two dividers children, no exception
	split.setChildren(Arrays.asList(one, divider12, two, divider23, three));
	panel.doLayout();

	try {
	    // sibling weights exceed 1.0 - invalid
	    one.setWeight(0.5);
	    two.setWeight(0.6);
	    three.setWeight(0.0);
            panel.doLayout();
            fail("Should throw InvalidLayoutException [3]");
	}
	catch(InvalidLayoutException e) {
	}
	// valid total sibling weight, no exception
	two.setWeight(0.5);
	panel.doLayout();


	try {
	    // divider in the wrong place - invlaid
	    split.setChildren(Arrays.asList(divider12, one, two));
            panel.doLayout();
            fail("Should throw InvalidLayoutException 4");
	}
	catch(InvalidLayoutException e) {
	}
    }

    public void testSimpleRowSplit() {
        Leaf one = new Leaf("one");  // left 
        Leaf two = new Leaf("two");  // right
	Divider divider12 = new Divider();
	Split split = new Split();
	split.setChildren(Arrays.asList(one, divider12, two));
	MultiSplitLayout msl = new MultiSplitLayout();
	msl.setModel(split);

	JPanel panel = new JPanel(msl);
	JLabel labelOne = new JLabel("one");
	JLabel labelTwo = new JLabel("two");
	panel.add(labelOne, "one");
	panel.add(labelTwo, "two");

	panel.setBounds(50, 60, 100, 200);
	panel.doLayout();

	/* Split's size should match panel and the split's
	 * origin should be 0,0
	 */
	{
	    Dimension d1 = panel.getSize();
	    Dimension d2 = split.getBounds().getSize();
	    assertEquals("Split's size should match panel", d1, d2);
	}
	{
	    Point p1 = new Point(0, 0);
	    Point p2 = split.getBounds().getLocation();
	    assertEquals("Split's origin should be 0,0", p1, p2);
	}
	
	/* Height of all split children should be the same as the
	 * panel's height.  Width of Leaf "one" and JLabel "one" 
	 * should be the same as JLabel one's preferred width.
	 * Width of the divider12 should be dividerSize and
	 * Leaf/JLabel "two" should fill the remaining
	 * width. Y coordinate of all components should be 0
	 * and the X coordinate of Node one should be 0.
	 */

	int x1 = 0;
	for(Node node : new Node[]{one, divider12, two}) {
	    int h1 = panel.getHeight();
	    int h2 = node.getBounds().height;
	    int x2 = node.getBounds().x;
	    int y = node.getBounds().y;
	    assertEquals("Height of Split row nodes should be equal", h1, h2);
	    assertEquals("Y of Split row nodes should be 0", y, 0);
	    assertEquals("X of Split row node, right of previous node", x1, x2);
	    x1 = node.getBounds().x + node.getBounds().width;
	}
	{
	    int w1 = one.getBounds().width;
	    int w2 = labelOne.getPreferredSize().width;
	    assertEquals("Node one's width should equal label's preferred width", w1, w2);
	}
	{
	    int w1 = divider12.getBounds().width;
	    int w2 = msl.getDividerSize();
	    assertEquals("Divider width should equal dividerSize", w1, w2);
	}
	{
	    int w1 = panel.getWidth() - (one.getBounds().width + divider12.getBounds().width);
	    int w2 = two.getBounds().width;
	    assertEquals("Remaining space to be allocated to Node two ", w1, w2);
	}
	{
	    Rectangle r1 = labelOne.getBounds();
	    Rectangle r2 = one.getBounds();
	    assertEquals("one: leaf/component bounds should match", r1, r2);
	}
	{
	    Rectangle r1 = labelTwo.getBounds();
	    Rectangle r2 = two.getBounds();
	    assertEquals("two: leaf/component bounds should match", r1, r2);
	}
    }


    public void testSimpleColumnSplit() {
        Leaf one = new Leaf("one");  // top
        Leaf two = new Leaf("two");  // bottom
	Divider divider12 = new Divider();
	Split split = new Split();
	split.setChildren(Arrays.asList(one, divider12, two));
	split.setRowLayout(false);  // COLUMN
	MultiSplitLayout msl = new MultiSplitLayout();
	msl.setModel(split);

	JPanel panel = new JPanel(msl);
	JLabel labelOne = new JLabel("one");
	JLabel labelTwo = new JLabel("two");
	panel.add(labelOne, "one");
	panel.add(labelTwo, "two");

	panel.setBounds(5, 6, 200, 300);
	panel.doLayout();

	/* Split's size should match panel and the split's
	 * origin should be 0,0
	 */
	{
	    Dimension d1 = panel.getSize();
	    Dimension d2 = split.getBounds().getSize();
	    assertEquals("Split's size should match panel", d1, d2);
	}
	{
	    Point p1 = new Point(0, 0);
	    Point p2 = split.getBounds().getLocation();
	    assertEquals("Split's origin should be 0,0", p1, p2);
	}
	
	/* Width of all split children should be the same as the
	 * panel's width.  Height of Leaf "one" and JLabel "one" 
	 * should be the same as JLabel one's preferred height.
	 * Height of the divider12 should be dividerSize and
	 * Leaf/JLabel "two" should fill the remaining
	 * height. X coordinate of all components should be 0
	 * and the Y coordinate of Node one should be 0.
	 */

	int y1 = 0;
	for(Node node : new Node[]{one, divider12, two}) {
	    int w1 = panel.getWidth();
	    int w2 = node.getBounds().width;
	    int y2 = node.getBounds().y;
	    int x = node.getBounds().x;
	    System.out.println(node);
	    assertEquals("Width of Split column nodes should be equal", w1, w2);
	    assertEquals("X of Split column nodes should be 0", x, 0);
	    assertEquals("Y of Split row node, right of previous node", y1, y2);
	    y1 = node.getBounds().y + node.getBounds().height;
	}
	{
	    int h1 = one.getBounds().height;
	    int h2 = labelOne.getPreferredSize().height;
	    assertEquals("Node one's height should equal label's preferred width", h1, h2);
	}
	{
	    int h1 = divider12.getBounds().height;
	    int h2 = msl.getDividerSize();
	    assertEquals("Divider height should equal dividerSize", h1, h2);
	}
	{
	    int h1 = panel.getHeight() - (one.getBounds().height + divider12.getBounds().height);
	    int h2 = two.getBounds().height;
	    assertEquals("Remaining space to be allocated to Node two ", h1, h2);
	}
	{
	    Rectangle r1 = labelOne.getBounds();
	    Rectangle r2 = one.getBounds();
	    assertEquals("one: leaf/component bounds should match", r1, r2);
	}
	{
	    Rectangle r1 = labelTwo.getBounds();
	    Rectangle r2 = two.getBounds();
	    assertEquals("two: leaf/component bounds should match", r1, r2);
	}
    }

}
