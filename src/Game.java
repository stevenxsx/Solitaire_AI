public class Game {

    public void startGame() throws Exception {
        CardDeck cardDeck = new CardDeck();
        cardDeck.populate();
        System.out.println(cardDeck);
        cardDeck.shuffleDeck();
        System.out.println("|||||||||||||||||||||||||||||||||||||");
        System.out.println(cardDeck);
    }

}
