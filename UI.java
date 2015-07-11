package hackbotui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import javax.swing.JFrame;

/**
 * The window that contains all of the game's UI elements.
 */
public class UI extends JFrame{

    /** The grid that contains the game state. **/
    private Grid grid;

    /** The player that is currently moving. **/
    private Player turn;

    /** Set up the interface. **/
    public UI(Grid grid) {
        // Set up the main window.
        this.grid = grid;
        turn = Player.HUMAN;
        JFrame frame = new JFrame("Hackbot");
        Container c = frame.getContentPane();
        c.setBackground(Color.BLACK);

        // Set up the field display.
        FieldDisplay field = new FieldDisplay(grid);
        frame.add(field, BorderLayout.CENTER);

        // Set up the side panel.
        SidePanel side = new SidePanel();
        frame.add(side, BorderLayout.WEST);

        // Finalize the main window.
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public enum Player {
        HUMAN, COMPUTER
    }
}
