package de.ollie.servicemonitor.model;

import java.util.List;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;

/**
 * @author ollie (23.11.2021)
 */
@Accessors(chain = true)
@Data
@Generated
public class CheckRequest {

	public static final String HTTP_PROTOCOL = "http://";

	public enum ReturnedMediaType {
		JSON,
		STRING,
		XML;
	}

	private String authenticationBearer;
	private String checkExpression;
	private CheckRequestGroup group;
	private String host;
	private String name;
	private List<OutputAlternative> outputAlternatives;
	private String path;
	private Integer port;
	private ReturnedMediaType returnedMediaType;

	public String getUrl() {
		return HTTP_PROTOCOL + host + getPortStr() + getPathStr();
	}

	private String getPortStr() {
		return getPort() != null ? ":" + getPort() : "";
	}

	private String getPathStr() {
		return getPath() != null ? (!getPath().startsWith("/") ? "/" : "") + getPath() : "";
	}

}