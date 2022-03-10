import Exceptions.NotEnoughCardsException;

import java.util.ArrayList;

public class Game {
    CardDeck cardDeck;
    ArrayList<CardPile> piles;

    public void startGame() throws Exception {
        cardDeck = new CardDeck();
        cardDeck.populate();
        System.out.println(cardDeck);
        cardDeck.shuffleDeck();
        System.out.println("|||||||||||||||||||||||||||||||||||||");
        System.out.println(cardDeck);

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
