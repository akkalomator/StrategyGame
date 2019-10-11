package akkalomator.strategygame.game;

import akkalomator.strategygame.client.GameInfo;
import akkalomator.strategygame.client.GameStatus;
import akkalomator.strategygame.client.MovePhase;
import akkalomator.strategygame.client.PlayerColor;

import java.util.List;

public class GameInfoImpl implements GameInfo {

    private List<String> players;

    private GameStatus gameStatus;

    private int currentMove;

    private MovePhase movePhase;

    private PlayerColor currentPlayerColor;

    private int timeLimit;

    private String lastMove;

    GameInfoImpl() {

    }

    public GameInfoImpl(List<String> players, int timeLimit) {

        if (timeLimit <= 0) {
            throw new IllegalArgumentException("Time limit must be greater than 0, but " + timeLimit + "was passed");
        }

        this.players = players;
        gameStatus = GameStatus.NOT_FINISHED;
        currentMove = 1;
        movePhase = MovePhase.PREPARATION;
        currentPlayerColor = PlayerColor.WHITE;
        this.timeLimit = timeLimit;
        lastMove = "";
    }

    public List<String> getPlayers() {
        return players;
    }

    void setPlayers(List<String> players) {
        this.players = players;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public int getCurrentMove() {
        return currentMove;
    }

    void setCurrentMove(int currentMove) {
        this.currentMove = currentMove;
    }

    public MovePhase getMovePhase() {
        return movePhase;
    }

    void setMovePhase(MovePhase movePhase) {
        this.movePhase = movePhase;
    }

    public PlayerColor getCurrentPlayerColor() {
        return currentPlayerColor;
    }

    void setCurrentPlayerColor(PlayerColor currentPlayerColor) {
        this.currentPlayerColor = currentPlayerColor;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getLastMove() {
        return lastMove;
    }

    void setLastMove(String lastMove) {
        this.lastMove = lastMove;
    }
}
