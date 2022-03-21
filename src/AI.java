import java.util.ArrayList;
import java.util.Comparator;

public class AI {
    public ArrayList<Move> movesList;
    public Board board;
    public boolean gameIsLost = false;
    public boolean gameIsWon = false;

    public AI(Board board) {
        this.board = board;
        this.movesList = new ArrayList<>();
    }


    /* AI functions
     All functions must find all candidate cards that apply, then transfer the candidate from the pile with the most downcards
     Search for Ace in number piles and move to foundation
     Search for Deuce in number piles and move to foundation
     Search for transferable face-up card(s) that will free a face-down card
         If search returns multiple options select pile with most face-down cards
     Search for transferable face-up card(s) that will clear a space
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

    */


    public void executeTurn() {
        //remove all elements from previous AI call, if any.
        movesList.clear();
        scanForMoveType1();
        scanForMoveType2();
        scanForMoveType3();
        scanForMoveType4();
        scanForMoveType5();
        scanForMoveType6();
        scanForMoveType7();
        if (!movesList.isEmpty()) {
            board.attemptMove(movesList.get(0));
        }
        else {
            System.out.println("no moves found");
        }
    }

    public void addCandidateMoves(ArrayList<Move> candidates, int moveType) {
        // If there are candidates, sort them and add to the list
        if (!candidates.isEmpty()) {
            candidateSorter(candidates);
            movesList.addAll(candidates);
            System.out.println("Move type " + moveType + " detected.");
        }
    }

    public void checkIfGameIsWon() {
        //for-loop that checks if everything is face-up
        //if so, -> gameIsWon = true
    }

    public void noMovesFound() {
        //end the game :(
    }

    // Search for Aces in number piles and move best candidate to foundation
    // Author SIMON + STEVEN
    private void scanForMoveType1() {
        // Initialize list of candidate card.
        ArrayList<Move> candidates = new ArrayList<>();

        // Scan number piles
        for (int i = 1; i <= 7; i++) {
            // Number pile variables
            CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
            int sourceTopCardIndex = sourceDeck.getBottomFaceCardIndex();
            Card sourceTopCard = sourceDeck.get(sourceTopCardIndex);

            // Ace pile variables
            CardDeck destinationDeck;

            // Identify destionation Ace pile. Only runs if source card is an Ace.
            // Default is just an empty deck.
            if (sourceTopCard.getValue() == 1) {

                switch (sourceTopCard.getSuit()) {
                    case HEARTS -> destinationDeck = board.heartsPile;
                    case SPADES -> destinationDeck = board.spadesPile;
                    case DIAMONDS -> destinationDeck = board.diamondsPile;
                    case CLUBS -> destinationDeck = board.clubsPile;
                    default -> destinationDeck = board.initialDeck;
                }

                candidates.add((new Move(sourceDeck, destinationDeck, sourceTopCardIndex)));

            }
        }
        addCandidateMoves(candidates,1);
    }

    // Search for Deuces in number piles and move best candidate to foundation
    // Author SIMON
    private void scanForMoveType2() {

        // Initialize list of candidate card.
        ArrayList<Move> candidates = new ArrayList<>();

        // Scan number piles
        for (int i = 1; i <= 7; i++) {
            // Number pile variables
            CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
            int sourceTopCardIndex = sourceDeck.getBottomFaceCardIndex();
            Card sourceTopCard = sourceDeck.get(sourceTopCardIndex);

            // Ace pile variables
            CardDeck destinationDeck;

            // Identify destionation Ace pile. Only runs if source card is a deuce.
            // Default is just so deck is a non-empty, non-ace deck.
            if (sourceTopCard.getValue() == 2) {

                switch (sourceTopCard.getSuit()) {
                    case HEARTS -> destinationDeck = board.heartsPile;
                    case SPADES -> destinationDeck = board.spadesPile;
                    case DIAMONDS -> destinationDeck = board.diamondsPile;
                    case CLUBS -> destinationDeck = board.clubsPile;
                    default -> destinationDeck = sourceDeck;
                }

                // Check if the top card of the destionation pile is an ace, if so add candidate
                if (board.canMoveToFoundation(sourceDeck, destinationDeck, sourceTopCardIndex)) {
                    candidates.add((new Move(sourceDeck, destinationDeck, sourceTopCardIndex)));

                }
            }
        }
        addCandidateMoves(candidates,2);
    }

    // Search for transferable face-up card(s) that will free a face-down card (the second to last card in pile is face-down)
    // Author SIMON
    private void scanForMoveType3() {

        // Initialize list of candidate card.
        ArrayList<Move> candidates = new ArrayList<>();

        // Search all number piles
        for (int i = 1; i <= 7; i++) {
            // Number pile variables
            CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
            int sourceTopCardIndex = sourceDeck.getBottomFaceCardIndex();
            Card sourceTopCard = sourceDeck.get(sourceTopCardIndex);

            // Check if transfering a card will free a down card
            if (sourceDeck.canFreeDownCard()) {
                CardDeck destinationDeck;

                // Check if top-card can be placed anywhere in the piles
                for (int j = 1; j <= 7; j++) {
                    destinationDeck = this.board.getDeck(Integer.toString(j));

                    if (board.canMoveToNumberPile(sourceDeck, destinationDeck, sourceTopCardIndex)) {

                        candidates.add((new Move(sourceDeck, destinationDeck, sourceTopCardIndex)));
                    }
                }
                // Check if top-card can be placed on Ace stack
                switch (sourceTopCard.getSuit()) {

                    case HEARTS -> destinationDeck = this.board.heartsPile;
                    case SPADES -> destinationDeck = this.board.spadesPile;
                    case DIAMONDS -> destinationDeck = this.board.diamondsPile;
                    case CLUBS -> destinationDeck = this.board.clubsPile;
                    default -> destinationDeck = board.initialDeck;
                }

                if (board.canMoveToFoundation(sourceDeck, destinationDeck, sourceTopCardIndex)) {
                    candidates.add((new Move(sourceDeck, destinationDeck, sourceTopCardIndex)));
                }
            }
            addCandidateMoves(candidates,3);
        }
    }

    // Search for transferable face-up card(s) that will clear a space
    // Author: SIMON

    private void scanForMoveType4() {

        // Initialize variables
        ArrayList<Card> kings = new ArrayList<>();
        ArrayList<Move> candidates= new ArrayList<>();

        // Check each deck for a transferable king.
        for (int k = 1; k <= 7; k++) {
            CardDeck possibleKingDeck = this.board.getDeck(Integer.toString(k));
            kings.addAll(possibleKingDeck.getAllCardsOfValue(13));
        }

        // Check draw deck for a transferable king.
        kings.addAll(this.board.drawDeck.getAllCardsOfValue(13));

        // Check draw discard deck for a transferable king.
        kings.addAll(this.board.drawDiscard.getAllCardsOfValue(13));

        // If the candidate list is not empty we look moves that will clear a space.
        if (!kings.isEmpty()) {
            // Search for a clearable space
            for (int c = 1; c <= 7; c++) {
                CardDeck clearableDeck = this.board.getDeck(Integer.toString(c));
                isDeckClearable(clearableDeck);
                candidates.addAll(getTransferCandidates(clearableDeck, 0));
            }
        }

        // If we found clearable decks it can be assued that we also have transferable kings
        if (candidates != null){
            // If clearing the deck allows for a legal king move according to our strategy we are happy
            if (clearIsLegal(kings)){
                addCandidateMoves(candidates,4);
            }
        }
    }

    //TODO Person currently working:
    // Search for a pile that can be smoothed
    //Author: ZAINAB
    private void scanForMoveType5() {
        /*
        //        EITHER a top card or a group of cards that can be transfered to make a pile smooth


        // Initialize list of candidate card.
        ArrayList<Move> candidates = new ArrayList<>();
        // Scan number piles

        for (int i = 1; i <= 7; i++) {
                // Number pile variables
                CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
                int sourceTopCardIndex = sourceDeck.getBottomFaceCardIndex();
                Card sourceTopCard = sourceDeck.get(sourceTopCardIndex);
                //smoothing is most important for numbers from 5-8
                if( sourceTopCard.getValue() == 5 ||
                    sourceTopCard.getValue() == 6 ||
                    sourceTopCard.getValue() == 7 ||
                    sourceTopCard.getValue() == 8 ){
                    // Check if top-card can be placed anywhere in the piles
                    for (int j = 1; j <= 7; j++) {

                        CardDeck destinationDeck;
                        destinationDeck = this.board.getDeck(Integer.toString(j));
                        int destTopCardIndex = destinationDeck.getBottomFaceCardIndex();
                        Card destTopCard = destinationDeck.get(destTopCardIndex);
                        //card before the top card
                        Card predestTopCard = destinationDeck.get(destTopCardIndex-1);

                        if (board.canMoveToNumberPile(sourceDeck, destinationDeck, sourceTopCardIndex)) {
                            //check if move is smooth (compare source card with with it's next highest same-color partner in the column)
                            if(sourceTopCard.getSuit() == predestTopCard.getSuit())
                                candidates.add((new Move(sourceDeck, destinationDeck, sourceTopCardIndex)));
                        }
                }
            }
        }
        */
    }

    //TODO Person currently working:
    //  Search for cards that can be played from the Deck then play them
    // Author : Zainab - IN PROGRESS
    private void scanForMoveType6() {

        // Initialize list of candidate card.
        ArrayList<Move> candidates = new ArrayList<>();

        // Number pile variables
        CardDeck sourceDeck = this.board.getDeck("12");

        for (int i = 0; i <= sourceDeck.size()-1; i++) {

                // Check if top-card can be placed anywhere in the piles
                for (int j = 1; j <= 7; j++) {

                    CardDeck destinationDeck;
                    destinationDeck = this.board.getDeck(Integer.toString(j));
                    int destTopCardIndex = destinationDeck.getBottomFaceCardIndex();
                    Card destTopCard = destinationDeck.get(destTopCardIndex);

                    if (board.canMoveToNumberPile(sourceDeck, destinationDeck, i)) {
                        candidates.add((new Move(sourceDeck, destinationDeck, i)));
                    }
                }
        }
        if (!candidates.isEmpty()) {
            candidateSorter(candidates);
            // executeBestCandidate(candidates.get(candidates.size()-1)); Uncomment when function parameters are refactored.
        }


    }

    //TODO Person currently working:
    //  Search for cards that can move to the foundation, check that it is safe, then move.
    private void scanForMoveType7() {
        // TODO: Copy to where applicable then delete.
        /*// If yes we need to ensure that the next card is protected.
        // Initialize helper variables
        boolean canMove = false;
        int nextCardsPlayed = 0;

        // Go through all the piles
        for (int j = 1; j <= 11; i++) {
            CardDeck deck = this.board.getDeck(Integer.toString(i));
            int deckTopCardIndex = deck.getBottomFaceCardIndex();
            Card deckTopCard = deck.get(deckTopCardIndex);

            System.out.println("Debug - sourceDeck: " + sourceDeck + ", sourceTopCardIndex: " + sourceTopCardIndex + ", sourceTopCard: " + sourceTopCard);

            // if the same color (not suit!) + same number card is a topcard on a number pile
            // the card can be moved
            if (j <= 7 && sameValueSameColor(sourceTopCard, deckTopCard)) {

                System.out.println(sourceTopCard + " & " + deckTopCard + "have the same value and sout color!");

                canMove = true;

                System.out.println(sourceTopCard + "canMove: " + canMove);

                break;
            }
            // Or if both not-color cards are have already been played the card can be moved

            System.out.println("Checking if the next lower card of opposite color to " + sourceTopCard + "has been played");

            nextCardsPlayed += nextCardsPlayed(sourceTopCard, deck);

            System.out.println("It has! Number of next cards played is: "+ nextCardsPlayed);

            if (nextCardsPlayed == 2) {

                System.out.println(nextCardsPlayed + " next cards have been played.");

                canMove = true;

                System.out.println(sourceTopCard + "canMove: " + canMove);

                break;
            }
        }
        // If the card is safe to move add it to the candidates list.
        if (canMove) {
            candidates.add((new Move(sourceDeck, destinationDeck, sourceTopCardIndex)));

            System.out.println("Debug - " + sourceTopCard + " to foundation move added to candidates: " + candidates);
        }
         */

    }

    public void validateCandidates1() {

    }

    public void validateCandidates2() {

    }

    public void validateCandidates3() {

    }

    public void validateCandidates4() {

    }

    public void validateCandidates5() {

    }

    public void validateCandidates6() {

    }

    // Sorts a list of candidates in ascending order
    // Author SIMON
    private void candidateSorter(ArrayList<Move> candidates) {
        candidates.sort(
                Comparator.comparing(
                        c -> c.getSourceDeck().getNumberOfFaceDownCards()
                )
        );
    }

    // Same as candidateSorter but for decks
    private void candidateDeckSorter(ArrayList<CardDeck> candidates) {
        candidates.sort(
                Comparator.comparing(
                        c -> c.getNumberOfFaceDownCards()
                )
        );
    }

    private boolean clearDeckOkay(Move move) {
        // 1. The calling function should  iterate through the list of candidates starting with the last index

        // 2. Before continuing the function should call clearDeckOkay(move) and ensure that the move is desirable
        // 3. If move is desirable call executeBestCandidate() and terminate iteration
        // 5. Else continue iteration to next candidate
        return false;
    }

    // Check if two cards have the same value and color (but not the same suit!)
    // Author SIMON
    private boolean sameValueSameColor(Card sourceTopCard, Card deckTopCard) {
        return sourceTopCard.getValue() == deckTopCard.getValue() &&
                sourceTopCard.getSuit() != deckTopCard.getSuit() &&
                (
                        (sourceTopCard.isBlack() && deckTopCard.isBlack()) ||
                                (sourceTopCard.isRed() && deckTopCard.isRed())
                );
    }

    // Check how many times the next playable card of a parameter card has been played in a deck
    // Author SIMON
    private int nextCardsPlayed(Card sourceTopCard, CardDeck deck) {
        ArrayList<Card> faceUpCards = deck.getFaceUpCards();
        int targetValue = sourceTopCard.getValue() - 1;
        boolean targetBlack = !sourceTopCard.isBlack();
        int numberOfCardsPlayed = 0;

        for (Card card : faceUpCards) {
            if (
                    card.getValue() == targetValue &&
                            card.isBlack() == targetBlack
            ) {
                numberOfCardsPlayed++;
            }
        }
        return numberOfCardsPlayed;
    }

    private boolean isDeckClearable(CardDeck deck) {
        return deck.getNumberOfFaceDownCards() == 0;
    }



    private ArrayList<Move> getTransferCandidates(CardDeck sourceDeck, int index) {
        // Check if top-card can be placed anywhere in the piles
        CardDeck destinationDeck;
        Card sourceCard = sourceDeck.get(index);
        ArrayList<Move> candidates = new ArrayList<>();
        for (int t = 1; t <= 7; t++) {
            destinationDeck = this.board.getDeck(Integer.toString(t));

            //System.out.println("Debug - Checking if " + sourceCard + " can be placed on " + destinationDeck);

            if (board.canMoveToNumberPile(sourceDeck, destinationDeck, index)) {

                candidates.add((new Move(sourceDeck, destinationDeck, index)));

                //System.out.println("Debug - " + sourceDeck.get(index) + "can be placed on " + destinationDeck + "; move added to candidates: " + candidates);
            }
        }
        // Check if top-card can be placed on Ace stack
        switch (sourceCard.getSuit()) {

            case HEARTS -> destinationDeck = this.board.heartsPile;
            case SPADES -> destinationDeck = this.board.spadesPile;
            case DIAMONDS -> destinationDeck = this.board.diamondsPile;
            case CLUBS -> destinationDeck = this.board.clubsPile;
            default -> destinationDeck = board.initialDeck;
        }

        //System.out.println("Debug - Checking if" + sourceCard + "can be placed on foundation: " + destinationDeck);

        if (board.canMoveToFoundation(sourceDeck, destinationDeck, index)) {
            candidates.add((new Move(sourceDeck, destinationDeck, index)));

            //System.out.println("Debug - Deuce to foundation move added to candidates: " + candidates);

        }
        return candidates;
    }

    private boolean clearIsLegal(ArrayList<Card> kings){
        ArrayList<CardDeck> decks = new ArrayList<>();
        //can playing the king free up a downcard?
        // i.e.
        // either king is bottom face-up card
        for (int k = 1; k <= 7; k++){
            CardDeck deck = this.board.getDeck(Integer.toString(k));
            int sourceTopCardIndex = deck.getBottomFaceCardIndex();
            Card sourceTopCard = deck.get(sourceTopCardIndex);
            decks.add(deck);
            if(sourceTopCard.getValue() == 13 && deck.getNumberOfFaceDownCards() != 0){
                return true;
            }
            if (
                    deck.canFreeDownCard() &&
                    sourceTopCard.getValue() == 12
            ){
                for (Card king: kings){
                    if ( king.isBlack() != sourceTopCard.isBlack()) {
                        return true;
                    }
                }
            }
        }

        // ELSE IF will this play benefit the pile with most downcards?
        // (i.e. at some point a transfer can be made to the new pile
        // and down-face card can be free)
        candidateDeckSorter(decks);
        CardDeck mostDownCards = decks.get(decks.size()-1);
        int topCardIndex = mostDownCards.getBottomFaceCardIndex();
        Card topCard = mostDownCards.get(topCardIndex);

        for(Card king : kings){
            if (
                    king.isBlack() != topCard.isBlack() &&
                            (
                                topCard.getValue() == 10 ||
                                topCard.getValue() == 8 ||
                                topCard.getValue() == 6 ||
                                topCard.getValue() == 4 ||
                                topCard.getValue() == 2
                            )
            ) {
                return true;
            }

            if (
                    king.isBlack() == topCard.isBlack() &&
                            (
                                    topCard.getValue() == 11 ||
                                    topCard.getValue() == 9 ||
                                    topCard.getValue() == 7 ||
                                    topCard.getValue() == 5 ||
                                    topCard.getValue() == 3 ||
                                    topCard.getValue() == 1
                            )
            ) {
                return true;
            }
        }
        return false;
    }
}
