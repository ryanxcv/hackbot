package hackbotcore;

import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;

import hackbotutil.Coordinate;

/**
 * This class is the core of the battle engine. It keeps track of the gameboard
 * and pieces as well as the gamestate, and provides helper functions. Just as
 * the GameInterface connects the UI to the engine, this class connects the
 * GameInterface to the low-level piece positions and states.
 */
public class Battle {

    private final int columns;
    private final int rows;
    protected final Set<Unit> playerUnits;
    protected final Set<Unit> computerUnits;
    protected Unit selected;
    protected Ability selectedAbility;

    private Unit.Team turn;

    public Battle(int columns, int rows) {
        playerUnits   = new HashSet<Unit>();
        computerUnits = new HashSet<Unit>();
        selected = null;
        turn = Unit.Team.PLAYER;
        this.columns = columns;
        this.rows    = rows;
    }

    protected int getWidth() { return columns; }
    protected int getHeight() { return rows; }

    protected Unit.Team getTurn() { return turn; }

    /**
     * Runs at the start of each turn to set up units.
     */
    protected void beginTurn() {
        for (Unit u : allUnits())
            u.reset();
    }

    protected void passTurn() {
        turn = turn.next();
        beginTurn();
    }

    protected Set<Unit> allUnits() {
        HashSet<Unit> result = new HashSet<Unit>(playerUnits);
        result.addAll(computerUnits);
        return result;
    }

    private boolean allUnitsDone() {
        if (turn == Unit.Team.PLAYER) {
            for (Unit u : playerUnits)
                if (!u.isDone())
                    return false;
            return true;
        } else {
            for (Unit u : computerUnits)
                if (!u.isDone())
                    return false;
            return true;
        }
    }

    protected void selectFirst() {
        if (turn == Unit.Team.PLAYER) {
            for (Unit u : playerUnits) {
                selected = u;
                return;
            }
        } else {
            for (Unit u : computerUnits) {
                selected = u;
                return;
            }
        }
    }

    protected void useAbility(Coordinate coord) {
        assert selected        != null;
        assert selectedAbility != null;
        Unit target = unitFromTile(coord);
        if (selected.useAbility(selectedAbility, target)) {
            // Remove any dead units from the grid.
            for (Unit u : playerUnits)
                if (u.sectors.size() == 0) {
                    assert playerUnits.remove(u);
                    break;
                }
            for (Unit u : computerUnits)
                if (u.sectors.size() == 0) {
                    assert computerUnits.remove(u);
                    break;
                }
        }
    }

    /**
     * This function tells a program whether it is possible for it to occupy a
     * given tile; i.e, the tile is not already occupied by another program,
     * zeroed by a Bit-Man, etc.
     */
    protected boolean canOccupy(Unit unit, Coordinate coord) {
        // Check if the program already occupies that tile.
        if (unit.contains(coord))
            return true;

        // Check if any other programs occupy that tile.
        for (Unit u : allUnits())
            if (u.contains(coord))
            	return false;

        // Otherwise, it should be occupable.
        return true;
    }

    public void addPlayerUnit(  Unit unit)   { playerUnits.add(unit); }
    public void addComputerUnit(Unit unit) { computerUnits.add(unit); }

    /**
     * Get the unit (if any) that occupies a chosen tile.
     */
    public Unit unitFromTile(Coordinate coord) {
        // Iterate through all the programs.
        for (Unit u : allUnits())
            for (Coordinate c : u.sectors)
                if (coord.equals(c))
                    return u;
        return null;
    }
}
