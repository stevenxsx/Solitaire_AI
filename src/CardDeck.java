import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class CardDeck {

    ArrayList<Card> deck = new ArrayList<>(52);

    public void initialPopulate() throws IOException {

        File file = new File(System.getProperty("user.dir") + "\\resources\\cards.txt"
        );

        BufferedReader br = new BufferedReader(new FileReader(file));

        String str;

        while ((str = br.readLine()) != null) {
            //handle each line of input
            deck.add(new Card(parseInputSuit(str),parseInputValue(str)));
        }

    }

    public void shuffleDeck() throws Exception {
        for (Card c:deck) {
            if (c == null) {
                throw new Exception("Null card slot exception");
            }
        }
        ArrayList<Card> deck2 = new ArrayList<>(52);
        while (!deck.isEmpty()) {
            int rdm = (int) (Math.random() * deck.size());
            deck2.add(deck.get(rdm));
            deck.remove(rdm);
        }
        deck = deck2;

    }

    public Suit parseInputSuit(String s) throws IOException {

        return switch (s.substring(0,1)) {
            case "H" -> Suit.HEARTS;
            case "S" -> Suit.SPADES;
            case "D" -> Suit.DIAMONDS;
            case "C" -> Suit.CLUBS;
            default -> throw new IOException("Input text format error");
        };
    }
    public int parseInputValue(String s) throws IOException {
        if (s.length() > 2) {
            throw new IOException("Input text format error");
        }
        return switch (s.substring(1,2)) {
            case "A" -> 1;
            case "T" -> 10;
            case "J" -> 11;
            case "Q" -> 12;
            case "K" -> 13;
            default -> Integer.parseInt(s.substring(1,2));
        };


    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Card a:deck) {
            s.append(a.toString());
        }
        return s.toString();
    }
}
