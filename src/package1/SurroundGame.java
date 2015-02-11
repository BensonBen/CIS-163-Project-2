package package1;

import java.awt.Color;
import java.util.ArrayList;

/**********************************************************************
 * Class that creates and manages the logic behind the operation of 
 * the Surround game. Creates arrays that hold Cell, player, and
 * removed player values.
 * 
 * CIS 163
 * @author Jesse Roe & Ben Benson
 * @version 2/11/2015
 *********************************************************************/
public class SurroundGame {

	/** 2D Array of Cell objects */
	private Cell[][] board;

	/** Creates GameStatus enum */
	private GameStatus status;

	/** Value that stores the games current player */
	private int currentPlayer;

	/** Value that is used to directly access array list of players */
	private int playerIndex;

	/** Max number of player for this game. Set from GUI */
	private int playersNumber;

	/** Array that holds values for each player in the game */
	private ArrayList<Integer> players;

	/** Array that holds the players who have been defeated */
	private ArrayList<Integer> deceased;

	/** Max size of the game board */
	private int size;

	/******************************************************************
	 * Getter for the board 2d array.
	 * 
	 * @return 2d Cell array board
	 *****************************************************************/
	public Cell[][] getBoard() {
		return board;
	}

	/******************************************************************
	 * Getter for the current status of the game.
	 * 
	 * @return GameStatus status
	 *****************************************************************/
	public GameStatus getGameStatus() {
		return status;
	}

	/******************************************************************
	 * Default constructor for the Surround Game class. Sets default
	 * value of 10 and 2 for size and players *
	 * 
	 * @param none
	 *****************************************************************/
	public SurroundGame() {
		
		playersNumber = 2;
		size = 10;
		currentPlayer = 0;
		board = new Cell[10][10];
		status = GameStatus.IN_PROGRESS;
		playerIndex = 0;
		reset();
	}

	/******************************************************************
	 * Constructor for Surround Game class that will create the board,
	 * number of players, and the first player based on user 
	 * input from GUI.
	 * 
	 * @param int pSize, int pPlayersNumber, int firstTurn
	 *****************************************************************/
	public SurroundGame(int pSize, int pPlayersNumber, int firstTurn) {
		
		playersNumber = pPlayersNumber;
		size = pSize;
		board = new Cell[size][size];
		status = GameStatus.IN_PROGRESS;
		reset();
		playerIndex = firstTurn - 1;

		if (playerIndex < 0 || playerIndex >= players.size()) {
			playerIndex = 0;
		}

		currentPlayer = players.get(playerIndex);
	}

	/******************************************************************
	 * Getter for the current Player
	 * 
	 * @return int currentPlayer
	 *****************************************************************/
	public int getPlayer() {
		return currentPlayer;
	}

	/******************************************************************
	 * Getter for the max number of players
	 * 
	 * @return int playersNumber
	 *****************************************************************/
	public int numberOfPlayers() {
		return playersNumber;
	}

	/******************************************************************
	 * Method that increments the current player based on the array 
	 * of players. Resets current player to 0 if the end of 
	 * the array is reached
	 *****************************************************************/
	public void nextPlayer() {
		
		playerIndex++;

		// Checks if the player index is within bounds of player array
		if (playerIndex >= players.size()) {
			playerIndex = 0;
			currentPlayer = players.get(playerIndex);
		} else {
			currentPlayer = players.get(playerIndex);
		}

	}

	/******************************************************************
	 * Method that determines if a selection made in the GUI is
	 * valid based on the current value of the selected Cell object *
	 * 
	 * @param int row, int col
	 * @return boolean true, boolean false
	 *****************************************************************/
	public Boolean select(int row, int col) {
		
		// Local variable that holds value of current Cell
		Cell currentTurn;
		currentTurn = board[row][col];

		if (currentTurn.getPlayer() == -1) {
			board[row][col].setPlayer(currentPlayer);
			return true;
		}

		else {
			return false;
		}
	}

	/******************************************************************
	 * Checks if a cell is surrounded on all sides by other players. 
	 * Adds functionality of wrapping the 2d array board around a 
	 * sphere through various modulus functions *
	 * 
	 * @param int row, int col, int rowTemp, int colTemp
	 * @return boolean true, boolean false
	 *****************************************************************/
	private Boolean determineType(int row, int col, 
									int rowTemp, int colTemp) {
		
		// Local variables that hold parameter values for cell being
		// checked
		int newRow = rowTemp;
		int newCol = colTemp;

		if (newRow < 0) {
			// function that adjusts for out of bound inputs and wraps
			// these inputs to another area of board
			newRow = (((newRow % size) + size) % size);
		} else if (newRow >= size) {
			newRow = newRow % size;
		}

		// function that adjusts for out of bound inputs and wraps
		// these inputs to another area of board
		if (newCol < 0) {
			newCol = (((newCol % size) + size) % size);
		} else if (newCol >= size) {
			newCol = newCol % size;
		}

		// Local variables that hold Cell objects for evaluation
		Cell temp1 = board[row][col];

		Cell temp2 = board[newRow][newCol];

		if (temp2.getPlayer() != -1 && temp2.getPlayer() 
				!= temp1.getPlayer() && temp1.getPlayer() != -1) {
			return true;
		} else
			return false;

	}

	/******************************************************************
	 * Checks if conditions for a player to lose are met. If a player 
	 * has lost (is completely surrounded), the player is removed or 
	 * game is won by surrounding player Also checks if game is a draw.
	 * returns an int based on GameStatus *
	 * 
	 * @return int -2, int -3, int currentPlayer, int -1
	 *****************************************************************/
	public int isWinner() {

		// Sets GameStatus to default condition
		status = GameStatus.IN_PROGRESS;
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++) {

				// Checks if game is a draw
				if (isCats()) {
					status = GameStatus.CATS;
					return -2;
				}

				// Contains logic to determine if a specific player
				// is surrounded and if the player has already been
				// removed
				if (board[row][col].getPlayer() != -3) {
					if ((determineType(row, col, row - 1, col - 1)
							&& determineType(row, col, row - 1, col)
							&& determineType(row, col, row - 1, 
									col + 1)
							&& determineType(row, col, row, col + 1)
							&& determineType(row, col, row + 1, 
									col + 1)
							&& determineType(row, col, row + 1, col)
							&& determineType(row, col, row + 1, 
									col - 1)
							&& determineType(row, col, row, col - 1)))
							{
						

						if (players.size() > 2) {

							// Removes player if current number of
							// players if greater that 2
							RemovePlayer(board[row][col].getPlayer());
							
							// Resets Cells of removed player so that
							// The surrounded state of their Cells
							// is no longer checked
							resetRemoved(board[row][col]);
							
							// Diagnostic code to print out arrays
							// for player and deceased
							// Currently deactivated
//							System.out.println(players.toString());
//							System.out.println(deceased.toString());

							// Updates GameStatus for trigger in GUI
							status = GameStatus.REMOVED;
							return -3;
						}

						// If game has been won by a single player
						// updates GameStatus for trigger in GUI
						status = GameStatus.WINNER;
						RemovePlayer(board[row][col].getPlayer());
						currentPlayer = players.get(0);
						return currentPlayer;
					}
				}
			}

		// Default if no conditions are met
		return -1;
	}

	/******************************************************************
	 * Getter for the array list of players *
	 * 
	 * @return ArrayList players
	 *****************************************************************/
	public ArrayList<Integer> getPlayers() {
		return players;
	}

	/******************************************************************
	 * Getter for the array list of removed players *
	 * 
	 * @return ArrayList deceased
	 *****************************************************************/
	public ArrayList<Integer> getDecease() {
		return deceased;
	}



	/******************************************************************
	 * Changes color of a cell based on how close the cell is to be 
	 * surrounded.
	 *  
	 * @param int row, int col
	 * @return Color, null
	 *****************************************************************/
	public Color inTrouble(int row, int col) {

		// Local variable to track number of Cells that are currently
		// surrounding a specified Cell
		int trouble = 0;

		if (board[row][col].getPlayer() != -1) {

			if (determineType(row, col, row - 1, col - 1)) {
				trouble++;
			}
			if (determineType(row, col, row - 1, col)) {
				trouble++;
			}
			if (determineType(row, col, row - 1, col + 1)) {
				trouble++;
			}
			if (determineType(row, col, row, col + 1)) {
				trouble++;
			}
			if (determineType(row, col, row + 1, col + 1)) {
				trouble++;
			}
			if (determineType(row, col, row - 1, col)) {
				trouble++;
			}
			if (determineType(row, col, row + 1, col - 1)) {
				trouble++;
			}
			if (determineType(row, col, row, col - 1)) {
				trouble++;
			}

			// Sets color based on value of int trouble
			if (trouble >= 3 && trouble <= 6) {
				return Color.YELLOW;
			}

			if (trouble > 6 && trouble <= 8) {
				return Color.RED;
			}
			if (trouble > 0) {
				return Color.GREEN;
			}

			// returns null if Cell is not surrounded in any way
			else
				return null;
		} else {
			return null;
		}
	}

	/******************************************************************
	 * Removes player from array list of players
	 * 
	 * @param int playerNumber
	 *****************************************************************/
	public void RemovePlayer(int playerNumber) {
		
		// Loops through all values in array list and removes		
		// specified value
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i) == playerNumber) {
				deceased.add(0, players.get(i));
				players.remove(i);
			}

		}
	}

	/******************************************************************
	 * Method that calculates the conditions for a draw game. If all 
	 * Cells are filled with player values game is draw.
	 * @return boolean true, boolean false
	 *****************************************************************/
	private boolean isCats() {
		
		// Local variable that tracks number of filled Cells
		int numberOfUsedCells = 0;

		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++) {
				if (board[row][col].getPlayer() != -1) {
					numberOfUsedCells++;
				}
			}
		
		// Compares local variable to max area of board
		if (numberOfUsedCells == (size * size)) {
			return true;
		} else
			return false;
	}

	/******************************************************************
	 * Resets board array to default empty state and creates the 
	 * array of players based on input from GUI.	 * 
	 *****************************************************************/
	private void reset() {
		// Declares ArrayLists for players and deceased
		players = new ArrayList<Integer>();
		deceased = new ArrayList<Integer>();

		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++)
				board[row][col] = new Cell(-1);
		
		// Sets all entries in players array list to correct integers
		for (int i = 0; i < playersNumber; i++)
			players.add(i, i);		
	}

	/******************************************************************
	 * Resets specific Cells in board array to a integer value that 
	 * represents an removed player.
	 * @param Cell temp 
	 *****************************************************************/
	private void resetRemoved(Cell temp) {
		
		// Local variable for temp Cell
		int playerCur = temp.getPlayer();
		
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++)
				if (board[row][col].getPlayer() == playerCur) {
					board[row][col].setPlayer(-3);
					System.out.println(board[row][col].getPlayer());
				}

	}

	/******************************************************************
	 * ToString method to print the current player of the game.
	 * Used within the GUI
	 * @return String toString
	 *****************************************************************/
	public String toString() {
		
		// Local variable to hold value of player, adjusted to not
		// display 0
		int currentTurn = getPlayer() + 1;
		
		return "It is player " + currentTurn + "'s turn.";
	}	
}
