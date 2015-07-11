package hackbotcore;

import java.util.LinkedList;

/**
 * Allows for safe engine-UI interaction by providing public helper methods.
 * The entire contents of the game engine are concealed, and only these
 * functions can be run from outside.
 */
public class GameInterface {

    private Grid grid;

    public GameInterface(Grid grid) {
        this.grid = grid;
    }

    /**
     * GAMESTATE COMMANDS
     *
     * The following methods deliver commands to the game engine, and are
     * intended to change the gamestate. They may also return information
     * regarding the gamestate.
     */

     /** Selects the unit at a given tile. **/
    public boolean selectUnit(Unit unit) {
        return false;
    }

    /** Moves the selected unit to a given tile. **/
    public boolean moveToTile(Tile tile) {
        return false;
    }

    /** Select an attack from among a unit's attacks. **/
    public boolean selectAttack(int index) {
        return false;
    }

    /** Attack a given tile with the currently selected attack. **/
    public boolean attackTile() {
        return false;
    }

    /** Undo any movement made by the selected unit this turn. **/
    public boolean undo() {
        return false;
    }

    public boolean done() {
        return false;
    }

    /**
     * GAMESTATE INFO
     *
     * The following methods read information from the game engine only, and
     * must not affect the gamestate.
     */

    /** Returns the list of units currently on the board. **/
    public LinkedList<Unit> getUnitList() {
        return null;
    }

    /** Returns the selected unit. **/
    public Unit getSelectedUnit() {
        return grid.selected;
    }

    /** Returns the selected attack. **/
    public void getSelectedAttack() {
    }

    /** Temporary accessor **/
    public Grid getGrid() {
        return grid;
    }

    /** Returns the grid tiles. **/
    public Tile[][] getGridTiles() {
        return grid.tiles;
    }
}
