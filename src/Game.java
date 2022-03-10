import Exceptions.NotEnoughCardsException;

import java.util.ArrayList;

public class Game {


    public void startGame() throws Exception {
        Board board = new Board();
        board.initialDeck.populate();
        board.initialDeck.shuffleDeck();
        System.out.println("Initial Deck Size = " + board.initialDeck.size());
        board.initialPopulateBoard();
        board.printBoard();

    }

    /*private void dealCards(int piles) {
        this.piles = new ArrayList<>(piles);
        for (int i = 1; i <= piles; i++){
            try {
                CardPile pile;
                pile = new CardPile(this.cardDeck.dealCards(i));
                this.piles.add(pile);
            } catch (NotEnoughCardsException e) {
                e.printStackTrace();
            }
        }
    }*/
}
