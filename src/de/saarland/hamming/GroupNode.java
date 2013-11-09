package de.saarland.hamming;

import de.saarland.util.Logger;

import java.util.*;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:17 PM
 */
public class GroupNode {
	private static final String TAG = GroupNode.class.getName();



	public enum GroupType { ONE, TWO;}
	private GroupType groupType;

	private String id;              // dummy id

	private GroupNode leftChild;
	private GroupNode rightChild;
	private GroupNode parent;
	private Node node;
	private GroupNode() {

	}

	public GroupNode(GroupType groupType) {
		this.groupType = groupType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public GroupNode getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(GroupNode leftChild) {
		this.leftChild = leftChild;
	}

	public GroupNode getRightChild() {
		return rightChild;
	}

	public void setRightChild(GroupNode rightChild) {
		this.rightChild = rightChild;
	}

	public GroupNode getParent() {
		return parent;
	}

	public void setParent(GroupNode parent) {
		this.parent = parent;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public static GroupNode buildGroup(List<GroupNode> groupNodes) {
		Logger.log(TAG, String.format("buildGroup() groupNodes.size=%d", groupNodes.size()));

		// TODO Check
		if (groupNodes.size() == 0) {
			System.err.println("No error trees in the list.");
			return null;
		} else if (groupNodes.size() == 1) {
			GroupNode groupNode = new GroupNode();
			groupNode.setNode(groupNodes.get(0).getNode());
			return groupNode;
		}

		return build(groupNodes, 0, groupNodes.size() - 1);
	}

	private static GroupNode build(List<GroupNode> groupNodes, int p, int r) {
		Logger.log(TAG, String.format("build() pIndex=%d, qIndex=%d", p, r));

		// TODO Check
		if (p < r) {
			GroupNode result =  new GroupNode();

			int q = (p + r) / 2;
			GroupNode left = build(groupNodes, p, q);
			GroupNode right = build(groupNodes, q + 1, r);

			left.setParent(result);
			right.setParent(result);

			result.setLeftChild(left);
			result.setRightChild(right);

			Node merged = Node.mergeNodes(left.getNode(), right.getNode());
			result.setNode(merged);

			return result;
		}

		return groupNodes.get(p);
	}

	public Set<Integer> search(String query, int startIndex, String groupNodeId) {
		Logger.log(TAG, String.format("search() query=%s, startIndex=%d, groupNodeId=%c", query, startIndex, groupNodeId));

		GroupNode groupNode = findGroup(groupNodeId);

		Set<Integer> results = null;

		switch (groupType) {
			case ONE:
				results = searchTypeOne(query, startIndex);
				break;
			case TWO:
				results = searchTypeTwo(query, startIndex);
				break;
			default:
				results = new HashSet<>();
				break;
		}

		return results;
	}

	private Set<Integer> searchTypeOne(String query, int startIndex) {

		// TODO IMPLEMENT

		return null;
	}

	private Set<Integer> searchTypeTwo(String query, int startIndex) {

		// TODO IMPLEMENT

		return null;
	}

	private GroupNode findGroup(String groupNodeId) {
		Logger.log(TAG, String.format("findGroup() groupNodeId=%c", groupNodeId));

		Queue<GroupNode> queue = new LinkedList<>();
		queue.add(this);

		while (!queue.isEmpty()) {
			GroupNode gn = queue.remove();
			if (gn.id != null && gn.id.equals(groupNodeId)) {
				return gn;
			}

			if (gn.getLeftChild() != null)
				queue.add(gn.getLeftChild());

			if (gn.getRightChild() != null)
				queue.add(gn.getRightChild());
		}

		return null;
	}

	public void buildMismatchesIndex(int k) {
		Logger.log(TAG, String.format("buildMismatchesIndex() k=%d", k));

		if (k <= 0) {
			Logger.log(TAG, String.format(" k<=0 Nothing to do here"));
			return;
		}

		Queue<GroupNode> groupNodeQueue = new LinkedList<>();
		groupNodeQueue.add(this);

		while (!groupNodeQueue.isEmpty()) {
			GroupNode currGroupNode = groupNodeQueue.remove();
			currGroupNode.getNode().buildMismatchesIndex(k-1);

			GroupNode currLeftChild = currGroupNode.getLeftChild();
			GroupNode currRightChild = currGroupNode.getRightChild();

			if (currLeftChild != null) {
				groupNodeQueue.add(currLeftChild);
			}

			if (currRightChild != null) {
				groupNodeQueue.add(currRightChild);
			}
		}
	}
}
