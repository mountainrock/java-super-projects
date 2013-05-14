package parser.htmlParser;
import net.htmlparser.jericho.*;
import java.util.*;

public class HTMLSanitiser {
	private HTMLSanitiser() {} // not instantiable

	// list of HTML elements that will be retained in the final output:
	private static final Set<String> VALID_ELEMENT_NAMES=new HashSet<String>(Arrays.asList(new String[] {
		HTMLElementName.BR,
		HTMLElementName.P,
		HTMLElementName.B,
		HTMLElementName.I,
		HTMLElementName.OL,
		HTMLElementName.UL,
		HTMLElementName.LI,
		HTMLElementName.A
	}));

	// list of HTML attributes that will be retained in the final output:
	private static final Set<String> VALID_ATTRIBUTE_NAMES=new HashSet<String>(Arrays.asList(new String[] {
		"id","class","href","target","title"
	}));

	private static final Object VALID_MARKER=new Object();

	public static String encodeInvalidMarkup(String pseudoHTML) {
		return pseudoHTML;//TODO: encodeInvalidMarkup(pseudoHTML,false);
	}

	public static String stripInvalidMarkup(String pseudoHTML) {
		return stripInvalidMarkup(pseudoHTML,false);
	}

	public static String stripInvalidMarkup(String pseudoHTML, boolean formatWhiteSpace) {
		return sanitise(pseudoHTML,formatWhiteSpace,true);
	}

	private static String sanitise(String pseudoHTML, boolean formatWhiteSpace, boolean stripInvalidElements) {
		Source source=new Source(pseudoHTML);
		source.fullSequentialParse();
		OutputDocument outputDocument=new OutputDocument(source);
		List<Tag> tags=source.getAllTags();
		int pos=0;
	  for (Tag tag : tags) {
			if (processTag(tag,outputDocument)) {
			  tag.setUserData(VALID_MARKER);
			} else {
				if (!stripInvalidElements) continue; // element will be encoded along with surrounding text
				outputDocument.remove(tag);
			}
			reencodeTextSegment(source,outputDocument,pos,tag.getBegin(),formatWhiteSpace);
			pos=tag.getEnd();
		}
	  reencodeTextSegment(source,outputDocument,pos,source.getEnd(),formatWhiteSpace);
		return outputDocument.toString();
	}

	private static boolean processTag(Tag tag, OutputDocument outputDocument) {
		String elementName=tag.getName();
		if (!VALID_ELEMENT_NAMES.contains(elementName)) return false;
		if (tag.getTagType()==StartTagType.NORMAL) {
			Element element=tag.getElement();
			if (HTMLElements.getEndTagRequiredElementNames().contains(elementName)) {
				if (element.getEndTag()==null) return false; // refect start tag if its required end tag is missing
			} else if (HTMLElements.getEndTagOptionalElementNames().contains(elementName)) {
				if (elementName==HTMLElementName.LI && !isValidLITag(tag)) return false; // reject invalid LI tags
				if (element.getEndTag()==null) outputDocument.insert(element.getEnd(),getEndTagHTML(elementName)); // insert optional end tag if it is missing
			}
			outputDocument.replace(tag,getStartTagHTML(element.getStartTag()));
		} else if (tag.getTagType()==EndTagType.NORMAL) {
			if (tag.getElement()==null) return false; // reject end tags that aren't associated with a start tag
			if (elementName==HTMLElementName.LI && !isValidLITag(tag)) return false; // reject invalid LI tags
			outputDocument.replace(tag,getEndTagHTML(elementName));
		} else {
			return false; // reject abnormal tags
		}
		return true;
	}

	private static boolean isValidLITag(Tag tag) {
		Element parentElement=tag.getElement().getParentElement();
		if (parentElement==null) return false; // ignore LI elements without a parent
		if (parentElement.getStartTag().getUserData()!=VALID_MARKER) return false; // ignore LI elements who's parent is not valid
		return parentElement.getName()==HTMLElementName.UL || parentElement.getName()==HTMLElementName.OL; // only accept LI tags who's immediate parent is UL or OL.
	}

	private static void reencodeTextSegment(Source source, OutputDocument outputDocument, int begin, int end, boolean formatWhiteSpace) {
	  if (begin>=end) return;
	  Segment textSegment=new Segment(source,begin,end);
		String decodedText=CharacterReference.decode(textSegment);
		String encodedText=formatWhiteSpace ? CharacterReference.encodeWithWhiteSpaceFormatting(decodedText) : CharacterReference.encode(decodedText);
    outputDocument.replace(textSegment,encodedText);
	}

	private static CharSequence getStartTagHTML(StartTag startTag) {
		// tidies and filters out non-approved attributes
		StringBuilder sb=new StringBuilder();
		sb.append('<').append(startTag.getName());
	  for (Attribute attribute : startTag.getAttributes()) {
	    if (VALID_ATTRIBUTE_NAMES.contains(attribute.getKey())) {
				sb.append(' ').append(attribute.getName());
				if (attribute.getValue()!=null) {
					sb.append("=\"");
				  sb.append(CharacterReference.encode(attribute.getValue()));
					sb.append('"');
				}
			}
	  }
	  if (startTag.getElement().getEndTag()==null && !HTMLElements.getEndTagOptionalElementNames().contains(startTag.getName())) sb.append(" /");
		sb.append('>');
		return sb;
	}

	private static String getEndTagHTML(String tagName) {
		return "</"+tagName+'>';
	}


	//////////////////////////////////////////////////////////////////////////////////////
	// THE METHODS BELOW ARE USED ONLY FOR DEMONSTRATING THE FUNCTIONALITY OF THE CLASS //
	//////////////////////////////////////////////////////////////////////////////////////
	// See test/src/samples/HTMLSanitiserTest.java for a comprehensive test suite.

	public static void main(String[] args) throws Exception {
		System.out.println("Examples of HTMLSanitiser.encodeInvalidMarkup:");
		System.out.println("----------------------------------------------\n");
		
		displayEncodeInvalidMarkup("ab & c","encode text");
		displayEncodeInvalidMarkup("abc <u>def</u> geh","<U> element not allowed");
		displayEncodeInvalidMarkup("<p>abc","add optional end tag");
		displayEncodeInvalidMarkup("<script>abc</script>","remove potentially dangerous script");
		displayEncodeInvalidMarkup("<p class=\"xyz\" onmouseover=\"nastyscript\">abc</p>","keep approved attributes but strip non-approved attributes");
		displayEncodeInvalidMarkup("<p id=abc class='xyz'>abc</p>","tidy up attributes to make them XHTML compliant");
		displayEncodeInvalidMarkup("List:<ul><li>A</li><li>B<li>C</ul>","inserts optional end tags");

		System.out.println("Examples of HTMLSanitiser.stripInvalidMarkup:");
		System.out.println("---------------------------------------------\n");

		displayStripInvalidMarkup("ab & c","encode text");
		displayStripInvalidMarkup("abc <u>def</u> geh","<U> element not allowed");
		displayStripInvalidMarkup("<p>abc","add optional end tag");
		displayStripInvalidMarkup("<script>abc</script>","remove potentially dangerous script");
		displayStripInvalidMarkup("<p class=\"xyz\" onmouseover=\"nastyscript\">abc</p>","keep approved attributes but strip non-approved attributes");
		displayStripInvalidMarkup("<p id=abc class='xyz'>abc</p>","tidy up attributes to make them XHTML compliant");
		displayStripInvalidMarkup("List:<ul><li>A</li><li>B<li>C</ul>","inserts optional end tags");
		displayStripInvalidMarkup("List:<li>A</li><li>B<li>C","missing required <UL> or <OL> element");
		displayStripInvalidMarkup("List:<ul><li>A</li><b><li>B</b><li>C</ul>","<LI> is invalid as it is not directly under <UL> or <OL>");

		System.out.println("Examples of HTMLSanitiser.stripInvalidMarkup with formatWhiteSpace=true:");
		System.out.println("------------------------------------------------------------------------\n");

		displayStripInvalidMarkup("abc\ndef",true,"convert LF to <BR>");
		displayStripInvalidMarkup("    abc",true,"ensure consecutive spaces are rendered");
		displayStripInvalidMarkup("\tabc",true,"convert TAB to equivalent of four spaces");
	}

	private static void displayEncodeInvalidMarkup(String input, String explanation) {
		display(input,explanation,HTMLSanitiser.encodeInvalidMarkup(input));
	}

	private static void displayStripInvalidMarkup(String input, String explanation) {
		display(input,explanation,HTMLSanitiser.stripInvalidMarkup(input));
	}

	private static void displayStripInvalidMarkup(String input, boolean formatWhiteSpace, String explanation) {
		display(input,explanation,HTMLSanitiser.stripInvalidMarkup(input,formatWhiteSpace));
	}

	private static void display(String input, String explanation, String output) {
		System.out.println(explanation+":\ninput : "+input+"\noutput: "+output+"\n");
	}
}
