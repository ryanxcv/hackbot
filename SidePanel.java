package hackbotui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

import java.applet.AudioClip;

import hackbotcore.*;

public class SidePanel extends JPanel {

    protected final GameInterface iface;
    protected final UI gameui;

    private InfoPane info;
    private JButton undoButton;

    public SidePanel(GameInterface iface, UI ui) {
        super();
        this.iface  = iface;
        this.gameui = ui;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue());

        // Add components.
        GameFrame unitList = new GameFrame("ls programs/");
        unitList.setPreferredSize(new Dimension(160, 20));
        add(unitList);

        info = new InfoPane();
        add(info);

        undoButton = new JButton("Undo");
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameui.tryUndo();
            }
        });
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

        private ImagePanel img  = new ImagePanel();
        private JLabel  name    = new JLabel();
        private JLabel  maxSize = new JLabel();
        private JLabel  size    = new JLabel();
        private JLabel  speed   = new JLabel();
        private JButton ability = new JButton();
        private JButton cancel  = new JButton("Cancel");

        public InfoPane() {
            super("cat program.info");

            // Set up the program details panel.
            JPanel north = new JPanel();
            JPanel northeast = new JPanel();
            northeast.setLayout(new BoxLayout(northeast, BoxLayout.Y_AXIS));
            northeast.add(Box.createVerticalGlue());
            north.add(img, BorderLayout.WEST);
            north.add(northeast, BorderLayout.EAST);
            northeast.add(name);
            northeast.add(maxSize);
            northeast.add(size);
            northeast.add(speed);
            add(north, BorderLayout.NORTH);

            // Set up the action panel.
            JPanel center = new JPanel();
            center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
            ability.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gameui.trySelectAbility(0);
                }
            });
            cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    gameui.trySelectAbility(-1);
                }
            });
            center.add(ability);
            center.add(cancel);
            add(center, BorderLayout.CENTER);

            setComponentsVisible(false);
            pack();
        }

        protected void update() {
            Unit selected = iface.getSelectedUnit();
            if (selected == null) {
                setComponentsVisible(false);
                return;
            }
            setComponentsVisible(true);
            img.setImage(selected.imgHead);
            name.setText(selected.getName());
            maxSize.setText("Max size: " + selected.getMaxSize());
            size.setText("Current size: " + selected.sectors.size());
            speed.setText("Moves: " + selected.getSpeed());
            ability.setText(selected.abilities[0].name);
        }

        private void setComponentsVisible(boolean set) {
            for (Component c : new Component[]
                               { img, name, maxSize, size, speed, ability, cancel })
                c.setVisible(set);
        }
    }

    public class ImagePanel extends JPanel {

        private BufferedImage image;

        public ImagePanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(32, 32));
        }

        public void setImage(BufferedImage image) { this.image = image; }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null)
                g.drawImage(image, 0, 0, null);
        }
    }
}
