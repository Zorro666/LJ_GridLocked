package com.zorro6666.gridlocked;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class Board 
{
	public Board()
	{
        final int one = 0x10000;
        int vertices[] = {
                0, 0, 0,
                0,  one, 0,
                one, 0, 0,
                one, one, 0,
        };
        byte indices[] = { 0, 1, 2, 3 };
        byte lineIndices[] = { 0, 1, 3, 2, 0 };

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        m_vertexBuffer = vbb.asIntBuffer();
        m_vertexBuffer.put(vertices);
        m_vertexBuffer.position(0);
        
        m_indexBuffer = ByteBuffer.allocateDirect(indices.length);
        m_indexBuffer.put(indices);
        m_indexBuffer.position(0);
        
        m_lineBuffer = ByteBuffer.allocateDirect(lineIndices.length);
        m_lineBuffer.put(lineIndices);
        m_lineBuffer.position(0);
        
        m_pieces = new ArrayList(Board.MAX_NUM_ELEMENTS);
	}
	public void draw( GL10 gl )
	{
        gl.glFrontFace(GL10.GL_CW);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_vertexBuffer);
        int size = m_pieces.size();
		for ( int i = 0; i < size; ++i )
		{
			Piece piece = m_pieces.get(i);
			drawPiece(gl, piece);
		}
	}
	private void drawPiece(GL10 gl, Piece piece)
	{
		gl.glPushMatrix();
		
		int type = piece.GetType();
		if ( type == 1 )
		{
			gl.glColor4f( 1.0f, 0.0f, 0.0f, 1.0f );
		}
		else
		{
			gl.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );
		}
		int x = piece.GetX();
		int y = piece.GetY();
		gl.glTranslatef( x * 0.2f, y * 0.2f, 1.0f );
		gl.glScalef( 0.2f, 0.2f, 1.0f );
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, m_indexBuffer);
			
		gl.glTranslatef( 0.01f, 0.01f, 1.0f );
		gl.glScalef( 0.99f, 0.99f, 1.0f );
		gl.glColor4f(1.0f,0,0.5f,1.0f);
		gl.glDrawElements(GL10.GL_LINE_STRIP, 5, GL10.GL_UNSIGNED_BYTE, m_lineBuffer ); 
		
		gl.glPopMatrix();
	}
    private IntBuffer   		m_vertexBuffer;
    private ByteBuffer  		m_indexBuffer;
    private ByteBuffer  		m_lineBuffer;
    private ArrayList<Piece>	m_pieces;
    static final private int			MAX_NUM_ELEMENTS = (8 * 8);
}
