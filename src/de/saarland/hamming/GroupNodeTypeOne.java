package de.saarland.hamming;

import java.util.Set;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:17 PM
 */
public class GroupNodeTypeOne extends GroupNode {
	private static final String TAG = GroupNodeTypeOne.class.getName();

	@Override
	public Set<Integer> search(String query, int startIndex, char groupNodeId) {
		Logger.log(TAG, String.format("search() query=%s, startIndex=%d, groupNodeId=%c", query, startIndex, groupNodeId));

		// TODO IMPLEMENT

		return super.search(query, startIndex, groupNodeId);
	}
}
