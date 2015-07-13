package hackbotutil;

import java.util.Set;
import java.util.TreeSet;

public class Coordinate implements Comparable<Coordinate> {

    private int column;
    private int row;

    public Coordinate(int column, int row) {
        this.column = column;
        this.row    = row;
    }

    public int getColumn() { return column; }

    public int getRow() { return row; }

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

    /** Returns the set of all Coordinates within a given distance. **/
    public Set<Coordinate> distanceSet(int max) {
        Set<Coordinate> result = new TreeSet<Coordinate>();
        for (int c = column - max; c <= column + max; c++)
            for (int r = row - max; r <= row + max; r++)
                if (distance(c, r) <= max)
                    result.add(new Coordinate(c, r));
        return result;
    }
    public Set<Coordinate> distanceSet(int max, int min) {
        assert max > min;
        Set<Coordinate> result = distanceSet(max);
        result.removeAll(distanceSet(min));
        return result;
    }

    public int compareTo(Coordinate coord) {
        if (row != coord.getRow())
            return row - coord.getRow();
        return column - coord.getColumn();
    }

    /** Returns a copy of this Coordinate. **/
    public Coordinate copy() {
        return new Coordinate(column, row);
    }
}
