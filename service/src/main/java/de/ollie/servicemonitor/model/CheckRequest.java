package de.ollie.servicemonitor.model;

import java.util.List;

import lombok.Data;
import lombok.Generated;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author ollie (23.11.2021)
 */
@Accessors(chain = true)
@Data
@Generated
@ToString(exclude = { "group" })
public class CheckRequest {

	public static final String HTTP_PROTOCOL = "http://";
	public static final String HTTPS_PROTOCOL = "https://";

	public enum ReturnedMediaType {
		JSON,
		STRING,
		XML;
	}

	private String authenticationBearer;
	private String checkExpression;
	private CheckRequestGroup group;
	private String host;
	private boolean https;
	private String name;
	private List<OutputAlternative> outputAlternatives;
	private String path;
	private Integer port;
	private ReturnedMediaType returnedMediaType;

	public String getUrl() {
		return (isHttps() ? HTTPS_PROTOCOL : HTTP_PROTOCOL) + host + getPortStr() + getPathStr();
	}

	private String getPortStr() {
		return getPort() != null ? ":" + getPort() : "";
	}

	private String getPathStr() {
		return getPath() != null ? (!getPath().startsWith("/") ? "/" : "") + getPath() : "";
	}

}