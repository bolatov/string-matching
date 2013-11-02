package de.saarland.hamming;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Almer Bolatov
 *         Date: 10/16/13
 *         Time: 11:22 PM
 */
public class TestHamming {
	public static void main(String[] args) {
		List<String> strings = new ArrayList<>();

//		strings.add("far");
//		strings.add("fat");
//		strings.add("fit");
//		strings.add("pay");
//		strings.add("pin");
//		strings.add("sit");

		strings.add("bba"); // id = 0
		strings.add("bbc"); // id = 1, etc
		strings.add("bbab");
		strings.add("bbabk");

		Trie trie = new Trie(strings);
//		trie.buildMismatchesIndex(1);

//		System.out.println("Query for word 'bba' with k=0");
//		for (int i : trie.search("bba", 0)) {
//			System.out.println("matches to " + strings.get(i));
//		}

		Node rootCopy = trie.getRoot().deepCopy();

		Node sub = rootCopy.findEdge('b').sub();
		Node sub1 = sub.findEdge('b').getEndNode().findEdge('a').sub();
		System.out.println("Test end");



//		List<Tree> list = new ArrayList<>();
//		for (int i = 1; i <= 5; i++) {
//			Tree t = new Tree();
//			t.setId(i + "");
//			list.add(t);
//		}
//
//		GroupTree g = GroupTree.buildGroupTrees(list);
//		System.out.println("End.");
//
//		Queue<GroupTree> q = new LinkedList<>();
//		q.add(g);
//
//		while (!q.isEmpty()) {
//			GroupTree gt = q.remove();
//			System.out.println(gt.getTree().getId());
//
//			GroupTree left = gt.getLeftChild();
//			GroupTree right = gt.getRightChild();
//
//			if (left != null)
//				q.add(gt.getLeftChild());
//
//			if (right != null)
//				q.add(gt.getRightChild());
//		}
	}
}
