package de.saarland.hamming;

import de.saarland.util.Logger;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Almer Bolatov
 *         Date: 11/9/13
 *         Time: 10:38 PM
 */
public class TestBuildMismatchesIndex extends TestCase{
	private static final String TAG = TestBuildMismatchesIndex.class.getSimpleName();

	public void test1() {
		Logger.log(TAG, "test1(): START");
		List<String> s = new ArrayList<>();
		s.add("far");
		s.add("fat");
		s.add("fit");
		s.add("pay");
		s.add("pin");
		s.add("sit");

		Trie t = new Trie(s);
		int k = 1;
		t.buildMismatchesIndex(k);

		Node root = t.getRoot();
		GroupNode rootGroupNode = root.getGroupType1();
		assertNotNull(rootGroupNode);

		// root 1+2+3
		Node r123Node = rootGroupNode.getNode();
		assertNotNull(r123Node);
		assertEquals(1, r123Node.getEdges().size());
		Edge fEdge = r123Node.findEdge('f');
		assertNotNull(fEdge);
		assertEquals(0, fEdge.getSpan());
		Node fNode = fEdge.getEndNode();
		assertNotNull(fNode);
		assertEquals(2, fNode.getEdges().size());


		// root 3

		// root 1

		// root 2

		Logger.log(TAG, "test1(): END");
	}
}
