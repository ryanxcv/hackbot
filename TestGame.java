package hackbotui;

import hackbotcore.*;
import hackbotcore.Unit.*;
import hackbotutil.Coordinate;

public class TestGame {

    // Set up a simple testing environment
    public static void main(String[] args) {

        // Set up a test playing field.
        Battle battle = new Battle(10, 10);
        GameInterface iface = new GameInterface(battle);
        battle.addUnit(new Hack(new Coordinate(1, 2)));
        battle.addUnit(new Sentinel(new Coordinate(6, 4)));

        UI ui = new UI(iface);
    }
}
