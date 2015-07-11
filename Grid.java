package hackbotcore;

import java.util.LinkedList;
import java.util.Map;

import hackbotutil.Coordinate;

/**
}
 * The grid is the playing field, composed of a 2D array of tiles
 * (in [column][row] format) that house the landscape and units.
 *
 * The main function of this class is to house a 2D array of tiles, but it
 * also provides some useful helper functions.
 */
public class Grid {

    private int columns;
    private int rows;
    protected LinkedList<Unit> units;
    protected Unit selected;

    public Grid(int columns, int rows) {
        units = new LinkedList<Unit>();
        selected = null;
        this.columns = columns;
        this.rows    = rows;

        /*
        tiles = new Tile[columns][rows];
        for (int c = 0; c < tiles.length; c++) {
            for (int r = 0; r < tiles[c].length; r++) {
                tiles[c][r] = new Tile(c, r);
            }
        }
        */
    }

    public int getWidth() {
        return columns;
    }

    public int getHeight() {
        return rows;
    }

    /**
     * This function tells a program whether it is possible for it to occupy a
     * given tile; i.e, the tile is not already occupied by another program,
     * zeroed by a Bit-Man, etc.
     */
    protected boolean canOccupy(Unit unit, Coordinate coord) {
        // Check if the program already occupies that tile.
        if (unit.sectors.contains(coord)) { return true; }

        // Otherwise, it should be occupable.
        return true;
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

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
