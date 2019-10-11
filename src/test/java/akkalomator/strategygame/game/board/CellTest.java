package akkalomator.strategygame.game.board;

import akkalomator.strategygame.client.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void getLines_worksCorrectly() {

        Cell cell = new Cell(new boolean[][]{{true, false, true}, {false, false, false}, {true, false, true}}, PlayerColor.WHITE);

        assertEquals(List.of(2, 2, 2, 2, 2, 2), cell.getLines());
    }

}