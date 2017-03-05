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

        get("/move", "application/json", (req, res) -> {
            String response;
            String clientMove = req.queryParams("board");
            if (clientMove == null) {
                res.status(400);
                response = "Error 400. Please send a board parameter (for example: /move?board=+xxxxxxxx).";
            }
            else {
                clientMove = clientMove.replaceAll(" ", String.valueOf(BLANK)); //TODO Use space for blank rather than +
                Brains.MoveResult serverMove = new Brains().makeOptimalMove(clientMove);
                if (serverMove == null) {
                    res.status(400);
                    response = "Error 400. Invalid board.";
                }
                else {
                    res.status(200);
                    res.type("text/plain");
                    response = serverMove.getServerMove();
                }
            }
            return response;
        });

        get("/play", "application/json", (req, res) -> {
            if (mGame == null) {
                mGame = new Game();
            }

            TicTacToeResponse response;
            String clientMove = req.queryParams("board");
            if (clientMove == null) {
                response = new TicTacToeError(mGame.getLastValidBoard());
            }
            else {
                clientMove = clientMove.replaceAll(" ", String.valueOf(BLANK));
                response = respondTo(mGame.playGame(clientMove), mGame.getLastValidBoard());
            }
            res.status(response.status);
            res.type("application/json");
            return response;
        }, json());

        get("/play-again", (req, res) -> {
            mGame.resetGame();
            res.status(200);
            res.type("application/json");
            return new ValidTicTacToeResponse(200, "+++++++++", BLANK);

        }, json());
    }

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }

    private static TicTacToeResponse respondTo(Brains.MoveResult serverMove, String lastMove) {
        if (serverMove == null) {
            return new TicTacToeError(lastMove);
        }
        String newBoard = serverMove.getServerMove().replaceAll("\\" + BLANK, " "); //TODO: And here
        return new ValidTicTacToeResponse(200, newBoard, serverMove.getWinner());
    }

    static class JsonUtil {
        static String toJson(Object object) {
            return new Gson().toJson(object);
        }

        static ResponseTransformer json() {
            return JsonUtil::toJson;
        }
    }

    static class ValidTicTacToeResponse extends TicTacToeResponse {
        private String board;
        private String winner;

        ValidTicTacToeResponse(int status, String board, char winner) {
            this.status = status;
            this.board = board;
            if (winner != BLANK) {
                this.winner = String.valueOf(winner);
            }
        }
    }

    static class TicTacToeError extends TicTacToeResponse {
        private String error;
        private String lastValid;

        TicTacToeError(String lastValid) {
            this.status = 400;
            this.lastValid = lastValid;
            this.error = "Invalid move.";
        }
    }

    static class TicTacToeResponse {
        int status = 0;
    }
}
