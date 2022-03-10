import java.io.IOException;
import java.util.ArrayList;

public class Board {

    //All board operations and card movement go in this class.

    CardDeck initialDeck = new CardDeck(); //id = "deck"
    CardDeck drawDeck = new CardDeck(); // "draw"
    CardDeck drawDiscard = new CardDeck(); // "discard"
    CardDeck pile1 = new CardDeck(); // "1"
    CardDeck pile2 = new CardDeck(); // "2"
    CardDeck pile3 = new CardDeck(); // "3"
    CardDeck pile4 = new CardDeck(); // "4"
    CardDeck pile5 = new CardDeck(); // "5"
    CardDeck pile6 = new CardDeck(); // "6"
    CardDeck pile7 = new CardDeck(); // "7"
    CardDeck heartsPile = new CardDeck(); // "hearts"
    CardDeck spadesPile = new CardDeck(); // "spades"
    CardDeck diamondsPile = new CardDeck(); // "diamonds"
    CardDeck clubsPile = new CardDeck(); // "clubs"


    public void moveCardDeckToDeck(CardDeck deck1, CardDeck deck2, int index,boolean flipFaceUp) {

        //forced move -> no check to see if its legal
        while (deck1.size() > index) {
            deck2.add(deck1.get(index));
            deck1.remove(index);
        }
    }

    //Allows for referencing all the card decks by string. Useful for using the 7 piles in for loops where 'i'
    //is equal to the pile you want
    public CardDeck getDeck(String input) throws Exception {
        return switch (input) {
            case "deck" -> initialDeck;
            case "draw" -> drawDeck;
            case "discard" -> drawDiscard;
            case "1" -> pile1;
            case "2" -> pile2;
            case "3" -> pile3;
            case "4" -> pile4;
            case "5" -> pile5;
            case "6" -> pile6;
            case "7" -> pile7;
            case "hearts" -> heartsPile;
            case "spades" -> spadesPile;
            case "diamonds" -> diamondsPile;
            case "clubs" -> clubsPile;
            default -> throw new Exception("Typo in deck name \"" + input + "\"");
        };
    }

    //Creates the board using the initial card deck. Make sure the deck is shuffled.
    public void initialPopulateBoard() throws Exception {
        for (int i = 1; i <= 7; i++) {
            //Fills each card pile with 1-7 cards, respectively. Then it flips the last card face up.
            moveCardDeckToDeck(initialDeck, getDeck(Integer.toString(i)),
                    initialDeck.size()-(i),false);
            getDeck(Integer.toString(i)).get(i-1).faceCardUp(true);

        }
    }

    public void printBoard() {
        StringBuilder sb = new StringBuilder();
        sb
                .append(initialDeck.toString("Initial"))
                .append(drawDeck.toString("Draw"))
                .append(drawDiscard.toString("Discard"))
                .append(pile1.toString("Pile1"))
                .append(pile2.toString("Pile2"))
                .append(pile3.toString("Pile"))
                .append(pile4.toString("Pile4"))
                .append(pile5.toString("Pile5"))
                .append(pile6.toString("Pile6"))
                .append(pile7.toString("Pile7"))
                .append(heartsPile.toString("Hearts"))
                .append(spadesPile.toString("Spades"))
                .append(diamondsPile.toString("Diamonds"))
                .append(clubsPile.toString("Clubs"));
        System.out.println(sb);

        // Create new super print method with formatting


    }

    //Superfluous method?
    /*public String toString(ArrayList<Card> deck) {
        StringBuilder sb = new StringBuilder();
        for (Card c:deck
             ) {
            sb.append(c.toString());
        }
        return sb.toString();
    }*/
}
