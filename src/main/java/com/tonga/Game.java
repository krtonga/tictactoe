package com.tonga;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * This class represents a TicTacToe game.
 */
public class Game {
    public static final char TIE = 't';

    private Board mBoard;
    private int updateCount;
    private char winner;

    /**
     * This method will take a string representation of a TicTacToe board,
     * validate that it is a playable board, make an optimal move, and
     * return a string representation of the updated board.
     *
     * If a winner is found, board will be reset. This method will return
     * the winning board. Last winner is kept until a new board is provided
     * by the client.
     *
     * If the client sends an invalid input, this method will return null.
     */
    public String sendBoard(String board) {
        if (mBoard == null) {
            winner = Board.BLANK; // reset for round of game
            if (Board.isValidFirst(board)) {
                mBoard = new Board(board);
                winner = Board.BLANK;
                return prepareResponse(makeFirstMove());
            }
        }
        else if (mBoard.update(board)) {
            updateCount++;
            if (updateCount == 2) {
                return prepareResponse(makeThirdMove());
            }
            if (updateCount == 3) {
                return prepareResponse(makeForthMove());
            }
            else {
                boolean winOrTie = checkForWinner(Board.CLIENT);
                if (winOrTie) {
                    resetGame();
                    return board;
                }
                return prepareResponse(makeSubsequentMoves());
            }
        }
        return null; // invalid board sent
    }

    public char getWinner() {
        return winner;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    String getLastValidBoard() {
        if (mBoard == null) {
            return null;
        }
        return mBoard.toString();
    }

    private boolean checkForWinner(char symbol) {
        if (!mBoard.hasFreeSpaces()) {
            winner = TIE;
        }
        if (mBoard.hasThreeInARow(symbol)) {
            winner = symbol;
        }
        return winner != Board.BLANK;
    }

    private void resetGame() {
        mBoard = null;
        updateCount = 0;
    }

    private String prepareResponse(String logicalResponse) {
        if (logicalResponse == null) {
            logicalResponse = mBoard.makeDefaultMove();
        }
        if (logicalResponse != null) {
            updateCount++;
        }
        if(checkForWinner(Board.SERVER)) {
            resetGame();
        }
        return logicalResponse;
    }

    private String makeFirstMove() {
        // server can make first move
        if (mBoard.isEmpty()) {
            return mBoard.makeCornerMove();
        }

        // client made first move
        updateCount++;
        int x = mBoard.getAnX();
        if (mBoard.isInCorner(Board.CLIENT)) {
            return mBoard.makeCenterMove();
        }
        if (mBoard.isInCenter(Board.CLIENT)) {
            return mBoard.makeMove(mBoard.getOppositeCorner(x));
        }
        if (mBoard.isOnEdge(Board.CLIENT)) {
            return mBoard.moveAdjacentCorner(x);
        }
        return null;
    }

    // server made first move
    private String makeThirdMove() {
        if (mBoard.isInCenter(Board.CLIENT)) {
            return mBoard.moveAdjacentCorner(mBoard.getAnO());
        }
        else if (mBoard.isOnEdge(Board.CLIENT)) {
            return mBoard.makeCenterMove();
        }
        else if (mBoard.isInCorner(Board.CLIENT)) {
            return mBoard.makeCornerMove();
        }
        return null;
    }

    // client made first move
    private String makeForthMove() {
        String serverMove;
        int blocking = mBoard.getBlockingMove(Board.CLIENT);
        serverMove = mBoard.makeMove(blocking);

        if (serverMove == null) {
            if (!mBoard.isInCenter(Board.SERVER)) {
                serverMove = mBoard.makeCenterMove();
            }
        }
        return serverMove;
    }

    private String makeSubsequentMoves() {
        int winningMove = mBoard.getBlockingMove(Board.SERVER);
        if (winningMove != -1) {
            return mBoard.makeMove(winningMove);
        }
        int blockingMove = mBoard.getBlockingMove(Board.CLIENT);
        if (blockingMove != -1) {
            return mBoard.makeMove(blockingMove);
        }
        if (mBoard.isOnEdge(Board.CLIENT) && mBoard.isInCorner(Board.SERVER) && mBoard.isInCenter(Board.SERVER)) {
            return mBoard.makeCornerMove();
        }
        return mBoard.makeDefaultMove();
    }
}
