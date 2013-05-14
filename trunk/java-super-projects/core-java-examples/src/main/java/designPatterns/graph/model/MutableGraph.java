package designPatterns.graph.model;

import org.apache.log4j.Category;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class MutableGraph extends AbstractGraph {
  private static Category logger = Category.getInstance(MutableGraph.class);

  Map descendentEdges;
  Map ancestorEdges;

  public MutableGraph() {
    this(HashMap.class);
  }

  public MutableGraph(Class mapType) {
    try {
      descendentEdges = (Map)mapType.newInstance();
      ancestorEdges = (Map)mapType.newInstance();
    } catch(Exception e) {
      logger.error("IllegalArgumentException in MutableGraph method: " + e.getMessage());
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  public MutableGraph(Graph graph) {
    this((graph instanceof MutableGraph) ? ((MutableGraph)graph).descendentEdges.getClass() : HashMap.class);
    Iterator it = graph.getVertices().iterator();
    while(it.hasNext()) {
      Object vertex = it.next();
      // add the vertex as it may not have any edges
      this.addVertex(vertex);
      Iterator edges = graph.getEdgesFor(vertex).iterator();
      while(edges.hasNext()) {
        this.addEdge(vertex, edges.next());
      }
    }
  }

  public void addAll(Graph g) {
    Iterator vertices = g.getVertices().iterator();
    while(vertices.hasNext()) {
      Object fromVertex = vertices.next();
      // add vertex - since it may have no neighbours (edges)
      this.addVertex(fromVertex);
      Iterator edges = g.getEdgesFor(fromVertex).iterator();
      while(edges.hasNext()) {
        Object toVertex = edges.next();
        this.addEdge(fromVertex, toVertex);
      }
    }
  }
  
  public Collection getVertices() {
    return Collections.unmodifiableCollection(descendentEdges.keySet());
  }

  public Collection getEdgesFor(Object vertex) {
    return get(descendentEdges, vertex);
  }

  public int numVertices() {
    return descendentEdges.size();
  }

  public boolean isVertex(Object vertex) {
    return descendentEdges.containsKey(vertex);
  }

  public boolean isEdge(Object from, Object to) {
    return isVertex(from) && get(descendentEdges, from).contains(to);
  }

  public boolean isIsolated(Object vertex) {
    return isVertex(vertex) && get(ancestorEdges, vertex).isEmpty();
  }

  public void addEdge(Object from, Object to) {
    Collection fromEdges = get(descendentEdges, from);
    fromEdges.add(to);
    Collection toEdges = get(ancestorEdges, to);
    toEdges.add(from);
  }

  public void addTwoWay(Object vertexA, Object vertexB) {
    addEdge(vertexA, vertexB);
    addEdge(vertexB, vertexA);
  }

  public void removeEdge(Object from, Object to) {
    Collection fromEdges = get(descendentEdges, from);
    fromEdges.remove(to);
    Collection toEdges = get(ancestorEdges, to);
    toEdges.remove(from);
  }

  public void removeAllEdgesFrom(Object fromVertex) {
    // first remove fromVertex as a parent of its descendents
    Collection fromEdges = get(descendentEdges, fromVertex);
    Iterator descendants = fromEdges.iterator();
    while(descendants.hasNext()) {
      Object toVertex = descendants.next();
      Collection toEdges = get(ancestorEdges, toVertex);
      toEdges.remove(fromVertex);
    }
    fromEdges.clear();
  }

  public void removeAllEdgesTo(Object toVertex) {
    Collection toEdges = get(ancestorEdges, toVertex);
    Iterator ancestors = toEdges.iterator();
    while(ancestors.hasNext()) {
      Object fromVertex = ancestors.next();
      Collection fromEdges = get(descendentEdges, fromVertex);
      fromEdges.remove(toVertex);
    }
    toEdges.remove(toVertex);
  }

  public void addVertex(Object newVertex) {
    get(descendentEdges, newVertex);
  }

  public void removeVertex(Object vertex) {
    removeAllEdgesFrom(vertex);
    removeAllEdgesTo(vertex);
    descendentEdges.remove(vertex);
    ancestorEdges.remove(vertex);
  }

  private Collection get(Map map, Object key) {
    ArrayList values = (ArrayList) map.get(key);
    if (values == null) {
      values = new ArrayList(2);
      map.put(key, values);
    }
    return values;
  }

}
