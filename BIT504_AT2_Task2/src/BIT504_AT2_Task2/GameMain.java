package BIT504_AT2_Task2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;


public class GameMain extends JPanel implements MouseListener{
	//Constants for game 
	public static final int ROWS = 3;     
	public static final int COLS = 3;  
	public static final String TITLE = "Tic Tac Toe";

	//constants for dimensions used for drawing
	//cell width and height
	public static final int CELL_SIZE = 100;
	//drawing canvas
	public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
	public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
	//Noughts and Crosses are displayed inside a cell, with padding from border
	public static final int CELL_PADDING = CELL_SIZE / 6;    
	public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;    
	public static final int SYMBOL_STROKE_WIDTH = 8;
	
	/*declare game object variables*/

	// the game board 
	private Board board;
	 	 
	// Setting the default for the GameState
	private GameState currentState; 
		
	// The current player
	private Player currentPlayer; 
	
	// Displaying game status message
	private JLabel statusBar;       
	
	// Constructor to setup the UI and game components on the panel
	public GameMain() {   
		
		// Event Listener for mouse click          
	    addMouseListener(this);
	    
		// Setup the status bar (JLabel) to display status message       
		statusBar = new JLabel("         ");       
		statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));       
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));       
		statusBar.setOpaque(true);       
		statusBar.setBackground(Color.LIGHT_GRAY);  
		
		//layout of the panel is in border layout
		setLayout(new BorderLayout());       
		add(statusBar, BorderLayout.SOUTH);
		// account for statusBar height in overall height
		setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT + 30));
				
		// New instance of the game "Board"class		
		board = new Board();
		
		//Initialise the game board		
		initGame();

}

	// Main code 
	public static void main(String[] args) {
		    // Run GUI code in Event Dispatch thread for thread safety.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
				//Create a main window to contain the panel
				JFrame frame = new JFrame(TITLE);
				
				//Create the new GameMain panel and add it to the frame
				GameMain gamePanel = new GameMain();
				frame.add(gamePanel);
				
				//Set the default close operation of the frame to exit_on_close
		       	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
				
		       	frame.pack();             
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);

	         	}
		 	});
}

	// Custom painting codes on this JPanel
	public void paintComponent(Graphics g) {
		//fill background and set colour to white
		super.paintComponent(g);
		setBackground(Color.WHITE);
		//ask the game board to paint itself
		board.paint(g);
				
		// Set status bar message
		// Checking Gamestate as different messages
		// Playing
		if (currentState == GameState.Playing) {          
			statusBar.setForeground(Color.BLACK);   // Colour of message       
			if (currentPlayer == Player.Cross) {   // Determining which message to display based on player
				statusBar.setText("It is player X's turn");
			} else {
				statusBar.setText("It is player O's turn");
			}
		// Draw	
		} else if (currentState == GameState.Draw) {          
			statusBar.setForeground(Color.RED);          
			statusBar.setText("It's a Draw! Click to play again."); 
		// Cross win	
		} else if (currentState == GameState.Cross_won) {          
			statusBar.setForeground(Color.RED);          
			statusBar.setText("'X' Won! Click to play again.");  
		// Nought win	
		} else if (currentState == GameState.Nought_won) {          
			statusBar.setForeground(Color.RED);          
			statusBar.setText("'O' Won! Click to play again.");       
		}
}
			
	  	// Starting the game and having the Cross player start
		public void initGame() {
			for (int row = 0; row < ROWS; ++row) {          
				for (int col = 0; col < COLS; ++col) {  
					// all cells empty
					board.cells[row][col].content = Player.Empty;           
				}
			}
			 currentState = GameState.Playing;
			 currentPlayer = Player.Cross;
		}
				
		// Checking the game status after each turn 
		public void updateGame(Player thePlayer, int row, int col) {
			
			//Checking for win after each turn play
			if(board.hasWon(thePlayer, row, col)) {
				if (thePlayer == Player.Nought) {
					currentState = GameState.Nought_won; 	// if Nought is the winning player
				} else {
					currentState = GameState.Cross_won; 	// if Cross is the winning player
				}
				
			// If there is no winner, checking for a draw after each play	
			} else if (board.isDraw ()) {
				currentState = GameState.Draw;

			// If it is neither a win or a draw then play carries on
			} else
				currentState = GameState.Playing;
}
		

	
		/** Event handler for the mouse click on the JPanel. If selected cell is valid and Empty then current player is added to cell content.
		 *  UpdateGame is called which will call the methods to check for winner or Draw. if none then GameState remains playing.
		 *  If win or Draw then call is made to method that resets the game board.  Finally a call is made to refresh the canvas so that new symbol appears*/
	
	public void mouseClicked(MouseEvent e) {  
	    // get the coordinates of where the click event happened            
		int mouseX = e.getX();             
		int mouseY = e.getY();             
		// Get the row and column clicked             
		int rowSelected = mouseY / CELL_SIZE;             
		int colSelected = mouseX / CELL_SIZE;               			
		if (currentState == GameState.Playing) {                
			if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS && board.cells[rowSelected][colSelected].content == Player.Empty) {
				// move  
				board.cells[rowSelected][colSelected].content = currentPlayer; 
				// update currentState                  
				updateGame(currentPlayer, rowSelected, colSelected); 
				// Switch player
				if (currentPlayer == Player.Cross) {
					currentPlayer =  Player.Nought;
				}
				else {
					currentPlayer = Player.Cross;
				}
			}             
		} else {        
			// game over and restart              
			initGame();            
		}   
		
		// Redraw the graphics          
		repaint();
           
}
	
	// Events not used
	
	@Override
	public void mousePressed(MouseEvent e) {
		//  Auto-generated event not used
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		//  Auto-generated event not used
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// Auto-generated event not used
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// Auto-generated event not used
		
	}

}
