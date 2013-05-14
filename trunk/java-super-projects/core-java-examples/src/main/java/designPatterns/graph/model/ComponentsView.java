package designPatterns.graph.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashSet;

import designPatterns.graph.DepthFirstSearch;

public class ComponentsView extends ImmutableGraphView {

  private Map vertexToComponentIndex;
  private Collection components;

  public ComponentsView(Graph underlyingGraph) {
    super(underlyingGraph);
    this.components = ComponentsView.getConnectedComponents(underlyingGraph);
    // build a Map of vertex to the connected component that contains it
    this.vertexToComponentIndex = new HashMap();
    Iterator it = components.iterator();
    while(it.hasNext()) {
      ConnectedComponentView component = (ConnectedComponentView) it.next();
      Iterator vertices = component.getVertices().iterator();
      while(vertices.hasNext()) {
        Object vertex = vertices.next();
        vertexToComponentIndex.put(vertex, component);
      }
    }
  }
  /**
   * Returns a collection of views, each view representing one connected component of the supplied graph
   *
   * @param graph The graph
   * @return Collection<ConnectedComponentView> A collection of views, each view representing one connected component of the supplied graph
   */
  public static Collection getConnectedComponents(Graph graph) {
    ArrayList components = new ArrayList();
    HashSet vertices = new HashSet(graph.getVertices());
    DepthFirstSearch dfs = new DepthFirstSearch(graph);
    //
    while(vertices.size() > 0) {
      ConnectedComponentView component = dfs.getComponentContaining(vertices.iterator().next());
      components.add(component);
      vertices.removeAll(component.getVertices());
    }
    return components;
  }

  public Collection getComponents() {
    return components;
  }

  public ConnectedComponentView getComponentContaining(Object vertex) {
    return (ConnectedComponentView) vertexToComponentIndex.get(vertex);
  }

}
