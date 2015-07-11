package hackbotcore;

import java.util.LinkedList;

/**
 * All of the battling programs inherit from this general class, called Unit.
 * It has all the generic properties and defaults.
 */
public abstract class Unit {

    protected LinkedList<Tile> sectors;
    protected int moves;
    private boolean done;

    // The following fields will be set by a program's constructor.
    protected String name;
    protected int speed;
    protected int maxSize;
    protected int team;

    protected String imgHead;
    protected String imgBody;

    // Set up universal initial properties.
    protected void init(Tile tile) {
        sectors = new LinkedList<Tile>();
        sectors.add(tile);
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
    protected void move(Tile tile) {
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

    public String getImgHead() {
        return imgHead;
    }

    public String getImgBody() {
        return imgBody;
    }

    public int distance(int column, int row) {
        Tile head = getHead();
        return Math.abs(head.getColumn() - column) +
               Math.abs(head.getRow() - row);
    }

    protected int distance(Tile tile) {
        Tile head = getHead();
        return Math.abs(head.getColumn() - tile.getColumn()) +
               Math.abs(head.getRow() - tile.getRow());
    }

    public static class Hack extends Unit {

        public Hack(Tile tile) {
            name = "Hack";
            speed = 2;
            maxSize = 4;
            team = 0;

            init(tile);
        }
    }

    public static class Sentinel extends Unit {

        public Sentinel(Tile tile) {
            name = "Sentinel";
            speed = 1;
            maxSize = 4;
            team = 1;

            init(tile);
        }
    }
}
