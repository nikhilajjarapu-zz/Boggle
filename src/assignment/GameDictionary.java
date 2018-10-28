package assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class GameDictionary implements BoggleDictionary
{

	// INSTANCE VARIABLES
	TrieNode root;
	ArrayList<String> words;

	// CONSTRUCTOR
	public GameDictionary()
	{
		root = new TrieNode('-');
		words = new ArrayList<String>();
	}

	// METHODS
	public void loadDictionary(String filename) throws IOException
	{
		File file = new File(filename);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String temporaryReader;

		// iterate through file and add commands to arraylist
		while ((temporaryReader = bufferedReader.readLine()) != null)
		{
			addWord(temporaryReader);
			words.add(temporaryReader);
		}
		bufferedReader.close();
	}

	@Override
	public boolean isPrefix(String prefix)
	{
		char[] chars = prefix.toCharArray();
		TrieNode tempNode = root;
		for (int i = 0; i < chars.length; i++)
		{
			char tempChar = chars[i];
			if (!tempNode.children.containsKey(tempChar))
			{
				return false;
			}
			tempNode = tempNode.children.get(tempChar);
		}
		return true;

	}

	@Override
	public boolean contains(String word)
	{
		char[] chars = word.toCharArray();
		TrieNode tempNode = root;
		for (int i = 0; i < chars.length; i++)
		{
			char tempChar = chars[i];
			if (!tempNode.children.containsKey(tempChar))
			{
				return false;
			}
			tempNode = tempNode.children.get(tempChar);
		}
		return tempNode.isCompleteWord();
	}

	@Override
	public Iterator<String> iterator()
	{
		return words.iterator();
	}

	private void addWord(String word)
	{
		char[] chars = word.toCharArray();
		TrieNode tempNode = root;
		for (int i = 0; i < chars.length; i++)
		{
			char tempChar = chars[i];
			if (!tempNode.children.containsKey(tempChar))
			{
				tempNode.addNode(tempChar);
			}
			tempNode = tempNode.children.get(tempChar);
		}
		tempNode.setCompleteWord(true);
	}

	public void printTree(TrieNode rootNode)
	{

		System.out.println(rootNode.getValue());

		for (Map.Entry<Character, TrieNode> entry : rootNode.children.entrySet())
		{
			printTree(entry.getValue());
		}

	}

}
