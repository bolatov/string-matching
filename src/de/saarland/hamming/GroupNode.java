package de.saarland.hamming;

import de.saarland.util.Logger;

import java.util.*;

/**
 * Logical grouping of nodes in a binary tree manner
 *
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

	/**
	 * Constructor
	 */
	private GroupNode() {
		this.id = -1;
	}

	/**
	 * Constructor
	 * @param id - group node id. At the very bottom level it is equal to
	 *           a relative node depth. In the upper levels it simply -1
	 * @param node that is wrapped by this group node
	 */
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

	/**
	 * Groups nodes in a binary tree manner. Each node has at most two children,
	 * all nodes except the root have a pointer to their parent.
	 * @param groupNodes list of group nodes to build the structure from
	 * @return the root node of the structure
	 */
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

	/**
	 * Let n be the length of the groupNodes array. Divide the array into n subarrays, each containing 1 element.
	 * Repeatedly merge subarrays and make merged node a parent of the merged nodes until there is only 1 subarray
	 * remaining. This will be the root node of the structure.
	 * @param groupNodes array of group nodes to build the structure from
	 * @param p start index
	 * @param r end index
	 * @return root node of a created structure
	 */
	private static GroupNode build(List<GroupNode> groupNodes, int p, int r) {
		if (p < r) {
			GroupNode parent =  new GroupNode();

			int q = (p + r) / 2;
			GroupNode left = build(groupNodes, p, q);
			GroupNode right = build(groupNodes, q + 1, r);

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

	/**
	 * @param id the relative depth of the node in the heavy path
	 * @return the group nodes or the merge of group nodes whose id's are less or equal to the
	 * id given in a parameter
	 */
	public Queue<Node> getSearchableNodes(int id) {
		Queue<Node> nodes = new LinkedList<>();

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

	/**
	 * Find a group node with a specified id
	 * @param groupNodeId
	 * @return
	 */
	private GroupNode findGroup(int groupNodeId) {
		Logger.increment();
		Logger.log(TAG, String.format("findGroup(): depth='%d', this=%s", groupNodeId, this.toString()));

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
	public Queue<Node> getNodes() {
		Queue<Node> nodes = new LinkedList<>();

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
