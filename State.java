import java.util.Arrays;
// Represents the state of an 8-puzzle board


public class State {
    // 2D array representation of the current state
    // 1-8 represent the numbers
    // 0 represents the blank
    private byte[][] board;

    // keeps track of blank position
    private int[] blank;
    // 2D array representation of the goal state
    private final byte[][] GOAL = { {0, 1, 2},
				   {3, 4, 5},
				   {6, 7, 8} };
    private final int DIMENSION = 3;
    
    // Default Constructor
    // Initializes the state to the goal state.
    public State() {
	board = new byte[][] { {0, 1, 2},
			      {3, 4, 5},
			      {6, 7, 8} };
	blank = new int[] {0, 0};
    }

    // Overloaded Constructor
    // Initializes the state of the board to the 2D array passed in
    // Peforms a deep copy on the 2D array
    public State(byte[][] copyBoard) {
	board = new byte[DIMENSION][DIMENSION];
	for ( int i = 0; i < DIMENSION; i++ ) {
	    for ( int j = 0; j < DIMENSION; j++ ) {
		if ( copyBoard[i][j] == 0 ) // found blank position
		    blank = new int[] {i, j}; // copy blank position
		// copy element
		board[i][j] = copyBoard[i][j];
	    }
	}
    }

    // Returns a new State object represnting the same board configuration as this State
    public State duplicate() {
	return new State(board);
    }
    
    // Moves the blank tile up
    public void moveUp() {
	swap(blank[0], blank[1], blank[0]-1, blank[1]);
	// update blank position
	blank[0] -= 1;
    }

    // Moves the blank tile down
    public void moveDown() {
	swap(blank[0], blank[1], blank[0]+1, blank[1]);
	blank[0] += 1;
    }

    // Moves the blank tile left
    public void moveLeft() {
	swap(blank[0], blank[1], blank[0], blank[1]-1);
	blank[1] -= 1;
    }

    // Moves the blank tile right
    public void moveRight() {
	swap(blank[0], blank[1], blank[0], blank[1]+1);
	blank[1] += 1;
    }

    // Returns a boolean array of 4 values
    // [up, down, left, right]
    // array element is true if moving in that direction is legal
    public boolean[] legalMoves() {
	boolean[] legal = {true, true, true, true};
	if (blank[0] == 0) // blank in first row
	    legal[0] = false;
	if (blank[0] == 2) // blank in last row
	    legal[1] = false;
	if (blank[1] == 0) // blank in first column
	    legal[2] = false;
	if (blank[1] == 2) // blank in last column
	    legal[3] = false;

	return legal;
    }
    
    // swaps the tiles at position x1, y1 and x2, y2
    private void swap(int x1, int y1, int x2, int y2) {
	byte tmp = board[x1][y1];
	board[x1][y1] = board[x2][y2];
	board[x2][y2] = tmp;
    }
    
    public void print() {
	for ( int i = 0; i < board.length; i++ ) {
	    for ( int j = 0; j < board[i].length; j++ )
		System.out.print(board[i][j] + "\t");
	    System.out.println();
	}
    }
    
    public static void main(String[] args) {

	State b1 = new State();
	b1.print();
	System.out.println(Arrays.toString(b1.legalMoves()));
	System.out.println();

	b1.moveDown();
	b1.print();
	System.out.println(Arrays.toString(b1.legalMoves()));
	System.out.println();

	b1.moveRight();
	b1.print();
	System.out.println(Arrays.toString(b1.legalMoves()));
	System.out.println();

	b1.moveDown();
	b1.print();
	System.out.println(Arrays.toString(b1.legalMoves()));
	System.out.println();

	b1.moveRight();
	b1.print();
	System.out.println(Arrays.toString(b1.legalMoves()));
	System.out.println();

	b1.moveUp();
	b1.print();
	System.out.println(Arrays.toString(b1.legalMoves()));
	System.out.println();

	b1.moveLeft();
	b1.print();
	System.out.println(Arrays.toString(b1.legalMoves()));
	System.out.println();

	
	State b2 = b1.duplicate();
	b2.print();
	System.out.println();
	
	b2.moveRight();
	b1.moveDown();

	b2.print();
	System.out.println();
	b1.print();
	

	
    }


}
    
