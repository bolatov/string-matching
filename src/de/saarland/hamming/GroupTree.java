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
	 * @param errorTrees
	 * @return a root of built structure
	 */
	public static GroupTree buildGroupTrees(List<Tree> errorTrees) {
		return null;
	}
}
