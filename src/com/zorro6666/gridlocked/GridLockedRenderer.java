package com.zorro6666.gridlocked;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class GridLockedRenderer implements GLSurfaceView.Renderer 
{
    public GridLockedRenderer(boolean useTranslucentBackground)
    {
        m_translucentBackground = useTranslucentBackground;
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
        
    }
    public void SetBoard( Board board )
    {
    	m_board = board;
    }
    public void onDrawFrame(GL10 gl) 
    {
        /*
         * Usually, the first thing one might want to do is to clear
         * the screen. The most efficient way of doing this is to use
         * glClear().
         */

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        /*
         * Now we're ready to draw some 3D objects
         */

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
		GLU.gluOrtho2D(gl,0.0f, 1.0f, 0.0f, 1.0f);
		
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        //gl.glFrontFace(GL10.GL_CW);
        //gl.glDepthFunc(GL10.GL_LEQUAL);
        //gl.glColor4f( 1.0f, 0.0f, 0.5f, 1.0f );
        //drawOutlineRectangle(gl, 0.1f, 0.1f, 0.3f, 0.3f );
        m_board.draw(gl, this);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) 
    {
         gl.glViewport(0, 0, width, height);

         /*
          * Set our projection matrix. This doesn't have to be done
         * each time we draw, but usually a new projection needs to
          * be set when the viewport is resized.
          */

         float ratio = (float) width / height;
         gl.glMatrixMode(GL10.GL_PROJECTION);
         gl.glLoadIdentity();
         gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) 
    {
        /*
         * By default, OpenGL enables features that improve quality
         * but reduce performance. One might want to tweak that
         * especially on software renderer.
         */
        gl.glDisable(GL10.GL_DITHER);

        /*
         * Some one-time OpenGL initialization can be made here
         * probably based on features of this particular context
         */
         gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

         if (m_translucentBackground) 
         {
             gl.glClearColor(0,0,0,0);
         }
         else 
         {
             gl.glClearColor(1,1,1,1);
         }
         gl.glEnable(GL10.GL_CULL_FACE);
         gl.glShadeModel(GL10.GL_SMOOTH);
         gl.glEnable(GL10.GL_DEPTH_TEST);
         
    }
    public void drawOutlineRectangle(GL10 gl, float x0, float y0, float width, float height)
    {
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_vertexBuffer);
		gl.glTranslatef( x0, y0, 1.0f );
		gl.glScalef( width, height, 1.0f );
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, m_indexBuffer);
			
		gl.glTranslatef( 0.01f, 0.01f, 1.0f );
		gl.glScalef( 0.99f, 0.99f, 1.0f );
		gl.glColor4f(1.0f,0,0.5f,1.0f);
		gl.glDrawElements(GL10.GL_LINE_STRIP, 5, GL10.GL_UNSIGNED_BYTE, m_lineBuffer ); 
    }
    private boolean 		m_translucentBackground;
    private Board 			m_board;
    private IntBuffer   	m_vertexBuffer;
    private ByteBuffer  	m_indexBuffer;
    private ByteBuffer  	m_lineBuffer;
}
