package de.saarland.util;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:23 PM
 */
public class Logger {
	static int line = 1;
	static int indent = 0;
	private Logger() {}

	private static boolean ON = false;

	public static void log(String tag, String msg) {
		if (ON) {
			System.out.println(String.format("%d.\t%s: %s%s", line++, tag, ind(), msg));
		}
	}

	public static void err(String tag, String msg) {
		if (ON) {
	//		System.err.println(String.format("%d.\t%s: %s", line++, tag, msg));
			System.out.println(String.format("%d.\t%s: %s%s", line++, tag, ind(), msg).toUpperCase());
		}
	}

	public static void increment() {
		indent++;
	}

	public static void decrement() {
		indent--;
	}

	private static String ind() {
		assert indent >= 0;

		String s = "";
		for (int i = 0; i < indent; i++) {
			s += "\t";
		}
		return s;
	}
}
