package com.eg.instrumental;

import java.util.regex.Pattern;

/**
 * Created by bvarner on 12/19/14.
 */
public class Metric {
	Type type;
	String key;
	float value;
	long time;
	long count;

	Metric(final Type type, final String key, final Number value, final long time, final long count) {
		this.type = type;
		this.key = key;
		this.value = value.floatValue();
		this.time = time;
		this.count = count;
	}


	@Override
	public String toString() throws IllegalArgumentException {
		if (type.isValid(this.key)) {
			return type.format(this);
		} else {
			throw new IllegalArgumentException("Invalid name : " + this.key);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Metric)) {
			return false;
		}

		Metric metric = (Metric) o;

		if (time != metric.time) {
			return false;
		}
		if (Float.compare(metric.value, value) != 0) {
			return false;
		}
		if (key != null ? !key.equals(metric.key) : metric.key != null) {
			return false;
		}
		if (type != metric.type) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = type != null ? type.hashCode() : 0;
		result = 31 * result + (key != null ? key.hashCode() : 0);
		result = 31 * result + (value != +0.0f ? Float.floatToIntBits(value) : 0);
		result = 31 * result + (int) (time ^ (time >>> 32));
		return result;
	}


	/**
	 * Enumeration of Types....
	 */
	enum Type {
		GAUGE("gauge"),
		INCREMENT("increment"),
		NOTICE("notice");

		private static final Pattern NAME_PATTERN = Pattern.compile("^([\\d\\w\\-_])+\\.*[\\d\\w\\-_]+$");

		private String type;

		private Type(final String type) {
			this.type = type;
		}

		String format(final Metric metric) {
			if (type.equals("notice")) {
				return this.type + " " + (metric.time / 1000) + " " + metric.value + " " + metric.key;
			} else {
				return this.type + " " + metric.key + " " + metric.value + " " + (metric.time / 1000);
			}
		}

		boolean isValid(final String key) {
			if (type.equals("notice")) {
				return key.indexOf("\r") == -1 && key.indexOf("\n") == -1;
			} else {
				return NAME_PATTERN.matcher(key).matches();
			}
		}
	}
}
