package il.co.topq.elastic;

import java.io.Closeable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import il.co.topq.elastic.ESClient;
import il.co.topq.model.ElasticsearchTest;

public class ElasticClient implements Closeable {

	private final static String INDEX_PREFIX = "teststash_";
	private static final String DOCUMENT = "test";
	private final String host;
	private final int port;
	private ESClient client;
	private boolean connected;
	private String index;

	public ElasticClient(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public void send(List<ElasticsearchTest> tests) throws IOException {
		if (!connected) {
			connect();
		}
		if (tests == null) {
			throw new IllegalStateException("tests can't be nul");
		}
		createIndex();
		String[] ids = new String[tests.size()];
		for (int i = 0; i < ids.length; i++) {
			ids[i] =  UUID.randomUUID().toString();
		}
		client.index(index).document(DOCUMENT).add().bulk(ids, tests);
	}

	private void createIndex() throws IOException {
		if (!connected) {
			throw new IllegalStateException("Client is not connected");
		}
		index = INDEX_PREFIX + new SimpleDateFormat("yy_MM").format(new Date());
		if (!client.index(index).isExists()) {
			client.index(index).create(resourceToString("mapping.json"));
		}

	}

	private String resourceToString(final String resourceName) throws IOException {
		try (Scanner s = new Scanner(getClass().getClassLoader().getResourceAsStream(resourceName))) {
			s.useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}
	}

	private void connect() {
		if (connected) {
			return;
		}
		client = new ESClient(host, port);
		connected = true;

	}

	@Override
	public void close() throws IOException {
		if (client != null && connected) {
			client.close();
			connected = false;
		}
	}

}
