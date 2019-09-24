import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;

// Represents the state of an 8-puzzle board


public class State implements Comparable {
    // 2D array representation of the current state
    // 1-8 represent the numbers
    // 0 represents the blank
    private byte[][] board;
    private final int DIMENSION = 3;

    // keeps track of blank position
    private int[] blank;
    // 2D array representation of the goal state
    private static final byte[][] GOAL = { {0, 1, 2},
				   {3, 4, 5},
				   {6, 7, 8} };

    // number of moves it took to go from start state to this node
    // g(n) component of the evaluation function
    // calculated during A* 
    private int pathCost;
    // heuristic value h(n)
    private int estimatedCost;

    // a list of the previous moves start to this state
    List<String> path;
    
    // Default Constructor
    // Initializes the state to the goal state.
    public State() {
	board = new byte[][] { {0, 1, 2},
			      {3, 4, 5},
			      {6, 7, 8} };
	blank = new int[] {0, 0};
	pathCost = 0;
	estimatedCost = 0;
	path = new LinkedList<String>();
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
	pathCost = 0;
	estimatedCost = 0;
	path = new LinkedList<String>();
	
    }

    // Returns a new State object represnting the same board configuration as this State
    // The path cost g(n) is also duplicated
    // Note: Heuristic values do not need to be copied over to duplicate states. 
    public State duplicate() {
	State newState = new State(board); // copies 2D array
	newState.setPathCost(this.getPathCost()); // copies over path cost
	newState.setPath(this.getPath());
	return newState;
    }

    // Returns true if the current state matches the goal state
    // Returns false otherwise
    public boolean isGoal() {
	for ( int i = 0; i < DIMENSION; i++ ) {
	    for ( int j = 0; j < DIMENSION; j++ ) {
		if ( board[i][j] != GOAL[i][j] )
		    return false;
	    }
	}
	return true;
    }

    
    // Returns the value of the A* evaluation function f(n) on this state
    // f(n) = g(n) + h(n)
    // Uses the heuristic specified by input
    // Note: Calling this method recomputes the heuristic value
    public int evaluate(String heuristic) {
	if ( heuristic.equals("h1") )
	    estimatedCost = calculateH1();
	else if ( heuristic.equals("h2") )
	    estimatedCost = calculateH2();

	return pathCost + estimatedCost;
    }


    // Two states are equal if their board configurations are the ssame
    // Used in priority queue contains()
    @Override
    public boolean equals(Object other) {
	byte[][] otherBoard = ((State) other).getBoard();
	for ( int i = 0; i < DIMENSION; i++ ) {
	    for ( int j = 0; j < DIMENSION; j++ ) {
		if ( board[i][j] != otherBoard[i][j] )
		    return false;
	    }
	}
	return true;
    }

    // Must override hashcode so that works with HashSet
    @Override
    public int hashCode() {
	int hash = 0;
	for ( int i = 0; i < DIMENSION; i++ ) {
	    for ( int j = 0; j < DIMENSION; j++ ) {
		hash += (i+1) * (j+1) * board[i][j] * Math.pow(1, i+j);
	    }
	}
	return hash;
    }
    
    // Used to order states in priority queue
    public int compareTo(Object other) {
	return (this.pathCost + this.estimatedCost)
	    - (((State) other).getPathCost() + ((State) other).getEstimatedCost());
    }
    
    // Calculates the h1 hueristic for the board state
    // Sum of the number of misplaced tiles
    public int calculateH1() {
	int error = 0; // number of misplaced tiles
	
	for ( int i = 0; i < board.length; i++ ) {
	    for ( int j = 0; j < board.length; j++ ) {
		// compare board value at (i,j) to the supposed value at (i,j) of the goal state
		if ( board[i][j] != 0 && board[i][j] != GOAL[i][j] )
		    error += 1;
	    }
	}
	return error;
    }

    // Calculates the h2 heuristic for the board state
    // Sum of distances of the tiles from their goal position
    public int calculateH2() {
	int error = 0; // sum of misplaced distances

	for ( int i = 0; i < board.length; i++ ) {
	    for ( int j = 0; j < board.length; j++ ) {
		if ( board[i][j] != 0 ) { // do not calculate Manhattan distance for zero
		    // the goal position of the tile that is currently at position (i,j) 
		    int[] goalPosition = { (board[i][j]) / 3, (board[i][j]) % 3 };
		    // calculate distance between (i,j) and goal position of this element
		    error += Math.abs( goalPosition[0] - i ) + Math.abs( goalPosition[1] - j );
		}
	    }
	}
	return error;
    }

    // Accessor method
    // Returns the 2D board array
    public byte[][] getBoard() {
	return board;
    }

    // Accessor method
    // Returns path cost from start node to this node g(n)
    public int getPathCost() {
	return pathCost;
    }

    // Accessor method
    // Returns the list of moves from start to current state
    public List<String> getPath() {
	return path;
    }

    // Accessor method
    // Returns estimated cost based on heuristic
    public int getEstimatedCost() {
	return estimatedCost;
    }
    
    // Mutator method
    // Sets path cost g(n) to input parameter
    // Returns the new path cost
    public int setPathCost(int n) {
	pathCost = n;
	return pathCost;
    }

    // Increments the path cost g(n) by 1
    // Returns the new path cost
    public int incrementPathCost() {
	pathCost += 1;
	return pathCost;
    }

    // Mutator method
    // Sets path to the list input
    // Performs a shallow copy
    private void setPath(List<String> oldPath) {
	path = new LinkedList<String>(oldPath);
    }

    // Adds the move specified by input into the path list
    public void addPathMove(String move) {
	path.add(move);
    }
    

    // Takes as input int n
    // Randomizes the board by performing n random legal moves
    public void randomize(int n) {
	for ( ; n > 0; n-- ) {
	    // count number of legal moves
	    boolean[] legal = legalMoves();
	    int numLegalMoves = 0;
	    for ( int i = 0; i < 4; i++ ) {
		if ( legal[i] )
		    numLegalMoves += 1;
	    }
	    // random integer from 0 to numLegalMoves-1 inclusive
	    int randInt = (int) (Math.random() * numLegalMoves);
	    
	    // choose a random move from legal moves based on randInt
	    // custom counting method to "skip over" the illegal moves
	    int counter = -1;
	    while (randInt >= 0) {
		counter += 1;
		if ( legal[counter] )
		    randInt -= 1;
	    } // at completion of while loop, counter corresponds to our random move

	    if ( counter == 0 )
		moveUp();
	    else if ( counter == 1 )
		moveDown();
	    else if ( counter == 2 )
		moveLeft();
	    else if ( counter == 3 )
		moveRight();
	}
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
	System.out.println(b1.calculateH1());
	System.out.println(b1.calculateH2());
	/*
	State b1 = new State();
	b1.randomize(100);
	b1.evaluate("h1");
	b1.print();
	System.out.println(b1.calculateH1());
	System.out.println();

	State b2 = new State();
	b2.randomize(100);
	b2.evaluate("h1");
	b2.incrementPathCost();
	b2.print();
	System.out.println(b2.calculateH1());
	System.out.println();

	State b3 = new State();
	b3.randomize(100);
	b3.evaluate("h1");
	b3.print();
	System.out.println(b3.calculateH1());
	
	System.out.println();
	System.out.println();

	State b4 = new State();
	    
	PriorityQueue<State> queue = new PriorityQueue<State>();
	queue.add(b1);
	queue.add(b2);
	queue.add(b3);

	System.out.println(queue.contains(b4));
	
	queue.poll().print(); System.out.println();
	queue.poll().print(); System.out.println();
	queue.poll().print(); System.out.println();
	*/
	
	
	/*
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
	*/

	
    }


}
    
