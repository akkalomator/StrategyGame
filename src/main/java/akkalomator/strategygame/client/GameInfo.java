package akkalomator.strategygame.client;

import java.util.List;

public interface GameInfo {

    List<String> getPlayers();

    GameStatus getGameStatus();

    int getCurrentMove();

    MovePhase getMovePhase();

    PlayerColor getCurrentPlayerColor();

    int getTimeLimit();

    String getLastMove();
}
