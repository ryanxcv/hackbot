package hackbotui;

import hackbotcore.*;
import hackbotcore.Unit.*;

public class TestGame {

    // Set up a simple testing environment
    public static void main(String[] args) {

        // Set up a test playing field.
        Grid grid = new Grid(10, 10);
        GameInterface iface = new GameInterface(grid);
        grid.addUnit(new Hack(iface.getGridTiles()[1][1]));
        grid.addUnit(new Sentinel(iface.getGridTiles()[6][4]));

        UI ui = new UI(iface);
    }
}
