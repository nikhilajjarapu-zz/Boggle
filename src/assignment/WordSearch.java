package assignment;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WordSearch
{
	// initialize private variables
	char[][] board;
	BoggleDictionary dictionary;
	boolean[][] visited;
	ArrayList<String> words;

	// constructor
	public WordSearch(char[][] board, BoggleDictionary dictionary)
	{
		Iterator<String> i = dictionary.iterator();
		this.board = board;
		this.dictionary = dictionary;
		words = new ArrayList<>();
		visited = new boolean[board.length][board.length];
	}

	// implements board-driven search: uses the efficiency of the trie data
	// structure and recurses through the board to check for all words
	public ArrayList<String> getPossibleWordsTrie()
	{

		words = new ArrayList<>();

		// calls recursive function on every tile
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				searchWords(i, j, "");
			}
		}

		// removes duplicates that appear due to the multiple ways possible to create
		// the word
		words = new ArrayList<String>(new HashSet<String>(words));
		Collections.sort(words);
		return words;

	}

	public void searchWords(int i, int j, String word)
	{

		// check bounds
		if (i < 0 || i > board.length - 1)
		{
			return;
		}
		if (j < 0 || j > board.length - 1)
		{
			return;
		}
		if (visited[i][j])
		{
			return;
		}

		// append character to word
		word += board[i][j];
		if (!dictionary.isPrefix(word.toLowerCase()))
		{
			return;
		}
		if (word.length() >= 4 && dictionary.contains(word.toLowerCase()))
		{
			if (!words.contains(word))
			{
				words.add(word.toLowerCase());
			}
		}

		visited[i][j] = true;
		// recursive step
		searchWords(i, j - 1, word);
		searchWords(i, j + 1, word);
		searchWords(i + 1, j, word);
		searchWords(i - 1, j, word);
		searchWords(i + 1, j + 1, word);
		searchWords(i - 1, j + 1, word);
		searchWords(i + 1, j - 1, word);
		searchWords(i - 1, j - 1, word);

		visited[i][j] = false;

	}

	// implements dictionary-driven search: iterates through all the words and
	// checks if they exist on board through recursion
	public ArrayList<String> getPossibleWordsDictionary()
	{
		words = new ArrayList<String>();

		// generates list of characters on board
		Set<Character> chars = new HashSet<Character>();
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board.length; j++)
			{
				chars.add(board[i][j]);
			}
		}
		Iterator<String> i = dictionary.iterator();
		while (i.hasNext())
		{
			String word = i.next();
			// eliminates all words shorter than 4 characters or with starting letters not
			// present on board and calls recursive step
			if (word.length() >= 4 && chars.contains(word.charAt(0)))
			{
				checkWords(word.toUpperCase().toCharArray(), 0, new Point(-1, -1),
						new boolean[board.length][board.length]);
			}
		}

		words = new ArrayList<String>(new HashSet<String>(words));
		Collections.sort(words);
		return words;
	}

	public void checkWords(char[] wordChars, int k, Point indexOfLastChar, boolean[][] visited)
	{
		// finds all possible starting locations
		if (k == 0)
		{
			for (int i = 0; i < board.length; i++)
			{
				for (int j = 0; j < board[i].length; j++)
				{
					if (board[i][j] == wordChars[k])
					{
						visited[i][j] = true;
						checkWords(wordChars, k + 1, new Point(i, j), visited);
						visited[i][j] = false;

					}
				}
			}
		}

		// adds word to list of words if recursive step reaches end of word
		if (k == wordChars.length)
		{
			words.add(new String(wordChars).toLowerCase());

		} else
		{
			// iterates through all neighbors
			int[] dx =
			{ 1, 1, 0, -1, -1, -1, 0, 1 };
			int[] dy =
			{ 0, 1, 1, 1, 0, -1, -1, -1 };
			int x;
			int y;
			for (int i = 0; i < 8; i++)
			{
				x = indexOfLastChar.x + dx[i];
				y = indexOfLastChar.y + dy[i];
				if ((x >= 0) && (x < board.length) && (y >= 0) && (y < board.length) && (!visited[x][y])
						&& board[x][y] == wordChars[k])
				{
					visited[x][y] = true;
					checkWords(wordChars, k + 1, new Point(x, y), visited);
					visited[x][y] = false;
				}

			}
		}
		return;
	}
}
