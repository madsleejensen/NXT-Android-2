package com.clwillingham.socket.io;

import java.io.IOException;
import java.nio.channels.NotYetConnectedException;
import java.util.TimerTask;

public class IOBeat extends TimerTask {
	
	private IOWebSocket socket;
	private boolean running = false;
	
	public IOBeat(IOWebSocket socket){
		this.socket = socket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(running){
			try {
				socket.send("2::"); //send heartbeat;
				System.out.println("HeartBeat Written to server");
			} catch (NotYetConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void start(){
		running = true;
	}
	
	public void stop(){
		running = false;
	}
	
	public boolean isRunning(){
		return running;
	}

}
