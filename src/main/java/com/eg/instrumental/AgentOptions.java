package com.eg.instrumental;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * An Instrumental Agent connects to the Instrumental backend and is used as the control point for sending metrics.
 * // TODO: Add JMX Exporting...
 */
public class AgentOptions {
	private String apiKey;
	private boolean enabled = true;
	private boolean synchronous = false;
	private String host = "collector.instrumentalapp.com";
	private Integer port = 8000;


	public AgentOptions() {
	}

	public String getApiKey() {
		return apiKey;
	}

	public AgentOptions setApiKey(String apiKey) {
		this.apiKey = apiKey;
		return this;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public AgentOptions setEnabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public boolean getSynchronous() {
		return synchronous;
	}

	public AgentOptions setSynchronous(boolean synchronous) {
		this.synchronous = synchronous;
		return this;
	}

	public String getHost() {
		return host;
	}

	public AgentOptions setHost(String host) {
		this.host = host;
		return this;
	}

	public Integer getPort() {
		return port;
	}

	public AgentOptions setPort(Integer port) {
		this.port = port;
		return this;
	}


}
