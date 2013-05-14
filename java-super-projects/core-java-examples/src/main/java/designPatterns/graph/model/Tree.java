package designPatterns.graph.model;

import org.apache.log4j.Category;

import designPatterns.graph.TreeVisitor;
import designPatterns.graph.TreeWriter;

import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;

public class Tree extends ImmutableGraphView {
   private static Category logger = Category.getInstance(Tree.class);

  Object root;

  public Tree(Graph graph) {
    super(graph);
    Collection isolatedVertices = graph.getIsolatedVertices();
    int numIsolatedVertices = isolatedVertices.size();
    if (numIsolatedVertices != 1) {
      logger.error("IllegalArgumentException in Tree method: Graph does not have 1 isolated vertex but instead has  " + numIsolatedVertices);
      throw new IllegalArgumentException("Graph does not have 1 isolated vertex but instead has " + numIsolatedVertices);
    }
    this.root = isolatedVertices.iterator().next();
  }

  public Object getRoot() {
    return root;
  }

  public Collection getChildrenOf(Object parent) {
    return getEdgesFor(parent);
  }

  public void accept(TreeVisitor visitor) {
    visitVertex(visitor, getRoot());
  }

  void visitVertex(TreeVisitor visitor, Object vertex) {
    visitor.visitVertex(vertex);
    Iterator it = getChildrenOf(vertex).iterator();
    visitor.open();
    while(it.hasNext()) {
      visitVertex(visitor, it.next());
    }
    visitor.close();
  }

  public String toString() {
    StringWriter out = new StringWriter();
    TreeWriter tw = new TreeWriter(out);
    this.accept(tw);
    return out.toString();
  }

}
