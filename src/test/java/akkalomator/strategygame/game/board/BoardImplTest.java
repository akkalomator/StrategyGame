package akkalomator.strategygame.game.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardImplTest {

    @Test
    void getCell_ThrowsOnIndexOutOfBounds() {

        BoardImpl board = new BoardImpl(8);
        assertThrows(IllegalArgumentException.class, () -> board.getCell("A0"));
        assertThrows(IllegalArgumentException.class, () -> board.getCell("A9"));
        assertThrows(IllegalArgumentException.class, () -> board.getCell("I1"));
        assertThrows(IllegalArgumentException.class, () -> board.getCell("I0"));
        assertThrows(IllegalArgumentException.class, () -> board.getCell("I9"));
    }
}