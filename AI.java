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
        LinkedList<Unit> computerUnits;
        Unit selected;
        while (true) {
            computerUnits = iface.getComputerUnitList();
            // If every unit is done, pass turn.
            if (allDone(computerUnits)) {
                gameui.passTurn();
                return;
            }
            // If nothing is selected, or the selected unit is done,
            // select something.
            selected = iface.getSelectedUnit();
            if (selected == null || selected.isDone()) {
                for (Unit u : computerUnits)
                    if (!u.isDone()) {
                        assert iface.selectUnit(u.getHead());
                        break;
                    }
                selected = iface.getSelectedUnit();
            }
            // If the selected unit isn't done moving, move it a space.
            if (selected.getMoves() > 0) {
                makeMove(selected);
            }
            // Otherwise, use its ability on the closest enemy.
            else {
                gameui.trySelectAbility(0);
                gameui.pause();
                iface.useAbility(closestEnemy(selected).getHead());
                iface.setDone();
                gameui.update();
                gameui.pause();
            }
        }
    }

    public boolean allDone(Collection<Unit> units) {
        for (Unit u : units)
            if (!u.isDone())
                return false;
        return true;
    }

    public void makeMove(Unit unit) {
        // Try moving in the preferred directions.
        for (Direction d : getPreferredDirections(unit)) {
            if (gameui.tryMove(d)) {
                gameui.pause();
                return;
            }
        }
    }

    private LinkedList<Direction> getPreferredDirections(Unit unit) {
        Unit target = closestEnemy(unit);
        LinkedList<Direction> result = new LinkedList<Direction>();
        if (unit.distance(target) > 1) {
            Coordinate offset = unit.getHead().offset(target.getHead());
            int xDist = offset.getColumn();
            int yDist = offset.getRow();
            if (Math.abs(xDist) < Math.abs(yDist)) {
                if (yDist < 0) {
                    result.add(Direction.UP);
                    if (xDist < 0) {
                        result.add(Direction.LEFT);
                        result.add(Direction.RIGHT);
                    } else {
                        result.add(Direction.RIGHT);
                        result.add(Direction.LEFT);
                    }
                    result.add(Direction.DOWN);
                } else {
                    result.add(Direction.DOWN);
                    if (xDist < 0) {
                        result.add(Direction.LEFT);
                        result.add(Direction.RIGHT);
                    } else {
                        result.add(Direction.RIGHT);
                        result.add(Direction.LEFT);
                    }
                    result.add(Direction.UP);
                }
            } else {
                if (xDist < 0) {
                    result.add(Direction.LEFT);
                    if (xDist < 0) {
                        result.add(Direction.UP);
                        result.add(Direction.DOWN);
                    } else {
                        result.add(Direction.DOWN);
                        result.add(Direction.UP);
                    }
                    result.add(Direction.RIGHT);
                } else {
                    result.add(Direction.RIGHT);
                    if (xDist < 0) {
                        result.add(Direction.UP);
                        result.add(Direction.DOWN);
                    } else {
                        result.add(Direction.DOWN);
                        result.add(Direction.UP);
                    }
                    result.add(Direction.LEFT);
                }
            }
            assert result.size() == 4;
            return result;
        }
        result.add(Direction.UP);
        result.add(Direction.DOWN);
        result.add(Direction.LEFT);
        result.add(Direction.RIGHT);
        return result;
    }

    private Unit closestEnemy(Unit unit) {
        int max = Integer.MAX_VALUE;
        Unit result = null;
        for (Unit u : iface.getPlayerUnitList())
            if (unit.distance(u) < max) {
                max = unit.distance(u);
                result = u;
            }
        return result;
    }
}
