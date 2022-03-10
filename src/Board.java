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

    public void initialPopulateBoard() throws Exception {
        for (int i = 1; i <= 7; i++) {
            moveCardDeckToDeck(getDeck("deck"), getDeck(Integer.toString(i)),
                    getDeck("deck").size()-(i),false);
            getDeck(Integer.toString(i)).get(i-1).faceCardUp(true);

        }
    }

    public void printBoard() {
        StringBuilder sb = new StringBuilder();
        sb
                .append(initialDeck.toString())
                .append(drawDeck.toString())
                .append(drawDiscard.toString())
                .append(pile1.toString())
                .append(pile2.toString())
                .append(pile3.toString())
                .append(pile4.toString())
                .append(pile5.toString())
                .append(pile6.toString())
                .append(pile7.toString())
                .append(heartsPile.toString())
                .append(spadesPile.toString())
                .append(diamondsPile.toString())
                .append(clubsPile.toString());
        System.out.println(sb);

    }

    /*public String toString(ArrayList<Card> deck) {
        StringBuilder sb = new StringBuilder();
        for (Card c:deck
             ) {
            sb.append(c.toString());
        }
        return sb.toString();
    }*/
}
