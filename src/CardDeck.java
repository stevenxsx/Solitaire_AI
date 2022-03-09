import Exceptions.NotEnoughCardsException;

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

    public void shuffleDeck() {
        Collections.shuffle(deck);
    }
    
    public ArrayList<Card> dealCards(int numberOfCards) throws NotEnoughCardsException{
        if (this.deck.size() >= numberOfCards){
            ArrayList<Card> dealtCards = new ArrayList<>();
            for (int i = numberOfCards; i != 0; i--){
                dealtCards.add(this.popCard());
            }
            dealtCards.get(0).faceCardUp(true);
            return dealtCards;
        } else {
            throw new NotEnoughCardsException("The deck only has" + deck.size() + "cards");
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Card card : deck) {
            s.append(card.toString());
        }
        return s.toString();
    }

    private Card popCard() throws NotEnoughCardsException {
        if (this.deck.size() != 0) {
            return this.deck.remove(0);
        } else {
            throw new NotEnoughCardsException("The deck is empty!");
        }
    }
}
