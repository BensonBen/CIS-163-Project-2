package package1;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

/**********************************************************************
 * Class that creates the JFrame for the Surround game. It creates a
 * SurroundPanel and sets the default conditions for the JFrame 
 * 
 * CIS 163
 * @author Jesse Roe & Ben Benson
 * @version 2/11/2015
 *********************************************************************/
public class Surround {
	
	/******************************************************************
	 * Main method of the Surround class. It instantiates an instance
	 * of the SurroundPanel and menu items.
	 * 
	 * CIS 163
	 * @author Jesse Roe & Ben Benson
	 * @version 2/11/2015
	 *****************************************************************/
		
	public static void main(String[] args){
		
		JFrame frame = new JFrame("Surround");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Menu elements that are used to close or reset the JFrame	
		JMenuItem quitItem = new JMenuItem("Quit");
		JMenuItem newGameItem = new JMenuItem("New Game");
		
		// New instance of the SurroundPanel
		SurroundPanel panel = new SurroundPanel(quitItem, newGameItem);
		frame.getContentPane().add(panel);
			
	
		frame.pack();
		frame.setVisible(true);
				
	}
	
	
	
}
