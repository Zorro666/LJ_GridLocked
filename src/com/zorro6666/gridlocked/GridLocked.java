package com.zorro6666.gridlocked;

import android.app.Activity;
import android.os.Bundle;
import android.opengl.GLSurfaceView;
import android.view.ViewGroup;
import android.os.Handler;

public class GridLocked extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        m_handler = new Handler();
        m_myRenderer = new GridLockedRenderer(true);
        m_gLSurfaceView = new GLSurfaceView( this );
        m_myMain = new GridLockedMain(m_myRenderer, m_handler);
        m_myRenderer.SetMainThread(m_myMain);
        
        m_gLSurfaceView.setRenderer(m_myRenderer);
        
        setContentView( R.layout.main );
        ViewGroup absLayout = (ViewGroup)findViewById( R.id.RenderView );
        absLayout.addView( m_gLSurfaceView );
    }
    
    @Override
    protected void onResume() 
    {
        super.onResume();
        m_gLSurfaceView.onResume();
        // Game thread needs to handle this
    }

    @Override
    protected void onPause() 
    {
        super.onPause();
        m_gLSurfaceView.onPause();
        // Game thread needs to handle this
    }
    private GLSurfaceView 		m_gLSurfaceView;
    private GridLockedRenderer 	m_myRenderer;
    private GridLockedMain		m_myMain;
    private Handler				m_handler;
}