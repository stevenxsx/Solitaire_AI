public class Card {

    private final Suit suit;
    private final int value;
    private boolean faceUp;


    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
        faceUp = false;
    }


}
