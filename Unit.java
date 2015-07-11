package hackbotcore;

import java.util.LinkedList;
import hackbotutil.Coordinate;

/**
 * All of the battling programs inherit from this general class, called Unit.
 * It has all the generic properties and defaults.
 */
public abstract class Unit {

    public LinkedList<Coordinate> sectors;
    protected int moves;
    private boolean done;

    // The following fields will be set by a program's constructor.
    protected String name;
    protected int speed;
    protected int maxSize;
    protected int team;

    // Set up universal initial properties.
    protected void init(Coordinate coord) {
        moves = speed;
        sectors = new LinkedList<Coordinate>();
        sectors.add(coord);
    }

    protected void setDone() { done = true; }

    /**
     * This method is run at the beginning of each turn.
     */
    protected void reset() {
        moves = speed;
        done = false;
    }

    /**
     * Move the unit into a given tile (which should be adjacent and
     * occupable). This tile becomes the new head, and the unit is shrunk
     * if already at max size.
     */
    protected void move(Coordinate coord) {
        // Insert the tile as the new head.
        sectors.add(0, coord);

        // If the program moved over itself, remove any duplicate tiles.
        Coordinate head = getHead();
        for (int i = 1; i < sectors.size(); i++) {
            if (head == sectors.get(i)) {
                sectors.remove(i);
                return;
            }
        }

        // If the program is over max size, trim it down to comply.
        if (sectors.size() > maxSize)
            sectors.removeLast();
    }

    public Coordinate getHead() { return sectors.getFirst(); }

    public int distance(int column, int row) {
        return getHead().distance(column, row);
    }

    public int distance(Coordinate coord) {
        return getHead().distance(coord);
    }

    public static class Hack extends Unit {

        public Hack(Coordinate coord) {
            name = "Hack";
            speed = 2;
            maxSize = 4;
            team = 0;

            init(coord);
        }
    }

    public static class Sentinel extends Unit {

        public Sentinel(Coordinate coord) {
            name = "Sentinel";
            speed = 1;
            maxSize = 4;
            team = 1;

            init(coord);
        }
    }
}
