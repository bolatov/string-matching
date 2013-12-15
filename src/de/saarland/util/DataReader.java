package de.saarland.util;

import de.saarland.hamming.BruteForce;

import java.io.*;
import java.util.*;

/**
 * @author Almer Bolatov
 *         Date: 11/26/13
 *         Time: 5:12 PM
 */
public class DataReader {

	public static void main(String[] args) throws IOException {
//		File dataFile = new File("res/hamming/geonames_k4_testdata.csv");
//		List<Data> data = readDataAsData(dataFile);
//		for (int i = 0; i < 10; i++) {
//			Data d = data.get(i);
//			System.out.printf("%d,%s\n",d.id,d.data);
//		}

//		File answersFile = new File("res/hamming/geonames_k4_testanswers.csv");
//		List<Answer> answers = readAnswers(answersFile);
//		for (int i = 0; i < 10; i++) {
//			Answer a = answers.get(i);
//			System.out.printf("%d:", a.queryId);
//			Iterator<Integer> iterator = a.positions.iterator();
//			if (!iterator.hasNext()) {
//				System.out.print("NO");
//			}
//			while (iterator.hasNext()) {
//				System.out.printf("%d", iterator.next());
//				if (iterator.hasNext())
//					System.out.printf(",");
//			}
//			System.out.println();
//		}

		prepareAnswers();
	}

	public static List<Answer> readAnswers(File file) {
		List<Answer> answers = new ArrayList<>();

		BufferedReader reader = null;
		String line;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {
				String[] split = line.split(":");
				int queryId = Integer.parseInt(split[0]);
				split = split[1].split(",");
				Set<Integer> positions = new HashSet<>();
				for (String pos : split) {
					if (!"NO".equals(pos))
						positions.add(Integer.parseInt(pos));
				}
				answers.add(new Answer(queryId, positions));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return answers;

	}

	public static void prepareAnswers() throws IOException {
        int kValue = 4;
		File dataFile = new File(String.format("res/hamming/geonames_k%d_testdata.csv", kValue));
		List<String> data = readDataAsStrings(dataFile);
//		for (int i = 0; i < data.size(); i++) {
//			System.out.println(i + ": " + data.get(i));
//		}

		File queriesFile = new File(String.format("res/hamming/geonames_k%d_testqueries.csv", kValue));
		List<Query> queries = readQueries(queriesFile);

		File answersFile = new File(String.format("res/hamming/geonames_k%d_testanswers.csv", kValue));
		FileWriter fw = new FileWriter(answersFile, true);
		BufferedWriter bw = new BufferedWriter(fw);

		int total = queries.size();
		for (int i = 0; i < queries.size(); i++) {

			if (i % 100 == 0) {
				System.out.printf("Left: %d\n", total-i-1);
			}

			Query q = queries.get(i);

			int id = q.id;
			String p = q.pattern;
			int k = q.k;

			Set<Integer> set = new HashSet<>();
			for (int j = 0; j < data.size(); j++) {
				String s = data.get(j);
				if (BruteForce.areHammingEqual(p, s, k)) {
					set.add(j + 1);
				}
			}

//			System.out.printf("%d:", id);
			bw.append(String.format("%d:", id));
			if (set.isEmpty()) {
//				System.out.print("NO");
				bw.append("NO");
			} else {
				Iterator<Integer> iterator = set.iterator();
				while (iterator.hasNext()) {
//					System.out.print(iterator.next());
					bw.append(String.valueOf(iterator.next()));

					if (iterator.hasNext()) {
//						System.out.printf(",");
						bw.append(",");
					}
				}
			}
//			System.out.println();
			bw.newLine();
		}
		bw.close();
		fw.close();
		System.out.println("Finished.");
	}

	public static List<String> readDataAsStrings(File file) {
		/**
		 * Read data file line by line.
		 * Ignore line id and start from 0.
		 */

		List<String> strings = new ArrayList<>();

		BufferedReader reader = null;
		String line;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {
				String[] columns = line.split(",");
				Integer id = Integer.parseInt(columns[0]);
				String word = columns[1];
				strings.add(word);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return strings;
	}

	public static List<Data> readDataAsData(File file) {
		/**
		 * Read data file line by line.
		 * Ignore line id and start from 0.
		 */

		List<Data> data = new ArrayList<>();

		BufferedReader reader = null;
		String line;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {
				String[] columns = line.split(",");
				Integer id = Integer.parseInt(columns[0]);
				String word = columns[1];
				Data d = new Data(id, word);
				data.add(d);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return data;

	}

	public static List<Query> readQueries(File file) {
		List<Query> queries = new ArrayList<>();

		BufferedReader reader = null;
		String line;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {
				String[] split = line.split(":");
				int id = Integer.parseInt(split[0]);
				split = split[1].split(",");
				String pattern = split[0];
				int k = Integer.parseInt(split[1]);
				queries.add(new Query(id, pattern, k));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return queries;
	}

	public static class Answer {
		public int queryId;
		public Set<Integer> positions;
		Answer(int id, Set<Integer> p) {
			this.queryId = id;
			this.positions = p;
		}
	}

	public static class Data {
		public int id;
		public String data;

		Data(int id, String d) {
			this.id = id;
			this.data = d;
		}
	}

	public static class Query {
		public int id;
		public String pattern;
		public int k;

		Query(int id, String p, int k) {
			this.id = id;
			this.pattern = p;
			this.k = k;
		}
	}
}
