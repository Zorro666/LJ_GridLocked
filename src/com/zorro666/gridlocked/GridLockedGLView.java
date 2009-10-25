package com.zorro666.gridlocked;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GridLockedGLView extends GLSurfaceView 
{
	public GridLockedGLView( Context context, GridLockedMain mainThread )
	{
		super( context );

		m_myRenderer = new GridLockedRenderer( true, mainThread );
		m_myMainThread = mainThread;
		setRenderer( m_myRenderer );
	}
	
	public boolean onTouchEvent( MotionEvent motionEvent )
	{
		int action = motionEvent.getAction();
		if ( action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE )
		{
			float x = motionEvent.getX();
			float y = motionEvent.getY();
			m_myMainThread.setTouchDown( x, y );
		}
		
		if ( action == MotionEvent.ACTION_DOWN )
		{
			queueEvent( 
					new Runnable() 
					{
						// This method will be called on the rendering thread:
						public void run() 
						{
							m_myRenderer.onTouchDown();
						}
					}
			);
			return true;
		}
		return super.onTouchEvent( motionEvent );
	}
    private GridLockedRenderer 	m_myRenderer;
    private GridLockedMain		m_myMainThread;
}
