package il.co.topq;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import il.co.topq.config.GenericBinderConfig;
import il.co.topq.config.GenericBinderConfig.BinderOptions;
import il.co.topq.elastic.ElasticClient;
import il.co.topq.model.ElasticsearchTest;
import il.co.topq.parser.JUnitXmlParser;
import il.co.topq.parser.Parser;

public class Main {

	private Logger log = LoggerFactory.getLogger(Main.class);

	private GenericBinderConfig config = new GenericBinderConfig();

	public void run() {
		Parser binder = new JUnitXmlParser();
		final File source = new File(config.getPropertyAsString(BinderOptions.SOURCE_FOLDER));
		final String elasticHost = config.getPropertyAsString(BinderOptions.ELASTIC_HOST);
		final int elasticPort = config.getPropertyAsInt(BinderOptions.ELASTIC_PORT);

		if (!source.exists()) {
			log.error("Source is not exists " + source.getAbsolutePath());
			return;
		}
		long startTime = System.currentTimeMillis();
		final File[] sourceFiles = source.listFiles((dir, name) -> name.startsWith("TEST-") && name.endsWith(".xml"));
		if (sourceFiles.length == 0) {
			log.warn("Found no JUnit XML files in source folder " + source.getAbsolutePath());
			return;
		}
		log.info("Found " + sourceFiles.length + " JUnit XML files");
		log.info("Running JUnit parser");
		final List<ElasticsearchTest> tests = new ArrayList<>();
		for (File sourceFile : sourceFiles) {
			try {
				binder.process(sourceFile);
				tests.addAll(binder.getTests());
			} catch (Exception e) {
				log.error("Failed to run parser on  " + sourceFile.getAbsolutePath(), e);
			}
		}
		try (ElasticClient client = new ElasticClient(elasticHost, elasticPort)) {
			client.send(tests);
		} catch (IOException e) {
			log.error("Failure in sending tests to elastic", e);
		}
		log.info("Finished running local engine in " + (System.currentTimeMillis() - startTime) / 1000d + " seconds");
	}

	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.run();
	}
}
