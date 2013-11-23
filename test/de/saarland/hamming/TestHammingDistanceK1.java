package de.saarland.hamming;

import de.saarland.util.Logger;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Almer Bolatov
 *         Date: 11/13/13
 *         Time: 9:10 PM
 */
public class TestHammingDistanceK1 extends TestCase {
	private static final String TAG = TestHammingDistanceK1.class.getSimpleName();

	private static final int K = 1;

	public void test0() {
		Logger.log(TAG, "test0()");

		List<String> s = new ArrayList<>();
		s.add("far");   // 0
		s.add("fat");   // 1
		s.add("fit");   // 2
		s.add("pay");   // 3
		s.add("pin");   // 4
		s.add("sat");   // 5
		s.add("fax");   // 6
		s.add("par");   // 7

		Trie t = new Trie(s);
		t.buildMismatchesIndex(K);

		System.out.println("START queries");

//		Set<Integer> r0 = t.search("six", K);
//		assertTrue(r0.contains(5));

		// exact matching
		Set<Integer> r1 = t.search("fat", K);
		assertTrue(r1.contains(1)); // exact matching
		assertTrue(r1.contains(0));
		assertTrue(r1.contains(2));
		assertTrue(r1.contains(5));

		r1 = t.search("far", K);
		assertTrue(r1.contains(0)); // far$     exact
		assertTrue(r1.contains(1)); // fat$     approximate matching

//		continue on heavy path
		Set<Integer> r2 = t.search("for", K);
		assertTrue(r2.contains(0));

		// type 2
		Set<Integer> r3 = t.search("lit", K);
//		assertTrue(r3.contains(5));
		assertTrue(r3.contains(2));

		Set<Integer> r4 = t.search("pax", K);
		assertTrue(r4.contains(3));
		assertTrue(r4.contains(7));

		Set<Integer> r5 = t.search("pan", K);
		assertTrue(r5.contains(7));
		assertTrue(r5.contains(3));
		assertTrue(r5.contains(4));

		Set<Integer> r6 = t.search("faz", K);
		assertTrue(r6.contains(1));
		assertTrue(r6.contains(0));
		assertTrue(r6.contains(6));

		Set<Integer> r7 = t.search("zar", K);
		assertTrue(r7.contains(0));
		assertTrue(r7.contains(7));
		assertEquals(2, r7.size());

		assertTrue(true);
	}
}
