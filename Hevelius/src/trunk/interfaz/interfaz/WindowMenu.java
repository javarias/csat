
import java.awt.Container;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


public class WindowMenu extends JMenuBar
{

	JMenuBar menuBar;

	JMenu fileMenu;
	JMenuItem fileExit;

	JMenu aboutMenu;
	JMenuItem aboutabout;


	public WindowMenu()
	{
		setupMenuBar();
		setupEventHandlers();


	}

	void setupMenuBar()
	{
		menuBar = new JMenuBar();

		fileMenu = new JMenu("File");
		fileExit = new JMenuItem("Exit");

		fileMenu.add(fileExit);
		
		aboutMenu = new JMenu("About");
		aboutabout = new JMenuItem("ABout");

		fileMenu.add(fileExit);

		menuBar.add(fileMenu);
		menuBar.add(aboutMenu);

//		setJMenuBar(menuBar);

	}



	void setupEventHandlers()
	{
		//addWindowListener(new WindowHandler());
        fileExit.addActionListener(new MenuItemHandler());
  	}


  	public class MenuItemHandler implements ActionListener
  	{
  		public void actionPerformed(ActionEvent e)
  		{
  			String cmd = e.getActionCommand();
  			if(cmd.equals("Exit")) System.exit(0);
  	//		else cajaTexto.setText(cmd);

  		}
  	}


	public class ItemHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			AbstractButton boton = (AbstractButton) e.getItem();
			String etiqueta = boton.getText();
			if(boton.isSelected()) etiqueta+= "true";
	/*		else etiqueta += "false";
			cajaTexto.setText(etiqueta);*/
		}
	}



}
