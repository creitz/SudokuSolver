
package com.creitz.sudokusolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

//Charles Reitz
//1/4/15

public class Grid {

	private ArrayList<Square> squares = new ArrayList<Square>();
	private int gridSize = -1;
	
	public Grid(String file) {
		
		BufferedReader myFile = null;
		try {
	        myFile = new BufferedReader(new FileReader(file));
	    } catch (Exception e) {
	    	System.err.println("Ooops!  I can't seem to read the file on the standard input!");
	        System.exit(1);
	    }
		
		readSquares(myFile);
		erasePossibilitiesFromFilledNumbers();
	}
	
	public ArrayList<Square> getSquares() {
		return squares;
	}
	
	/**
	 * Returns the value in the square at the given <code>row</code> and <code>col</code>.
	 * The top left square is row 0, column 0.
	 * @param row
	 * @param col
	 * @return
	 */
	public int getValueAtLocation(int row, int col) {
		return squares.get(convertToPos(row, col)).getValue();
	}
	
	public boolean fillAnyEmptySquare() {
		if (fillDefinite()) {
			return true;
		} else if (fillByLookingAtColumns()) {
			return true;
		} else if (fillByLookingAtRows()) {
			return true;
		} else if (fillByLookingAtBoxes()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Loops through all squares until an empty square with only one possible value is
	 * found, or the squares are exhausted.  If found, the square's value is set to
	 * that possible value, and appropriate erasing is done. 
	 * @return true if a value is set; false otherwise
	 */
	private boolean fillDefinite() {
		for (int i=0; i < squares.size(); i++) {
			Square s = squares.get(i);
			if (!s.hasValue() && s.possibleValues().size() == 1) {
				int value = (int)s.possibleValues().toArray()[0];
				s.setValue(value);
				eraseEach(i, value);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Traverses the columns, stopping when a value can be 
	 * filled in by examining a column
	 * @return true if a value is filled in; false otherwise
	 */
	private boolean fillByLookingAtColumns() {
		
		for (int col = 0; col < gridSize; col++) {
			if (findValueInColumn(col)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Traverses the rows, stopping when a value can be 
	 * filled in by examining a row
	 * @return true if a value is filled in; false otherwise
	 */
	private boolean fillByLookingAtRows() {
		
		for (int row = 0; row < gridSize; row++) {
			if (findValueInRow(row)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Traverses the "quadrants", stopping when a value can be 
	 * filled in by examining a quadrant
	 * @return true if a value is filled in; false otherwise
	 */
	private boolean fillByLookingAtBoxes() {
		
		int quadrantSize = (int)Math.sqrt(gridSize);
		
		for (int col = 0; col < gridSize; col += quadrantSize) {
			for (int row = 0; row < gridSize; row += quadrantSize) {
				int position = convertToPos(row, col);
				if (findValueInBox(position)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Looks for a value in a given column.
	 * @param col The column number
	 * @return true if a value is filled in; false otherwise
	 */
	private boolean findValueInColumn(int col) {
		int position = convertToPos(0, col);
		ArrayList<Integer> inCol = incidentalInColumn(position); 
		return findValueInSet(inCol);
	}
	
	/**
	 * Looks for a value in a given row.
	 * @param row The row number
	 * @return true if a value is filled in; false otherwise
	 */
	private boolean findValueInRow(int row) {
		int position = convertToPos(row, 0);
		ArrayList<Integer> inRow = incidentalInRow(position); 
		return findValueInSet(inRow);
	}
	
	/**
	 * Looks for a value in a given boxposition.
	 * @param boxPos
	 * @return true if a value is filled in; false otherwise
	 */
	private boolean findValueInBox(int boxPos) {
		ArrayList<Integer> inBox = incidentalInBox(boxPos); 
		return findValueInSet(inBox);
	}
	
	/**
	 * Looks for a value that can be determined for the squares in the given
	 * <code>poses</code> by looking for a unique possible value among those 
	 * squares.  If a unique possible value is found for a square in the set,
	 * it's value is set to that, and the affected squares are erased of having
	 * that value as a possible value.
	 * @param poses the positions to examine
	 * @return
	 */
	private boolean findValueInSet(ArrayList<Integer> poses) {
		for (int pos : poses) {
			Square s = squares.get(pos);
			if (!s.hasValue()) {
				ArrayList<Integer> possibleValues = new ArrayList<>(s.possibleValues());
				for (int value : possibleValues) {
					if (isOnly(value, poses, pos)) {
						s.setValue(value);
						eraseEach(pos, value);
						return true;
					}
				}
			}
		}
		
		return false;	
	}
	
	/**
	 * 
	 * Determines if the given <code>value</code> is unique in possible values 
	 * for the square at <code>noLookPos</code> among <code>poses</code>.
	 * 
	 * @param value The value to examine
	 * @param poses The list of positions to examine
	 * @param noLookPos The position of the square whose value may be set
	 * @return true if value is not a possible value at any square corresponding to
	 * <code>poses</code> besides the square at <code>noLooksPos</code>; false otherwise
	 */
	private boolean isOnly(int value, ArrayList<Integer> poses, int noLookPos) {
		
		HashSet<Integer> otherValues = new HashSet<>();
		
		for (int pos : poses) {
			if (pos != noLookPos) {
				Square s = squares.get(pos);
				otherValues.addAll(s.possibleValues());
			}
		}
		
		return !otherValues.contains(value);
	}
	
	/**
	 * Erase affected squares' possibilities from each square 
	 * that has a value
	 */
	public void erasePossibilitiesFromFilledNumbers() {
		for (int i = 0; i < squares.size(); i++) {
			Square s = squares.get(i);
			if (s.hasValue()) {
				int value = s.getValue();
				eraseEach(i, value);
			}
		}
	}
	
	/**
	 * Erase across, down, and in-box possibilities of the given value.
	 * @param pos 
	 * @param value
	 */
	private void eraseEach(int pos, int value) {
		eraseAcross(pos, value);
		eraseDown(pos, value);
		eraseInBox(pos, value);
	}
	
	/**
	 * Erase across the row the given value from the squares' possibilities.
	 * @param position
	 * @param value
	 */
	private void eraseAcross(int position, int value) {
		ArrayList<Integer> inRow = incidentalInRow(position);
		erase(inRow, value);
	}
	
	/**
	 * Erase up and down the column the given value from the squares' possibilities.
	 * @param position
	 * @param value
	 */
	private void eraseDown(int position, int value) {
		ArrayList<Integer> inColumn = incidentalInColumn(position);
		erase(inColumn, value);
	}
	
	/**
	 * Erase in box the given value from the squares' possibilities.
	 * @param position
	 * @param value
	 */
	private void eraseInBox(int position, int value) {
		ArrayList<Integer> inBox = incidentalInBox(position);
		erase(inBox, value);
	}
	
	/**
	 * Removes the given <code>value</code> from each square at <code>poses</code>'s 
	 * possibilities
	 * @param poses
	 * @param value
	 */
	private void erase(ArrayList<Integer> poses, int value) {
		
		for (int i = 0; i < poses.size(); i++) {
			int pos = poses.get(i);
			Square s = squares.get(pos);
			s.removePossibility(value);
		}
	}
	
	/**
	 * Checks if there are still squares without a value
	 * @return true if there is at least one empty square; false otherwise
	 */
	public boolean hasEmpty() {
		
		for (int i=0; i < squares.size(); i++) {
			Square s = squares.get(i);
			if (!s.hasValue()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns an ArrayList of all of the positions in the array that correspond to being
	 * in the same box as the given <code>pos</code>
	 */
	private ArrayList<Integer> incidentalInBox(int pos) {
		
		//create a new array
		ArrayList<Integer> inBox = new ArrayList<Integer>();
		
		//get the position of the top left of the box
		int topLeftOfBox = topLeftOfBox(pos);
		
		//get the dimensions of the box
		int sqrtSize = (int)Math.sqrt(gridSize);
		
		//get the corresponding row and columns of the top left of the box
		int startColumn = getColumn(topLeftOfBox);
		int startRow = getRow(topLeftOfBox);
		
		//iterate through all of the positions in the box, adding them
		//to the list if they are not equal to pos
		for (int col=startColumn; col < startColumn + sqrtSize; col++) {
			for (int row = startRow; row < startRow + sqrtSize; row++) {
				int thisPos = convertToPos(row, col);
				//if (thisPos != pos) {
					inBox.add(thisPos);
				//}
			}
		}	
		
		return inBox;
	}
	
	/**
	 * Converts a given <code>row</code> and <code>col</code> to an integer position
	 * in the vector representation.
	 */
	private int convertToPos(int row, int col) {
		return col + gridSize * row;
	}
	
	/**
	 * Returns the position in the vector representation of the top-left-most square
	 * of the box in which the given position exists.
	 */
	private int topLeftOfBox(int pos) {
		
		//get the row and column of the given pos
		int rowOfPos = getRow(pos);
		int columnOfPos = getColumn(pos);
		
		//get the dimensions of the box
		int quadrantSize = (int)Math.sqrt(gridSize);
		
		//use integer division to truncate and get the left/top most box
		int horizontalBoxNumber = columnOfPos / quadrantSize;
		int verticalBoxNumber = rowOfPos / quadrantSize;
		
		//get the column and row of that box
		int columnOfBox = horizontalBoxNumber * quadrantSize;
		int rowOfBox = verticalBoxNumber * quadrantSize;
		
		//convert to position and return
		return convertToPos(rowOfBox, columnOfBox);
	}
	
	/**
	 * Returns an ArrayList of all of the positions in the array that correspond to being
	 * in the same column as the given <code>pos</code>
	 */
	private ArrayList<Integer> incidentalInColumn(int pos) {
		
		ArrayList<Integer> incidents = new ArrayList<Integer>();
		
		//descend down the column and add each position to the array
		int column = getColumn(pos);
		for (int i=column; i < gridSize * gridSize; i += gridSize) {
			//if (i != pos) {
				incidents.add(i);
			//}
		}
		
		return incidents;
	}
	
	/**
	 * Returns an ArrayList of all of the positions in the array that correspond to being
	 * in the same row as the given <code>pos</code>
	 */
	private ArrayList<Integer> incidentalInRow(int pos) {
		
		ArrayList<Integer> incidents = new ArrayList<Integer>();
		
		//traverse the row and add each position to the array
		int row = getRow(pos);
		int startPos = convertToPos(row, 0);
		for (int i = startPos; i < startPos + gridSize; i++) {
			//if (i != pos) {
				incidents.add(i);
			//}			
		}
		
		return incidents;
	}
	
	/**
	 * Get the row of the given <code>pos</code>
	 */
	private int getRow(int pos) {
		return pos / gridSize;
	}
	
	/**
	 * Get the column of the given <code>pos</code>
	 */
	private int getColumn(int pos) {
		return pos % gridSize;
	}
	
	/**
	 * Prints the state of the grid
	 */
	public void print() {
		
		for (int i=0; i < squares.size(); i++) {
			
			if (i % gridSize == 0) {
				System.out.println();
			}
			Square s = squares.get(i);
			if (s.hasValue()) {
				System.out.print(s.getValue());
			} else {
				System.out.print(Square.EMPTY_SQUARE_INDICATOR);
			}

		}
		System.out.println("\n");
	}
	
	/**
	 * Read from the given <code>file</code> and populate the vector
	 * representation based on its contents.
	 */
	private void readSquares(BufferedReader file) {
		
		//Read the gridSize (width)
		try {
			gridSize = Integer.valueOf(file.readLine());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		//Read a gridSize # of lines
		for (int i=0; i < gridSize; i++) {
			try {
				readLine(file.readLine());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Process one line.  Creates a square from each number or * and
	 * adds it to the vector representation.
	 */
	private void readLine(String line) {
		
		for (int i=0; i < gridSize; i++) {
			Square square = new Square(String.valueOf(line.charAt(i)), gridSize);
			squares.add(square);
		}
	}	
	
	@Override
	public boolean equals(Object o) {
		
		Grid state = (Grid)o;
		
		if (gridSize != state.gridSize || squares.size() != state.squares.size()) {
			return false;
		}
		
		for (int i = 0; i < squares.size(); i++) {
			
			if (squares.get(i).getValue() != state.squares.get(i).getValue()) {
				return false;
			}
			
		}
		
		return true;
	}
	
}
