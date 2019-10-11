package akkalomator.strategygame.game.board;

import akkalomator.strategygame.client.PlayerColor;

import java.util.List;

public class Cell {

    private boolean[][] points;

    private PlayerColor controlledBy;
    private List<Integer> lines;

    private boolean areLinesCalculated;

    public Cell(boolean[][] points, PlayerColor controlledBy) {
        this.points = points;
        this.controlledBy = controlledBy;
        areLinesCalculated = false;
    }

    public boolean[][] getPoints() {
        return points;
    }

    public List<Integer> getLines() {
        if (!areLinesCalculated) {
            lines = List.of(
                getIntFromBoolean(points[0][0]) + getIntFromBoolean(points[0][1]) + getIntFromBoolean(points[0][2]),
                getIntFromBoolean(points[2][0]) + getIntFromBoolean(points[2][1]) + getIntFromBoolean(points[2][2]),
                getIntFromBoolean(points[0][0]) + getIntFromBoolean(points[1][1]) + getIntFromBoolean(points[2][2]),
                getIntFromBoolean(points[0][2]) + getIntFromBoolean(points[1][2]) + getIntFromBoolean(points[2][2]),
                getIntFromBoolean(points[0][0]) + getIntFromBoolean(points[1][0]) + getIntFromBoolean(points[2][0]),
                getIntFromBoolean(points[2][0]) + getIntFromBoolean(points[1][1]) + getIntFromBoolean(points[2][0])
            );
        }


        return lines;
    }

    public void updateCell(boolean[][] points, PlayerColor controlledBy) {
        this.points = points;
        this.controlledBy = controlledBy;
    }

    public PlayerColor controlledBy() {
        return controlledBy;
    }

    private int getIntFromBoolean(boolean b) {
        return b ? 1 : 0;
    }
}
