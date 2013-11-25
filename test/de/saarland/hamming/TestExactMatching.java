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
		s.add("far");   // 0
		s.add("fat");   // 1
		s.add("fix");   // 2
		s.add("pay");   // 3
		s.add("pin");   // 4
		s.add("sit");   // 5

		Trie t = new Trie(s);
		int k = 0;

		Set<Integer> set;

		set = t.search("far", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(0));

		set = t.search("fat", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(1));

		set = t.search("fix", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(2));

		set = t.search("pay", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(3));

		set = t.search("pin", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(4));

		set = t.search("sit", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(5));

	}

	public void test2() {
		List<String> s = new ArrayList<>();
		s.add("x");     // 0
		s.add("y");     // 1
		s.add("xab");   // 2
		s.add("yac");   // 3

		Trie t = new Trie(s);
		int k = 0;

		Set<Integer> set;

		set = t.search("x", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(0));

		set = t.search("y", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(1));

		set = t.search("xab", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(2));

		set = t.search("yac", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(3));
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

		set = t.search("x", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(0));

		set = t.search("y", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(1));

		set = t.search("xaa", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(2));

		set = t.search("yaa", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(3));
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

		set = t.search("x", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(0));

		set = t.search("xa", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(1));

		set = t.search("xab", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(2));

		set = t.search("y", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(3));

		set = t.search("yab", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(4));

		set = t.search("yabc", k);
		assertEquals(1, set.size());
		assertTrue(set.contains(5));
	}
}
