package com.zorro6666.gridlocked;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import java.util.Random;

public class Board 
{
	public Board()
	{
        m_pieces = new ArrayList<Piece>(Board.MAX_NUM_ELEMENTS);
        m_generator = new Random();
        addPiece(0,0,0);
        addPiece(0,1,1);
        addPiece(0,2,2);
	}
    public void addPiece( int x, int y, int type )
    {
    	if ( m_pieces.size() >= (Board.MAX_NUM_ELEMENTS/4) )
   		{
    		m_pieces.remove(0);
  		}
        m_pieces.add( new Piece( x, y, type ) );
	}
	public synchronized void draw( GL10 gl, GridLockedRenderer renderer )
	{
        gl.glFrontFace(GL10.GL_CW);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        int size = m_pieces.size();
		for ( int i = 0; i < size; ++i )
		{
			Piece piece = m_pieces.get(i);
			gl.glPushMatrix();
			piece.draw(gl, renderer);
			gl.glPopMatrix();
		}
	}
	public synchronized void update()
	{
		if ( m_generator.nextInt(100) < 10 )
		{
			int x = m_generator.nextInt(8);
			int y = m_generator.nextInt(8);
			int type = m_generator.nextInt(4);
			addPiece( x, y, type );
		}
	}
    private ArrayList<Piece>	m_pieces;
    static final private int	MAX_NUM_ELEMENTS = (8 * 8);
    private Random				m_generator;
}
