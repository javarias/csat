package Hevelius.interfaz;

//import Hevelius.utilities.converter.*;
import Hevelius.acsmodules.*;
import Hevelius.heveliusmodules.*;
import Hevelius.interfaz.*;
import Hevelius.utilities.converter.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
//import java.awt.event.MouseEvent.*;
import java.text.DecimalFormat;
import alma.TYPES.*;
import Hevelius.weather.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;
import java.awt.Image;
import javax.swing.border.BevelBorder;
import java.util.regex.*;

public class MenuPanel extends JPanel //implements Runnable
{
	//VARIABLES
	private JLabel conf;

	private int dy;
	private int dx;

	private static JFrame frame;
	private static DrawingPanel pane = interfaz.getDrawingPanel();
	private Image hevelius = null;	

	private static JDialog config;
	private static JDialog about;
	private static JDialog history;
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

   	//private static JTextArea text_hist;
   	//private static Color B, F1, F2, F3;
	private static JComboBox jtfInput;
        private static JTextArea jtAreaOutput;
	private static JLabel jtText;


	private static Vector<WeatherCityId> vector_city = new Vector<WeatherCityId>();

	//private static JButton menu_config;
	private static JLabel menu_config;
	private static JLabel menu_help;
	private static JLabel menu_login;
	private static JLabel menu_history;
	private WindowLogin wl = new WindowLogin(interfaz.getMainFrame(), "Hevelius - Login");
	//private static JButton menu_about;


	//CONSTRUCTOR
	public MenuPanel(LayoutManager l)
	{
		super(l);
	}

	//INIT
	public void init()
	{	
		//dy = getSize().height;
		//dx = getSize().width;

		setBackground(Color.BLACK);
		aboutWindow();
		configWindow();
		historyWindow();
		wl.init();

		menu_config = new JLabel(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/conf.png")).getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH)));
		menu_config.setBackground(Color.BLACK);
		menu_config.setSize(50,50);
		add(menu_config);

		ImageIcon hbutton = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/help.png")).getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH));

		menu_help = new JLabel(hbutton);
		menu_help.setBackground(Color.BLACK);
		//menu_config.setLocation(5*dx/6,dy/2);
		menu_help.setSize(50,50);
		add(menu_help);

		menu_history = new JLabel(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/history.png")).getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH)));
                menu_history.setBackground(Color.BLACK);
                menu_history.setSize(50,50);
                add(menu_history);

		menu_login = new JLabel(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/login.png")).getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH)));
                menu_login.setBackground(Color.BLACK);
                menu_login.setSize(50,50);
                add(menu_login);

	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		boolean updatePanel = false;

		if(dx != getSize().width || dy != getSize().height)
		{
			updatePanel = true;
		}
		dy = getSize().height;
		dx = getSize().width;

		if(updatePanel)
		{
			menu_config.setSize(dy-dy*12/40,dy-dy*12/40);
			menu_config.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/conf.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));



			menu_config.addMouseListener(new MouseListener(){

					public void mouseExited(MouseEvent event){
					menu_config.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/conf.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
					}

					public void mouseEntered(MouseEvent event){
					menu_config.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/conf-encima.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
					}

					public void mouseReleased(MouseEvent event){
					menu_config.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/conf.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
					}

					public void mouseClicked(MouseEvent event){
					menu_config.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/conf-encima.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
					config.setVisible(true);
					setConfigWindow();			
					}

					public void mousePressed(MouseEvent event){
						menu_config.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/conf-click.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
					}

			});


			menu_config.setLocation(4*dx/6 + dy-dy*12/40 + dx/40,dy*6/40);

			hevelius = setImage("Hevelius/images/heveliusi.png",new Dimension(2*(dy-dy*20/40),dy-dy*12/40));

			menu_help.setSize(dy-dy*12/40,dy-dy*12/40);
			menu_help.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/help.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));

			menu_help.addMouseListener(new MouseListener(){

					public void mouseExited(MouseEvent event){
					menu_help.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/help.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
					}

					public void mouseEntered(MouseEvent event){
					menu_help.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/help-encima.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
					}

					public void mouseReleased(MouseEvent event){
					menu_help.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/help.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
					}

					public void mouseClicked(MouseEvent event){
					menu_help.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/help-encima.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
					about.setVisible(true);
					}

					public void mousePressed(MouseEvent event){
					menu_help.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/help-click.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
					}

			});


			menu_help.setLocation(4*dx/6 + 2*(dy-dy*12/40 + dx/40),dy*6/40);



			menu_history.setSize(dy-dy*12/40,dy-dy*12/40);
                        menu_history.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/history.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));


                        menu_history.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        menu_history.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/history.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        menu_history.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/history-encima.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        menu_history.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/history.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseClicked(MouseEvent event){
                                       menu_history.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/history-encima.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
                                        //WindowLogin wl = new WindowLogin(interfaz.getMainFrame(), "A");
                                        history.setVisible(true);
					setHistoryWindow(test.getOption("user"));
                                        }

                                        public void mousePressed(MouseEvent event){
                                                menu_history.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/history-click.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
                                        }

                        });


                        menu_history.setLocation(4*dx/6,dy*6/40);




                        menu_login.setSize(dy-dy*12/40,dy-dy*12/40);
                        menu_login.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/login.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));


                        menu_login.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        menu_login.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/login.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        menu_login.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/login-encima.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        menu_login.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/login.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
                                        }

                                        public void mouseClicked(MouseEvent event){
                                       menu_login.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/login-encima.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
					//WindowLogin wl = new WindowLogin(interfaz.getMainFrame(), "A");
                                        //wl.init();
					wl.setLoginWindow();
                                        }

                                        public void mousePressed(MouseEvent event){
                                                menu_login.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("Hevelius/images/login-click.png")).getImage().getScaledInstance(dy-dy*12/40,dy-dy*12/40,Image.SCALE_SMOOTH)));
                                        }

                        });


                        menu_login.setLocation(4*dx/6 - dy-dy*12/40 + dx/40,dy*6/40);


		}
		g.drawImage(hevelius,dy*6/40,dy*6/40,this);
	}

	//VENTANAS DEL MENU: CONFIGURATION Y ABOUT	

	public static void configWindow() {

		config = new JDialog(frame,"Hevelius - Configuration");
		config.setLayout(null);
		config.pack();	

		Image icono = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("Hevelius/images/hevelius.png"));
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
		tracking.setLocation(10,120);
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
		track.add(activate_track);
		track.add(desactivate_track);

		activate_track.setLocation(150,123);
		desactivate_track.setLocation(150,143);
		activate_track.setSize(150,15);
		desactivate_track.setSize(150,15);
		panel1.add(activate_track);
		panel1.add(desactivate_track);

/*
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

/*				if(weather.isSelected())
				test.setOption("weather","1");
				else
				test.setOption("weather","0");

				if(opengl.isSelected())
				test.setOption("opengl","1");
				else
				test.setOption("opengl","0");
*/
				if(activate_track.isSelected())
				test.setOption("tracking","1");
				if(desactivate_track.isSelected())
					test.setOption("tracking","0");

/*				if(compass.isSelected())
					test.setOption("compass","1");
				else
					test.setOption("compass","0");

				test.setOption("background", String.valueOf(color.getSelectedIndex()));
*/
				if(String.valueOf(location.getSelectedItem()).compareTo("(none)") != 0){				
					test.setOption("location",String.valueOf(vector_city.get(
									location.getSelectedIndex()).getId()));
					test.setOption("city",String.valueOf(vector_city.get(
									location.getSelectedIndex()).getNameCity()));
				}

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

/*		if(weather.isSelected())
			test.setOption("weather","1");
		else
			test.setOption("weather","0");

		if(opengl.isSelected())
			test.setOption("opengl","1");
		else
			test.setOption("opengl","0");
*/
		if(activate_track.isSelected())
			test.setOption("tracking","1");
		if(desactivate_track.isSelected())
			test.setOption("tracking","0");

/*		if(compass.isSelected())
			test.setOption("compass","1");
		else
			test.setOption("compass","0");

		test.setOption("background", String.valueOf(color.getSelectedIndex()));
*/
		if(String.valueOf(location.getSelectedItem()).compareTo("(none)") != 0){				
			test.setOption("location",String.valueOf(vector_city.get(
							location.getSelectedIndex()).getId()));
			test.setOption("city",String.valueOf(vector_city.get(
							location.getSelectedIndex()).getNameCity()));
		}

		test.store();

	}



	public static void setConfigWindow(){
		if(Integer.parseInt(test.getOption("coordinate"))==0)
			radec.setSelected(true);
		if(Integer.parseInt(test.getOption("coordinate"))==1)
			altaz.setSelected(true);

		city2find.setText(test.getOption("city"));

		location.removeAllItems();
		location.addItem("(none)");
		location.setSelectedIndex(0);

		if(Integer.parseInt(test.getOption("tracking"))==0)
			desactivate_track.setSelected(true);
		if(Integer.parseInt(test.getOption("tracking"))==1)
			activate_track.setSelected(true);

		/*if(Integer.parseInt(test.getOption("weather"))==1)
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

		color.setSelectedIndex(Integer.parseInt(test.getOption("background")));*/
	}	



	public static void aboutWindow() {
		about = new JDialog(frame,"Hevelius - Help");
		about.pack();	

		about.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
				about.setVisible(false);
				}

				public void windowClosed(WindowEvent e) {
				about.setVisible(false);
				}
				});

		about.setSize(800,600);
		about.setLocationRelativeTo(null);
//		about.setLayout(null);
		about.setResizable(false);
//		about.getContentPane().setBackground(Color.GRAY);


		final JEditorPane htmlPane = new JEditorPane();

		JPanel sidebar = new JPanel();
		sidebar.setBackground(Color.LIGHT_GRAY);
		final JLabel intr = new JLabel("Introduction");
		final JLabel req = new JLabel("Requirements");
		final JLabel conn = new JLabel("Connection");
		//final JLabel inst = new JLabel("Install");
		final JLabel comp = new JLabel("Interface");
		final JLabel obs = new JLabel("Observation");
		final JLabel cat = new JLabel("Catalogues");
		final JLabel faq = new JLabel("FAQ's");
		final JLabel ref = new JLabel("References");
		final JLabel abo = new JLabel("About Hevelius");

		JLabel line1 = new JLabel("|");
                JLabel line2 = new JLabel("|");
                JLabel line3 = new JLabel("|");
                JLabel line4 = new JLabel("|");
                JLabel line5 = new JLabel("|");
                JLabel line6 = new JLabel("|");
                JLabel line7 = new JLabel("|");
                JLabel line8 = new JLabel("|");
                JLabel line9 = new JLabel("|");

		intr.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        intr.setForeground(Color.BLACK);
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        intr.setForeground(Color.BLUE);
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        intr.setForeground(Color.BLACK);
                                        }

                                        public void mouseClicked(MouseEvent event){
                                        intr.setForeground(Color.BLUE);
					try{
                                        	htmlPane.setPage(MenuPanel.class.getClassLoader().getResource("Hevelius/manual/manual.html#intr"));
					}
					catch(IOException err){
						System.out.println("ERROR DEL HELP");
					}
                                        }

                                        public void mousePressed(MouseEvent event){
					intr.setForeground(Color.DARK_GRAY);
                                        }

                        });

                req.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        req.setForeground(Color.BLACK);
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        req.setForeground(Color.BLUE);
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        req.setForeground(Color.BLACK);
                                        }

                                        public void mouseClicked(MouseEvent event){
                                        req.setForeground(Color.BLUE);
                                        try{
                                                htmlPane.setPage(MenuPanel.class.getClassLoader().getResource("Hevelius/manual/manual.html#req"));
                                        }
                                        catch(IOException err){
                                                System.out.println("ERROR DEL HELP");
                                        }
                                        }

                                        public void mousePressed(MouseEvent event){
                                        req.setForeground(Color.DARK_GRAY);
                                        }

                        });

                conn.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        conn.setForeground(Color.BLACK);
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        conn.setForeground(Color.BLUE);
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        conn.setForeground(Color.BLACK);
                                        }

                                        public void mouseClicked(MouseEvent event){
                                        conn.setForeground(Color.BLUE);
                                        try{
                                                htmlPane.setPage(MenuPanel.class.getClassLoader().getResource("Hevelius/manual/manual.html#conn"));
                                        }
                                        catch(IOException err){
                                                System.out.println("ERROR DEL HELP");
                                        }
                                        }

                                        public void mousePressed(MouseEvent event){
                                        conn.setForeground(Color.DARK_GRAY);
                                        }

                        });

                comp.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        comp.setForeground(Color.BLACK);
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        comp.setForeground(Color.BLUE);
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        comp.setForeground(Color.BLACK);
                                        }

                                        public void mouseClicked(MouseEvent event){
                                        comp.setForeground(Color.BLUE);
                                        try{
                                                htmlPane.setPage(MenuPanel.class.getClassLoader().getResource("Hevelius/manual/manual.html#comp"));
                                        }
                                        catch(IOException err){
                                                System.out.println("ERROR DEL HELP");
                                        }
                                        }

                                        public void mousePressed(MouseEvent event){
                                        comp.setForeground(Color.DARK_GRAY);
                                        }

                        });

                obs.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        obs.setForeground(Color.BLACK);
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        obs.setForeground(Color.BLUE);
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        obs.setForeground(Color.BLACK);
                                        }

                                        public void mouseClicked(MouseEvent event){
                                        obs.setForeground(Color.BLUE);
                                        try{
                                                htmlPane.setPage(MenuPanel.class.getClassLoader().getResource("Hevelius/manual/manual.html#obs"));
                                        }
                                        catch(IOException err){
                                                System.out.println("ERROR DEL HELP");
                                        }
                                        }

                                        public void mousePressed(MouseEvent event){
                                        obs.setForeground(Color.DARK_GRAY);
                                        }

                        });

                cat.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        cat.setForeground(Color.BLACK);
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        cat.setForeground(Color.BLUE);
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        cat.setForeground(Color.BLACK);
                                        }

                                        public void mouseClicked(MouseEvent event){
                                        cat.setForeground(Color.BLUE);
                                        try{
                                                htmlPane.setPage(MenuPanel.class.getClassLoader().getResource("Hevelius/manual/manual.html#cat"));
                                        }
                                        catch(IOException err){
                                                System.out.println("ERROR DEL HELP");
                                        }
                                        }

                                        public void mousePressed(MouseEvent event){
                                        cat.setForeground(Color.DARK_GRAY);
                                        }

                        });

                faq.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        faq.setForeground(Color.BLACK);
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        faq.setForeground(Color.BLUE);
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        faq.setForeground(Color.BLACK);
                                        }

                                        public void mouseClicked(MouseEvent event){
                                        faq.setForeground(Color.BLUE);
                                        try{
                                                htmlPane.setPage(MenuPanel.class.getClassLoader().getResource("Hevelius/manual/manual.html#faq"));
                                        }
                                        catch(IOException err){
                                                System.out.println("ERROR DEL HELP");
                                        }
                                        }

                                        public void mousePressed(MouseEvent event){
                                        faq.setForeground(Color.DARK_GRAY);
                                        }

                        });

                ref.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        ref.setForeground(Color.BLACK);
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        ref.setForeground(Color.BLUE);
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        ref.setForeground(Color.BLACK);
                                        }

                                        public void mouseClicked(MouseEvent event){
                                        ref.setForeground(Color.BLUE);
                                        try{
                                                htmlPane.setPage(MenuPanel.class.getClassLoader().getResource("Hevelius/manual/manual.html#ref"));
                                        }
                                        catch(IOException err){
                                                System.out.println("ERROR DEL HELP");
                                        }
                                        }

                                        public void mousePressed(MouseEvent event){
                                        ref.setForeground(Color.DARK_GRAY);
                                        }

                        });

                abo.addMouseListener(new MouseListener(){

                                        public void mouseExited(MouseEvent event){
                                        abo.setForeground(Color.BLACK);
                                        }

                                        public void mouseEntered(MouseEvent event){
                                        abo.setForeground(Color.BLUE);
                                        }

                                        public void mouseReleased(MouseEvent event){
                                        abo.setForeground(Color.BLACK);
                                        }

                                        public void mouseClicked(MouseEvent event){
                                        abo.setForeground(Color.BLUE);
                                        try{
                                                htmlPane.setPage(MenuPanel.class.getClassLoader().getResource("Hevelius/manual/manual.html#abo"));
                                        }
                                        catch(IOException err){
                                                System.out.println("ERROR DEL HELP");
                                        }
                                        }

                                        public void mousePressed(MouseEvent event){
                                        abo.setForeground(Color.DARK_GRAY);
                                        }

                        });

		
		sidebar.add(intr);
                sidebar.add(line1);
                sidebar.add(req);
                sidebar.add(line2);
                sidebar.add(conn);
                //sidebar.add(line3);
                //sidebar.add(inst);
                sidebar.add(line4);
                sidebar.add(comp);
                sidebar.add(line5);
                sidebar.add(obs);
                sidebar.add(line6);
                sidebar.add(cat);
                sidebar.add(line7);
                sidebar.add(faq);
                sidebar.add(line8);
                sidebar.add(ref);
                sidebar.add(line9);
                sidebar.add(abo);
		

		about.getContentPane().add(sidebar, BorderLayout.NORTH);


		try{
		//htmlPane = new JEditorPane("http://www.google.cl");
		htmlPane.setEditable(false);
		htmlPane.addHyperlinkListener(new HyperlinkListener(){
				public void hyperlinkUpdate(HyperlinkEvent event){
					if(event.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
						try{
							htmlPane.setPage(event.getURL());
						}
						catch(IOException ioe){
							System.out.println("ERROR DEL HELP");
						}
					}
				}
			}
		);		

		JScrollPane scrollPane = new JScrollPane(htmlPane);
		about.getContentPane().add(scrollPane, BorderLayout.CENTER);
		htmlPane.setPage(MenuPanel.class.getClassLoader().getResource("Hevelius/manual/manual.html"));
		}
		catch(IOException e){
			System.out.println("ERROR DEL HELP");
		}
		

	}

	public Image setImage(String img, Dimension dim)
	{
		Image imag = null;
		try
		{
			imag = ImageIO.read(getClass().getClassLoader().getResource(img));
		}
		catch(IOException e)
		{
		}
		imag = Transparency.makeColorTransparent(imag, Color.BLACK);
		imag = imag.getScaledInstance(dim.width,dim.height,Image.SCALE_FAST);
		return imag;
	}

	public WindowLogin getWindowLogin(){
		return wl;
	}

	public static void historyWindow() {
		history = new JDialog(frame,"Hevelius - History");
                history.pack();

                history.addWindowListener(new WindowAdapter() {
                                public void windowClosing(WindowEvent e) {
                                history.setVisible(false);
                                }

                                public void windowClosed(WindowEvent e) {
                                history.setVisible(false);
                                }
                                });

                history.setSize(500,600);
                history.setLocationRelativeTo(null);
                history.setLayout(null);
                history.setResizable(false);
		history.getContentPane().setBackground(Color.BLACK);

/*		JComboBox jtfInput;
		JTextArea jtAreaOutput;
*/
		
		jtText = new JLabel("User:");
		jtText.setForeground(Color.WHITE);
		jtText.setSize(100,20);
		jtText.setLocation(10,10);
		history.add(jtText);
		
		
		jtfInput = new JComboBox();

		jtAreaOutput = new JTextArea(30, 40);
		jtAreaOutput.setCaretPosition(jtAreaOutput.getDocument().getLength());
		jtAreaOutput.setEditable(false);
		jtAreaOutput.setBackground(Color.LIGHT_GRAY);
		jtAreaOutput.setForeground(Color.BLACK);
		JScrollPane scrollPane = new JScrollPane(jtAreaOutput,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
/*		GridBagConstraints gridCons1 = new GridBagConstraints();
       		gridCons1.gridwidth = GridBagConstraints.REMAINDER;
        	gridCons1.fill = GridBagConstraints.HORIZONTAL;*/

		//jtfInput.setModel(new DefaultComboBoxModel(new String[]{
                //                        "(none)"}));


                String[] children;
		File dir = new File(System.getProperty("user.home") + "/.hevelius/history");


                FilenameFilter filter = new FilenameFilter()
                {
                        public boolean accept(File dir, String name)
                        {
                                Pattern pat;
                                Matcher mat;
                                pat = Pattern.compile("(.*)\\.log$");
                                File cat;
                                mat = pat.matcher(name);
                                if(mat.find())
                                {
                                        return true;
                                }
                                else
                                {
                                        return false;
                                }
                        }
                };
                children = dir.list(filter);
		
		int op = 0;

		for(int i = 0; i < children.length; i++){
			children[i] = children[i].substring(0,children[i].length()-4);
			if(children[i].compareTo(test.getOption("user")) == 0){
				op = i;
			}
		}


                jtfInput.setModel(new DefaultComboBoxModel(children));

                jtfInput.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                //object.getSelectedIndex()
                                setHistoryWindow(String.valueOf(jtfInput.getSelectedItem()));
                                }
                                });



                jtfInput.setSize(200,20);
		jtfInput.setLocation(100,10);

		jtfInput.setSelectedIndex(op);

        	history.add(jtfInput);
        	
        	GridBagConstraints gridCons2 = new GridBagConstraints();
        	gridCons2.weightx = 1.0;
        	gridCons2.weighty = 1.0;
		scrollPane.setSize(470,520);
		scrollPane.setLocation(10,40);
        	history.add(scrollPane);


	}


	public static void setHistoryWindow(String user){


/*		ComboBoxModel list;
		list = jtfInput.getModel();
		int op = 0;
		for(int i = 0; i < list.getSize(); i++){
			if(String.valueOf(list.getElementAt(i)).compareTo(user) == 0){
				op = i;
			}
		}
		
		jtfInput.setSelectedIndex(op);
*/




		String[] children;
                File dir = new File(System.getProperty("user.home") + "/.hevelius/history");


                FilenameFilter filter = new FilenameFilter()
                {
                        public boolean accept(File dir, String name)
                        {
                                Pattern pat;
                                Matcher mat;
                                pat = Pattern.compile("(.*)\\.log$");
                                File cat;
                                mat = pat.matcher(name);
                                if(mat.find())
                                {
                                        return true;
                                }
                                else
                                {
                                        return false;
                                }
                        }
                };
                children = dir.list(filter);

                int op = 0;

                for(int i = 0; i < children.length; i++){
                        children[i] = children[i].substring(0,children[i].length()-4);
                        if(children[i].compareTo(user) == 0){
                                op = i;
                        }
                }


                jtfInput.setModel(new DefaultComboBoxModel(children));

		jtfInput.setSelectedIndex(op);

		jtAreaOutput.setText("");
		try{
			
				File f = new File( System.getProperty("user.home") + "/.hevelius/history/"+user+".log" );
				BufferedReader entrada = new BufferedReader( new FileReader( f ) );
		    		if ( f.exists() ){
					String s;
					//s = entrada.readLine();
					//while(!s.equals(null)){
					while((s=entrada.readLine()) != null){
					//	System.out.println(s);
						jtAreaOutput.append(s+"\n");
					//	s = entrada.readLine();	
					}
				}
		}
		catch(Exception e){
			jtAreaOutput.setText("Historial No Disponible");
		}

	}

/*	public String getURLManual(){
		return getClass().getClassLoader().getResource("Hevelius/manual/manual.html");
	}
*/

}
