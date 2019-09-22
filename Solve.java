// Solves the 8-puzzle using command-line specified input file
// Performs parsing functions
// Delegates artificial intelligence functions to other classes

import java.io.File;
import java.util.Scanner;

public class Solve {

    private static State board;
    
    public static void main(String[] args) throws Exception{
	// instantiate a puzzle board
	board = new State();
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
	}

	board.print();
	System.out.println();

    }
	

}
