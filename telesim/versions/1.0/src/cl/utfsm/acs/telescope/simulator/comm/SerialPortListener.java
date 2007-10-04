/**
 * 
 */
package cl.utfsm.acs.telescope.simulator.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

/**
 * @author dcontard
 *
 */
public class SerialPortListener {
	protected Nexstar4MessageWrapper wrapper = null;
	
	public SerialPortListener()
    {
        super();
        wrapper = new Nexstar4MessageWrapper();
    }
    
    public Thread connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort ) {
                SerialPort serialPort = (SerialPort) commPort;
                /*
                 * Velocity:300, 600, 1200, 2400, 4800*, 9600, 19200
                 * DataBits: 7*, 8
                 * StopBits 1
                 * Parity: 1 (EVEN)
                 * StartBit: 1
                 */
                //serialPort.setSerialPortParams(4800,SerialPort.DATABITS_7,SerialPort.STOPBITS_1,SerialPort.PARITY_EVEN);
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                serialPort.setRTS(false);
                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                //OutputStream out = serialPort.getOutputStream();
                
                Thread t =new Thread(new SerialReaderWriter(in, out));
                t.start();
                return t;
                //(new Thread(new SerialWriter(out))).start();
            }
        }
		return null;    
        
    } 
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }
        
        public void run ()
        {
            try
            {      
            	while(true){
            		byte[] buff = new byte[1024];
	                buff[0]=0x01;
	                buff[1]=0x20;
	                buff[2]=0x02;
	                buff[9]=0x03;
	                buff[10]=0x02;
	                for(int i=0; i<6;i++){
	                	String c = Long.toHexString((long)Math.random()*10%10);
	                	buff[3+i]=Byte.parseByte(c);
	                }
                    this.out.write(buff,0,11);
            	}
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
        }
    }  
    public static class SerialReader implements Runnable 
    {
        InputStream in;
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
            	while(true) {
	                if ( ( len = this.in.read(buffer)) > -1 ) {
	                	String instruction = new String(buffer,0,len);
	                    System.out.print(instruction);
	                    this.wait(1000);
	                }
	                else
	                	this.wait(3000);
            	}
            }
            catch ( IOException e ) {
                e.printStackTrace();
            } catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
    public static class SerialReaderWriter implements Runnable 
    {
        InputStream in;
        OutputStream out;
        long azimuth; // between 0 and 4294967296
        long altitude; // between 0 and 4294967296
        Date time;
        String slewSpeed;
        
        public SerialReaderWriter ( InputStream in, OutputStream out)
        {
            this.in = in;
            this.out = out;
            azimuth = (long)(4294967296.0 * (((double)new Random().nextLong())/(Math.pow(2.0,64))));
            azimuth = 2;
            altitude = (long)(4294967296.0 * (((double)new Random().nextLong())/(Math.pow(2.0,64))));
            altitude = 5;
            time = new Date();
            slewSpeed = "4";
        }
        
        public void run ()
        {
            byte[] buffer = new byte[1024];
//	    byte[] outBuffer = new byte[1024];
            int len = -1;
            try
            {
            	while(true) {
	                if ( ( len = this.in.read(buffer)) > -1 ) {
	                	String instruction = new String(buffer,0,len);
                  	byte[] outBuffer = new byte[1024];
                	String reply = "";
	                    System.out.print(instruction + " - " + instruction.length());
	                    //Get RA/DEC
	                    if(instruction == "E"){
	                    	reply = Long.toHexString(azimuth).substring(0, 4) + "," + Long.toHexString(altitude).substring(0, 4) + "#";
	                    	for(int i=0; i<reply.length(); i++){
	                    		outBuffer[i] = (byte) reply.charAt(i);
	                    	}
	                    	this.out.write(outBuffer,0,reply.length());
		                    System.out.print(reply);
	                    }
	                    //Get precise RA/DEC
	                    else if(instruction == "e"){
	                    	reply = Long.toHexString(azimuth) + "," + Long.toHexString(altitude) + "#";
	                    	for(int i=0; i<reply.length(); i++){
	                    		outBuffer[i] = (byte) reply.charAt(i);
	                    	}
	                    	this.out.write(outBuffer,0,reply.length());
		                    System.out.println(reply);
		                    System.out.println(outBuffer.toString().substring(0, reply.length()));
	                    }
	                    //Get AZM-ALT
	                    else if(instruction == "Z"){
	                    	reply = Long.toHexString(azimuth).substring(0, 4) + "," + Long.toHexString(altitude).substring(0, 4) + "#";
	                    	for(int i=0; i<reply.length(); i++){
	                    		outBuffer[i] = (byte) reply.charAt(i);
	                    	}
	                    	this.out.write(outBuffer,0,reply.length());
		                    System.out.println(reply);
		                    System.out.println(outBuffer.toString().substring(0, reply.length()));
	                    }
	                    //Get precise AZM-ALT
	                    else if(instruction == "z"){
	                    	reply = Long.toHexString(azimuth) + "," + Long.toHexString(altitude) + "#";
	                    	for(int i=0; i<reply.length(); i++){
	                    		outBuffer[i] = (byte) reply.charAt(i);
	                    	}
	                    	this.out.write(outBuffer,0,reply.length());
		                    System.out.println(reply);
		                    System.out.println(outBuffer.toString().substring(0, reply.length()));
	                    }
			    else{
				   this.out.write(buffer,0,buffer.length);
				   System.out.println("Test -- No entre");
			    }
	                    //GOTO precise RA/DEC
	                    //GOTO AZM-ALT
	                    //GOTO precise AZM-ALT
	                    //Get Version
	                    //Echo
	                    //Alignment complete?
	                    //GOTO complete?
	                    //Cancel GOTO
	                    System.out.println(reply);
	                    System.out.println(outBuffer.toString().substring(0, reply.length()));
                    	   // this.wait(50);
	                }
	                //else
	                //	this.wait(100);
			
/*                    	outBuffer[0] = 0x00;
                    	outBuffer[1] = 0x01;
                    	outBuffer[2] = 0x02;
                    	outBuffer[3] = 0x03;
			this.out.write(outBuffer,0,4);
	                this.wait(1500);
*/            	}
            }
            catch ( IOException e ) {
                e.printStackTrace();
            } /*catch (InterruptedException e) {
				e.printStackTrace();
			}*/
        }
    }
    
    public static void main(String[] args) {
    	SerialPortListener com = new SerialPortListener();
    	try {
//			com.connect("COM1");
			Thread t=com.connect("/dev/ttyS0");
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
