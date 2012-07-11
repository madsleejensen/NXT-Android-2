package net.contentcube.robot.nxt;

public class NXTCommandFactory
{	
	public static final byte MOTOR_A = 0;
	public static final byte MOTOR_B = 1;
	public static final byte MOTOR_C = 2;
	
	public static final byte FORWARD_SPEED = 100;
	public static final byte BACKWARD_SPEED = -100;
	
	public static final byte TURN_DEGREES = 100;
	
	public static byte[] resetMotorPosition(byte motor, boolean relative)
	{
		byte[] buffer = new byte[6];
		buffer[0] = (byte) (14-2);  // length lsb
		buffer[1] = 0;
		buffer[2] = 0;
		buffer[3] = 0x0A;
		buffer[4] = motor;
		buffer[5] = (byte) (relative == true ? 0x1 : 0x0);
		
		return buffer;
 	}
	
	public static byte[] turnLeft(byte motor)
	{
		byte[] buffer = new byte[14];
		buffer[0] = (byte) (14-2);  // length lsb
		buffer[1] = 0; // length msb
		buffer[2] = 0; // direct command (with response)
		buffer[3] = 0x04; // set output state
		buffer[4] = motor; // motor (A:0, B:1, C:2)
		buffer[5] = FORWARD_SPEED; // speed range (-100 : 100)
		buffer[6] = 1 + 2; // mode (MOTOR_ON)
		buffer[7] = 0;
		buffer[8] = 0;
		buffer[9] = 0x20; // run state (RUNNING)
		
		int degrees = TURN_DEGREES;
		
		buffer[10] = (byte) (degrees & 0xFF); // TachoLimit (maximum degrees)
		buffer[11] = (byte) (degrees >> 8 & 0xFF);
		buffer[12] = (byte) (degrees >> 16 & 0xFF);
		buffer[13] = (byte) (degrees >> 24 & 0xFF);
		
		return buffer;
	}
	
	public static byte[] turnRight(byte motor)
	{
		byte[] buffer = new byte[14];
		buffer[0] = (byte) (14-2);  // length lsb
		buffer[1] = 0; // length msb
		buffer[2] = 0; // direct command (with response)
		buffer[3] = 0x04; // set output state
		buffer[4] = motor; // motor (A:0, B:1, C:2)
		buffer[5] = BACKWARD_SPEED; // speed range (-100 : 100)
		buffer[6] = 1 + 2; // mode (MOTOR_ON)
		buffer[7] = 0;
		buffer[8] = 0;
		buffer[9] = 0x20; // run state (RUNNING)
		
		int degrees = TURN_DEGREES;
		
		buffer[10] = (byte) (degrees & 0xFF); // TachoLimit (maximum degrees)
		buffer[11] = (byte) (degrees >> 8 & 0xFF);
		buffer[12] = (byte) (degrees >> 16 & 0xFF);
		buffer[13] = (byte) (degrees >> 24 & 0xFF);
		
		return buffer;
	}
	
	public static byte[] brake(byte motor)
	{
		byte[] buffer = new byte[14];
		buffer[0] = (byte) (14-2);  // length lsb
		buffer[1] = 0; // length msb
		buffer[2] = 0; // direct command (with response)
		buffer[3] = 0x04; // set output state
		buffer[4] = motor; // motor (A:0, B:1, C:2)
		buffer[5] = 0; // speed range (-100 : 100)
		buffer[6] = 2; // mode (MOTOR_ON)
		buffer[7] = -100;
		buffer[8] = 0;
		buffer[9] = 0x20; // run state (RUNNING)
	
		buffer[10] = 0; // TachoLimit (maximum degrees)
		buffer[11] = 0;
		buffer[12] = 0;
		buffer[13] = 0;
		
		return buffer;
	}
	
	public static byte[] forward(byte motor)
	{
		byte[] buffer = new byte[14];
		buffer[0] = (byte) (14-2);  // length lsb
		buffer[1] = 0; // length msb
		buffer[2] = 0; // direct command (with response)
		buffer[3] = 0x04; // set output state
		buffer[4] = motor; // motor (A:0, B:1, C:2)
		buffer[5] = BACKWARD_SPEED; // speed range (-100 : 100)
		buffer[6] = 1 + 2; // mode (MOTOR_ON)
		buffer[7] = 0;
		buffer[8] = 0;
		buffer[9] = 0x20; // run state (RUNNING)
		buffer[10] = 0;
		buffer[11] = 0;
		buffer[12] = 0;
		buffer[13] = 0;
		
		/*
		buffer[10] = (byte) (wheelTurnDegrees & 0xFF); // TachoLimit (maximum degrees)
		buffer[11] = (byte) (wheelTurnDegrees >> 8 & 0xFF);
		buffer[12] = (byte) (wheelTurnDegrees >> 16 & 0xFF);
		buffer[13] = (byte) (wheelTurnDegrees >> 24 & 0xFF);
		*/
		
		return buffer;
	}
	
	public static byte[] backward(byte motor)
	{
		byte[] buffer = new byte[14];
		buffer[0] = (byte) (14-2);  // length lsb
		buffer[1] = 0; // length msb
		buffer[2] = 0; // direct command (with response)
		buffer[3] = 0x04; // set output state
		buffer[4] = motor; // motor (A:0, B:1, C:2)
		buffer[5] = FORWARD_SPEED; // speed range (-100 : 100)
		buffer[6] = 1 + 2; // mode (MOTOR_ON)
		buffer[7] = 0;
		buffer[8] = 0;
		buffer[9] = 0x20; // run state (RUNNING)
		buffer[10] = 0;
		buffer[11] = 0;
		buffer[12] = 0;
		buffer[13] = 0;
		
		/*
		buffer[10] = (byte) (wheelTurnDegrees & 0xFF); // TachoLimit (maximum degrees)
		buffer[11] = (byte) (wheelTurnDegrees >> 8 & 0xFF);
		buffer[12] = (byte) (wheelTurnDegrees >> 16 & 0xFF);
		buffer[13] = (byte) (wheelTurnDegrees >> 24 & 0xFF);
		*/
		return buffer;
	}
	
	public static byte[] ping()
	{
		byte[] buffer = new byte[4];
		buffer[0] = (byte) (4-2);
		buffer[1] = 0;
		buffer[2] = 0x00;
		buffer[3] = 0x0D;
		
		return buffer;
	}
}
