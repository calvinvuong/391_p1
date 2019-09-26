// Contains code for gathering statistic on A* and local beam search algorithms
import java.util.ArrayList;

public class Experiment {

    // experiment 2
    public static void experiment2() {
	//public static void main(String[] args) {
	
	// set up matrix to contain data
	double[][] data = new double[20][4];
	int[] solution;
	
	// initialize n = max nodes
	for ( int i = 0; i < data.length; i++ ) 
	    data[i][0] = (i+1) * 200;

	// initialize data matrix to 0 first
	for ( int i = 0; i < data.length; i++ ) {
	    for ( int j = 1; j < data[i].length; j++ )
		data[i][j] = 0;
	}

	

	
	// run trials
	for ( int limit = 200; limit <= 4000; limit += 200 ) {
	    // perform 200 experiments for each maxNodes limit
	    for ( int i = 0; i < 200; i++ ) {
		// randomize a board
		State board = new State();
		board.randomize(100);

		// solve using A* h1
		solution = Solve.solveAStar(board, "h1", limit);
		if (solution[0] > -1) // search did not fail
		    data[limit / 200 - 1][1] += 1;

		// solve using A* h2
		solution = Solve.solveAStar(board, "h2", limit);
		if (solution[0] > -1) // search did not fail
		    data[limit / 200 - 1][2] += 1;

		// solve using local beam search k = 50
		solution = Solve.solveLocalBeamSearch(board, 50, limit);
		if (solution[0] > -1) // search did not fail
		    data[limit / 200 - 1][3] += 1;
	    }
	}

	// compute fractions
	
	for ( int i = 0; i < data.length; i++ ) {
	    for ( int j = 1; j < data[0].length; j++ )
		data[i][j] = data[i][j] / 200;
	}

	// print data matrix
	for ( int i = 0; i < data.length; i++ ) {
	    for ( int j = 0; j < data[i].length; j++ ) 
		System.out.print(data[i][j] + "\t");
	    System.out.println();
	}
	

    }
    public static void main(String[] args) {
	  //public static void experiment1() {
	// set up matrix to contain data
	Object[][] data = new Object[50][5];
	// solution[0] = numMoves, solution[1] = nodes considered
	int[] solution; 
	// initialize n = number of trials performed to 0
	for ( int i = 0; i < data.length; i++ )
	    data[i][0] = 0;
	// initialize d = number of steps to solution
	for ( int i = 0; i < data.length; i++ )
	    data[i][1] = i;
	// initialize columns for nodes generated to an array of Integers
	for ( int i = 0; i < data.length; i++ ) {
	    for ( int j = 2; j < 5; j++ ) {
		data[i][j] = new ArrayList<Integer>();
	    }
	}
	
	// perform 1000 experiments
	for ( int i = 0; i < 1000; i++ ) {
	    // randomize a board	    
	    State board = new State();
	    board.randomize(100);
	    // solve using A* h1
	    solution = Solve.solveAStar(board, "h1", -1);
	    data[solution[0]][0] = (Integer) data[solution[0]][0] + 1; // n = number of trials
	    ((ArrayList<Integer>) data[solution[0]][2]).add(solution[1]);

	    // solve using A* h2
	    solution = Solve.solveAStar(board, "h2", -1);
	    data[solution[0]][0] = (Integer) data[solution[0]][0] + 1; // n = number of trials
	    ((ArrayList<Integer>) data[solution[0]][3]).add(solution[1]);

	    // solve using local beam search k = 50
	    solution = Solve.solveLocalBeamSearch(board, 50, -1);
	    data[solution[0]][0] = (Integer) data[solution[0]][0] + 1; // n = number of trials
	    ((ArrayList<Integer>) data[solution[0]][4]).add(solution[1]);
	}

	// get averages from data
	for ( int i = 0; i < data.length; i++ ) {
	    for ( int j = 2; j < 5; j++ ) {
		ArrayList<Integer> nodeList = ((ArrayList<Integer>) data[i][j]);
		int avg = 0;
		for ( int k = 0; k < nodeList.size(); k++ ) 
		    avg += nodeList.get(k);
		data[i][j] = (int) ((double) avg / nodeList.size());
	    }
	}

	// print data matrix
	for ( int i = 0; i < data.length; i++ ) {
	    for ( int j = 0; j < data[i].length; j++ ) 
		System.out.print(data[i][j] + "\t");
	    System.out.println();
	}
		    
    }
}
