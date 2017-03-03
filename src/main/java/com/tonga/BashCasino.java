package com.tonga;

import java.util.Scanner;

/**
 * This is used to play a TicTacToe game via the command line.
 *
 * Assuming that the JDK is in your classpath, to run from the command line,
 * cd into the `src` directory and run the following:
 *      javac -cp . com/tonga/main/*.java
 *      java -cp . com/tonga/main/BashCasino
 *
 */
public class BashCasino {
    public static void main(String[] args) {
        char winner = Board.BLANK;
        String userMove;
        Game game = new Game();

        Scanner in = new Scanner(System.in);
        System.out.println("Send a board to begin playing.");
        System.out.println("Type 'q' or 'quit' at any time to stop.");
        while (winner == Board.BLANK) {
            userMove = in.nextLine();
            if (userMove.equals("quit") || userMove.equals("q")) {
                winner = 'q';
            }

            Brains.MoveResult serverMove = game.playGame(userMove);
            if (serverMove == null) {
                System.out.println("!! ERROR: 400 !!");
            } else {
                System.out.println(prettyBoard("USER", userMove));
                System.out.println(prettyBoard("SERVER", serverMove.getServerMove()));
            }
            System.out.println(congratulateWinner(game.getWinner()));
        }
        System.out.print("TIC-TAC-TOE TERMINATED!");
    }

    private static String prettyBoard(String user, String s) {
        if (s.length() < 9) {
            return "That's not a board! Try again!";
        }
        return "\n"+user+":\n"+s.substring(0,3)+"\n"+s.substring(3,6)+"\n"+s.substring(6,9)+"\n";
    }

    private static String congratulateWinner(char winner) {
        switch (winner) {
            case Game.TIE :
                return "Game was tied.\nSend a new board to start the game again.";
            case Board.CLIENT :
                return "Good job! You won.\nSend a new board to start the game again.";
            case Board.SERVER :
                return "API won!\nSend a new board to start the game again.";
            default :
                return "";
        }
    }
}
