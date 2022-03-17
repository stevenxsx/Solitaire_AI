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

    public boolean attemptMove(Move move) {
        CardDeck s = move.getSourceDeck();
        CardDeck d = move.getDestinationDeck();
        int x = move.getIndex();

        //Attempts to move card(s) from number pile to number pile.
        if (isNumberPile(s) && isNumberPile(d)) {
            if (canMoveToNumberPile(s, d,x)) {
                moveCardDeckToDeck(s,d,x,true);
                return true;
            }
        }
        //Attempts to move card from number pile to foundation
        else if (isNumberPile(s) && isFoundationPile(d)) {
            if (canMoveToFoundation(s,d,x)) {
                moveCardDeckToDeck(s,d,x,true);
                return true;
            }
        }
        //Attempts to move card from foundation to number pile
        else if (isFoundationPile(s) && isNumberPile(d)) {
            if (canMoveToNumberPile(s,d,x)) {
                moveCardDeckToDeck(s,d,x,true);
                return true;
            }
        }
        //Attempts to move card from Draw to Discard
        else if (isDrawPile(s) && isDiscardPile(d)) {
            if (s.size()-1 == x) {
                moveCardDeckToDeck(s,d,x,true);
                return true;
            }
        }
        //Attempts to move card from Discard to Number pile
        else if (isDiscardPile(s) && isNumberPile(d)) {
            if (canMoveToNumberPile(s,d,x)) {
                moveCardDeckToDeck(s,d,x,true);
                return true;
            }
        }
        //Attempts to move card from Discard to Foundation
        else if (isDiscardPile(s) && isFoundationPile(d)) {
            if (canMoveToFoundation(s,d,x)) {
                moveCardDeckToDeck(s,d,x,true);
                return true;
            }
        }
        //If the move wasnt legal, and wasnt executed
        System.out.println("Illegal move sent to attemptMove():\nSource: " + s.toString() + "Destination: " + d.toString() + "Index: " + x);
        return false;
    }

    public boolean canMoveToNumberPile(CardDeck source, CardDeck destination, int index) {
        boolean value = false;
        boolean suit = false;
        boolean isFaceUp;
        boolean legalNumberOfCards = true;
        //Source number value is 1 less than destination number.
        if (destination.get(destination.size()-1).getValue() - source.get(index).getValue() == 1) {
            value = true;
        }
        //Source color is opposite of destination color
        if ((source.get(index).isRed() && destination.get(destination.size()-1).isBlack()) || (source.get(index).isBlack() && source.get(index).isRed())) {
            suit = true;
        }
        //Both cards must be face-up for the move to make any sense
        isFaceUp = areFaceUp(source,destination,index);
        //Can only move multiple cards if the source deck is a number pile.
        if (source.size()-1 > index && !isNumberPile(source)) {
            legalNumberOfCards = false;
        }

        return (value && suit && isFaceUp && legalNumberOfCards);
    }

    public boolean canMoveToFoundation(CardDeck source, CardDeck destination, int index) {
        boolean legalIndex = false;
        Suit suit = source.get(index).getSuit();
        boolean matchingSuit = false;
        boolean matchingValue = false;
        boolean isFaceUp;

        //You can only move the last card in a pile to a foundation (1 at a time)
        if (source.size()-1 == index) {legalIndex = true;}
        //Suits must match on source & destination
        if ((suit == Suit.HEARTS && destination == heartsPile)
                || (suit == Suit.SPADES && destination == spadesPile)
                || (suit == Suit.DIAMONDS && destination == diamondsPile)
                || (suit == Suit.CLUBS && destination == clubsPile))
        {matchingSuit = true;        }
        //Source number must be 1 higher than destination number
        if (destination.size() > 0) {
            if (source.get(index).getValue() - destination.get(destination.size()-1).getValue() == 1) {
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
        isFaceUp = areFaceUp(source,destination,index);

        return (legalIndex && matchingSuit && matchingValue && isFaceUp);
    }

    public void moveCardDeckToDeck(CardDeck source, CardDeck destination, int index,boolean flipFaceUp) {

        //forced move -> no check to see if its legal
        //Moves every card from the index to the end in order of index first
        while (source.size() > index) {
            destination.add(source.get(index));
            source.remove(index);
            destination.get(destination.size()-1).setFaceUp(flipFaceUp);
        }
    }
    public boolean areFaceUp(CardDeck source, CardDeck destination, int index) {
        if (source.get(index).isFaceUp() && destination.get(destination.size()-1).isFaceUp()) {
            return true;
        }
        else {
            System.out.println("Attempted to move a face-down card.");
            return false;
        }
    }

    public boolean numberOfCardsMovedIsLegal(CardDeck source, CardDeck destination, int index) {
        boolean isLegal = false;
        //If moving 1 card, move is legal
        if (index == source.size()-1) {
            isLegal = true;
        }
        //If moving multiple cards, both source & destination must be number piles.
        else if (isNumberPile(source) && isNumberPile(destination)) {
            isLegal = true;
        }
        return isLegal;
    }

    public boolean isNumberPile(CardDeck source) {
        return source == pile1 || source == pile2 || source == pile3 || source == pile4 || source == pile5 || source == pile6 || source == pile7;
    }
    public boolean isDiscardPile(CardDeck source) {
        return source == drawDiscard;
    }
    public boolean isDrawPile(CardDeck source) {
        return source == drawDeck;
    }
    public boolean isFoundationPile(CardDeck source) {
        return source == heartsPile || source == spadesPile || source == diamondsPile || source == clubsPile;
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
            getDeck(Integer.toString(i)).get(i-1).setFaceUp(true);

        }
        moveCardDeckToDeck(initialDeck,drawDeck, 0,false);
    }

    public void printBoard() {
        String sb = initialDeck.toString("Initial") +
                drawDeck.toString("Draw") +
                drawDiscard.toString("Discard") +
                pile1.toString("Pile1") +
                pile2.toString("Pile2") +
                pile3.toString("Pile") +
                pile4.toString("Pile4") +
                pile5.toString("Pile5") +
                pile6.toString("Pile6") +
                pile7.toString("Pile7") +
                heartsPile.toString("Hearts") +
                spadesPile.toString("Spades") +
                diamondsPile.toString("Diamonds") +
                clubsPile.toString("Clubs");
        System.out.println(sb);

        // Create new super print method with formatting


    }

}
