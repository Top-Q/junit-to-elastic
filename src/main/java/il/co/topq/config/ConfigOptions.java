package il.co.topq.config;

public interface ConfigOptions {

	String getProperty();

	String getDefaultValue();

	ConfigOptions[] getOptions();

}
