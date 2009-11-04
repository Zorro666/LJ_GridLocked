package com.zorro666.gridlocked;

import javax.microedition.khronos.opengles.GL10;
import java.util.Random;
import android.util.Log;

public class Board 
{
	public Board()
	{
    	Log.i( TAG,"Construct");
        s_generator = new Random();
    	m_boardSquares = new Piece[MAX_NUM_COLUMNS][MAX_NUM_ROWS];
    	int maxPossibleMatches = Math.max( MAX_NUM_COLUMNS, MAX_NUM_ROWS );
    	m_matchPieces = new Piece[maxPossibleMatches];
    	m_score = 0;
    	
    	// Create the board
    	for ( int x = 0; x < MAX_NUM_COLUMNS; ++x )
    	{
    		for ( int y = 0; y < MAX_NUM_ROWS; ++y )
    		{
    			m_boardSquares[x][y] = new Piece( x, y );
    		}
    	}
    	
    	reset();
	}
	public void reset()
	{
    	m_score = 0;
    	randomBoard();
	}
	private void randomBoard()
	{
    	// Make a random board
    	for ( int x = 0; x < MAX_NUM_COLUMNS; ++x )
    	{
    		for ( int y = 0; y < MAX_NUM_ROWS; ++y )
    		{
    			addRandomPiece( x, y );
    		}
    	}
	}
	private void addRandomPiece( int x, int y )
	{
		int type = s_generator.nextInt(Piece.MAX_NUM_TYPES);
 		int colour = s_generator.nextInt(Piece.MAX_NUM_COLOURS);
   		addPiece( x, y, type, colour );
	}
    public void addPiece( int x, int y, int type, int colour )
    {
    	m_boardSquares[x][y].update( type, colour );
	}
	public void draw( GL10 gl, GridLockedRenderer renderer )
	{
    	//Log.i( TAG,"draw");
		gl.glColor4f( 0.2f, 0.2f, 0.2f, 1.0f );
		final float x0 = 0.1f;
		final float y0 = 0.1f * renderer.getRatio();
		renderer.drawRectangle(gl, x0, y0, 0.8f, 0.8f*renderer.getRatio());
		
		gl.glColor4f( 0.7f, 0.7f, 0.7f, 1.0f );
    	for ( int x = 0; x < MAX_NUM_COLUMNS; ++x )
    	{
    		float xpos = x0 + ( x * 0.8f / (MAX_NUM_COLUMNS) );
    		renderer.drawVerticalLine(gl, xpos, y0, 0.8f*renderer.getRatio());
    	}
   		renderer.drawVerticalLine(gl, 0.9f, y0, 0.8f*renderer.getRatio());
   		
   		for ( int y = 0; y < MAX_NUM_ROWS; ++y )
   		{
   			float ypos = y0 + ( y * 0.8f * renderer.getRatio() / (MAX_NUM_ROWS) );
   			renderer.drawHorizontalLine(gl, x0, ypos, 0.8f);
   		}
		renderer.drawHorizontalLine(gl, x0, 0.9f*renderer.getRatio(), 0.8f);
    		
    	for ( int x = 0; x < MAX_NUM_COLUMNS; ++x )
    	{
    		for ( int y = 0; y < MAX_NUM_ROWS; ++y )
    		{
    			Piece piece = m_boardSquares[x][y];
    			gl.glPushMatrix();
    			piece.draw(gl, renderer);
    			gl.glPopMatrix();
    		}
    	}
	}
	public void update()
	{
		if ( s_generator.nextInt(100) < 0 )
		{
			randomTesting();
		}
		// Clear out the matches from the previous frame
    	for ( int x = 0; x < MAX_NUM_COLUMNS; ++x )
    	{
    		for ( int y = 0; y < MAX_NUM_ROWS; ++y )
    		{
    			Piece piece = m_boardSquares[x][y];
    			if ( piece.isMatch() )
    			{
    				if ( piece.updateCount() == true )
    				{
    					// Some scoring happened
    					addScore( 10 );
    				}
    			}
    		}
    	}
		// Look over the board and find pieces which match
    	for ( int x = 0; x < MAX_NUM_COLUMNS; ++x )
    	{
    		for ( int y = 0; y < MAX_NUM_ROWS; ++y )
    		{
    			Piece piece = m_boardSquares[x][y];
    			if ( piece.isActive() )
    			{
    				int matchCount;
    				
    				// Check horizontally for types which match
    				int type = piece.getType();
    				matchCount = 0;
    				for ( int x2 = x+1; x2 < MAX_NUM_COLUMNS; ++x2 )
    				{
    					Piece newPiece = m_boardSquares[x2][y];
    					if ( newPiece.isActive() )
    					{
    						int newType = newPiece.getType();
    						if ( newType == type )
    						{
    							m_matchPieces[matchCount] = newPiece;
    							matchCount++;
    							continue;
    						}
    					}
    					x2 = MAX_NUM_COLUMNS;
    					break;
    				}
    				// 3 or more in a row
    				if ( matchCount > 1 )
    				{
    					piece.setMatch();
    					for ( int p = 0; p < matchCount; ++p )
    					{
    						m_matchPieces[p].setMatch();
    					}
    				}
    				
    				// Check vertically for types which match
    				int colour = piece.getColour();
    				matchCount = 0;
    				for ( int y2 = y+1; y2 < MAX_NUM_ROWS; ++y2 )
    				{
    					Piece newPiece = m_boardSquares[x][y2];
    					if ( newPiece.isActive() )
    					{
    						int newColour = newPiece.getColour();
    						if ( newColour == colour )
    						{
    							m_matchPieces[matchCount] = newPiece;
    							matchCount++;
    							continue;
    						}
    					}
    					y2 = MAX_NUM_ROWS;
    					break;
    				}
    				// 3 or more in a column
    				if ( matchCount > 1 )
    				{
    					piece.setMatch();
    					for ( int p = 0; p < matchCount; ++p )
    					{
    						m_matchPieces[p].setMatch();
    					}
    				}
    			}
    		}
    	}
	}
	private void randomTesting()
	{
		if ( s_generator.nextInt(100) < 10 )
		{
			// Move row or column
			if ( s_generator.nextInt(100) > 50 )
			{
				// Piece a random row and move it
				int row = s_generator.nextInt(MAX_NUM_ROWS);
				if ( s_generator.nextInt(100) > 100 )
				{
					moveRow( row, RIGHT );
				}
				else
				{
					moveRow( row, LEFT );
				}
			}
			else
			{
				// Piece a random column and move it
				int column = s_generator.nextInt(MAX_NUM_COLUMNS);
				if ( s_generator.nextInt(100) > 50 )
				{
					moveColumn( column, UP );
				}
				else
				{
					moveColumn( column, DOWN );
				}
			}
		}
	}
	public void moveRow( int row, int direction )
	{
		if ( row < 0 ) row = 0;
		if ( row >= Board.MAX_NUM_ROWS ) row = Board.MAX_NUM_ROWS - 1;
		
		int xStart = 0;
		int xEnd = 0;
		int xIncrement = 0;
		
		if ( direction == LEFT )
		{
			xStart = 0;
			xEnd = MAX_NUM_COLUMNS-1;
			xIncrement = +1;
		}
		else if ( direction == RIGHT )
		{
			xStart = MAX_NUM_COLUMNS-1;
			xEnd = 0;
			xIncrement = -1;
		}
		int x = xStart;
		while ( x != xEnd )
    	{
			int xNew = x + xIncrement;
   			Piece piece = m_boardSquares[x][row];
   			Piece pieceNew = m_boardSquares[xNew][row];
   			piece.update( pieceNew );
   			x = xNew;
    	}
   		m_boardSquares[xEnd][row].empty();
   		// Add a new random piece in the new place
   		addRandomPiece( xEnd, row );
	}
	public void moveColumn( int column, int direction )
	{
		if ( column < 0 ) column = 0;
		if ( column >= Board.MAX_NUM_COLUMNS ) column = Board.MAX_NUM_COLUMNS - 1;
		
		int yStart = 0;
		int yEnd = 0;
		int yIncrement = 0;
		
		if ( direction == UP )
		{
			yStart = 0;
			yEnd = MAX_NUM_ROWS-1;
			yIncrement = +1;
		}
		else if ( direction == DOWN )
		{
			yStart = MAX_NUM_ROWS-1;
			yEnd = 0;
			yIncrement = -1;
		}
		int y = yStart;
		while ( y != yEnd )
    	{
			int yNew = y + yIncrement;
   			Piece piece = m_boardSquares[column][y];
   			Piece pieceNew = m_boardSquares[column][yNew];
   			piece.update( pieceNew );
   			y = yNew;
    	}
   		m_boardSquares[column][yEnd].empty();
   		// Add a new random piece in the new place
   		addRandomPiece( column, yEnd );
	}
	public void copy( Board otherBoard )
	{
    	for ( int x = 0; x < MAX_NUM_COLUMNS; ++x )
    	{
    		for ( int y = 0; y < MAX_NUM_ROWS; ++y )
    		{
    			Piece piece = otherBoard.m_boardSquares[x][y];
    			m_boardSquares[x][y].copy( piece );
    		}
   		}
	}
	public int getScore()
	{
		return m_score;
	}
	private void addScore( int delta )
	{
		m_score += delta;
	}
	static public int convertToRow( float y, float ratio )
	{
		if ( y < 0.1f )
		{
			return -1;
		}
		if ( y > 0.9f )
		{
			return MAX_NUM_ROWS;
		}
			
       	// Convert to board co-ordinates
       	int row = (int)( Math.round( ( ( y - 0.1f ) / 0.8f ) * ((float)MAX_NUM_ROWS/ratio) - 0.5f ) );
       	return row;
	}
	static public int convertToColumn( float x )
	{
		if ( x < 0.1f )
		{
			return -1;
		}
		if ( x > 0.9f )
		{
			return MAX_NUM_COLUMNS;
		}
       	int column = (int)( Math.round( ( ( x - 0.1f ) / 0.8f ) * (float)MAX_NUM_COLUMNS - 0.5f ) );
       	return column;
	}
	
    static final public int	MAX_NUM_ROWS = 		8;
    static final public int	MAX_NUM_COLUMNS = 	8;
    static final public int	UP = 				-1;
    static final public int	DOWN =  			+1;
    static final public int	LEFT = 				-1;
    static final public int	RIGHT = 			+1;
    
    static private Random		s_generator;
    
    private Piece[][]			m_boardSquares;
    private Piece[]				m_matchPieces;
    private int					m_score;
    
    private static final String TAG = "BD";
}

