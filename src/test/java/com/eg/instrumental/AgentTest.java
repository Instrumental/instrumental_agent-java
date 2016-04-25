package com.eg.instrumental;

import org.junit.*;
import java.io.*;
import java.util.Scanner;
import java.util.Random;

public class AgentTest {

    private static long start = System.currentTimeMillis();

    private static Random r = new Random();
    private static String apiKey;

    @Before
    public void setUp() throws Exception {
        try {
            Scanner scanner = new Scanner( new File("test_key") );
            apiKey = scanner.useDelimiter("\\A").next();
        } catch(FileNotFoundException e) {
            Assert.assertTrue("Please put the test project key into file 'test_key' in the project root", false);
        }
    }

    @Test
    public void gaugeTest() {
        Agent agent = new Agent(apiKey);

        for (int i = 1; i < 20; i++) {
            float val = r.nextFloat() * 100;
            agent.gauge("test.gauge", val);
        }

        while (agent.getPending() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {}
        }

        // TODO: Assert the number of metrics sent.
    }

    @Test
    public void incrementTest() {
        Agent agent = new Agent(apiKey);

        for (int i = 1; i < 20; i++) {
            agent.increment("test.increment");
        }

        while (agent.getPending() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {}
        }

        // TODO: Assert the number of metrics sent.
    }

    @Test
    public void noticeTest() {
        Agent agent = new Agent(apiKey);

        agent.notice("test.execution", (System.currentTimeMillis() - start) / 1000, start);

        while (agent.getPending() > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {}
        }
    }

    @Test
    public void nonblockingTest() {
        Agent agent = new Agent(apiKey);

        for (int i = 1; i < (Connection.MAX_QUEUE_SIZE + 1); i++) {
            agent.increment("test.increment");
        }

        Assert.assertFalse("Queue buffer overrun when it shouldn't", agent.isQueueOverflowing());
    }
}
