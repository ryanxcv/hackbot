package hackbotui;

import hackbotcore.*;
import hackbotcore.Unit.*;
import hackbotutil.Coordinate;

public class TestGame {

    // Set up a simple testing environment
    public static void main(String[] args) {

        // Set up a test playing field.
        Grid grid = new Grid(10, 10);
        GameInterface iface = new GameInterface(grid);
        grid.addUnit(new Hack(new Coordinate(1, 1)));
        grid.addUnit(new Sentinel(new Coordinate(6, 4)));

        UI ui = new UI(iface);
    }
}
