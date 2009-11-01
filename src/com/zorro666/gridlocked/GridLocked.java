package com.zorro666.gridlocked;

import android.app.Activity;
import android.util.Log;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.os.Handler;
import android.os.Message;

public class GridLocked extends Activity implements OnTouchListener, Handler.Callback
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	Log.i( TAG,"onCreate");
        super.onCreate(savedInstanceState);
        
        m_handler = new Handler( this );
        
        m_myMain = new GridLockedMain();
        m_myGLSurfaceView = new GridLockedGLView( this, m_myMain );
        m_myMain.setUI( this );
        
        setContentView( R.layout.main );
        ViewGroup absLayout = (ViewGroup)findViewById( R.id.RenderView );
        absLayout.addView( m_myGLSurfaceView );
        
        m_resetButton = (Button)findViewById( R.id.ButtonReset );
        m_resetButton.setOnTouchListener(this);

        m_scoreText = (TextView)findViewById( R.id.TextScore );
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
    public boolean onTouch( View v, MotionEvent event )
	{
    	if ( v == m_resetButton )
    	{
    		if ( event.getAction() == MotionEvent.ACTION_UP )
    		{
    			m_myMain.resetButton();
    			return false;
    		}
    	}
    	return false;
	}
    public void setScore( int score )
    {
		m_handler.sendEmptyMessage( score );
    }
    private void updateScore( int score )
    {
		m_scoreString = String.format("%06d", score );
		m_scoreText.setText( m_scoreString );
    }
    public float getRatio()
	{
    	return m_myGLSurfaceView.getRatio();
	}
    public boolean handleMessage( Message msg )
    {
    	int newScore = msg.what;
    	if ( newScore > 0 )
    	{
    		updateScore( newScore );
    		return true;
    	}
    	return false;
    }
    
    private GridLockedGLView 	m_myGLSurfaceView;
    private GridLockedMain		m_myMain;
    private Button				m_resetButton;
    private TextView			m_scoreText;
    private String				m_scoreString;
    private Handler				m_handler;
    
    private static final String TAG = "GL";
}