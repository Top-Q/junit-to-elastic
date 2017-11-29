package il.co.topq.config;

public class GenericBinderConfig extends AbstractPropertiesConfigFile {

	public GenericBinderConfig() {
		super(BinderOptions.values());
	}

	@Override
	protected String getFileName() {
		return "config.properties";
	}

	public enum BinderOptions implements ConfigOptions {
		// @formatter:off
		ELASTIC_HOST("elastic.host","localhost"),
		ELASTIC_PORT("elastic.port","8080"),
		SOURCE_FOLDER("source.folder","");
		// @formatter:on

		private String property;

		private String defaultValue;

		private BinderOptions(final String property, final String defaultValue) {
			this.property = property;
			this.defaultValue = defaultValue;
		}

		@Override
		public String getProperty() {
			return property;
		}

		@Override
		public String getDefaultValue() {
			return defaultValue;
		}

		@Override
		public ConfigOptions[] getOptions() {
			return BinderOptions.values();
		}
	}
}
