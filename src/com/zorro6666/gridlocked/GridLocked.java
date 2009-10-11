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
        
        m_myRenderer = new GridLockedRenderer(true);
        m_gLSurfaceView = new GLSurfaceView( this );
        m_gLSurfaceView.setRenderer(m_myRenderer);
        
        setContentView( R.layout.main );
        ViewGroup absLayout = (ViewGroup)findViewById( R.id.RenderView );
        absLayout.addView( m_gLSurfaceView );
    }
    
    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        m_gLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        m_gLSurfaceView.onPause();
    }
    
    private GLSurfaceView m_gLSurfaceView;
    private GridLockedRenderer m_myRenderer;
}