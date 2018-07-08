import java.awt.*;
import javax.swing.*;

public class KitchenPro extends JPanel {

    public KitchenPro() {
        super(new BorderLayout());

        JPanel topPanel = new JPanel(new GridBagLayout());
        JLabel label = new JLabel("Number of people:");
        JSpinner spinner = new JSpinner();
        label.setLabelFor(spinner);
        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0; c.gridwidth = 1; c.weightx = 0.0;
        topPanel.add(label, c);
        c.gridx = 1; c.gridy = 0; c.gridwidth = 1; c.weightx = 1.0;
        topPanel.add(spinner, c);
        c.gridx = 0; c.gridy = 1; c.gridwidth = 2; c.weightx = 0.5;
        topPanel.add(new JButton("Update"), c);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        c.gridwidth = 1; c.gridheight = 1;
        for (int i = 0; i < 25; i ++) {
            c.gridy = i;

            c.gridx = 0; c.weightx = 1.0;
            centerPanel.add(new JLabel("A fairly long label"), c);

            JLabel lbl = new JLabel(Integer.toString(i));
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
            lbl.setBackground(new Color(0, 255, 0));
            c.gridx = 1; c.weightx = 0.0;
            centerPanel.add(lbl, c);
        }
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Create the GUI and show it.  For thread safety,
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
