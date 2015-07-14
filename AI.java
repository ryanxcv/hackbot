package hackbotui;

import hackbotcore.*;
import hackbotutil.*;

public class AI {

    private UI gameui;

    public AI(UI gameui) {
        this.gameui = gameui;
    }

    /**
     * A simple AI will do as follows:
     * Move toward the closest enemy, and attack if possible.
     */


    public void conductTurn() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println(e);
        }
        gameui.passTurn();
    }

    public void makeMove(Unit unit) {
    }
}
