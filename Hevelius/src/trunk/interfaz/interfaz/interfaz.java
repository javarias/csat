import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class interfaz {
	public static void main(String[] args){
		try {
		    UIManager.setLookAndFeel(
		        UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) { }

		
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;
		JCheckBoxMenuItem cbMenuItem;
		JRadioButtonMenuItem rbMenuItem;
		
		
	
		

		
		JFrame frame = new JFrame("Hevelius v0.0.1");
		frame.setSize(800,600);
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{			
				System.exit(0);
			}
		});
		frame.pack();
		frame.setVisible(true);
		
		
		
/*		Graphics dibujito = frame.getGraphics();
		dibujito.setColor(Color.BLACK);
		dibujito.clearRect(0, 0, 800, 600);
		frame.update(dibujito);
		*/
		
		frame.setBackground(Color.BLACK);
		
		
		
		
		
		
//		Create the menu bar.
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

//		Build the first menu.
		menu = new JMenu("File");
		menuBar.add(menu);

//		a group of JMenuItems
		menuItem = new JMenuItem("Close");
		menu.add(menuItem);
/*
//		a group of radio button menu items
		menu.addSeparator();
		ButtonGroup group = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
		rbMenuItem.setSelected(true);
		group.add(rbMenuItem);
		menu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Another one");
		group.add(rbMenuItem);
		menu.add(rbMenuItem);

//		a group of check box menu items
		menu.addSeparator();
		cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
		menu.add(cbMenuItem);
		cbMenuItem = new JCheckBoxMenuItem("Another one");
		menu.add(cbMenuItem);

//		a submenu
		menu.addSeparator();
		submenu = new JMenu("A submenu");
		menuItem = new JMenuItem("An item in the submenu");
		submenu.add(menuItem);
		menuItem = new JMenuItem("Another item");
		submenu.add(menuItem);
		menu.add(submenu);
*/
//		Build second menu in the menu bar.
		menu = new JMenu("About");
		menuBar.add(menu);
		
		
	}
	
	
	
}