package Hevelius.interfaz;

import java.io.*;
import javax.imageio.*;
import java.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Hevelius.weather.*;

public class interfaz {
	private static JFrame frame;
	private static DrawingPanel pane;

	private static JDialog config;
	private static JDialog about;
	private static Configuration test = new Configuration();

	private static JTabbedPane tabbedPane;
	private static JPanel panel1;
	private static JLabel coordtype;
	private static JRadioButton radec;
	private static JRadioButton altaz;
	private static ButtonGroup coord;
	private static JLabel city;
	private static JTextField city2find;
	private static JButton find;
	private static JComboBox location;
	private static JLabel tracking;
        private static JRadioButton activate_track;
        private static JRadioButton desactivate_track;
        private static ButtonGroup track;	

	private static JPanel panel2;
	private static JLabel modules;
	private static JCheckBox weather;
	private static JCheckBox opengl;
	private static JCheckBox compass;
	private static JLabel background;
	private static JComboBox color;

	private static Vector<WeatherCityId> vector_city = new Vector<WeatherCityId>();



	static String[ ] fileItems = new String[ ] { "Exit" };
	static String[ ] editItems = new String[ ] { "Configuration" };
	static char[ ] fileShortcuts = { 'Q' };
	static char[ ] editShortcuts = { 'P' };	

	public static JFrame getMainFrame()
	{
		return frame;
	}

	public static DrawingPanel getDrawingPanel()
	{
		return pane;
	}

	public static JMenuBar createMenuBar() {
		JMenuBar menu = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		JMenu aboutMenu = new JMenu("Help");
		JMenu subMenu = new JMenu("SubMenu");
		JMenu subMenu2 = new JMenu("SubMenu2");

		// Assemble the File menus with mnemonics.
		ActionListener printListener = new ActionListener(  ) {
			public void actionPerformed(ActionEvent event) {
				if(event.getActionCommand().compareTo("Exit")==0){
					System.exit(0);
				}
				else{
					if(event.getActionCommand().compareTo("Configuration")==0){
						//frame.setVisible(false);
						config.setVisible(true);
					}
					else{
						if(event.getActionCommand().compareTo("About Hevelius")==0){
							//frame.setVisible(false);
							about.setVisible(true);
						}
					/*	else{
							System.out.println("Menu item [" + 
									event.getActionCommand(  ) + "] was pressed.");
						}*/
					}
				}
			}
		};
		for (int i=0; i < fileItems.length; i++) {
			JMenuItem item = new JMenuItem(fileItems[i], fileShortcuts[i]);
			item.addActionListener(printListener);
			fileMenu.add(item);
		}

		// Assemble the File menus with keyboard accelerators.
		for (int i=0; i < editItems.length; i++) {
			JMenuItem item = new JMenuItem(editItems[i]);
			item.setAccelerator(KeyStroke.getKeyStroke(editShortcuts[i],
						Toolkit.getDefaultToolkit(  ).getMenuShortcutKeyMask(  ), false));

			/* if(item.getName().compareTo("Configuration") == 0){
			   item.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
			   frame.setVisible(false);
			   config.setVisible(true);
			   }

			   });
			   }
			   else{*/
			item.addActionListener(printListener);
			//}

			editMenu.add(item);
		}

		JMenuItem item;
		
		aboutMenu.add(item = new JMenuItem("About Hevelius"));
		item.addActionListener(printListener);

		// Finally, add all the menus to the menu bar.
		menu.add(fileMenu);
		menu.add(editMenu);
		menu.add(aboutMenu);

		return menu;
	}



	public static void configWindow() {

		config = new JDialog(frame,"Hevelius - Configuration");
		config.setLayout(null);
		config.pack();	

		Image icono = Toolkit.getDefaultToolkit().getImage("Hevelius/images/hevelius.png");
		//config.setIconImage(icono);

		config.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
				config.setVisible(false);
				setConfigWindow();
				}

				public void windowClosed(WindowEvent e) {
				config.setVisible(false);
				setConfigWindow();
				}
				});

		config.setSize(500,600);
		config.setLocationRelativeTo(null);

		config.setResizable(false);

		tabbedPane = new JTabbedPane();
		tabbedPane.setSize(490,530);
		tabbedPane.setLocation(0,0);

		panel1 = new JPanel();
		tabbedPane.addTab("System",panel1);
		//tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		panel1.setLayout(null);


		coordtype = new JLabel("Coordinate Selection: ");
		coordtype.setLocation(10,10);
		coordtype.setSize(150,20);
		panel1.add(coordtype);

		radec = new JRadioButton("RaDec");
		altaz = new JRadioButton("Horizontal");
		coord = new ButtonGroup();
		
		try{
			if(Integer.parseInt(test.getOption("coordinate"))==0)
				radec.setSelected(true);
			if(Integer.parseInt(test.getOption("coordinate"))==1)
				altaz.setSelected(true);
		}catch(NumberFormatException e){
			radec.setSelected(true);
		}
		coord.add(radec);
		coord.add(altaz);

		radec.setLocation(150,13);
		altaz.setLocation(150,33);
		radec.setSize(150,15);
		altaz.setSize(150,15);
		panel1.add(radec);
		panel1.add(altaz);		

		city = new JLabel("Location: ");
		city.setLocation(10,70);
		city.setSize(150,20);
		panel1.add(city);

		city2find = new JTextField(50);
		city2find.setLocation(150,70);
		city2find.setSize(100,20);
		city2find.setText(test.getOption("location"));
		panel1.add(city2find);
		
		find = new JButton("Find");
		find.setLocation(260,70);
		find.setSize(70,20);
		panel1.add(find);

		location = new JComboBox();
		location.setModel(new DefaultComboBoxModel(new String[]{
					"(none)"}));
		location.setLocation(150,90);
		location.setSize(200,20);
		location.setSelectedIndex(0);
		panel1.add(location);

		tracking = new JLabel("Tracking: ");
                tracking.setLocation(10,110);
                tracking.setSize(150,20);
                panel1.add(tracking);

                activate_track = new JRadioButton("Enable");
                desactivate_track = new JRadioButton("Disable");
                track = new ButtonGroup();

                try{
                        if(Integer.parseInt(test.getOption("tracking"))==0)
                                desactivate_track.setSelected(true);
                        if(Integer.parseInt(test.getOption("tracking"))==1)
                                activate_track.setSelected(true);
                }catch(NumberFormatException e){
                        activate_track.setSelected(true);
                }
                coord.add(activate_track);
                coord.add(desactivate_track);

                activate_track.setLocation(250,13);
                desactivate_track.setLocation(250,33);
                activate_track.setSize(150,15);
                desactivate_track.setSize(150,15);
                panel1.add(activate_track);
                panel1.add(desactivate_track);


		panel2 = new JPanel();
		tabbedPane.addTab("Interface",panel2);
		//tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		panel2.setLayout(null);

		modules = new JLabel("Modules:");
		modules.setLocation(10,10);
		modules.setSize(70,20);
		panel2.add(modules);

		weather = new JCheckBox("Weather");
		weather.setLocation(150,10);
		weather.setSize(150,20);
		try{
			if(Integer.parseInt(test.getOption("weather"))==1)
				weather.setSelected(true);
		}catch(NumberFormatException e){
			weather.setSelected(true);
		}
		panel2.add(weather);

		opengl = new JCheckBox("OpenGL Model");
		opengl.setLocation(150,30);
		opengl.setSize(150,20);
		try{
			if(Integer.parseInt(test.getOption("opengl"))==1)
				opengl.setSelected(true);
		}catch(NumberFormatException e){
			opengl.setSelected(true);
		}
		panel2.add(opengl);

		compass = new JCheckBox("Compass");
		compass.setLocation(150,50);
		compass.setSize(150,20);
		try{
			if(Integer.parseInt(test.getOption("compass"))==1)
				compass.setSelected(true);
		}catch(NumberFormatException e){
			compass.setSelected(true);
		}
		panel2.add(compass);

		background = new JLabel("Background Color:");
		background.setLocation(10,90);
		background.setSize(150,20);
		panel2.add(background);

		color = new JComboBox();
		color.setModel(new DefaultComboBoxModel(new String[]{
					"Black",
					"Blue",
					"Cyan",
					"DarkGray",
					"Gray",
					"Green",
					"LightGray",
					"Magenta",
					"Orange",
					"Pink",
					"Red",
					"Yellow"}));
		color.setLocation(150,90);
		color.setSize(200,20);
		try{
			color.setSelectedIndex(Integer.parseInt(test.getOption("background")));
		}catch(NumberFormatException e){
			color.setSelectedIndex(0);
		}
		panel2.add(color);	



		config.add(tabbedPane);

		JButton close = new JButton("Close");
		JButton save = new JButton("Save");

		close.setLocation(340,540);
		close.setSize(70,25);
		config.add(close);

		save.setLocation(415,540);
		save.setSize(70,25);
		config.add(save);

		saveConfig();

		close.addActionListener(new ActionListener(  ) {
				public void actionPerformed(ActionEvent event) {
				config.setVisible(false);
				setConfigWindow();
				}
				});

		save.addActionListener(new ActionListener(  ) {
				public void actionPerformed(ActionEvent event) {
				//System.out.println(radec.isSelected() + " " + altaz.isSelected());
				if(radec.isSelected())
				test.setOption("coordinate","0");
				if(altaz.isSelected())
				test.setOption("coordinate","1");

				if(weather.isSelected())
				test.setOption("weather","1");
				else
				test.setOption("weather","0");

				if(opengl.isSelected())
				test.setOption("opengl","1");
				else
				test.setOption("opengl","0");

				if(activate_track.isSelected())
                                test.setOption("tracking","1");
                                if(desactivate_track.isSelected())
                                test.setOption("tracking","0");

				if(compass.isSelected())
				test.setOption("compass","1");
				else
					test.setOption("compass","0");

				test.setOption("background", String.valueOf(color.getSelectedIndex()));

				if(String.valueOf(location.getSelectedItem()).compareTo("(none)") != 0)				
					test.setOption("location",String.valueOf(vector_city.get(
						location.getSelectedIndex()).getId()));

				test.store();

				pane.updateWindow(false);

				}
		});

		find.addActionListener(new ActionListener(  ) {
                                public void actionPerformed(ActionEvent event) {
					if(city2find.getText().trim().compareTo("") != 0){
						location.removeAllItems();						
						WeatherCity weather=  new WeatherCity(city2find.getText().trim());
						vector_city.clear();
						vector_city = weather.ListCity();
						
						if(vector_city.size() > 0)
							for(int i = 0; i < vector_city.size(); i++)
								location.addItem(vector_city.get(i).getNameCity());
							//System.out.println(vector_city.get(i).getNameCity());
						
						location.addItem("(none)");
					}
                                }
                });


	}


	public static void saveConfig(){
		//System.out.println(radec.isSelected() + " " + altaz.isSelected());
		if(radec.isSelected())
			test.setOption("coordinate","0");
		if(altaz.isSelected())
			test.setOption("coordinate","1");

		if(weather.isSelected())
			test.setOption("weather","1");
		else
			test.setOption("weather","0");

		if(opengl.isSelected())
			test.setOption("opengl","1");
		else
			test.setOption("opengl","0");

		if(activate_track.isSelected())
                        test.setOption("tracking","1");
                if(desactivate_track.isSelected())
                        test.setOption("tracking","0");

		if(compass.isSelected())
			test.setOption("compass","1");
		else
			test.setOption("compass","0");

		test.setOption("background", String.valueOf(color.getSelectedIndex()));

		if(String.valueOf(location.getSelectedItem()).compareTo("(none)") != 0)				
			test.setOption("location",String.valueOf(vector_city.get(
				location.getSelectedIndex()).getId()));

		test.store();

	}



	public static void setConfigWindow(){
		if(Integer.parseInt(test.getOption("coordinate"))==0)
			radec.setSelected(true);
		if(Integer.parseInt(test.getOption("coordinate"))==1)
			altaz.setSelected(true);
		
		city2find.setText(test.getOption("location"));
		
		location.removeAllItems();
		location.addItem("(none)");
		location.setSelectedIndex(0);
		
		if(Integer.parseInt(test.getOption("tracking"))==0)
                        desactivate_track.setSelected(true);
                if(Integer.parseInt(test.getOption("tracking"))==1)
                        activate_track.setSelected(true);

		if(Integer.parseInt(test.getOption("weather"))==1)
			weather.setSelected(true);
		else
			weather.setSelected(false);
		
		if(Integer.parseInt(test.getOption("opengl"))==1)
			opengl.setSelected(true);
		else
			opengl.setSelected(false);
		
		if(Integer.parseInt(test.getOption("compass"))==1)
			compass.setSelected(true);
		else
			compass.setSelected(false);

		color.setSelectedIndex(Integer.parseInt(test.getOption("background")));
	}	



	public static void aboutWindow() {
		about = new JDialog(frame,"Hevelius - About");
		about.pack();	

		about.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
				about.setVisible(false);
				}

				public void windowClosed(WindowEvent e) {
				about.setVisible(false);
				}
				});

		about.setSize(340,300);
		about.setLocationRelativeTo(null);
		about.setLayout(null);
		about.setResizable(false);
		about.getContentPane().setBackground(Color.WHITE);


		JLabel about1 = new JLabel("Hevelius v. ALFA rc1");
		JLabel about2 = new JLabel("DevNull Enterprise");
		JLabel about3 = new JLabel("2007");
		
		Icon logoImagen = new ImageIcon("Hevelius/images/logo.png");
		JLabel logo = new JLabel(logoImagen);

		about1.setLocation(50,15);
		about1.setFont(new Font("DejaVu Sans",1,18));
		about1.setSize(250,20);
		about1.setHorizontalAlignment(SwingConstants.CENTER);
		about.add(about1);

		about2.setLocation(95,30);
		about2.setSize(150,20);
		about2.setHorizontalAlignment(SwingConstants.CENTER);
		about.add(about2);

		about3.setLocation(95,50);
		about3.setSize(150,20);
		about3.setHorizontalAlignment(SwingConstants.CENTER);
		about.add(about3);

		logo.setLocation(10,70);
		logo.setSize(300,250);
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		about.add(logo);

	}

	private static void createAndShowGUI()
	{
		long time1 = System.currentTimeMillis();
		long time2 = System.currentTimeMillis();

		//Intro
		JFrame intro = new JFrame("Hevelius v. ALFA rc1");
		IntroPanel in = new IntroPanel(null);
		intro.setContentPane(in);
		intro.setUndecorated(true);
		intro.pack();
		in.setBackground(Color.WHITE);
		intro.setResizable(false);
		intro.setSize(400,200);
		intro.setLocationRelativeTo(null);
		intro.setVisible(true);

		frame = new JFrame("Hevelius v. ALFA rc1");
		Image icono = null;
		try
		{
			icono = ImageIO.read(new File("Hevelius/images/hevelius.png"));
		}
		catch(IOException e)
		{
		}
		frame.setIconImage(icono);
		pane = new DrawingPanel(null);
		pane.init();
		pane.updateWindow(true);
		frame.setContentPane(pane);
		Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.addWindowListener(new WindowAdapter()
				{
					public void windowClosing(WindowEvent e)
		{
			System.exit(0);
		}
		});

		switch(Integer.parseInt(test.getOption("background"))){
			case 0: pane.setBackground(Color.BLACK); break;
			case 1: pane.setBackground(Color.BLUE); break;
			case 2: pane.setBackground(Color.CYAN); break;
			case 3: pane.setBackground(Color.DARK_GRAY); break;
			case 4: pane.setBackground(Color.GRAY); break;
			case 5: pane.setBackground(Color.GREEN); break;
			case 6: pane.setBackground(Color.LIGHT_GRAY); break;
			case 7: pane.setBackground(Color.MAGENTA); break;
			case 8: pane.setBackground(Color.ORANGE); break;
			case 9: pane.setBackground(Color.PINK); break;
			case 10: pane.setBackground(Color.RED); break;
			case 11: pane.setBackground(Color.YELLOW); break;
			default: pane.setBackground(Color.BLACK); break;
		}

		frame.setResizable(false);

		frame.setJMenuBar(createMenuBar());
		frame.pack();
		frame.setSize(ScreenSize);
		while(time2-time1<5000)
			time2 = System.currentTimeMillis();
		intro.setVisible(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		time1 = System.currentTimeMillis();
		time2 = System.currentTimeMillis();
		while(time2-time1<500)
			time2 = System.currentTimeMillis();
	}

	public static void main(String[] args){
		try {
			UIManager.setLookAndFeel(
					UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) { }

		aboutWindow();
		configWindow();
		createAndShowGUI();
	}
}
