package hackbotui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;

import java.applet.AudioClip;

import hackbotcore.*;
import hackbotutil.Coordinate;

/** An class derived from JComponent that displays a custom image. **/
class FieldDisplay extends JComponent {

    private final GameInterface iface;
    private final UI gameui;

    /** The edge length of a tile in pixels. **/
    public static final int TILE_PIX_SIZE = 32;

    /** Images **/
    private final BufferedImage imgAttack;
    private final BufferedImage imgDone;
    private final BufferedImage imgDown;
    private final BufferedImage imgLeft;
    private final BufferedImage imgMove;
    private final BufferedImage imgRight;
    private final BufferedImage imgSelect;
    private final BufferedImage imgTile;
    private final BufferedImage imgUp;

    /** Class constructor. **/
    public FieldDisplay(GameInterface iface, UI gameui) {
        this.iface  = iface;
        this.gameui = gameui;

        // Compute the necessary field size.
        int columns = iface.getWidth();
        int rows    = iface.getHeight();
        int width = columns * TILE_PIX_SIZE;
        int height = rows   * TILE_PIX_SIZE;
        setPreferredSize(new Dimension(width, height));

        // Load images.
        imgAttack = UI.getImage(   "attack.png");
        imgDone   = UI.getImage(     "done.png");
        imgDown   = UI.getImage(     "down.png");
        imgLeft   = UI.getImage(     "left.png");
        imgMove   = UI.getImage(     "move.png");
        imgRight  = UI.getImage(    "right.png");
        imgSelect = UI.getImage("selection.png");
        imgTile   = UI.getImage(     "tile.png");
        imgUp     = UI.getImage(       "up.png");

        addMouseListener(new FieldMouseListener(iface));
    }

    /** Display the current field data. **/
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the tiles.
        for (int c = 0; c < iface.getWidth(); c++)
            for (int r = 0; r < iface.getHeight(); r++)
                drawTile(g, new Coordinate(c, r), imgTile);

        // Draw the units.
        for (Unit unit : iface.getUnitList()) {
            // Draw that unit's images.
            drawTile(g, unit.getHead(), unit.imgHead);
            if (unit.isDone())
                drawTile(g, unit.getHead(), imgDone);
            for (int i = 1; i < unit.sectors.size(); i++)
                drawTile(g, unit.sectors.get(i), unit.imgBody);
        }

        // If there is a selection, draw the selection overlay.
        Unit selected = iface.getSelectedUnit();
        if (selected == null)
            return;
        drawTile(g, selected.getHead(), imgSelect);

        // If there is a selected ability, draw its overlay.
        if (selected.getState() == Unit.TurnState.USING_ABILITY) {
            drawTiles(g, iface.getSelectedAbility().rangeSet(selected.getHead()), imgAttack);
            return;
        }

        // Draw the movement overlays.
        if (selected.isDone() || selected.getMoves() == 0)
            return;
        Coordinate selectionHead = selected.getHead();
        int column = selectionHead.getColumn();
        int row    = selectionHead.getRow();
        if (row > 0)
            drawTile(g, new Coordinate(column, row - 1), imgUp);
        if (row < iface.getHeight() - 1)
            drawTile(g, new Coordinate(column, row + 1), imgDown);
        if (column > 0)
            drawTile(g, new Coordinate(column - 1, row), imgLeft);
        if (column < iface.getWidth() - 1)
            drawTile(g, new Coordinate(column + 1, row), imgRight);

        if (selected.getMoves() == 1)
            return;
        drawTiles(g, selectionHead.distanceSet(selected.getMoves(), 1), imgMove);

    }

    /** Draw an image at a tile. **/
    public void drawTile(Graphics g, Coordinate coord, Image img) {
        int column = coord.getColumn();
        int row    = coord.getRow();
        g.drawImage(img, TILE_PIX_SIZE * column,
                         TILE_PIX_SIZE * row, null);
    }
    public void drawTiles(Graphics g, Collection<Coordinate> coords, Image img) {
        for (Coordinate coord : coords)
            drawTile(g, coord, img);
    }

    /** Get the tile coordinates of a click, given its x and y position. **/
    public Coordinate pixelToCoords(int x, int y) {
        return new Coordinate(x / TILE_PIX_SIZE, y / TILE_PIX_SIZE);
    }

    /**
     * Get the unit (if any) that occupies a chosen tile.
     */
    public Unit unitFromTile(Coordinate coord) {
        // Iterate through all the programs.
        for (Unit u : iface.getUnitList())
            for (Coordinate c: u.sectors)
                if (coord.equals(c))
                    return u;
        return null;
    }

    /** Handles mouse clicks within the field object. **/
    private class FieldMouseListener extends MouseAdapter {

        private GameInterface iface;

        public FieldMouseListener(GameInterface iface) {
            this.iface = iface;
        }

        public void mouseClicked(MouseEvent e) {
            if (!gameui.isReady())
                return;
            // Get the coords that were clicked.
            Coordinate coords = pixelToCoords(e.getX(), e.getY());
            // Ignore clicks outside of the playing field
            if (coords.getRow() == -1) { return; }

            Unit clickedUnit = unitFromTile(coords);
            Unit selected = iface.getSelectedUnit();

            // Program selection
            if (selected == null) {
                gameui.trySelect(coords);
                return;
            }

            if (selected.getState() == Unit.TurnState.USING_ABILITY) {
                // Attacking
                Ability ability = iface.getSelectedAbility();
                if (ability.rangeSet(selected.getHead()).contains(coords)) {
                    gameui.tryAbility(coords);
                } else if (selected.distance(coords) == 0) {
                    // The user must have accidentally clicked the head.
                    // Do nothing.
                    return;
                } else {
                    // The user clicked far away from the unit.
                    // Select what they clicked.
                    gameui.trySelect(coords);
                }
            } else if (selected.getMoves() > 0) {
                // Movement
                if (selected.distance(coords) == 1) {
                    if (clickedUnit == null || selected.contains(coords)) {
                        gameui.tryMove(coords);
                    } else {
                        gameui.trySelect(coords);
                    }
                // Deselection
                } else if (selected.distance(coords) > 1) {
                    gameui.trySelect(coords);
                }
            } else {
                gameui.trySelect(coords);
            }

        }
    }
}
