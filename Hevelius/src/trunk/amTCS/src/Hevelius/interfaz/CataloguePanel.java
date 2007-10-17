package Hevelius.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import Hevelius.acsmodules.*;
import Hevelius.heveliusmodules.*;
import Hevelius.interfaz.*;
import javax.swing.table.AbstractTableModel;
import alma.TYPES.*;

public class CataloguePanel extends JPanel
{
	//private JLabel telstate;
	private JComboBox object;
	private JTable catalogue;
	private JLabel catType;
	private MyTableModel m;

	private int dx = 0;
	private int dy = 0;

	public CataloguePanel(LayoutManager l)
	{
		super(l);
	}
	public void init()
	{
		m = new MyTableModel();

		catType = new JLabel("Catalogue type:");
		catType.setSize(120,20);
		add(catType);

		String[] types = {"Stars", "Galaxies", "Nebulas", "Custom"};
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
		//		catalogue.setBackground(Color.BLACK);
		//		catalogue.setSize(100,100);
		//		catalogue.setLocation(0,0);
		//		catalogue.setFillsViewportHeight(true);

		JScrollPane catScroll = new JScrollPane(catalogue);
		catScroll.setSize(260,140);
		catScroll.setLocation(10,60);
		catScroll.setBackground(Color.BLACK);
		add(catScroll);
	}
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
		}
	}

	class MyTableModel extends AbstractTableModel 
	{
		private String[] columnNames = {"Name",
			"Ra",
			"Dec",
			"Epoch",
			"A"};

		private Object[][] data = {{"none","none","none","none","none"}};

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		/*
		 * JTable uses this method to determine the default renderer/
		 * editor for each cell.  If we didn't implement this method,
		 * then the last column would contain text ("true"/"false"),
		 * rather than a check box.
		 */
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/*
		 * Don't need to implement this method unless your table's
		 * editable.
		 */
		public boolean isCellEditable(int row, int col) {
			//Note that the data/cell address is constant,
			//no matter where the cell appears onscreen.
			if (col < 2) {
				return false;
			} else {
				return false;
			}
		}

		/*
		 * Don't need to implement this method unless your table's
		 * data can change.
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

		public void setDataSource(int cat)
		{
			data = new String[getCatRows()][5];
			readCatalogue();
			fireTableStructureChanged();
		}

		private void readCatalogue()
		{
			data[0][0] = "a";
                        data[0][1] = "a";
                        data[0][2] = "a";
                        data[0][3] = "a";
                        data[0][4] = "a";
		}

		private int getCatRows()
		{
			return 1;
		}
}
}
