package hackbotui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

import hackbotcore.*;

/**
 * The window that contains all of the game's UI elements.
 */
public class UI extends JFrame {

    private GameInterface iface;

    /** The player that is currently moving. **/
    private Player turn;

    /** Set up the interface. **/
    public UI(GameInterface iface) {
        // Set up the main window.
        this.iface = iface;
        turn = Player.HUMAN;
        JFrame frame = new JFrame("Hackbot");
        frame.getContentPane().setBackground(Color.BLACK);

        // Set up the field display.
        FieldDisplay field = new FieldDisplay(iface);
        frame.add(field, BorderLayout.CENTER);

        // Set up the side panel.
        SidePanel side = new SidePanel(iface, field);
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

    public static BufferedImage getImage(String filename) {
        try {
            return ImageIO.read(new File("img/" + filename));
        } catch (IOException e) {
            System.out.println("Warning: " + filename + " failed to load.");
        }
        return null;
    }

    public static AudioClip getSound(String filename) {
        try {
            File file = new File("sound/" + filename);
            return Applet.newAudioClip(file.toURI().toURL());
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Warning: " + filename + " failed to load.");
        }
        return null;
    }
}
