package de.saarland.hamming;

import java.util.Set;

/**
 * @author Almer Bolatov
 *         Date: 11/22/13
 *         Time: 3:37 PM
 */
public interface Searchable {
	public Set<Integer> search(char[] q, int start, int k);
}
