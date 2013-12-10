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

	public static enum GroupType { ONE, TWO}

	private GroupType groupType;

	private String id;

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
//		Logger.log(TAG, String.format("buildGroup() groupNodes.size=%d", groupNodes.size()));

		assert groupNodes != null;
		assert groupNodes.size() > 0;

		if (groupNodes.size() == 0) {
			System.err.println("No error trees in the list.");
			return null;
		} else if (groupNodes.size() == 1) {
			return groupNodes.get(0);
		}

		/**
		 * Actually it spends less memory if all of the light children are grouped together
		 */
		else if (groupNodes.get(0).groupType.equals(GroupType.TWO)) {
			Node n = groupNodes.get(0).getNode();
			for (int i = 1; i < groupNodes.size(); i++) {
				n = Node.mergeNodes(n, groupNodes.get(i).getNode());
			}
			GroupNode gn = new GroupNode(null);
			gn.groupType = GroupType.TWO;
			gn.node = n;
			return gn;
		}

		return build(groupNodes, 0, groupNodes.size() - 1);
	}

	private static GroupNode build(List<GroupNode> groupNodes, int p, int r) {
//		Logger.log(TAG, String.format("build() pIndex=%d, qIndex=%d", p, r));

		GroupType type = groupNodes.get(0).groupType;

		if (p < r) {
			GroupNode parent =  new GroupNode(type);

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

	public List<Node> getSearchableNodes(String id) {
		List<Node> nodes = new LinkedList<>();

		if (this.groupType.equals(GroupType.ONE)) {
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
		} else {
			nodes.add(this.node);
		}

		return nodes;
	}

	/*public Set<Integer> search(char[] q, int i, int k, String id) {
//		Logger.log(TAG, String.format("search() query=%s, startIndex=%d, groupNodeId=%s", String.valueOf(q), i, id));

		assert q != null;
		assert q.length > 0;
		assert i >= 0;
		assert i < q.length;
		assert k >= 0;
//		assert id != null;
//		assert !"".equals(id);

		Set<Integer> results = groupType.equals(GroupType.ONE) ?
				searchTypeOne(q, i, k, id) :
				searchTypeTwo(q, i, k, id);

		assert results != null;

		return results;
	}*/

	/**
	 * Search type 1 group trees. Query all group nodes whose
	 * merge includes precisely Err(T,v_1),..,Err(T,v_(id-1))
	 *
	 * HINT: Queries Err(T,v_1),..,Err(T,v_id)
	 */
	/*private Set<Integer> searchTypeOne(char[] q, int i, int k, String id) {
		Logger.increment();
		Logger.log(TAG, String.format("searchTypeOne(): query=%s, k=%d, groupNodeId='%s'", String.valueOf(q).substring(i), k, id));
		GroupNode groupNode = findGroup(id);

//		assert groupNode != null;

		Set<Integer> results = new HashSet<>();

		if (groupNode != null) {
			Queue<Node> queue = new LinkedList<>();
			queue.add(groupNode.node);

			while (groupNode.getParent() != null) {
				GroupNode parent = groupNode.getParent();
				GroupNode lChild = parent.getLeftChild();
				if (lChild != null && !lChild.equals(groupNode)) {   // do not add the same node twice
					queue.add(lChild.node);
				}
				groupNode = parent;
			}

			Node n;
			while (!queue.isEmpty()) {
				n = queue.remove();
				results.addAll(n.search(q, i, k));
			}
		}

		Logger.decrement();
		return results;
	}*/

	/**
	 * Search type 2 group trees. Query all group nodes, except
	 * the one with id='id'
	 */
	/*private Set<Integer> searchTypeTwo(char[] q, int i, int k, String id) {
		Logger.increment();
		Logger.log(TAG, String.format("searchTypeTwo(): query=%s, k=%d, groupNodeId=%s", String.valueOf(q).substring(i), k, id));
		Set<Integer> results = new HashSet<>();

		*//**
		 * TODO type 2 group tree will have only one node, no need to query and look for the right position in the tree
		 *//*

		GroupNode groupNode = findGroup(id);
		if (groupNode == null) {
			groupNode = this;
			results.addAll(groupNode.getNode().search(q, i, k));
		} else {

			assert groupNode != null;

			Node n;
			while (groupNode.getParent() != null) {
				GroupNode parent = groupNode.getParent();
				GroupNode lChild = parent.getLeftChild();
				GroupNode rChild = parent.getRightChild();
				if (!lChild.equals(groupNode)) {
					n = lChild.getNode();
					results.addAll(n.search(q, i, k));
				} else {
					n = rChild.getNode();
					results.addAll(n.search(q, i, k));
				}
				groupNode = parent;
			}
		}

		Logger.decrement();
		return results;
	}*/

	private GroupNode findGroup(String groupNodeId) {
		Logger.increment();
		Logger.log(TAG, String.format("findGroup(): groupNodeId='%s', this.id=%s, \t%s", groupNodeId, this.id, this.toString()));

		Queue<GroupNode> queue = new LinkedList<>();
		queue.add(this);

		while (!queue.isEmpty()) {
			GroupNode gn = queue.remove();
			if (gn.id != null && gn.id.equals(groupNodeId)) {
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
