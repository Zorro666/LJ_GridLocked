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
			float width = getWidth();
			float height = getHeight();
			float x = motionEvent.getX() / width;
			float y = motionEvent.getY() / height;
			m_myMainThread.setTouchPosition( x, y );
		}
		m_myMainThread.setTouchAction( action );
		
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
		if ( action == MotionEvent.ACTION_UP )
		{
			queueEvent( 
					new Runnable() 
					{
						// This method will be called on the rendering thread:
						public void run() 
						{
							m_myRenderer.onTouchUp();
						}
					}
			);
			return true;
		}
		return super.onTouchEvent( motionEvent );
	}
    public float getRatio()
    {
    	float width = getWidth();
    	float height = getHeight();
    	float ratio = width / height;
    	return ratio;
    }
    private GridLockedRenderer 	m_myRenderer;
    private GridLockedMain		m_myMainThread;
}
