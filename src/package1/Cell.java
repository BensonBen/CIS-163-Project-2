package package1;

/**********************************************************************
 * Class that creates and controls the cell object. Has various methods
 * to set or return the current value stored in the cell. 
 * 
 * CIS 163
 * @author Jesse Roe & Ben Benson
 * @version 2/11/2015
 *********************************************************************/
public class Cell {
	
	/**Private instance variable that stores cell integer value**/
	private int currentPlayer;
	
	/******************************************************************
	 * Alternate constructor that accepts a integer value.
	 * @param int pNumber
	 *****************************************************************/
	Cell(int pNumber){
		currentPlayer = pNumber;
	}
	
	/******************************************************************
	 * Setter method for modifying the value stored in cell object.
	 * @param int pNumber
	 *****************************************************************/	
	public void setPlayer(int pNumber){
		currentPlayer = pNumber;
	}
	
	/******************************************************************
	 * Alternate constructor that accepts a integer value.
	 * @return int currentPlayer
	 *****************************************************************/
	public int getPlayer(){
		return currentPlayer;
	}	

	
}
