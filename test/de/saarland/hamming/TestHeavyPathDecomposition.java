package de.saarland.hamming;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Almer Bolatov
 *         Date: 11/6/13
 *         Time: 4:35 PM
 */
public class TestHeavyPathDecomposition extends TestCase {
	public void testDepths() {
		List<String> s = new ArrayList<>();
		s.add("far");
		s.add("fat");
		s.add("fit");
		s.add("pay");
		s.add("pin");
		s.add("sit");

		Trie t = new Trie(s);
		t.buildMismatchesIndex(0);

		// root
		Node root = t.getRoot();
		assertEquals(0, root.getDepth());

		// f
		Node fNode = root.findEdge('f').getEndNode();
		assertEquals(1, fNode.getDepth());

		// fa
		Node faNode = fNode.findEdge('a').getEndNode();
		assertEquals(2, faNode.getDepth());

		// fit
		Node fiNode = fNode.findEdge('i').getEndNode();
		assertEquals(0, fiNode.getDepth());

		// sit
		Node sNode = root.findEdge('s').getEndNode();
		assertEquals(0, sNode.getDepth());
	}
}
