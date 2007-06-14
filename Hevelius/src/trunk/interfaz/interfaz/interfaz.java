import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class interfaz {
	private static JFrame frame;
	private static void createAndShowGUI()
	{
		frame = new JFrame("Hevelius v0.0.1");
		//frame.setSize(800,600);
		//frame.getContentPane().setBackground(Color.BLACK);
		frame.addWindowListener(new WindowAdapter()
				{
					public void windowClosing(WindowEvent e)
		{
			System.exit(0);
		}
		});
		aim circ = new aim();
		frame.getContentPane().add(circ, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.setSize(800,600);
		frame.getContentPane().setBackground(Color.BLACK);
	}
	public static void main(String[] args){
		try {
			UIManager.setLookAndFeel(
					UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) { }

		createAndShowGUI();
	}
}
