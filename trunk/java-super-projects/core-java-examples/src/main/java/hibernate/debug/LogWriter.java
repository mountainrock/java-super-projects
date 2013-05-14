package hibernate.debug;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class LogWriter
{
	private String fileName = "D:/jdbc-debug.log";
	private PrintStream printStream;
	
	static LogWriter _writer;
	
	synchronized public static LogWriter getInstance()
	{
		try
		{
			if( _writer==null )
			{
				_writer = new LogWriter();
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		return _writer;
	}
	
	private LogWriter() throws IOException
	{
		OutputStream os = new FileOutputStream(fileName);
		printStream = new PrintStream(os);
	}
	
	public PrintStream logger()
	{
		return printStream;
	}
}
