/**
 * 
 */
package cl.utfsm.acs.telescope.simulator.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

/**
 * @author dcontard
 *
 */
public class SerialPortListener implements Runnable {
	protected Nexstar4MessageWrapper wrapper;
	protected InputStream in;
	protected OutputStream out;
	protected boolean connected;
    byte[] buffer;
	byte[] outBuffer;
	int bufferSizes;
	
	public SerialPortListener() 
    {
        super();
        wrapper = new Nexstar4MessageWrapper();
        connected = false;
        bufferSizes = 1024;
        buffer = new byte[bufferSizes];
    	outBuffer = new byte[bufferSizes];
    }
	
    public boolean connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
            connected = false;
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
                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                connected = true;
                return true;
            }
            else
            	connected = false;
        }
		return connected;   
    }
    
	public void run ()
    {
		String message = "";
    	String reply = "";
        int length = -1;
        int expectedLength;
        
        if (connected) try
        {
        	while(true) {
                if ( ( length = this.in.read(buffer,0,1)) > -1 ) {
                	message += new String(buffer,0,length,"US-ASCII");
                	expectedLength = wrapper.expectedMessageLength(message.substring(0,1));
                	
                	while(message.length() < expectedLength){
                		if ( ( length = this.in.read(buffer,0,1)) > -1 )
                        	message += new String(buffer,0,length,"US-ASCII");
                		else
                    		Thread.sleep(10);

                	}
                	System.out.println("Message: " + message + " Lenght: " + message.length() + " Expected Lenght: " + expectedLength);
                	//if(message.length() == expectedLength){
                		reply = wrapper.executeAction(message);
                		outBuffer = reply.getBytes("US-ASCII");
                		out.write(outBuffer);
                		out.flush();
                		System.out.println("Devolvemos " + reply);
                		message="";
                	//}
                	//else if(length > expectedLength){
                	//	wrapper.executeAction(message);
                		//System.out.print(reply + " - " + reply.length());
                	//}
                } 
               else
                	Thread.sleep(100);
        	}
        }
        catch ( IOException e ) {
            e.printStackTrace();
        } catch (InterruptedException e) {
			e.printStackTrace();
		} 
    }
		
	public static void main(String args[]){
		try {
			SerialPortListener spl = new SerialPortListener();
			spl.connect("/dev/ttyS0");
			Thread t = new Thread(spl);
			t.start();
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
