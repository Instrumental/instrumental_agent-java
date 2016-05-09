package com.instrumentalapp;

import org.junit.*;
import java.io.*;
import java.util.Scanner;

public class ConnectionTest {

  private static String apiKey;
  private Connection connection;

  @BeforeClass
  public static void setUp() throws Exception {
    try {
        Scanner scanner = new Scanner( new File("test_key") );
        apiKey = scanner.useDelimiter("\\A").next();
    } catch(FileNotFoundException e) {
        Assert.assertTrue("Please put the test project key into file 'test_key' in the project root", false);
    }
  }

  @Before
  public void initializeConnection() {
      connection = new Connection(new AgentOptions().setApiKey(apiKey));
  }

	@Test
	public void connectionLazyStart() {
		Connection c = new Connection(new AgentOptions().setApiKey(apiKey));
		c.outputStream = System.out;

		Assert.assertFalse(c.isShutdown());
		Assert.assertFalse(c.isRunning());

		c.send(new Metric(Metric.Type.GAUGE, "connection.status", 1, 0, 1).toString(), false);

		Assert.assertTrue(c.isRunning());
		Assert.assertFalse(c.isShutdown());

		c.setShutdown(true);

		Assert.assertFalse("Failed to shutdown.", c.isRunning());
		Assert.assertTrue(c.isShutdown());
	}

	@Test
	public void connectionStreamTest() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Connection c = new Connection(new AgentOptions().setApiKey(apiKey));
		c.outputStream = baos;

		Assert.assertEquals(0, baos.size());

		c.send(new Metric(Metric.Type.GAUGE, "connection.status", 1, 0, 1).toString(), true);
		c.send(new Metric(Metric.Type.GAUGE, "connection.status", 1, 0, 1).toString(), true);

		while (c.messages.size() > 0) {
		    try {
		        Thread.sleep(10);
		    } catch (InterruptedException ie) {}
		}

		Assert.assertNotEquals(0, baos.size());

		c.setShutdown(true);

		Assert.assertFalse("Failed to shutdown.", c.isRunning());
		Assert.assertTrue(c.isShutdown());
	}

	@Test
	public void restartTest() {
		// Initialize
		Connection c = new Connection(new AgentOptions().setApiKey(apiKey));
		c.outputStream = System.out;

		Assert.assertFalse(c.isShutdown());
		Assert.assertFalse(c.isRunning());

		// Lazy start
		c.send(new Metric(Metric.Type.GAUGE, "connection.status", 1, 0, 1).toString(), false);

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
		c.send(new Metric(Metric.Type.GAUGE, "connection.status", 1, 0, 1).toString(), false);

		Assert.assertTrue(c.isRunning());
		Assert.assertFalse(c.isShutdown());

		// Shutdown
		c.setShutdown(true);

		Assert.assertFalse("Failed to shutdown.", c.isRunning());
		Assert.assertTrue(c.isShutdown());
	}
}
