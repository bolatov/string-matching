package de.saarland.hamming;

import de.saarland.util.Logger;

import java.util.*;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:17 PM
 */
public class GroupNode {
	private static final String TAG = "GN";//GroupNode.class.getSimpleName();

	private int id;

	private GroupNode leftChild;
	private GroupNode rightChild;
	private GroupNode parent;
	private Node node;

	public GroupNode() {
		this.id = -1;
	}

	public GroupNode(int id, Node node) {
		this.id = id;
		this.node = node;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
//		Logger.log(TAG, String.format("buildGroup() groupNodes.size=%d", groupNodes.size()));

		assert groupNodes != null;
		assert groupNodes.size() > 0;

		if (groupNodes.size() == 0) {
			System.err.println("No error trees in the list.");
			return null;
		} else if (groupNodes.size() == 1) {
			return groupNodes.get(0);
		}

		return build(groupNodes, 0, groupNodes.size() - 1);
	}

	private static GroupNode build(List<GroupNode> groupNodes, int p, int r) {
		if (p < r) {
			GroupNode parent =  new GroupNode();

			int q = (p + r) / 2;
			GroupNode left = build(groupNodes, p, q);
			GroupNode right = build(groupNodes, q + 1, r);

			// test
			parent.setId(left.id + right.id);

			left.setParent(parent);
			right.setParent(parent);

			parent.setLeftChild(left);
			parent.setRightChild(right);

			Node merged = Node.mergeNodes(left.getNode(), right.getNode());
			parent.setNode(merged);

			return parent;
		}

		return groupNodes.get(p);
	}

	public List<Node> getSearchableNodes(int id) {
		List<Node> nodes = new LinkedList<>();

		GroupNode groupNode = findGroup(id);
//	    assert groupNode != null;
		if (groupNode != null) {
			nodes.add(groupNode.node);

			while (groupNode.getParent() != null) {
				GroupNode parent = groupNode.getParent();
				GroupNode lChild = parent.getLeftChild();
				if (lChild != null && !lChild.equals(groupNode)) {   // do not add the same node twice
					nodes.add(lChild.node);
				}
				groupNode = parent;
			}
		}

		return nodes;
	}

	private GroupNode findGroup(int groupNodeId) {
		Logger.increment();
		Logger.log(TAG, String.format("findGroup(): groupNodeId='%s', this.id=%s, \t%s", groupNodeId, this.id, this.toString()));

		Queue<GroupNode> queue = new LinkedList<>();
		queue.add(this);

		while (!queue.isEmpty()) {
			GroupNode gn = queue.remove();
//			if (gn.id != null && gn.id.equals(groupNodeId)) {
			if (gn.id == groupNodeId) {
				Logger.decrement();
				return gn;
			}

			if (gn.getLeftChild() != null)
				queue.add(gn.getLeftChild());

			if (gn.getRightChild() != null)
				queue.add(gn.getRightChild());
		}

		Logger.decrement();
		return null;
	}

	/**
	 * Get all nodes that are stored in this group node
	 * @return
	 */
	public List<Node> getNodes() {
		List<Node> nodes = new LinkedList<>();

		Queue<GroupNode> queue = new LinkedList<>();
		queue.add(this);
		while (!queue.isEmpty()) {
			GroupNode gn = queue.remove();
			nodes.add(gn.node);

			if (gn.leftChild != null) {
				queue.add(gn.leftChild);
			}

			if (gn.rightChild != null) {
				queue.add(gn.rightChild);
			}
		}

		return nodes;
	}
}
