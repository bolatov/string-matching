package de.saarland.hamming;

import de.saarland.util.DataReader;
import junit.framework.TestCase;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * @author Almer Bolatov
 *         Date: 11/26/13
 *         Time: 9:20 PM
 */
public class TestHammingAdvanced extends TestCase {
	public void testSmall() {
		System.out.println("START");

		int k = 4;
		File dataFile = new File("res/hamming/simple_testdata.txt");
		List<String> strings = DataReader.readDataAsStrings(dataFile);

		Trie t = new Trie(strings);
		t.buildMismatchesIndex(k);

		File queriesFile = new File("res/hamming/simple_testqueries.txt");
		List<DataReader.Query> queries = DataReader.readQueries(queriesFile);

		File answersFile = new File("res/hamming/simple_testanswers.txt");
		List<DataReader.Answer> answers = DataReader.readAnswers(answersFile);

		for (DataReader.Query q : queries) {
			int qid = q.id;


			String pattern = q.pattern;
			int localK = q.k;

			Set<Integer> myAnswers = t.search(pattern, localK);
			DataReader.Answer answer = answers.get(qid-1);
			if (answer.queryId != qid) {
				System.err.println("Mismatch!!!");
			}
			Set<Integer> correctAnswers = answer.positions;

			assertEquals(myAnswers.size(), correctAnswers.size());
			for (int myAnswer : myAnswers) {
				assertTrue(correctAnswers.contains(myAnswer+1));
			}
			System.out.printf("Query %d PASSED\n", qid);
		}

		System.out.println("FINISH");
	}
}
