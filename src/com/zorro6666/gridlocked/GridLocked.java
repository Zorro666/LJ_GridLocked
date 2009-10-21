package com.zorro6666.gridlocked;

import android.app.Activity;
import android.os.Bundle;
import android.opengl.GLSurfaceView;
import android.view.ViewGroup;

public class GridLocked extends Activity {
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        m_gLSurfaceView = new GLSurfaceView( this );
        
        m_myRenderer = new GridLockedRenderer(true);
        m_myMain = new GridLockedMain(m_myRenderer);
        m_myRenderer.SetBoard(m_myMain.GetBoard());
        	
       	m_gLSurfaceView.setRenderer(m_myRenderer);
        
        setContentView( R.layout.main );
        ViewGroup absLayout = (ViewGroup)findViewById( R.id.RenderView );
        absLayout.addView( m_gLSurfaceView );
    }
    
    
    @Override
    protected void onStart() 
    {
        super.onStart();
        m_myMain.onStart();
        m_myMain.start();
    }
    @Override
    protected void onResume() 
    {
        super.onResume();
        m_gLSurfaceView.onResume();
        m_myMain.onResume();
    }

    @Override
    protected void onPause() 
    {
        super.onPause();
        m_gLSurfaceView.onPause();
        m_myMain.onPause();
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        m_myMain.onStop();
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // Kill the thread
        m_myMain.onDestroy();
    }
    
    private GLSurfaceView 		m_gLSurfaceView;
    private GridLockedRenderer 	m_myRenderer;
    private GridLockedMain		m_myMain;
}