package com.zorro6666.gridlocked;

import android.util.Log;

public class GridLockedMain extends Thread 
{
    public GridLockedMain() 
    {
    	Log.i( TAG,"Construct");
    	m_boards = new Board[2];
    	m_boards[0] = new Board();
    	m_boards[1] = new Board();
    	m_boardGame = m_boards[0];
    	m_boardRender = m_boards[1];
        m_state = STATE_STOPPED;
        m_run = false;
    }
    
    public Board GetRenderBoard()
    {
    	return m_boardRender;
    }
    public void onStart() 
    {
    	Log.i( TAG,"onStart");
    	m_nextTime = System.currentTimeMillis() + 1;
    	if (m_state == STATE_STOPPED)
    	{
    		setState(STATE_READY);
    		setRunning(true);
    		Log.i( TAG,"start");
    		super.start();
    	}
    }     
    protected void onResume()
    {
    	setState(STATE_RUNNING);
    }
    public void onPause() 
    {
    	setState(STATE_PAUSED);
    }
    protected void onStop()
    {
    	setState(STATE_PAUSED);
    }
    protected void onDestroy()
    {
    	setState(STATE_PAUSED);
    	setRunning(false);
    }
    @Override
    public void run() 
    {
    	Log.i( TAG,"run");
        while (m_run) 
        {
            try 
            {
            	if (m_state == STATE_RUNNING)
                {
            		update();
                }
            } finally 
            {
                // do this in a finally so that if an exception is thrown during the above
            }
        }
    	Log.i( TAG,"run end");
        setState(STATE_STOPPED);
    }

    public void setRunning(boolean run)
    {
        m_run = run;
    } 
    public void setState(int state)
    {
    	m_state = state;
    }
	public void update()
	{
		long now = System.currentTimeMillis();
		if ( now < m_nextTime )
		{
			return;
		}
		m_nextTime = now + 1;
		m_boardGame.update();
		
		// Update the render board from game board
		updateRenderBoard();
	}
	private void updateRenderBoard()
	{
		// Need to sync with the render thread
		synchronized ( m_boardRender )
		{
			m_boardRender.copy(m_boardGame);
		}
	}
    
    public static final int STATE_PAUSED 	= 1;
    public static final int STATE_READY 	= 2;
    public static final int STATE_RUNNING 	= 3;
    public static final int STATE_STOPPED 	= 4;
    
    private long				m_nextTime;
    private int 				m_state;
    private boolean 			m_run = false;
	private Board				m_boardGame;
	private Board				m_boardRender;
	private Board[]				m_boards;
	
    private static final String TAG = "GLM";
}
        
