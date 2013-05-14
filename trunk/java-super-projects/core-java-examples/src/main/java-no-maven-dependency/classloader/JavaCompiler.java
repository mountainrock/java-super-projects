package classloader;

import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.Main;
import com.sun.tools.javac.code.*;
import com.sun.tools.javac.comp.*;
import com.sun.tools.javac.jvm.*;
import com.sun.tools.javac.parser.Parser;
import com.sun.tools.javac.parser.Scanner;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.List;
import com.sun.tools.javadoc.DocCommentScanner;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.lang.model.SourceVersion;
import javax.tools.*;

public class JavaCompiler
    implements com.sun.tools.javac.jvm.ClassReader.SourceCompleter
{
    /* member class not found */
    class CompilePolicy {}

    /* member class not found */
    class ImplicitSourcePolicy {}


    protected static final com.sun.tools.javac.util.Context.Key compilerKey = new com.sun.tools.javac.util.Context.Key();
    private static final String versionRBName = "com.sun.tools.javac.resources.version";
    private static ResourceBundle versionRB;
    private static CompilePolicy DEFAULT_COMPILE_POLICY;
    public Log log;
    protected TreeMaker make;
    protected ClassReader reader;
    protected ClassWriter writer;
    protected Enter enter;
    protected Symtab syms;
    protected Source source;
    protected Gen gen;
    protected com.sun.tools.javac.util.Name.Table names;
    protected Attr attr;
    protected Check chk;
    protected Flow flow;
    TransTypes transTypes;
    Lower lower;
    protected Annotate annotate;
    protected final Name completionFailureName;
    protected Types types;
    protected JavaFileManager fileManager;
    protected com.sun.tools.javac.parser.Parser.Factory parserFactory;
    protected TaskListener taskListener;
    protected JavaCompiler delegateCompiler;
    protected boolean annotationProcessingOccurred;
    protected boolean implicitSourceFilesRead;
    protected Context context;
    public boolean verbose;
    public boolean sourceOutput;
    public boolean stubOutput;
    public boolean attrParseOnly;
    boolean relax;
    public boolean printFlat;
    public String encoding;
    public boolean lineDebugInfo;
    public boolean genEndPos;
    protected boolean devVerbose;
    protected boolean processPcks;
    protected boolean explicitAnnotationProcessingRequested;
    protected CompilePolicy compilePolicy;
    protected ImplicitSourcePolicy implicitSourcePolicy;
    public boolean verboseCompilePolicy;
    public Todo todo;
    private Set deferredSugar;
    protected Set inputFiles;
    public boolean keepComments;
    private boolean hasBeenUsed;
    private long start_msec;
    public long elapsed_msec;
    private boolean parseErrors;
    private List rootClasses;
    boolean processAnnotations;
    JavacProcessingEnvironment procEnvImpl;
    static final boolean $assertionsDisabled = false;

    public static JavaCompiler instance(Context context1)
    {
        JavaCompiler javacompiler = (JavaCompiler)context1.get(compilerKey);
        if(javacompiler == null)
        {
            javacompiler = new JavaCompiler(context1);
        }
        return javacompiler;
    }

    public static String version()
    {
        return version("release");
    }

    public static String fullVersion()
    {
        return version("full");
    }

    private static String version(String s)
    {
        if(versionRB == null)
        {
            try
            {
                versionRB = ResourceBundle.getBundle("com.sun.tools.javac.resources.version");
            }
            catch(MissingResourceException missingresourceexception)
            {
                return Log.getLocalizedString("version.resource.missing", new Object[] {
                    System.getProperty("java.version")
                });
            }
        }
        return versionRB.getString(s);
        MissingResourceException missingresourceexception1;
        return Log.getLocalizedString("version.unknown", new Object[] {
            System.getProperty("java.version")
        });
    }

    public JavaCompiler(Context context1)
    {
        explicitAnnotationProcessingRequested = false;
        deferredSugar = new HashSet();
        inputFiles = new HashSet();
        keepComments = false;
        hasBeenUsed = false;
        start_msec = 0L;
        elapsed_msec = 0L;
        parseErrors = false;
        processAnnotations = false;
        procEnvImpl = null;
        context = context1;
        context1.put(compilerKey, this);
        if(context1.get(JavaFileManager.class) == null)
        {
            DefaultFileManager.preRegister(context1);
        }
        names = com.sun.tools.javac.util.Name.Table.instance(context1);
        log = Log.instance(context1);
        reader = ClassReader.instance(context1);
        make = TreeMaker.instance(context1);
        writer = ClassWriter.instance(context1);
        enter = Enter.instance(context1);
        todo = Todo.instance(context1);
        fileManager = (JavaFileManager)context1.get(JavaFileManager.class);
        parserFactory = com.sun.tools.javac.parser.Parser.Factory.instance(context1);
        try
        {
            syms = Symtab.instance(context1);
        }
        catch(com.sun.tools.javac.code.Symbol.CompletionFailure completionfailure)
        {
            log.error("cant.access", new Object[] {
                completionfailure.sym, completionfailure.errmsg
            });
            if(completionfailure instanceof com.sun.tools.javac.jvm.ClassReader.BadClassFile)
            {
                throw new Abort();
            }
        }
        source = Source.instance(context1);
        attr = Attr.instance(context1);
        chk = Check.instance(context1);
        gen = Gen.instance(context1);
        flow = Flow.instance(context1);
        transTypes = TransTypes.instance(context1);
        lower = Lower.instance(context1);
        annotate = Annotate.instance(context1);
        types = Types.instance(context1);
        taskListener = (TaskListener)context1.get(TaskListener.class);
        reader.sourceCompleter = this;
        Options options = Options.instance(context1);
        verbose = options.get("-verbose") != null;
        sourceOutput = options.get("-printsource") != null;
        stubOutput = options.get("-stubs") != null;
        relax = options.get("-relax") != null;
        printFlat = options.get("-printflat") != null;
        attrParseOnly = options.get("-attrparseonly") != null;
        encoding = options.get("-encoding");
        lineDebugInfo = options.get("-g:") == null || options.get("-g:lines") != null;
        genEndPos = options.get("-Xjcov") != null || context1.get(DiagnosticListener.class) != null;
        devVerbose = options.get("dev") != null;
        processPcks = options.get("process.packages") != null;
        verboseCompilePolicy = options.get("verboseCompilePolicy") != null;
        if(attrParseOnly)
        {
            compilePolicy = CompilePolicy.ATTR_ONLY;
        } else
        {
            compilePolicy = CompilePolicy.decode(options.get("compilePolicy"));
        }
        implicitSourcePolicy = ImplicitSourcePolicy.decode(options.get("-implicit"));
        completionFailureName = options.get("failcomplete") == null ? null : names.fromString(options.get("failcomplete"));
    }

    public int errorCount()
    {
        if(delegateCompiler != null && delegateCompiler != this)
        {
            return delegateCompiler.errorCount();
        } else
        {
            return log.nerrors;
        }
    }

    protected final List stopIfError(ListBuffer listbuffer)
    {
        if(errorCount() == 0)
        {
            return listbuffer.toList();
        } else
        {
            return List.nil();
        }
    }

    protected final List stopIfError(List list)
    {
        if(errorCount() == 0)
        {
            return list;
        } else
        {
            return List.nil();
        }
    }

    public int warningCount()
    {
        if(delegateCompiler != null && delegateCompiler != this)
        {
            return delegateCompiler.warningCount();
        } else
        {
            return log.nwarnings;
        }
    }

    public boolean parseErrors()
    {
        return parseErrors;
    }

    protected com.sun.tools.javac.parser.Scanner.Factory getScannerFactory()
    {
        return com.sun.tools.javac.parser.Scanner.Factory.instance(context);
    }

    public CharSequence readSource(JavaFileObject javafileobject)
    {
        inputFiles.add(javafileobject);
        return javafileobject.getCharContent(false);
        IOException ioexception;
        ioexception;
        log.error("error.reading.file", new Object[] {
            javafileobject, ioexception.getLocalizedMessage()
        });
        return null;
    }

    protected com.sun.tools.javac.tree.JCTree.JCCompilationUnit parse(JavaFileObject javafileobject, CharSequence charsequence)
    {
        long l = now();
        com.sun.tools.javac.tree.JCTree.JCCompilationUnit jccompilationunit = make.TopLevel(List.nil(), null, List.nil());
        if(charsequence != null)
        {
            if(verbose)
            {
                printVerbose("parsing.started", javafileobject);
            }
            if(taskListener != null)
            {
                TaskEvent taskevent = new TaskEvent(com.sun.source.util.TaskEvent.Kind.PARSE, javafileobject);
                taskListener.started(taskevent);
            }
            int i = log.nerrors;
            Scanner scanner = getScannerFactory().newScanner(charsequence);
            Parser parser = parserFactory.newParser(scanner, keepComments(), genEndPos);
            jccompilationunit = parser.compilationUnit();
            parseErrors |= log.nerrors > i;
            if(lineDebugInfo)
            {
                jccompilationunit.lineMap = scanner.getLineMap();
            }
            if(verbose)
            {
                printVerbose("parsing.done", Long.toString(elapsed(l)));
            }
        }
        jccompilationunit.sourcefile = javafileobject;
        if(charsequence != null && taskListener != null)
        {
            TaskEvent taskevent1 = new TaskEvent(com.sun.source.util.TaskEvent.Kind.PARSE, jccompilationunit);
            taskListener.finished(taskevent1);
        }
        return jccompilationunit;
    }

    protected boolean keepComments()
    {
        return keepComments || sourceOutput || stubOutput;
    }

    /**
     * @deprecated Method parse is deprecated
     */

    public com.sun.tools.javac.tree.JCTree.JCCompilationUnit parse(String s)
        throws IOException
    {
        DefaultFileManager defaultfilemanager = (DefaultFileManager)fileManager;
        return parse((JavaFileObject)defaultfilemanager.getJavaFileObjectsFromStrings(List.of(s)).iterator().next());
    }

    public com.sun.tools.javac.tree.JCTree.JCCompilationUnit parse(JavaFileObject javafileobject)
    {
        JavaFileObject javafileobject1 = log.useSource(javafileobject);
        com.sun.tools.javac.tree.JCTree.JCCompilationUnit jccompilationunit1;
        try
        {
            com.sun.tools.javac.tree.JCTree.JCCompilationUnit jccompilationunit = parse(javafileobject, readSource(javafileobject));
            if(jccompilationunit.endPositions != null)
            {
                log.setEndPosTable(javafileobject, jccompilationunit.endPositions);
            }
            jccompilationunit1 = jccompilationunit;
        }
        finally
        {
            log.useSource(javafileobject1);
        }
        return jccompilationunit1;
    }

    public Symbol resolveIdent(String s)
    {
        JavaFileObject javafileobject;
        if(s.equals(""))
        {
            return syms.errSymbol;
        }
        javafileobject = log.useSource(null);
        Object obj;
        String as[];
        int i;
        int j;
        obj = null;
        as = s.split("\\.", -1);
        i = as.length;
        j = 0;
_L1:
        String s1;
        com.sun.tools.javac.code.Symbol.ClassSymbol classsymbol;
        if(j >= i)
        {
            break MISSING_BLOCK_LABEL_129;
        }
        s1 = as[j];
        if(SourceVersion.isIdentifier(s1))
        {
            break MISSING_BLOCK_LABEL_82;
        }
        classsymbol = syms.errSymbol;
        return classsymbol;
        obj = obj != null ? ((Object) (make.Select(((com.sun.tools.javac.tree.JCTree.JCExpression) (obj)), names.fromString(s1)))) : ((Object) (make.Ident(names.fromString(s1))));
        j++;
          goto _L1
        Symbol symbol;
        com.sun.tools.javac.tree.JCTree.JCCompilationUnit jccompilationunit = make.TopLevel(List.nil(), null, List.nil());
        jccompilationunit.packge = syms.unnamedPackage;
        symbol = attr.attribIdent(((JCTree) (obj)), jccompilationunit);
        return symbol;
        local;
        log.useSource(javafileobject);
        JVM INSTR ret 10;
    }

    JavaFileObject printSource(Env env, com.sun.tools.javac.tree.JCTree.JCClassDecl jcclassdecl)
        throws IOException
    {
        JavaFileObject javafileobject = fileManager.getJavaFileForOutput(StandardLocation.CLASS_OUTPUT, jcclassdecl.sym.flatname.toString(), javax.tools.JavaFileObject.Kind.SOURCE, null);
        if(inputFiles.contains(javafileobject))
        {
            log.error(jcclassdecl.pos(), "source.cant.overwrite.input.file", new Object[] {
                javafileobject
            });
            return null;
        }
        BufferedWriter bufferedwriter = new BufferedWriter(javafileobject.openWriter());
        try
        {
            (new Pretty(bufferedwriter, true)).printUnit(env.toplevel, jcclassdecl);
            if(verbose)
            {
                printVerbose("wrote.file", javafileobject);
            }
        }
        finally
        {
            bufferedwriter.close();
        }
        return javafileobject;
    }

    JavaFileObject genCode(Env env, com.sun.tools.javac.tree.JCTree.JCClassDecl jcclassdecl)
        throws IOException
    {
        if(gen.genClass(env, jcclassdecl))
        {
            return writer.writeClass(jcclassdecl.sym);
        }
        break MISSING_BLOCK_LABEL_96;
        Object obj;
        obj;
        log.error(jcclassdecl.pos(), "limit.pool", new Object[0]);
        break MISSING_BLOCK_LABEL_96;
        obj;
        log.error(jcclassdecl.pos(), "limit.string.overflow", new Object[] {
            ((com.sun.tools.javac.jvm.ClassWriter.StringOverflow) (obj)).value.substring(0, 20)
        });
        break MISSING_BLOCK_LABEL_96;
        obj;
        chk.completionError(jcclassdecl.pos(), ((com.sun.tools.javac.code.Symbol.CompletionFailure) (obj)));
        return null;
    }

    public void complete(com.sun.tools.javac.code.Symbol.ClassSymbol classsymbol)
        throws com.sun.tools.javac.code.Symbol.CompletionFailure
    {
        if(completionFailureName == classsymbol.fullname)
        {
            throw new com.sun.tools.javac.code.Symbol.CompletionFailure(classsymbol, "user-selected completion failure by class name");
        }
        JavaFileObject javafileobject = classsymbol.classfile;
        JavaFileObject javafileobject1 = log.useSource(javafileobject);
        com.sun.tools.javac.tree.JCTree.JCCompilationUnit jccompilationunit;
        try
        {
            jccompilationunit = parse(javafileobject, javafileobject.getCharContent(false));
        }
        catch(IOException ioexception)
        {
            log.error("error.reading.file", new Object[] {
                javafileobject, ioexception
            });
            jccompilationunit = make.TopLevel(List.nil(), null, List.nil());
        }
        finally
        {
            log.useSource(javafileobject1);
        }
        if(taskListener != null)
        {
            TaskEvent taskevent = new TaskEvent(com.sun.source.util.TaskEvent.Kind.ENTER, jccompilationunit);
            taskListener.started(taskevent);
        }
        enter.complete(List.of(jccompilationunit), classsymbol);
        if(taskListener != null)
        {
            TaskEvent taskevent1 = new TaskEvent(com.sun.source.util.TaskEvent.Kind.ENTER, jccompilationunit);
            taskListener.finished(taskevent1);
        }
        if(enter.getEnv(classsymbol) == null)
        {
            boolean flag = jccompilationunit.sourcefile.isNameCompatible("package-info", javax.tools.JavaFileObject.Kind.SOURCE);
            if(flag)
            {
                if(enter.getEnv(jccompilationunit.packge) == null)
                {
                    log;
                    String s = Log.getLocalizedString("file.does.not.contain.package", new Object[] {
                        classsymbol.location()
                    });
                    throw new com.sun.tools.javac.jvm.ClassReader.BadClassFile(classsymbol, javafileobject, s);
                }
            } else
            {
                throw new com.sun.tools.javac.jvm.ClassReader.BadClassFile(classsymbol, javafileobject, log.getLocalizedString("file.doesnt.contain.class", new Object[] {
                    classsymbol.fullname
                }));
            }
        }
        implicitSourceFilesRead = true;
    }

    public void compile(List list)
        throws Throwable
    {
        compile(list, List.nil(), null);
    }

    public void compile(List list, List list1, Iterable iterable)
        throws IOException
    {
        if(iterable != null && iterable.iterator().hasNext())
        {
            explicitAnnotationProcessingRequested = true;
        }
        if(hasBeenUsed)
        {
            throw new AssertionError("attempt to reuse JavaCompiler");
        }
        hasBeenUsed = true;
        start_msec = now();
        try
        {
            initProcessAnnotations(iterable);
            delegateCompiler = processAnnotations(enterTrees(stopIfError(parseFiles(list))), list1);
            delegateCompiler.compile2();
            delegateCompiler.close();
            elapsed_msec = delegateCompiler.elapsed_msec;
        }
        catch(Abort abort)
        {
            if(devVerbose)
            {
                abort.printStackTrace();
            }
        }
    }

    private void compile2()
    {
    /* anonymous class not found */
    class _anm1 {}

        try
        {
            switch(_cls1..SwitchMap.com.sun.tools.javac.main.JavaCompiler.CompilePolicy[compilePolicy.ordinal()])
            {
            case 1: // '\001'
                attribute(todo);
                break;

            case 2: // '\002'
                flow(attribute(todo));
                break;

            case 3: // '\003'
                generate(desugar(flow(attribute(todo))));
                break;

            case 4: // '\004'
                List list;
                for(Iterator iterator = groupByFile(flow(attribute(todo))).values().iterator(); iterator.hasNext(); generate(desugar(list)))
                {
                    list = (List)iterator.next();
                }

                break;

            case 5: // '\005'
                for(; todo.nonEmpty(); generate(desugar(flow(attribute((Env)todo.next()))))) { }
                break;

            default:
                if(!$assertionsDisabled)
                {
                    throw new AssertionError("unknown compile policy");
                }
                break;
            }
        }
        catch(Abort abort)
        {
            if(devVerbose)
            {
                abort.printStackTrace();
            }
        }
        if(verbose)
        {
            elapsed_msec = elapsed(start_msec);
            printVerbose("total", Long.toString(elapsed_msec));
        }
        reportDeferredDiagnostics();
        if(!log.hasDiagnosticListener())
        {
            printCount("error", errorCount());
            printCount("warn", warningCount());
        }
    }

    public List parseFiles(List list)
        throws IOException
    {
        if(errorCount() > 0)
        {
            return List.nil();
        }
        ListBuffer listbuffer = ListBuffer.lb();
        JavaFileObject javafileobject;
        for(Iterator iterator = list.iterator(); iterator.hasNext(); listbuffer.append(parse(javafileobject)))
        {
            javafileobject = (JavaFileObject)iterator.next();
        }

        return listbuffer.toList();
    }

    public List enterTrees(List list)
    {
        if(taskListener != null)
        {
            TaskEvent taskevent;
            for(Iterator iterator = list.iterator(); iterator.hasNext(); taskListener.started(taskevent))
            {
                com.sun.tools.javac.tree.JCTree.JCCompilationUnit jccompilationunit = (com.sun.tools.javac.tree.JCTree.JCCompilationUnit)iterator.next();
                taskevent = new TaskEvent(com.sun.source.util.TaskEvent.Kind.ENTER, jccompilationunit);
            }

        }
        enter.main(list);
        if(taskListener != null)
        {
            TaskEvent taskevent1;
            for(Iterator iterator1 = list.iterator(); iterator1.hasNext(); taskListener.finished(taskevent1))
            {
                com.sun.tools.javac.tree.JCTree.JCCompilationUnit jccompilationunit1 = (com.sun.tools.javac.tree.JCTree.JCCompilationUnit)iterator1.next();
                taskevent1 = new TaskEvent(com.sun.source.util.TaskEvent.Kind.ENTER, jccompilationunit1);
            }

        }
        if(sourceOutput || stubOutput)
        {
            ListBuffer listbuffer = ListBuffer.lb();
            for(Iterator iterator2 = list.iterator(); iterator2.hasNext();)
            {
                com.sun.tools.javac.tree.JCTree.JCCompilationUnit jccompilationunit2 = (com.sun.tools.javac.tree.JCTree.JCCompilationUnit)iterator2.next();
                List list1 = jccompilationunit2.defs;
                while(list1.nonEmpty()) 
                {
                    if(list1.head instanceof com.sun.tools.javac.tree.JCTree.JCClassDecl)
                    {
                        listbuffer.append((com.sun.tools.javac.tree.JCTree.JCClassDecl)list1.head);
                    }
                    list1 = list1.tail;
                }
            }

            rootClasses = listbuffer.toList();
        }
        return list;
    }

    public void initProcessAnnotations(Iterable iterable)
    {
        Options options = Options.instance(context);
        if(options.get("-proc:none") != null)
        {
            processAnnotations = false;
        } else
        if(procEnvImpl == null)
        {
            procEnvImpl = new JavacProcessingEnvironment(context, iterable);
            processAnnotations = procEnvImpl.atLeastOneProcessor();
            if(processAnnotations)
            {
                if(context.get(com.sun.tools.javac.parser.Scanner.Factory.scannerFactoryKey) == null)
                {
                    com.sun.tools.javadoc.DocCommentScanner.Factory.preRegister(context);
                }
                options.put("save-parameter-names", "save-parameter-names");
                reader.saveParameterNames = true;
                keepComments = true;
                if(taskListener != null)
                {
                    taskListener.started(new TaskEvent(com.sun.source.util.TaskEvent.Kind.ANNOTATION_PROCESSING));
                }
            } else
            {
                procEnvImpl.close();
            }
        }
    }

    public JavaCompiler processAnnotations(List list)
        throws IOException
    {
        return processAnnotations(list, List.nil());
    }

    public JavaCompiler processAnnotations(List list, List list1)
        throws IOException
    {
        if(errorCount() != 0 && todo.isEmpty())
        {
            return this;
        }
        if(!processAnnotations)
        {
            Options options = Options.instance(context);
            if(options.get("-proc:only") != null)
            {
                log.warning("proc.proc-only.requested.no.procs", new Object[0]);
                todo.clear();
            }
            if(!list1.isEmpty())
            {
                log.error("proc.no.explicit.annotation.processing.requested", new Object[] {
                    list1
                });
            }
            return this;
        }
        List list2;
        List list3;
        list2 = List.nil();
        list3 = List.nil();
        if(list1.isEmpty())
        {
            break MISSING_BLOCK_LABEL_384;
        }
        if(explicitAnnotationProcessingRequested())
        {
            break MISSING_BLOCK_LABEL_135;
        }
        log.error("proc.no.explicit.annotation.processing.requested", new Object[] {
            list1
        });
        return this;
        boolean flag;
        Iterator iterator;
        flag = false;
        iterator = list1.iterator();
_L2:
        String s;
        Symbol symbol;
        if(!iterator.hasNext())
        {
            break; /* Loop/switch isn't completed */
        }
        s = (String)iterator.next();
        symbol = resolveIdent(s);
        if(symbol == null || symbol.kind == 1 && !processPcks)
        {
            log.error("proc.cant.find.class", new Object[] {
                s
            });
            flag = true;
            continue; /* Loop/switch isn't completed */
        }
        if(symbol.kind == 1)
        {
            symbol.complete();
        }
        if(symbol.exists())
        {
            Name name = names.fromString(s);
            if(symbol.kind == 1)
            {
                list3 = list3.prepend((com.sun.tools.javac.code.Symbol.PackageSymbol)symbol);
            } else
            {
                list2 = list2.prepend((com.sun.tools.javac.code.Symbol.ClassSymbol)symbol);
            }
            continue; /* Loop/switch isn't completed */
        }
        try
        {
            if(!$assertionsDisabled && symbol.kind != 1)
            {
                throw new AssertionError();
            }
            log.warning("proc.package.does.not.exist", new Object[] {
                s
            });
            list3 = list3.prepend((com.sun.tools.javac.code.Symbol.PackageSymbol)symbol);
        }
        catch(com.sun.tools.javac.code.Symbol.CompletionFailure completionfailure1)
        {
            log.error("proc.cant.find.class", new Object[] {
                s
            });
            flag = true;
        }
        if(true) goto _L2; else goto _L1
_L1:
        if(flag)
        {
            return this;
        }
        JavaCompiler javacompiler;
        javacompiler = procEnvImpl.doProcessing(context, list, list2, list3);
        if(javacompiler != this)
        {
            annotationProcessingOccurred = javacompiler.annotationProcessingOccurred = true;
        }
        return javacompiler;
        com.sun.tools.javac.code.Symbol.CompletionFailure completionfailure;
        completionfailure;
        log.error("cant.access", new Object[] {
            completionfailure.sym, completionfailure.errmsg
        });
        return this;
    }

    boolean explicitAnnotationProcessingRequested()
    {
        Options options = Options.instance(context);
        return explicitAnnotationProcessingRequested || options.get("-processor") != null || options.get("-processorpath") != null || options.get("-proc:only") != null || options.get("-Xprint") != null;
    }

    public List attribute(ListBuffer listbuffer)
    {
        ListBuffer listbuffer1 = ListBuffer.lb();
        for(; listbuffer.nonEmpty(); listbuffer1.append(attribute((Env)listbuffer.next()))) { }
        return listbuffer1.toList();
    }

    public Env attribute(Env env)
    {
        if(verboseCompilePolicy)
        {
            Log _tmp = log;
            Log.printLines(log.noticeWriter, (new StringBuilder()).append("[attribute ").append(env.enclClass.sym).append("]").toString());
        }
        if(verbose)
        {
            printVerbose("checking.attribution", env.enclClass.sym);
        }
        if(taskListener != null)
        {
            TaskEvent taskevent = new TaskEvent(com.sun.source.util.TaskEvent.Kind.ANALYZE, env.toplevel, env.enclClass.sym);
            taskListener.started(taskevent);
        }
        JavaFileObject javafileobject = log.useSource(env.enclClass.sym.sourcefile == null ? env.toplevel.sourcefile : env.enclClass.sym.sourcefile);
        try
        {
            attr.attribClass(env.tree.pos(), env.enclClass.sym);
        }
        finally
        {
            log.useSource(javafileobject);
        }
        return env;
    }

    public List flow(List list)
    {
        ListBuffer listbuffer = ListBuffer.lb();
        for(List list1 = list; list1.nonEmpty(); list1 = list1.tail)
        {
            flow((Env)list1.head, listbuffer);
        }

        return stopIfError(listbuffer);
    }

    public List flow(Env env)
    {
        ListBuffer listbuffer = ListBuffer.lb();
        flow(env, listbuffer);
        return stopIfError(listbuffer);
    }

    protected void flow(Env env, ListBuffer listbuffer)
    {
        JavaFileObject javafileobject;
        try
        {
            if(errorCount() > 0)
            {
                return;
            }
        }
        finally
        {
            if(taskListener != null)
            {
                TaskEvent taskevent = new TaskEvent(com.sun.source.util.TaskEvent.Kind.ANALYZE, env.toplevel, env.enclClass.sym);
                taskListener.finished(taskevent);
            }
        }
        if(relax || deferredSugar.contains(env))
        {
            listbuffer.append(env);
            return;
        }
        if(verboseCompilePolicy)
        {
            log;
            Log.printLines(log.noticeWriter, (new StringBuilder()).append("[flow ").append(env.enclClass.sym).append("]").toString());
        }
        javafileobject = log.useSource(env.enclClass.sym.sourcefile == null ? env.toplevel.sourcefile : env.enclClass.sym.sourcefile);
        make.at(0);
        TreeMaker treemaker = make.forToplevel(env.toplevel);
        flow.analyzeTree(env.tree, treemaker);
        if(errorCount() > 0)
        {
            return;
        }
        listbuffer.append(env);
          goto _L1
        local;
        log.useSource(javafileobject);
        JVM INSTR ret 6;
_L1:
    }

    public List desugar(List list)
    {
        ListBuffer listbuffer = ListBuffer.lb();
        for(List list1 = list; list1.nonEmpty(); list1 = list1.tail)
        {
            desugar((Env)list1.head, listbuffer);
        }

        return stopIfError(listbuffer);
    }

    protected void desugar(Env env, ListBuffer listbuffer)
    {
        if(errorCount() > 0)
        {
            return;
        }
        if(implicitSourcePolicy == ImplicitSourcePolicy.NONE && !inputFiles.contains(env.toplevel.sourcefile))
        {
            return;
        }
        if(desugarLater(env))
        {
            if(verboseCompilePolicy)
            {
                log;
                Log.printLines(log.noticeWriter, (new StringBuilder()).append("[defer ").append(env.enclClass.sym).append("]").toString());
            }
            todo.append(env);
            return;
        }
        deferredSugar.remove(env);
        if(verboseCompilePolicy)
        {
            log;
            Log.printLines(log.noticeWriter, (new StringBuilder()).append("[desugar ").append(env.enclClass.sym).append("]").toString());
        }
        JavaFileObject javafileobject = log.useSource(env.enclClass.sym.sourcefile == null ? env.toplevel.sourcefile : env.enclClass.sym.sourcefile);
        JCTree jctree;
        TreeMaker treemaker;
        Object obj;
        List list1;
        com.sun.tools.javac.tree.JCTree.JCClassDecl jcclassdecl;
        try
        {
            jctree = env.tree;
            make.at(0);
            treemaker = make.forToplevel(env.toplevel);
            if(env.tree instanceof com.sun.tools.javac.tree.JCTree.JCCompilationUnit)
            {
                if(!stubOutput && !sourceOutput && !printFlat)
                {
                    List list = lower.translateTopLevelClass(env, env.tree, treemaker);
                    if(list.head != null)
                    {
                        if(!$assertionsDisabled && !list.tail.isEmpty())
                        {
                            throw new AssertionError();
                        }
                        listbuffer.append(new Pair(env, (com.sun.tools.javac.tree.JCTree.JCClassDecl)list.head));
                    }
                }
                return;
            }
        }
        finally
        {
            log.useSource(javafileobject);
        }
        if(stubOutput)
        {
            obj = (com.sun.tools.javac.tree.JCTree.JCClassDecl)env.tree;
            if((jctree instanceof com.sun.tools.javac.tree.JCTree.JCClassDecl) && rootClasses.contains((com.sun.tools.javac.tree.JCTree.JCClassDecl)jctree) && ((((com.sun.tools.javac.tree.JCTree.JCClassDecl) (obj)).mods.flags & 5L) != 0L || ((com.sun.tools.javac.tree.JCTree.JCClassDecl) (obj)).sym.packge().getQualifiedName() == names.java_lang))
            {
                listbuffer.append(new Pair(env, removeMethodBodies(((com.sun.tools.javac.tree.JCTree.JCClassDecl) (obj)))));
            }
            return;
        }
        env.tree = transTypes.translateTopLevelClass(env.tree, treemaker);
        if(errorCount() != 0)
        {
            return;
        }
        if(sourceOutput)
        {
            obj = (com.sun.tools.javac.tree.JCTree.JCClassDecl)env.tree;
            if((jctree instanceof com.sun.tools.javac.tree.JCTree.JCClassDecl) && rootClasses.contains((com.sun.tools.javac.tree.JCTree.JCClassDecl)jctree))
            {
                listbuffer.append(new Pair(env, obj));
            }
            return;
        }
        obj = lower.translateTopLevelClass(env, env.tree, treemaker);
        if(errorCount() != 0)
        {
            return;
        }
        for(list1 = ((List) (obj)); list1.nonEmpty(); list1 = list1.tail)
        {
            jcclassdecl = (com.sun.tools.javac.tree.JCTree.JCClassDecl)list1.head;
            listbuffer.append(new Pair(env, jcclassdecl));
        }

    }

    public boolean desugarLater(Env env)
    {
        if(compilePolicy == CompilePolicy.BY_FILE)
        {
            return false;
        }
        if(!devVerbose && deferredSugar.contains(env))
        {
            return false;
        }
    /* block-local class not found */
    class _cls1ScanNested {}

        _cls1ScanNested _lcls1scannested = new _cls1ScanNested(env);
        _lcls1scannested.scan(env.tree);
        if(_lcls1scannested.externalSupers.isEmpty())
        {
            return false;
        }
        if(!deferredSugar.add(env) && devVerbose)
        {
            throw new AssertionError((new StringBuilder()).append(env.enclClass.sym).append(" was deferred, ").append("second time has these external super types ").append(_lcls1scannested.externalSupers).toString());
        } else
        {
            return true;
        }
    }

    public void generate(List list)
    {
        generate(list, null);
    }

    public void generate(List list, ListBuffer listbuffer)
    {
        boolean flag = stubOutput || sourceOutput || printFlat;
        for(List list1 = list; list1.nonEmpty(); list1 = list1.tail)
        {
            Pair pair = (Pair)list1.head;
            Env env = (Env)pair.fst;
            com.sun.tools.javac.tree.JCTree.JCClassDecl jcclassdecl = (com.sun.tools.javac.tree.JCTree.JCClassDecl)pair.snd;
            if(verboseCompilePolicy)
            {
                log;
                Log.printLines(log.noticeWriter, (new StringBuilder()).append("[generate ").append(flag ? " source" : "code").append(" ").append(env.enclClass.sym).append("]").toString());
            }
            if(taskListener != null)
            {
                TaskEvent taskevent = new TaskEvent(com.sun.source.util.TaskEvent.Kind.GENERATE, env.toplevel, jcclassdecl.sym);
                taskListener.started(taskevent);
            }
            JavaFileObject javafileobject = log.useSource(env.enclClass.sym.sourcefile == null ? env.toplevel.sourcefile : env.enclClass.sym.sourcefile);
            try
            {
                JavaFileObject javafileobject1;
                if(flag)
                {
                    javafileobject1 = printSource(env, jcclassdecl);
                } else
                {
                    javafileobject1 = genCode(env, jcclassdecl);
                }
                if(listbuffer != null && javafileobject1 != null)
                {
                    listbuffer.append(javafileobject1);
                }
            }
            catch(IOException ioexception)
            {
                log.error(jcclassdecl.pos(), "class.cant.write", new Object[] {
                    jcclassdecl.sym, ioexception.getMessage()
                });
                return;
            }
            finally
            {
                log.useSource(javafileobject);
            }
            if(taskListener != null)
            {
                TaskEvent taskevent1 = new TaskEvent(com.sun.source.util.TaskEvent.Kind.GENERATE, env.toplevel, jcclassdecl.sym);
                taskListener.finished(taskevent1);
            }
        }

    }

    Map groupByFile(List list)
    {
        LinkedHashMap linkedhashmap = new LinkedHashMap();
        HashSet hashset = new HashSet();
        for(List list1 = list; list1.nonEmpty(); list1 = list1.tail)
        {
            Env env = (Env)list1.head;
            List list2 = (List)linkedhashmap.get(env.toplevel);
            if(list2 == null)
            {
                list2 = List.of(env);
            } else
            {
                list2 = list2.prepend(env);
                hashset.add(env.toplevel);
            }
            linkedhashmap.put(env.toplevel, list2);
        }

        com.sun.tools.javac.tree.JCTree.JCCompilationUnit jccompilationunit;
        for(Iterator iterator = hashset.iterator(); iterator.hasNext(); linkedhashmap.put(jccompilationunit, ((List)linkedhashmap.get(jccompilationunit)).reverse()))
        {
            jccompilationunit = (com.sun.tools.javac.tree.JCTree.JCCompilationUnit)iterator.next();
        }

        return linkedhashmap;
    }

    com.sun.tools.javac.tree.JCTree.JCClassDecl removeMethodBodies(com.sun.tools.javac.tree.JCTree.JCClassDecl jcclassdecl)
    {
        boolean flag = (jcclassdecl.mods.flags & 512L) != 0L;
    /* block-local class not found */
    class _cls1MethodBodyRemover {}

        _cls1MethodBodyRemover _lcls1methodbodyremover = new _cls1MethodBodyRemover(flag);
        return (com.sun.tools.javac.tree.JCTree.JCClassDecl)_lcls1methodbodyremover.translate(jcclassdecl);
    }

    public void reportDeferredDiagnostics()
    {
        if(annotationProcessingOccurred && implicitSourceFilesRead && implicitSourcePolicy == ImplicitSourcePolicy.UNSET)
        {
            if(explicitAnnotationProcessingRequested())
            {
                log.warning("proc.use.implicit", new Object[0]);
            } else
            {
                log.warning("proc.use.proc.or.implicit", new Object[0]);
            }
        }
        chk.reportDeferredDiagnostics();
    }

    public void close()
    {
        close(true);
    }

    private void close(boolean flag)
    {
        rootClasses = null;
        reader = null;
        make = null;
        writer = null;
        enter = null;
        if(todo != null)
        {
            todo.clear();
        }
        todo = null;
        parserFactory = null;
        syms = null;
        source = null;
        attr = null;
        chk = null;
        gen = null;
        flow = null;
        transTypes = null;
        lower = null;
        annotate = null;
        types = null;
        log.flush();
        try
        {
            fileManager.flush();
        }
        catch(IOException ioexception)
        {
            throw new Abort(ioexception);
        }
        finally
        {
            if(names != null && flag)
            {
                names.dispose();
            }
            names = null;
        }
    }

    protected void printVerbose(String s, Object obj)
    {
        Log.printLines(log.noticeWriter, log.getLocalizedString((new StringBuilder()).append("verbose.").append(s).toString(), new Object[] {
            obj
        }));
    }

    protected void printCount(String s, int i)
    {
        if(i != 0)
        {
            String s1;
            if(i == 1)
            {
                Log _tmp = log;
                s1 = Log.getLocalizedString((new StringBuilder()).append("count.").append(s).toString(), new Object[] {
                    String.valueOf(i)
                });
            } else
            {
                Log _tmp1 = log;
                s1 = Log.getLocalizedString((new StringBuilder()).append("count.").append(s).append(".plural").toString(), new Object[] {
                    String.valueOf(i)
                });
            }
            Log.printLines(log.errWriter, s1);
            log.errWriter.flush();
        }
    }

    private static long now()
    {
        return System.currentTimeMillis();
    }

    private static long elapsed(long l)
    {
        return now() - l;
    }

    public void initRound(JavaCompiler javacompiler)
    {
        keepComments = javacompiler.keepComments;
        start_msec = javacompiler.start_msec;
        hasBeenUsed = true;
    }

    public static void enableLogging()
    {
        Logger logger = Logger.getLogger(com/sun/tools/javac/Main.getPackage().getName());
        logger.setLevel(Level.ALL);
        Handler ahandler[] = logger.getParent().getHandlers();
        int i = ahandler.length;
        for(int j = 0; j < i; j++)
        {
            Handler handler = ahandler[j];
            handler.setLevel(Level.ALL);
        }

    }

    static 
    {
        DEFAULT_COMPILE_POLICY = CompilePolicy.BY_TODO;
    }

}
