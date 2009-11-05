package com.zorro666.gridlocked;

import javax.microedition.khronos.opengles.GL10;

public class Piece 
{
	public Piece()
	{
		m_x = 0;
		m_y = 0;
		m_type = EMPTY;
		m_colour = EMPTY;
		m_match = false;
		m_count = 0;
	}
	public Piece( int x, int y, int type, int colour )
	{
		m_x = x;
		m_y = y;
		m_type = type;
		m_colour = colour;
		m_match = false;
		m_count = 0;
	}
	public Piece( int x, int y )
	{
		m_x = x;
		m_y = y;
		m_type = EMPTY;
		m_colour = EMPTY;
		m_match = false;
		m_count = 0;
	}
	public void empty()
	{
		update(EMPTY,EMPTY);
	}
	public boolean isActive()
	{
		return ( m_type != EMPTY );
	}
	public boolean isMatch()
	{
		return ( ( m_type != EMPTY ) && ( m_match == true ) );
	}
	public int getType()
	{
		return m_type;
	}
	public int getColour()
	{
		return m_colour;
	}
	public int getCount()
	{
		return m_count;
	}
	public boolean updateCount()
	{
		m_count--;
		if ( m_count < 0 )
		{
			empty();
			return true;
		}
		return false;
	}
	public void update( int type, int colour )
	{
 		if ( type == Piece.EMPTY )
 		{
 			colour = Piece.EMPTY;
 		}
 		if ( colour == Piece.EMPTY )
 		{
 			type = Piece.EMPTY;
 		}
		m_type = type;
		m_colour = colour;
		m_match = false;
	}
	public void update( Piece otherPiece )
	{
		m_type = otherPiece.m_type;
		m_colour = otherPiece.m_colour;
	}
	public void draw(GL10 gl, GridLockedRenderer renderer)
	{
		int colour = m_colour;
		switch (colour)
		{
			case EMPTY:
			{
				gl.glColor4f( 1.0f, 0.5f, 0.8f, 1.0f );
				return;
			}
			case COLOUR_WHITE:
			{
				gl.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );
				break;
			}
			case COLOUR_RED:
			{
				gl.glColor4f( 1.0f, 0.0f, 0.0f, 1.0f );
				break;
			}
			case COLOUR_BLUE:
			{
				gl.glColor4f( 0.0f, 0.0f, 1.0f, 1.0f );
				break;
			}
			case COLOUR_YELLOW:
			{
				gl.glColor4f( 1.0f, 1.0f, 0.0f, 1.0f );
				break;
			}
			case COLOUR_GREEN:
			{
				gl.glColor4f( 0.0f, 1.0f, 0.0f, 1.0f );
				break;
			}
			default:
			{
				return;
			}
		}
		// Flash it
		if ( m_match )
		{
			if ( ( m_count % FLASH_COUNT_PERIOD ) > FLASH_COUNT_PERIOD/2 )
			{
				gl.glColor4f( 1.0f, 0.5f, 0.8f, 1.0f );
			}
		}
		final float x0 = Board.X_ORIGIN;
		final float y0 = Board.Y_ORIGIN;
		final float canvasWidth = Board.WIDTH;
		
		int x = m_x;
		int y = m_y;
		
		float ratio = renderer.getRatio();
		
		float width =  ( canvasWidth / Board.MAX_NUM_COLUMNS );
		float height = width;
		
		float xpos = x0 + x * width;
		float ypos = ( y0 + y * height );
		xpos += width * Board.PIECE_OFFSET;
		ypos += height * Board.PIECE_OFFSET;
		
		ypos *= ratio;
		height *= ratio;
		
		width *= Board.PIECE_SCALE;
		height *= Board.PIECE_SCALE;
		
		int type = m_type;
		switch ( type )
		{
			case EMPTY:
			{
				return;
			}
			case TYPE_RECTANGLE:
			{
				renderer.drawOutlineRectangle(gl, xpos, ypos, width, height );
				break;
			}
			case TYPE_TRIANGLE:
			{
				renderer.drawOutlineTriangle(gl, xpos, ypos, width, height );
				break;
			}
			case TYPE_HEXAGON:
			{
				renderer.drawOutlineHexagon(gl, xpos, ypos, width, height );
				break;
			}
			default:
			{
				return;
			}
		}
	}
	public void copy( Piece otherPiece )
	{
		m_x = otherPiece.m_x;
		m_y = otherPiece.m_y;
		m_type = otherPiece.m_type;
		m_colour = otherPiece.m_colour;
		m_match = otherPiece.m_match;
		m_count = otherPiece.m_count;
	}
	public void setMatch()
	{
		if ( m_match == false )
		{
			m_match = true;
			m_count = FLASH_COUNT_LENGTH;
		}
	}
	
	static public final int EMPTY = 			0;
	
	static public final int TYPE_RECTANGLE =	1;
	static public final int TYPE_TRIANGLE = 	2;
	static public final int TYPE_HEXAGON = 		3;
	static public final int MAX_NUM_TYPES = 	4;
	
	static public final int COLOUR_WHITE =		1;
	static public final int COLOUR_RED = 		2;
	static public final int COLOUR_BLUE = 		3;
	static public final int COLOUR_YELLOW =  	4;
	static public final int COLOUR_GREEN = 		5;
	static public final int MAX_NUM_COLOURS =	6;
	
	static private final int FLASH_COUNT_PERIOD =	16;
	static private final int FLASH_COUNT_LENGTH =	FLASH_COUNT_PERIOD * 5;
		
    private int   		m_x;
    private int  		m_y;
    private int  		m_type;
    private int			m_colour;
    private boolean		m_match;
    private int			m_count;
}
