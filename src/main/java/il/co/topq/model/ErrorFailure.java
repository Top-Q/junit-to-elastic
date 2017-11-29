package il.co.topq.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorFailure {

	@JsonProperty("errorOrFailure")
	private String errorOrFailure;

	@JsonProperty("type")
	private String type;

	@JsonProperty("message")
	private String message;

	public ErrorFailure() {
	}

	public ErrorFailure(String errorOrFailure, String type, String message) {
		this.errorOrFailure = errorOrFailure;
		this.type = type;
		this.message = message;
	}

	public String getErrorOrFailure() {
		return errorOrFailure;
	}

	public void setErrorOrFailure(String errorOrFailure) {
		this.errorOrFailure = errorOrFailure;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
