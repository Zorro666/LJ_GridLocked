package com.zorro6666.gridlocked;

import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class Board 
{
	public Board()
	{
        int one = 0x10000;
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
	}
	public void draw( GL10 gl )
	{
        gl.glFrontFace(GL10.GL_CW);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_vertexBuffer);
        int size = 1;
		for ( int i = 0; i < size; ++i )
		{
			gl.glPushMatrix();
			gl.glScalef( 0.2f, 0.2f, 1.0f );
			
			gl.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );
			gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, m_indexBuffer);
			
			gl.glTranslatef( 0.01f, 0.01f, 1.0f );
			gl.glScalef( 0.99f, 0.99f, 1.0f );
			gl.glColor4f(1.0f,0,0.5f,1.0f);
			gl.glDrawElements(GL10.GL_LINE_STRIP, 5, GL10.GL_UNSIGNED_BYTE, m_lineBuffer ); 
			
			gl.glPopMatrix();
		}
	}
    private IntBuffer   m_vertexBuffer;
    private ByteBuffer  m_indexBuffer;
    private ByteBuffer  m_lineBuffer;
}
