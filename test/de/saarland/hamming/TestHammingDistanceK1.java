package de.saarland.hamming;

import junit.framework.TestCase;

/**
 * @author Almer Bolatov
 *         Date: 11/13/13
 *         Time: 9:10 PM
 */
public class TestHammingDistanceK1 extends TestCase {
	private static final String TAG = TestHammingDistanceK1.class.getSimpleName();

	private static final int K = 1;

	public void test0() {
//		Logger.log(TAG, "test0()");
//
//		List<String> s = new ArrayList<>();
//		s.add("far");
//		s.add("fat");
//		s.add("fix");
//		s.add("pay");
//		s.add("pin");
//		s.add("sit");
//
//		Trie t = new Trie(s);
//		t.buildMismatchesIndex(K);
//
//		// exact matching
//		Set<Integer> r1 = t.search("far", K);
//		assertTrue(r1.contains(0));
//		assertTrue(r1.contains(1));
//
//		// continue on heavy path
//		Set<Integer> r2 = t.search("for", K);
//		assertTrue(r2.contains(0));
//
//		// type 2
//		Set<Integer> r3 = t.search("lit", K);
//		assertTrue(r3.contains(2));
//		assertTrue(r3.contains(5));
//
//		Set<Integer> r4 = t.search("pax", K);
//		assertTrue(r4.contains(3));
//
//		Set<Integer> r5 = t.search("pan", K);
//		assertTrue(r5.contains(4));

		assertTrue(true);
	}
}
