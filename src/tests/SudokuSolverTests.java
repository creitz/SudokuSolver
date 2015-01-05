package tests;

import static org.junit.Assert.*;

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

}
