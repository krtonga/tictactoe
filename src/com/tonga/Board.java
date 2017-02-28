package com.tonga;

import static junit.framework.TestCase.assertEquals;

public class Board {
    String mBoard;
    char mClientSymbol = 'x';
    char mServerSymbol = 'o';
    char mBlankSymbol = '+';

    String validBoardRegex = "^[ox\\+]+$";
    
    int[][] possibleThreeInARows;


    public static void main(String[] args) {
	// write your code here
    }

    public Board(String board) {
        mBoard = board;
        possibleThreeInARows = new int[][]{{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    }

    public boolean isValidBoard() {
        return mBoard.length() == 9 && mBoard.matches(validBoardRegex);
    }

    public boolean hasFreeSpaces() {
        return mBoard.contains(String.valueOf(mBlankSymbol));
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
        if (mBoard.charAt(position) == mBlankSymbol) {
            char[] board = mBoard.toCharArray();
            board[position] = mServerSymbol;
            mBoard = String.valueOf(board);
            return mBoard;
        }
        return null;
    }

    public int getBlockingMove() {

    }
}
