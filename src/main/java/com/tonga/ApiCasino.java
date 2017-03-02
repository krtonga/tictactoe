package com.tonga;

import com.google.gson.Gson;
import spark.ResponseTransformer;

import static com.tonga.Board.BLANK;
import static com.tonga.ApiCasino.JsonUtil.json;
import static spark.Spark.*;

/**
 * This can be used to create a server endpoint.
 *
 */
public class ApiCasino {
    private static Game mGame;

    public static void main(String[] args) {
        port(getHerokuAssignedPort());

        get("/tictactoe", "application/json", (req, res) -> {
            if (mGame == null) {
                mGame = new Game();
            }

            String board = req.queryParams("board");
            if (board != null) {
                board = board.replaceAll(" ", String.valueOf(BLANK)); //TODO Use space for blank rather than +
                String serverMove = mGame.sendBoard(board);
                if (serverMove == null) {
                    res.status(400);
                    return new TicTacToeError();
                }
                res.status(200);
                serverMove = serverMove.replaceAll("\\" + BLANK, " ");
                return new TicTacToeResponse(200, serverMove, mGame.getWinner());
            }
            return new TicTacToeError();
        }, json());

        get("/tictactoe/new", (req, res) -> {
            mGame = new Game();
            return new TicTacToeResponse(200, "+++++++++", BLANK);
        }, json());

        after((request, response) -> response.type("application/json"));
    }

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

    static class JsonUtil {
        static String toJson(Object object) {
            return new Gson().toJson(object);
        }

        static ResponseTransformer json() {
            return JsonUtil::toJson;
        }
    }

    static class TicTacToeResponse {
        private int status;
        private String board;
        private String winner;

        TicTacToeResponse(int status, String board, char winner) {
            this.status = status;
            this.board = board;
            if (winner != BLANK) {
                this.winner = String.valueOf(winner);
            }
        }
    }

    static class TicTacToeError {
        private int status;
        private String error;
        private String lastValid;

        TicTacToeError() {
            this.status = 400;
            this.lastValid = mGame.getLastValidBoard();
            if (lastValid == null) {
                this.error = "Please send a valid start board. For example: +++++++++";
            }
            this.error = "Invalid move.";
        }
    }
}
