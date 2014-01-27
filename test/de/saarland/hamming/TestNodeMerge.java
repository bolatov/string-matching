package de.saarland.hamming;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Almer Bolatov
 *         Date: 11/3/13
 *         Time: 12:41 PM
 */
public class TestNodeMerge extends TestCase {
////	public void testMergeNodes1() {
////		System.out.println("testMergeNodes1(): START");
////		List<String> s = new ArrayList<>();
////
////		s.add("x#");    // 0
////		s.add("y#");    // 1
////		s.add("x");     // 2
////		s.add("y");     // 3
////
////		Trie t = new Trie(s);
////
////		Node x = t.getRoot().findEdge('x').getEndNode();
////		Node y = t.getRoot().findEdge('y').getEndNode();
////
////		Node m = Node.mergeNodes(x, y);
////
////		Collection<Edge> edges = m.getEdges();
////
////		assertEquals(2, edges.size());
////
////		// $
////		Edge dollarEdge = m.findEdge('$');
////		assertNotNull(dollarEdge);
////		Node dollarNode = dollarEdge.getEndNode();
////		assertEquals(2, dollarNode.getValues().size());
////		assertTrue(dollarNode.getValues().contains(2));
////		assertTrue(dollarNode.getValues().contains(3));
////
////		// #
////		Edge hashEdge = m.findEdge('#');
////		assertNotNull(hashEdge);
////		Node hashNode = hashEdge.getEndNode();
////		assertEquals(2, hashNode.getValues().size());
////		assertTrue(hashNode.getValues().contains(0));
////		assertTrue(hashNode.getValues().contains(1));
////
////
////		System.out.println("testMergeNodes1(): END");
////	}
//
//	public void testMergeNodes2() {
//		System.out.println("testMergeNodes2(): START");
//		List<String> s = new ArrayList<>();
//
//		s.add("x");    // 0
//		s.add("y");    // 1
//		s.add("xab");   // 2
//		s.add("yac");   // 3
//
//		Trie t = new Trie(s);
//
//		Node x = t.getRoot().findEdge('x').getEndNode();
//		Node y = t.getRoot().findEdge('y').getEndNode();
//
//		Node m = Node.mergeNodes(x, y);
//
//		Collection<Edge> edges = m.getEdges();
//
//		assertEquals(2, edges.size());
//
//		// $
//		Edge dollarEdge = m.findEdge('$');
//		assertNotNull(dollarEdge);
//		assertEquals(0, dollarEdge.getSpan());            // beginIndex
//		Node dollarNode = dollarEdge.getEndNode();
//		assertEquals(2, dollarNode.getValues().size());
//		assertTrue(dollarNode.getValues().contains(0));
//		assertTrue(dollarNode.getValues().contains(1));
//
//		// a
//		Edge aEdge = m.findEdge('a');
//		assertNotNull(aEdge);
//		assertEquals(0, aEdge.getSpan());                 // beginIndex
//
//		Node aNode = aEdge.getEndNode();
//		assertNotNull(aNode);
//		assertEquals(2, aNode.getEdges().size());
//
//		// ab
//		Edge bEdge = aNode.findEdge('b');
//		assertNotNull(bEdge);
//		assertEquals(1, bEdge.getSpan());
//
//		Node bNode = bEdge.getEndNode();
//		assertEquals(1, bNode.getValues().size());
//		assertTrue(bNode.getValues().contains(2));
//		assertTrue(bNode.isLeaf());
//
//		// ac
//		Edge cEdge = aNode.findEdge('c');
//		assertNotNull(cEdge);
//		assertEquals(1, cEdge.getSpan());
//
//		Node cNode = cEdge.getEndNode();
//		assertEquals(1, cNode.getValues().size());
//		assertTrue(cNode.getValues().contains(3));
//		assertTrue(cNode.isLeaf());
//
//		System.out.println("testMergeNodes2(): END");
//	}
//
//	public void testMergeNodes3() {
//		System.out.println("testMergeNodes3(): START");
//		List<String> s = new ArrayList<>();
//
//		s.add("x");    // 0
//		s.add("y");    // 1
//		s.add("xaa");   // 2
//		s.add("yaa");   // 3
//
//		Trie t = new Trie(s);
//
//		Node x = t.getRoot().findEdge('x').getEndNode();
//		Node y = t.getRoot().findEdge('y').getEndNode();
//
//		Node m = Node.mergeNodes(x, y);
//
//		Collection<Edge> edges = m.getEdges();
//
//		assertEquals(2, edges.size());
//
//		// $
//		Edge dollarEdge = m.findEdge('$');
//		assertNotNull(dollarEdge);
//		assertEquals(1, dollarEdge.getBeginIndex());            // beginIndex
//		assertEquals(1, dollarEdge.getEndIndex());              // endIndex
//
//		Node dollarNode = dollarEdge.getEndNode();
//		assertEquals(2, dollarNode.getValues().size());
//		assertTrue(dollarNode.getValues().contains(0));
//		assertTrue(dollarNode.getValues().contains(1));
//
//		// a
//		Edge aEdge = m.findEdge('a');
//		assertNotNull(aEdge);
//		assertEquals(1, aEdge.getBeginIndex());
//		assertEquals(3, aEdge.getEndIndex());
//
//		Node aNode = aEdge.getEndNode();
//		assertNotNull(aNode);
//		assertTrue(aNode.isLeaf());
//		assertTrue(aNode.getValues().contains(2));
//		assertTrue(aNode.getValues().contains(3));
//
//		System.out.println("testMergeNodes3(): END");
//	}
//
//	public void testMergeNodes4() {
//		System.out.println("testMergeNodes4(): START");
//		List<String> s = new ArrayList<>();
//
//		s.add("x");     // 0
//		s.add("xa");    // 1
//		s.add("xab");   // 2
//		s.add("y");     // 3
//		s.add("yab");   // 4
//		s.add("yabc");  // 5
//
//		Trie t = new Trie(s);
//
//		Node x = t.getRoot().findEdge('x').getEndNode();
//		Node y = t.getRoot().findEdge('y').getEndNode();
//
//		Node m = Node.mergeNodes(x, y);
//
//		Collection<Edge> edges = m.getEdges();
//
//		assertEquals(2, edges.size());
//
//		// $
//		Edge dollarEdge = m.findEdge('$');
//		assertNotNull(dollarEdge);
//		assertEquals(1, dollarEdge.getBeginIndex());            // beginIndex
//		assertEquals(1, dollarEdge.getEndIndex());              // endIndex
//
//		Node dollarNode = dollarEdge.getEndNode();
//		assertEquals(2, dollarNode.getValues().size());
//		assertTrue(dollarNode.getValues().contains(0));
//		assertTrue(dollarNode.getValues().contains(3));
//
//		// a
//		Edge aEdge = m.findEdge('a');
//		assertNotNull(aEdge);
//		assertEquals(1, aEdge.getBeginIndex());
//		assertEquals(1, aEdge.getEndIndex());
//
//		Node aNode = aEdge.getEndNode();
//		assertNotNull(aNode);
//		assertTrue(!aNode.isLeaf());
//		assertEquals(2, aNode.getEdges().size());
//
//		// a$
//		Edge aDollarEdge = aNode.findEdge('$');
//		assertNotNull(aDollarEdge);
//		assertEquals(2, aDollarEdge.getBeginIndex());
//		assertEquals(2, aDollarEdge.getEndIndex());
//
//		Node aDollarNode = aDollarEdge.getEndNode();
//		assertNotNull(aDollarNode);
//		assertTrue(aDollarNode.isLeaf());
//		assertEquals(1, aDollarNode.getValues().size());
//		assertTrue(aDollarNode.getValues().contains(1));
//
//		// ab
//		Edge abEdge = aNode.findEdge('b');
//		assertNotNull(abEdge);
//		assertEquals(2, abEdge.getBeginIndex());
//		assertEquals(2, abEdge.getEndIndex());
//
//		Node abNode = abEdge.getEndNode();
//		assertNotNull(abNode);
//		assertEquals(2, abNode.getEdges().size());
//
//		// ab$
//		Edge abDollarEdge = abNode.findEdge('$');
//		assertNotNull(abDollarEdge);
//		assertEquals(3, abDollarEdge.getBeginIndex());
//		assertEquals(3, abDollarEdge.getEndIndex());
//
//		Node abDollarNode = abDollarEdge.getEndNode();
//		assertTrue(abDollarNode.isLeaf());
//		assertEquals(2, abDollarNode.getValues().size());
//		assertTrue(abDollarNode.getValues().contains(2));
//		assertTrue(abDollarNode.getValues().contains(4));
//
//		// abc
//		Edge abcEdge = abNode.findEdge('c');
//		assertNotNull(abcEdge);
//		assertEquals(3, abcEdge.getBeginIndex());
//		assertEquals(4, abcEdge.getEndIndex());
//
//		Node abcNode = abcEdge.getEndNode();
//		assertNotNull(abcNode);
//		assertTrue(abcNode.isLeaf());
//		assertEquals(1, abcNode.getValues().size());
//		assertTrue(abcNode.getValues().contains(5));
//
//		System.out.println("testMergeNodes4(): END");
//	}
//
//	public void testMergeNodes5() {
//		System.out.println("testMergeNodes5(): START");
//		List<String> s = new ArrayList<>();
//
//		s.add("xaabcef");   // 0
//		s.add("xaabcebk");  // 1
//		s.add("xaabce");    // 2
//		s.add("xaab");      // 3
//		s.add("yaa");       // 4
//		s.add("yaabc");     // 5
//		s.add("yaabcef");   // 6
//		s.add("x");         // 7
//		s.add("y");         // 8
//
//		Trie t = new Trie(s);
//
//		Node x = t.getRoot().findEdge('x').getEndNode();
//		Node y = t.getRoot().findEdge('y').getEndNode();
//
//		Node m = Node.mergeNodes(x, y);
//
//		Collection<Edge> edges = m.getEdges();
//
//		assertEquals(2, edges.size());
//
//		// $
//		Edge dEdge = m.findEdge('$');
//		assertNotNull(dEdge);
//		assertEquals(1, dEdge.getBeginIndex());
//		assertEquals(1, dEdge.getEndIndex());
//
//		Node dNode = dEdge.getEndNode();
//		assertNotNull(dNode);
//		assertTrue(dNode.isLeaf());
//		assertEquals(2, dNode.getValues().size());
//		assertTrue(dNode.getValues().contains(7));
//		assertTrue(dNode.getValues().contains(8));
//
//		// aa
//		Edge aEdge = m.findEdge('a');
//		assertNotNull(aEdge);
//		assertEquals(1, aEdge.getSpan());
//
//		Node aNode = aEdge.getEndNode();
//		assertNotNull(aNode);
//		assertFalse(aNode.isLeaf());
//
//		// aa$
//		Edge aaDollarEdge = aNode.findEdge('$');
//		assertNotNull(aaDollarEdge);
//		assertEquals(0, aaDollarEdge.getSpan());
//
//		Node aaDollarNode = aaDollarEdge.getEndNode();
//		assertNotNull(aaDollarNode);
//		assertTrue(aaDollarNode.isLeaf());
//		assertEquals(1, aaDollarNode.getValues().size());
//		assertTrue(aaDollarNode.getValues().contains(4));
//
//		// aab
//		Edge aabEdge = aNode.findEdge('b');
//		assertNotNull(aabEdge);
//		assertEquals(0, aabEdge.getSpan());
//
//		Node aabNode = aabEdge.getEndNode();
//		assertNotNull(aabNode);
//		assertFalse(aabNode.isLeaf());
//
//		// aab$
//		Edge aabDollarEdge = aabNode.findEdge('$');
//
//		Node aabDollarNode = aabDollarEdge.getEndNode();
//		assertTrue(aabDollarNode.isLeaf());
//		assertEquals(1, aabDollarNode.getValues().size());
//		assertTrue(aabDollarNode.getValues().contains(3));
//
//		// aabc
//
//		// aabc$
//
//		// aabce
//
//		// aabce$
//
//		// aabcef$
//		Edge lastF = aabNode.findEdge('c').getEndNode().findEdge('e').getEndNode().findEdge('f');
//		assertEquals(1, lastF.getSpan());
//
//		Node lastFNode = lastF.getEndNode();
//		assertTrue(lastFNode.isLeaf());
//		assertEquals(2, lastFNode.getValues().size());
//		assertTrue(lastFNode.getValues().contains(0));
//		assertTrue(lastFNode.getValues().contains(6));
//
//		// aabcebk$
//		Edge last = aabNode.findEdge('c').getEndNode().findEdge('e').getEndNode().findEdge('b');
//		assertEquals(2, last.getSpan());
//
//		Node lastNode = last.getEndNode();
//		assertTrue(lastNode.isLeaf());
//		assertEquals(1, lastNode.getValues().size());
//		assertTrue(lastNode.getValues().contains(1));
//
//
//		System.out.println("testMergeNodes5(): END");
//	}
}
