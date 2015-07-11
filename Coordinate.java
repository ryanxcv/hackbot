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

    /** Returns the integer distance to another Coordinate. **/
    public int distance(Coordinate coord2) {
        return Math.abs(column - coord2.getColumn()) +
               Math.abs(row    - coord2.getRow());
    }

    /** Returns a copy of this Coordinate. **/
    public Coordinate copy() {
        return new Coordinate(column, row);
    }
}
