package parser.java;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class MyJavaParser {
  int i;

  public static void main(String[] args)
      throws Exception {
    if (args.length == 0) {
      System.err.println("Usage: java MyJavaParser [javafilepath]");
      return;
    }

    String javaClassPath = args[0];
    InputStream in = new FileInputStream(new File(javaClassPath));
    CompilationUnit cu = JavaParser.parse(in);
    List<TypeDeclaration> types = cu.getTypes();
    for (TypeDeclaration typeDeclaration : types) {
      List<BodyDeclaration> members = typeDeclaration.getMembers();
      for (BodyDeclaration bodyDeclaration : members) {
        if (bodyDeclaration instanceof MethodDeclaration) {
          MethodDeclaration mDeclaration = (MethodDeclaration)bodyDeclaration;
          
          //apply refactoring logic here
          System.out.println(mDeclaration.getBody().getStmts());
        }

      }

    }

  }
}
