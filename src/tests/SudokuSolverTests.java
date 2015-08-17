package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.junit.Test;
import com.creitz.sudokusolver.*;

public class SudokuSolverTests {

	private void check(String testFile, String solutionFile) {
		Grid solutionGrid = new Grid(solutionFile);
		SudokuSolver solver = new SudokuSolver(testFile);
		solver.solve();
		assertEquals(solutionGrid, solver.getGrid());
	}
	
	@Test
	public void fourGridTest1() {
		String testFile = "testfiles/4testfile1";
		String solutionFile = "testfiles/4testfile1sol";
		check(testFile, solutionFile);
	}
	
	@Test
	public void fourGridTest2() {
		String testFile = "testfiles/4testfile2";
		String solutionFile = "testfiles/4testfile2sol";
		check(testFile, solutionFile);
	}
	
	@Test
	public void fourGridTest3() {
		String testFile = "testfiles/4testfile3";
		String solutionFile = "testfiles/4testfile3sol";
		check(testFile, solutionFile);
	}
	
	@Test
	public void nineGridTest1() {
		String testFile = "testfiles/9testfile1";
		String solutionFile = "testfiles/9testfile1sol";
		check(testFile, solutionFile);
	}
	
	@Test
	public void nineGridTest2() {
		String testFile = "testfiles/9testfile2";
		String solutionFile = "testfiles/9testfile2sol";
		check(testFile, solutionFile);
	}
	
	@Test
	public void nineGridTest3() {
		String testFile = "testfiles/9testfile3";
		String solutionFile = "testfiles/9testfile3sol";
		check(testFile, solutionFile);
	}
	
	@Test
	public void sixteenGridTest1() {
		String testFile = "testfiles/16testfile1";
		String solutionFile = "testfiles/16testfile1sol";
		check(testFile, solutionFile);
	}
	
	@Test
	public void twentyFiveGridTest1() {
		String testFile = "testfiles/25testfile1";
		String solutionFile = "testfiles/25testfile1sol";
		check(testFile, solutionFile);
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
