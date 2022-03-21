import Exceptions.NotEnoughCardsException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Game {


    public void startGame() throws Exception {
        Board board = new Board();
        AI ai = new AI(board);
        board.initialDeck.populate();
        board.initialDeck.shuffleDeck();
        board.initialPopulateBoard();
        //board.printBoard();

        Scanner sc = new Scanner(System.in);
        String input;
        do {
            board.updateBoardState();
            board.printBoard();
            System.out.println("Ready for Input");
            input = sc.nextLine();
            board.parseInput(input);
        } while (!Objects.equals(input, "goodbye"));
        /*do {
            ai.executeTurn();
            if (ai.gameIsWon) {
                winGame();
            }
        } while (!ai.gameIsLost);*/

    }

    public void winGame() {
        //you won. good job
    }

}
