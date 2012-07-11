package net.contentcube.robot.nxt;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.util.Log;

public class NXTCommand {
	public interface OnCompleteListener
	{
		public void onComplete();
	}
	
	protected OutputStream mOutputStream;
	protected OnCompleteListener mCompleteListener; 
	protected ArrayList<byte[]> mBuffers;
	
	public NXTCommand()
	{
		mBuffers = new ArrayList<byte[]>();
	}
	
	public void addBuffer(byte[] buffer)
	{
		mBuffers.add(buffer);
	}
	
	public void run(OutputStream stream)
	{
		mOutputStream = stream;
		
		try
		{
			for (byte[] buffer : mBuffers)
			{
				stream.write(buffer);
			}
			
			stream.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		afterRun();
	}
	
	public void afterRun() 
	{
		if (mCompleteListener != null)
		{
			mCompleteListener.onComplete();
		}
	}
	
	public void setOnCompleteListener(OnCompleteListener listener)
	{
		mCompleteListener = listener;
		Log.e("mads", "set listener");
	}
}
