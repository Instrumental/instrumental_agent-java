package com.eg.instrumental;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * An Instrumental Agent connects to the Instrumental backend and is used as the control point for sending metrics.
 * // TODO: Add JMX Exporting...
 */
public class Agent {
	private AgentOptions agentOptions;
	private Connection connection;

	public Agent(final AgentOptions agentOptions) {
		this.agentOptions = agentOptions;
		initializeConnection();
	}

	public Agent(final String apiKey) {
		this.agentOptions = new AgentOptions();
		agentOptions.setApiKey(apiKey);
		initializeConnection();
	}

	private void initializeConnection() {
		if (agentOptions.getEnabled()) {
			connection = new Connection(agentOptions);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		if (agentOptions.getEnabled()) {
			connection.setShutdown(true);
		}
	}

	public boolean isRunning() {
		return agentOptions.getEnabled() && connection.isRunning();
	}

	public boolean getShutdown() {
		return !agentOptions.getEnabled() || connection.isShutdown();
	}

	public void setShutdown(boolean shutdown) {
		if (agentOptions.getEnabled()) {
			connection.setShutdown(shutdown);
		}
	}

	public int getPending() {
		if (agentOptions.getEnabled()) {
			return 0;
		} else {
			return connection.messages.size();
		}
	}

	public void increment(final String metricName, final Number value, final long time, final long count) {
		if (agentOptions.getEnabled()) {
			connection.send(new Metric(Metric.Type.INCREMENT, metricName, value, time, count).toString(), agentOptions.getSynchronous());
		}
	}

	public void increment(final String metricName, final Number value, final long time) {
		increment(metricName, value, time, 1);
	}

	public void increment(final String metricName, long time) {
		increment(metricName, 1, time);
	}

	public void increment(final String metricName) {
		increment(metricName, System.currentTimeMillis());
	}

	public void gauge(final String metricName, final Number value, final long time, final long count) {
		if (agentOptions.getEnabled()) {
			connection.send(new Metric(Metric.Type.GAUGE, metricName, value, time, count).toString(), agentOptions.getSynchronous());
		}
	}

	public void gauge(final String metricName, final Number value, final long time) {
		gauge(metricName, value, time, 1);
	}

	public void gauge(final String metricName, final Number value) {
		gauge(metricName, value, System.currentTimeMillis());
	}


	public void time(final String metricName, Runnable runnable) {
		// Synchronous in current thread.
		long start = System.currentTimeMillis();

		try {
			runnable.run();
		} finally {
			gauge(metricName, System.currentTimeMillis() - start);
		}
	}


	public <V> V time(final String metricName, Callable<V> callable) throws Exception {
		// Synchronous in current thread.
		long start = System.currentTimeMillis();

		V val = null;
		Exception thrown = null;
		try {
			val = callable.call();
		} finally {
			gauge(metricName, System.currentTimeMillis() - start);
		}

		if (thrown != null) {
			throw thrown;
		}

		return val;
	}


	// Wrap and submit to ExecutorService.
	public <V> Future<V> time(final String metricName, final ExecutorService executor, final Callable<V> callable) {
		return executor.submit(new Callable<V>() {
			@Override
			public V call() throws Exception {
				long start = System.currentTimeMillis();
				V val = null;
				Exception thrown = null;
				try {
					val = callable.call();
				} catch (Exception ex) {
					thrown = ex;
				} finally {
					gauge(metricName, System.currentTimeMillis() - start);
				}

				if (thrown != null) {
					throw thrown;
				}

				return val;
			}
		});
	}

	// Wrap and submit to ExecutorService.
	public <T> Future<T> time(final String metricName, final ExecutorService executor, final Runnable r, final T result) {
		return executor.submit(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				try {
					r.run();
				} finally {
					gauge(metricName, System.currentTimeMillis() - start);
				}
			}
		}, result);
	}

	public void notice(final String message, final long time, final long duration) {
		if (agentOptions.getEnabled()) {
			connection.send(new Notice(message, time, duration).toString(), agentOptions.getSynchronous());
		}
	}

	public void notice(final String message, long time) {
		notice(message, time, 0);
	}

	public void notice(final String message) {
		notice(message, System.currentTimeMillis(), 0);
	}

	public boolean isQueueOverflowing() {
		return connection.isQueueOverflowing();
	}
}
