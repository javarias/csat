/**
 * CataloguePanel is a JPanel class that is used to gruop catalogue 
 * objects for the GUI and communicates with ReadCatalogue class to 
 * retrieve catalogue information.
 */


package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import java.io.*;
import Hevelius.acsmodules.*;
import Hevelius.heveliusmodules.*;
import Hevelius.interfaz.*;
import javax.swing.table.AbstractTableModel;
import alma.TYPES.*;
import Hevelius.catalogues.*;
import Hevelius.utilities.historial.*;

public class CataloguePanel extends JPanel
{
	//private JLabel telstate;
	private JComboBox object;
	private JTable catalogue;
	private JLabel catType;
	private MyTableModel m;
	private JScrollPane catScroll;

	private int dx = 0;
	private int dy = 0;
	private static Historial hist = new Historial();

	/**
	 * Constructor which extends JPanel constructor in
	 * order to allow giving a LayoutManager to instantiate
	 * the panel.
	 * @param l      LayoutManager
	 */
	public CataloguePanel(LayoutManager l)
	{
		super(l);
	}

	/**
	 * Method used to initialize variables and graphical states.
	 * It instantiate, set and add to panel all necessary widgets.
	 * It also adds actionListeners to some widgets.
	 */
	public void init()
	{
		m = new MyTableModel();

		catType = new JLabel("Catalogue type:");
		catType.setSize(120,20);
		add(catType);

		String[] types = {"Stars", "Galaxies", "Nebulas", "Pulsars", "Custom"};
		object = new JComboBox();
		object.setModel(new DefaultComboBoxModel(types));
		object.setSelectedIndex(0);
		object.setSize(120,20);
		add(object);

		m.setDataSource(0);

		object.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				//object.getSelectedIndex()
				m.setDataSource(object.getSelectedIndex());
				}
				});

		catalogue = new JTable(m);
		catalogue.setPreferredScrollableViewportSize(new Dimension(500, 70));
		catalogue.setBackground(Color.LIGHT_GRAY);
		catalogue.setForeground(Color.BLACK);
		//		catalogue.setSize(100,100);
		//		catalogue.setLocation(0,0);
		//		catalogue.setFillsViewportHeight(true);

		catalogue.addMouseListener(new java.awt.event.MouseAdapter() 
				{
				public void mouseClicked(java.awt.event.MouseEvent e) 
				{
				if ( e.getClickCount() == 2 ) 
				{
				RadecPos rd = new RadecPos();
				rd.ra = Double.parseDouble((String)m.getValueAt(catalogue.getSelectedRow(),1));
				rd.dec = Double.parseDouble((String)m.getValueAt(catalogue.getSelectedRow(),2));
				if(interfaz.getDrawingPanel().getCSATControl()!=null){
				interfaz.getDrawingPanel().getCSATControl().preset(rd);
				hist.addHistoryCatalogue(rd.ra,rd.dec);
				}
				}
				}
				});

		catScroll = new JScrollPane(catalogue);
		catScroll.setSize(280,140);
		catScroll.setLocation(10,60);
		//catScroll.setBackground(Color.LIGHT_GRAY);
		add(catScroll);
	}

	/**
        * This method is extended in order to allow autoresizing
        * of widgets whenever window's size changes.
        * @param g      Graphics
        */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		boolean updatePanel = false;
		float fsize, osize;
		if (dx != getSize().width || dy != getSize().height)
			updatePanel = true;
		dx = getSize().width;
		dy = getSize().height;

		if(updatePanel)
		{
			if(dx/280.0f>dy/154.0f)
			{
				fsize = (12.0f*dy)/154.0f;
				osize = dy/154.0f;
			}
			else
			{
				fsize = (12.0f*dx)/280.0f;
				osize = dx/280.0f;
			}

			catType.setLocation((int)(10*osize),(int)(20*osize));
			catType.setFont(catType.getFont().deriveFont(fsize));
			catType.setSize((int)(120*osize),(int)(20*osize));

			object.setLocation((int)(140*osize),(int)(20*osize));
			object.setFont(object.getFont().deriveFont(fsize));
			object.setSize((int)(120*osize),(int)(20*osize));

			catScroll.setLocation((int)(10*osize),(int)(60*osize));
			catalogue.setFont(catalogue.getFont().deriveFont(fsize));
			catScroll.setSize((int)(dx-20*osize),(int)(200*osize));
		}
	}

	/**
	* This is a class that extends AbstractTableModel which is used to retrieve, store and show
	* actual catalogue information.
	*/
	class MyTableModel extends AbstractTableModel 
	{
		private String[] columnNames = {"Name",
			"Ra",
			"Dec",
			"Epoch"};

		CatalogueInfo[] catData = null;

		private Object[][] data = {{"none","none","none","none"}};

		/**
		* This is a method that returns the number of columns of the table.
		* return	int with the number of columns of table.
		*/
		public int getColumnCount() {
			return columnNames.length;
		}

		/**
                * This is a method that returns the number of rows of the table.
                * return        int with the number of rows of table.
                */
		public int getRowCount() {
			return data.length;
		}

		/**
                * This is a method that returns the name of a column.
		* @param col	int that points which column name is retrieved.
                * return        String with the name of selected column.
                */
		public String getColumnName(int col) {
			return columnNames[col];
		}

		/**
		* This method returns actual value of one cell.
		* @param row	int that points to the row of the cell.
		* @param col	int that points to the column of the cell.
		* return	Object with the information of the selected cell.
		*/
		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		/**
		 * JTable uses this method to determine the default renderer/
		 * editor for each cell.  If we didn't implement this method,
		 * then the last column would contain text ("true"/"false"),
		 * rather than a check box.
		 * @param c	int that points to the column of which we want to know the class.
		 * return	Class of selected column.
		 */
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/**
		 * Method that decides where a cell is editable or not.
		 * @param row	int that points to the row of the cell.
		 * @param col	int that points to the column of the cell.
		 * return	boolean that indicates if a cell is editable or not.
		 */
		public boolean isCellEditable(int row, int col) {
			return false;
		}

		/**
		 * Method that decides what happens when cell data is changed.
		 * @param row   int that points to the row of the cell.
                 * @param col   int that points to the column of the cell.
		 */
		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}

		private void printDebugData() {
			int numRows = getRowCount();
			int numCols = getColumnCount();

			for (int i=0; i < numRows; i++) {
				System.out.print("    row " + i + ":");
				for (int j=0; j < numCols; j++) {
					System.out.print("  " + data[i][j]);
				}
				System.out.println();
			}
			System.out.println("--------------------------");
		}

		/**
		* Method that sets information about the selected catalogue.
		* @param cat	int that indicates which catalogue to load.
		*/
		public void setDataSource(int cat)
		{
			readCatalogues(cat);
			if(catData!=null)
			{
				data = new String[getCatRows()][5];
				writeData();
			}
			else
			{
				data = new String[1][4];
				data[0][0]= "none";
				data[0][1]= "none";
				data[0][2]= "none";
				data[0][3]= "none";
				//data[0][4]= "none";
			}
			//System.out.println(data[0][0]);
			fireTableStructureChanged();
		}

		/**
		* Method that loads information about the selected catalogue.
                * @param cat    int that indicates which catalogue to load.
		*/
		private void readCatalogues(int cat)
		{
			String[] list = null;
			String[] custom = null;
			int j = 0;
			int objs = 0;
			File pathl = null;
			File pathc = null;
			switch(cat)
			{
				case 0:	pathl = new File(getClass().getClassLoader().getResource("Hevelius/catalogues/stars/").getFile());
					pathc = new File(System.getProperty("user.home")+"/.hevelius/catalogues/stars/");
					break;
				case 1:	pathl = new File(getClass().getClassLoader().getResource("Hevelius/catalogues/galaxies/").getFile());
					pathc = new File(System.getProperty("user.home")+"/.hevelius/catalogues/galaxies/"); 
					break;
				case 2: pathl = new File(getClass().getClassLoader().getResource("Hevelius/catalogues/nebulas/").getFile()); 
					pathc = new File(System.getProperty("user.home")+"/.hevelius/catalogues/nebulas/");
					break;
				case 3: pathl = new File(getClass().getClassLoader().getResource("Hevelius/catalogues/pulsars/").getFile());
					pathc = new File(System.getProperty("user.home")+"/.hevelius/catalogues/pulsars/");
					break;
				case 4: pathl = new File(getClass().getClassLoader().getResource("Hevelius/catalogues/custom/").getFile());
					pathc = new File(System.getProperty("user.home")+"/.hevelius/catalogues/custom/");
					break;
				default: break;
			}
			list = ReadCatalogue.searchCatalogues(pathl);
			custom = ReadCatalogue.searchCatalogues(pathc);
			if(list!=null)
				objs = list.length;
			if(custom!=null)
				objs += custom.length;
			if(objs>0)
				catData = new CatalogueInfo[objs];
			else
				catData = null;
			if(list!=null)
			{
				for (int i=0; i<list.length; i++)
				{
					// Get filename of file or directory
					String filename = list[i];
					catData[j] = ReadCatalogue.parseCatalogue(pathl+"/"+filename);
					j++;
				}
			}

			if(custom!=null)
			{
				for (int i=0; i<custom.length; i++)
				{
					// Get filename of file or directory
					String filename = custom[i];
					catData[j] = ReadCatalogue.parseCatalogue(pathc+"/"+filename);
					j++;
				}
			}
		}

		/**
		* Method that returns the number of rows that catalogue will require.
		* return	int with the number of rows needed by catalogue.
		*/
		private int getCatRows()
		{
			int rows = 0;
			if(catData!=null)
			{
				for(int i=0; i<catData.length; i++)
					rows += catData[i].getLength();
			}
			return rows;
		}

		/**
		* Method that writes to data 2D array the information of each catalogue 
		* row.
		*/
		private void writeData()
		{
			int k = 0;
			ObjectInfo temp = null;
			for(int i=0; i<catData.length; i++)
			{
				for(int j=0; j<catData[i].getLength(); j++)
				{
					temp = catData[i].get(j);
					data[k][0]= temp.getName();
					data[k][1]= Double.toString(temp.getRa());
					data[k][2]= Double.toString(temp.getDec());
					data[k][3]= "1950";
					//data[k][4]= "none";
					k++;
				}
			}
		}
	}
}
