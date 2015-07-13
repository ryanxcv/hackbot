package hackbotcore;

import java.util.concurrent.Callable;
import java.util.LinkedList;

import hackbotcore.Ability.*;
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
    private Ability[] abilities;

    public static enum Team { PLAYER, COMPUTER;
        private static Team[] vals = values();
        public Team next() {
            return vals[(ordinal() + 1) % vals.length];
        }
    }

    public static enum TurnState { READY, MOVING, USING_ABILITY, DONE }

    public Unit(Coordinate coord, String name, int speed, int maxSize,
                Team team, Ability[] abilities) {
        sectors = new LinkedList<Coordinate>();
        savedSectors = new LinkedList<Coordinate>();
        sectors.add(coord);
        savedSectors.add(coord);
        this.sectors = sectors;
        init(speed, TurnState.READY, name, speed, maxSize, team, abilities);
    }

    /** General constructor for creating a Unit when all fields are known. **/
    public Unit(LinkedList<Coordinate> sectors, int moves, TurnState state,
                String name, int speed, int maxSize, Team team,
                Ability[] abilities) {
        this.sectors = sectors;
        savedSectors = sectorsCopy();
        init(moves, state, name, speed, maxSize, team, abilities);

    }

    private void init(int moves, TurnState state, String name, int speed,
                      int maxSize, Team team, Ability[] abilities) {
        this.moves     = moves;
        this.state     = state;
        this.name      = name;
        this.speed     = speed;
        this.maxSize   = maxSize;
        this.team      = team;
        this.abilities = abilities;
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
        if (this.getClass().equals(Hack.class))
            return new Hack(sectors, moves, state, name, speed, maxSize, team,
                            abilities);
        else // if (this.getClass().equals(Sentinel.class))
            return new Sentinel(sectors, moves, state, name, speed, maxSize,
                                team, abilities);
    }

    public LinkedList<Coordinate> sectorsCopy() {
        LinkedList<Coordinate> copy = new LinkedList<Coordinate>();
        for (Coordinate coord : sectors)
            copy.add(coord.copy());
        return copy;
    }

    public static class Hack extends Unit {

        public Hack(Coordinate coord) {
            super(coord, "Hack", 2, 4, Team.PLAYER, null);
        }
        public Hack(LinkedList<Coordinate> sectors, int moves, TurnState state,
                    String name, int speed, int maxSize, Team team,
                    Ability[] abilities) {
            super(sectors, moves, state, name, speed, maxSize, team, abilities);
        }
    }

    public static class Sentinel extends Unit {

        public Sentinel(Coordinate coord) {
            super(coord, "Sentinel", 1, 4, Team.COMPUTER,
                  new Ability[] { new Cut() });
        }
        public Sentinel(LinkedList<Coordinate> sectors, int moves, TurnState state,
                    String name, int speed, int maxSize, Team team,
                    Ability[] abilities) {
            super(sectors, moves, state, name, speed, maxSize, team,
                  new Ability[] { new Cut() });
        }
    }
}
