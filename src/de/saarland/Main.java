package de.saarland;

import java.util.Set;

public class Main {

    public static void main(String[] args) {
        // number of wildcards to support
        int k = 3;

        // write your code here
        System.out.println("Hello world");

        Trie trie = new Trie();
        trie.addWord("far", 1);
        trie.addWord("fat", 2);
        trie.addWord("fit", 3);
        trie.addWord("pay", 4);
        trie.addWord("pin", 5);
        trie.addWord("sit", 6);

        trie.addWildcardSupport(k);

        query("pin", trie);
        query("p*n", trie);
        query("*it", trie);
        query("fa*", trie);
        query("f*r", trie);
        query("f*t", trie);
        query("f**", trie);
        query("**t", trie);
        query("***", trie);

        System.out.println("Finished");

    }

    public static void query(String pattern, Trie trie) {
        Set<Integer> results = trie.lookUp(pattern);
        if (results.isEmpty()) {
            System.out.println(String.format("FAIL. Pattern '%s' NOT found", pattern));
        } else {
            String locations = "(";
            for (Integer i : results) {
                locations += i + " ";
            }
            locations += ")";
            System.out.println(String.format("SUCCESS. Pattern '%s' found at %s", pattern, locations));
        }
    }
}
