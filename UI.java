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

    private TopBar top;
    private FieldDisplay field;
    private SidePanel side;

    /** Images **/
    private BufferedImage imgBG;
    private BufferedImage imgTurn;

    /** Audio **/
    private AudioClip sndMove;
    private AudioClip sndSelect;
    private AudioClip sndTurn;
    private AudioClip sndUndo;

    /** The player that is currently moving. **/
    private Player turn;

    /** Set up the interface. **/
    public UI(GameInterface iface) {
        super("Hackbot");
        // Set up the main window.
        this.iface = iface;
        turn = Player.HUMAN;
        //JFrame frame = new JFrame("Hackbot");
        getContentPane().setBackground(Color.BLACK);

        // Set up the top bar.
        top = new TopBar(iface, this);
        add(top, BorderLayout.NORTH);

        // Set up the field display.
        field = new FieldDisplay(iface, this);
        add(field, BorderLayout.CENTER);

        // Set up the side panel.
        side = new SidePanel(iface, this);
        add(side, BorderLayout.WEST);

        // Load images.
        imgTurn = getImage("turn.png");

        // Load sounds.
        sndMove   = getSound("sound2.wav");
        sndSelect = getSound("sound7.wav");
        sndTurn   = getSound("sound1.wav");
        sndUndo   = getSound("sound5.wav");

        // Finalize the main window.
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    protected void update() {
        repaint();
        field.repaint();
        side.update();
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

    protected void passTurn() {
        sndTurn.play();
        field.getGraphics().drawImage(imgTurn, 100, 100, field);
        //repaint();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e);
        }
        iface.passTurn();
        update();
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
