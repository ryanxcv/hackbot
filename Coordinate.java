package hackbotutil;

public class Coordinate {

    private int column;
    private int row;

    public Coordinate(int column, int row) {
        this.column = column;
        this.row    = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public boolean equals(Coordinate coord2) {
        return column == coord2.getColumn() &&
               row    == coord2.getRow();
    }

    /** Returns the integer distance to another Coordinate. **/
    public int distance(Coordinate coord2) {
        return Math.abs(column - coord2.getColumn()) +
               Math.abs(row    - coord2.getRow());
    }

    /** Same as above, but for literal coordinates. **/
    public int distance(int column, int row) {
        return Math.abs(this.column - column) +
               Math.abs(this.row    - row);
    }

    /** Returns a copy of this Coordinate. **/
    public Coordinate copy() {
        return new Coordinate(column, row);
    }
}
