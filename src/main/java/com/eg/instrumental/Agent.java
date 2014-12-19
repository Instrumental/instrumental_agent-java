package com.eg.instrumental;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * An Instrumental Agent connects to the Instrumental backend and is used as the control point for sending metrics.
 * // TODO: Add JMX Exporting...
 */
public class Agent {

	private boolean synchronous = false;

	private Collector collector;

	public Agent(final String apiKey) {
		collector = new Collector(apiKey);
	}

	@Override
	protected void finalize() throws Throwable {
		collector.setShutdown(true);
	}

	public String getApiKey() {
		return collector.getApiKey();
	}

	public void setApiKey(final String apiKey) {
		collector.setApiKey(apiKey);
	}

	public boolean isRunning() {
		return collector.isRunning();
	}

	public boolean getShutdown() {
		return collector.isShutdown();
	}

	public void setShutdown(boolean shutdown) {
		collector.setShutdown(shutdown);
	}

	public boolean getSynchronous() {
		return synchronous;
	}

	public int getPending() {
		return collector.messages.size();
	}

	public void setSynchronous(boolean synchronous) {
		this.synchronous = synchronous;
	}


	public void increment(final String metricName, final Number value, final long time) {
		collector.send(new Metric(Metric.Type.INCREMENT, metricName, value, time), synchronous);
	}

	public void increment(final String metricName, long time) {
		increment(metricName, 1, time);
	}

	public void increment(final String metricName) {
		increment(metricName, System.currentTimeMillis());
	}

	public void gauge(final String metricName, final Number value, final long time) {
		collector.send(new Metric(Metric.Type.GAUGE, metricName, value, time), synchronous);
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

	public void notice(final String message, long duration, final long time) {
		collector.send(new Metric(Metric.Type.NOTICE, message, duration, time), synchronous);
	}

	public void notice(final String message, long duration) {
		notice(message, duration, System.currentTimeMillis());
	}
}
