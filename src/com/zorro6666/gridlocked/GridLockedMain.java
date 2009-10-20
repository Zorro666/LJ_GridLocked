package com.zorro6666.gridlocked;

import android.os.Handler;
import android.os.SystemClock;

public class GridLockedMain extends Thread 
{
    public GridLockedMain(GridLockedRenderer renderer, Handler handler) 
    {
    	m_board = new Board();
        m_handler = handler;
        m_renderer = renderer;
        m_renderer.SetBoard(m_board);
    }
    
    public Board GetBoard()
    {
    	return m_board;
   }
    public void doStart() 
    {
    	m_lastTime = System.currentTimeMillis() + 100;
    	setState(STATE_RUNNING);
    }     
    @Override
    public void start() 
    {
    	super.start();
    	doStart();
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
    
    public static final int STATE_PAUSE = 1;
    public static final int STATE_READY = 2;
    public static final int STATE_RUNNING = 3;
    
    private long				m_lastTime;
    private int 				m_state;
    private boolean 			m_run = false;
    private GridLockedRenderer 	m_renderer;
    private Handler 			m_handler;
	private Board				m_board;
}
        
