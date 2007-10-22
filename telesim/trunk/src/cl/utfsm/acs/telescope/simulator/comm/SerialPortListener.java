/**
 * 
 */
package cl.utfsm.acs.telescope.simulator.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

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
	/**
	 * 
	 * 
	 * @param wrapper
	 */
	public SerialPortListener(Nexstar4MessageWrapper wrapper) 
    {
        super();
        this.wrapper = wrapper;
        connected = false;
        bufferSizes = 1024;
        buffer = new byte[bufferSizes];
    	outBuffer = new byte[bufferSizes];
    }
	/**
	 * 
	 *
	 */
	public SerialPortListener() 
    {
        this(new Nexstar4MessageWrapper());
    }
	/**
	 * Indicates to the <code>{@link SerialPortListener}</code> to witch port it 
	 * should be listening. The user that excecutes the program
	 * needs to have access permissions for reading and writing the
	 * specified port. At least in Linux Environments, the user also
	 * needs to be capable of locking the port.
	 * <p>
	 * In Linux Systems (and probably in other UNIX Systems too), 
	 * the most frecuently used port is the <code>/dev/ttyS0</code> device, but
	 * this could not be the case for all the circumstances. For 
	 * instances, in a computer that has two serial ports, the second
	 * port may be acceded in the <code>/dev/ttyS1</code> character device.
	 * <p>
	 * If the computer in witch the <code>{@link SerialPortListener}</code> is running 
	 * has no serial port, under Linux Environments, the <code>/dev/null</code> device 
	 * can be used for testing purposes.
	 * 
	 * @param	portName	the path to the port device (usually <code>"/dev/ttyS0"</code>)
	 * @return				<code>true</code> if the <code>{@link SerialPortListener}</code> has access to 
	 * 						the specified port, otherwise <code>false</code>
	 * @throws NoSuchPortException	if the specified port does not exist
	 * @throws PortInUseException 	if the specified port is currently in use by 
	 * 								another user or software (and is provably locked)
	 * @throws UnsupportedCommOperationException	if the specified port doesn't support
	 * 												the <code>{@link CommPortIdentifier#open(java.io.FileDescriptor)}</code> 
	 * 												method
	 * @throws IOException 			if the specified port couldn't be read or written during the
	 * 								excecution of the method
	 */
    public boolean connect ( String portName ) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException
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
    /**
     * Excecutes the {@link SerialPortListener#listen()} method.
     * 
     */
	public void run ()
    {
		listen ();
    }
	
	/**
	 * Makes the <code>{@link SerialPortListener}</code> to start a loop for listening 
	 * and responding to the specified port by the <code>{@link SerialPortListener#connect(String)}</code> 
	 * method. 
	 * <p>
	 * If the <code>{@link SerialPortListener}</code> is not connected, the method will 
	 * print a pair of lines in the <code>{@link System#err}</code> output stream and then 
	 * will break the listen-respond loop (which will also end the method). 
	 * 
	 */
	public void listen ()
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
        			if(message.charAt(0)=='P'){
        				System.out.println("P"
        						+" "+(int)message.charAt(1)+" "+(int)message.charAt(2)
        						+" "+(int)message.charAt(3)+" "+(int)message.charAt(4)
        						+" "+(int)message.charAt(5)+" "+(int)message.charAt(6)
        						+" "+(int)message.charAt(7));
        			}
        			if( message.length() == 1 && expectedLength == -2 )
        				System.out.println("Not recognized message: "+message+" - "+(int)message.charAt(0));
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
        } catch ( IOException e ) {
        	e.printStackTrace();
        } catch (InterruptedException e) {
        	e.printStackTrace();
        } else {
        	System.err.println("The SerialPortListener is not connected to any port.");
        	System.err.println("The SerialPortListener will shut down now.");
        }
    }
	
	/**
	 * Determines whether the <code>{@link SerialPortListener}</code> is connected or
	 * not to a port.
	 * 
	 * @return true	if the <code>{@link SerialPortListener}</code> is already connected,
	 * 				otherwise false
	 */	
	public boolean isConnected() {
		return connected;
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
