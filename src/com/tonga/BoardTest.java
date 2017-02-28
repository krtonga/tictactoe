package com.tonga;


import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class BoardTest {
    Board mBoard;

    @Test
    public void testIsValidBoard() {
        String[] invalidBoards = {"", "xo++++i++", "++++++++++"};
        for (String board : invalidBoards) {
            mBoard = new Board(board);
            assertFalse("This board ("+board+") is not valid.", mBoard.isValidBoard());
        }

        String validBoard = "xoxxoo+++";
        mBoard = new Board(validBoard);
        assertTrue("This board ("+validBoard+") is valid.", mBoard.isValidBoard());
    }

    @Test
    public void testHasFreeSpaces() {
        Board board = new Board("xoox+ooox");
        assertTrue(board.hasFreeSpaces());

        board = new Board("xooxoooox");
        assertFalse(board.hasFreeSpaces());
    }

    @Test
    public void testThreeInARow() {
        String[] winningXBoards = {"xxx++++++", "+++xxx+++", "++++++xxx", "x++x++x++", "+x++x++x+", "++x++x++x", "x+++x+++x", "++x+x+x++"};
        for (String board : winningXBoards) {
            mBoard = new Board(board);
            assertTrue("This board ("+board+") has 3 in a row.", mBoard.hasThreeInARow('x'));
        }

        String unfinishedGame = "x++++++++";
        mBoard = new Board(unfinishedGame);
        assertFalse("This board ("+unfinishedGame+") does not have 3 in a row.", mBoard.hasThreeInARow('x'));
    }

    @Test
    public void testMakeMove() {
        mBoard = new Board("x++++++++");
        assertEquals("x+o++++++", mBoard.makeMove(2));
        assertNull("Null is returned if position is not a valid move", mBoard.makeMove(2));
    }

    @Test
    public void testBlockingMove() {
        String[] boardsNeedingBlock = {"+xx++++++", "++++xx+++", "++++++x+x", "x++x+++++", "++++x++x+", "++x+++++x", "++++x+++x", "++x+x++++"};
        int[] expectedBlock = {0, 3, 7, 6, 1, 5, 0, 6};
        String[] expectedBlock = {"oxx++++++", "+++oxx+++", "++++++xox", "x++x++o++", "+o++x++x+", "++x++o++x", "o+++x+++x", "++x+x+o++"};

        for (int i = 0; i < boardsNeedingBlock.length; i++) {
            mBoard = new Board(boardsNeedingBlock[i]);
        }
    }
}