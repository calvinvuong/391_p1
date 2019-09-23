// Solves the 8-puzzle using command-line specified input file
// Performs parsing functions
// Delegates artificial intelligence functions to other classes

import java.io.File;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.HashSet;

public class Solve {

    
    public static void main(String[] args) throws Exception{
	// instantiate a puzzle board
	State board = new State();
	board.print();
	System.out.println();
	
	// create File and Scanner objects
	File commands = new File(args[0]);
	Scanner scan = new Scanner(commands);

	// read commands from file line by line
	while (scan.hasNextLine()) {
	    String commandLine = scan.nextLine();
	    // randomizeState command
	    if ( commandLine.toLowerCase().startsWith("randomizestate") ) {
		// get number of steps to randomize to
		int steps = Integer.parseInt(commandLine.substring("randomizeState".length() + 1));
		board.randomize(steps);
	    }
	    else if ( commandLine.toLowerCase().startsWith("solve a-star") ) {
		String heuristic = "h2";
		board.evaluate(heuristic);
		solveAStar(board, heuristic);
	}

	board.print();
	System.out.println();

    }

    // Solves the 8-puzzle using the A* algorithm
    // Returns the number of moves to reach goal, -1 if no solution found
    // Prints out the steps to reach goal
    // Takes as input: the start state, the heuristic to use, etc.
    // TODO: Implement maxnodes
    public static int solveAStar(State startState, String heuristic) {
	// initialize data structures
	PriorityQueue<State> frontier = new PriorityQueue<State>();
	HashSet<State> explored = new HashSet<State>();
	frontier.add(startState);

	// start loop
	while ( ! frontier.isEmpty() ) {
	    State currentState = frotnier.poll();
	    // reached goal
	    if ( currentState.isGoal() ) {
		printMoves(currentState, currentState.getPath());
		return currentState.getPathCost();
	    }
	    // if currentState has already been explored fully, don't bother looking at children
	    if ( !explored.contains(currentState) ) {
		explored.add(currentState);
		
		// generate children state from set of legal moves
		boolean[] legal = currentState.legalMoves();
		for ( int i = 0; i < legal.length; i++ ) {
		    if ( legal[i] == true ) {
			State childState = currentState.duplicate();
			// perform move based on what's legal
			if ( i == 0 ) {
			    childState.moveUp();
			    childState.addPathMove("up");
			}
			else if ( i == 1 ) {
			    childState.moveDown();
			    childState.addPathMove("down");
			}
			else if ( i == 2 ) {
			    childState.moveLeft();
			    childState.addPathMove("left");
			}
			else if ( i == 3 ) {
			    childState.moveRight();
			    childState.addPathMove("right");
			}
			
			// increment path cost
			childState.incrementPathCost();
			// perform evaluation function on child
			childState.evaluate(heuristic);

			if ( !explored.contains(childState) )
			    frontier.add(childState);

			// Note: We don't have to deal with replacing a state already in frontier if childState has a lower cost, because the least costly path will surface up before the more costly path(s)
			
		    }
		}
	    }   
	}
	// frontier is empty at end of while loop
	// no solution
	System.out.println("No solution found.");
	return -1;
    }

    // prints the moves needed to go from start state to current state, given the list of states along the optimal path
    public static void printMoves(State state, List<String> path) {
	System.out.println("Number of moves: " + state.getPathCost());
	System.out.println(path);
    }

	

}
