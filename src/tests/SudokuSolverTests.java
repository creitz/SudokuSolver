package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.junit.Test;
import com.creitz.sudokusolver.*;

public class SudokuSolverTests {

	
	@Test
	public void fourGridTest1() {
		Grid solutionGrid = new Grid("testfiles/4testfile1sol");
		SudokuSolver solver = new SudokuSolver("testfiles/4testfile1");
		solver.solve();
		assertEquals(solutionGrid, solver.getGrid());
	}
	
	@Test
	public void fourGridTest2() {
		Grid solutionGrid = new Grid("testfiles/4testfile2sol");
		SudokuSolver solver = new SudokuSolver("testfiles/4testfile2");
		solver.solve();
		assertEquals(solutionGrid, solver.getGrid());
	}
	
	@Test
	public void fourGridTest3() {
		Grid solutionGrid = new Grid("testfiles/4testfile3sol");
		SudokuSolver solver = new SudokuSolver("testfiles/4testfile3");
		solver.solve();
		assertEquals(solutionGrid, solver.getGrid());
	}
	
	@Test
	public void nineGridTest1() {
		Grid solutionGrid = new Grid("testfiles/9testfile1sol");
		SudokuSolver solver = new SudokuSolver("testfiles/9testfile1");
		solver.solve();
		assertEquals(solutionGrid, solver.getGrid());
	}
	
	@Test
	public void nineGridTest2() {
		Grid solutionGrid = new Grid("testfiles/9testfile2sol");
		SudokuSolver solver = new SudokuSolver("testfiles/9testfile2");
		solver.solve();
		assertEquals(solutionGrid, solver.getGrid());
	}
	
	@Test
	public void nineGridTest3() {
		Grid solutionGrid = new Grid("testfiles/9testfile3sol");
		SudokuSolver solver = new SudokuSolver("testfiles/9testfile3");
		solver.solve();
		assertEquals(solutionGrid, solver.getGrid());
	}
	
	@Test
	public void sixteenGridTest1() {
		Grid solutionGrid = new Grid("testfiles/16testfile1sol");
		SudokuSolver solver = new SudokuSolver("testfiles/16testfile1");
		solver.solve();
		assertEquals(solutionGrid, solver.getGrid());
	}
	
	@Test
	public void manualGridtest() {

		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader("testfiles/9testfile2"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		int gridSize = 0;
		try {
			gridSize = Integer.valueOf(file.readLine());
		} catch (Exception e1) {
			e1.printStackTrace();
		} 

		Grid newGrid = new Grid(gridSize);
		
		if (file != null) {
		
			//Read a gridSize # of lines
			for (int row = 0; row < gridSize; row++) {
				try {
					String line = file.readLine();
					int col = 0;
					for (int pos = 0; pos < line.length(); pos++) {
						
						String indicator = String.valueOf(line.charAt(pos));
						if (indicator.equals(" ")) {
							continue;
						}
						
						if (!indicator.equals(Square.EMPTY_SQUARE_INDICATOR)) {
							newGrid.setValueAtCoords(row, col, Integer.valueOf(indicator));
						}	
						
						col++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		Grid solutionGrid = new Grid("testfiles/9testfile2sol");
		SudokuSolver solver = new SudokuSolver(newGrid);
		assertTrue(!solutionGrid.equals(solver.getGrid()));
		solver.solve();
		assertTrue(solutionGrid.equals(solver.getGrid()));
	}
	

}
