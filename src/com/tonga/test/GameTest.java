package com.tonga.test;


import org.junit.Test;
import com.tonga.main.Game;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

/**
 *
 */
public class GameTest {
    private Game mGame;

    @Test
    public void testValidStartingBoard() {
        String[] validStartingBoard = {"x++++++++","+++++++++"};
        for (String board : validStartingBoard) {
            mGame = new Game();
            assertNotNull(mGame.sendBoard(board));
        }

        String[] invalidStartingBoard = {"o++++++++", "xx+++++++", "xoxoxo+++","++"};
        for (String board: invalidStartingBoard) {
            mGame = new Game();
            assertNull("Board ("+board+") should be invalid first move.", mGame.sendBoard(board));
        }
    }

    @Test
    public void testPlayGame() {
        mGame = new Game();
        assertEquals(0, mGame.getUpdateCount());
        assertEquals("x+++o++++", mGame.sendBoard("x++++++++"));
        assertEquals(2, mGame.getUpdateCount());
        assertEquals("xxo+o++++", mGame.sendBoard("xx++o++++"));
        assertEquals(4, mGame.getUpdateCount());
        assertEquals("xxooo+x++", mGame.sendBoard("xxo+o+x++"));
        assertEquals(6, mGame.getUpdateCount());
        assertEquals("xxoooxx+o", mGame.sendBoard("xxoooxx++"));
        assertEquals(8, mGame.getUpdateCount());
        assertEquals("xxoooxxxo", mGame.sendBoard("xxoooxxxo"));
        assertEquals('t', mGame.getWinner());

        assertEquals(0, mGame.getUpdateCount());
    }

    @Test
    public void testCannotUpdateWonGame() {
        mGame = new Game();
        assertEquals(0, mGame.getUpdateCount());
        assertEquals("x+++o++++", mGame.sendBoard("x++++++++"));
        assertEquals(2, mGame.getUpdateCount());
        assertEquals("xxo+o++++", mGame.sendBoard("xx++o++++"));
        assertEquals(4, mGame.getUpdateCount());
        assertEquals("xxo+oxo++", mGame.sendBoard("xxo+ox+++"));
        assertEquals('o', mGame.getWinner());

        assertNull(mGame.sendBoard("xxoxoxo++"));
        assertEquals(0, mGame.getUpdateCount());
        assertEquals('+', mGame.getWinner());
    }
}