import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class interfaz {
	public static JFrame frame;
	public static DrawingPanel pane;



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

	private static JPanel panel2;
	private static JLabel modules;
	private static JCheckBox weather;
	private static JCheckBox opengl;
	private static JCheckBox compass;
	private static JLabel background;
	private static JComboBox color;



	static String[ ] fileItems = new String[ ] { "Exit" };
	static String[ ] editItems = new String[ ] { "Configuration" };
	static char[ ] fileShortcuts = { 'Q' };
	static char[ ] editShortcuts = { 'P' };	

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
						else{
							System.out.println("Menu item [" + 
									event.getActionCommand(  ) + "] was pressed.");
						}
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

		// Insert a separator in the Edit menu in Position 1 after "Undo".
		//editMenu.insertSeparator(1);

		// Assemble the submenus of the Other menu.
		JMenuItem item;
		/*   subMenu2.add(item = new JMenuItem("Extra 2"));
		     item.addActionListener(printListener);
		     subMenu.add(item = new JMenuItem("Extra 1"));
		     item.addActionListener(printListener);
		     subMenu.add(subMenu2);

		// Assemble the Other menu itself.
		aboutMenu.add(subMenu);
		aboutMenu.add(item = new JCheckBoxMenuItem("Check Me"));
		item.addActionListener(printListener);
		aboutMenu.addSeparconfigator(  );
		ButtonGroup buttonGroup = new ButtonGroup(  );
		aboutMenu.add(item = new JRadioButtonMenuItem("Radio 1"));
		item.addActionListener(printListener);
		buttonGroup.add(item);
		aboutMenu.add(item = new JRadioButtonMenuItem("Radio 2"));
		item.addActionListener(printListener);
		buttonGroup.add(item);
		aboutMenu.addSeparator(  );*/
		aboutMenu.add(item = new JMenuItem("About Hevelius"));
		item.addActionListener(printListener);

		// Finally, add all the menus to the menu bar.
		menu.add(fileMenu);
		menu.add(editMenu);
		menu.add(aboutMenu);


		//   PropertiesConfiguration config = new PropertiesConfiguration("app.windows.properties");



		return menu;
	}



	public static void configWindow() {


		config = new JDialog(frame,"Hevelius - Configuration");
		config.setLayout(null);
		config.pack();	

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

		if(Integer.parseInt(test.getOption("coordinate"))==0)
			radec.setSelected(true);
		if(Integer.parseInt(test.getOption("coordinate"))==1)
			altaz.setSelected(true);
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
		if(Integer.parseInt(test.getOption("weather"))==1)
			weather.setSelected(true);
		panel2.add(weather);

		opengl = new JCheckBox("OpenGL Model");
		opengl.setLocation(150,30);
		opengl.setSize(150,20);
		if(Integer.parseInt(test.getOption("opengl"))==1)
			opengl.setSelected(true);
		panel2.add(opengl);

		compass = new JCheckBox("Compass");
		compass.setLocation(150,50);
		compass.setSize(150,20);
		if(Integer.parseInt(test.getOption("compass"))==1)
			compass.setSelected(true);
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
					"White",
					"Yellow"}));
		color.setLocation(150,90);
		color.setSize(200,20);
		color.setSelectedIndex(Integer.parseInt(test.getOption("background")));
		panel2.add(color);	



		/*	JPanel panel3 = new JPanel();
			tabbedPane.addTab("Tab 3",panel3);
		//tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
		panel3.setLayout(null);
		 */

		config.add(tabbedPane);

		JButton close = new JButton("Close");
		JButton save = new JButton("Save");

		close.setLocation(340,540);
		close.setSize(70,25);
		config.add(close);

		save.setLocation(415,540);
		save.setSize(70,25);
		config.add(save);


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

				if(compass.isSelected())
				test.setOption("compass","1");
				else
					test.setOption("compass","0");

				test.setOption("background", String.valueOf(color.getSelectedIndex()));

				test.store();


				}
		});




	}

	public static void setConfigWindow(){
		if(Integer.parseInt(test.getOption("coordinate"))==0)
			radec.setSelected(true);
		if(Integer.parseInt(test.getOption("coordinate"))==1)
			altaz.setSelected(true);
		
		city2find.setText("");
		
		location.setSelectedIndex(0);



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

		about.setSize(400,400);
		about.setLocationRelativeTo(null);
		about.setLayout(null);
		about.setResizable(false);


		JLabel about1 = new JLabel("Hevelius v. ALFA rc1");
		JLabel about2 = new JLabel("DevNull Enterprise");
		JLabel about3 = new JLabel("2007");


		about1.setLocation(100,15);
		about1.setFont(new Font("DejaVu Sans",1,18));
		about1.setSize(250,20);
		about.add(about1);

		about2.setLocation(150,30);
		about2.setSize(150,20);
		about.add(about2);

		about3.setLocation(185,70);
		about3.setSize(150,20);
		about.add(about3);

		JButton close = new JButton("Close");

		close.setLocation(160,300);
		close.setSize(70,25);
		about.add(close);

		close.addActionListener(new ActionListener(  ) {
				public void actionPerformed(ActionEvent event) {
				about.setVisible(false);
				}
				});
	}

	private static void createAndShowGUI()
	{
		frame = new JFrame("Hevelius v0.0.1");
		pane = new DrawingPanel(null);
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
			case 11: pane.setBackground(Color.WHITE); break;
			case 12: pane.setBackground(Color.YELLOW); break;
			default: pane.setBackground(Color.BLACK); break;
		}

		frame.setResizable(false);
		long time1 = System.currentTimeMillis();
		long time2 = System.currentTimeMillis();

		//Presentacion
		JFrame intro = new JFrame();
		intro.setUndecorated(true);
		intro.pack();
		intro.setResizable(false);
		intro.setSize(400,200);
		intro.setLocationRelativeTo(null);
		intro.setVisible(true);
		while(time2-time1<500)
			time2 = System.currentTimeMillis();
		intro.setVisible(false);


		frame.setJMenuBar(createMenuBar());
		frame.pack();
		frame.setSize(ScreenSize);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		time1 = System.currentTimeMillis();
		time2 = System.currentTimeMillis();
		while(time2-time1<500)
			time2 = System.currentTimeMillis();
		//frame.setJMenuBar(createMenuBar());

		//Label Coordinates
		JLabel coor;
		switch(Integer.parseInt(test.getOption("coordinate"))){
			case 0: coor = new JLabel("Coordinates RaDec"); break;
			case 1: coor = new JLabel("Coordinates Horizontal"); break;
			default: coor = new JLabel("Coordinates RaDec"); break;
		}

		coor.setLocation(/*dist-*/15,240);
		coor.setSize(250,20);
		coor.setForeground(Color.WHITE);
		pane.add(coor);
	}

	public static void main(String[] args){
		try {
			UIManager.setLookAndFeel(
					UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) { }
		
		configWindow();
		createAndShowGUI();
		aboutWindow();
	}
}
