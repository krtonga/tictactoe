import com.tonga.Board;
import com.tonga.Brains;
import com.tonga.Game;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

/**
 *
 */
public class BrainsTest {
    private Brains mBrains;

    @Before
    public void init() {
        mBrains = new Brains();
    }

    @Test
    public void testGetOptimalMove() {
        assertEquals("x+++o++++", mBrains.makeOptimalMove("x++++++++").getServerMove());
        assertEquals("xxo+o++++", mBrains.makeOptimalMove("xx++o++++").getServerMove());
        assertEquals("xxooo+x++", mBrains.makeOptimalMove("xxo+o+x++").getServerMove());
        assertEquals("xxoooxx+o", mBrains.makeOptimalMove("xxoooxx++").getServerMove());
        assertEquals("x+++o++++", mBrains.makeOptimalMove("x++++++++").getServerMove());
        assertEquals("xxo+o++++", mBrains.makeOptimalMove("xx++o++++").getServerMove());
    }

    @Test
    public void testInvalidBoardReturnsNull() {
        String[] invalidBoards = {"o++++++++", "xx+++++++", "foxyroxy+","++", "+++++xo++xo"};
        for (String board: invalidBoards) {
            assertNull("Board ("+board+") should be invalid.", mBrains.makeOptimalMove(board));
        }
    }

    @Test
    public void testWinnerIsFound() {
        Brains.MoveResult tie = mBrains.makeOptimalMove("xxoooxxxo");
        assertEquals("xxoooxxxo", tie.getServerMove());
        assertEquals('t', tie.getWinner());

        Brains.MoveResult clientWin = mBrains.makeOptimalMove("xxx+oxo+o");
        assertEquals("xxx+oxo+o", clientWin.getServerMove());
        assertEquals('x', clientWin.getWinner());

        Brains.MoveResult serverWin = mBrains.makeOptimalMove("xxo+ox+++");
        assertEquals("xxo+oxo++", serverWin.getServerMove());
        assertEquals('o', serverWin.getWinner());

        Brains.MoveResult noWin = mBrains.makeOptimalMove("xx++o++++");
        assertEquals("xxo+o++++", noWin.getServerMove());
        assertEquals('+', noWin.getWinner());
    }
}