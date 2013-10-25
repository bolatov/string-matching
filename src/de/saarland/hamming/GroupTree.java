package de.saarland.hamming;

import java.util.List;

/**
 * @author Almer Bolatov
 *         Date: 10/12/13
 *         Time: 1:38 PM
 */
public class GroupTree {
	private GroupTree leftChild;
	private GroupTree rightChild;
	private GroupTree parent;
	private Tree tree;

	public GroupTree getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(GroupTree leftChild) {
		this.leftChild = leftChild;
	}

	public GroupTree getRightChild() {
		return rightChild;
	}

	public void setRightChild(GroupTree rightChild) {
		this.rightChild = rightChild;
	}

	public GroupTree getParent() {
		return parent;
	}

	public void setParent(GroupTree parent) {
		this.parent = parent;
	}

	public Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	/**
	 * Builds a group tree structure from an array of trees.
	 *
	 * The idea is as in a merge sort. Split array into two parts
	 * until there is a single element in the array. Then merge
	 * in a bottom-up order.
	 *
	 * @param errorTrees
	 * @return a root of a built structure
	 */
	public static GroupTree buildGroupTrees(List<Tree> errorTrees) {
		if (errorTrees.size() == 0) {
			System.err.println("No error trees in the list.");
			return null;
		} else if (errorTrees.size() == 1) {
			GroupTree groupTree = new GroupTree();
			groupTree.setTree(errorTrees.get(0));
			return groupTree;
		}

		return group(errorTrees, 0, errorTrees.size() - 1);
	}

	private static GroupTree group(List<Tree> errorTrees, int p, int r) {
		if (p < r) {
			GroupTree result =  new GroupTree();

			int q = (p + r) / 2;
			GroupTree left = group(errorTrees, p, q);
			GroupTree right = group(errorTrees, q + 1, r);

			left.setParent(result);
			right.setParent(result);

			result.setLeftChild(left);
			result.setRightChild(right);

			Tree merged = Tree.merge(left.getTree(), right.getTree());
			merged.setId(left.getTree().getId() + right.getTree().getId());
			result.setTree(merged);

			return result;

		} else {
			GroupTree result = new GroupTree();
			result.setTree(errorTrees.get(p));

			return result;
		}
	}
}
