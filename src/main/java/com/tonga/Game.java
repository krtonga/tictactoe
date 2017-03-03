package com.tonga;

import static com.tonga.Board.BLANK;
import static com.tonga.Board.isValidFirst;

/**
 * This class represents a TicTacToe game.
 */
public class Game {
    public static final char TIE = 't';

    private Board mBoard;
    private Brains mBrains;
    private Brains.MoveResult mLastValidMove;

    public Game() {
        mBrains = new Brains();
    }

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
    public Brains.MoveResult playGame(String clientMove) {
        if (mBoard == null) {
            if (isValidFirst(clientMove)) {
                mBoard = new Board(clientMove);
                return makeMove();
            } else {
                return null;
            }
        } else if (mBoard.isValidUpdate(clientMove)) {
            mBoard.updateBoard(clientMove);
            return makeMove();
        }
        return null; // invalid board sent
    }

    private Brains.MoveResult makeMove() {
        Brains.MoveResult serverMove = mBrains.makeOptimalMove(mBoard);
        if (serverMove != null) {
            if (serverMove.getWinner() == BLANK) {
                mBoard.updateBoard(serverMove.getServerMove());
                mLastValidMove = serverMove;
            } else {
                resetGame(); // someone has won, so start over
            }
        }
        return serverMove;
    }

    public char getWinner() {
        if (mLastValidMove != null) {
            return mLastValidMove.getWinner();
        }
        return BLANK;
    }

    public String getLastValidBoard() {
        if (mBoard == null) {
            return null;
        }
        return mBoard.toString();
    }

    void resetGame() {
        mBoard = null;
        mLastValidMove = null;
    }
}
