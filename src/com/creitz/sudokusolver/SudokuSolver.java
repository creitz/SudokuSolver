package com.creitz.sudokusolver;

//Charles Reitz
//1/4/15

public class SudokuSolver {

	private Grid mGrid;
	
	public SudokuSolver(String file) {
		mGrid = new Grid(file);
	}
	
	public boolean solve() {
		
		while (mGrid.hasEmpty()) {
			if (!mGrid.fillAnyEmptySquare()) {
				break;
			}
		}
		
		return !mGrid.hasEmpty();
	}
	
	public Grid getGrid() {
		return mGrid;
	}
	
	public void print() {
		mGrid.print();
	}
	
	public static void main(String[] args) {
		
		SudokuSolver solver = new SudokuSolver(args[0]);
		System.out.println("\nRead board: ");
		solver.print();
		if (solver.solve()) {
			System.out.println("Solution board: ");
			solver.print();
		} else {
			System.out.println("Board does not have a unique solution.\n");
		}
	}
}
