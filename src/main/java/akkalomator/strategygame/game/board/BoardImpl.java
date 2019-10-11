package akkalomator.strategygame.game.board;

import akkalomator.strategygame.client.PlayerColor;

import java.util.HashMap;
import java.util.Map;

public class BoardImpl implements Board {

    private int size;

    private Map<String, Cell> cells;

    public BoardImpl(int size) {
        this.size = size;

        cells = new HashMap<>();

        for (int i = 1; i <= 2; i++) {
            for (char c = 'A'; c < 'A' + size; c++) {
                cells.put(
                    String.valueOf(c) + i,
                    new Cell(
                        new boolean[][]{
                            {true, false, true},
                            {false, false, false},
                            {true, false, true}
                        },
                        PlayerColor.WHITE)
                );
            }
        }

        for (int i = 3; i <= size - 2; i++) {
            for (char c = 'A'; c < 'A' + size; c++) {
                cells.put(String.valueOf(c) + i, new Cell(
                    new boolean[][]{
                        {false, false, false},
                        {false, false, false},
                        {false, false, false}
                    },
                    PlayerColor.NONE)
                );
            }
        }

        for (int i = size - 1; i <= size; i++) {
            for (char c = 'A'; c < 'A' + size; c++) {
                cells.put(String.valueOf(c) + i, new Cell(
                    new boolean[][]{
                        {true, false, true},
                        {false, false, false},
                        {true, false, true}
                    },
                    PlayerColor.BLACK)
                );
            }
        }
    }

    @Override
    public Cell getCell(String coordinates) {
        if (!cells.containsKey(coordinates)) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        return cells.get(coordinates);
    }

    @Override
    public int getSize() {
        return size;
    }
}
