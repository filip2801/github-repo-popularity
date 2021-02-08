package com.filip2801.yndexercise.utils;

public class Validator {

	public static String notBlank(String value) {
		if (value == null || value.trim().isBlank()) {
			throw new IllegalArgumentException("Value cannot be null");
		}

		return value;
	}

}
