import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class interfaz {
	private static JFrame frame;
	private static void createAndShowGUI()
	{
		frame = new JFrame("Hevelius v0.0.1");
		DrawingPanel pane = new DrawingPanel(null);
		Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});

		pane.setBackground(Color.BLACK);

		frame.setResizable(false);
		frame.pack();
		frame.setSize(ScreenSize);
		frame.setVisible(true);
		Dimension PanelSize = frame.getContentPane().getSize();
		frame.setContentPane(pane);
		pane.setSize(PanelSize);
		System.out.println(pane.getSize());
		int dx = PanelSize.width;
		int dy = PanelSize.height;
		int rectx = dx/2;
		int recty = dy/2;
		int dist = (3*dx + rectx)/4 -40;

		//Label Coordinates
		JLabel coor = new JLabel("Coordinates Z");
		//coor.setLocation((dx+rectx)/2 + 20+dist/2-50,210);
		coor.setLocation(dist,240);
		coor.setSize(100,20);
		coor.setForeground(Color.WHITE);
		pane.add(coor);
		
		//RA or ALT Label
		JLabel coor1L = new JLabel("X");
		coor1L.setLocation(dist,260);
		coor1L.setSize(20,20);
		coor1L.setForeground(Color.WHITE);
		pane.add(coor1L);

		//RA or ALT Coordinate
		JTextField coor1 = new JTextField("0");
		coor1.setLocation(dist+15,260);
		coor1.setSize(80,20);
		pane.add(coor1);

		//DEC or AZ Label
		JLabel coor2L = new JLabel("Y");
		coor2L.setLocation(dist,280);
		coor2L.setSize(20,20);
		coor2L.setForeground(Color.WHITE);
		pane.add(coor2L);

		//DEC or AZ Coordinate
		JTextField coor2 = new JTextField("0");
		coor2.setLocation(dist + 15,280);
		coor2.setSize(80,20);
		pane.add(coor2);

		//Goto
		JButton go = new JButton("Go");
		go.setLocation(dist + 55,302);
		go.setSize(40,20);
		go.setMargin(new Insets(0,0,0,0));
		pane.add(go);

		//Change RA-DEC <-> ALT-AZ
		JButton change = new JButton("x");
		change.setLocation(dist-5,302);
		change.setSize(20,20);
		change.setMargin(new Insets(0,0,0,0));
		pane.add(change);

		//Current coordinates
		JLabel ccoor = new JLabel("Coordinates");
		ccoor.setLocation(dist,420);
		ccoor.setSize(80,20);
		ccoor.setForeground(Color.WHITE);
		pane.add(ccoor);

		//Current RA or ALT Label
		JLabel ccoor1L = new JLabel("X");
		ccoor1L.setLocation(dist,450);
		ccoor1L.setSize(20,20);
		ccoor1L.setForeground(Color.WHITE);
		pane.add(ccoor1L);

		//Current RA or ALT Coordinate
		JLabel ccoor1C = new JLabel("0.00000");
		ccoor1C.setLocation(dist + 15,450);
		ccoor1C.setSize(80,20);
		ccoor1C.setForeground(Color.WHITE);
		pane.add(ccoor1C);

		//Current DEC or AZ Label
		JLabel ccoor2L = new JLabel("Y");
		ccoor2L.setLocation(dist, 470);
		ccoor2L.setSize(20,20);
		ccoor2L.setForeground(Color.WHITE);
		pane.add(ccoor2L);

		//Current DEC or AZ Coordinate
		JLabel ccoor2C = new JLabel("0.00000");
		ccoor2C.setLocation(dist + 15,470);
		ccoor2C.setSize(80,20);
		ccoor2C.setForeground(Color.WHITE);
		pane.add(ccoor2C);

		//Emergency Stop Button
		JButton stop = new JButton("STOP!");
		stop.setLocation(PanelSize.width/2-25,PanelSize.height - 150);
		stop.setSize(50,50);
		stop.setMargin(new Insets(0,0,0,0));
		pane.add(stop);

		//South Manual Pointing Button 
		JButton smp = new JButton("v");
		smp.setLocation((dx-rectx)/2+1,(dy+recty)/2+1);
		smp.setSize(rectx+2,20);
		smp.setMargin(new Insets(0,0,0,0));
		pane.add(smp);

		//Top Manual Pointing Button
		JButton nmp = new JButton("^");
		nmp.setLocation((dx-rectx)/2+1,(dy-recty)/2-19);
		nmp.setSize(rectx+2,20);
		nmp.setMargin(new Insets(0,0,0,0));
		pane.add(nmp);

		//West Manual Pointing Button
		JButton wmp = new JButton("<");
		wmp.setLocation((dx-rectx)/2-19,(dy-recty)/2);
		wmp.setSize(20,recty+2);
		wmp.setMargin(new Insets(0,0,0,0));
		pane.add(wmp);

		//East Manual Pointing Button
		JButton emp = new JButton(">");
		emp.setLocation((dx+rectx)/2+2,(dy-recty)/2);
		emp.setSize(20,recty+2);
		emp.setMargin(new Insets(0,0,0,0));
		pane.add(emp);

		//Temperature Label
		JLabel tempL = new JLabel("Temperature:");
		tempL.setLocation(10,10);
		tempL.setSize(100,20);
		tempL.setForeground(Color.WHITE);
		pane.add(tempL);

		//Weather Status Label
		JLabel wStatL = new JLabel("Weather:");
		wStatL.setLocation(10,40);
		wStatL.setSize(100,20);
		wStatL.setForeground(Color.WHITE);
		pane.add(wStatL);

		//Wind Label
		JLabel windL = new JLabel("Wind:");
		windL.setLocation(10,70);
		windL.setSize(100,20);
		windL.setForeground(Color.WHITE);
		pane.add(windL);

		//Humdity Label
		JLabel humL = new JLabel("Humidity:");
		humL.setLocation(10,100);
		humL.setSize(100,20);
		humL.setForeground(Color.WHITE);
		pane.add(humL);

		//Temperature Bar
		JLabel tempB = new JLabel("0");
		tempB.setLocation(140,10);
		tempB.setSize(100,20);
		tempB.setForeground(Color.WHITE);
		pane.add(tempB);

		//Weather Status
		JLabel wStatB = new JLabel("Sunny");
		wStatB.setLocation(140,40);
		wStatB.setSize(100,20);
		wStatB.setForeground(Color.WHITE);
		pane.add(wStatB);

		//Wind Bar
		JLabel windB = new JLabel("0");
		windB.setLocation(140,70);
		windB.setSize(100,20);
		windB.setForeground(Color.WHITE);
		pane.add(windB);

		//Humdity Bar
		JLabel humB = new JLabel("0");
		humB.setLocation(140,100);
		humB.setSize(100,20);
		humB.setForeground(Color.WHITE);
		pane.add(humB);
	}
	public static void main(String[] args){
		try {
			UIManager.setLookAndFeel(
					UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) { }

		createAndShowGUI();
	}
}
