import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class Board {

    //Initialize all the piles contained in the board
    CardDeck initialDeck = new CardDeck("Initial Deck"); //id = "deck"
    CardDeck drawDeck = new CardDeck("Draw Pile"); // "draw"
    CardDeck drawDiscard = new CardDeck("Discard Pile"); // "discard"
    CardDeck pile1 = new CardDeck("Pile 1"); // "1"
    CardDeck pile2 = new CardDeck("Pile 2"); // "2"
    CardDeck pile3 = new CardDeck("Pile 3"); // "3"
    CardDeck pile4 = new CardDeck("Pile 4"); // "4"
    CardDeck pile5 = new CardDeck("Pile 5"); // "5"
    CardDeck pile6 = new CardDeck("Pile 6"); // "6"
    CardDeck pile7 = new CardDeck("Pile 7"); // "7"
    CardDeck heartsPile = new CardDeck("Hearts Foundation"); // "hearts"
    CardDeck spadesPile = new CardDeck("Spades Foundation"); // "spades"
    CardDeck diamondsPile = new CardDeck("Diamonds Foundation"); // "diamonds"
    CardDeck clubsPile = new CardDeck("Clubs Foundation"); // "clubs"

    AI ai = new AI(this); //Initialize the AI inside the board

    /** Author STEVEN
     *  Parses text input for manual use of the program
     *  Cheatsheet -> piles 1-7, foundations 8-11, draw 12, discard 13
     */
    public void parseInput(String input) throws Exception {
        switch (input) {
            case "goodbye": {return;}
            case "shuffle": {drawDeck.shuffleDeck();break;}
            case "draw": {attemptMove(new Move(drawDeck,drawDiscard, drawDeck.size()-1));break;}
            case "ai": {executeAI();break;}
            case "free 5": {drawUntilCardAvailable(5);break;}
            case "restart": {
                initialDeck.clearDeck();
                drawDeck.clearDeck();
                drawDiscard.clearDeck();
                pile1.clearDeck();
                pile2.clearDeck();
                pile3.clearDeck();
                pile4.clearDeck();
                pile5.clearDeck();
                pile6.clearDeck();
                pile7.clearDeck();
                heartsPile.clearDeck();
                spadesPile.clearDeck();
                diamondsPile.clearDeck();
                clubsPile.clearDeck();
                this.initialDeck.populate();
                this.initialDeck.shuffleDeck();
                this.initialPopulateBoard();
                break;
            }
            default: {StringTokenizer st = new StringTokenizer(input," ");
                String s = st.nextToken();
                String d = st.nextToken();
                String i = st.nextToken();
                if (i.equals("last")) {
                    i = String.valueOf(getDeck(s).size()-1);
                }
                Move move = new Move(getDeck(s),getDeck(d),Integer.parseInt(i));
                System.out.println("Move is: " + move);
                attemptMove(move);}
        }

    }

    public void executeAI() {
        ai.executeTurn();
    }

    /** Author STEVEN
     Flips newly revealed cards to face-up.
     */
    public void updateBoardState() {
        for (int i = 1; i < 8; i++) {
            CardDeck cd = getDeck(Integer.toString(i));
            if (cd.size() > 0) {
                cd.get(cd.size()-1).setFaceUp(true);
            }
        }
        if (drawDiscard.size() > 0) {
            if (!drawDiscard.get(drawDiscard.size() - 1).isFaceUp()) {
                drawDiscard.get(drawDiscard.size() - 1).setFaceUp(true);
            }
        }
    }
    /** Author STEVEN
     Moves a card given by an index from anywhere in the draw pile to the top of the discard pile naturally
     */
    public void drawUntilCardAvailable(int index) {
        int num = drawDeck.size() - index;
        for (int i = 0; i < num; i++) {
            drawCard(drawDeck,drawDiscard);
        }
    }
    /** Author STEVEN
     Moves a card given by an index from anywhere in the draw pile to the top of the discard pile naturally
     */
    public void emptyDiscardPile() {
        for (int i = 0; i < drawDiscard.size()-1; i++) {
            drawDiscard.get(i).setFaceUp(false);
            drawDeck.add(drawDiscard.get(i));
            drawDiscard.remove(i);
        }
    }

    /** Author STEVEN
     * Given a move object, checks the source & destination piles and attempts to move it there.
     */
    public boolean attemptMove(Move move) {
        CardDeck s = move.getSourceDeck();
        CardDeck d = move.getDestinationDeck();
        int x = move.getIndex();

        //Attempts to move card(s) from number pile to number pile.
        if (isNumberPile(s) && isNumberPile(d)) {
            if (canMoveToNumberPile(s, d, x)) {
                moveCardDeckToDeck(s, d, x, true);
                return true;
            }
        }
        //Attempts to move card from number pile to foundation
        else if (isNumberPile(s) && isFoundationPile(d)) {
            if (canMoveToFoundation(s, d, x)) {
                moveCardDeckToDeck(s, d, x, true);
                return true;
            }
        }
        //Attempts to move card from foundation to number pile
        else if (isFoundationPile(s) && isNumberPile(d)) {
            if (canMoveToNumberPile(s, d, x)) {
                moveCardDeckToDeck(s, d, x, true);
                return true;
            }
        }
        //Attempts to move card from Draw to Discard
        else if (isDrawPile(s) && isDiscardPile(d)) {
            drawCard(s,d);
            return true;
        }
        //Attempts to move card from Discard to Number pile
        else if (isDiscardPile(s) && isNumberPile(d)) {
            if (canMoveToNumberPile(s, d, x)) {
                moveCardDeckToDeck(s, d, x, true);
                return true;
            }
        }
        //Attempts to move card from Discard to Foundation
        else if (isDiscardPile(s) && isFoundationPile(d)) {
            if (canMoveToFoundation(s, d, x)) {
                moveCardDeckToDeck(s, d, x, true);
                return true;
            }
        }
        //If the move wasnt legal, and wasnt executed
        System.out.println("Illegal move sent to attemptMove() -> Source: " + s.toString() + " Destination: " + d.toString() + " Index: " + x);
        return false;
    }

    /** Author STEVEN
     * Check if the index card in the source pile is allowed to be moved to the destination number pile.
     */
    public boolean canMoveToNumberPile(CardDeck source, CardDeck destination, int index) {
        boolean value = false;
        boolean suit = false;
        boolean isFaceUp;
        boolean legalNumberOfCards = true;
        //Checks for attempting a move of king to empty pile.
        if (destination.size() == 0 ) {
            if (source.get(index).getValue() == 13) {
                if (source.get(index).isFaceUp()) {
                    return true;
                }
            }
            else return false;
        }
        //Source number value is 1 less than destination number.
        if (destination.get(destination.size() - 1).getValue() - source.get(index).getValue() == 1) {
            value = true;
        }
        //Source color is opposite of destination color
        if ((source.get(index).isRed() && destination.get(destination.size() - 1).isBlack()) || (source.get(index).isBlack() && destination.get(destination.size()-1).isRed())) {
            suit = true;
        }
        //Both cards must be face-up for the move to make any sense
        isFaceUp = areFaceUp(source, destination, index);
        //Can only move multiple cards if the source deck is a number pile.
        if (source.size() - 1 > index && !isNumberPile(source)) {
            legalNumberOfCards = false;
        }
        //System.out.println("canMoveToNumberPile boolean results -> " + value + " " + suit + " " + isFaceUp + " " + legalNumberOfCards);
        return (value && suit && isFaceUp && legalNumberOfCards);
    }

    /** Author STEVEN
     * Check if the index card in the source pile is allowed to be moved to the destination foundation pile.
     */
    public boolean canMoveToFoundation(CardDeck source, CardDeck destination, int index) {
        boolean legalIndex = false;
        Suit suit = source.get(index).getSuit();
        boolean matchingSuit = false;
        boolean matchingValue = false;
        boolean isFaceUp;

        //You can only move the last card in a pile to a foundation (1 at a time)
        if (source.size() - 1 == index) {
            legalIndex = true;
        }
        //Suits must match on source & destination
        if ((suit == Suit.HEARTS && destination == heartsPile)
                || (suit == Suit.SPADES && destination == spadesPile)
                || (suit == Suit.DIAMONDS && destination == diamondsPile)
                || (suit == Suit.CLUBS && destination == clubsPile)) {
            matchingSuit = true;
        }
        //Source number must be 1 higher than destination number
        if (destination.size() > 0) {
            if (source.get(index).getValue() - destination.get(destination.size() - 1).getValue() == 1) {
                matchingValue = true;
            }
        }
        //Otherwise, the source must be an Ace and the destination pile empty
        else if (destination.size() == 0) {
            if (source.get(index).getValue() == 1) {
                matchingValue = true;
            }
        }
        //Double-check that both cards are face-up.
        isFaceUp = areFaceUp(source, destination, index);

        return (legalIndex && matchingSuit && matchingValue && isFaceUp);
    }

    /** Author STEVEN
     * Simply moves a card or several from 1 deck to another. Checks for legality of moves must be done before calling
     * this function. The function moves every card from the given index to the end of the list, in order.
     */
    public void moveCardDeckToDeck(CardDeck source, CardDeck destination, int index, boolean flipFaceUp) {

        while (source.size() > index) {
            destination.add(source.get(index));
            source.remove(index);
            destination.get(destination.size() - 1).setFaceUp(flipFaceUp);
        }
    }

    /** Author ZAINAB + STEVEN
     *  Draws a card from the deck and puts it in the discard pile face-up. If the draw pile is empty, fills the draw
     *  pile with the discard pile, retaining their natural order before drawing a card again.
     */
    public void drawCard(CardDeck source, CardDeck destination) {
        int n = destination.size()-1;
        if ( source.size() == 0 ){
            while(destination.size() != 0){

                source.add(destination.get(n));
                source.get(source.size()-1).setFaceUp(false);
                destination.remove(n);
                n--;
            }
            drawCard(source, destination);

        }
        else{
            destination.add(source.get(source.size()-1));
            source.remove(source.size()-1);


            for (int i = 0; i <= destination.size()-1 ; i++){
                destination.get(i).setFaceUp(false);
            }

            destination.get(destination.size()-1).setFaceUp(true);

        }

    }

    /** Author STEVEN
     * Checks if a given card is face-up, allowing it to be moved from its pile.
     */
    public boolean areFaceUp(CardDeck source, CardDeck destination, int index) {
        return source.get(index).isFaceUp();
    }

    /** Author STEVEN
     *  Checks if a move is legal purely based on which pile is being transferred from as well as how many cards are
     *  being moved.
     */
    public boolean numberOfCardsMovedIsLegal(CardDeck source, CardDeck destination, int index) {
        boolean isLegal = false;
        //If moving 1 card, move is legal
        if (index == source.size() - 1) {
            isLegal = true;
        }
        //If moving multiple cards, both source & destination must be number piles.
        else if (isNumberPile(source) && isNumberPile(destination)) {
            isLegal = true;
        }
        return isLegal;
    }

    /** Author STEVEN
     * Helper function for determining pile type.
     */
    public boolean isNumberPile(CardDeck source) {
        return source == pile1 || source == pile2 || source == pile3 || source == pile4 || source == pile5 || source == pile6 || source == pile7;
    }
    /** Author STEVEN
     * Helper function for determining pile type.
     */
    public boolean isDiscardPile(CardDeck source) {
        return source == drawDiscard;
    }
    /** Author STEVEN
     * Helper function for determining pile type.
     */
    public boolean isDrawPile(CardDeck source) {
        return source == drawDeck;
    }
    /** Author STEVEN
     * Helper function for determining pile type.
     */
    public boolean isFoundationPile(CardDeck source) {
        return source == heartsPile || source == spadesPile || source == diamondsPile || source == clubsPile;
    }


    /** Author STEVEN
     * Allows for refering a deck by its string value. Useful for looping through the 7 number piles where the loop
     * index corresponds to the pile number.
     */
    public CardDeck getDeck(String input) {
        return switch (input) {
            case "-1", "deck" -> initialDeck;
            case "12", "draw" -> drawDeck;
            case "13", "discard" -> drawDiscard;
            case "1" -> pile1;
            case "2" -> pile2;
            case "3" -> pile3;
            case "4" -> pile4;
            case "5" -> pile5;
            case "6" -> pile6;
            case "7" -> pile7;
            case "8", "hearts" -> heartsPile;
            case "9", "spades" -> spadesPile;
            case "10", "diamonds" -> diamondsPile;
            case "11", "clubs" -> clubsPile;
            default -> null; //Asking for a non-existing pile will cause a null-pointer exception from this.
        };
    }

    /** Author STEVEN
     * Returns a nicer representation of deck names for use in TUI or debugging.
     */
    public String getDeckName(CardDeck deck) {
        if (deck.equals(initialDeck)) {return "Initial Deck";}
        else if (deck.equals(drawDeck)) {return "Draw Pile";}
        else if (deck.equals(drawDiscard)) {return "Discard Pile";}
        else if (deck.equals(pile1)) {return "Pile 1";}
        else if (deck.equals(pile2)) {return "Pile 2";}
        else if (deck.equals(pile3)) {return "Pile 3";}
        else if (deck.equals(pile4)) {return "Pile 4";}
        else if (deck.equals(pile5)) {return "Pile 5";}
        else if (deck.equals(pile6)) {return "Pile 6";}
        else if (deck.equals(pile7)) {return "Pile 7";}
        else if (deck.equals(heartsPile)) {return "Hearts Foundation";}
        else if (deck.equals(spadesPile)) {return "Spades Foundation";}
        else if (deck.equals(diamondsPile)) {return "Diamonds Foundation";}
        else if (deck.equals(clubsPile)) {return "Clubs Foundation";}
        else return "IMPOSSIBLE PILE (error)";
    }

    /** Author STEVEN
     * Creates the board using the initial card deck. Make sure the deck is shuffled beforehand.
     */
    public void initialPopulateBoard() {
        for (int i = 1; i <= 7; i++) {
            //Fills each card pile with 1-7 cards, respectively. Then it flips the last card face up.
            moveCardDeckToDeck(initialDeck, getDeck(Integer.toString(i)),
                    initialDeck.size() - (i), false);
            getDeck(Integer.toString(i)).get(i - 1).setFaceUp(true);

        }
        moveCardDeckToDeck(initialDeck, drawDeck, 0, false);
    }

    /** Author STEVEN
     * Prints out a TUI representation of the board.
     */
    public void printBoard() {
        String tab = "\t";
        String dtab = "\t\t";

        // Create new super print method with formatting
        System.out.println("DR" + tab + drawDeck.printCard(drawDeck.size()-1) + dtab + "FH" + tab + "FS" + tab + "FD" + tab + "FC");
        System.out.println("DI" + tab + drawDiscard.printCard(drawDiscard.size()-1) + dtab +heartsPile.printCard(heartsPile.size() - 1) + tab + spadesPile.printCard(spadesPile.size() - 1)
                + tab + diamondsPile.printCard(diamondsPile.size() - 1) + tab + clubsPile.printCard(clubsPile.size() - 1));
        System.out.println("P1  P2  P3  P4  P5  P6  P7");
        for (int i = 0; i < longestNumberPileLength(); i++) {
            System.out.println(pile1.printCard(i) + tab
                    + pile2.printCard(i) + tab
                    + pile3.printCard(i) + tab
                    + pile4.printCard(i) + tab
                    + pile5.printCard(i) + tab
                    + pile6.printCard(i) + tab
                    + pile7.printCard(i));
        }


    }

    /** Author STEVEN
     * Returns the length value of the longest number pile, for use in determining the size of the TUI.
     */
    public int longestNumberPileLength() {
        int l = 0;
        ArrayList<CardDeck> piles = new ArrayList<>();
        piles.add(pile1);
        piles.add(pile2);
        piles.add(pile3);
        piles.add(pile4);
        piles.add(pile5);
        piles.add(pile6);
        piles.add(pile7);
        for (CardDeck a : piles) {
            if (a.size() > l) {
                l = a.size();
            }
        }
        return l;
    }

}
