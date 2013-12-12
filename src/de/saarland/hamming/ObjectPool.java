package de.saarland.hamming;

import de.saarland.util.Pair;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Almer Bolatov
 *         Date: 12/12/13
 *         Time: 4:33 PM
 */
public class ObjectPool {
	private static int SIZE = 300;

	private static ObjectPool instance;

	private ObjectPool() {}

	private Queue<Pair<Node, Integer>> pairs;

	private Queue<Query> queries;

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
	}

	public void destroyQueries() {
		this.queries.clear();
		this.queries = null;
	}
}
