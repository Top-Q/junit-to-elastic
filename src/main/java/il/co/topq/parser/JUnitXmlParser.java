package il.co.topq.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import il.co.topq.model.ElasticsearchTest;
import il.co.topq.model.ErrorFailure;

public class JUnitXmlParser extends DefaultHandler implements Parser {

	private List<ElasticsearchTest> tests;

	private String currentSuite;

	private ElasticsearchTest currentTest;

	private StringBuilder currentContent;

	private Map<String, String> globalProperties;

	@Override
	public void process(File source) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		saxParser.parse(source, this);
	}

	@Override
	public void startDocument() throws SAXException {
		tests = new ArrayList<ElasticsearchTest>();
		globalProperties = new HashMap<String, String>();
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		if (null == currentContent || null == currentTest) {
			return;
		}
		final String content = new String(Arrays.copyOfRange(ch, start, start + length));
		if (content.replace("\n", "").trim().isEmpty()) {
			return;
		}
		currentContent.append(content);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals("testsuite")) {
			startSuite(attributes.getValue("name"));
			return;
		}
		if (qName.equals("property")) {
			startProperty(attributes);
			return;

		}
		if (qName.equals("testcase")) {
			startTest(qName, attributes);
			return;
		}
		if (qName.equals("failure")) {
			startFailure(attributes);
			return;
		}
		if (qName.equals("error")) {
			startError(attributes);
			return;
		}
		if (qName.equals("system-out")) {
			startSystemOut();
			return;
		}
	}

	private void startError(Attributes attributes) {
		currentContent = new StringBuilder();
		currentTest.setStatus("error");
		currentTest.setErrorFailure(
				new ErrorFailure("error", attributes.getValue("type"), attributes.getValue("message")));
	}

	private void startFailure(Attributes attributes) {
		currentContent = new StringBuilder();
		currentTest.setStatus("failure");
		currentTest.setErrorFailure(
				new ErrorFailure("failure", attributes.getValue("type"), attributes.getValue("message")));
	}

	private void startSystemOut() {
		currentContent = new StringBuilder();
	}

	private void startProperty(Attributes attributes) {
		String key = attributes.getValue("name");
		key = key.replace('.', '_');
		String value = attributes.getValue("value");
		globalProperties.put(key, value);
	}

	private void startTest(String qName, Attributes attributes) {
		currentTest = new ElasticsearchTest();
		currentTest.setSuite(currentSuite);
		currentTest.setStatus("success");
		final Map<String, String> properties = attributesToMap(attributes);
		currentTest.setClassName(properties.get("classname"));
		currentTest.setName(properties.get("name"));
		currentTest.setTimeStampAsDate(new Date());
		if (null != properties.get("time")) {
			currentTest.setDuration(Double.parseDouble(properties.get("time")));
		}
//		HashMap<String,String> m = new HashMap<String,String>();
//		m.put("foo", "bar");
//		currentTest.setProperties(new HashMap<String,String>(m));
		currentTest.setProperties(new HashMap<String,String>(globalProperties));

	}

	private void startSuite(String name) {
		currentSuite = name;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals("testsuite")) {
			endSuite(qName);
			return;
		}
		if (qName.equals("testcase")) {
			endTest();
			return;
		}
		if (qName.equals("failure")) {
			endFailure();
			return;
		}
		if (qName.equals("error")) {
			endError();
			return;
		}

		if (qName.equals("system-out")) {
			endSystemOut();
			return;
		}
	}

	private void endError() {
	}

	private void endSystemOut() {
		currentTest.setStdout(currentContent.toString());
	}

	private void endFailure() {
	}

	private void endSuite(String qName) {
		currentSuite = "";
	}

	private void endTest() {
		tests.add(currentTest);
		currentTest = null;
	}

	private static Map<String, String> attributesToMap(Attributes attributes) {
		final Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < attributes.getLength(); i++) {
			map.put(attributes.getQName(i), attributes.getValue(i));
		}
		return map;
	}

	@Override
	public List<ElasticsearchTest> getTests() {
		return tests;
	}

}
