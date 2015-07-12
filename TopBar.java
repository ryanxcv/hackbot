package hackbotui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import hackbotcore.GameInterface;

public class TopBar extends JPanel {

    protected GameInterface iface;
    private UI ui;

    private JButton pass;

    public TopBar(GameInterface iface, UI ui) {
        super();
        this.iface = iface;
        this.ui    = ui;

        setLayout(new BorderLayout());

        add(new JLabel("databattle in progress"), BorderLayout.WEST);

        pass = new JButton("end turn");
        pass.addActionListener(new PassButtonListener());
        add(pass, BorderLayout.EAST);
    }

    private class PassButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ui.passTurn();
        }
    }
}
