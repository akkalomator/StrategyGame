package akkalomator.strategygame.game;

import akkalomator.strategygame.client.GameStatus;
import akkalomator.strategygame.client.MovePhase;
import akkalomator.strategygame.client.PlayerColor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameInfoImplTest {

    @Test
    void constructor_throwsOnTimeLimitLessOrEqualToZero() {
        assertThrows(IllegalArgumentException.class, () -> new GameInfoImpl(List.of("P1", "P2"), 0));
        assertThrows(IllegalArgumentException.class, () -> new GameInfoImpl(List.of("P1", "P2"), -1));
    }

    @Test
    void constructor_worksCorrectly() {

        GameInfoImpl info = new GameInfoImpl(List.of("P1", "P2"), 30);

        assertEquals(List.of("P1", "P2"), info.getPlayers());
        assertEquals(1, info.getCurrentMove());
        assertEquals(MovePhase.PREPARATION, info.getMovePhase());
        assertEquals(GameStatus.NOT_FINISHED, info.getGameStatus());
        assertEquals(PlayerColor.WHITE, info.getCurrentPlayerColor());
        assertEquals(30, info.getTimeLimit());
        assertEquals("", info.getLastMove());
    }
}