package com.eg.instrumental;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

public class CollectorTest {

	@Test
	public void collectorLazyStart() {
		Collector c = new Collector("foobar");
		c.outputStream = System.out;

		Assert.assertFalse(c.isShutdown());
		Assert.assertFalse(c.isRunning());

		c.send(new Metric(Metric.Type.GAUGE, "collector.status", 1, 0, 1).toString(), false);

		Assert.assertTrue(c.isRunning());
		Assert.assertFalse(c.isShutdown());

		c.setShutdown(true);

		Assert.assertFalse("Failed to shutdown.", c.isRunning());
		Assert.assertTrue(c.isShutdown());
	}

	@Test
	public void collectorStreamTest() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Collector c = new Collector("foobar");
		c.outputStream = baos;

		Assert.assertEquals(0, baos.size());

		c.send(new Metric(Metric.Type.GAUGE, "collector.status", 1, 0, 1).toString(), true);

		Assert.assertNotEquals(0, baos.size());

		c.setShutdown(true);

		Assert.assertFalse("Failed to shutdown.", c.isRunning());
		Assert.assertTrue(c.isShutdown());
	}

	@Test
	public void restartTest() {
		// Initialize
		Collector c = new Collector("foobar");
		c.outputStream = System.out;

		Assert.assertFalse(c.isShutdown());
		Assert.assertFalse(c.isRunning());

		// Lazy start
		c.send(new Metric(Metric.Type.GAUGE, "collector.status", 1, 0, 1).toString(), false);

		Assert.assertTrue(c.isRunning());
		Assert.assertFalse(c.isShutdown());

		// Shutdown
		c.setShutdown(true);

		Assert.assertFalse("Failed to shutdown.", c.isRunning());
		Assert.assertTrue(c.isShutdown());

		// Re-enable.
		c.setShutdown(false);

		Assert.assertFalse(c.isShutdown());
		Assert.assertFalse(c.isRunning());

		// Lazy re-start
		c.send(new Metric(Metric.Type.GAUGE, "collector.status", 1, 0, 1).toString(), false);

		Assert.assertTrue(c.isRunning());
		Assert.assertFalse(c.isShutdown());

		// Shutdown
		c.setShutdown(true);

		Assert.assertFalse("Failed to shutdown.", c.isRunning());
		Assert.assertTrue(c.isShutdown());
	}
}
