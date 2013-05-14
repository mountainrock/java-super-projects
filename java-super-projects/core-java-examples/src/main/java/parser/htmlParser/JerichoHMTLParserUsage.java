/**
 * 
 */
package parser.htmlParser;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


public class JerichoHMTLParserUsage {

	static final List symbols = new ArrayList();
	static final String URL = "https://secure.icicidirect.com/trading/equity/trading_stock_quote.asp?Symbol=";
	static String sendTo = "";

	static {
		// mahendra bhai's Sept 1st week.
		// String symbol, String name, double purchasePrice/watch price, Date dateOfPurchase, String stockMarketName, double target1, double target2, long quantity,boolean watchOnly
		symbols.add(new Quote("HOTLEE", "Hotel Leela", 62.55, null, "BSE", 90, 100, 100, false));
		/*
		 * symbols.add(new Quote("USHBEL", "USHA MARTIN LIMITED", 170.00, null, "NSE", 185, 202, 47, false)); symbols.add(new Quote("KERMIC", "Kernex Mic", 167.00, null, "NSE", 190, 207, 47, false));
		 * 
		 * symbols.add(new Quote("FCSSOF", "FCS Software ", 106.25, null, "BSE", 120, 132, 87, false)); symbols.add(new Quote("BALCHI", "Balrampur chini ", 97.91, null, "NSE", 108, 115, 100, false));
		 */
		// other tips
		// symbols.add(new Quote("KAMIS", "Kamadhenu ISPAT", 19.26, null, "NSE", 110));
		// watch list of unpurchased stocks...but willing to buy at watch price.
		// symbols.add(new Quote("SBTINT", " S.B & T International  ", 39.55, null, "NSE", 57, 89, 0, true));
	}

	public static void main(String[] args) throws Exception
	{
		while (true) {

			final Date date = new Date();
			final int hours = date.getHours();
			final int dayOfWeek = date.getDay();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

			if (hours < 10 || hours > 17 || dayOfWeek == 0 || dayOfWeek == 6) {
				System.out.println("Waiting for market to open...<" + simpleDateFormat.format(date) + ">");
				Thread.sleep(1000 * 60 * 60);
				continue;
				// System.exit(0);
			}
			try {
				getQuote();
			} catch (Exception e) {
				e.printStackTrace();
				// continue..
			}
			Thread.sleep(1000 * 60 * 60);
			System.out.println("Thread woke up after 1 hr...<" + simpleDateFormat.format(date) + ">");
		}

	}

	private static Segment getElementsText(Source source, String elementName)
	{
		Element bodyElement = source.getNextElement(0, elementName);
		Segment contentSegment = (bodyElement == null) ? source : bodyElement.getContent();
		return contentSegment;
	}

	private static void getQuote() throws Exception
	{
		List mailMessages = new ArrayList();
		for (Iterator iter = symbols.iterator(); iter.hasNext();) {
			Quote quote = (Quote) iter.next();

			String scripCode = quote.getScripCode();
			System.out.println("getting ..." + quote.getDescriptiveName());
			Source source = new Source(new URL(URL + scripCode));

			source.fullSequentialParse();
			// System.out.println("\nAll text from TABLE (exluding content inside SCRIPT and STYLE elements):");
			Segment contentSegment = getElementsText(source, HTMLElementName.TABLE);
			final String extractedText = contentSegment.getTextExtractor().toString();

			// log(extractedText);

			// final String stockName = extractedText.substring(extractedText.indexOf("LATEST QUOTE STOCK NAME"), extractedText.indexOf("NSE BSE"));
			// log(stockName);
			final String stockPriceNSE_BSE = extractedText.substring(extractedText.indexOf("LAST TRADE PRICE"), extractedText.indexOf("BEST BID PRICE"));
			// log(stockPriceNSE_BSE);

			String[] priceNSE_BSE = stockPriceNSE_BSE.split(" ");
			final String priceAtNSE = priceNSE_BSE[3];
			// log(priceAtNSE);
			final String priceAtBSE = priceNSE_BSE[4];
			// log(priceAtBSE);

			final String stockMarketName = quote.getStockMarketName();
			double dCurrentPrice = 0;
			if (stockMarketName != null && stockMarketName.equals("NSE")) {
				dCurrentPrice = Double.parseDouble(priceAtNSE);
			} else if (stockMarketName != null && stockMarketName.equals("BSE")) {
				dCurrentPrice = Double.parseDouble(priceAtBSE);
			}

			if (quote.isWatchOnly()) {
				double watchPrice = quote.getPurchasePrice();
				if (dCurrentPrice <= watchPrice) {

					final String message = "(" + quote.getDescriptiveName() + ") Crossed Watch Price " + watchPrice + "...";
					System.out.println(message);
					mailMessages.add(message);
				}
			} else {
				double purchasePrice = quote.getPurchasePrice();
				double changePercent = Math.round((dCurrentPrice / purchasePrice) * 100 - 100);

				final String resultString = dCurrentPrice + "(" + changePercent + "%)";
				System.out.println(resultString);

				if (dCurrentPrice > purchasePrice) {
					System.out.println("< " + quote + "> Above purchase price... " + purchasePrice);
				}
				String reachedTarget = null;
				if (dCurrentPrice >= quote.getTarget2()) {
					reachedTarget = "target 2 ";
				} else if (dCurrentPrice >= quote.getTarget1()) {
					reachedTarget = "target 1 ";
				}
				if (reachedTarget != null) {
					final String message = "(" + quote.getDescriptiveName() + ") has raised to " + reachedTarget + "..." + resultString + " from " + purchasePrice;
					mailMessages.add(message);
				}
			}
		}
		if (mailMessages.size() > 0) {
			System.out.println(mailMessages + " sent to " + sendTo);
			// SimpleMail.sendMail(sendTo, "Stock Status", mailMessages.toString());
		}
		mailMessages.clear();
	}

	private static void log(final String str)
	{
		System.out.println(str);
	}

	static class Quote {
		public Quote(String symbol, String name, double purchaseOrWatchPrice, Date dateOfPurchase, String stockMarketName, double target1, double target2, long quantity, boolean watchOnly) {
			this.scripCode = symbol;
			this.descriptiveName = name;
			this.purchaseDate = dateOfPurchase;
			this.stockMarketName = stockMarketName;
			this.purchasePrice = purchaseOrWatchPrice;
			this.quantity = quantity;
			this.target1 = target1;
			this.target2 = target2;
			this.watchOnly = watchOnly;
		}

		boolean watchOnly = false;
		String scripCode;
		String descriptiveName;
		double purchasePrice;
		long quantity;
		Date purchaseDate;
		String stockMarketName;
		double target1;
		double target2;

		public double getTarget1()
		{
			return target1;
		}

		public void setTarget1(double target1)
		{
			this.target1 = target1;
		}

		public double getTarget2()
		{
			return target2;
		}

		public void setTarget2(double target2)
		{
			this.target2 = target2;
		}

		public String getStockMarketName()
		{
			return stockMarketName;
		}

		public void setStockMarketName(String stockMarketName)
		{
			this.stockMarketName = stockMarketName;
		}

		public String getDescriptiveName()
		{
			return descriptiveName;
		}

		public void setDescriptiveName(String descriptiveName)
		{
			this.descriptiveName = descriptiveName;
		}

		public Date getPurchaseDate()
		{
			return purchaseDate;
		}

		public void setPurchaseDate(Date purchaseDate)
		{
			this.purchaseDate = purchaseDate;
		}

		public double getPurchasePrice()
		{
			return purchasePrice;
		}

		public void setPurchasePrice(double purchasePrice)
		{
			this.purchasePrice = purchasePrice;
		}

		public String getScripCode()
		{
			return scripCode;
		}

		public void setScripCode(String symbol)
		{
			this.scripCode = symbol;
		}

		public String toString()
		{

			return new ToStringBuilder(this).reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}

		public long getQuantity()
		{
			return quantity;
		}

		public void setQuantity(long quantity)
		{
			this.quantity = quantity;
		}

		
		public boolean isWatchOnly()
		{
			return watchOnly;
		}

		
		public void setWatchOnly(boolean watchOnly)
		{
			this.watchOnly = watchOnly;
		}
	}
}
