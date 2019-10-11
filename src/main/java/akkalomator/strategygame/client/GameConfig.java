package akkalomator.strategygame.client;

import java.util.List;

public class GameConfig {

    private int boardSize;

    private int timeLimit;

    private List<String> playersNames;

    private String initialPositionFilePath;

    public GameConfig(int boardSize, int timeLimit, List<String> playersNames, String initialPositionFilePath) {
        // TODO: Split into two: with initial position and with border size
        if (boardSize < 4 && (initialPositionFilePath == null || initialPositionFilePath.isEmpty())) {
            throw new IllegalArgumentException("Board must be greater than 3, but " + boardSize + "was passed");
        }

        if (timeLimit <= 0) {
            throw new IllegalArgumentException("Time limit must be greater than 0, but " + timeLimit + "was passed");
        }

        this.boardSize = boardSize;
        this.timeLimit = timeLimit;
        this.playersNames = playersNames;
        this.initialPositionFilePath = initialPositionFilePath;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public List<String> getPlayersNames() {
        return playersNames;
    }

    public String getInitialPositionFilePath() {
        return initialPositionFilePath;
    }

    public int getBoardSize() {
        return boardSize;
    }
}
