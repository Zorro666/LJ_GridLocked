package com.zorro6666.gridlocked;

import android.os.SystemClock;

public class GridLockedMain extends Thread 
{
    public GridLockedMain(GridLockedRenderer renderer ) 
    {
    	m_board = new Board();
        m_state = STATE_READY;
        m_run = false;
    }
    
    public Board GetBoard()
    {
    	return m_board;
    }
    public void onStart() 
    {
    	m_lastTime = System.currentTimeMillis() + 100;
    	setState(STATE_READY);
    	setRunning(true);
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
    	setState(STATE_READY);
    	setRunning(false);
    }
    protected void onDestroy()
    {
    	setState(STATE_PAUSED);
    	setRunning(false);
    }
    @Override
    public void start() 
    {
    	super.start();
    }
    @Override
    public void run() 
    {
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
		m_board.update();
	}
    
    public static final int STATE_PAUSED 	= 1;
    public static final int STATE_READY 	= 2;
    public static final int STATE_RUNNING 	= 3;
    public static final int STATE_STOPPED 	= 4;
    
    private long				m_lastTime;
    private int 				m_state;
    private boolean 			m_run = false;
	private Board				m_board;
}
        