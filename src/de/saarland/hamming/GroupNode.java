package de.saarland.hamming;

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

		return null;
	}

	private static GroupNode build(List<GroupNode> groupNodes, int pIndex, int qIndex) {
		Logger.log(TAG, String.format("build() pIndex=%d, qIndex=%d", pIndex, qIndex));

		// TODO IMPLEMENT

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
