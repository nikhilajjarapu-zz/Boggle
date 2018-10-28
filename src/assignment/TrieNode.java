package assignment;

import java.util.HashMap;

public class TrieNode {

    private char value;
    HashMap<Character, TrieNode> children;
    private boolean isCompleteWord;

    public TrieNode(char value) {
        this.value = value;
        children = new HashMap<Character, TrieNode>();
        isCompleteWord = false;
    }

    public char getValue()
    {
        return value;
    }
    public void setValue(char c)
    {
        value = c;
    }

    public void addNode(char c)
    {
        children.put(c,new TrieNode(c));
    }

    public boolean isCompleteWord() {
        return isCompleteWord;
    }

    public void setCompleteWord(boolean val) {
        isCompleteWord = val;
    }
}