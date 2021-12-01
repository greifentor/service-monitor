package de.ollie.servicemonitor.model;

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

	private String name;
	private String message;
	private String host;
	private Integer port;
	private String path;
	private ReturnedMediaType returnedMediaType;
	private String checkExpression;

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