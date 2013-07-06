package de.saarland;

public class Main {

    public static void main(String[] args) {
        // write your code here
        System.out.println("Hello world");

        Trie trie = new Trie();
        trie.add("aba", 0);
        trie.add("bba", 1);

        String pattern = "aba";
        int id[] = trie.lookUp(pattern);
        if (id.length < 1) {
            System.out.println(String.format("Pattern '%s' is not found in the dictionary.%n", pattern));
        } else {
            System.out.println(String.format("Pattern '%s' is found", pattern));
        }
    }
}
