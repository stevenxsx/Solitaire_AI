import Exceptions.NotEnoughCardsException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class CardDeck {

    private final ArrayList<Card> cards = new ArrayList<>(52);

    public void populate() throws IOException {
        File file = new File("./resources/cards.txt");
        if (!file.exists())
            throw new FileNotFoundException("Could not find " + file.getPath());

        Scanner scanner = new Scanner(file);
        String next;
        while (scanner.hasNextLine()) {
            next = scanner.nextLine();
            cards.add(new Card(parseSuit(next), parseValue(next)));
        }
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
        Collections.shuffle(cards);
    }
    
    public ArrayList<Card> dealCards(int numberOfCards) throws NotEnoughCardsException{
        if (this.cards.size() >= numberOfCards){
            ArrayList<Card> dealtCards = new ArrayList<>();
            for (int i = numberOfCards; i != 0; i--){
                dealtCards.add(this.popCard());
            }
            dealtCards.get(this.cards.size()-1).faceCardUp(true);
            return dealtCards;
        } else {
            throw new NotEnoughCardsException("The deck only has" + cards.size() + "cards");
        }
    }

    public String toString(String deckName) {
        StringBuilder s = new StringBuilder();
        s.append(deckName).append(" includes the following:\n");
        for (Card card : cards) {
            s.append(card.toString()).append(" - Face Up -> ").append(card.getFaceUp()).append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    private Card popCard() throws NotEnoughCardsException {
        if (this.cards.size() != 0) {
            return this.cards.remove(this.cards.size()-1);
        } else {
            throw new NotEnoughCardsException("The deck is empty!");
        }
    }

    public int size() {
        return cards.size();
    }

    public Card get(int index) {
        return cards.get(index);
    }

    public void add(Card card) {
        cards.add(card);
    }

    public void remove(int index) { cards.remove(index);
    }

    //Call this function to recieve the index of the top card in the deck
    public int getTopCardIndex() {
        return this.cards.size()-1;
    }

    // Call this function to recieve the number of face down cards in the deck
    public Integer getNumberOfFaceDownCards(){
        int number = 0;
        for (Card card : this.cards){
            if(!card.getFaceUp()){
                number++;
            }
        }
        return number;
    }

    // Pass this function the index of a card in the deck to see if it matches the given card
    public boolean isCardValue(int index, Card card){
        Card deckCard = this.cards.get(index);
        return deckCard.getValue() == card.getValue() &&
                deckCard.getSuit() == card.getSuit();
    }

    // Call this function to check if popping the topcard will free a downcard
    public boolean canFreeDownCard(){
        return !this.cards.get(getTopCardIndex()-1).getFaceUp();
    }
}
