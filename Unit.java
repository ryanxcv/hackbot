package hackbotcore;

import java.util.LinkedList;
import hackbotutil.Coordinate;

/**
 * All of the battling programs inherit from this general class, called Unit.
 * It has all the generic properties and defaults.
 */
public abstract class Unit {

    public LinkedList<Coordinate> sectors;
    public LinkedList<Coordinate> savedSectors;
    protected int moves;
    private TurnState state;

    // The following fields will be set by a program's constructor.
    protected String name;
    protected int speed;
    protected int maxSize;
    protected Team team;

    public static enum Team { PLAYER, COMPUTER;
        private static Team[] vals = values();
        public Team next() {
            return vals[(ordinal() + 1) % vals.length];
        }
    }

    public static enum TurnState { READY, MOVING, USING_ABILITY, DONE }

    // Set up universal initial properties.
    protected void init(Coordinate coord) {
        sectors = new LinkedList<Coordinate>();
        sectors.add(coord);
        reset();
    }

    protected void setDone() { state = TurnState.DONE; }
    public boolean isDone() { return state == TurnState.DONE; }
    public TurnState getState() { return state; }

    public int getMoves() { return moves; }
    public String getName() { return name; }
    public int getSpeed() { return speed; }
    public int getMaxSize() { return maxSize; }
    public Team getTeam() { return team; }

    /**
     * This method is run at the beginning of each turn.
     */
    protected void reset() {
        savedSectors = sectorsCopy();
        moves = speed;
        state = TurnState.READY;
    }

    protected void deselect() {
        if (moves != speed)
            setDone();
    }

    /**
     * Resets any movement during the unit's move, but cannot undo once it is
     * done. Returns true on success.
     */
    protected boolean undo() {
        if (isDone())
            return false;
        sectors = savedSectors;
        reset();
        return true;
    }

    /**
     * Move the unit into a given tile (which should be adjacent and
     * occupable). This tile becomes the new head, and the unit is shrunk
     * if already at max size.
     */
    protected void move(Coordinate coord) {
        assert moves > 0;

        if (state == TurnState.READY)
            state = TurnState.MOVING;
        else
            assert state == TurnState.MOVING;

        // Reduce moves.
        moves--;

        // Insert the tile as the new head.
        sectors.add(0, coord);

        // If the program moved over itself, remove any duplicate tiles.
        Coordinate head = getHead();
        for (int i = 1; i < sectors.size(); i++)
            if (head.equals(sectors.get(i))) {
                sectors.remove(i);
                return;
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

    public boolean contains(Coordinate coord) {
        for (Coordinate c : sectors)
            if (coord.equals(c))
                return true;
        return false;
    }

    public Unit copy() {
        Unit copy = null;
        if (this.getClass().equals(Hack.class))
            copy = new Hack(new Coordinate(0, 0));
        else // if (this.getClass().equals(Sentinel.class))
            copy = new Sentinel(new Coordinate(0, 0));
        copy.sectors = sectorsCopy();
        copy.moves = moves;
        if (isDone())
            copy.setDone();
        copy.name = name;
        copy.speed = speed;
        copy.maxSize = maxSize;
        copy.team = team;
        return copy;
    }

    public LinkedList<Coordinate> sectorsCopy() {
        LinkedList<Coordinate> copy = new LinkedList<Coordinate>();
        for (Coordinate coord : sectors)
            copy.add(coord.copy());
        return copy;
    }

    public static class Hack extends Unit {

        public Hack(Coordinate coord) {
            name = "Hack";
            speed = 2;
            maxSize = 4;
            team = Team.PLAYER;

            init(coord);
        }
    }

    public static class Sentinel extends Unit {

        public Sentinel(Coordinate coord) {
            name = "Sentinel";
            speed = 1;
            maxSize = 4;
            team = Team.COMPUTER;

            init(coord);
        }
    }
}
