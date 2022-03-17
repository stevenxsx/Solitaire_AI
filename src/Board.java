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

    public boolean canMoveToNumberPile(Card source, Card destination) {
        boolean value = false;
        boolean suit = false;
        if (destination.getValue() - source.getValue() == 1) {
            value = true;
        }
        if ((source.isRed() && destination.isBlack()) || (source.isBlack() && source.isRed())) {
            suit = true;
        } {

        }
        return (value && suit);
    }

    /** AI functions
     All functions must find all candidate cards that apply, then transfer the candidate from the pile with the most downcards
     SIMON -- Search for Ace in number piles and move to foundation
     SIMON -- Search for Deuce in number piles and move to foundation
     SIMON -- Search for transferable face-up card(s) that will free a face-down card
         If search returns multiple options select pile with most face-down cards
     SIMON -- Search for transferable face-up card(s) that will clear a space
        IF yes is a king playable?
            IF yes can playing the king free up a downcard? (i.e. allow transfer of a queen)
            ELSE IF will this play benefit the pile with most downcards? (i.e. same color)
                IF yes play then transfer card
                THEN play king
     Search for any card that can be transfered to an Ace-stack
         Vet candidates
             Keep candidate
                 IF its same color twin is on the board (e.g. keep 3D if 3H is in play)
                 OR if it's not-same color successors are on the board (e.g. keep 3D if both 2C & 2S are in play)
                     AND the play will free a downcard
                     Nice to have smarty pants AI option: OR if the subsequent play will free a downcard
                     OR it will clear a spot for a waiting king (remember to call "should I clear a space?" function)
     Search for a pile that can be smoothed
        EITHER a top card or a group of cards that can be transfered to make a pile smooth
    Search for cards that can be played from the Deck then play them
    Another nice to have smarty pants AI option: Try to transfer cards/piles to open up playing cards from the dack
    IF no plays can be made, flip the table and rage quit!

    In a given turn, the AI will for every type of move in the hierarchy scan the whole board for moves of that type.
     If a move or more are found, they will be added to a 'candidate list' that will be processed after all moves of
     that type have been found, with the process eliminating the sub-optimal moves in favor of the best one. If no
     moves of a given type were found, the algorithm will proceed to the next type of move. The hierarchy should
     therefore include a scan-type function and validation-type function for every type of move. Lastly, some function
     should be created to handle no possible moves, ending game, etc.

     ExecuteTurn()
        ScanForMoveType1()
            AddCandidatesToList(Move object)
        ValidateCandidates(ListOfMoves)
            ExecuteBestCandidate(Move)
        call ScanForMoveType2()

     repeat sequence for moveTypes all the way down

     NoMovesFound()
        EndGame()

    **/

    public boolean canMoveToFoundation(CardDeck source, CardDeck destination, int index) {
        boolean legalIndex = false;
        Suit suit = source.get(index).getSuit();
        boolean matchingSuit = false;
        boolean matchingValue = false;

        if (source.size()-1 == index) {legalIndex = true;}
        if ((suit == Suit.HEARTS && destination == heartsPile)
        || (suit == Suit.SPADES && destination == spadesPile)
        || (suit == Suit.DIAMONDS && destination == diamondsPile)
        || (suit == Suit.CLUBS && destination == clubsPile))
        {matchingSuit = true;        }
        if (destination.size() > 0) {
            if (source.get(index).getValue() - destination.get(destination.size()-1).getValue() == 1) {
                matchingValue = true;
            }
        }
        else if (destination.size() == 0) {
            if (source.get(index).getValue() == 1) {
                matchingValue = true;
            }
        }
        return (legalIndex && matchingSuit && matchingValue);
    }

    public void moveCardDeckToDeck(CardDeck source, CardDeck destination, int index,boolean flipFaceUp) {

        //forced move -> no check to see if its legal
        while (source.size() > index) {
            destination.add(source.get(index));
            source.remove(index);
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
        moveCardDeckToDeck(initialDeck,drawDeck, 0,false);
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
