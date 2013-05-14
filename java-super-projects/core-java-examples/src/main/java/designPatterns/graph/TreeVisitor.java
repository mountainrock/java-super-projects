package designPatterns.graph;

public interface TreeVisitor {
  void visitVertex(Object vertex);
  void open();
  void close();
}
