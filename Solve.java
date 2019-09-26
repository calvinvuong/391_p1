// Solves the 8-puzzle using command-line specified input file
// Performs parsing functions
// Delegates artificial intelligence functions to other classes

import java.io.File;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.List;

public class Solve {

    
    public static void main(String[] args) throws Exception{
	// instantiate a puzzle board
	State board = new State();
	int maxNodes = -1; // default no max
	
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

	    // set stae command
	    else if ( commandLine.toLowerCase().startsWith("setstate") ) {
		// get the parameter
		String inputState = commandLine.substring("setState".length() + 1);
		// popoulate 2D array
		byte[][] inputBoard = new byte[3][3];
		int counter = 0; 
		for ( int i = 0; i < inputState.length(); i++ ) {
		    char c = inputState.charAt(i);
		    if ( c == 'b' ) {
			inputBoard[counter/3][counter%3] = 0;
			counter += 1;
		    }
		    else if ( c != ' ' ) {
			inputBoard[counter/3][counter%3] = (byte) (Character.getNumericValue(c));
			counter += 1;
		    }
		}
		board = new State(inputBoard);
		//board.print();
	    }
	    
	    else if ( commandLine.toLowerCase().startsWith("solve a-star") ) {
		String heuristic = commandLine.substring("solve a-star".length() + 1);
		board.evaluate(heuristic);
		int[] solution = solveAStar(board, heuristic, maxNodes);
	    }

	    else if ( commandLine.toLowerCase().startsWith("solve beam") ) {
		int k = Integer.parseInt(commandLine.substring("solve beam".length() + 1));
		board.evaluate("h2");
		int[] solution = solveLocalBeamSearch(board, k, maxNodes); 
	    }

	    else if ( commandLine.toLowerCase().startsWith("maxnodes") )
		maxNodes = Integer.parseInt(commandLine.substring("maxnodes".length() + 1));
		
	    else if ( commandLine.toLowerCase().startsWith("printstate") )
		board.print();
	    
	    else if ( commandLine.toLowerCase().startsWith("move") ) {
		String direction = commandLine.substring("move".length() + 1);
		if ( direction.equals("up") )
		    board.moveUp();
		else if ( direction.equals("down") )
		    board.moveDown();
		else if ( direction.equals("left") )
		    board.moveLeft();
		else if ( direction.equals("right") )
		    board.moveRight();
	    }
	    
	}

	System.out.println();
	
    }

    // Solves the 8-puzzle using the A* algorithm
    // Returns an int[] array
    // First returned element is the number of moves to reach goal, -1 if no solution found
    // Second returned element is the number of nodes considered during search
    // Prints out the steps to reach goal
    // Takes as input: the start state, the heuristic to use, maximum number of nodes to consider
    // TODO: Implement maxnodes
    public static int[] solveAStar(State startState, String heuristic, int maxNodes) {
	// initialize data structures
	PriorityQueue<State> frontier = new PriorityQueue<State>();
	HashSet<State> explored = new HashSet<State>();
	int numStatesSeen = 0;
	
	frontier.add(startState);

	// start loop
	while ( ! frontier.isEmpty() ) {
	    // if max nodes limit has been reached, stop search
	    if ( maxNodes > -1 && numStatesSeen > maxNodes ) {
		System.out.println("Reach maximum number of nodes: " + maxNodes);
		return new int[] {-1, numStatesSeen};
	    }
	    State currentState = frontier.poll(); // remove from queue
	    // reached goal
	    if ( currentState.isGoal() ) {
		printMoves(currentState, currentState.getPath());
		return new int[] {currentState.getPathCost(), numStatesSeen};
	    }
	    // if currentState has already been explored fully, don't bother looking at children
	    if ( !explored.contains(currentState) ) {
		explored.add(currentState);		
		// generate child states and add to the frontier queue
		numStatesSeen += processChildren(currentState, heuristic, frontier, explored);
	    }   
	}
	// frontier is empty at end of while loop
	// no solution
	System.out.println("No solution found.");
	return new int[] {-1, numStatesSeen};
    }


    // Solves the 8-puzzle using a local beam search that keeps track of k states
    // Returns an int[] array of two values
    // First returned value is the number of moves to reach goal, -1 if no solution found
    // Second returned value is the number of nodes considered during search
    // Prints out the steps to reach goal
    // Starts from input startState
    // Uses heuristic h2
    // TODO: Implement maxnodes
    public static int[] solveLocalBeamSearch(State startState, int k, int maxNodes) {
	// initialize data structures
	PriorityQueue<State> beam = new PriorityQueue<State>();
	HashSet<State> explored = new HashSet<State>();
	// set of all children generated from states in the beam
	// implemented as a priority queue, so we can poll() k times to get the k best children
	PriorityQueue<State> childSet = new PriorityQueue<State>();
	int counter = 0; // counts from 0 to k
	int numStatesSeen = 0;
	
	beam.add(startState);

	// start loop
	// terminate search when the beam is empty after children are added onto beam
	while ( ! beam.isEmpty() ) {
	    // reset childSet and counter
	    childSet.clear();
	    counter = 0;

	    // if max nodes limit has been reached, stop search
	    if ( maxNodes > -1 && numStatesSeen > maxNodes ) {
		System.out.println("Reach maximum number of nodes: " + maxNodes);
		return new int[] {-1, numStatesSeen};
	    }
	    
	    // generate children state for each state currently in the beam
	    while ( ! beam.isEmpty() ) {
		State currentState = beam.poll(); // remove from queue
		// check if reached goal
		if ( currentState.isGoal() ) {
		    printMoves(currentState, currentState.getPath());
		    return new int[] {currentState.getPathCost(), numStatesSeen};
		}
		
		explored.add(currentState);
		// add each child state to childSet as a "temporary" holder
		numStatesSeen += processChildren(currentState, "h2", childSet, explored);
	    } 

	    // reset beam
	    beam.clear();
	    // fill beam with k best states from childSet
	    while ( !childSet.isEmpty() && counter < k ) {
		// get the best child state from childSet and remove it
		State bestChild = childSet.poll();
		// do not add child to beam if already explored
		if ( !explored.contains(bestChild) ) {
		    beam.add(bestChild);
		    counter += 1;
		}
	    }
	}

	// goal not found
	System.out.println("Goal not found.");
	return new int[] {-1, numStatesSeen};
    }

    // Takes as input a state, a heuristic, and a queue, and a hashset
    // Generates all the successor states of the input state and adds
    // Adds the successor states into the queue if not already in hashset
    // Returns the number of children spawned that have not been seen before
    public static int processChildren(State currentState, String heuristic, PriorityQueue<State> queue, HashSet<State> set) {
	int newChildren = 0;
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

		if ( !set.contains(childState) ) {
		    queue.add(childState);
		    newChildren += 1;
		}
		// Note: We don't have to deal with replacing a state already in frontier if childState has a lower cost, because the least costly path will surface up before the more costly path(s).
	    }
	}
	return newChildren;
    }

    // Prints the moves needed to go from start state to current state, given the list of states along the optimal path
    public static void printMoves(State state, List<String> path) {
	System.out.println("Number of moves: " + state.getPathCost());
	System.out.println(path);
    }

	

}
