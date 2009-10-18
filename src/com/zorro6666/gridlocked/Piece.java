package com.zorro6666.gridlocked;

public class Piece 
{
	public Piece()
	{
		m_x = 0;
		m_y = 0;
		m_type = 0;
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
