package akkalomator.strategygame.game;

import akkalomator.strategygame.client.GameConfig;
import akkalomator.strategygame.client.GameInfo;
import akkalomator.strategygame.client.GameStatus;
import akkalomator.strategygame.client.MovePhase;
import akkalomator.strategygame.client.PlayerColor;
import akkalomator.strategygame.game.board.Cell;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private static final boolean[][] CELL_A1 = new boolean[][]{
        {true, false, true},
        {false, false, false},
        {true, false, true}
    };
    private static final boolean[][] CELL_B1 = new boolean[][]{
        {false, false, false},
        {false, false, false},
        {true, false, false}
    };
    private static final boolean[][] CELL_C1 = new boolean[][]{
        {true, false, true},
        {false, false, false},
        {true, false, true}
    };
    private static final boolean[][] CELL_A2 = new boolean[][]{
        {false, false, false},
        {false, false, false},
        {false, false, false}
    };
    private static final boolean[][] CELL_B2 = new boolean[][]{
        {false, false, false},
        {false, false, false},
        {false, false, false}
    };
    private static final boolean[][] CELL_C2 = new boolean[][]{
        {false, false, false},
        {false, false, false},
        {false, false, false}
    };
    private static final boolean[][] CELL_A3 = new boolean[][]{
        {true, false, false},
        {false, false, false},
        {true, true, true}
    };
    private static final boolean[][] CELL_B3 = new boolean[][]{
        {false, false, true},
        {false, true, false},
        {true, false, true}
    };
    private static final boolean[][] CELL_C3 = new boolean[][]{
        {true, false, true},
        {false, false, false},
        {false, false, true}
    };

    @Test
    @Ignore
    void importInitialPositionToJSON() throws FileNotFoundException {
        JSONObject jo = new JSONObject();

        jo.put("date", "12.12.12 12:12");
        JSONArray players = new JSONArray();
        players.add("P1");
        players.add("P2");
        jo.put("players", players);
        jo.put("gameStatus", "NotFinished");
        jo.put("move", 12);
        jo.put("currentPlayer", "white");
        jo.put("phase", "Preparation");
        jo.put("timeLimit", 30);
        jo.put("size", 3);

        JSONArray rows = new JSONArray();
        JSONArray row;
        JSONObject cell;
        JSONArray cellRows;
        JSONArray cellPoints;

        row = new JSONArray();

        cell = new JSONObject();
        cell.put("color", "white");
        cellRows = new JSONArray();
        for (int i = 0; i < 3; i++) {
            cellPoints = new JSONArray();
            for (int j = 0; j < 3; j++) {
                cellPoints.add(CELL_A1[i][j]);
            }
            cellRows.add(cellPoints);
        }
        cell.put("points", cellRows);
        row.add(cell);

        cell = new JSONObject();
        cell.put("color", "white");
        cellRows = new JSONArray();
        for (int i = 0; i < 3; i++) {
            cellPoints = new JSONArray();
            for (int j = 0; j < 3; j++) {
                cellPoints.add(CELL_B1[i][j]);
            }
            cellRows.add(cellPoints);
        }
        cell.put("points", cellRows);
        row.add(cell);

        cell = new JSONObject();
        cell.put("color", "white");
        cellRows = new JSONArray();
        for (int i = 0; i < 3; i++) {
            cellPoints = new JSONArray();
            for (int j = 0; j < 3; j++) {
                cellPoints.add(CELL_C1[i][j]);
            }
            cellRows.add(cellPoints);
        }
        cell.put("points", cellRows);
        row.add(cell);

        rows.add(row);

        // more row to rows

        jo.put("board", rows);


        PrintWriter pw = new PrintWriter("src/test/resources/akkalomator.strategygame.game/InitialPosition.json");
        pw.write(jo.toJSONString());

        pw.flush();
        pw.close();
    }

    @Test
    void startGame_ThrowsWhenGameHasBeenAlreadyStarted() throws IOException {
        GameConfig config = new GameConfig(8, 30, List.of("P1", "P2"), "");

        Game game = new Game();
        game.startGame(config);

        assertThrows(IllegalStateException.class, () -> game.startGame(config));
    }

    @Test
    void startGame_InitialisesFromConfig() throws IOException {

        GameConfig config = new GameConfig(8, 30, List.of("P1", "P2"), "");

        Game game = new Game();
        game.startGame(config);

        GameInfo info = game.getInfo();
        assertEquals(List.of("P1", "P2"), info.getPlayers());
        assertEquals(GameStatus.NOT_FINISHED, info.getGameStatus());
        assertEquals(1, info.getCurrentMove());
        assertEquals(MovePhase.PREPARATION, info.getMovePhase());
        assertEquals(PlayerColor.WHITE, info.getCurrentPlayerColor());
        assertEquals(30, info.getTimeLimit());
        assertEquals("", info.getLastMove());
        assertEquals(8, game.board.getSize());

        for (int i = 1; i <= 2; i++) {
            for (char c = 'A'; c <= 'H'; c++) {
                Cell currentCell = game.board.getCell(Character.toString(c) + i);
                assertEquals(List.of(2, 2, 2, 2, 2, 2), currentCell.getLines());
                assertEquals(PlayerColor.WHITE, currentCell.controlledBy());
            }
        }

        for (int i = 3; i <= 6; i++) {
            for (char c = 'A'; c <= 'H'; c++) {
                Cell currentCell = game.board.getCell(Character.toString(c) + i);
                assertEquals(List.of(0, 0, 0, 0, 0, 0), currentCell.getLines());
                assertEquals(PlayerColor.NONE, currentCell.controlledBy());
            }
        }

        for (int i = 7; i <= 8; i++) {
            for (char c = 'A'; c <= 'H'; c++) {
                Cell currentCell = game.board.getCell(Character.toString(c) + i);
                assertEquals(List.of(2, 2, 2, 2, 2, 2), currentCell.getLines());
                assertEquals(PlayerColor.BLACK, currentCell.controlledBy());
            }
        }

        assertTrue(game.moveEndedNotifier.isTimerStarted());
    }

    @Test
    void startGame_InitialisesFromConfigWithGivenPositions() throws IOException {
        GameConfig config = new GameConfig(
            8,
            30,
            List.of("P1", "P2"),
            "src/test/resources/akkalomator.strategygame.game/InitialPosition.json"
        );

        Game game = new Game();
        game.startGame(config);

        GameInfo info = game.getInfo();
        assertEquals(List.of("P1", "P2"), info.getPlayers());
        assertEquals(GameStatus.NOT_FINISHED, info.getGameStatus());
        assertEquals(12, info.getCurrentMove());
        assertEquals(MovePhase.PREPARATION, info.getMovePhase());
        assertEquals(PlayerColor.WHITE, info.getCurrentPlayerColor());
        assertEquals(30, info.getTimeLimit());
        assertEquals("", info.getLastMove());
        assertEquals(3, game.board.getSize());

        assertEquals(CELL_A1, game.board.getCell("A1").getPoints());
        assertEquals(PlayerColor.WHITE, game.board.getCell("A1").controlledBy());
        assertEquals(CELL_B1, game.board.getCell("B1").getLines());
        assertEquals(PlayerColor.WHITE, game.board.getCell("B1").controlledBy());
        assertEquals(CELL_C1, game.board.getCell("C1").getLines());
        assertEquals(PlayerColor.WHITE, game.board.getCell("C1").controlledBy());
        assertEquals(CELL_A2, game.board.getCell("A2").getLines());
        assertEquals(PlayerColor.NONE, game.board.getCell("A2").controlledBy());
        assertEquals(CELL_B2, game.board.getCell("B2").getLines());
        assertEquals(PlayerColor.NONE, game.board.getCell("B2").controlledBy());
        assertEquals(CELL_C2, game.board.getCell("C2").getLines());
        assertEquals(PlayerColor.NONE, game.board.getCell("C2").controlledBy());
        assertEquals(CELL_A3, game.board.getCell("A3").getLines());
        assertEquals(PlayerColor.BLACK, game.board.getCell("A3").controlledBy());
        assertEquals(CELL_B3, game.board.getCell("B3").getLines());
        assertEquals(PlayerColor.BLACK, game.board.getCell("B3").controlledBy());
        assertEquals(CELL_C3, game.board.getCell("C3").getLines());
        assertEquals(PlayerColor.BLACK, game.board.getCell("C3").controlledBy());

        assertFalse(game.moveEndedNotifier.isTimerStarted());
    }

    @Test
    void getPossibleMoves_ThrowsWhenGameHasNotBeenStarted() {
        Game game = new Game();

        assertThrows(IllegalStateException.class, () -> game.getPossibleMoves("A1"));
    }

    @Test
    void getPossibleMoves_ThrowsOnInvalidInput() throws IOException {

        GameConfig config = new GameConfig(8, 30, List.of("P1", "P2"), "");
        Game game = new Game();
        game.startGame(config);

        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves(""));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("1A_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("AB_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("12_"));
        assertDoesNotThrow(() -> game.getPossibleMoves("A1_"));

        game.changeMovePhase(MovePhase.ACTION);
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves(""));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;A1"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;1A_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;AB_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;12_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345A1_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_0123A5"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_0123456;"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_0123456"));
        assertDoesNotThrow(() -> game.getPossibleMoves("A1_012345;A2_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;A2_012345-A1"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;A2_012345-1A_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;A2_012345-AB_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;A2_012345-12_"));
        assertDoesNotThrow(() -> game.getPossibleMoves("A1_012345;A2_012345-C3_012345"));
    }

    @Test
    void getPossibleMoves_ThrowsOnCellOutsideTheBoard() throws IOException {

        GameConfig config = new GameConfig(8, 30, List.of("P1", "P2"), "");
        Game game = new Game();
        game.startGame(config);

        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A9_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("I1_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("I9_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A0_"));

        game.changeMovePhase(MovePhase.ACTION);
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;A9_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;I1_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;I9_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;A0_"));

        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;A0_012345-A9_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;A0_012345-I1_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;A0_012345-I9_"));
        assertThrows(IllegalArgumentException.class, () -> game.getPossibleMoves("A1_012345;A0_012345-A0_"));
    }

    @Test
    void getPossibleMoves_ReturnsEmptyWhenAskedFromOpponentsCell() throws IOException {

        GameConfig config = new GameConfig(8, 30, List.of("P1", "P2"), "");
        Game game = new Game();
        game.startGame(config);

        assertEquals(List.of(), game.getPossibleMoves("A8_"));

        game.changeMovePhase(MovePhase.ACTION);
        assertEquals(List.of(), game.getPossibleMoves("A1_012345;A8_"));
    }

    @Test
    void getPossibleMoves_ReturnsEmptyListWhenAskedFromEmptyCell() throws IOException {

        GameConfig config = new GameConfig(8, 30, List.of("P1", "P2"), "");
        Game game = new Game();
        game.startGame(config);

        assertEquals(List.of(), game.getPossibleMoves("A3_"));

        game.changeMovePhase(MovePhase.ACTION);
        assertEquals(List.of(), game.getPossibleMoves("A1_012345;A3_"));
    }

    @Test
    void getPossibleMoves_WorksCorrectly() throws IOException {

        GameConfig config = new GameConfig(8, 30, List.of("P1", "P2"), "src/test/resources/akkalomator.strategygame.game/InitialPosition.json");
        Game game = new Game();
        game.startGame(config);

        assertEquals(
            List.of(
                "A1_131212;",
                "A1_132121;",
                "A1_311122;",
                "A1_312211;",
                "A1_122131;",
                "A1_211132;",
                "A1_121312;",
                "A1_212311;",
                "A1_122213;",
                "A1_212123;",
                "A1_123122;",
                "A1_213212;",
                "A1_041111;",
                "A1_401111;",
                "A1_111041;",
                "A1_111401;",
                "A1_114110;",
                "A1_110114;"
            ),
            game.getPossibleMoves("A1_")
        );
        assertEquals(
            List.of(
                "B1_011100;",
                "B1_100101;",
                "B1_101010;"
            ),
            game.getPossibleMoves("B1_")
        );

        game.changeMovePhase(MovePhase.ACTION);
        assertEquals(
            List.of(
                "A1_131212;C1_121212",
                "A1_131212;C1_122121",
                "A1_131212;C1_212211",
                "A1_131212;C1_211122",
                "A1_131212;C1_111021",
                "A1_131212;C1_111201",
                "A1_131212;C1_021111",
                "A1_131212;C1_201111",
                "A1_131212;C1_112110",
                "A1_131212;C1_110112"
            ),
            game.getPossibleMoves("A1_131212;C1_")
        );

        assertEquals(
            List.of("A1_131212;B1_000000"),
            game.getPossibleMoves("A1_131212;B1_")
        );

        assertEquals(
            List.of("A1_131212;C1_021111-B1_031111"),
            game.getPossibleMoves("A1_131212;C1_021111-")
        );

        assertEquals(
            List.of(
                "A1_131212;C1_111021-C2_111201",
                "A1_131212;C1_111021-C3_000000"
            ),
            game.getPossibleMoves("A1_131212;C1_111021-")
        );

        assertEquals(
            List.of(
                "A1_131212;B1_000000-B2_010011",
                "A1_131212;B1_000000-B2_101010",
                "A1_131212;B1_000000-B3_111201",
                "A1_131212;B1_000000-B3_021111",
                "A1_131212;B1_000000-B3_110112"
            ),
            game.getPossibleMoves("A1_131212;B1_00000-")
        );
    }

    @Test
    void isPossible_WorksCorrectly() throws IOException {

        GameConfig config = new GameConfig(8, 30, List.of("P1", "P2"), "src/test/resources/akkalomator.strategygame.game/InitialPosition.json");
        Game game = new Game();
        game.startGame(config);

        assertTrue(game.isPossible("A1_131212;C1_111021-C2_111201"));
        assertTrue(game.isPossible("A1_131212;C1_111021-C3_000000"));

        assertFalse(game.isPossible("A1_131212;C1_111021-C2_111021"));
    }

    @Test
    void makeMove_ThrowsWhenMoveIsInvalid() throws IOException {

        GameConfig config = new GameConfig(8, 30, List.of("P1", "P2"), "src/test/resources/akkalomator.strategygame.game/InitialPosition.json");
        Game game = new Game();
        game.startGame(config);

        assertThrows(IllegalArgumentException.class, () -> game.makeMove("A1_131212;C1_111021-C2_111021"));
    }

    @Test
    void makeMove_WorksCorrectly() throws IOException {

        GameConfig config = new GameConfig(8, 30, List.of("P1", "P2"), "src/test/resources/akkalomator.strategygame.game/InitialPosition.json");
        Game game = new Game();
        game.startGame(config);

        game.makeMove("A1_131212;C1_111021-C3_000000");

        assertEquals(
            List.of(1, 3, 1, 2, 1, 2),
            game.board.getCell("A1").getLines()
        );
        assertEquals(PlayerColor.WHITE, game.board.getCell("A1").controlledBy());

        assertEquals(
            List.of(0, 1, 0, 0, 1, 1),
            game.board.getCell("B1").getLines()
        );
        assertEquals(PlayerColor.WHITE, game.board.getCell("A1").controlledBy());

        assertEquals(
            List.of(1, 1, 1, 0, 2, 1),
            game.board.getCell("C1").getLines()
        );
        assertEquals(PlayerColor.WHITE, game.board.getCell("A1").controlledBy());

        assertEquals(
            List.of(0, 0, 0, 0, 0, 0),
            game.board.getCell("A2").getLines()
        );
        assertEquals(PlayerColor.NONE, game.board.getCell("A1").controlledBy());

        assertEquals(
            List.of(0, 0, 0, 0, 0, 0),
            game.board.getCell("B2").getLines()
        );
        assertEquals(PlayerColor.NONE, game.board.getCell("A1").controlledBy());

        assertEquals(
            List.of(0, 0, 0, 0, 0, 0),
            game.board.getCell("C2").getLines()
        );
        assertEquals(PlayerColor.NONE, game.board.getCell("A1").controlledBy());

        assertEquals(
            List.of(1, 3, 1, 2, 1, 2),
            game.board.getCell("A3").getLines()
        );
        assertEquals(PlayerColor.BLACK, game.board.getCell("A1").controlledBy());

        assertEquals(
            List.of(1, 2, 2, 2, 1, 3),
            game.board.getCell("B3").getLines()
        );
        assertEquals(PlayerColor.BLACK, game.board.getCell("A1").controlledBy());

        assertEquals(
            List.of(0, 0, 0, 0, 0, 0),
            game.board.getCell("C1").getLines()
        );
        assertEquals(PlayerColor.NONE, game.board.getCell("A1").controlledBy());
    }

    @Test
    void undoMove_WorksCorrectly() throws IOException {

        GameConfig config = new GameConfig(8, 30, List.of("P1", "P2"), "src/test/resources/akkalomator.strategygame.game/InitialPosition.json");
        Game game = new Game();
        game.startGame(config);

        game.makeMove("A1_131212;C1_111021-C3_000000");
        game.undoMove();

        assertEquals(CELL_A1, game.board.getCell("A1").getLines());
        assertEquals(PlayerColor.WHITE, game.board.getCell("A1").controlledBy());
        assertEquals(CELL_B1, game.board.getCell("B1").getLines());
        assertEquals(PlayerColor.WHITE, game.board.getCell("B1").controlledBy());
        assertEquals(CELL_C1, game.board.getCell("C1").getLines());
        assertEquals(PlayerColor.WHITE, game.board.getCell("C1").controlledBy());
        assertEquals(CELL_A2, game.board.getCell("A2").getLines());
        assertEquals(PlayerColor.NONE, game.board.getCell("A2").controlledBy());
        assertEquals(CELL_B2, game.board.getCell("B2").getLines());
        assertEquals(PlayerColor.NONE, game.board.getCell("B2").controlledBy());
        assertEquals(CELL_C2, game.board.getCell("C2").getLines());
        assertEquals(PlayerColor.NONE, game.board.getCell("C2").controlledBy());
        assertEquals(CELL_A3, game.board.getCell("A3").getLines());
        assertEquals(PlayerColor.BLACK, game.board.getCell("A3").controlledBy());
        assertEquals(CELL_B3, game.board.getCell("B3").getLines());
        assertEquals(PlayerColor.BLACK, game.board.getCell("B3").controlledBy());
        assertEquals(CELL_C3, game.board.getCell("C3").getLines());
        assertEquals(PlayerColor.BLACK, game.board.getCell("C3").controlledBy());
    }
}