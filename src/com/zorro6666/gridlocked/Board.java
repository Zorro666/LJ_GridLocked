package com.zorro6666.gridlocked;

import javax.microedition.khronos.opengles.GL10;
import java.util.ArrayList;
import java.util.Random;
import android.util.Log;

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
    	if ( m_pieces.size() >= (Board.MAX_NUM_ELEMENTS/1) )
   		{
    		m_pieces.remove(0);
  		}
        m_pieces.add( new Piece( x, y, type ) );
	}
	public void draw( GL10 gl, GridLockedRenderer renderer )
	{
    	//Log.v( TAG,"draw");
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
	public void update()
	{
		if ( m_generator.nextInt(100) < 10 )
		{
			int x = m_generator.nextInt(8);
			int y = m_generator.nextInt(8);
			int type = m_generator.nextInt(4);
			addPiece( x, y, type );
		}
	}
	public void copy( Board otherBoard )
	{
		int oldSize = m_pieces.size();
   		int size = otherBoard.m_pieces.size();
   		for ( int i = 0; i < size; ++i )
   		{
   			Piece piece = otherBoard.m_pieces.get(i);
   			if ( i >= oldSize )
   			{
   				m_pieces.add( new Piece( 0, 0, 0 ) );
   			}
			m_pieces.get(i).copy( piece );
   		}
   		// Remove any pieces that shouldn't be there
   		for ( int i = size; i < oldSize; ++i )
   		{
   			m_pieces.remove(i);
   		}
	}
	
    private ArrayList<Piece>	m_pieces;
    static final private int	MAX_NUM_ELEMENTS = (8 * 8);
    static private Random		m_generator;
    
    private static final String TAG = "BD";
}
