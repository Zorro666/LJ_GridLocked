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
	}
	public Piece( int x, int y, int type, int colour )
	{
		m_x = x;
		m_y = y;
		m_type = type;
		m_colour = colour;
	}
	public Piece( int x, int y )
	{
		m_x = x;
		m_y = y;
		m_type = EMPTY;
		m_colour = EMPTY;
	}
	public void empty()
	{
		update(EMPTY,EMPTY);
	}
	public void update( int type, int colour )
	{
		m_type = type;
		m_colour = colour;
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
		int x = m_x;
		int y = m_y;
		float ratio = renderer.getRatio();
		float width =  ( 1.0f / 8.0f );
		float height = width * ratio;
		float x0 = x * width;
		float y0 = y * height;
		
		int type = m_type;
		switch ( type )
		{
			case EMPTY:
			{
				return;
			}
			case TYPE_RECTANGLE:
			{
				renderer.drawOutlineRectangle(gl, x0, y0, width, height );
				break;
			}
			case TYPE_TRIANGLE:
			{
				renderer.drawOutlineTriangle(gl, x0, y0, width, height );
				break;
			}
			case TYPE_HEXAGON:
			{
				renderer.drawOutlineHexagon(gl, x0, y0, width, height );
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
	
    private int   		m_x;
    private int  		m_y;
    private int  		m_type;
    private int			m_colour;
}
