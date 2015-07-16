package hackbotui;

import java.util.Collection;
import java.util.LinkedList;

import hackbotcore.*;
import hackbotutil.*;

public class AI extends Thread {

    private UI gameui;
    private GameInterface iface;

    public AI(UI gameui, GameInterface iface) {
        this.gameui = gameui;
        this.iface  = iface;
    }

    /**
     * A simple AI will do as follows:
     * Move toward the closest enemy, and attack if possible.
     */


    public void run() {
        LinkedList<Unit> units;
        Unit selected;
        while (true) {
            units = iface.getUnitList();
            // If every unit is done, pass turn.
            if (allDone(units))
                break;
            // If nothing is selected, select something.
            selected = iface.getSelectedUnit();
            if (selected == null)
                break;
            // If the selected unit isn't done moving, move it a space.
            if (selected.getMoves() > 0)
                makeMove(selected);
            else
                break;
        }
        gameui.setReady(true);
        gameui.passTurn();
    }

    public boolean allDone(Collection<Unit> units) {
        for (Unit u : units)
            if (!u.isDone())
                return false;
        return true;
    }

    public void makeMove(Unit unit) {
        iface.selectUnit(unit.getHead());
        // Get the closest enemy.
        Unit target = closest(unit);
        assert target != null;
        // Move toward it.
        int moves = unit.getMoves();
        Coordinate offset = unit.getHead().offset(target.getHead());
        int xDist = offset.getColumn();
        int yDist = offset.getRow();
        if (Math.abs(xDist) < Math.abs(yDist)) {
            if (yDist < 0)
                assert iface.move(Direction.UP);
            else
                assert iface.move(Direction.DOWN);
        } else {
            if (xDist < 0)
                assert iface.move(Direction.LEFT);
            else
                assert iface.move(Direction.RIGHT);
        }
        gameui.pause();
    }

    private Unit closest(Unit unit) {
        int max = Integer.MAX_VALUE;
        Unit result = null;
        for (Unit u : iface.getUnitList())
            if (u.getTeam() == Unit.Team.PLAYER)
                if (unit.distance(u) < max) {
                    max = unit.distance(u);
                    result = u;
                }
        return result;
    }
}
