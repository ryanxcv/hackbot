package spybot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SidePanel extends JPanel {

    public SidePanel() {
        super();
        BoxLayout boxLayout1 = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout1);
        add(Box.createVerticalGlue());

        // Add components.
        GameFrame unitList = new GameFrame("ls programs/");
        unitList.setVisible(true);
        unitList.setPreferredSize(new Dimension(160, 20));
        add(unitList);

        GameFrame info = new GameFrame("cat program.info");
        info.setVisible(true);
        add(info);
        
        JButton undoButton = new JButton("Undo");
        add(undoButton, BorderLayout.WEST);
    }
}
