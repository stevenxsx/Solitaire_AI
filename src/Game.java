import Exceptions.NotEnoughCardsException;

import java.util.ArrayList;

public class Game {


    public void startGame() throws Exception {
        Board board = new Board();
        AI ai = new AI(board);
        board.initialDeck.populate();
        board.initialDeck.shuffleDeck();
        System.out.println("Initial Deck Size = " + board.initialDeck.size());
        board.initialPopulateBoard();
        board.printBoard();
        do {
            ai.executeTurn();
            if (ai.gameIsWon) {
                winGame();
            }
        } while (!ai.gameIsLost);

    }

    public void winGame() {
        //you won. good job
    }

}
