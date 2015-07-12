package hackbotui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.applet.AudioClip;

import hackbotcore.GameInterface;

public class SidePanel extends JPanel {

    private GameInterface iface;
    private FieldDisplay field;

    private AudioClip sndUndo;

    public SidePanel(GameInterface iface, FieldDisplay field) {
        super();
        this.iface = iface;
        this.field = field;
        sndUndo = UI.getSound("sound5.wav");

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
        undoButton.addActionListener(new UndoButtonListener());
        add(undoButton, BorderLayout.WEST);
    }

    public void tryUndo() {
        if (iface.undo()) {
            sndUndo.play();
            field.repaint();
        }
    }

    public class UndoButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            tryUndo();
        }
    }
}
