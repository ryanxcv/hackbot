package hackbotui;

import hackbotcore.*;
import hackbotcore.Unit.*;
import hackbotutil.Coordinate;

public class TestGame {

    // Set up a simple testing environment
    public static void main(String[] args) {

        // Set up a test playing field.
        Battle battle = new Battle(14, 12);
        GameInterface iface = new GameInterface(battle);
        battle.addPlayerUnit(new Hack(new Coordinate(1, 2)));
        battle.addComputerUnit(new Sentinel(new Coordinate(6, 6)));

        UI ui = new UI(iface);
    }
}
