package dns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;

import org.xbill.DNS.ARecord;
import org.xbill.DNS.Address;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TXTRecord;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import utils.LogUtil;

public class DNSJavaDemo {

	private static final String SAMPLE_ADDRESS = "www.dnsjava.org";
	private static String DNS_SERVER = "segblogr0003.springer-sbm.com";

	public static void main(String[] args) throws Throwable
	{

		// printDNSServerDetails();
		printDNSServer();
		// printLookupAddress();
	}

	private static void printLookupAddress() throws UnknownHostException
	{
		InetAddress addr = Address.getByName(SAMPLE_ADDRESS);
		LogUtil.logConsole(addr.toString());
	}

	private static void printDNSServerDetails() throws TextParseException, UnknownHostException
	{
		Lookup l = new Lookup("version.bind.", Type.TXT, DClass.CH);

		l.setResolver(new SimpleResolver(DNS_SERVER));
		l.run();

		LogUtil.logConsole(l.getResult());
		if (l.getResult() == Lookup.SUCCESSFUL)
			LogUtil.logConsole(l.getAnswers()[0].rdataToString());
	}

	public static void printDNSServer() throws Exception
	{
		String ipAddress = "version.bind.";
		String dnsblDomain = "google.com";

		Lookup lookup = new Lookup(ipAddress, Type.ANY);
		Resolver resolver = new SimpleResolver();
		lookup.setResolver(resolver);
		lookup.setCache(null);
		Record[] records = lookup.run();
		if (lookup.getResult() == Lookup.SUCCESSFUL) {
			String responseMessage = null;
			String listingType = null;
			for (int i = 0; i < records.length; i++) {
				if (records[i] instanceof TXTRecord) {
					TXTRecord txt = (TXTRecord) records[i];
					for (Iterator j = txt.getStrings().iterator(); j.hasNext();) {
						responseMessage += (String) j.next();
					}
				} else if (records[i] instanceof ARecord) {
					listingType = ((ARecord) records[i]).getAddress().getHostAddress();
				}
			}

			System.err.println("Found!");
			System.err.println("Response Message: " + responseMessage);
			System.err.println("Listing Type: " + listingType);
		} else if (lookup.getResult() == Lookup.HOST_NOT_FOUND) {
			System.err.println("Not found.");
		} else {
			System.err.println("Error!");
		}
	}
}
