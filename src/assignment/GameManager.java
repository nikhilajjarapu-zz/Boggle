package assignment;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameManager implements BoggleGame {

	// INSTANCE VARIABLES
	char[][] board;
	int size;
	int numPlayers;
	String cubeFile;
	BoggleDictionary dictionary;
	SearchTactic tactic;
	ArrayList<ArrayList<String>> players;
	int[] playerScores;
	ArrayList<Point> lastWordPoints;
	
	// CONSTRUCTORS
	public GameManager(int size, int numPlayers, String cubeFile, BoggleDictionary dictionary) throws IOException {
		newGame(size, numPlayers, cubeFile, dictionary);
	}

	public GameManager() throws IOException {
		newGame(4, 1, "../../cubes.txt", new GameDictionary());
	}

	public GameManager(int numPlayers) throws IOException {
		newGame(4, numPlayers, "../../cubes.txt", new GameDictionary());
	}

	// METHODS
	@Override
	public void newGame(int size, int numPlayers, String cubeFile, BoggleDictionary dict) throws IOException {
		this.size = size;
		this.numPlayers = numPlayers;
		this.cubeFile = cubeFile;
		this.dictionary = dict;
		board = new char[size][size];
		tactic = SearchTactic.SEARCH_BOARD;
		scrambleBoard();
		players = new ArrayList<ArrayList<String>>();

		for (int i = 0; i < numPlayers; i++) {
			players.add(new ArrayList<String>());
		}

		playerScores = new int[numPlayers];
		lastWordPoints = new ArrayList<Point>();
	}

	@Override
	public char[][] getBoard() {
		return board;
	}

	@Override
	public int addWord(String word, int player) {
		int score = word.length() - 3;
		playerScores[player] += word.length() - 3;
		players.get(player).add(word);
		return score;
	}

	@Override
	public List<Point> getLastAddedWord() {
		return lastWordPoints;
	}

	@Override
	public void setGame(char[][] board) {
		this.board = board;
	}

	@Override
	public Collection<String> getAllWords() {
		WordSearch wordSearch = new WordSearch(board, dictionary);
		if (tactic.equals(SearchTactic.SEARCH_BOARD)) {
			return wordSearch.getPossibleWordsTrie();
		} else {
			return wordSearch.getPossibleWordsDictionary();
		}
	}

	@Override
	public void setSearchTactic(SearchTactic tactic) {
		this.tactic = tactic;
	}

	@Override
	public int[] getScores() {
		return playerScores;
	}

	public void scrambleBoard() throws IOException {

		// read cube list
		ArrayList<String> cubeList = new ArrayList<String>();
		File file = new File("cubes.txt");
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String temporaryReader;
		Random rand = new Random();

		// iterate through file and add commands to arraylist
		while ((temporaryReader = bufferedReader.readLine()) != null) {
			cubeList.add(temporaryReader);
		}

		for (int i = 0; i < cubeList.size(); i++) {
			int randIndex = rand.nextInt(cubeList.get(i).length());
			char randomChar = cubeList.get(i).charAt(randIndex);

			int randomRow = rand.nextInt(board.length);
			int randomCol = rand.nextInt(board[0].length);

			while (board[randomRow][randomCol] != 0) {
				randomRow = rand.nextInt(board.length);
				randomCol = rand.nextInt(board[0].length);
			}

			board[randomRow][randomCol] = randomChar;
		}
		bufferedReader.close();

	}



}