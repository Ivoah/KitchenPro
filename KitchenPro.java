import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.regex.*;

public class KitchenPro extends JPanel implements ActionListener {

    private class Tuple {
        public String name;
        public int number;
        public JLabel label;

        public Tuple(String name, int number, JLabel label) {
            this.name = name;
            this.number = number;
            this.label = label;
        }
    }

    private ArrayList<Tuple> list;
    private JSpinner spinner;

    public KitchenPro() {
        super(new BorderLayout());

        loadQuantities("quantities.txt");

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
        c.gridx = 0; c.gridy = 1; c.gridwidth = 2; c.weightx = 1.0;
        topPanel.add(btn, c);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        c.gridwidth = 1; c.gridheight = 1;
        for (int i = 0; i < list.size(); i++) {
            c.gridy = i;

            c.gridx = 0; c.weightx = 1.0;
            if (list.get(i) != null) {
                centerPanel.add(new JLabel(list.get(i).name + ": "), c);

                list.get(i).label.setHorizontalAlignment(SwingConstants.RIGHT);
                c.gridx = 1; c.weightx = 0.0;
                centerPanel.add(list.get(i).label, c);
            } else {
                c.gridwidth = 2;
                centerPanel.add(new JLabel(" "), c);
            }
        }
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadQuantities(String filename) {
        list = new ArrayList<>();

        try {
            Scanner file = new Scanner(new File(filename));
            while (file.hasNextLine()) {
                Pattern pattern = Pattern.compile("(.*): (.*)");
                Matcher matcher = pattern.matcher(file.nextLine());
                if (matcher.matches()) {
                    list.add(new Tuple(matcher.group(1), Integer.parseInt(matcher.group(2)), new JLabel()));
                } else {
                    list.add(null);
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.err.println("Could not load " + filename);
        }
    }

    public void actionPerformed(ActionEvent e) {
        int people = (int)spinner.getValue();
        for (Tuple t : list) {
            if (t == null) continue;
            t.label.setText(Integer.toString((int)Math.ceil(t.number*people)));
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
