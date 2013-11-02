package de.saarland.hamming;

import de.saarland.util.Logger;

import java.util.List;
import java.util.Set;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:17 PM
 */
public abstract class GroupNode {
	private static final String TAG = GroupNode.class.getName();

	protected char id;
	protected GroupNode left;
	protected GroupNode right;
	protected GroupNode parent;
	protected GroupNode node;

	public static GroupNode buildGroup(List<GroupNode> groupNodes) {
		Logger.log(TAG, String.format("buildGroup() groupNodes.size=%d", groupNodes.size()));

		// TODO IMPLEMENT
//		if (errorTrees.size() == 0) {
//			System.err.println("No error trees in the list.");
//			return null;
//		} else if (errorTrees.size() == 1) {
//			GroupTree groupTree = new GroupTree();
//			groupTree.setTree(errorTrees.get(0));
//			return groupTree;
//		}

		return build(groupNodes, 0, groupNodes.size() - 1);
	}

	private static GroupNode build(List<GroupNode> groupNodes, int p, int r) {
		Logger.log(TAG, String.format("build() pIndex=%d, qIndex=%d", p, r));

		// TODO IMPLEMENT
//		if (p < r) {
//			GroupTree result =  new GroupTree();
//
//			int q = (p + r) / 2;
//			GroupNode left = build(groupNodes, p, q);
//			GroupNode right = build(groupNodes, q + 1, r);
//
//			left.setParent(result);
//			right.setParent(result);
//
//			result.setLeftChild(left);
//			result.setRightChild(right);
//
//			Tree merged = Tree.merge(left.getTree(), right.getTree());
//			merged.setId(left.getTree().getId() + right.getTree().getId());
//			result.setTree(merged);
//
//			return result;

//		} else {
//			GroupNode result = new G();
//			result.setTree(errorTrees.get(p));
//
//			return result;
//		}
		return null;
	}

	public Set<Integer> search(String query, int startIndex, char groupNodeId) {
		Logger.log(TAG, String.format("search() query=%s, startIndex=%d, groupNodeId=%c", query, startIndex, groupNodeId));

		// TODO IMPLEMENT

		return null;
	}

	protected GroupNode findGroup(char groupNodeId) {
		Logger.log(TAG, String.format("findGroup() groupNodeId=%c", groupNodeId));

		// TODO IMPLEMENT

		return null;
	}
}
