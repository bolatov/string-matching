package de.saarland.hamming;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Almer Bolatov
 *         Date: 12/13/13
 *         Time: 4:56 PM
 */
public class BruteForce {
	private List<String> strings;

	public BruteForce(List<String> strings) {
		this.strings = strings;
	}

	public Set<Integer> search(String query, int k) {
		Set<Integer> results = new HashSet<>();

		for (int i = 0; i < strings.size(); i++) {
			String s = strings.get(i);
			if (areHammingEqual(query, s, k)) {
				results.add(i);
			}
		}

		return results;
	}

	public static boolean areHammingEqual(String s, String t, int k) {
		if (s == null || t == null)     return false;

		if (s.length() != t.length())   return false;

		if (k < 0)  return false;

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != t.charAt(i)) k--;

			if (k < 0)  return false;
		}

		return true;
	}
}
