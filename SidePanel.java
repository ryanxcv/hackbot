package hackbotui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.applet.AudioClip;

import hackbotcore.*;

public class SidePanel extends JPanel {

    protected GameInterface iface;
    protected UI gameui;

    private InfoPane info;
    private JButton undoButton;

    public SidePanel(GameInterface iface, UI gameui) {
        super();
        this.iface  = iface;
        this.gameui = gameui;

        BoxLayout boxLayout1 = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout1);
        add(Box.createVerticalGlue());

        // Add components.
        GameFrame unitList = new GameFrame("ls programs/");
        unitList.setPreferredSize(new Dimension(160, 20));
        add(unitList);

        info = new InfoPane();
        add(info);

        undoButton = new JButton("Undo");
        undoButton.addActionListener(new UndoButtonListener());
        add(undoButton, BorderLayout.WEST);
    }

    protected void update() {
        info.update();
        if (iface.canUndo())
            undoButton.setEnabled(true);
        else
            undoButton.setEnabled(false);
    }

    private class InfoPane extends GameFrame {

        private JLabel name;
        private JLabel maxSize;
        private JLabel size;
        private JLabel speed;
        private JButton ability;

        public InfoPane() {
            super("cat program.info");

            BoxLayout layout = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
            setLayout(layout);
            add(Box.createVerticalGlue());

            name    = new JLabel();
            maxSize = new JLabel();
            size    = new JLabel();
            speed   = new JLabel();
            ability = new JButton();
            ability.addActionListener(new AbilityListener());
            add(name);
            add(maxSize);
            add(size);
            add(speed);
            add(ability);
            pack();
        }

        protected void update() {
            Unit selected = iface.getSelectedUnit();
            if (selected == null) {
                setComponentsVisible(false);
                return;
            }
            setComponentsVisible(true);
            name.setText(selected.getName());
            maxSize.setText("Max size: " + selected.getMaxSize());
            size.setText("Current size: " + selected.sectors.size());
            speed.setText("Moves: " + selected.getSpeed());
            ability.setText(selected.abilities[0].name);
        }

        private void setComponentsVisible(boolean set) {
            for (Component c : new Component[] { name, maxSize, size, speed, ability })
                c.setVisible(set);
        }

        private class AbilityListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                gameui.trySelectAbility(0);
            }
        }
    }

    private class UndoButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            gameui.tryUndo();
        }
    }
}
