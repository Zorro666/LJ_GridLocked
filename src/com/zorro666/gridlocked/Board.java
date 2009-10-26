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
    	
    	// Create the board
    	for ( int x = 0; x < MAX_NUM_COLUMNS; ++x )
    	{
    		for ( int y = 0; y < MAX_NUM_ROWS; ++y )
    		{
    			m_boardSquares[x][y] = new Piece( x, y );
    		}
    	}
    	
    	randomBoard();
	}
	private void randomBoard()
	{
    	// Make a random board
    	for ( int x = 0; x < MAX_NUM_COLUMNS; ++x )
    	{
    		for ( int y = 0; y < MAX_NUM_ROWS; ++y )
    		{
    			int type = s_generator.nextInt(Piece.MAX_NUM_TYPES);
    			int colour = s_generator.nextInt(Piece.MAX_NUM_COLOURS);
    			addPiece( x, y, type, colour );
    		}
    	}
	}
    public void addPiece( int x, int y, int type, int colour )
    {
    	m_boardSquares[x][y].update( type, colour );
	}
	public void draw( GL10 gl, GridLockedRenderer renderer )
	{
    	//Log.i( TAG,"draw");
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
    				piece.updateCount();
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
    				// Check horizontally for types which match
    				int type = piece.getType();
    				int matchCount = 0;
    				for ( int x2 = x+1; x2 < MAX_NUM_COLUMNS; ++x2 )
    				{
    					Piece newPiece = m_boardSquares[x2][y];
    					if ( newPiece.isActive() )
    					{
    						int newType = newPiece.getType();
    						if ( newType == type )
    						{
    							newPiece.setMatch();
    							matchCount++;
    							continue;
    						}
    					}
    					break;
    				}
    				if ( matchCount > 0 )
    				{
    					piece.setMatch();
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
	}
	public void moveColumn( int column, int direction )
	{
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
	static public int convertToRow( float y, float ratio )
	{
       	// Convert to board co-ordinates
       	int row = (int)( Math.round( y * ((float)MAX_NUM_ROWS/ratio) - 0.5f ) );
       	return row;
	}
	static public int convertToColumn( float x )
	{
       	int column = (int)( Math.round( x * (float)MAX_NUM_COLUMNS - 0.5f ) );
       	return column;
	}
	
    static final public int	MAX_NUM_ROWS = 		8;
    static final public int	MAX_NUM_COLUMNS = 	8;
    static final public int	UP = 				-1;
    static final public int	DOWN =  			+1;
    static final public int	LEFT = 				-1;
    static final public int	RIGHT = 			+1;
    
    private Piece[][]			m_boardSquares;
    static private Random		s_generator;
    
    private static final String TAG = "BD";
}

