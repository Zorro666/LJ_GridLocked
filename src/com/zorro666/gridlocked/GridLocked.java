package com.zorro666.gridlocked;

import android.app.Activity;
import android.util.Log;
import android.os.Bundle;
import android.view.ViewGroup;

public class GridLocked extends Activity 
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	Log.i( TAG,"onCreate");
        super.onCreate(savedInstanceState);
        
        m_myMain = new GridLockedMain();
        m_myGLSurfaceView = new GridLockedGLView( this, m_myMain );
        m_myMain.setView( m_myGLSurfaceView );
        
        setContentView( R.layout.main );
        ViewGroup absLayout = (ViewGroup)findViewById( R.id.RenderView );
        absLayout.addView( m_myGLSurfaceView );
    }
    
    @Override
    protected void onStart() 
    {
    	Log.i( TAG,"onStart");
        super.onStart();
        m_myMain.onStart();
    }
    @Override
    protected void onRestart() 
    {
    	Log.i( TAG,"onRestart");
        super.onResume();
    }
    @Override
    protected void onResume() 
    {
    	Log.i( TAG,"onResume");
        super.onResume();
        m_myGLSurfaceView.onResume();
        m_myMain.onResume();
    }

    @Override
    protected void onPause() 
    {
    	Log.i( TAG,"onPause");
        super.onPause();
        m_myGLSurfaceView.onPause();
        m_myMain.onPause();
    }
    @Override
    protected void onStop()
    {
    	Log.i( TAG,"onStop");
        super.onStop();
        m_myMain.onStop();
    }
    @Override
    protected void onDestroy()
    {
    	Log.i( TAG,"onDestroy");
        super.onDestroy();
        // Kill the thread
        m_myMain.onDestroy();
    }
    
    private GridLockedGLView 	m_myGLSurfaceView;
    private GridLockedMain		m_myMain;
    
    private static final String TAG = "GL";
}