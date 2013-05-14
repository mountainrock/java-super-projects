package designPatterns.graph;

import junit.framework.TestCase;

import java.util.Collection;

import designPatterns.graph.model.ComponentsView;
import designPatterns.graph.model.Graph;
import designPatterns.graph.model.MutableGraph;
import designPatterns.graph.model.Tree;

public class DepthFirstSearchTest extends TestCase {


  public DepthFirstSearchTest(String s) {
    super(s);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(DepthFirstSearchTest.class);
  }

  /**
   *   1 --- 2 ---- 3
   *    \   / \
   *     \ /   \
   *      4 --- 5 --- 6
   *             \
   *              \
   *               7    8
   */
  public void test() {
    MutableGraph g = new MutableGraph();
    g.addVertex("1");
    g.addVertex("2");
    g.addVertex("3");
    g.addVertex("4");
    g.addVertex("5");
    g.addVertex("6");
    g.addVertex("7");
    g.addVertex("8");
    g.addTwoWay("1", "2");
    g.addTwoWay("2", "3");
    g.addTwoWay("1", "4");
    g.addTwoWay("2", "4");
    g.addTwoWay("4", "5");
    g.addTwoWay("2", "5");
    g.addTwoWay("5", "6");
    g.addTwoWay("5", "7");
    //
    assertEquals(8, g.numVertices());
    assertEquals(16, g.numEdges());
    //
    DepthFirstSearch dfs = new DepthFirstSearch(g);
    Tree tree = dfs.search("5");
    System.out.println(tree);

    // Check that g remains un-mutated following a search
    assertEquals(8, g.numVertices());
    assertEquals(16, g.numEdges());
    //
    Collection components = ComponentsView.getConnectedComponents(g);
    assertEquals(2, components.size());
    Graph g0 = (Graph)components.toArray()[0];
    Graph g1 = (Graph)components.toArray()[1];
    if (g0.numVertices() != 1) {
      Graph tmp = g1;
      g1 = g0;
      g0 = tmp;
    }
    assertEquals(1, g0.numVertices());
    assertEquals(7, g1.numVertices());
  }

}