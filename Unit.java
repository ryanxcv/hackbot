package spybot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

/**
 * All of the battling programs inherit from this general class, called Unit.
 * It has all the generic properties and defaults.
 */
public abstract class Unit {

    public LinkedList<Tile> sectors;
    protected int moves;
    private boolean done;

    // The following fields will be set by a program's constructor.
    public String name;
    protected int speed;
    protected int maxSize;
    public int team;

    public BufferedImage imgHead;
    public BufferedImage imgBody;

    // Set up universal initial properties.
    public void init(Tile tile) {
        sectors = new LinkedList<Tile>();
        sectors.add(tile);
        getUnitImages();
    }

    public void getUnitImages() {
        String fileStr = name.toLowerCase();
        imgHead = getImage(fileStr +      ".png");
        imgBody = getImage(fileStr + "_body.png");
    }

    public void setDone() { done = true; }

    /**
     * This method is run at the beginning of each turn.
     */
    public void reset() {
        moves = speed;
        done = false;
    }

    /**
     * Move the unit into a given tile (which should be adjacent and
     * occupable). This tile becomes the new head, and the unit is shrunk
     * if already at max size.
     */
    public void move(Tile tile) {
        // Insert the tile as the new head.
        sectors.add(0, tile);

        // If the program moved over itself, remove any duplicate tiles.
        Tile head = getHead();
        for (int i = 1; i < sectors.size(); i++) {
            if (head == sectors.get(i)) {
                sectors.remove(i);
                return;
            }
        }

        // If the program is over max size, trim it down to comply.
        if (sectors.size() > maxSize) {
            sectors.removeLast();
        }
    }

    public Tile getHead() {
        return sectors.getFirst();
    }

    public static BufferedImage getImage(String filename) {
        try {
            return ImageIO.read(new File("img/" + filename));
        } catch (IOException e) {
            System.out.println("Warning: " + filename + " failed to load.");
        }
        return null;
    }

    public int distance(int column, int row) {
        Tile head = getHead();
        return Math.abs(head.getColumn() - column) +
               Math.abs(head.getRow() - row);
    }
    public int distance(Tile tile) {
        Tile head = getHead();
        return Math.abs(head.getColumn() - tile.getColumn()) +
               Math.abs(head.getRow() - tile.getRow());
    }

    public class Hack extends Unit {

        public Hack(Tile tile) {
            name = "Hack";
            speed = 2;
            maxSize = 4;
            team = 0;

            init(tile);
        }
    }

    public class Sentinel extends Unit {

        public Sentinel(Tile tile) {
            name = "Sentinel";
            speed = 1;
            maxSize = 4;
            team = 1;

            init(tile);
        }
    }
}
