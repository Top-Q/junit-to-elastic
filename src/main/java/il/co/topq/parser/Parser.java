package il.co.topq.parser;

import java.io.File;
import java.util.List;

import il.co.topq.model.ElasticsearchTest;

public interface Parser {

	void process(File source) throws Exception;

	List<ElasticsearchTest> getTests();

}
