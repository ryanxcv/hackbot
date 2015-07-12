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
import hackbotutil.Coordinate;

/**
 * The window that contains all of the game's UI elements.
 */
public class UI extends JFrame {

    private GameInterface iface;

    private FieldDisplay field;
    private SidePanel side;

    /** Audio **/
    private AudioClip sndMove;
    private AudioClip sndSelect;
    private AudioClip sndUndo;

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
        field = new FieldDisplay(iface, this);
        frame.add(field, BorderLayout.CENTER);

        // Set up the side panel.
        side = new SidePanel(iface, this);
        frame.add(side, BorderLayout.WEST);

        // Load sounds.
        sndMove   = UI.getSound("sound2.wav");
        sndSelect = UI.getSound("sound6.wav");
        sndUndo   = UI.getSound("sound5.wav");

        // Finalize the main window.
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    protected void updateField() {
        field.repaint();
    }

    protected void updateSide() {
        side.update();
    }

    public void trySelect(Coordinate coord) {
        if (iface.selectUnit(coord)) {
            if (iface.getSelectedUnit() != null)
                sndSelect.play();
            updateField();
            updateSide();
        }
    }

    public void tryMove(Coordinate coord) {
        if (iface.moveToTile(coord)) {
            sndMove.play();
            updateField();
            updateSide();
        } else {
            System.out.println("Movement failed");
        }
    }

    protected void tryUndo() {
        if (iface.undo()) {
            sndUndo.play();
            updateField();
            updateSide();
        }
    }

    protected enum Player {
        HUMAN, COMPUTER
    }

    protected static BufferedImage getImage(String filename) {
        try {
            return ImageIO.read(new File("img/" + filename));
        } catch (IOException e) {
            System.out.println("Warning: " + filename + " failed to load.");
        }
        return null;
    }

    protected static AudioClip getSound(String filename) {
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
