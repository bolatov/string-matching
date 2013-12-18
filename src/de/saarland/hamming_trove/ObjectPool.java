package de.saarland.hamming_trove;

import de.saarland.util.Pair;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * @author Almer Bolatov
 *         Date: 12/12/13
 *         Time: 4:33 PM
 */
public class ObjectPool {
	private static final int SIZE = 300;

//	private int maxQueries;
//	private int maxPairs;

	private static ObjectPool instance;

	private ObjectPool() {}

	private Queue<Pair<Node, Integer>> pairs;

	private Queue<Query> queries;

	private Queue<Set<Character>> characters;

	private Queue<Queue<Node>> nodeLists;

	public static ObjectPool getInstance() {
		if (instance == null) {
			instance = new ObjectPool();
		}
		return instance;
	}

	public Pair acquirePair(Node node, int k) {
		if (pairs == null) {
			pairs = new LinkedList();
		}

		if (pairs.isEmpty()) {
			for (int i = 0; i < SIZE; i++) {
				pairs.add(new Pair<Node, Integer>());
			}
		}

//		Pair p = new Pair();
		Pair p = pairs.remove();
		p.setFirst(node);
		p.setSecond(k);

		return p;
	}

	public void releasePair(Pair p) {
		p.setFirst(null);
		p.setSecond(null);
		pairs.add(p);
//		this.maxPairs = Math.max(maxPairs, pairs.size());
	}

	public void destroyPairs() {
		this.pairs.clear();
		this.pairs = null;
	}

	public Query acquireQuery(Node node, int start, int k) {
		if (queries == null) {
			queries = new LinkedList<>();
		}

		if (queries.isEmpty()) {
			for (int i = 0; i < SIZE; i++) {
				queries.add(new Query());
			}
		}

//		Query q = new Query();
		Query q = queries.remove();
		q.setNode(node);
		q.setStart(start);
		q.setK(k);
		return q;
	}

	public void releaseQuery(Query q) {
		q.setNode(null);
		q.setStart(-1);
		q.setK(-1);
		queries.add(q);
//		this.maxQueries = Math.max(maxQueries, queries.size());
	}

	public void destroyQueries() {
		this.queries.clear();
		this.queries = null;
	}

//	public int getMaxQueries() {
//		return maxQueries;
//	}
//
//	public int getMaxPairs() {
//		return maxPairs;
//	}

	public Set<Character> acquireCharacterSet() {
		if (characters == null) {
			characters = new LinkedList<>();
		}

		if (characters.isEmpty()) {
			characters.add(new HashSet<Character>());
		}

		return characters.remove();
	}

	public void releaseCharacterSet(Set<Character> set) {
		set.clear();
		characters.add(set);
	}

	public Queue<Node> acquireNodeList() {
		if (nodeLists == null) {
			nodeLists = new LinkedList<>();
		}

		if (nodeLists.isEmpty()) {
			nodeLists.add(new LinkedList<Node>());
		}

		return nodeLists.remove();
	}

	public void releaseNodeList(Queue<Node> list) {
		list.clear();
		nodeLists.add(list);
	}
}
