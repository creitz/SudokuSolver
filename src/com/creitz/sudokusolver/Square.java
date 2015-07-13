package com.creitz.sudokusolver;
import java.util.HashSet;


//Charles Reitz
//1/4/15

//A container object to represent a square in the sudoku board. 

public class Square {

	private HashSet<Integer> mPossibleValues = new HashSet<>();
	public static final String EMPTY_SQUARE_INDICATOR = "*";
	
	//the integer value of the square
	private int value = -1;
	
	/**
	 * Initialize the square with the given <code>gridSize</code>, NOT
	 * the value of the square. Use setValue(int) to set the value.
	 * @param gridSize
	 */
	public Square(int gridSize) {
		for (int i=1; i <= gridSize; i++) {
			mPossibleValues.add(i);
		}	
	}
	
	private void removeAllPossibilities() {
		mPossibleValues = new HashSet<>();
	}
		
	public HashSet<Integer> possibleValues() {
		return mPossibleValues;
	}
	
	public void removePossibility(int i) {
		mPossibleValues.remove(i);
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
