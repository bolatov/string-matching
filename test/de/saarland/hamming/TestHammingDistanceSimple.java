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
public class TestHammingDistanceSimple extends TestCase {
	private static final String TAG = TestHammingDistanceSimple.class.getSimpleName();

	public void test0() {
		Logger.log(TAG, "test0()");

		final int K = 1;

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

		// exact matching
		Set<Integer> r1 = t.search("fat", K);
		assertTrue(r1.contains(0));
		assertTrue(r1.contains(1)); // exact matching
		assertTrue(r1.contains(2));
		assertTrue(r1.contains(5));
		assertTrue(r1.contains(6));
		assertEquals(5, r1.size());

		r1 = t.search("far", K);
		assertTrue(r1.contains(0)); // far$     exact
		assertTrue(r1.contains(1)); // fat$     approximate matching
		assertTrue(r1.contains(6)); // fat$     approximate matching
		assertEquals(3, r1.size());

//		continue on heavy path
		Set<Integer> r2 = t.search("for", K);
		assertTrue(r2.contains(0));
		assertEquals(1, r2.size());

		// type 2
		Set<Integer> r3 = t.search("lit", K);
		assertTrue(r3.contains(2));
		assertEquals(1, r3.size());

		Set<Integer> r4 = t.search("pax", K);
		assertTrue(r4.contains(3));
		assertTrue(r4.contains(6));
		assertTrue(r4.contains(7));
		assertEquals(3, r4.size());

		Set<Integer> r5 = t.search("pan", K);
		assertTrue(r5.contains(7));
		assertTrue(r5.contains(3));
		assertTrue(r5.contains(4));
		assertEquals(3, r5.size());

		Set<Integer> r6 = t.search("faz", K);
		assertTrue(r6.contains(1));
		assertTrue(r6.contains(0));
		assertTrue(r6.contains(6));
		assertEquals(3, r6.size());

		Set<Integer> r7 = t.search("zar", K);
		assertTrue(r7.contains(0));
		assertTrue(r7.contains(7));
		assertEquals(2, r7.size());

		Set<Integer> r8 = t.search("zat", K);
		assertTrue(r8.contains(1));
		assertTrue(r8.contains(5));
		assertEquals(2, r8.size());

		assertTrue(true);
	}

	public void test1() {
		final int K = 2;

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

		Set<Integer> r0 = t.search("fXX", K);
		assertEquals(4, r0.size());
		assertTrue(r0.contains(0));
		assertTrue(r0.contains(1));
		assertTrue(r0.contains(2));
		assertTrue(r0.contains(6));

		r0 = t.search("Xar", K);
		assertEquals(6, r0.size());
		assertTrue(r0.contains(0));     // far
		assertTrue(r0.contains(1));     // fat
		assertTrue(r0.contains(3));     // pay .
		assertTrue(r0.contains(5));     // sat
		assertTrue(r0.contains(6));     // fax .
		assertTrue(r0.contains(7));     // par

		r0 = t.search("XXX", K);
		assertEquals(0, r0.size());

		r0 = t.search("XXr", K);
		assertTrue(r0.contains(0));
		assertTrue(r0.contains(7));
		assertEquals(2, r0.size());

		r0 = t.search("XiX", K);
		assertTrue(r0.contains(2));
		assertTrue(r0.contains(4));
		assertEquals(2, r0.size());

		r0 = t.search("X", K);
		assertEquals(0, r0.size());

		r0 = t.search("s", K);
		assertEquals(0, r0.size());

		r0 = t.search("p", K);
		assertEquals(0, r0.size());

		r0 = t.search("f", K);
		assertEquals(0, r0.size());

		r0 = t.search("XXx", K);
		assertTrue(r0.contains(6));
		assertEquals(1, r0.size());

		r0 = t.search("XXt", K);
		assertTrue(r0.contains(1));
		assertTrue(r0.contains(2));
		assertTrue(r0.contains(5));
		assertEquals(3, r0.size());

		r0 = t.search("faX", K-1);
		assertTrue(r0.contains(0));
		assertTrue(r0.contains(1));
		assertTrue(r0.contains(6));
		assertEquals(3, r0.size());

		assertTrue(true);
	}
}
