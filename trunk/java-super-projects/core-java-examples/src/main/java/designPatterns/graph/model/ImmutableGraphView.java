package designPatterns.graph.model;

import java.util.Collection;


class ImmutableGraphView extends AbstractGraph {

  Graph underlyingGraph;

  public ImmutableGraphView(Graph underlyingGraph) {
    this.underlyingGraph = underlyingGraph;
  }

  public Collection getVertices() {
    return underlyingGraph.getVertices();
  }

  public Object getOneVertex() {
    return underlyingGraph.getOneVertex();
  }

  public Collection getEdgesFor(Object vertex) {
    return underlyingGraph.getEdgesFor(vertex);
  }

  public int numVertices() {
    return underlyingGraph.numVertices();
  }

  public int numEdges() {
    return underlyingGraph.numEdges();
  }

  public boolean isVertex(Object vertex) {
    return underlyingGraph.isVertex(vertex);
  }

  public boolean isEdge(Object from, Object to) {
    return underlyingGraph.isEdge(from, to);
  }

  public boolean isIsolated(Object vertex) {
    return underlyingGraph.isIsolated(vertex);
  }

  public Collection getIsolatedVertices() {
    return underlyingGraph.getIsolatedVertices();
  }
}
