
package com.creitz.sudokusolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

//Charles Reitz
//1/4/15

public class Grid {

	private ArrayList<Square> mSquares = new ArrayList<Square>();
	private int mGridSize = -1;
	
	public Grid(String file) {
		
		BufferedReader myFile = null;
		try {
	        myFile = new BufferedReader(new FileReader(file));
	    } catch (Exception e) {
	    	System.err.println("Ooops!  I can't seem to read the file!");
	        System.exit(1);
	    }
		
		readSquares(myFile);
		finalize();
	}
	
	public Grid(int grSize) {
		mGridSize = grSize;
		
		int numSquares = mGridSize * mGridSize;
		for (int i=0; i < numSquares; i++) {
			mSquares.add(new Square(mGridSize));
		}
	}
	
	public void finalize() {
		erasePossibilitiesFromFilledNumbers();
	}
	
	public void setValueAtCoords(int row, int column, int value) {
		int squarePos = convertToPos(row, column);
		mSquares.get(squarePos).setValue(value);
	}
	
	public ArrayList<Square> getSquares() {
		return mSquares;
	}
	
	/**
	 * Returns the value in the square at the given <code>row</code> and <code>col</code>.
	 * The top left square is row 0, column 0.
	 * @param row
	 * @param col
	 * @return
	 */
	public int getValueAtLocation(int row, int col) {
		int squarePos = convertToPos(row, col);
		return mSquares.get(squarePos).getValue();
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
	 * Checks if there are still squares without a value
	 * @return true if there is at least one empty square; false otherwise
	 */
	public boolean hasEmpty() {
		
		for (Square s : mSquares) {
			if (!s.hasValue()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Prints the state of the grid
	 */
	public void print() {
		
		int blockSize = 3;
		
		for (int i=0; i < mSquares.size(); i++) {
			
			if (i % mGridSize == 0) {
				System.out.println();
			}
			Square s = mSquares.get(i);
			String singleOutput;
			if (s.hasValue()) {
				singleOutput = Integer.toString(s.getValue()) + " ";
			} else {
				singleOutput = Square.EMPTY_SQUARE_INDICATOR + " ";
			}
			
			System.out.print(singleOutput);
			
			for (int pos=singleOutput.length(); pos <= blockSize; pos++) {
				System.out.print(" ");
			}
		}
		System.out.println("\n");
	}
	

	/**
	 * Loops through all squares until an empty square with only one possible value is
	 * found, or the squares are exhausted.  If found, the square's value is set to
	 * that possible value, and appropriate erasing is done. 
	 * @return true if a value is set; false otherwise
	 */
	private boolean fillDefinite() {
		for (int i=0; i < mSquares.size(); i++) {
			Square s = mSquares.get(i);
			if (!s.hasValue() && s.getPossibleValues().size() == 1) {
				int value = (int)s.getPossibleValues().toArray()[0];
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
		
		for (int col = 0; col < mGridSize; col++) {
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
		
		for (int row = 0; row < mGridSize; row++) {
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
		
		int quadrantSize = (int)Math.sqrt(mGridSize);
		
		for (int col = 0; col < mGridSize; col += quadrantSize) {
			for (int row = 0; row < mGridSize; row += quadrantSize) {
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
			Square s = mSquares.get(pos);
			if (!s.hasValue()) {
				ArrayList<Integer> possibleValues = new ArrayList<>(s.getPossibleValues());
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
				Square s = mSquares.get(pos);
				otherValues.addAll(s.getPossibleValues());
			}
		}
		
		return !otherValues.contains(value);
	}
	
	/**
	 * Erase affected squares' possibilities from each square 
	 * that has a value
	 */
	private void erasePossibilitiesFromFilledNumbers() {
		for (int i = 0; i < mSquares.size(); i++) {
			Square s = mSquares.get(i);
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
			Square s = mSquares.get(pos);
			s.removePossibility(value);
		}
	}
	
	/**
	 * Returns an ArrayList of all of the positions in the array that correspond to being
	 * in the same box as the given <code>pos</code>
	 */
	private ArrayList<Integer> incidentalInBox(int pos) {
		
		ArrayList<Integer> inBox = new ArrayList<Integer>();
		
		int topLeftOfBox = topLeftOfBox(pos);
		
		//get the dimensions of the box
		int sqrtSize = (int)Math.sqrt(mGridSize);
		
		//get the corresponding row and columns of the top left of the box
		int startColumn = getColumn(topLeftOfBox);
		int startRow = getRow(topLeftOfBox);
		
		for (int col=startColumn; col < startColumn + sqrtSize; col++) {
			for (int row = startRow; row < startRow + sqrtSize; row++) {
				int thisPos = convertToPos(row, col);
				inBox.add(thisPos);
			}
		}	
		
		return inBox;
	}
	
	/**
	 * Converts a given <code>row</code> and <code>col</code> to an integer position
	 * in the vector representation.
	 */
	private int convertToPos(int row, int col) {
		return col + mGridSize * row;
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
		int quadrantSize = (int)Math.sqrt(mGridSize);
		
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
		for (int i=column; i < mGridSize * mGridSize; i += mGridSize) {
			incidents.add(i);
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
		for (int i = startPos; i < startPos + mGridSize; i++) {
			incidents.add(i);
		}
		
		return incidents;
	}
	
	/**
	 * Get the row of the given <code>pos</code>
	 */
	private int getRow(int pos) {
		return pos / mGridSize;
	}
	
	/**
	 * Get the column of the given <code>pos</code>
	 */
	private int getColumn(int pos) {
		return pos % mGridSize;
	}
	
	/**
	 * Read from the given <code>file</code> and populate the vector
	 * representation based on its contents.
	 */
	private void readSquares(BufferedReader file) {
		
		//Read the gridSize (width)
		try {
			mGridSize = Integer.valueOf(file.readLine());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		//Read a gridSize # of lines
		for (int i=0; i < mGridSize; i++) {
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
		
		String[] chunks = line.split(" ");
		
		for (String chunk : chunks) {
			
			if (chunk.isEmpty() || chunk.equals(" ")) {
				continue;
			}
			
			Square square = new Square(mGridSize);
			if (!chunk.equals(Square.EMPTY_SQUARE_INDICATOR)) {
				square.setValue(Integer.valueOf(chunk));
			}
			mSquares.add(square);
		}
	}	
	
	@Override
	public boolean equals(Object o) {
		
		Grid state = (Grid)o;
		
		if (mGridSize != state.mGridSize || mSquares.size() != state.mSquares.size()) {
			return false;
		}
		
		for (int i = 0; i < mSquares.size(); i++) {
			
			if (mSquares.get(i).getValue() != state.mSquares.get(i).getValue()) {
				return false;
			}
			
		}
		
		return true;
	}
	
}
