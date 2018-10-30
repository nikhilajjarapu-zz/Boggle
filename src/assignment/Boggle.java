package assignment;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Boggle {

	GameManager boggle;
	WordSearch wordSearch;
	Scanner scan;
	ArrayList<Point> highlightPoints;

	public Boggle(GameManager boggle) {
		this.boggle = boggle;
		wordSearch = new WordSearch(boggle.board, boggle.dictionary);
		scan = new Scanner(System.in);
		highlightPoints = new ArrayList<Point>();
	}

	public void printBoard(char[][] board) {
		System.out.println();
		for (int i = 0; i < board.length; i++) {
			int lineLength = 0;
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j] + " | ");
				lineLength += (board[i][j] + " | ").length();
			}
			System.out.println();
			if (i < board.length - 1) {
				for (int k = 0; k < lineLength - 1; k++) {
					System.out.print("-");
				}
				System.out.println();
			}
		}
		System.out.println();
	}

	public void highlightWord(String word, char[][] board) {
		ArrayList<Point> startingPoints = getStartingIndices(board, word.charAt(0));
		highlightPoints.clear();
		char[][] tempBoard = new char[board.length][board[0].length];

		// copy board
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				tempBoard[i][j] = board[i][j];
			}
		}
		for (Point p : startingPoints) {
			highlightPoints.add(p);
			getPointsRecursive(board, p.x, p.y, 0, word);
			if (highlightPoints.size() >= word.length()) {
				break;
			}
			else {
				highlightPoints.clear();
			}

		}
		
		// change board based on points
		for (Point a : highlightPoints) {
			tempBoard[a.x][a.y] = Character.toLowerCase(tempBoard[a.x][a.y]);
		}
		
		boggle.lastWordPoints = highlightPoints;
		
		printBoard(tempBoard);

	}

	public ArrayList<Point> getStartingIndices(char[][] board, char c) {
		ArrayList<Point> points = new ArrayList<Point>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if ((board[i][j] + "").equalsIgnoreCase(c + "")) {
					points.add(new Point(i, j));
				}
			}
		}
		return points;
	}

	public void getPointsRecursive(char[][] board, int x, int y, int k, String word) {
		if (k == word.length() - 1) {
			return;
		}
		ArrayList<Point> neighbors = checkNeighbors(board, x, y, word.charAt(k + 1));
		if (neighbors != null) {
			for (Point p : neighbors) {
				highlightPoints.add(p);
				getPointsRecursive(board, p.x, p.y, k + 1, word);
			}
		} else {
			highlightPoints.remove(highlightPoints.size() - 1);
			return;
		}
	}

	public ArrayList<Point> checkNeighbors(char[][] board, int x, int y, char letter) {
		ArrayList<Point> points = new ArrayList<Point>();
		int[] dx = { 1, 1, 0, -1, -1, -1, 0, 1 };
		int[] dy = { 0, 1, 1, 1, 0, -1, -1, -1 };
		int newX;
		int newY;
		for (int i = 0; i < 8; i++) {
			newX = x + dx[i];
			newY = y + dy[i];
			if ((newX >= 0) && (newX < board.length) && (newY >= 0) && (newY < board.length)
					&& (board[newX][newY] + "").equalsIgnoreCase(letter + "")) {
				points.add(new Point(newX, newY));
			}
		}
		if (points.isEmpty()) {
			return null;
		}
		return points;
	}

	public boolean playGame(BoggleGame.SearchTactic tactic) {
		
		ArrayList<String> possibleWords = new ArrayList<String>();
		if (tactic == BoggleGame.SearchTactic.SEARCH_BOARD) {
			possibleWords = wordSearch.getPossibleWordsTrie();
		}
		else {
			possibleWords = wordSearch.getPossibleWordsDictionary();
		}
		
		for (int i = 0; i < boggle.numPlayers; i++) {

			// initial input
			delay(500);
			System.out.println();
			System.out.println("** PLAYER " + (i + 1) + " **");
			delay(500);
			printBoard(boggle.board);
			delay(500);
			System.out.println("When you are done with your turn, enter /over");
			delay(500);
			System.out.println("Enter a word: ");
			String word = scan.nextLine();
			delay(500);

			while (!word.equals("/over")) {
				if (!containsIgnoreCase(possibleWords, word) || word.length() < 4) {
					System.out.println("Not a valid word.");
					delay(300);
					printBoard(boggle.board);
					delay(700);
				} else {
					System.out.println("Valid word!");
					deleteIgnoreCase(possibleWords, word);
					highlightWord(word, boggle.board);
					boggle.addWord(word, i);
					delay(500);
				}

				System.out.print("Scores: | ");
				for (int j = 0; j < boggle.numPlayers; j++) {
					System.out.print("Player " + (j + 1) + ": " + calculateScore(boggle.players.get(j)) + " | ");
				}
				System.out.println();

				delay(500);
				System.out.println("Please enter another word, or type /over: ");
				word = scan.nextLine();
			}
		}

		displayGameOver(possibleWords);
		delay(700);
		System.out.println("Would you like to play again? (Y/N): ");
		String gameChoice = scan.nextLine().toLowerCase();
		while (!((gameChoice.equals("y") || gameChoice.equals("n") || gameChoice.equals("yes")
				|| gameChoice.equals("no")))) {
			System.out.println("Please enter a valid input (Y/N): ");
			gameChoice = scan.nextLine().toLowerCase();
		}

		if (gameChoice.equals("y") || gameChoice.equals("yes")) {
			return true;
		}

		return false;

	}

	public boolean containsIgnoreCase(ArrayList<String> words, String word) {
		for (String w : words) {
			if (w.equalsIgnoreCase(word)) {
				return true;
			}
		}
		return false;
	}

	public void deleteIgnoreCase(ArrayList<String> words, String word) {
		for (int i = 0; i < words.size(); i++) {
			if (words.get(i).equalsIgnoreCase(word)) {
				words.remove(i);
			}
		}
	}

	public int calculateScore(ArrayList<String> possibleWords) {
		int totalScore = 0;
		for (String word : possibleWords) {
			totalScore += word.length() - 3;
		}

		return totalScore;
	}

	public void displayGameOver(ArrayList<String> computerWords) {
		delay(500);
		System.out.println();
		System.out.println("***************");
		System.out.println("** GAME OVER **");
		System.out.println("***************");
		System.out.println();
		delay(500);
		for (int i = 0; i < boggle.numPlayers; i++) {
			System.out.println("Player " + (i + 1) + ": " + calculateScore(boggle.players.get(i)));
		}
		System.out.println("Computer score: " + calculateScore(computerWords));
		System.out.println();
		delay(1000);
		System.out.println("Computer words: ");
		for (int i = 0; i < computerWords.size(); i++) {
			System.out.print(computerWords.get(i) + " ");
			if (i % 4 == 0 && i != 0) {
				System.out.println();
			}
		}
		System.out.println();
		System.out.println();
	}

	public static void delay(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {

		Scanner scan = new Scanner(System.in);
		
		while (true) {
			System.out.println();
			System.out.println("************");
			System.out.println("** BOGGLE **");
			System.out.println("************");
			Boggle.delay(600);
			System.out.println("\nEnter number of players: ");
			int players = scan.nextInt();
			scan.nextLine();
			GameManager game = new GameManager(players);
			game.dictionary.loadDictionary("../../words.txt");
			Boggle ui = new Boggle(game);

			if (!ui.playGame(game.tactic)) {
				scan.close();
				break;
			}

		}

	}

}