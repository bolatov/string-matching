package de.saarland;

public class Main {

    public static void main(String[] args) {
        // number of wildcards to support
        int k = 2;

        // write your code here
        System.out.println("Hello world");

        Trie trie = new Trie();
        trie.add("far", 1);
        trie.add("fat", 2);
        trie.add("fit", 3);
        trie.add("pay", 4);
        trie.add("pin", 5);
        trie.add("sit", 6);

        String pattern = "fit";
        int id[] = trie.lookUp(pattern);
        if (id.length < 1) {
            System.out.println(String.format("Pattern '%s' is not found in the dictionary.%n", pattern));
        } else {
            System.out.println(String.format("Pattern '%s' is found", pattern));
        }
        trie.decompose(trie.getRoot());
        trie.addWildcardSupport(trie.getRoot(), k);
        System.out.println("Finished");
    }
}
