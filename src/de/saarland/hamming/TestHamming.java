package de.saarland.hamming;

/**
 * @author Almer Bolatov
 *         Date: 10/16/13
 *         Time: 11:22 PM
 */
public class TestHamming {
	public static void main(String[] args) {
		Tree tree = new Tree();
		tree.addWord("far", 1);
		tree.addWord("fat", 2);
		tree.addWord("fit", 3);
		tree.addWord("pay", 4);
		tree.addWord("pin", 5);
		tree.addWord("sit", 6);

		tree.buildMismatchesIndex(1);

		GroupTreeStorage storage = GroupTreeStorage.getInstance();

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
