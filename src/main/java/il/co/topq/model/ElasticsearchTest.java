package il.co.topq.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * 
 * @author Itai.Agmon*
 */
public class ElasticsearchTest {

	public static final SimpleDateFormat ELASTIC_SEARCH_TIMESTAMP_STRING_FORMATTER = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss");

	@JsonProperty("name")
	private String name;

	@JsonProperty("suite")
	private String suite;

	@JsonProperty("className")
	private String className;

	@JsonProperty("status")
	private String status;

	@JsonProperty("stdout")
	private String stdout;

	@JsonProperty("timestamp")
	private String timeStamp;

	@JsonProperty("duration")
	private double duration;

	@JsonProperty("parameters")
	private Map<String, String> parameters;

	@JsonProperty("properties")
	private Map<String, String> properties;
	
	@JsonProperty("errorOrFailure")
	private ErrorFailure errorFailure;
	
	public ElasticsearchTest() {
		ELASTIC_SEARCH_TIMESTAMP_STRING_FORMATTER.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@JsonIgnore
	@Override
	public boolean equals(Object other) {
		if (null == other) {
			return false;
		}
		if (other.hashCode() != hashCode()) {
			return false;
		}
		return true;
	}

	@JsonIgnore
	@Override
	public int hashCode() {
		int result = 31;
		if (name != null) {
			result = 31 * result + name.hashCode();
		}
		if (status != null) {
			result = 31 * result + status.hashCode();
		}

		if (timeStamp != null) {
			result = 31 * result + timeStamp.hashCode();
		}
		result = 31 * result + new Double(duration).intValue();

		if (parameters != null) {
			result = 31 * result + parameters.hashCode();
		}
		if (properties != null) {
			result = 31 * result + properties.hashCode();
		}
		return result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSuite() {
		return suite;
	}

	public void setSuite(String suite) {
		this.suite = suite;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStdout() {
		return stdout;
	}

	public void setStdout(String stdout) {
		this.stdout = stdout;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	@JsonIgnore
	public void setTimeStampAsDate(Date date) {
		setTimeStamp(ELASTIC_SEARCH_TIMESTAMP_STRING_FORMATTER.format(date));
	}

	@JsonIgnore
	public Date getTimeStampAsDate() {
		try {
			return ELASTIC_SEARCH_TIMESTAMP_STRING_FORMATTER.parse(getTimeStamp());
		} catch (ParseException e) {
			return null;
		}
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	

	public ErrorFailure getErrorFailure() {
		return errorFailure;
	}

	public void setErrorFailure(ErrorFailure errorFailure) {
		this.errorFailure = errorFailure;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ").append(name).append("\n");
		sb.append("Suite: ").append(suite).append("\n");
		sb.append("Status: ").append(status).append("\n");
		sb.append("Duration: ").append(duration).append("\n");
		sb.append("Stdout: ").append(stdout).append("\n");
		return sb.toString();
	}

}
