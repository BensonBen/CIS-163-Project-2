package package1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**********************************************************************
 * Class that creates and handles the creation and managment of the
 * various GUI elements of the surround game. All logic is handled
 * within the SurroundGame Class. Only actions related to the visual
 * elements of the game are contained within this class
 * This class creates various arrays of GUI elements and updates
 * those elements based on user input and conditions within the 
 * SurroundGame class
 * 
 * CIS 163
 * @author Jesse Roe & Ben Benson
 * @version 2/11/2015
 *********************************************************************/
@SuppressWarnings("serial")
public class SurroundPanel extends JPanel {

	/** JButton array to initialize the board **/
	private JButton[][] board;

	/** Instance of the Surround game does most of background work **/
	private SurroundGame game;

	/** JMenuItem to start a new Game if the user desires **/
	private JMenuItem newGameItem;

	/** JMenuItem to exit out of the Game itself **/
	private JMenuItem quitItem;

	/** JMenuBar for the drop down resources **/
	private JMenuBar menuBar;

	/** The Menu Item to add to NewGame, and Quit item into the GUI **/
	private JMenu menu;

	/** Text Area to say which Players Turn it is **/
	private JTextArea playersTurn;

	/** Instance Variable used to set the dimensions of the Board **/
	private Dimension dim;

	/**
	 * Instance Variable to add functionality to any 
	 * actionable JComponent
	 **/
	private ButtonListener listener;

	/** Instance of the panel used to zone the GUI **/
	private JPanel center, top, bottom, southTextArea, east;

	/** An array of image icons **/
	private ImageIcon[] icons;

	/**
	 * Instantiated to set up the board size, and the Array of JButtons
	 **/
	private int selectedSize;

	/** Sets the number of selectedPlayers within the game **/
	private int selectedPlayers;

	/** Int to set the first turn of the player **/
	private int firstTurn;

	/** An array of text areas to designate each players wins **/
	private JTextArea winnerCount[];

	/**
	 * Array of Cells to keep track of each players wins implemented 
	 * within the game class because instantiating a game would 
	 * de-reference the players current wins.
	 **/
	private Cell[] winsTracker;

	/******************************************************************
	 * Constructor for the Surround Panel Inherits the attributes of a
	 * JPanel. Enables the user to interact with the SurroundGame.
	 * 
	 * @param quitItem
	 *            a JMenuItem that's instantiated and added 
	 *            to the panel.
	 * @param newGameItem
	 *            newGameItem that is later instantiated and 
	 *            added to the panel.
	 * @throws MalformedURLexception
	 *             Throws a malformed exception if the addIcons method 
	 *             is unable to find the URLs of the Icons used
	 *             in the game.
	 *****************************************************************/
	public SurroundPanel(JMenuItem quitItem, JMenuItem newGameItem) {
		try {
			this.addIcons();
		} catch (MalformedURLException exception) {
			exception.printStackTrace();
		}

		this.quitItem = quitItem;
		this.newGameItem = newGameItem;

		startUpConditions();
		// Sets up the game based on the startUpConditons.
		game = new SurroundGame(selectedSize, 
				selectedPlayers, firstTurn);

		playersTurn = new JTextArea(1, 10);
		// Initializes the players current turn
		// to indicate who's going first
		playersTurn.setEditable(false);
		playersTurn.setText(game.toString());

		menuBar = new JMenuBar();
		menu = new JMenu("Options");

		playersTurn.setFont(new Font("Serif", Font.BOLD, 24));

		// instantiates the Text Areas
		southTextArea = new JPanel();
		center = new JPanel();
		top = new JPanel();
		bottom = new JPanel();

		setLayout(new BorderLayout());

		listener = new ButtonListener();

		center.setLayout(new GridLayout(selectedSize, 
				selectedSize, 3, 2));
		southTextArea.setLayout(new GridLayout(1, 2, 50, 0));

		// sets the dimensions of the Buttons
		// Later used within the add buttons class.
		dim = new Dimension(30, 30);

		// Sets up the Buttons, and Sets the
		// current player wins to 0 with the setcell method
		Buttons();
		setCells();

		quitItem.addActionListener(listener);
		newGameItem.addActionListener(listener);

		// Add the JComponents their corresponding JPanels
		menu.add(quitItem);
		menu.add(newGameItem);
		menuBar.add(menu);

		top.add(menuBar);
		bottom.add(playersTurn);

		southTextArea.add(playersTurn);

		JLabel title = new JLabel("Surround");
		top.add(title);

		center.setBackground(Color.LIGHT_GRAY);
		add(setWinPanel(), BorderLayout.EAST);
		add(center, BorderLayout.CENTER);
		add(top, BorderLayout.NORTH);
		add(bottom, BorderLayout.SOUTH);
		add(southTextArea, BorderLayout.SOUTH);
		setPreferredSize(new Dimension(1000, 800));
	}

	/******************************************************************
	 * Method that initializes the array of buttons based on the 
	 * players desired selected size. Also, adds an action listener
	 * to enable functionality within the GUI.
	 *****************************************************************/
	private void Buttons() {
		board = new JButton[selectedSize][selectedSize];
		for (int row = 0; row < selectedSize; row++) {
			for (int col = 0; col < selectedSize; col++) {
				board[row][col] = new JButton("");
				board[row][col].setPreferredSize(dim);

				board[row][col].addActionListener(listener);
				center.add(board[row][col]);
			}
		}
	}

	/******************************************************************
	 * CreateBoard is a method can will re-initialize the 
	 * JButton board. Based on the start up conditions passed into 
	 * the GUI. Removes the JButtons from the center panel. Re-calls
	 * the startUpConditions to the desired players, board size, 
	 * and first turn.
	 *****************************************************************/
	private void createBoard() {
		center.removeAll();
		startUpConditions();
		game = new SurroundGame(selectedSize, 
				selectedPlayers, firstTurn);
		center.setLayout(new GridLayout(selectedSize, 
				selectedSize, 3, 2));
		Buttons();
		center.revalidate();
		center.repaint();

	}

	/******************************************************************
	 * Initializes the array of images based on the URL passed into the
	 * ImageIcon constructor. Each URL is specific, and a for loop 
	 * could not be used to initialize the array of PNG images.
	 * 
	 * @throws MalformedURLException
	 *             If there is a URL that the method cannot initialize
	 *             using media tracker. The method will throw an 
	 *             exception which is caught in the JPanel 
	 *             SurroundPanel constructor.
	 *****************************************************************/
	private void addIcons() throws MalformedURLException {
		// all the icons are hosted on my public profile
		// on imageShack.com where I posted the public we needed
		// and generated links specific to the images needed
		// So any person can use the game even though they may
		// not have the icons on their computer.
		URL url1 = new URL("http://imageshack.com/a"
				+ "/img661/3996/WvLYyY.png");
		URL url2 = new URL("http://imageshack.com/a"
				+ "/img538/3466/GN136H.png");
		URL url3 = new URL("http://imageshack.com/a"
				+ "/img537/6084/c4YH04.png");
		URL url4 = new URL("http://imageshack.com/a"
				+ "/img538/438/LE45HQ.png");
		URL url5 = new URL("http://imageshack.com/a"
				+ "/img905/5369/EF7ClE.png");
		URL url6 = new URL("http://imageshack.com/a"
				+ "/img537/1483/90oh1M.png");
		URL url7 = new URL("http://imageshack.com/a"
				+ "/img537/7053/MQHWpW.png");
		URL url8 = new URL("http://imageshack.com/a"
				+ "/img540/2675/kwg3F4.png");
		URL url9 = new URL("http://imageshack.com/a"
				+ "/img673/2/bCuGcb.png");
		URL url10 = new URL("http://imageshack.com/a"
				+ "/img633/3541/UzTbhW.png");

		icons = new ImageIcon[11];
		icons[0] = new ImageIcon(url1);
		icons[1] = new ImageIcon(url2);
		icons[2] = new ImageIcon(url3);
		icons[3] = new ImageIcon(url4);
		icons[4] = new ImageIcon(url5);
		icons[5] = new ImageIcon(url6);
		icons[6] = new ImageIcon(url7);
		icons[7] = new ImageIcon(url8);
		icons[8] = new ImageIcon(url9);
		icons[9] = new ImageIcon(url10);
	}

	/******************************************************************
	 * Sets the cells initialize values to zero since each player has 
	 * not wins	 * to begin with. winsTracker is the array of cells.
	 *****************************************************************/
	private void setCells() {
		winsTracker = new Cell[10];
		for (int i = 0; i < 10; i++) {
			winsTracker[i] = new Cell(0);
		}
	}

	/******************************************************************
	 * The winnerCount JTextArea is initialized here to a 
	 * default of 10. Using the GridLayout in the Swing class of 
	 * Java east has a grid Layout. Each TextArea is added to the 
	 * east panel. Then returned to the constructor.
	 * 
	 * @return returns the JPanel east which is added to the east 
	 * 				side of the GUI using the borderLayout API.
	 *****************************************************************/
	private JPanel setWinPanel() {

		east = new JPanel();
		east.setLayout(new GridLayout(10, 1, 5, 5));

		winnerCount = new JTextArea[10];

		for (int i = 0; i < 10; i++) {
			winnerCount[i] = new JTextArea(1, 10);
			// prevents the user from manipulating
			// the wins a person has.
			winnerCount[i].setEditable(false);
			east.add(winnerCount[i]);
		}
		east.setBackground(Color.LIGHT_GRAY);
		return east;
	}

	/******************************************************************
	 * Method to set the text of a JText area using the player who 
	 * has won the current Surround Game.
	 * 
	 * @param Player
	 *            Passed into the method to determine who has won 
	 *            based on the integer returned by the isWinner 
	 *            method in the SurroundGame class.
	 * @return returns a String that will be passed to the 
	 * 			  corresponding array of Text Areas. Sets the text 
	 * 			  of that area to a String representing that
	 * 			  player's wins.
	 *****************************************************************/
	public String setWins(int Player) {
		// the index of the array is the player
		// for instance player 1 is the index 0.
		int winsUpdater = winsTracker[Player].getPlayer();
		winsUpdater++;

		for (int i = 0; i < winsTracker.length; i++) {
			if (i == Player) {

				winsTracker[Player].setPlayer(winsUpdater);
				Player++;
				return "Player " + Player + "'s wins " + winsUpdater;
			}

		}
		return "null";
	}

	/******************************************************************
	 * Displays the Current state of the game, using methods within 
	 * the SurroundGame. Sets the danger level a certain cell faces, 
	 * and the icon corresponding to that players index. 
	 * Also determines if a cell is occupied by another 
	 * players cell within the game.
	 *****************************************************************/
	private void displayBoard() {
		
		Cell[][] tempBoard = game.getBoard();

		for (int r = 0; r < selectedSize; r++)
			for (int c = 0; c < selectedSize; c++)
				for (int i = 0; i < game.numberOfPlayers(); i++) {
					if (tempBoard[r][c].getPlayer() == i) {

						board[r][c]
								.setIcon(getIcon(tempBoard[r][c]
										.getPlayer()));
					}

					if (game.inTrouble(r, c) != null) {
						board[r][c].setBackground(game.
								inTrouble(r, c));
					}
				}
	}

	/******************************************************************
	 * Method used to retrieve a players Icon from the array of icons 
	 * within the GUI.
	 * 
	 * @param Player
	 *            parameter passed is the players index within the 
	 *            ArrayList of players currently in the game.
	 * @return returns an "Icon" from the array of "icons" if 
	 * 			  the players index matches the array index. 
	 * 			  Otherwise it will return null if that
	 *         	  player is not found.
	 *****************************************************************/
	private Icon getIcon(int Player) {
		for (int i = 0; i < icons.length; i++) {
			if (Player == i) {
				return icons[i];
			}
		}
		return null;
	}
	
	/******************************************************************
	 * Sets the start up conditions for the Game. Retrieves the values 
	 * for the "selected Size", "playerSize", and "firstTurn". 
	 * If the users do not appropriately set the start up 
	 * conditions of the game. The boardSize, number of players, 
	 * and first player will default.
	 * 
	 * This methodology was used to present the user from spamming
	 * enter, and causing a program crash in the real world.
	 * 
	 * @throws NumberFormatException
	 *             Will throw a number format exception if the parsed
	 *             String contains anything other than Whole Numbers.
	 *****************************************************************/
	private void startUpConditions() {
		String strSize = JOptionPane.showInputDialog(null,
				"Pick a Board Size between 3-30.");
		try {
			selectedSize = Integer.parseInt(strSize);

			if (selectedSize < 2 || selectedSize > 30) {
				selectedSize = 10;
				JOptionPane.showMessageDialog(null, "The Game has"
						+ " defaulted to a Board Size of 10.");
			}

		} catch (NumberFormatException exception) {

			JOptionPane.showMessageDialog(null, "The Game has"
					+ " defaulted to a Board Size of 10."
					+ " Because you input characters "
					+ "other than numbers.");
			selectedSize = 10;
		}

		// selecting the number of players within the game
		// verifying a valid input for the Players
		// It's awfully boring to play by yourself after all.
		String strPlay = JOptionPane.showInputDialog(null,
				"Pick number of players between 2-10");
		try {
			if (strPlay == null) {
				JOptionPane.showMessageDialog(null, "The game"
						+ " has defaulted to 2 players.");
				selectedPlayers = 2;
			}

			if (strPlay != null) {
				selectedPlayers = Integer.parseInt(strPlay);

				if (selectedPlayers < 2 || selectedPlayers > 10) {
					selectedPlayers = 2;
					JOptionPane.showMessageDialog(null, "The game"
							+ " has defaulted to 2 players."
							+ " Because you input a number that "
							+ "was out of bounds.");
				}
			}
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(null, "You input "
					+ "something that was not a number."
					+ " The game has defaulted to 2 players.");
			selectedPlayers = 2;
		}

		String goFirst = JOptionPane.showInputDialog(null, "Pick"
				+ " who will go first.");

		try {
			if (goFirst == null) {
				JOptionPane.showMessageDialog(null, "The game"
						+ " has defaulted to Player 1s turn.");
				this.firstTurn = 0;
			}
			if (goFirst != null) {
				this.firstTurn = Integer.parseInt(goFirst);
			}
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(null, "You input "
					+ "something other than a number."
					+ " The Game has defaulted to player 1s turn.");
			this.firstTurn = 0;
		} finally {
			JOptionPane.showMessageDialog(null, "The game" + 
							" will now begin.");
		}
	}

	/******************************************************************
	 * Implements the abstract class ActionListener. This method 
	 * handles all of the Listener events and resulting in the 
	 * corresponding actions according to what the user has pressed.
	 *****************************************************************/
	private class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			
			// determines the JCompent that was pressed.
			JComponent fired = (JComponent) e.getSource();

			if (fired == quitItem) {
				
				// Checks if the user really intended to quit
				final int quit = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to quit?", null,
						JOptionPane.YES_NO_OPTION);
				if (quit == JOptionPane.OK_OPTION) {
					System.exit(0);
				} else if (quit == JOptionPane.CANCEL_OPTION) {
					// keep going with the game.
				}
			}

			if (fired == newGameItem) {
				createBoard();
			}

			for (int r = 0; r < selectedSize; r++)
				for (int c = 0; c < selectedSize; c++)
					if (board[r][c] == fired) {
						if (game.select(r, c)) {
							displayBoard();
							game.isWinner();
							game.nextPlayer();
							// sets the current players
							playersTurn.setText(game.toString());

						}
					}
			if (game.getGameStatus() == GameStatus.CATS) {
				JOptionPane.showMessageDialog(null, "The game is a "
						+ "draw!" + "\n The game will now reset");
				createBoard();

			}

			if (game.getGameStatus() == GameStatus.WINNER) {
				int temp = game.getPlayer() + 1;
				JOptionPane.showMessageDialog(null, "Player" + temp
						+ " has won the game!" 
						+ "\n The game will now reset");

				int winner = game.getPlayer();
				if (winner >= 0) {
					winnerCount[winner].setText(setWins(winner));
				}
				createBoard();
			}

			if (game.getGameStatus() == GameStatus.REMOVED) {
				int temp = game.getDecease().get(0) + 1;
				JOptionPane.showMessageDialog(null, "Player" + temp 
						+ "was surrounded, and will be " + 
						"removed from game"
						+ "\n The game will continue");
			}
		}
	}
}
