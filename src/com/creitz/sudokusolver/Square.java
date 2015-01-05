package com.creitz.sudokusolver;
import java.util.HashSet;


//Charles Reitz
//1/4/15

//A container object to represent a square in the sudoku board. 

public class Square {

	private HashSet<Integer> possibleValues = new HashSet<>();
	public static final String EMPTY_SQUARE_INDICATOR = "*";
	
	//the integer value of the square
	private int value = -1;
	
	/**
	 * Creates a new Square from the given <code>s</code>, which must be an integer
	 * or a *.
	 */
	public Square(String s, int gridSize) {
		
		initialize(gridSize);
		
		if (!s.equals(EMPTY_SQUARE_INDICATOR)) {
			value = Integer.valueOf(s, gridSize+1);
			removeAllPossibilities();
		}
	}
	
	private void removeAllPossibilities() {
		possibleValues = new HashSet<>();
	}
		
	public HashSet<Integer> possibleValues() {
		return possibleValues;
	}
	
	public void removePossibility(int i) {
		possibleValues.remove(i);
	}
	
	private void initialize(int gridSize) {
		
		for (int i=1; i <= gridSize; i++) {
			possibleValues.add(i);
		}	
	}
	
	/**
	 * Returns the value of the square.
	 * @return
	 */
	public int getValue() {
		return value;
	}
	
	public boolean hasValue() {
		return value != -1;
	}
	
	/**
	 * Sets the value of the square to the value of <code>val</code>,
	 *  if the square is modifiable.
	 */
	public void setValue(int val) {
		value = val;
		removeAllPossibilities();
	}
	
}
