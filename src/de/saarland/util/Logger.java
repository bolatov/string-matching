package de.saarland.util;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:23 PM
 */
public class Logger {
	static int line = 0;
	public static void log(String tag, String msg) {
		System.out.println(String.format("%d. %s: %s", line++, tag, msg));
	}

	public static void err(String tag, String msg) {
		System.err.println(String.format("%d. %s: %s", line++, tag, msg));
	}
}
