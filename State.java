// Represents the state of an 8-puzzle board

public class State {
    // 2D array representation of the current state
    // 1-8 represent the numbers
    // 0 represents the blank
    private byte[][] board;

    // keeps track of blank position
    private byte[] blank;
    // 2D array representation of the goal state
    private final byte[][] GOAL = { {0, 1, 2},
				   {3, 4, 5},
				   {6, 7, 8} };

    // Constructor
    // Initializes the state to the goal state.
    public State() {
	board = new byte[][] { {0, 1, 2},
			      {3, 4, 5},
			      {6, 7, 8} };
	blank = new byte[] {0, 0};
    }
    

    // swaps the tiles at position x1, y1 and x2, y2
    private void swap(int x1, int y1, int x2, int y2) {
	byte tmp = board[x1][y1];
	board[x1][y1] = board[x2][y2];
	board[x2][y2] = tmp;
    }
    
    public void print() {
	for ( int i = 0; i < board.length; i++ )
	    for ( int j = 0; j < board[i].length; j++ )
		System.out.print(board[i][j] + " ");
	System.out.println();
    }
    
    public static void main(String[] args) {

	State b1 = new State();
	b1.print();

	
	State b2 = new State();
	b2.print();
    }


}
    
