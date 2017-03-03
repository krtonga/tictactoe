import com.tonga.Brains;
import org.junit.Test;
import com.tonga.Game;

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
            assertNotNull(mGame.playGame(board));
        }

        String[] invalidStartingBoard = {"o++++++++", "xx+++++++", "xoxoxo+++","++"};
        for (String board: invalidStartingBoard) {
            mGame = new Game();
            assertNull("Board ("+board+") should be invalid first move.", mGame.playGame(board));
        }
    }

    @Test
    public void testPlayGame() {
        mGame = new Game();
        assertEquals("x+++o++++", mGame.playGame("x++++++++").getServerMove());
        assertEquals("xxo+o++++", mGame.playGame("xx++o++++").getServerMove());
        assertEquals("xxooo+x++", mGame.playGame("xxo+o+x++").getServerMove());
        assertEquals("xxoooxx+o", mGame.playGame("xxoooxx++").getServerMove());

        Brains.MoveResult tie = mGame.playGame("xxoooxxxo");
        assertEquals("xxoooxxxo", tie.getServerMove());
        assertEquals('t', tie.getWinner());

        assertNull("After a tie, game should be reset", mGame.getLastValidBoard());
    }

    @Test
    public void testCannotUpdateWonGame() {
        mGame = new Game();
        assertEquals("x+++o++++", mGame.playGame("x++++++++").getServerMove());
        assertEquals("xxo+o++++", mGame.playGame("xx++o++++").getServerMove());

        Brains.MoveResult win = mGame.playGame("xxo+ox+++");
        assertEquals("xxo+oxo++", win.getServerMove());
        assertEquals('o', win.getWinner());

        assertNull("After a win, game should be reset", mGame.getLastValidBoard());
        assertEquals('+', mGame.getWinner());
        assertNull(mGame.playGame("xxoxoxo++"));
    }

    @Test
    public void testCannotMakeInvalidMove() {
        mGame = new Game();
        assertEquals("x+++o++++", mGame.playGame("x++++++++").getServerMove());
        assertEquals("xxo+o++++", mGame.playGame("xx++o++++").getServerMove());

        assertNull("Cannot move more than once.", mGame.playGame("xxo+o+xxx"));
        assertEquals("xxo+o++++", mGame.getLastValidBoard());

        assertNull("Cannot move for server.", mGame.playGame("xxo+o+++o"));
        assertEquals("xxo+o++++", mGame.getLastValidBoard());

        assertNull("Cannot modify previous moves", mGame.playGame("xxx+o++++"));
        assertEquals("xxo+o++++", mGame.getLastValidBoard());
    }
}