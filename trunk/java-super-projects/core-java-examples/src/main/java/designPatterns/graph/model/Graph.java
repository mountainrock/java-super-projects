package designPatterns.graph.model;

import java.util.Collection;

public interface Graph {

  Collection getVertices();

  Object getOneVertex();

  Collection getEdgesFor(Object vertex);

  int numVertices();

  int numEdges();

  boolean isVertex(Object vertex);

  boolean isEdge(Object from, Object to);

  boolean isIsolated(Object vertex);

  Collection getIsolatedVertices();

  void addEdge(Object from, Object to);

  void removeEdge(Object from, Object to);

  void addVertex(Object newVertex);

  void removeVertex(Object vertex);

  void addAll(Collection vertices);

  void removeAll(Collection vertices);

}
