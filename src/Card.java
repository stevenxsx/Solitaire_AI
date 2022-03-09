public class Card {

    private final Suit suit;
    private final int value;
    private boolean faceUp;


    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
        faceUp = false;
    }

    public void faceCardUp(boolean up){this.faceUp = up;}

    public Suit getSuit(){return this.suit;}

    public int getValue(){return this.value;}

    public boolean getFaceUp(){return this.faceUp;}

    public String toString() {
        return value + " of " + suit.toString() + "\n";
    }

}
