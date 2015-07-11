package hackbotcore;

import java.util.LinkedList;

import hackbotutil.Coordinate;

/**
 * Allows for safe engine-UI interaction by providing public helper methods.
 * The entire contents of the game engine are concealed, and only these
 * functions can be run from outside.
 */
public class GameInterface {

    private Battle battle;

    public GameInterface(Battle battle) {
        this.battle = battle;
    }

    /**
     * GAMESTATE COMMANDS
     *
     * The following methods deliver commands to the game engine, and are
     * intended to change the gamestate. They may also return information
     * regarding the gamestate.
     */

     /** Selects a given unit. **/
    public boolean selectUnit(Coordinate coord) {
        Unit unit = battle.unitFromTile(coord);
        battle.selected = unit;
        return battle.selected == null;
    }

    /** Moves the selected unit to a given tile. **/
    public boolean moveToTile(Coordinate coord) {
        // Check if the unit's done
        if (battle.selected.isDone())
            return false;

        // Check if the destination is valid
        if (!battle.canOccupy(battle.selected, coord))
            return false;

        // Movement is successful. Make the move and return true.
        battle.selected.move(coord);
        return true;
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

    public boolean setDone() {
        if (battle.selected.isDone())
            return false;
        battle.selected.setDone();
        return true;
    }

    /**
     * GAMESTATE INFO
     *
     * The following methods read information from the game engine only, and
     * must not affect the gamestate.
     */

    /** Returns the list of units currently on the board. **/
    public LinkedList<Unit> getUnitList() {
        return battle.units;
    }

    /** Returns the selected unit. **/
    public Unit getSelectedUnit() {
        return battle.selected;
    }

    /** Returns the selected attack. **/
    public void getSelectedAttack() {
    }

    public int getWidth() {
        return battle.getWidth();
    }

    public int getHeight() {
        return battle.getHeight();
    }
}
