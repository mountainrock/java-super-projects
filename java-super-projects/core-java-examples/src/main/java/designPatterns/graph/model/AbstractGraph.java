package designPatterns.graph.model;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractGraph implements Graph {
  public abstract Collection getVertices();

  public Object getOneVertex() {
    return getVertices().iterator().next();
  }

  public abstract Collection getEdgesFor(Object vertex);

  public abstract int numVertices();

  public int numEdges() {
    int total = 0;
    Iterator vertices = getVertices().iterator();
    while(vertices.hasNext()) {
      Collection edges = getEdgesFor(vertices.next());
      total += edges.size();
    }
    return total;
  }

  public abstract boolean isVertex(Object vertex);

  public abstract boolean isEdge(Object from, Object to);

  public abstract boolean isIsolated(Object vertex);

  public Collection getIsolatedVertices() {
    ArrayList isolated = new ArrayList();
    Iterator it = getVertices().iterator();
    while(it.hasNext()) {
      Object vertex = it.next();
      if(isIsolated(vertex)) {
        isolated.add(vertex);
      }
    }
    return isolated;
  }

  public void addEdge(Object from, Object to) {
    throw new UnsupportedOperationException();
  }

  public void removeEdge(Object from, Object to) {
    throw new UnsupportedOperationException();
  }

  public void addVertex(Object newVertex) {
    throw new UnsupportedOperationException();
  }

  public void removeVertex(Object vertex) {
    throw new UnsupportedOperationException();
  }

  public void addAll(Collection vertices) {
    Iterator it = vertices.iterator();
    while(it.hasNext()) {
      addVertex(it.next());
    }
  }

  public void removeAll(Collection vertices) {
    Iterator it = vertices.iterator();
    while(it.hasNext()) {
      Object vertex = it.next();
      removeVertex(vertex);
    }
  }

  public String toString() {
    StringBuffer buf = new StringBuffer();
    Iterator vertices = getVertices().iterator();
    while(vertices.hasNext()) {
      Object vertex = vertices.next();
      Collection edges = getEdgesFor(vertex);
      buf.append(vertex + " -> [" + edges + "]\n");
    }
    return buf.toString();
  }

}
