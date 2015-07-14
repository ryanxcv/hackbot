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

    /** Selects a given unit. Returns true if the selection changed. **/
    public boolean selectUnit(Coordinate coord) {
        Unit lastSelection = battle.selected;
        battle.selected = battle.unitFromTile(coord);
        if (lastSelection != battle.selected) {
            if (lastSelection != null)
                lastSelection.deselect();
            return true;
        }
        return false;
    }

    /** Moves the selected unit to a given tile. **/
    public boolean moveToTile(Coordinate coord) {
        // Make sure there is a selected unit on the current team that isn't
        // done, with a valid destination.
        Unit u = battle.selected;
        if (u == null || u.getTeam() != battle.getTurn() ||
            u.getState() == Unit.TurnState.USING_ABILITY ||
            u.isDone() || u.moves < 1 || !battle.canOccupy(u, coord))
            return false;

        // Movement is successful. Make the move and return true.
        battle.selected.move(coord);
        return true;
    }

    /** Select an ability from among a unit's abilities. **/
    public boolean selectAbility(int index) {
        // Make sure there is a selected unit that isn't
        // done, with a valid ability.
        if (index < 0) {
            battle.selectedAbility = null;
            return true;
        }
        Unit u = battle.selected;
        if (u == null || u.isDone() || u.abilities == null || u.abilities.length <= index)
            return false;
        u.setState(Unit.TurnState.USING_ABILITY);
        battle.selectedAbility = u.abilities[index];
        return true;
    }

    /** Use the currently selected ability on a given tile. **/
    public boolean useAbility(Coordinate coord) {
        if (battle.selected == null || battle.selectedAbility == null ||
            battle.selected.isDone())
            return false;
        Unit target = battle.unitFromTile(coord);
        if (target == null || battle.selected.contains(coord)) {
            battle.selected.setDone();
            return false;
        }
        battle.useAbility(target);
        return true;
    }

    public boolean canUndo() {
        Unit u = battle.selected;
        return u != null &&
               u.getTeam() == battle.getTurn() &&
               (u.getState() == Unit.TurnState.MOVING ||
                u.getState() == Unit.TurnState.USING_ABILITY);
    }

    /** Undo any movement made by the selected unit this turn. **/
    public boolean undo() {
        if (!canUndo())
            return false;
        return battle.selected.undo();
    }

    public boolean setDone() {
        if (battle.selected == null || battle.selected.isDone() ||
            battle.selected.getTeam() != battle.getTurn())
            return false;

        battle.selected.setDone();
        return true;
    }

    public void passTurn() {
        battle.passTurn();
        battle.selectFirst();
    }

    /**
     * GAMESTATE INFO
     *
     * The following methods read information from the game engine only, and
     * must not affect the gamestate.
     */

    /** Returns the list of units currently on the board. **/
    public LinkedList<Unit> getUnitList() {
        LinkedList<Unit> result = new LinkedList<Unit>();
        for (Unit u : battle.units)
            result.add(u.copy());
        return result;
    }

    /** Returns the selected unit. **/
    public Unit getSelectedUnit() {
        if (battle.selected == null)
            return null;
        return battle.selected.copy();
    }

    /** Returns the selected ability. **/
    public Ability getSelectedAbility() {
        return battle.selectedAbility;
    }

    public Unit.Team getTurn() {
        return battle.getTurn();
    }

    public int getWidth() {
        return battle.getWidth();
    }

    public int getHeight() {
        return battle.getHeight();
    }
}
