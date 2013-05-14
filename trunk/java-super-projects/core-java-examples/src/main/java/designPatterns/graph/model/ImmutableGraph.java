package designPatterns.graph.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;


public class ImmutableGraph extends AbstractGraph {

  private ArrayList vertices;
  private int[][] edges;
  private static final int[] NO_NEIGHBOURS = new int[0];
  public ImmutableGraph(Graph graph) {
    this.vertices = new ArrayList(graph.getVertices());
    Collections.sort(vertices);
    //
    // At this point the indexOf() method _is_ usable
    //
    final int size = vertices.size();
    this.edges = new int[size][];
    //
    for(int i = 0; i < size; i++) {
      Object vertexFrom = vertices.get(i);
      Collection neighbours = graph.getEdgesFor(vertexFrom);
      final int numNeighbours = (neighbours == null ? 0 : neighbours.size());
      if(numNeighbours == 0) {
        edges[i] = NO_NEIGHBOURS;
      } else {
        int[] indexes = new int[numNeighbours];
        Iterator it = neighbours.iterator();
        int j = 0;
        while(it.hasNext()) {
          Object neighbour = it.next();
          indexes[j++] = indexOf(neighbour);
        }
        // finish off by sorting the indexes..
        Arrays.sort(indexes);
        edges[i] = indexes;
      }
    }
  }

  public Collection getVertices() {
    return Collections.unmodifiableCollection(vertices);
  }

  public Object getOneVertex() {
    return (numVertices() == 0 ? null : vertices.get(0));
  }

  public int numVertices() {
    return vertices.size();
  }

  public int numEdges() {
    int total = 0;
    for(int i = 0; i < edges.length; i++) {
      total += edges[i].length;
    }
    return total;
  }

  public boolean isVertex(Object vertex) {
    return indexOf(vertex) != -1;
  }

  public boolean isEdge(Object from, Object to) {
    int fromIndex = vertices.indexOf(from);
    int toIndex = vertices.indexOf(to);
    return (fromIndex != -1 && toIndex != -1 && isEdge(fromIndex, toIndex));
  }

  private boolean isEdge(int fromIndex, int toIndex) {
    // is toIndex in edges[fromIndex] ?
    return Arrays.binarySearch(edges[fromIndex], toIndex) >= 0;
  }

  public Collection getEdgesFor(Object vertex) {
    Collection response = null;
    int index = vertices.indexOf(vertex);
    if(index != -1) {
      int[] edgeIndexes = edges[index];
      int size = edgeIndexes.length;
      response = new ArrayList(size);
      for(int i = 0; i < size; i++) {
        response.add(vertices.get(edgeIndexes[i]));
      }
    } else {
      response = Collections.EMPTY_LIST;
    }
    return response;
  }

  public boolean isIsolated(Object vertex) {
    if(!isVertex(vertex)) {
      return false;
    }
    int index = indexOf(vertex);
    return false;
  }

  private int indexOf(Object vertex) {
    return vertices.indexOf(vertex);
  }

}
