package designPatterns.graph.model;

import java.util.Collection;
import java.util.Collections;


/**
 * Provides a read only view of one or more connected components of an underlying graph.
 */
public class ConnectedComponentView extends AbstractGraph {

  Graph underlyingGraph;
  Collection connectedVertices;

  public ConnectedComponentView(Graph underlyingGraph, Collection connectedVertices) {
    this.underlyingGraph = underlyingGraph;
    this.connectedVertices = connectedVertices;
  }

  public Collection getVertices() {
    return connectedVertices;
  }

  public Collection getEdgesFor(Object vertex) {
    if (isVertex(vertex)) {
      return underlyingGraph.getEdgesFor(vertex);
    } else {
      return Collections.EMPTY_LIST;
    }
  }

  public int numVertices() {
    return connectedVertices.size();
  }

  public boolean isVertex(Object vertex) {
    return connectedVertices.contains(vertex);
  }

  public boolean isEdge(Object from, Object to) {
    return isVertex(from) && isVertex(to) && underlyingGraph.isEdge(from, to);
  }

  public boolean isIsolated(Object vertex) {
    return isVertex(vertex) && underlyingGraph.isIsolated(vertex);
  }

}
