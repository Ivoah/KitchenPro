import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

public class KitchenPro extends JPanel implements ActionListener {

    private HashMap<String, JLabel> labels;
    private HashMap<String, Double> portions;

    private JSpinner spinner;

    public KitchenPro() {
        super(new BorderLayout());

        loadPortions("portions.txt");

        JPanel topPanel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel("Number of people:");
        spinner = new JSpinner(new SpinnerNumberModel(100, 0, 10000, 10));
        label.setLabelFor(spinner);
        JButton btn = new JButton("Update");
        btn.addActionListener(this);
        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.weightx = 0.0;
        topPanel.add(label, c);
        c.gridx = 1; c.gridy = 0; c.gridwidth = 1; c.weightx = 1.0;
        topPanel.add(spinner, c);
        c.gridx = 0; c.gridy = 1; c.gridwidth = 2; c.weightx = 0.5;
        topPanel.add(btn, c);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        labels = new HashMap<>();
        int i = 0;
        c.gridwidth = 1; c.gridheight = 1;
        for (String item : portions.keySet()) {
            c.gridy = i++;

            c.gridx = 0; c.weightx = 1.0;
            centerPanel.add(new JLabel(item + ": "), c);

            JLabel lbl = new JLabel("∞");
            labels.put(item, lbl);
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
            c.gridx = 1; c.weightx = 0.0;
            centerPanel.add(lbl, c);
        }
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadPortions(String filename) {
        portions = new HashMap<>();

        try {
            Scanner file = new Scanner(new File(filename));
            while (file.hasNextLine()) {
                String[] line = file.nextLine().split(": ");
                String item = line[0];
                String fraction[] = line[1].split("/");

                portions.put(item, Double.parseDouble(fraction[0])/Double.parseDouble(fraction[1]));
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.err.println("Could not load " + filename);
        }
    }

    public void actionPerformed(ActionEvent e) {
        int people = (int)spinner.getValue();
        for (String item : portions.keySet()) {
            labels.get(item).setText(Integer.toString((int)Math.ceil(portions.get(item)*people)));
        }
    }

    /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("KitchenPro v0.1");
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
