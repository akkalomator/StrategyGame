package akkalomator.strategygame.game;

import akkalomator.strategygame.client.GameConfig;
import akkalomator.strategygame.client.GameInfo;
import akkalomator.strategygame.client.GameStatus;
import akkalomator.strategygame.client.MoveEndedObserver;
import akkalomator.strategygame.client.MovePhase;
import akkalomator.strategygame.client.PlayerColor;
import akkalomator.strategygame.game.board.Board;
import akkalomator.strategygame.game.board.BoardImpl;
import akkalomator.strategygame.game.utils.MoveEndedNotifier;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Game {

    private GameInfoImpl info;

    Board board;

    MoveEndedNotifier moveEndedNotifier;
    private boolean isStarted;

    public Game() {
        moveEndedNotifier = new MoveEndedNotifier();
        isStarted = false;
    }

    void attachMoveEndedObserver(MoveEndedObserver observer) {
        moveEndedNotifier.attachObserver(observer);
    }

    void startGame(GameConfig config) throws IOException {

        if (isStarted) {
            throw new IllegalStateException("Game has been already started");
        }
        isStarted = true;


        if (config.getInitialPositionFilePath() != null && !config.getInitialPositionFilePath().isEmpty()) {

            try {
                JSONObject jo = (JSONObject) new JSONParser().parse(new FileReader(config.getInitialPositionFilePath()));

                passInfoFromJSONToGameInfo(jo);

                board = new BoardImpl(((Long) jo.get("size")).intValue());

                JSONArray boardDescription = (JSONArray) jo.get("board");
                for (int i = 0; i < board.getSize(); i++) {
                    JSONArray row = (JSONArray) boardDescription.get(i);
                    for (int j = 0; j < board.getSize(); j++) {
                        JSONObject cell = (JSONObject) row.get(j);
                        List<Boolean> points;
                        switch ((String) cell.get("color")) {
                            case "white":
                                points = (List<Boolean>) cell.get("points");
                                board
                                    .getCell(Character.toString('A' + j) + (i + 1))
                                    .updateCell(
                                        new boolean[][] {
                                            {points.get(0), points.get(1), points.get(2)},
                                            {points.get(3), points.get(4), points.get(5)},
                                            {points.get(6), points.get(7), points.get(8)}
                                        },
                                        PlayerColor.WHITE
                                    );
                                break;
                            case "black":
                                points = (List<Boolean>) cell.get("points");
                                board
                                    .getCell(Character.toString('A' + j) + (i + 1))
                                    .updateCell(
                                        new boolean[][] {
                                            {points.get(0), points.get(1), points.get(2)},
                                            {points.get(3), points.get(4), points.get(5)},
                                            {points.get(6), points.get(7), points.get(8)}
                                        },
                                        PlayerColor.BLACK
                                    );
                                break;
                            case "none":
                                board
                                    .getCell(Character.toString('A' + j) + (i + 1))
                                    .updateCell(
                                        new boolean[][] {
                                            {false, false, false},
                                            {false, false, false},
                                            {false, false, false}
                                        },
                                        PlayerColor.NONE
                                    );
                                break;
                            default:
                                throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN);
                        }
                    }
                }

            } catch (ParseException e) {
                throw new RuntimeException("Cannot parse initial position. File might be corrupted", e);
            }

            return;
        }
        info = new GameInfoImpl(config.getPlayersNames(), config.getTimeLimit());
        board = new BoardImpl(8);

        for (int i = 1; i <= 2; i++) {
            for (char c = 'A'; c < 'A' + board.getSize(); c++) {
                board.getCell(Character.toString(c) + i).updateCell(
                    new boolean[][]{
                        {true, false, true},
                        {false, false, false},
                        {true, false, true}
                    },
                    PlayerColor.WHITE
                );
            }
        }

        for (int i = 3; i <= board.getSize() - 2; i++) {
            for (char c = 'A'; c < 'A' + board.getSize(); c++) {
                board.getCell(Character.toString(c) + i).updateCell(
                    new boolean[][]{
                        {false, false, false},
                        {false, false, false},
                        {false, false, false}
                    },
                    PlayerColor.NONE
                );
            }
        }

        for (int i = board.getSize() - 1; i <= board.getSize(); i++) {
            for (char c = 'A'; c < 'A' + board.getSize(); c++) {
                board.getCell(Character.toString(c) + i).updateCell(
                    new boolean[][]{
                        {true, false, true},
                        {false, false, false},
                        {true, false, true}
                    },
                    PlayerColor.BLACK
                );
            }
        }

        moveEndedNotifier.notifyAfter(info.getTimeLimit());
    }

    private void passInfoFromJSONToGameInfo(JSONObject jo) throws ParseException {
        info = new GameInfoImpl();
        info.setPlayers((List<String>) jo.get("players"));

        String gameStatus = (String) jo.get("gameStatus");
        switch (gameStatus) {
            case "NotFinished":
                info.setGameStatus(GameStatus.NOT_FINISHED);
                break;
            case "WhiteWon":
                info.setGameStatus(GameStatus.WHITE_WON);
                break;
            case "BlackWon":
                info.setGameStatus(GameStatus.BLACK_WON);
                break;
            default:
                throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN);
        }

        info.setCurrentMove(((Long) jo.get("move")).intValue());

        String movePhase = (String) jo.get("phase");
        switch (movePhase) {
            case "Preparation":
                info.setMovePhase(MovePhase.PREPARATION);
                break;
            case "Action":
                info.setMovePhase(MovePhase.ACTION);
                break;
            default:
                throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN);
        }

        String currentPlayerColor = (String) jo.get("currentPlayer");
        switch (currentPlayerColor) {
            case "white":
                info.setCurrentPlayerColor(PlayerColor.WHITE);
                break;
            case "black":
                info.setCurrentPlayerColor(PlayerColor.BLACK);
            default:
                throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN);
        }

        info.setTimeLimit(((Long) jo.get("timeLimit")).intValue());
        info.setLastMove("");
    }

    List<String> getPossibleMoves(String moveBegining) {
        return null;
    }

    boolean isPossible(String move) {
        return false;
    }

    void makeMove(String move) {

    }

    void undoMove() {

    }

    public GameInfo getInfo() {
        return info;
    }

    void changeMovePhase(MovePhase phase) {
        info.setMovePhase(phase);
    }
}
