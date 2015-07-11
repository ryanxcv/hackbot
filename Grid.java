package hackbotcore;

import java.util.LinkedList;
import java.util.Map;

/**
}
 * The grid is the playing field, composed of a 2D array of tiles
 * (in [column][row] format) that house the landscape and units.
 *
 * The main function of this class is to house a 2D array of tiles, but it
 * also provides some useful helper functions.
 */
public class Grid {

    protected Tile[][] tiles;
    protected LinkedList<Unit> units;
    protected Unit selected;

    public Grid(int columns, int rows) {
        units = new LinkedList<Unit>();
        tiles = new Tile[columns][rows];
        selected = null;
        for (int c = 0; c < tiles.length; c++) {
            for (int r = 0; r < tiles[c].length; r++) {
                tiles[c][r] = new Tile(c, r);
            }
        }
    }

    /**
     * This function tells a program whether it is possible for it to occupy a
     * given tile; i.e, the tile is not already occupied by another program,
     * zeroed by a Bit-Man, etc.
     */
    protected boolean canOccupy(Unit unit, Tile tile) {
        // Check if the program already occupies that tile.
        if (unit.sectors.contains(tile)) { return true; }

        // Check that the space is filled.
        if (!tile.isFilled()) { return false; }

        // Otherwise, it should be occupable.
        return true;
    }

    protected void addUnit(Unit unit) {
        units.add(unit);
    }

    /**
     * Get the unit (if any) that occupies a chosen tile.
     */
    protected Unit unitFromTile(Tile tile) {
        // Iterate through all the programs.
        for (Unit u : units) {
            for (Tile t : u.sectors) {
                if (tile == t) {
                    return u;
                }
            }
        }
        return null;
    }
}
