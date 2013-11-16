package de.saarland.hamming;

import de.saarland.util.Logger;

import java.util.*;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:17 PM
 */
public class GroupNode {
	private static final String TAG = GroupNode.class.getSimpleName();


	public enum GroupType { ONE, TWO;}
	private GroupType groupType;

	private String id;              // dummy id

	private GroupNode leftChild;
	private GroupNode rightChild;
	private GroupNode parent;
	private Node node;

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

		assert groupNodes != null;

		GroupType type = groupNodes.get(0).groupType;

		if (groupNodes.size() == 0) {
			System.err.println("No error trees in the list.");
			return null;
		} else if (groupNodes.size() == 1) {
			return groupNodes.get(0);
		}

		return build(groupNodes, 0, groupNodes.size() - 1);
	}

	private static GroupNode build(List<GroupNode> groupNodes, int p, int r) {
		Logger.log(TAG, String.format("build() pIndex=%d, qIndex=%d", p, r));

		GroupType type = groupNodes.get(0).groupType;

		if (p < r) {
			GroupNode result =  new GroupNode(type);

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

	public Set<Integer> search(char[] q, int i, int k, String id) {
		Logger.log(TAG, String.format("search() query=%s, startIndex=%d, groupNodeId=%c", String.valueOf(q), i, id));

		assert q != null;
		assert q.length > 0;
		assert i >= 0;
		assert i < q.length;
		assert k >= 0;
		assert id != null;
		assert !"".equals(id);

		Set<Integer> results = null;

		switch (groupType) {
			case ONE:
				results = searchTypeOne(q, i, k, id);
				break;
			case TWO:
				results = searchTypeTwo(q, i, k, id);
				break;
			default:
//				results = new HashSet<>();
				break;
		}

		assert results != null;

		return results;
	}

	/**
	 * Search type 1 group trees. Query all group nodes whose
	 * merge includes precisely Err(T,v_1),..,Err(T,v_(id-1))
	 * @param q
	 * @param i
	 * @param k
	 * @param id
	 * @return
	 */
	private Set<Integer> searchTypeOne(char[] q, int i, int k, String id) {
		GroupNode groupNode = findGroup(id);

		assert groupNode != null;

		List<Node> list = new LinkedList<>();
		list.add(groupNode.getNode());

		while (groupNode.getParent() != null) {
			GroupNode parent = groupNode.getParent();
			GroupNode lChild = parent.getLeftChild();
			if (lChild != null) {
				list.add(lChild.getNode());
			}
			groupNode = parent;
		}

		Set<Integer> results = new HashSet<>();
		for (Node n : list) {
			results.addAll(n.search(q, i, k));
		}

		return results;
	}

	/**
	 * Search type 2 group trees. Query all group nodes, except
	 * the one with id='id'
	 * @param q
	 * @param i
	 * @param k
	 * @param id
	 * @return
	 */
	private Set<Integer> searchTypeTwo(char[] q, int i, int k, String id) {
		GroupNode groupNode = findGroup(id);

		assert groupNode != null;

		List<Node> list = new LinkedList<>();
		list.add(groupNode.getNode());

		while (groupNode.getParent() != null) {
			GroupNode parent = groupNode.getParent();
			GroupNode lChild = parent.getLeftChild();
			GroupNode rChild = parent.getRightChild();
			if (!lChild.equals(groupNode)) {
				list.add(lChild.getNode());
			} else {
				list.add(rChild.getNode());
			}
			groupNode = parent;
		}

		Set<Integer> results = new HashSet<>();
		for (Node n : list) {
			results.addAll(n.search(q, i, k));
		}

		return results;
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
//			return;
		}

		assert k > 0;

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
