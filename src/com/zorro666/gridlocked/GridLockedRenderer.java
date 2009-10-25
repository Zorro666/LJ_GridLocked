package com.zorro666.gridlocked;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class GridLockedRenderer implements GLSurfaceView.Renderer 
{
    public GridLockedRenderer(boolean useTranslucentBackground, GridLockedMain mainThread)
    {
    	Log.i( TAG,"Construct");
    	
        m_translucentBackground = useTranslucentBackground;
        m_mainThread = mainThread;
        
        final int zero = 		0x00000;
        final int one = 		0x10000;
        final int half = 		one/2;
        final int onethird =	0x10000/3;
        final int twothird =	onethird*2;
        
        {
        	int rectangleVertices[] = 	{
        			zero, 	zero, 	zero,
        			zero,  	one, 	zero,
        			one, 	zero, 	zero,
        			one, 	one, 	zero,
        								};
        	byte rectangleIndices[] = { 0, 1, 2, 3 };
        	byte rectangleLineIndices[] = { 0, 1, 3, 2, 0 };
        	
        	ByteBuffer vbb = ByteBuffer.allocateDirect(rectangleVertices.length*4);
        	vbb.order(ByteOrder.nativeOrder());
        	m_rectangleVertexBuffer = vbb.asIntBuffer();
        	m_rectangleVertexBuffer.put(rectangleVertices);
        	m_rectangleVertexBuffer.position(0);
        	
        	m_rectangleIndexBuffer = ByteBuffer.allocateDirect(rectangleIndices.length);
        	m_rectangleIndexBuffer.put(rectangleIndices);
        	m_rectangleIndexBuffer.position(0);
        	
        	m_rectangleLineBuffer = ByteBuffer.allocateDirect(rectangleLineIndices.length);
        	m_rectangleLineBuffer.put(rectangleLineIndices);
        	m_rectangleLineBuffer.position(0);
        }
        
        {
        	int triangleVertices[] = 	{
        			zero, 	zero, 	zero,
        			half,	one,	zero,
        			one,	zero,	zero,
        								};
        	byte triangleIndices[] = { 0, 1, 2 };
        	byte triangleLineIndices[] = { 0, 1, 2, 0 };
        	
        	ByteBuffer vbb = ByteBuffer.allocateDirect(triangleVertices.length*4);
        	vbb.order(ByteOrder.nativeOrder());
        	m_triangleVertexBuffer = vbb.asIntBuffer();
        	m_triangleVertexBuffer.put(triangleVertices);
        	m_triangleVertexBuffer.position(0);
        	
        	m_triangleIndexBuffer = ByteBuffer.allocateDirect(triangleIndices.length);
        	m_triangleIndexBuffer.put(triangleIndices);
        	m_triangleIndexBuffer.position(0);
        	
        	m_triangleLineBuffer = ByteBuffer.allocateDirect(triangleLineIndices.length);
        	m_triangleLineBuffer.put(triangleLineIndices);
        	m_triangleLineBuffer.position(0);
        }
        {
        	int hexagonVertices[] = 	{
        			half,		half,	zero,
        			zero,		half,	zero,
        			onethird, 	one, 	zero,
        			twothird, 	one, 	zero,
        			one, 		half, 	zero,
        			twothird, 	zero, 	zero,
        			onethird, 	zero, 	zero,
        								};
        	byte hexagonIndices[] = { 0, 1, 2, 3, 4, 5, 6, 1 };
        	byte hexagonLineIndices[] = { 1, 2, 3, 4, 5, 6, 1 };
        	
        	ByteBuffer vbb = ByteBuffer.allocateDirect(hexagonVertices.length*4);
        	vbb.order(ByteOrder.nativeOrder());
        	m_hexagonVertexBuffer = vbb.asIntBuffer();
        	m_hexagonVertexBuffer.put(hexagonVertices);
        	m_hexagonVertexBuffer.position(0);
        	
        	m_hexagonIndexBuffer = ByteBuffer.allocateDirect(hexagonIndices.length);
        	m_hexagonIndexBuffer.put(hexagonIndices);
        	m_hexagonIndexBuffer.position(0);
        	
        	m_hexagonLineBuffer = ByteBuffer.allocateDirect(hexagonLineIndices.length);
        	m_hexagonLineBuffer.put(hexagonLineIndices);
        	m_hexagonLineBuffer.position(0);
        }
    }
    public void onDrawFrame(GL10 gl) 
    {
    	//Log.i( TAG,"onDrawFrame");
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
        Board board = m_mainThread.GetRenderBoard();
        synchronized( board)
        {
        	board.draw(gl, this);
        }

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) 
    {
    	Log.i( TAG,"onSurfaceChanged");
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
    	Log.i( TAG,"onSurfaceCreated");
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
    public void drawOutlineTriangle(GL10 gl, float x0, float y0, float width, float height)
    {
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_triangleVertexBuffer);
		gl.glTranslatef( x0, y0, 1.0f );
		gl.glScalef( width, height, 1.0f );
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 3, GL10.GL_UNSIGNED_BYTE, m_triangleIndexBuffer);
			
		gl.glTranslatef( 0.01f, 0.01f, 1.0f );
		gl.glScalef( 0.99f, 0.99f, 1.0f );
		gl.glColor4f(1.0f,0,0.5f,1.0f);
		gl.glDrawElements(GL10.GL_LINE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, m_triangleLineBuffer ); 
    }
    public void drawOutlineHexagon(GL10 gl, float x0, float y0, float width, float height)
    {
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_hexagonVertexBuffer);
		gl.glTranslatef( x0, y0, 1.0f );
		gl.glScalef( width, height, 1.0f );
		gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 8, GL10.GL_UNSIGNED_BYTE, m_hexagonIndexBuffer);
			
		gl.glTranslatef( 0.01f, 0.01f, 1.0f );
		gl.glScalef( 0.99f, 0.99f, 1.0f );
		gl.glColor4f(1.0f,0,0.5f,1.0f);
		gl.glDrawElements(GL10.GL_LINE_STRIP, 7, GL10.GL_UNSIGNED_BYTE, m_hexagonLineBuffer ); 
    }
    public void drawOutlineRectangle(GL10 gl, float x0, float y0, float width, float height)
    {
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_rectangleVertexBuffer);
		gl.glTranslatef( x0, y0, 1.0f );
		gl.glScalef( width, height, 1.0f );
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, m_rectangleIndexBuffer);
			
		gl.glTranslatef( 0.01f, 0.01f, 1.0f );
		gl.glScalef( 0.99f, 0.99f, 1.0f );
		gl.glColor4f(1.0f,0,0.5f,1.0f);
		gl.glDrawElements(GL10.GL_LINE_STRIP, 5, GL10.GL_UNSIGNED_BYTE, m_rectangleLineBuffer ); 
    }
    private boolean 		m_translucentBackground;
    private GridLockedMain 	m_mainThread;
    
    private IntBuffer   	m_rectangleVertexBuffer;
    private ByteBuffer  	m_rectangleIndexBuffer;
    private ByteBuffer  	m_rectangleLineBuffer;
    
    private IntBuffer   	m_triangleVertexBuffer;
    private ByteBuffer  	m_triangleIndexBuffer;
    private ByteBuffer  	m_triangleLineBuffer;
    
    private IntBuffer   	m_hexagonVertexBuffer;
    private ByteBuffer  	m_hexagonIndexBuffer;
    private ByteBuffer  	m_hexagonLineBuffer;
    
    private static final String TAG = "GLR";
}
