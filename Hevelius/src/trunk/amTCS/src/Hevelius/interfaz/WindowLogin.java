package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class WindowLogin extends JPanel implements Runnable
{
	private JLabel login;
	private static JFrame frame;
	private static JDialog log;
	private static DrawingPanel panel = interfaz.getDrawingPanel();	

	public WindowPanel(LayoutManager 1)
	{
		super(1);
	}
	public void init()
	{
		setBackground(Color.BLACK);
		aboutWindow();

	}
	log = new JDialog(frame,"Hevelius - Login");
	log.setLayout(null);
	logg.pack();

	Image icono = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("Hevelius/images/hevelius.png"));

	log.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			log.setVisible(false);
			setConfigWindow();
			}

			public void windowClosed(WindowEvent e) {
			log.setVisible(false);
			setConfigWindow();
			}
			});

	log.setSize(50,50);
	log.setLocationRelativeTo(null);
        log.setResizable(false);


}

