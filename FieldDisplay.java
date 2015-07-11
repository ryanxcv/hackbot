package hackbotui;

import javax.swing.JComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Graphics;

import hackbotcore.*;

/** An class derived from JComponent that displays a custom image. **/
class FieldDisplay extends JComponent {

    private GameInterface iface;

    /** The edge length of a tile in pixels. **/
    public static final int TILE_PIX_SIZE = 32;

    /** Images **/
    private BufferedImage bgImage;
    private BufferedImage imgTile;
    private BufferedImage imgSelect;
    private BufferedImage imgUp;
    private BufferedImage imgDown;
    private BufferedImage imgLeft;
    private BufferedImage imgRight;

    // Temporary unit images
    private BufferedImage imgHead;
    private BufferedImage imgBody;

    /** Class constructor for given dimensions. **/
    public FieldDisplay(GameInterface iface) {
        this.iface = iface;

        // Compute the necessary field size.
        Tile[][] tiles = iface.getGridTiles();
        int columns = tiles.length;
        int rows    = tiles[0].length;
        int width = columns * TILE_PIX_SIZE;
        int height = rows   * TILE_PIX_SIZE;
        setPreferredSize(new Dimension(width, height));

        // Load images.
        bgImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        imgTile   = UI.getImage(     "tile.png");
        imgSelect = UI.getImage("selection.png");
        imgUp     = UI.getImage(       "up.png");
        imgDown   = UI.getImage(     "down.png");
        imgLeft   = UI.getImage(     "left.png");
        imgRight  = UI.getImage(    "right.png");

        // Temporary unit images
        imgHead = UI.getImage("hack.png");
        imgBody = UI.getImage("hack_body.png");

        addMouseListener(new FieldMouseListener(iface));
    }

    /** Display the current field data. **/
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image.
        g.drawImage(bgImage, 0, 0, bgImage.getWidth(), bgImage.getHeight(), null);

        Tile[][] tiles = iface.getGridTiles();
        // Draw the tiles.
        for (Tile[] column : tiles)
            for (Tile tile : column)
                if (tile.isFilled())
                    drawTile(g, tile, imgTile);

        // Draw the units. (To do: fix)
        for (Unit unit : iface.getUnitList()) {
            drawTile(g, unit.getHead(), imgHead);
            for (int i = 1; i < unit.sectors.size(); i++)
                drawTile(g, unit.sectors.get(i), imgBody);
        }

        // If there is a selection, draw the selection overlay.
        Unit selected = iface.getSelectedUnit();
        if (selected == null)
            return;

        drawTile(g, selected.getHead(), imgSelect);

        // Draw the movement overlays.
        Tile selectionHead = selected.getHead();
        int column = selectionHead.getColumn();
        int row    = selectionHead.getRow();
        if (row > 0)
            drawTile(g, tiles[column][row - 1], imgUp);
        if (row < tiles[0].length - 1)
            drawTile(g, tiles[column][row + 1], imgDown);
        if (column > 0)
            drawTile(g, tiles[column - 1][row], imgLeft);
        if (column < tiles.length - 1)
            drawTile(g, tiles[column + 1][row], imgRight);
    }

    /** Draw an image at a tile. **/
    public void drawTile(Graphics g, Tile tile, Image img) {
        int column = tile.getColumn();
        int row    = tile.getRow();
        g.drawImage(img, TILE_PIX_SIZE * column,
                         TILE_PIX_SIZE * row, null);
    }

    /** Get the tile coordinates of a click, given its x and y position. **/
    public int[] pixelToCoords(int x, int y) {
        return new int[]{x / TILE_PIX_SIZE,
                         y / TILE_PIX_SIZE};
    }

    /**
     * Get the unit (if any) that occupies a chosen tile.
     */
    public Unit unitFromTile(Tile tile) {
        // Iterate through all the programs.
        for (Unit u : iface.getUnitList())
            for (Tile t : u.sectors)
                if (tile == t)
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
            // Get the coords that were clicked.
            int[] coords = pixelToCoords(e.getX(), e.getY());
            // Ignore clicks outside of the playing field
            if (coords[0] == -1) { return; }
            // Get the tile that was clicked.
            Tile[][] tiles = iface.getGridTiles();
            Tile clickedTile = tiles[coords[0]][coords[1]];
            Unit clickedUnit = unitFromTile(clickedTile);

            Unit selected = iface.getSelectedUnit();

            // Program selection
            if (selected == null) {
                iface.selectUnit(clickedUnit);
                repaint();
                return;
            }

            // Movement
            if (selected.distance(coords[0], coords[1]) == 1) {
                if (clickedUnit == null || selected == clickedUnit) {
                    iface.moveToTile(clickedTile);
                    repaint();
                } else {
                    iface.selectUnit(clickedUnit);
                    repaint();
                }
            // Deselection
            } else if (selected.distance(coords[0], coords[1]) > 1) {
                iface.selectUnit(clickedUnit);
                repaint();
            }
        }
    }
}
