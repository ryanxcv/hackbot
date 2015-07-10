package spybot;

import javax.swing.JComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Graphics;

/** An class derived from JComponent that displays a custom image. **/
class FieldDisplay extends JComponent {

    /** The grid to be displayed. **/
    private Grid grid;

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

    /** Class constructor for given dimensions. **/
    public FieldDisplay(Grid grid) {
        this.grid = grid;

        // Compute the necessary field size.
        int columns = grid.tiles.length;
        int rows    = grid.tiles[0].length;
        int width = columns * TILE_PIX_SIZE;
        int height = rows   * TILE_PIX_SIZE;
        setPreferredSize(new Dimension(width, height));

        // Load images.
        bgImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        imgTile   = Unit.getImage(     "tile.png");
        imgSelect = Unit.getImage("selection.png");
        imgUp     = Unit.getImage(       "up.png");
        imgDown   = Unit.getImage(     "down.png");
        imgLeft   = Unit.getImage(     "left.png");
        imgRight  = Unit.getImage(    "right.png");

        addMouseListener(new FieldMouseListener());
    }

    /** Display the current field data. **/
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image.
        g.drawImage(bgImage, 0, 0, bgImage.getWidth(), bgImage.getHeight(), null);

        // Draw the tiles.
        for (Tile[] column : grid.tiles)
            for (Tile tile : column)
                if (tile.isFilled())
                    drawTile(g, tile, imgTile);

        // Draw the units.
        for (Unit unit : grid.units) {
            drawTile(g, unit.getHead(), unit.imgHead);
            for (int i = 1; i < unit.sectors.size(); i++)
                drawTile(g, unit.sectors.get(i), unit.imgBody);
        }

        // If there is a selection, draw the selection overlay.
        if (grid.selected == null)
            return;

        drawTile(g, grid.selected.getHead(), imgSelect);

        // Draw the movement overlays.
        Tile selectionHead = grid.selected.getHead();
        int column = selectionHead.getColumn();
        int row    = selectionHead.getRow();
        if (row > 0)
            drawTile(g, grid.tiles[column][row - 1], imgUp);
        if (row < grid.tiles[0].length - 1)
            drawTile(g, grid.tiles[column][row + 1], imgDown);
        if (column > 0)
            drawTile(g, grid.tiles[column - 1][row], imgLeft);
        if (column < grid.tiles.length - 1)
            drawTile(g, grid.tiles[column + 1][row], imgRight);
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

    /** Handles mouse clicks within the field object. **/
    private class FieldMouseListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            // Get the coords that were clicked.
            int[] coords = pixelToCoords(e.getX(), e.getY());
            // Ignore clicks outside of the playing field
            if (coords[0] == -1) { return; }
            // Get the tile that was clicked.
            Tile clickedTile = grid.tiles[coords[0]][coords[1]];
            Unit clickedUnit = grid.unitFromTile(clickedTile);

            // Program selection
            if (grid.selected == null) {
                grid.selected = clickedUnit;
                repaint();
                return;
            }

            // Movement
            if (grid.selected.distance(coords[0], coords[1]) == 1) {
                if (clickedUnit == null || grid.selected == clickedUnit) {
                    grid.selected.move(clickedTile);
                    repaint();
                } else {
                    grid.selected = clickedUnit;
                    repaint();
                }
            // Deselection
            } else if (grid.selected.distance(coords[0], coords[1]) > 1) {
                grid.selected = clickedUnit;
                repaint();
            }
        }
    }
}
