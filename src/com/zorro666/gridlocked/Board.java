package com.zorro666.gridlocked;

import javax.microedition.khronos.opengles.GL10;
import java.util.Random;
import android.util.Log;

public class Board 
{
	public Board()
	{
    	Log.i( TAG,"Construct");
        m_generator = new Random();
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
    			int type = m_generator.nextInt(Piece.MAX_NUM_TYPES);
    			int colour = m_generator.nextInt(Piece.MAX_NUM_COLOURS);
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
   		gl.glFrontFace(GL10.GL_CW);
   		gl.glDepthFunc(GL10.GL_LEQUAL);
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
		if ( m_generator.nextInt(100) < 1 )
		{
/*
			int x = m_generator.nextInt(MAX_NUM_COLUMNS);
			int y = m_generator.nextInt(MAX_NUM_ROWS);
			int type = m_generator.nextInt(Piece.MAX_NUM_TYPES);
			int colour = m_generator.nextInt(Piece.MAX_NUM_COLOURS);
			addPiece( x, y, type, colour );
*/
			// Move row or column
			if ( m_generator.nextInt(100) > 50 )
			{
				// Piece a random row and move it
				int row = m_generator.nextInt(MAX_NUM_ROWS);
				if ( m_generator.nextInt(100) > 100 )
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
				int column = m_generator.nextInt(MAX_NUM_COLUMNS);
				if ( m_generator.nextInt(100) > 50 )
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
	private void moveRow( int row, int direction )
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
	private void moveColumn( int column, int direction )
	{
		int yStart = 0;
		int yEnd = 0;
		int yIncrement = 0;
		
		if ( direction == DOWN )
		{
			yStart = 0;
			yEnd = MAX_NUM_ROWS-1;
			yIncrement = +1;
		}
		else if ( direction == UP )
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
	
    static final private int	MAX_NUM_ROWS = 		8;
    static final private int	MAX_NUM_COLUMNS = 	8;
    static final private int	UP = 		-1;
    static final private int	DOWN =  	+1;
    static final private int	LEFT = 		-1;
    static final private int	RIGHT = 	+1;
    
    private Piece[][]			m_boardSquares;
    static private Random		m_generator;
    
    private static final String TAG = "BD";
}

