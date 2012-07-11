package net.contentcube.robot;

import java.io.IOException;

import net.contentcube.robot.nxt.NXTController;

import org.json.JSONObject;

import com.clwillingham.socket.io.IOSocket;
import com.clwillingham.socket.io.MessageCallback;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ControllerService extends Service {

	private IOSocket mSocketIO;
	private NXTController mController;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		mController = NXTController.getInstance();
		
		mSocketIO = new IOSocket("http://chat.foundershouse.dk:8080", new MessageCallback() {
			
			public void on(String event, JSONObject... data) {
				
				if (event.equals("forward"))
				{
					mController.forward(100);
				}
				else if (event.equals("back")) 
				{
					mController.backward(100);
				}
				else if (event.equals("left"))
				{
					mController.turnLeft(100);
				}
				else if (event.equals("right"))
				{
					mController.turnRight(100);
				}
			}
			
			public void onConnect() {
				try
				{
					mSocketIO.emit("carbot", new JSONObject());
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			public void onDisconnect() {
			    // Socket connection closed
			}
			
			public void onConnectFailure() {
				// TODO Auto-generated method stub
			}

			public void onMessage(String message) {
			    // Handle simple messages
			}
			
			public void onMessage(JSONObject message) {
			    // Handle JSON messages
			}
		});

	    mSocketIO.connect();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mSocketIO.disconnect();
	}

}
