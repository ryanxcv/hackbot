package hackbotui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Graphics;
import java.util.Collection;
import javax.swing.JComponent;

import java.applet.AudioClip;

import hackbotcore.*;
import hackbotutil.Coordinate;

/** An class derived from JComponent that displays a custom image. **/
class FieldDisplay extends JComponent {

    private GameInterface iface;

    /** The edge length of a tile in pixels. **/
    public static final int TILE_PIX_SIZE = 32;

    /** Images **/
    private BufferedImage bgImage;
    private BufferedImage imgTile;
    private BufferedImage imgDone;
    private BufferedImage imgSelect;
    private BufferedImage imgMove;
    private BufferedImage imgUp;
    private BufferedImage imgDown;
    private BufferedImage imgLeft;
    private BufferedImage imgRight;

    // Temporary unit images
    private BufferedImage imgHead;
    private BufferedImage imgBody;

    /** Audio **/
    private AudioClip sndSelect;
    private AudioClip sndMove;

    /** Class constructor. **/
    public FieldDisplay(GameInterface iface) {
        this.iface = iface;

        // Compute the necessary field size.
        int columns = iface.getWidth();
        int rows    = iface.getHeight();
        int width = columns * TILE_PIX_SIZE;
        int height = rows   * TILE_PIX_SIZE;
        setPreferredSize(new Dimension(width, height));

        // Load images.
        bgImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        imgTile   = UI.getImage(     "tile.png");
        imgDone   = UI.getImage(     "done.png");
        imgSelect = UI.getImage("selection.png");
        imgMove   = UI.getImage(     "move.png");
        imgUp     = UI.getImage(       "up.png");
        imgDown   = UI.getImage(     "down.png");
        imgLeft   = UI.getImage(     "left.png");
        imgRight  = UI.getImage(    "right.png");

        // Temporary unit images
        imgHead = UI.getImage("hack.png");
        imgBody = UI.getImage("hack_body.png");

        // Load sounds.
        sndSelect = UI.getSound("sound1.wav");
        sndMove   = UI.getSound("sound2.wav");

        addMouseListener(new FieldMouseListener(iface));
    }

    /** Display the current field data. **/
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image.
        g.drawImage(bgImage, 0, 0, bgImage.getWidth(), bgImage.getHeight(), null);

        // Draw the tiles.
        for (int c = 0; c < iface.getWidth(); c++)
            for (int r = 0; r < iface.getHeight(); r++)
                drawTile(g, new Coordinate(c, r), imgTile);

        // Draw the units. (To do: fix)
        for (Unit unit : iface.getUnitList()) {
            drawTile(g, unit.getHead(), imgHead);
            if (unit.isDone())
                drawTile(g, unit.getHead(), imgDone);
            for (int i = 1; i < unit.sectors.size(); i++)
                drawTile(g, unit.sectors.get(i), imgBody);
        }

        // If there is a selection, draw the selection overlay.
        Unit selected = iface.getSelectedUnit();
        if (selected == null)
            return;

        drawTile(g, selected.getHead(), imgSelect);

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
        System.out.println(selected.getMoves());
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
            // Get the coords that were clicked.
            Coordinate coords = pixelToCoords(e.getX(), e.getY());
            // Ignore clicks outside of the playing field
            if (coords.getRow() == -1) { return; }

            Unit clickedUnit = unitFromTile(coords);
            Unit selected = iface.getSelectedUnit();

            // Program selection
            if (selected == null) {
                if (iface.selectUnit(coords)) {
                    sndSelect.play();
                    repaint();
                }
                return;
            }

            // Movement
            if (selected.distance(coords) == 1) {
                if (clickedUnit == null || selected.contains(coords)) {
                    if (iface.moveToTile(coords))
                        repaint();
                    else
                        System.out.println("Movement failed");
                } else {
                    if (iface.selectUnit(coords));
                        repaint();
                }
            // Deselection
            } else if (selected.distance(coords) > 1) {
                if (iface.selectUnit(coords));
                    repaint();
            }
        }
    }
}
