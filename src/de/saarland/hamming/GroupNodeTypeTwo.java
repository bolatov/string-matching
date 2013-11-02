package de.saarland.hamming;

import de.saarland.util.Logger;

import java.util.Set;

/**
 * @author Almer Bolatov
 *         Date: 10/31/13
 *         Time: 1:18 PM
 */
public class GroupNodeTypeTwo extends GroupNode {

	private static final String TAG = GroupNodeTypeTwo.class.getName();

	@Override
	public Set<Integer> search(String query, int startIndex, char groupNodeId) {
		Logger.log(TAG, String.format("search() query=%s, startIndex=%d, groupNodeId=%c", query, startIndex, groupNodeId));

		// TODO IMPLEMENT

		return super.search(query, startIndex, groupNodeId);
	}
}
