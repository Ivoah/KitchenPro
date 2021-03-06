import java.io.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;

import java.util.*;
import java.util.jar.*;
import java.util.regex.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class KitchenPro extends JPanel {

    private class MyTableModel extends AbstractTableModel {
        private String[] columnNames = {"Inventory", "Current Stock", "To Order", "Total"};
        private ArrayList<Object[]> data;
        private ArrayList<Object[]> backupData;

        public MyTableModel(ArrayList<Object[]> list) {
            data = list;
        }

        public int getColumnCount() {return columnNames.length;}
        public int getRowCount() {return data.size();}
        public String getColumnName(int col) {return columnNames[col];}
        public Object getValueAt(int row, int col) {return data.get(row)[col];}
        public Class<?> getColumnClass(int c) {return getValueAt(0, c).getClass();}

        public boolean isCellEditable(int row, int col) {return col == 0 && getValueAt(row, col) != null;}
        public void setValueAt(Object value, int row, int col) {
            data.get(row)[col] = value;
            fireTableCellUpdated(row, col);
        }

        public void hideEmpty() {
            backupData = (ArrayList<Object[]>)data.clone();
            data.removeIf(r -> r[2] != null && (int)r[2] == 0);
            fireTableDataChanged();
        }

        public void showEmpty() {
            data = backupData;
            fireTableDataChanged();
        }
    }

    private class JSpinnerEditor extends AbstractCellEditor implements TableCellEditor {
        private JSpinner spinner = new JSpinner();

        public Object getCellEditorValue() {
            return spinner.getValue();
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            spinner.setValue(value);
            return spinner;
        }
    }

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

        JCheckBox cb = new JCheckBox("Hide empty rows");
        cb.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ((MyTableModel)table.getModel()).hideEmpty();
                } else {
                    ((MyTableModel)table.getModel()).showEmpty();
                }
            }
        });

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(btn);
        p.add(Box.createHorizontalGlue());
        p.add(cb);
        add(p, BorderLayout.NORTH);

        table = loadQuantities("quantities.txt");
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JTable loadQuantities(String filename) {
        ArrayList<Object[]> list = new ArrayList<>();

        try {
            JarFile jar = new JarFile("KitchenPro.jar");
            Scanner file = new Scanner(jar.getInputStream(jar.getEntry("quantities.txt")));
            while (file.hasNextLine()) {
                Pattern pattern = Pattern.compile("(.*): (.*)");
                Matcher matcher = pattern.matcher(file.nextLine());
                if (matcher.matches()) {
                    list.add(new Object[]{0, matcher.group(1), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(2))});
                } else {
                    list.add(new Object[]{null, null, null, null});
                }
            }
            file.close();
            jar.close();
        } catch (IOException e) {
            System.err.println("Could not load " + filename);
        }

        JTable table = new JTable(new MyTableModel(list));

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

        table.setDefaultRenderer(Integer.class, new DefaultTableCellRenderer());
        table.setDefaultEditor(Integer.class, new JSpinnerEditor());

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
