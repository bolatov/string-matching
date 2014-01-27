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
//	private static final String TAG = TestBuildMismatchesIndex.class.getSimpleName();
//
//	public void test1() {
//		Logger.log(TAG, "test1(): START");
//		List<String> s = new ArrayList<>();
//		s.add("far");
//		s.add("fat");
//		s.add("fit");
//		s.add("pay");
//		s.add("pin");
//		s.add("sit");
//
//		Trie t = new Trie(s);
//		int k = 1;
//		t.buildMismatchesIndex(k);
//
//		Node root = t.getRoot();
//		GroupNode rootGroupNode = root.getGroupType1();
//		assertNotNull(rootGroupNode);
//
//		// root 1+2+3
//		Node r123Node = rootGroupNode.getNode();
//		assertNotNull(r123Node);
//		assertEquals(1, r123Node.getEdges().size());
//		Edge fEdge = r123Node.findEdge('f');
//		assertNotNull(fEdge);
//		assertEquals(0, fEdge.getSpan());
//		Node fNode = fEdge.getEndNode();
//		assertNotNull(fNode);
//		assertEquals(2, fNode.getEdges().size());
//		assertEquals(2, fNode.findEdge('a').getEndNode().getEdges().size());
//		assertTrue(fNode.findEdge('a').getEndNode().findEdge('t').getEndNode().findEdge('$').getEndNode().getValues().contains(0));
//		assertTrue(fNode.findEdge('a').getEndNode().findEdge('t').getEndNode().findEdge('$').getEndNode().getValues().contains(2));
//		assertTrue(fNode.findEdge('a').getEndNode().findEdge('y').getEndNode().getValues().contains(3));
//
//
//		// root 3
//		Node r3Node = rootGroupNode.getRightChild().getNode();
//		assertTrue(r3Node.findEdge('f').getEndNode().findEdge('t').getEndNode().findEdge('$').getEndNode().getValues().contains(0));
//
//		// root 1+2
//		Node r12Node = rootGroupNode.getLeftChild().getNode();
//		assertTrue(r12Node.findEdge('f').getEndNode().findEdge('a').getEndNode().findEdge('t').getEndNode().getValues().contains(2));
//		assertTrue(r12Node.findEdge('f').getEndNode().findEdge('a').getEndNode().findEdge('y').getEndNode().getValues().contains(3));
//		assertTrue(r12Node.findEdge('f').getEndNode().findEdge('i').getEndNode().findEdge('n').getEndNode().getValues().contains(4));
//		assertTrue(r12Node.findEdge('f').getEndNode().findEdge('i').getEndNode().findEdge('t').getEndNode().getValues().contains(5));
//
//		// root 1
//		Node r1Node = rootGroupNode.getLeftChild().getLeftChild().getNode();
//		assertTrue(r1Node.findEdge('f').getEndNode().findEdge('a').getEndNode().getValues().contains(3));
//		assertTrue(r1Node.findEdge('f').getEndNode().findEdge('i').getEndNode().findEdge('n').getEndNode().getValues().contains(4));
//		assertTrue(r1Node.findEdge('f').getEndNode().findEdge('i').getEndNode().findEdge('t').getEndNode().getValues().contains(5));
//
//		// root 2
//		Node r2Node = rootGroupNode.getLeftChild().getRightChild().getNode();
//		assertTrue(r2Node.findEdge('f').getEndNode().findEdge('t').getEndNode().getValues().contains(2));
//
//		Logger.log(TAG, "test1(): END");
//	}
}
