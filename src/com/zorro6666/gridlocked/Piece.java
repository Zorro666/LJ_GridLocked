package com.zorro6666.gridlocked;

import javax.microedition.khronos.opengles.GL10;

public class Piece 
{
	public Piece()
	{
		m_x = 0;
		m_y = 0;
		m_type = 0;
	}
	public Piece( int x, int y, int type )
	{
		m_x = x;
		m_y = y;
		m_type = type;
	}
	public void draw(GL10 gl, GridLockedRenderer renderer)
	{
		int type = m_type;
		switch (type)
		{
			case 0:
			{
				gl.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );
				break;
			}
			case 1:
			{
				gl.glColor4f( 1.0f, 0.0f, 0.0f, 1.0f );
				break;
			}
			case 2:
			{
				gl.glColor4f( 0.0f, 1.0f, 1.0f, 1.0f );
				break;
			}
			default:
			{
				gl.glColor4f( 0.8f, 0.7f, 0.4f, 1.0f );
				break;
			}
		}
		int x = m_x;
		int y = m_y;
		float width =  ( 1.0f / 8.0f );
		float height = width * ( 3.0f / 4.0f );
		float x0 = x * width;
		float y0 = y * height;
		renderer.drawOutlineRectangle(gl, x0, y0, width, height );
	}
	public int GetX()
	{
		return m_x;
	}
	public int GetY()
	{
		return m_y;
	}
	public int GetType()
	{
		return m_type;
	}
    private int   		m_x;
    private int  		m_y;
    private int  		m_type;
}
