package hackbotcore;

/**
 * This class represents a single tile on the game board, which may or may not
 * be filled, contain a piece of a program, etc.
 */
public class Tile {

    private int column;
    private int row;

    private boolean filled;

    public Tile(int column, int row) {
        this.column = column;
        this.row    = row;

        filled = true;
    }

    public Tile() {
        filled = true;
    }

    // Accessors for class properties.
    protected int getColumn() { return column; }
    protected int getRow() { return row; }

    protected void setColumn(int c) { column = c; }
    protected void setRow(int c) { column = c; }

    protected boolean isFilled() { return filled; }

    // Returns the travel distance to another tile.
    protected int distanceTo(Tile tile) {
        return Math.abs(this.column - tile.getColumn()) +
               Math.abs(this.row    - tile.getRow());
    }

    /**
     * Returns the pixel coordinates of this tile, given the size of a tile in
     * pixels and the offset of the grid.
     */
    protected int[] position(int tileSize, int xOffSet, int yOffSet) {
        return new int[] {column * tileSize + xOffSet, row * tileSize + yOffSet};
    }

    protected int[] coords() {
        return new int[] {column, row};
    }
}
