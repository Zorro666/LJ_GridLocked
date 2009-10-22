package com.zorro6666.gridlocked;

import android.app.Activity;
import android.util.Log;
import android.os.Bundle;
import android.opengl.GLSurfaceView;
import android.view.ViewGroup;

public class GridLocked extends Activity 
{
    /** Called when the activity is first created. */
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	Log.v( TAG,"onCreate");
        super.onCreate(savedInstanceState);
        
        m_gLSurfaceView = new GLSurfaceView( this );
        
        m_myRenderer = new GridLockedRenderer(true);
        m_myMain = new GridLockedMain(m_myRenderer);
        m_myRenderer.SetBoard(m_myMain.GetBoard());
        	
        setContentView( R.layout.main );
        ViewGroup absLayout = (ViewGroup)findViewById( R.id.RenderView );
        absLayout.addView( m_gLSurfaceView );
    }
    
    
    @Override
    protected void onStart() 
    {
    	Log.v( TAG,"onStart");
        super.onStart();
       	m_gLSurfaceView.setRenderer(m_myRenderer);
        m_myMain.onStart();
        m_myMain.start();
    }
    @Override
    protected void onResume() 
    {
    	Log.v( TAG,"onResume");
        super.onResume();
        m_gLSurfaceView.onResume();
        m_myMain.onResume();
    }

    @Override
    protected void onPause() 
    {
    	Log.v( TAG,"onPause");
        super.onPause();
        m_gLSurfaceView.onPause();
        m_myMain.onPause();
    }
    @Override
    protected void onStop()
    {
    	Log.v( TAG,"onStop");
        super.onStop();
        m_myMain.onStop();
    }
    @Override
    protected void onDestroy()
    {
    	Log.v( TAG,"onDestroy");
        super.onDestroy();
        // Kill the thread
        m_myMain.onDestroy();
    }
    
    private GLSurfaceView 		m_gLSurfaceView;
    private GridLockedRenderer 	m_myRenderer;
    private GridLockedMain		m_myMain;
    
    private static final String TAG = "GL";
}