import com.tonga.Board;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class BoardTest {
    private Board mBoard;

    @Test
    public void testToString() {
        mBoard = new Board("xox++++++");
        assertEquals("xox++++++", mBoard.toString());
    }

    @Test
    public void boardIsEmpty() {
        mBoard = new Board("+++++++++");
        assertTrue(mBoard.isEmpty());

        mBoard = new Board("++x++++++");
        assertFalse(mBoard.isEmpty());
    }

    @Test
    public void testIsValidFirstBoard() {
        String[] validFirst = {"+++++++++", "++x++++++","++++++++x"};
        for (String board : validFirst) {
            assertTrue("Board ("+board+") should be valid", Board.isValidFirst(board));
        }

        String[] invalidFirst = {"++++++o++, xx+++++++, ++++++xx+, +xx+o++++","+++","+++++++i+"};
        for (String board : invalidFirst) {
            assertFalse("Board ("+board+") is invalid.", Board.isValidFirst(board));
        }
    }

    @Test
    public void testIsValidUpdate() {
        mBoard = new Board("o++++++++");
        assertTrue(mBoard.isValidUpdate("o++x+++++"));
        assertEquals("o++++++++", mBoard.toString());

        String[] invalidUpdates = {"o++++++++", "+o+++++++","xo+++++++","oxx++++++","+++"};
        for (String board : invalidUpdates) {
            mBoard = new Board("o++x+++++");
            assertFalse("Board ("+board+") is not a valid isValidUpdate.", mBoard.isValidUpdate(board));
            assertEquals("o++x+++++", mBoard.toString());
        }
    }

    @Test
    public void testUpdate() {
        mBoard = new Board("o++++++++");
        mBoard.updateBoard("o++x+++++");
        assertEquals("o++x+++++", mBoard.toString());
    }

    @Test
    public void testHasFreeSpaces() {
        Board board = new Board("xoox+ooox");
        assertTrue(board.hasFreeSpaces());

        board = new Board("xooxoooox");
        assertFalse(board.hasFreeSpaces());
    }

    @Test
    public void getLastX() {
        Board board = new Board("++x++++++");
        assertEquals(2, board.getAnX());
    }

    @Test
    public void getLastO() {
        Board board = new Board("+ox++++++");
        assertEquals(1, board.getAnO());
    }

    @Test
    public void getCountOfMovesForPlayers() {
        Board board = new Board("+++++++++");
        assertEquals(0, board.getMoveCounts().getTotalNumOfMoves());
        assertTrue(board.getMoveCounts().isValidMove());

        board = new Board("++xo+++++");
        assertEquals(2, board.getMoveCounts().getTotalNumOfMoves());
        assertEquals(1, board.getMoveCounts().getNumOfClientMoves());
        assertEquals(1, board.getMoveCounts().getNumOfServerMoves());
        assertTrue(board.getMoveCounts().isValidMove());

        board = new Board("xoxo++x++");
        assertEquals(5, board.getMoveCounts().getTotalNumOfMoves());
        assertEquals(3, board.getMoveCounts().getNumOfClientMoves());
        assertEquals(2, board.getMoveCounts().getNumOfServerMoves());
        assertTrue(board.getMoveCounts().isValidMove());

        board = new Board("xxxxxx+ox");
        assertFalse(board.getMoveCounts().isValidMove());

        board = new Board("xoo++++ox");
        assertFalse(board.getMoveCounts().isValidMove());
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
    public void testMakeDefaultMove() {
        mBoard = new Board("x++++++++");
        assertEquals("xo+++++++", mBoard.makeDefaultMove());

        mBoard = new Board("xxxxxxxxx");
        assertNull("Null is returned if position if board is full", mBoard.makeDefaultMove());
    }

    @Test
    public void testBlockingMove() {
        String[] boardsNeedingBlock = {"+xx++++++", "++++xx+++", "++++++x+x", "x++x+++++", "++++x++x+", "++x+++++x", "++++x+++x", "++x+x++++"};
        int[] expectedBlock = {0, 3, 7, 6, 1, 5, 0, 6};

        for (int i = 0; i < boardsNeedingBlock.length; i++) {
            mBoard = new Board(boardsNeedingBlock[i]);
            assertEquals("Board should know how to block. "+boardsNeedingBlock[i],
                    expectedBlock[i], mBoard.getBlockingMove('x'));
        }
    }

    @Test
    public void testIsInCorner() {
        String[] xInCorner = {"x++++++++", "++++++++x","++x++++++","++++++x++"};
        for (String board : xInCorner) {
            mBoard = new Board(board);
            assertTrue(mBoard.isInCorner('x'));
        }

        String[] oInCorner = {"o++++++++", "++++++++o","++o++++++","++++++o++"};
        for (String board : oInCorner) {
            mBoard = new Board(board);
            assertTrue(mBoard.isInCorner('o'));
        }

        mBoard = new Board("+o+xxx+o+");
        assertFalse(mBoard.isInCorner('o'));
        assertFalse(mBoard.isInCorner('x'));
    }

    @Test
    public void testCornerMove() {
        String[] possibleExpected = {"o++++++++","++o++++++","++++++o++","++++++++o"};
        mBoard = new Board("+++++++++");
        String cornerMove = mBoard.makeCornerMove();
        assertTrue("Move ("+cornerMove+") is in a corner.", cornerMove.equals(possibleExpected[0]) || cornerMove.equals(possibleExpected[1]) || cornerMove.equals(possibleExpected[2]) || cornerMove.equals(possibleExpected[3]));

        mBoard = new Board("x+x+++x++");
        assertEquals("x+x+++x+o", mBoard.makeCornerMove());

        mBoard = new Board("x+x+++x+x");
        assertNull(mBoard.makeCornerMove());
    }

    @Test
    public void testIsInCenter() {
        mBoard = new Board("++++o++++");
        assertTrue(mBoard.isInCenter('o'));
        mBoard = new Board("++++x++++");
        assertTrue(mBoard.isInCenter('x'));
        mBoard = new Board("+++++++++");
        assertFalse(mBoard.isInCenter('o'));
        assertFalse(mBoard.isInCenter('x'));
    }

    @Test
    public void testCenterMove() {
        mBoard = new Board("x++++++++");
        assertEquals("x+++o++++", mBoard.makeCenterMove());

        mBoard = new Board("++++x++++");
        assertNull(mBoard.makeCenterMove());
    }

    @Test
    public void testAnxiety() {
        String[] xOnEdge = {"+x+++++++","+++x+++++","+++++x+++","+++++++x+"};
        for (String board : xOnEdge) {
            mBoard = new Board(board);
            assertTrue(mBoard.isOnEdge('x'));
        }

        mBoard = new Board("x++++++++");
        assertFalse(mBoard.isOnEdge('x'));
    }

    @Test
    public void testAdjacentCorner() {
        mBoard = new Board("+x+++++++");
        String adjacent = mBoard.moveAdjacentCorner(1);
        assertTrue("Should be adjacent corner: "+adjacent,adjacent.equals("ox+++++++") || adjacent.equals("+xo++++++"));

        mBoard = new Board("+++x+++++");
        adjacent = mBoard.moveAdjacentCorner(3);
        assertTrue("Should be adjacent corner: "+adjacent,adjacent.equals("o++x+++++") || adjacent.equals("+++x++o++"));

        mBoard = new Board("+++++x+++");
        adjacent = mBoard.moveAdjacentCorner(5);
        assertTrue("Should be adjacent corner: "+adjacent,adjacent.equals("++o++x+++") || adjacent.equals("+++++x++o"));

        mBoard = new Board("+++++++x+");
        adjacent = mBoard.moveAdjacentCorner(7);
        assertTrue("Should be adjacent corner: "+adjacent,adjacent.equals("++++++ox+") || adjacent.equals("+++++++xo"));

        mBoard = new Board("ox+++++++");
        assertEquals("oxo++++++", mBoard.moveAdjacentCorner(1));

        mBoard = new Board("oxo++++++");
        assertNull(mBoard.moveAdjacentCorner(1));
    }
}