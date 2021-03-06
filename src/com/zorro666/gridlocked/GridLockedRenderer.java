package com.zorro666.gridlocked;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
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
        m_width = 1.0f;
        m_height = 1.0f;
        m_ratio = 1.0f;
        
        final int zero = 		0x00000;
        final int one = 		0x10000;
        final int half = 		one/2;
        final int onethird =	0x10000/3;
        final int twothird =	onethird*2;
        
    	m_touchAction = MotionEvent.ACTION_CANCEL;
    	
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
        {
        	int horizontalVertices[] = 	{
        			zero, 	zero, 	zero,
        			one, 	zero, 	zero,
        								};
        	byte horizontalLineIndices[] = { 0, 1 };
        	
        	ByteBuffer vbb = ByteBuffer.allocateDirect(horizontalVertices.length*4);
        	vbb.order(ByteOrder.nativeOrder());
        	m_horizontalVertexBuffer = vbb.asIntBuffer();
        	m_horizontalVertexBuffer.put(horizontalVertices);
        	m_horizontalVertexBuffer.position(0);
        	
        	m_horizontalLineBuffer = ByteBuffer.allocateDirect(horizontalLineIndices.length);
        	m_horizontalLineBuffer.put(horizontalLineIndices);
        	m_horizontalLineBuffer.position(0);
        }
        {
        	int verticalVertices[] = 	{
        			zero, 	zero, 	zero,
        			zero, 	one, 	zero,
        								};
        	byte verticalLineIndices[] = { 0, 1 };
        	
        	ByteBuffer vbb = ByteBuffer.allocateDirect(verticalVertices.length*4);
        	vbb.order(ByteOrder.nativeOrder());
        	m_verticalVertexBuffer = vbb.asIntBuffer();
        	m_verticalVertexBuffer.put(verticalVertices);
        	m_verticalVertexBuffer.position(0);
        	
        	m_verticalLineBuffer = ByteBuffer.allocateDirect(verticalLineIndices.length);
        	m_verticalLineBuffer.put(verticalLineIndices);
        	m_verticalLineBuffer.position(0);
        }
        
    }
    public void onDrawFrame(GL10 gl) 
    {
    	//Log.i( TAG,"onDrawFrame");
    	gl.glClearColor(0.4f, 0.0f, 0.2f, 1.0f );
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glFrontFace(GL10.GL_CCW);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        
        Board board = m_mainThread.getRenderBoard();
        synchronized( board)
        {
        	board.draw(gl, this);
        }
        
        // Draw where the touch event is
        if ( m_touchAction == MotionEvent.ACTION_DOWN )
        {
        	float touchX = m_mainThread.getTouchX();
        	float touchY = m_mainThread.getTouchY();
			int row = Board.convertToRow( touchY, m_ratio );
			int column = Board.convertToColumn( touchX );
        	
			// These settings should be in Piece or Board and referenced from a single place
			final float x0 = Board.X_ORIGIN;
			final float y0 = Board.Y_ORIGIN;
			final float canvasWidth = Board.WIDTH;
			
        	int x = column;
        	int y = row;
        	float cellWidth =  ( canvasWidth / Board.MAX_NUM_COLUMNS );
        	float cellHeight = cellWidth;
        	
        	float xpos = x0 + x * cellWidth;
        	float ypos = y0 + y * cellHeight;
        	
        	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        	gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        	gl.glEnable(GL10.GL_BLEND);
        	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        	
        	gl.glColor4f( 1.0f, 0.5f, 0.8f, 0.7f );
        	drawOutlineRectangle(gl, xpos, ypos, 0.1f, 0.1f );
        	
        	if ( ( column >= 0 )  && ( column < Board.MAX_NUM_COLUMNS ) )
        	{
        		if ( row == -1 )
        		{
        			gl.glColor4f( 1.0f, 0.0f, 0.0f, 0.4f );
        			drawOutlineRectangle(gl, xpos, y0, cellWidth, canvasWidth);
        		}
        		if ( row == Board.MAX_NUM_ROWS )
        		{
        			gl.glColor4f( 0.0f, 0.0f, 1.0f, 0.4f );
        			drawOutlineRectangle(gl, xpos, y0, cellWidth, canvasWidth);
        		}
        	}
        	if ( ( row >= 0 )  && ( row < Board.MAX_NUM_ROWS ) )
        	{
        		if ( column == -1 )
        		{
        			gl.glColor4f( 1.0f, 0.0f, 0.0f, 0.4f );
        			drawOutlineRectangle(gl, x0, ypos, canvasWidth, cellHeight);
        		}
        		if ( column == Board.MAX_NUM_COLUMNS )
        		{
        			gl.glColor4f( 0.0f, 0.0f, 1.0f, 0.4f );
        			drawOutlineRectangle(gl, x0, ypos, canvasWidth, cellHeight);
        		}
        	}
        }
        
        /*
       	gl.glEnable(GL10.GL_BLEND);
       	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4f( 1.0f, 1.0f, 0.5f, 0.4f );
        drawOutlineRectangle(gl, 0.0f, 0.0f, 1.0f, 1.0f );
        */

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        
    }
    public void onTouchDown()
    {
    	m_touchAction = MotionEvent.ACTION_DOWN;
    }
    public void onTouchUp()
    {
    	m_touchAction = MotionEvent.ACTION_UP;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) 
    {
    	Log.i( TAG,"onSurfaceChanged");
    	
    	/*
    	 * Set our projection matrix. This doesn't have to be done each time we draw, 
    	 * but usually a new projection needs to be set when the viewport is resized.
    	 */
    	
    	m_width = (float)width;
    	m_height = (float)height;
    	float ratio = m_width / m_height;
    	
    	gl.glMatrixMode(GL10.GL_PROJECTION);
    	gl.glLoadIdentity();
    	gl.glOrthof(0.0f, 1.0f, 1.0f/ratio, 0.0f, -1.0f, 1.0f );
    	
    	gl.glViewport(0, 0, width, height);
    	
    	m_ratio = ratio;
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
    public float getRatio()
    {
    	return m_ratio;
    }
    public void drawOutlineTriangle(GL10 gl, float x0, float y0, float width, float height)
    {
    	gl.glPushMatrix();
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_triangleVertexBuffer);
		gl.glTranslatef( x0, y0, 1.0f );
		gl.glScalef( width, height, 1.0f );
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 3, GL10.GL_UNSIGNED_BYTE, m_triangleIndexBuffer);
			
		gl.glColor4f(1.0f,0,0.5f,1.0f);
		gl.glDrawElements(GL10.GL_LINE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, m_triangleLineBuffer ); 
		gl.glPopMatrix();
    }
    public void drawOutlineHexagon(GL10 gl, float x0, float y0, float width, float height)
    {
    	gl.glPushMatrix();
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_hexagonVertexBuffer);
		gl.glTranslatef( x0, y0, 1.0f );
		gl.glScalef( width, height, 1.0f );
		gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 8, GL10.GL_UNSIGNED_BYTE, m_hexagonIndexBuffer);
			
		gl.glColor4f(1.0f,0,0.5f,1.0f);
		gl.glDrawElements(GL10.GL_LINE_STRIP, 7, GL10.GL_UNSIGNED_BYTE, m_hexagonLineBuffer ); 
		gl.glPopMatrix();
    }
    public void drawOutlineRectangle(GL10 gl, float x0, float y0, float width, float height)
    {
    	gl.glPushMatrix();
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_rectangleVertexBuffer);
		gl.glTranslatef( x0, y0, 1.0f );
		gl.glScalef( width, height, 1.0f );
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, m_rectangleIndexBuffer);
			
		gl.glColor4f(1.0f,0,0.5f,1.0f);
		gl.glDrawElements(GL10.GL_LINE_STRIP, 5, GL10.GL_UNSIGNED_BYTE, m_rectangleLineBuffer ); 
		gl.glPopMatrix();
    }
    public void drawRectangle(GL10 gl, float x0, float y0, float width, float height)
    {
    	gl.glPushMatrix();
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_rectangleVertexBuffer);
		gl.glTranslatef( x0, y0, 1.0f );
		gl.glScalef( width, height, 1.0f );
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, m_rectangleIndexBuffer);
		gl.glPopMatrix();
    }
    public void drawHorizontalLine(GL10 gl, float x0, float y0, float width )
    {
    	gl.glPushMatrix();
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_horizontalVertexBuffer);
		gl.glTranslatef( x0, y0, 1.0f );
		gl.glScalef( width, 1.0f, 1.0f );
		gl.glDrawElements(GL10.GL_LINE_STRIP, 2, GL10.GL_UNSIGNED_BYTE, m_horizontalLineBuffer);
		gl.glPopMatrix();
    }
    public void drawVerticalLine(GL10 gl, float x0, float y0, float height )
    {
    	gl.glPushMatrix();
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, m_verticalVertexBuffer);
		gl.glTranslatef( x0, y0, 1.0f );
		gl.glScalef( 1.0f, height, 1.0f );
		gl.glDrawElements(GL10.GL_LINE_STRIP, 2, GL10.GL_UNSIGNED_BYTE, m_verticalLineBuffer);
		gl.glPopMatrix();
    }
    private static final String TAG = "GLR";
    
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
    
    private IntBuffer   	m_horizontalVertexBuffer;
    private ByteBuffer  	m_horizontalLineBuffer;
    
    private IntBuffer   	m_verticalVertexBuffer;
    private ByteBuffer  	m_verticalLineBuffer;
    
    private int				m_touchAction;
    private float			m_width;
    private float			m_height;
    private float			m_ratio;
}
