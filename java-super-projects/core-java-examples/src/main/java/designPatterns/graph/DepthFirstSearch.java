package designPatterns.graph;

import java.util.Iterator;

import designPatterns.graph.model.ConnectedComponentView;
import designPatterns.graph.model.Graph;
import designPatterns.graph.model.MutableGraph;
import designPatterns.graph.model.Tree;


public class DepthFirstSearch {

  Graph graph;

  public DepthFirstSearch(Graph graph) {
    this.graph = graph;
  }

  public Tree search(Object vertex) {
    MutableGraph tree = new MutableGraph();
    doSearch(vertex, tree);
    return new Tree(tree);
  }

  public ConnectedComponentView getComponentContaining(Object vertex) {
    Tree tree = search(vertex);
    // At this point tree is a graph whose vertices are the connected component of this.graph that contains vertex
    // But tree is not based on the same graph structure as this.graph
    return new ConnectedComponentView(graph, tree.getVertices());
  }

  void doSearch(Object vertex, MutableGraph tree) {
    tree.addVertex(vertex); // marks vertex as visited
    Iterator neighbours = graph.getEdgesFor(vertex).iterator();
    while(neighbours.hasNext()) {
      Object next = neighbours.next();
      boolean unvisited = !tree.isVertex(next);
      if (unvisited) {
        doSearch(next, tree);
        tree.addEdge(vertex, next);
      }
    }
  }

}
