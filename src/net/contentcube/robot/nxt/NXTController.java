package net.contentcube.robot.nxt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import net.contentcube.robot.nxt.commands.MovementCommand;
import net.contentcube.robot.nxt.commands.TurnCommand;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class NXTController
{
	public static final int TIME_BETWEEN_PING = 7500; // miliseconds.
	private static NXTController mInstance;
	
	// used to ensure the NXT device wont turn off.
	private Timer mKeepAliveTimer;
	private TimerTask mPingTask = new TimerTask()
	{
		@Override
		public void run()
		{
			if (mOutputStream == null) return;
			
			NXTCommand ping = new NXTCommand();
			ping.addBuffer(NXTCommandFactory.ping());
			queue(ping);
			
			Log.e("mads", "ping");
		}
	};
	
	private BluetoothSocket mBluetoothSocket;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private LinkedList<NXTCommand> mCommandQueue;
	private boolean mRunning = false;
	private MovementCommand.OnCompleteListener mCompleteListener = new MovementCommand.OnCompleteListener() {

		public void onComplete()
		{
			mRunning = false;
			runQueue();
		}
		
	};
	
	public static NXTController getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new NXTController();
		}
		
		return mInstance;
	}
	
	public NXTController()
	{
		mCommandQueue = new LinkedList<NXTCommand>();
		mKeepAliveTimer = new Timer();
		mKeepAliveTimer.scheduleAtFixedRate(mPingTask, TIME_BETWEEN_PING, TIME_BETWEEN_PING);
	}
	
	public void disconnectDevice()
	{
		if (mBluetoothSocket != null)
		{
			try
			{
				mBluetoothSocket.close();
				mBluetoothSocket = null;
				
				Log.e("NXT", "Disconnect");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void setBluetoothSocket(BluetoothSocket socket)
	{	
		try
		{
			mBluetoothSocket = socket;
			mOutputStream = socket.getOutputStream();
			mInputStream = socket.getInputStream();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void forward(int duration)
	{
		MovementCommand command = new MovementCommand();
		command.setDuration(800);
		command.addBuffer(NXTCommandFactory.forward(NXTCommandFactory.MOTOR_A));
		
		queue(command);
	}
	
	public void turnRight(int duration)
	{
		MovementCommand movement = new MovementCommand();
		movement.setDuration(800);
		movement.addBuffer(NXTCommandFactory.forward(NXTCommandFactory.MOTOR_A));
		
		TurnCommand turn = new TurnCommand();
		turn.setTurnBuffer(NXTCommandFactory.turnRight(NXTCommandFactory.MOTOR_B));
		turn.setTurnBackBuffer(NXTCommandFactory.turnLeft(NXTCommandFactory.MOTOR_B));
		turn.setMovementCommand(movement);
		
		queue(turn);
	}
	
	public void turnLeft(int duration)
	{
		MovementCommand movement = new MovementCommand();
		movement.setDuration(800);
		movement.addBuffer(NXTCommandFactory.forward(NXTCommandFactory.MOTOR_A));
		
		TurnCommand turn = new TurnCommand();
		turn.setTurnBuffer(NXTCommandFactory.turnLeft(NXTCommandFactory.MOTOR_B));
		turn.setTurnBackBuffer(NXTCommandFactory.turnRight(NXTCommandFactory.MOTOR_B));
		turn.setMovementCommand(movement);
		
		queue(turn);
	}
	
	public void onlyTurnLeft()
	{
		NXTCommand turnleft = new NXTCommand();
		turnleft.addBuffer(NXTCommandFactory.turnLeft(NXTCommandFactory.MOTOR_B));
		queue(turnleft);
	}
	public void onlyTurnRight()
	{
		NXTCommand turnRight = new NXTCommand();
		turnRight.addBuffer(NXTCommandFactory.turnRight(NXTCommandFactory.MOTOR_B));
		queue(turnRight);
	}
	
	public void backward(int duration)
	{	
		MovementCommand command = new MovementCommand();
		command.setDuration(800);
		command.addBuffer(NXTCommandFactory.backward(NXTCommandFactory.MOTOR_A));
		
		queue(command);
	}
	
	public void brake()
	{
		NXTCommand brake = new NXTCommand();
		brake.addBuffer(NXTCommandFactory.brake(NXTCommandFactory.MOTOR_A));
		brake.addBuffer(NXTCommandFactory.brake(NXTCommandFactory.MOTOR_B));
		
		queue(brake);
	}
	
	public void queue(NXTCommand command)
	{
		if (mCommandQueue.size() < 10)
		{
			mCommandQueue.add(command);		
		}
		
		runQueue();
	}
	
	private void runQueue()
	{
		if (mRunning) return;
		mRunning = true;
		
		NXTCommand command = mCommandQueue.poll();
		
		if (command != null)
		{
			command.setOnCompleteListener(mCompleteListener);
			command.run(mOutputStream);
		}
		else
		{
			mRunning = false;
		}
	}
}
