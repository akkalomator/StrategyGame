package akkalomator.strategygame.client;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameConfigTest {

    @Test
    void constructor_throwsOnTimeBoardSizeLessThanFour() {
        assertThrows(IllegalArgumentException.class, () -> new GameConfig(-1, 5, List.of("P1", "P2"), ""));
        assertThrows(IllegalArgumentException.class, () -> new GameConfig(3, 5, List.of("P1", "P2"), ""));
    }

    @Test
    void constructor_throwsOnTimeLimitLessOrEqualToZero() {
        assertThrows(IllegalArgumentException.class, () -> new GameConfig(8, 0, List.of("P1", "P2"), ""));
        assertThrows(IllegalArgumentException.class, () -> new GameConfig(8, -1, List.of("P1", "P2"), ""));
    }
}