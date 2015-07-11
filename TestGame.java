package hackbotui;

import hackbotcore.*;

public class TestGame {

    // Set up a simple testing environment
    public static void main(String[] args) {

        // Set up a test playing field.
        Grid grid = new Grid(10, 10);
        grid.addUnit(new Unit.Hack(grid.tiles[1][1]));
        grid.addUnit(new Unit.Sentinel(grid.tiles[6][4]));

        UI ui = new UI(grid);
    }
}
