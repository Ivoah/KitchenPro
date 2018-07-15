import java.io.*;
import java.util.*;
import java.util.regex.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class KitchenPro extends JPanel {

    private JTable table;

    public KitchenPro() {
        super(new BorderLayout());

        JButton btn = new JButton("Print");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    table.print();
                } catch (PrinterException pe) {
                    JOptionPane.showMessageDialog(null, "Could not print document", "Printer error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(btn, BorderLayout.NORTH);

        table = loadQuantities("quantities.txt");
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JTable loadQuantities(String filename) {
        ArrayList<Object[]> list = new ArrayList<>();

        try {
            Scanner file = new Scanner(new File(filename));
            while (file.hasNextLine()) {
                Pattern pattern = Pattern.compile("(.*): (.*)");
                Matcher matcher = pattern.matcher(file.nextLine());
                if (matcher.matches()) {
                    list.add(new Object[]{0, matcher.group(1), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(2))});
                } else {
                    list.add(new Object[]{null, null, null});
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.err.println("Could not load " + filename);
        }

        JTable table = new JTable(new AbstractTableModel() {
            private String[] columnNames = {"Item", "Current Stock", "To Order"};
            private Object[][] data = list.toArray(new Object[][]{});

            public int getColumnCount() {return columnNames.length;}
            public int getRowCount() {return data.length;}
            public String getColumnName(int col) {return columnNames[col];}
            public Object getValueAt(int row, int col) {return data[row][col];}
            public Class getColumnClass(int c) {return getValueAt(0, c).getClass();}

            public boolean isCellEditable(int row, int col) {return col == 0 && getValueAt(row, col) != null;}
            public void setValueAt(Object value, int row, int col) {
                data[row][col] = value;
                fireTableCellUpdated(row, col);
            }
        });
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                TableModel model = (TableModel)e.getSource();

                if (col != 0) return;
                model.setValueAt((int)model.getValueAt(row, 3) - (int)model.getValueAt(row, 0), row, 2);
            }
        });
        /*table.setDefaultRenderer(Integer.class, new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value != null) {
                    JSpinner spinner = new JSpinner();
                    spinner.setValue(value);
                    return spinner;
                } else {
                    return null;
                }
            }
        });*/

        return table;
    }

    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("KitchenPro v0.2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new KitchenPro());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
	            UIManager.put("swing.boldMetal", Boolean.FALSE);
		        createAndShowGUI();
            }
        });
    }
}
