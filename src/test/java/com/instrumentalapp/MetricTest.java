package com.instrumentalapp;

import org.junit.*;
import java.io.*;
import java.util.Scanner;

public class MetricTest {

	@Test
	public void nameValidity() {
		Assert.assertTrue(Metric.Type.INCREMENT.isValid("good"));
		Assert.assertTrue(Metric.Type.INCREMENT.isValid("good.metric"));
		Assert.assertTrue(Metric.Type.INCREMENT.isValid("good.metric.name"));

		Assert.assertFalse(Metric.Type.INCREMENT.isValid("bad metric"));
		Assert.assertFalse(Metric.Type.INCREMENT.isValid(" badmetric"));
		Assert.assertFalse(Metric.Type.INCREMENT.isValid("badmetric "));
		Assert.assertFalse(Metric.Type.INCREMENT.isValid("bad(metric"));
	}
}
