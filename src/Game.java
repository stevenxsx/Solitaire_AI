import java.io.IOException;

public class Game {


    public void startGame() throws Exception {
        //make sure cards are shuffled
        //load cards into game space
        CardDeck cardDeck = new CardDeck();
        cardDeck.initialPopulate();
        System.out.println(cardDeck);
        cardDeck.shuffleDeck();
        System.out.println("|||||||||||||||||||||||||||||||||||||");
        System.out.println(cardDeck);

    }
}
