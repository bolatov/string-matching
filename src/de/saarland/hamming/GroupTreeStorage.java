package de.saarland.hamming;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Almer Bolatov
 *         Date: 10/16/13
 *         Time: 1:50 AM
 */
@Deprecated
public class GroupTreeStorage {
	private static GroupTreeStorage instance;

	private Map<Integer, GroupTree> map;

	private GroupTreeStorage() {
		map = new HashMap<>();
	}

	public static GroupTreeStorage getInstance() {
		if (instance == null)
			instance = new GroupTreeStorage();
		return  instance;
	}

	public GroupTree getGroupTree(int heavyPath) {
		return map.get(heavyPath);
	}

	public void addGroupTree(int heavyPath, GroupTree groupTree) {
		map.put(heavyPath, groupTree);
	}
}
