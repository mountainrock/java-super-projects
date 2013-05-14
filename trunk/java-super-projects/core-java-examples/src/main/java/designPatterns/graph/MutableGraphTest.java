package designPatterns.graph;

import junit.framework.TestCase;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

import designPatterns.graph.model.MutableGraph;

public class MutableGraphTest extends TestCase {


  public MutableGraphTest(String s) {
    super(s);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(MutableGraphTest.class);
  }

  public void testSimpleAdds() {
    MutableGraph g = new MutableGraph();
    assertEquals(0, g.numVertices());
    assertEquals(0, g.numEdges());
    //
    g.addVertex("1");
    g.addVertex("2");
    assertEquals(2, g.numVertices());
    assertEquals(0, g.numEdges());
    //
    g.addEdge("1", "2");
    assertEquals(2, g.numVertices());
    assertEquals(1, g.numEdges());
    //
    g.addEdge("2", "1");
    assertEquals(2, g.numVertices());
    assertEquals(2, g.numEdges());
  }

  public void testSimpleRemovals() {
    MutableGraph g = new MutableGraph();
    g.addVertex("1");
    g.addVertex("2");
    g.addVertex("3");
    g.addVertex("4");
    g.addEdge("1", "2");
    g.addEdge("2", "3");
    g.addEdge("3", "4");
    g.addEdge("4", "1");
    assertEquals(4, g.numVertices());
    assertEquals(4, g.numEdges());
    //
    g.removeVertex("4");
    assertEquals(3, g.numVertices());
    assertEquals(2, g.numEdges());
    //
    g.removeVertex("1");
    assertEquals(2, g.numVertices());
    assertEquals(1, g.numEdges());
    //
    g.removeEdge("2", "3");
    assertEquals(2, g.numVertices());
    assertEquals(0, g.numEdges());
  }

  /**
   *   1 --- 2 ---- 3
   *        / \
   *       /   \
   *      4 --- 5     6
   *
   * i.e. edges are 1>2 2>3 2>5 5>4 4>2
   */
  public void testIsolated() {
    MutableGraph g = new MutableGraph();
    g.addVertex("1");
    g.addVertex("2");
    g.addVertex("3");
    g.addVertex("4");
    g.addVertex("5");
    g.addVertex("6");
    g.addEdge("1", "2");
    g.addEdge("2", "3");
    g.addEdge("2", "5");
    g.addEdge("5", "4");
    g.addEdge("4", "2");
    Collection isolated = g.getIsolatedVertices();
    assertEquals(2, isolated.size());
    assertTrue(isolated.contains("1"));
    assertTrue(isolated.contains("6"));
  }

  public void testCopyConstructor() {
    MutableGraph g = new MutableGraph();
    g.addVertex("1");
    g.addVertex("2");
    g.addVertex("3");
    g.addVertex("4");
    g.addVertex("5");
    g.addVertex("6");
    g.addEdge("1", "2");
    g.addEdge("2", "3");
    g.addEdge("2", "5");
    g.addEdge("5", "4");
    g.addEdge("4", "2");
    //
    MutableGraph h = new MutableGraph(g);
    assertEquals(6, h.numVertices());
  }

  /**
   * Ensures vertices that have no neighbours are included in the union
   */
  public void testAddAll() {
    MutableGraph g1 = new MutableGraph();
    g1.addEdge("1", "2");
    g1.addEdge("2", "1");
    g1.addEdge("2", "3");
    g1.addVertex("4");

    MutableGraph g2 = new MutableGraph();
    g2.addEdge("3", "2");
    g2.addEdge("5", "4");
    g2.addVertex("6");

    MutableGraph union = new MutableGraph();
    union.addAll(g1);
    union.addAll(g2);

    //

    ArrayList actualVertices = new ArrayList(union.getVertices());
    Collections.sort(actualVertices);

    assertEquals(new String[]{"1", "2", "3", "4", "5", "6"}, union.getVertices());
    //
    assertEquals(new String[]{"2"},      union.getEdgesFor("1"));
    assertEquals(new String[]{"1", "3"}, union.getEdgesFor("2"));
    assertEquals(new String[]{"2"},      union.getEdgesFor("3"));
    assertEquals(new String[]{},         union.getEdgesFor("4"));
    assertEquals(new String[]{"4"},      union.getEdgesFor("5"));
    assertEquals(new String[]{},         union.getEdgesFor("6"));
  }

  /**
   * Helper method to assert that a Collection is the specified list of Comparables
   */
  private void assertEquals(Comparable[] expected, Collection actual) {
    List expectedList = Arrays.asList(expected);
    Collections.sort(expectedList);
    List actualList = new ArrayList(actual);
    Collections.sort(actualList);
    assertEquals(expectedList, actualList);
  }

}