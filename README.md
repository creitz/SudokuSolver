SudokuSolver
=========================

Simple command line utility and java components for solving 4- or 9-[Sudoku][] puzzles.

## Installation

To install you must clone the source from GitHub and install::

    $ git clone git@github.com:creitz/SudokuSolver.git
    $ cd SudokuSolver


## Compiling

Once in the cloned directory, run the following commands to create a bin/ directory and compile.

	$ mkdir bin
	$ javac -d bin/ src/com/creitz/sudokusolver/*

## Running from command line

	$ cd bin/
	$ java com/creitz/sudokusolver/SudokuSolver /path/to/file
	
The /path/to/file should be replaced by the path to the file containing a sudoku board you wish to solve.
The solver excepts a board formatted as such:

```
9
*3***7***
2***56*1*
***19****
*59***8**
3*******5
**6***79*
****41***
*2*83***7
***2***6*
```

The asterisks indicate an empty square in the board, and the numbers are the values for filled squares.
The 9 indicates that the board will be a 9x9.  If you wish to solve a 4x4, the file could containing the following:

```
4
3*1*
*12*
****
13*2
```

## Using the components



[Sudoku]: http://en.wikipedia.org/wiki/Sudoku
