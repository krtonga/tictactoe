package com.tonga;

import java.util.Random;
/**
 * This represents a board which can be used to play a game.
 * Right now it has utility methods for playing TicTacToe.
 */
public class Board {
    static final char CLIENT = 'x';
    static final char SERVER = 'o';
    static final char BLANK = '+';

    private static final String validBoardRegex = "^["+CLIENT+SERVER+"\\"+BLANK+"]+$";
    private static final String emptyBoardRegex = "\\"+BLANK+"{9}";
    private static final String validFirstBoardRegex = "^["+CLIENT+"\\"+BLANK+"]+$"; //TODO: regex pattern that limits to 0 or 1 x

    private final int[] corners = {0,2,6,8};
    private final int[] edges = {1,3,5,7};
    private final int center = 4;
    private int[][] possibleThreeInARows;

    private String mBoard;

    public Board(String initialBoard) {
        mBoard = initialBoard;
        possibleThreeInARows = new int[][]{{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    }

    @Override
    public String toString() {
        return mBoard;
    }

    public static boolean isValidFirst(String board) {
        return board.length() == 9 &&
                (board.matches(emptyBoardRegex) ||
                        (board.matches(validFirstBoardRegex) && board.indexOf(CLIENT) == board.lastIndexOf(CLIENT)));
    }

    private boolean isValidBoard(String board) {
        return board.length() == 9 && board.matches(validBoardRegex);
    }

    public boolean update(String newBoard) {
        if (isValidBoard(newBoard)) {
            int differenceCount = 0;
            char oldChar;
            char newChar;
            for (int i = 0; i < mBoard.length(); i++) {
                oldChar = mBoard.charAt(i);
                newChar = newBoard.charAt(i);
                if (oldChar != newChar) {
                    if (newChar != CLIENT) {
                        return false;
                    }
                    differenceCount++;
                }
                if (differenceCount > 1) {
                    return false;
                }
            }
            if (differenceCount == 1) {
                mBoard = newBoard;
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return mBoard.matches(emptyBoardRegex);
    }

    public boolean hasFreeSpaces() {
        return mBoard.contains(String.valueOf(BLANK));
    }

    public int getAnX() {
        return mBoard.indexOf(CLIENT);
    }

    public int getAnO() {
        return mBoard.indexOf(SERVER);
    }

    public boolean hasThreeInARow(char symbol) {
        boolean winner = false;
        int i = 0;
        while (!winner && i < possibleThreeInARows.length) {
            int[] possibility = possibleThreeInARows[i];
            winner = mBoard.charAt(possibility[0]) == symbol &&
                    mBoard.charAt(possibility[1]) == symbol &&
                    mBoard.charAt(possibility[2]) == symbol;
            i++;
        }
        return winner;
    }

    public String makeMove(int position) {
        if (position == -1) {
            return null;
        }
        if (mBoard.charAt(position) == BLANK) {
            char[] board = mBoard.toCharArray();
            board[position] = SERVER;
            mBoard = String.valueOf(board);
            return mBoard;
        }
        return null;
    }

    public String makeDefaultMove() {
        return makeMove(mBoard.indexOf(BLANK));
    }

    public int getBlockingMove(char symbol) {
        boolean winner = false;
        int i = 0;
        int counter = 0;
        int blockingPosition = -1;
        while (!winner && i < possibleThreeInARows.length) {
            int[] possibility = possibleThreeInARows[i];
            for (int position : possibility) {
                if (mBoard.charAt(position) == symbol) {
                    counter++;
                }
                else if (mBoard.charAt(position) == BLANK){
                    blockingPosition = position;
                }

                if (counter == 2 && blockingPosition != -1) {
                    return blockingPosition;
                }
                if (counter == 3) {
                    winner = true;
                }
            }
            counter = 0;
            blockingPosition = -1;
            i++;
        }
        return -1;
    }

    public boolean isInCorner(char symbol) {
        return mBoard.charAt(corners[0]) == symbol ||
                mBoard.charAt(corners[1]) == symbol ||
                mBoard.charAt(corners[2]) == symbol ||
                mBoard.charAt(corners[3]) == symbol;
    }

    int getOppositeCorner(int position) {
        switch (position) {
            case 1 : return 8;
            case 3 : return 6;
            case 6 : return 3;
            case 8 : return 1;
        }
        return -1;
    }

    public String makeCornerMove() {
        Random random = new Random();
        int randomCorner = random.nextInt(corners.length-1);
        if (mBoard.charAt(corners[randomCorner]) == BLANK) {
            return makeMove(corners[randomCorner]);
        }
        else {
            for (int corner : corners) {
                if (mBoard.charAt(corner) == BLANK) {
                    return makeMove(corner);
                }
            }
        }
        return null;
    }

    public boolean isInCenter(char symbol) {
        return mBoard.charAt(center) == symbol;
    }

    public String makeCenterMove() {
        if (mBoard.charAt(center) == BLANK) {
            return makeMove(center);
        }
        return null;
    }

    public boolean isOnEdge(char symbol) {
        return mBoard.charAt(edges[0]) == symbol ||
                mBoard.charAt(edges[1]) == symbol ||
                mBoard.charAt(edges[2]) == symbol ||
                mBoard.charAt(edges[3]) == symbol;
    }

    public String moveAdjacentCorner(int position) {
        switch (position) {
            case 1:
                return moveEither(0, 2);
            case 3:
                return moveEither(0, 6);
            case 5:
                return moveEither(2, 8);
            case 7:
                return moveEither(6, 8);
        }
        return null;
    }

    private String moveEither(int... positions) {
        String move = null;
        int i = 0;
        while (move == null && i < positions.length) {
            move = makeMove(positions[i]);
            i++;
        }
        return move;
    }
}
