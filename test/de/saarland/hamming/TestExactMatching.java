package de.saarland.hamming;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Almer Bolatov
 *         Date: 11/3/13
 *         Time: 8:28 PM
 */
public class TestExactMatching extends TestCase {
	public void test1() {
		List<String> s = new ArrayList<>();
		s.add("far");
		s.add("fat");
		s.add("fix");
		s.add("pay");
		s.add("pin");
		s.add("sit");

		Trie t = new Trie(s);
		int k = 0;

		Set<Integer> set;
		for (String query : s) {
			set = t.search(query, k);
			for (int i : set) {
				assertTrue(query.equals(s.get(i)));
			}
		}
	}

	public void test2() {
		List<String> s = new ArrayList<>();
		s.add("x");
		s.add("y");
		s.add("xab");
		s.add("yac");

		Trie t = new Trie(s);
		int k = 0;

		Set<Integer> set;
		for (String query : s) {
			set = t.search(query, k);
			for (int i : set) {
				assertTrue(query.equals(s.get(i)));
			}
		}
	}

	public void test3() {
		List<String> s = new ArrayList<>();
		s.add("x");
		s.add("y");
		s.add("xaa");
		s.add("yaa");

		Trie t = new Trie(s);
		int k = 0;

		Set<Integer> set;
		for (String query : s) {
			set = t.search(query, k);
			for (int i : set) {
				assertTrue(query.equals(s.get(i)));
			}
		}
	}

	public void test4() {
		List<String> s = new ArrayList<>();
		s.add("x");
		s.add("xa");
		s.add("xab");
		s.add("y");
		s.add("yab");
		s.add("yabc");

		Trie t = new Trie(s);
		int k = 0;

		Set<Integer> set;
		for (String query : s) {
			set = t.search(query, k);
			for (int i : set) {
				assertTrue(query.equals(s.get(i)));
			}
		}
	}
}
