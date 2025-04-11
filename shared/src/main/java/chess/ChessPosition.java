package chess;

import exception.DataAccessException;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    public boolean isValidPosition() {
        return (row <= 8 && row >= 1) && (col <= 8 && col >= 1);
    }

    public String toCoordString() {
        StringBuilder sb = new StringBuilder();
        if (col == 1) {
            sb.append("a");
        } else if (col == 2) {
            sb.append("b");
        } else if (col == 3) {
            sb.append("c");
        } else if (col == 4) {
            sb.append("d");
        } else if (col == 5) {
            sb.append("e");
        } else if (col == 6) {
            sb.append("f");
        } else if (col == 7) {
            sb.append("g");
        } else {
            sb.append("h");
        }
        sb.append(row);
        return sb.toString();
    }

    @Override
    public String toString() {
        return "ChessPosition{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessPosition that)) {
            return false;
        }
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }


}
