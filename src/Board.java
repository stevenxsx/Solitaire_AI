import java.util.ArrayList;

public class Board {

    //All board operations and card movement go in this class.

    ArrayList<Card> initialDeck = new ArrayList<>(); //id = "deck"
    ArrayList<Card> drawDeck = new ArrayList<>(); // "draw"
    ArrayList<Card> drawDiscard = new ArrayList<>(); // "discard"
    ArrayList<Card> pile1 = new ArrayList<>(); // "1"
    ArrayList<Card> pile2 = new ArrayList<>(); // "2"
    ArrayList<Card> pile3 = new ArrayList<>(); // "3"
    ArrayList<Card> pile4 = new ArrayList<>(); // "4"
    ArrayList<Card> pile5 = new ArrayList<>(); // "5"
    ArrayList<Card> pile6 = new ArrayList<>(); // "6"
    ArrayList<Card> pile7 = new ArrayList<>(); // "7"
    ArrayList<Card> heartsPile = new ArrayList<>(); // "hearts"
    ArrayList<Card> spadesPile = new ArrayList<>(); // "spades"
    ArrayList<Card> diamondsPile = new ArrayList<>(); // "diamonds"
    ArrayList<Card> clubsPile = new ArrayList<>(); // "clubs"

    public Board(CardDeck deck) {
        initialDeck = deck.getDeck();
    }

    public void moveCardDeckToDeck(ArrayList<Card> deck1, ArrayList<Card> deck2, int index,boolean flipFaceUp) {

        //forced move -> no check to see if its legal
        while (deck1.size() > index) {
            deck2.add(deck1.get(index));
            deck1.remove(index);
        }
    }

    public ArrayList<Card> getDeck(String input) throws Exception {
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
            getDeck(Integer.toString(i)).get(i-1).setFaceUp(true);

        }
    }

    public void printBoard() {
        StringBuilder sb = new StringBuilder();
        sb.append(toString(initialDeck))
                .append(drawDeck)
                .append(drawDiscard)
                .append(pile1)
                .append(pile2)
                .append(pile3)
                .append(pile4)
                .append(pile5)
                .append(pile6)
                .append(pile7)
                .append(heartsPile)
                .append(spadesPile)
                .append(diamondsPile)
                .append(clubsPile);
        System.out.println(sb);

    }

    public String toString(ArrayList<Card> deck) {
        StringBuilder sb = new StringBuilder();
        for (Card c:deck
             ) {
            sb.append(c.toString());
        }
        return sb.toString();
    }
}
