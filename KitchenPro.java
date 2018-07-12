import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.util.regex.*;
import javax.swing.event.*;

public class KitchenPro extends JPanel implements ChangeListener {

    private class Tuple {
        public JSpinner spinner;
        public String name;
        public int number;
        public JLabel label;

        public Tuple(JSpinner spinner, String name, int number, JLabel label) {
            this.spinner = spinner;
            this.name = name;
            this.number = number;
            this.label = label;
        }
    }

    private ArrayList<Tuple> list;

    public KitchenPro() {
        super(new BorderLayout());

        loadQuantities("quantities.txt");

        add(new JButton("Print"), BorderLayout.NORTH);

        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 1; c.fill = GridBagConstraints.HORIZONTAL;

        JPanel centerPanel = new JPanel(new GridBagLayout());
        c.gridwidth = 1; c.gridheight = 1;
        for (int i = 0; i < list.size(); i++) {
            Tuple tuple = list.get(i);
            c.gridy = i;

            if (tuple != null) {
                c.gridx = 0; c.weightx = 0.25; c.gridwidth = 1;
                tuple.spinner.addChangeListener(this);
                centerPanel.add(tuple.spinner, c);

                c.gridx = 1; c.weightx = 0.75;
                centerPanel.add(new JLabel(tuple.name + ": "), c);

                tuple.label.setHorizontalAlignment(SwingConstants.RIGHT);
                c.gridx = 2; c.weightx = 0.0;
                centerPanel.add(tuple.label, c);
            } else {
                c.gridx = 0; c.weightx = 1.0; c.gridwidth = 3;
                centerPanel.add(new JLabel(" "), c);
            }
        }
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        add(scrollPane, BorderLayout.CENTER);

        stateChanged(null);
    }

    private void loadQuantities(String filename) {
        list = new ArrayList<>();

        try {
            Scanner file = new Scanner(new File(filename));
            while (file.hasNextLine()) {
                Pattern pattern = Pattern.compile("(.*): (.*)");
                Matcher matcher = pattern.matcher(file.nextLine());
                if (matcher.matches()) {
                    list.add(new Tuple(new JSpinner(), matcher.group(1), Integer.parseInt(matcher.group(2)), new JLabel()));
                } else {
                    list.add(null);
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.err.println("Could not load " + filename);
        }
    }

    public void stateChanged(ChangeEvent e) {
        for (Tuple t : list) {
            if (t == null) continue;
            t.label.setText(Integer.toString(t.number - (int)t.spinner.getValue()));
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
