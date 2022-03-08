import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class CardDeck {

    private final ArrayList<Card> deck = new ArrayList<>(52);

    public void populate() throws IOException {
        File file = new File("./resources/cards.txt");
        if (!file.exists())
            throw new FileNotFoundException("Could not find " + file.getPath());

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine())
            deck.add(new Card(parseSuit(scanner.nextLine()), parseValue(scanner.nextLine())));

        scanner.close();
    }

    private Suit parseSuit(String s) {
        return switch (s.split("")[0]) {
            case "H" -> Suit.HEARTS;
            case "S" -> Suit.SPADES;
            case "D" -> Suit.DIAMONDS;
            case "C" -> Suit.CLUBS;
            default -> throw new IllegalStateException("Unexpected value: " + s.split("")[0]);
        };
    }

    private int parseValue(String s) {
        return switch (s.split("")[1]) {
            case "A" -> 1;
            case "T" -> 10;
            case "J" -> 11;
            case "Q" -> 12;
            case "K" -> 13;
            default -> Integer.parseInt(s.split("")[1]);
        };
    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Card card : deck) {
            s.append(card.toString());
        }
        return s.toString();
    }

}
