package com.zorro666.gridlocked;

import android.util.Log;
import android.view.MotionEvent;

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
        m_touchX = 0.0f;
        m_touchY = 0.0f;
        m_touchAction = MotionEvent.ACTION_CANCEL;
    }
    public void setUI( GridLocked ui )
    {
    	m_myUI = ui;
    }
    
    public Board getRenderBoard()
    {
    	return m_boardRender;
    }
    public float getTouchX()
    {
    	return m_touchX;
    }
    public float getTouchY()
    {
    	return m_touchY;
    }
    public void onStart() 
    {
    	Log.i( TAG,"onStart");
    	m_nextTime = System.currentTimeMillis() + 20;
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
		m_nextTime = now + 20;
		int oldScore = m_boardGame.getScore();
		m_boardGame.update();
		int newScore = m_boardGame.getScore();
		if ( newScore != oldScore )
		{
			m_myUI.setScore( 666 + newScore );
		}
		
		// Update the render board from game board
		updateRenderBoard();
	}
	public void setTouchAction( int action )
	{
		// A click !
		if ( action == MotionEvent.ACTION_UP )
		{
			if ( ( m_touchAction == MotionEvent.ACTION_DOWN ) || ( m_touchAction == MotionEvent.ACTION_MOVE ) )
			{
				float ratio = m_myUI.getRatio();
				// Move a row or column
				int row = Board.convertToRow( m_touchY, ratio );
				int column = Board.convertToColumn( m_touchX );
				
				if ( row != column )
				{
					if ( column == -1 )
					{
						if ( ( row >= 0 ) && ( row < Board.MAX_NUM_ROWS ) )
						{
							m_boardGame.moveRow( row, Board.RIGHT );
						}
					}
					if ( column == Board.MAX_NUM_COLUMNS )
					{
						if ( ( row >= 0 ) && ( row < Board.MAX_NUM_ROWS ) )
						{
							m_boardGame.moveRow( row, Board.LEFT );
						}
					}
					if ( row == -1 )
					{
						if ( ( column >= 0 ) && ( column < Board.MAX_NUM_COLUMNS ) )
						{
							m_boardGame.moveColumn( column, Board.DOWN );
						}
					}
					if ( row == Board.MAX_NUM_ROWS )
					{
						if ( ( column >= 0 ) && ( column < Board.MAX_NUM_COLUMNS ) )
						{
							m_boardGame.moveColumn( column, Board.UP );
						}
					}
				}
			}
		}
		m_touchAction = action;
	}
	public void setTouchPosition( float x, float y )
	{
		m_touchX = x;
		m_touchY = y;
	}
	private void updateRenderBoard()
	{
		// Need to sync with the render thread
		synchronized ( m_boardRender )
		{
			m_boardRender.copy(m_boardGame);
		}
	}
	public void resetButton()
	{
		m_boardGame.reset();
		m_myUI.setScore( 666 + 0 );
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
	private float				m_touchX;
	private float				m_touchY;
	private int					m_touchAction;
    private GridLocked 			m_myUI;
	
    private static final String TAG = "GLM";
}
        
