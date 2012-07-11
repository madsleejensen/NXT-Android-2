package net.contentcube.robot.nxt.commands;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import net.contentcube.robot.nxt.NXTCommand;

public class TurnCommand extends NXTCommand
{
	private Timer mTurnCompletedTimer;
	private Timer mTurnBackCompletedTimer;
	private byte[] mTurnBuffer;
	private byte[] mTurnBackBuffer;
	private NXTCommand mMovementCommand;
	
	public void setTurnBuffer(byte[] buffer)
	{
		mTurnBuffer = buffer;
	}
	
	public void setTurnBackBuffer(byte[] buffer)
	{
		mTurnBackBuffer = buffer;
	}
	
	public void setMovementCommand(NXTCommand command)
	{
		mMovementCommand = command;
	}
	
	public void run(OutputStream stream)
	{
		mOutputStream = stream;
		
		try
		{
			stream.write(mTurnBuffer);
			stream.flush();
			
			mTurnCompletedTimer = new Timer("completion");
			mTurnCompletedTimer.schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					runMovementAction();
				}
				
			}, 500);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void runMovementAction()
	{
		mMovementCommand.setOnCompleteListener(new OnCompleteListener()
		{
			public void onComplete()
			{
				runTurnBackAction();
			}
		});
		
		mMovementCommand.run(mOutputStream);
	}
	
	private void runTurnBackAction() {
		try
		{
			mOutputStream.write(mTurnBackBuffer);
			
			mTurnBackCompletedTimer = new Timer("back_completion");
			mTurnBackCompletedTimer.schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					afterRun();
				}
				
			}, 500);
			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
