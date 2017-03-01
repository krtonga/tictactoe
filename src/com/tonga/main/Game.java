package com.tonga.main;

import com.tonga.main.Board;

import static com.tonga.main.Board.BLANK;
import static com.tonga.main.Board.CLIENT;
import static com.tonga.main.Board.SERVER;

/**
 * This class represents a TicTacToe game.
 */
public class Game {
    public static final char TIE = 't';

    private Board mBoard;
    private int updateCount;
    char winner;

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
            winner = BLANK; // reset for round of game
            if (Board.isValidFirst(board)) {
                mBoard = new Board(board);
                winner = BLANK;
                return prepareResponse(makeFirstMove());
            }
        }
        else if (mBoard != null && mBoard.update(board)) {
            updateCount++;
            if (updateCount == 2) {
                return prepareResponse(makeThirdMove());
            }
            if (updateCount == 3) {
                return prepareResponse(makeForthMove());
            }
            else {
                boolean winOrTie = checkForWinner(CLIENT);
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

    private boolean checkForWinner(char symbol) {
        if (!mBoard.hasFreeSpaces()) {
            winner = TIE;
        }
        if (mBoard.hasThreeInARow(symbol)) {
            winner = symbol;
        }
        return winner != BLANK;
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
        if(checkForWinner(SERVER)) {
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
        if (mBoard.isInCorner(CLIENT)) {
            return mBoard.makeCenterMove();
        }
        if (mBoard.isInCenter(CLIENT)) {
            return mBoard.makeMove(mBoard.getOppositeCorner(x));
        }
        if (mBoard.isOnEdge(CLIENT)) {
            return mBoard.moveAdjacentCorner(x);
        }
        return null;
    }

    // server made first move
    private String makeThirdMove() {
        if (mBoard.isInCenter(CLIENT)) {
            return mBoard.moveAdjacentCorner(mBoard.getAnO());
        }
        else if (mBoard.isOnEdge(CLIENT)) {
            return mBoard.makeCenterMove();
        }
        else if (mBoard.isInCorner(CLIENT)) {
            return mBoard.makeCornerMove();
        }
        return null;
    }

    // client made first move
    private String makeForthMove() {
        String serverMove;
        int blocking = mBoard.getBlockingMove(CLIENT);
        serverMove = mBoard.makeMove(blocking);

        if (serverMove == null) {
            if (!mBoard.isInCenter(SERVER)) {
                serverMove = mBoard.makeCenterMove();
            }
        }
        return serverMove;
    }

    private String makeSubsequentMoves() {
        int winningMove = mBoard.getBlockingMove(SERVER);
        if (winningMove != -1) {
            return mBoard.makeMove(winningMove);
        }
        int blockingMove = mBoard.getBlockingMove(CLIENT);
        if (blockingMove != -1) {
            return mBoard.makeMove(blockingMove);
        }
        if (mBoard.isOnEdge(Board.CLIENT) && mBoard.isInCorner(SERVER) && mBoard.isInCenter(SERVER)) {
            return mBoard.makeCornerMove();
        }
        return mBoard.makeDefaultMove();
    }
}
