package de.saarland;

import de.saarland.hamming.Edge;
import de.saarland.hamming.Node;
import de.saarland.hamming.Trie;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Almer Bolatov
 *         Date: 11/11/13
 *         Time: 7:02 PM
 */
public class TestGather {
//	public static void main(String[] args) {
//		List<String> s = new ArrayList<>();
//		s.add("abcp");
//		s.add("km");
//		s.add("$");
//		s.add("#");
//		s.add("ny");
//
//
//		Trie t = new Trie(s);
//		Node dollar = new Node(t);
//		new Edge(2, 0, 0, dollar).insert();
//
//		Node hash = new Node(t);
//		new Edge(3, 0, 0, hash).insert();
//
//		Node v0 = new Node(t);
//		Edge v0_v1 = new Edge(0, 0, 1, v0);
//		v0_v1.insert();
//		v0.setHeavyEdge(v0_v1);
//		v0.setDepth(0);
//
//		Node v1 = v0_v1.getEndNode();
//		Edge v1_v2 = new Edge(0, 2, 3, v1);
//		v1_v2.insert();
//		v1.setHeavyEdge(v1_v2);
//		v1.setDepth(1);
//
//		Node v2 = v1_v2.getEndNode();
//		Edge v2_v3 = new Edge(1, 0, 1, v2);
//		v2_v3.insert();
//		v2.setHeavyEdge(v2_v3);
//		v2.setDepth(2);
//
//		Node v3 = v2_v3.getEndNode();
//		Edge v3_v4 = new Edge(4, 0, 1, v3);
//		v3_v4.insert();
//		v3.setHeavyEdge(v3_v4);
//		v3.setDepth(3);
//
//		Node mergedChildren = Node.mergeNodes(dollar, hash);
//
//		// *
//		List<Node> nodes = new ArrayList<>();
//		final Node head = v0;
//		Node current = v0;
//		while (!current.isLeaf()) {
//
//			final Edge nextHeavyEdge = current.getHeavyEdge();
//			final int nextStringIndex =  nextHeavyEdge.getStringIndex();
//			final int nextBeginIndex = nextHeavyEdge.getBeginIndex();
//
//			int prevStr = -1;
//
//			Node errTV = new Node(t);
//
//			Edge lastEdge = null;
//
//			// begin
//			// str(l)
//			Node temp = head;
//			for (int i = 0; i < current.getDepth(); i++) {
//				Edge tempHeavy = temp.getHeavyEdge();
//				int tStr = tempHeavy.getStringIndex();
//				int tBegin = tempHeavy.getBeginIndex();
//				int tEnd = tempHeavy.getEndIndex();
//				if (lastEdge == null) {
//					lastEdge = new Edge(tStr, tBegin, tEnd, errTV);
//					lastEdge.insert();
//				} else {
//					if (prevStr == tStr) {
//						lastEdge.setEndIndex(tEnd);
//					} else {
//						Node lastNode = lastEdge.getEndNode();
//						Edge newLastEdge = new Edge(tStr, tBegin, tEnd, lastNode);
//						newLastEdge.insert();
//						lastEdge = newLastEdge;
//					}
//				}
//
//				prevStr = tStr;
//				temp = tempHeavy.getEndNode();
//			}
//
//			// nextChar
//			if (lastEdge == null) {
//				lastEdge = new Edge(nextStringIndex, nextBeginIndex, nextBeginIndex, errTV);
//				lastEdge.insert();
//			} else {
//				int lastStringIndex = lastEdge.getStringIndex();
//				if (lastStringIndex == nextStringIndex) {
//					int newEndIndex = lastEdge.getEndIndex() + 1;
//					lastEdge.setEndIndex(newEndIndex);
//				} else {
//					Node lastNode = lastEdge.getEndNode();
//					Edge newLastEdge = new Edge(nextStringIndex, nextBeginIndex, nextBeginIndex, lastNode);
//					newLastEdge.insert();
//					lastEdge = newLastEdge;
//				}
//			}
//			// end
//
//			assert lastEdge != null;
//
//			Node endNode = lastEdge.getEndNode();
//			for (char ch : mergedChildren.nextChars()) {
//				final Edge edge = mergedChildren.findEdge(ch);
//				endNode.addEdge(edge.getStringIndex(), edge.getBeginIndex(), edge);
//			}
//
//			nodes.add(errTV);
//
//			current = nextHeavyEdge.getEndNode();
//		}
//
//		// V0
//		Node ev0 = nodes.get(0);
//		Edge ev0A = ev0.findEdge('a');
//		assert ev0A != null;
//		assert ev0A.getSpan() == 0;
//		assert ev0A.getStringIndex() == 0;
//		Node ev0End = ev0A.getEndNode();
//		assert ev0End.getEdges().size() == 2;
//		assert ev0End.findEdge('$') != null;
//		assert ev0End.findEdge('#') != null;
//
//
//		// V1
//		Node ev1 = nodes.get(1);
//		Edge ev1A = ev1.findEdge('a');
//		assert ev1A != null;
//		assert ev1A.getSpan() == 2;
//		assert ev1A.getStringIndex() == 0;
//		Node ev1End = ev1A.getEndNode();
//		assert ev1End.getEdges().size() == 2;
//		assert ev1End.findEdge('$') != null;
//		assert ev1End.findEdge('#') != null;
//
//		// V2
//		Node ev2 = nodes.get(2);
//		Edge ev2A = ev2.findEdge('a');
//		assert ev2A != null;
//		assert ev2A.getSpan() == 3;
//		assert ev2A.getStringIndex() == 0;
//		Node ev2End = ev2A.getEndNode();
//
//		System.out.println("dummy");
//
//		assert ev2End.getEdges().size() == 1;
//		assert ev2End.findEdge('k') != null;
//		assert ev2End.findEdge('k').getStringIndex() == 1;
//		assert ev2End.findEdge('k').getSpan() == 0;
//		Node ev2EndEnd = ev2End.findEdge('k').getEndNode();
//		assert ev2EndEnd != null;
//		assert ev2EndEnd.findEdge('$') != null;
//		assert ev2EndEnd.findEdge('#') != null;
//
//		// V3
//		Node ev3 = nodes.get(3);
//		Edge ev3A = ev3.findEdge('a');
//		assert ev3A != null;
//		assert ev3A.getSpan() == 3;
//		assert ev3A.getStringIndex() == 0;
//		Node ev3End = ev3A.getEndNode();
//		assert ev3End.getEdges().size() == 1;
//		assert ev3End.findEdge('k') != null;
//		assert ev3End.findEdge('k').getStringIndex() == 1;
//		assert ev3End.findEdge('k').getSpan() == 1;
//		Node ev3EndEnd = ev3End.findEdge('k').getEndNode();
//		assert ev3EndEnd != null;
//		assert ev3EndEnd.getEdges().size() == 1;
//		assert ev3EndEnd.findEdge('n') != null;
//		assert ev3EndEnd.findEdge('n').getStringIndex() == 4;
//		assert ev3EndEnd.findEdge('n').getSpan() == 0;
//		Node ev3EndEndEnd = ev3EndEnd.findEdge('n').getEndNode();
//		assert ev3EndEndEnd != null;
//		assert ev3EndEndEnd.findEdge('$') != null;
//		assert ev3EndEndEnd.findEdge('#') != null;
//
//		System.out.println("END");
//	}
}
