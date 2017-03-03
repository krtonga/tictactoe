package com.tonga;

import static com.tonga.Board.BLANK;
import static com.tonga.Board.CLIENT;
import static com.tonga.Board.SERVER;
import static com.tonga.Game.TIE;

/**
 * This contains the logic necessary to make optimal moves.
 */
public class Brains {
    /**
     * This method will take a string representation of a TicTacToe board,
     * validate that it is a potentially viable board, make an optimal move, and
     * return an object with a string representation of the updated board, and
     * a char representing the winner of the game.
     *
     * If the client sends an invalid input, this method will return null.
     * If no winner is yet found, the blank char will be returned.
     */
    public MoveResult makeOptimalMove(String board) {
        if (Board.isValidBoard(board)) {
            return makeOptimalMove(new Board(board));
        }
        return null;
    }

    MoveResult makeOptimalMove(Board clientBoard) {
        Board.SymbolCountResult result = clientBoard.getMoveCounts();
        if (result.isValidMove()) {
            String serverMove;
            switch (result.totalMoves) {
                case 0:
                    serverMove = makeFirstMove(clientBoard);
                    break;
                case 1:
                    serverMove = makeSecondMove(clientBoard);
                    break;
                case 2:
                    serverMove = makeThirdMove(clientBoard);
                    break;
                case 3:
                    serverMove = makeForthMove(clientBoard);
                    break;
                default:
                    char winner = getWinner(clientBoard, CLIENT);
                    if (winner == BLANK) {
                        serverMove = makeSubsequentMoves(clientBoard);
                    } else {
                        return new MoveResult(clientBoard.toString(), winner);
                    }
            }
            if (serverMove == null) {
                serverMove = clientBoard.makeDefaultMove();
            }
            return new MoveResult(serverMove, getWinner(serverMove, SERVER));
        }
        return null;
    }

    // server is making first move
    private String makeFirstMove(Board board) {
        return board.makeCornerMove();
    }

    // client made first move
    private String makeSecondMove(Board board) {
        int x = board.getAnX();
        if (board.isInCorner(CLIENT)) {
            return board.makeCenterMove();
        }
        if (board.isInCenter(CLIENT)) {
            return board.makeMove(board.getOppositeCorner(x));
        }
        if (board.isOnEdge(CLIENT)) {
            return board.moveAdjacentCorner(x);
        }
        return null;
    }

    private String makeThirdMove(Board board) {
        if (board.isInCenter(CLIENT)) {
            return board.moveAdjacentCorner(board.getAnO());
        }
        else if (board.isOnEdge(CLIENT)) {
            return board.makeCenterMove();
        }
        else if (board.isInCorner(CLIENT)) {
            return board.makeCornerMove();
        }
        return null;
    }

    private String makeForthMove(Board board) {
        String serverMove;
        int blocking = board.getBlockingMove(CLIENT);
        serverMove = board.makeMove(blocking);

        if (serverMove == null) {
            if (!board.isInCenter(Board.SERVER)) {
                serverMove = board.makeCenterMove();
            }
        }
        return serverMove;
    }

    private String makeSubsequentMoves(Board board) {
        int winningMove = board.getBlockingMove(Board.SERVER);
        if (winningMove != -1) {
            return board.makeMove(winningMove);
        }
        int blockingMove = board.getBlockingMove(CLIENT);
        if (blockingMove != -1) {
            return board.makeMove(blockingMove);
        }
        if (board.isOnEdge(CLIENT) && board.isInCorner(Board.SERVER) && board.isInCenter(Board.SERVER)) {
            return board.makeCornerMove();
        }
        return board.makeDefaultMove();
    }

    private char getWinner(String board, char symbol) {
        return getWinner(new Board(board), symbol);
    }

    private char getWinner(Board board, char symbol) {
        if (board.hasThreeInARow(symbol)) {
            return symbol;
        }
        if (!board.hasFreeSpaces()) {
            return TIE;
        }
        return BLANK;
    }

    public static class MoveResult {
        char winner;
        String serverMove;

        MoveResult(String serverMove, char winner) {
            this.winner = winner;
            this.serverMove = serverMove;
        }

        public char getWinner() {
            return winner;
        }

        public String getServerMove() {
            return serverMove;
        }
    }
}
