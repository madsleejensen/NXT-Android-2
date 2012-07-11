package net.contentcube.robot.nxt.commands;

import java.util.Timer;
import java.util.TimerTask;

import net.contentcube.robot.nxt.NXTCommand;
import net.contentcube.robot.nxt.NXTCommandFactory;

public class MovementCommand extends NXTCommand
{
	private Timer mBrakeTimer;
	private int mDuration = -1; // -1 == infinity.
	
	public void setDuration(int duration)
	{
		mDuration = duration;
	}
	
	public void afterRun()
	{
		scheduleBrakeAction();
	}
	
	private void scheduleBrakeAction()
	{
		mBrakeTimer = new Timer("completion");
		mBrakeTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				sendBrake();
				
				if (mCompleteListener != null)
				{
					mCompleteListener.onComplete();
				}
			}
			
		}, mDuration);
		
	}
	
	private void sendBrake() 
	{
		NXTCommand brake = new NXTCommand();
		brake.addBuffer(NXTCommandFactory.brake(NXTCommandFactory.MOTOR_A));
		brake.addBuffer(NXTCommandFactory.brake(NXTCommandFactory.MOTOR_B));
		brake.run(mOutputStream);
	}
	
	/*
	public static MovementCommand parseByJSONString(String jsonString)
	{
		MovementCommand command = null;
		String commandName;
		
		try
		{
			JSONObject data = new JSONObject(jsonString);
			
			commandName = data.getString("command");
			
			if (commandName.equals("forward"))
			{
				command = new MovementCommand(NXTCommand.Command.FORWARD);
				command.setDuration(500);
			}
			else if (commandName.equals("back"))
			{
				command = new MovementCommand(NXTCommand.Command.BACKWARD);
				command.setDuration(500);
			}
			else if (commandName.equals("left"))
			{
				command = new MovementCommand(NXTCommand.Command.TURN_LEFT);
				command.setDuration(500);
			}
			else if (commandName.equals("right"))
			{
				command = new MovementCommand(NXTCommand.Command.TURN_RIGHT);
				command.setDuration(500);
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return command;
	}*/
}
