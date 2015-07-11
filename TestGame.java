package hackbotui;

public class TestGame {

    // Set up a simple testing environment
    public static void main(String[] args) {

        // Set up a test playing field.
        Grid grid = new Grid(10, 10);
        grid.addUnit(new Hack(grid.tiles[1][1]));
        grid.addUnit(new Sentinel(grid.tiles[6][4]));

        UI ui = new UI(grid);
    }
}
