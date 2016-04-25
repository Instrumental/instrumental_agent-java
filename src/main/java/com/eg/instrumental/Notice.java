package com.eg.instrumental;

import java.util.regex.Pattern;

public class Notice {
	String message;
	long time;
	long duration;

	Notice(final String message, final long time, final long duration) {
		this.message = message;
		this.time = time;
		this.duration = duration;
	}


	@Override
	public String toString() throws IllegalArgumentException {
		if (isValid()) {
			return format();
		} else {
			throw new IllegalArgumentException("Invalid message : " + this.message);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Notice)) {
			return false;
		}

		Notice metric = (Notice) o;

		if (time != metric.time) {
			return false;
		}
		if (duration != metric.duration) {
			return false;
		}
		if (message != null ? !message.equals(metric.message) : metric.message != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = message != null ? message.hashCode() : 0;
		result = 31 * result + (int) (time ^ (time >>> 32));
		result = 31 * result + (int) (duration ^ (duration >>> 32));
		return result;
	}

	public String format() {
		return "notice " + (this.time / 1000) + " " + (this.duration / 1000) + " " + this.message;
	}

	public boolean isValid() {
		return message.indexOf("\r") == -1 && message.indexOf("\n") == -1;
	}
}
