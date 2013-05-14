package designPatterns.graph;

import java.io.Writer;
import java.io.IOException;

public class TreeWriter implements TreeVisitor {

  public static final String DEFAULT_INDENT = " ";

  Writer out;
  int depth = 0;
  String indent;

  public TreeWriter(Writer out) {
    this.out = out;
    this.depth = 0;
    this.indent = DEFAULT_INDENT;
  }

  public TreeWriter(Writer out, String indent) {
    this.out = out;
    this.indent = indent;
  }

  void write(Object vertex) {
    StringBuffer buf = new StringBuffer();
    for(int i = 0; i < depth; i++) {
      buf.append(indent);
    }
    buf.append(vertex);
    buf.append('\n');
    try {
      out.write(buf.toString());
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  public void visitVertex(Object vertex) {
    write(vertex);
  }

  public void open() {
    ++depth;
  }

  public void close() {
    --depth;
  }

}