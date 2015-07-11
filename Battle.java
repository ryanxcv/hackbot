package hackbotcore;

import java.util.LinkedList;

import hackbotutil.Coordinate;

/**
 * This class is the core of the battle engine. It keeps track of the gameboard
 * and pieces as well as the gamestate, and provides helper functions. Just as
 * the GameInterface connects the UI to the engine, this class connects the
 * GameInterface to the low-level piece positions and states.
 */
public class Battle {

    private int columns;
    private int rows;
    protected LinkedList<Unit> units;
    protected Unit selected;

    public Battle(int columns, int rows) {
        units = new LinkedList<Unit>();
        selected = null;
        this.columns = columns;
        this.rows    = rows;
    }

    protected int getWidth() { return columns; }

    protected int getHeight() { return rows; }

    /**
     * This function tells a program whether it is possible for it to occupy a
     * given tile; i.e, the tile is not already occupied by another program,
     * zeroed by a Bit-Man, etc.
     */
    protected boolean canOccupy(Unit unit, Coordinate coord) {
        // Check if the program already occupies that tile.
        if (unit.sectors.contains(coord))
            return true;

        // Otherwise, it should be occupable.
        return true;
    }

    public void addUnit(Unit unit) { units.add(unit); }

    /**
     * Get the unit (if any) that occupies a chosen tile.
     */
    public Unit unitFromTile(Coordinate coord) {
        // Iterate through all the programs.
        for (Unit u : units)
            for (Coordinate c : u.sectors)
                if (coord.equals(c))
                    return u;
        return null;
    }
}
