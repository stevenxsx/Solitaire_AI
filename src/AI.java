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


    /**
     * AI functions
     * All functions must find all candidate cards that apply, then transfer the candidate from the pile with the most downcards
     * Search for Ace in number piles and move to foundation
     * Search for Deuce in number piles and move to foundation
     * Search for transferable face-up card(s) that will free a face-down card
     * If search returns multiple options select pile with most face-down cards
     * Search for transferable face-up card(s) that will clear a space
     * IF yes is a king playable?
     * IF yes can playing the king free up a downcard? (i.e. allow transfer of a queen, or king is bottom face-up card)
     * ELSE IF will this play benefit the pile with most downcards? (i.e. same color)
     * IF yes play then transfer card
     * THEN play king
     * <p>
     * <p>
     * Search for any card that can be transfered to an Ace-stack
     * Vet candidates
     * Keep candidate
     * IF its same color twin is on the board (e.g. keep 3D if 3H is in play)
     * OR if it's not-same color successors are on the board (e.g. keep 3D if both 2C & 2S are in play)
     * AND the play will free a downcard
     * Nice to have smarty pants AI option: OR if the subsequent play will free a downcard
     * OR it will clear a spot for a waiting king (remember to call "should I clear a space?" function)
     * Search for a pile that can be smoothed
     * EITHER a top card or a group of cards that can be transfered to make a pile smooth
     * Search for cards that can be played from the Deck then play them
     * Another nice to have smarty pants AI option: Try to transfer cards/piles to open up playing cards from the dack
     * IF no plays can be made, flip the table and rage quit!
     * <p>
     * In a given turn, the AI will for every type of move in the hierarchy scan the whole board for moves of that type.
     * If a move or more are found, they will be added to a 'candidate list' that will be processed after all moves of
     * that type have been found, with the process eliminating the sub-optimal moves in favor of the best one. If no
     * moves of a given type were found, the algorithm will proceed to the next type of move. The hierarchy should
     * therefore include a scan-type function and validation-type function for every type of move. Lastly, some function
     * should be created to handle no possible moves, ending game, etc.
     * <p>
     * ExecuteTurn()
     * ScanForMoveType1()
     * AddCandidatesToList(Move object)
     * ValidateCandidates(ListOfMoves)
     * ExecuteBestCandidate(Move)
     * call ScanForMoveType2()
     * <p>
     * repeat sequence for moveTypes all the way down
     * <p>
     * NoMovesFound()
     * EndGame()
     **/


    public void executeTurn() {

        //needs to break the sequence if a move is executed. check between every type of move.
        scanForMoveType1();
        scanForMoveType2();
        scanForMoveType3();
        scanForMoveType4();
        scanForMoveType5();
        scanForMoveType6();
        checkIfGameIsWon();
        noMovesFound();

    }

    public void checkIfGameIsWon() {
        //for-loop that checks if everything is face-up
        //if so, -> gameIsWon = true
    }

    public void noMovesFound() {
        //end the game :(
    }

    public void addCandidatesToList(Move move) {
        movesList.add(move);
    }

    public void executeBestCandidate() {
        //Feed it one move somehow
    }

    // Search for Aces in number piles and move best candidate to foundation
    // Author SIMON
    private void scanForMoveType1() {
        // Initialize list of candidate card.
        ArrayList<Move> candidates = new ArrayList<>();

        // Scan number piles
        for (int i = 1; i <= 7; i++) {
            try {
                // Number pile variables
                CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
                int sourceTopCardIndex = sourceDeck.getBottomFaceCardIndex();
                Card sourceTopCard = sourceDeck.get(sourceTopCardIndex);

                // Ace pile variables
                CardDeck destinationDeck;

                // Identify destionation Ace pile. Only runs if source card is an Ace.
                // Default is just an empty deck.
                if ( sourceTopCard.getValue() == 1){
                    switch (sourceTopCard.getSuit()){
                        case HEARTS -> destinationDeck = board.heartsPile;
                        case SPADES -> destinationDeck = board.spadesPile;
                        case DIAMONDS -> destinationDeck = board.diamondsPile;
                        case CLUBS -> destinationDeck = board.clubsPile;
                        default -> {
                            destinationDeck = board.initialDeck;
                            System.out.println("No pile found, defaulted to Initial Deck");
                        }
                    }
                    candidates.add((new Move(sourceDeck, destinationDeck, sourceTopCardIndex)));
                }
            } catch (Exception e) {
                e.printStackTrace(); // Strings are hardcorded so this is used for bughunting
            }
        }
        // If there are candidates execute the move with the most downcards.
        if (!candidates.isEmpty()) {
            candidateSorter(candidates);
            // executeBestCandidate(candidates.get(candidates.size()-1)); Uncomment when function parameters are refactored.
        }
    }

    // Search for Deuces in number piles and move best candidate to foundation
    // Author SIMON
    private void scanForMoveType2() {

        // Initialize list of candidate card.
        ArrayList<Move> candidates = new ArrayList<>();

        // Scan number piles
        for (int i = 1; i <= 7; i++) {
            try {
                // Number pile variables
                CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
                int sourceTopCardIndex = sourceDeck.getBottomFaceCardIndex();
                Card sourceTopCard = sourceDeck.get(sourceTopCardIndex);

                // Ace pile variables
                CardDeck destinationDeck;
                Card ace = new Card(sourceTopCard.getSuit(), 1);
                int destionationTopCardIndex;

                // Identify destionation Ace pile. Only runs if source card is a deuce.
                // Default is just so deck is a non-empty, non-ace deck.
                if ( sourceTopCard.getValue() == 2){
                    switch (sourceTopCard.getSuit()){
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
            } catch (Exception e) {
                e.printStackTrace(); // Strings are hardcorded so this is used for bughunting
            }
        }
        // If there are candidates execute the move with the most downcards.
        if (!candidates.isEmpty()) {
            candidateSorter(candidates);
            // Execute best candidate move.
            // executeBestCandidate(candidates.get(candidates.size()-1)); Uncomment when function parameters are refactored.
        }
    }

    // Search for transferable face-up card(s) that will free a face-down card (the second to last card in pile is face-down)
    // Author SIMON
    private void scanForMoveType3() {
        /*
        // Initialize list of candidate card.
        ArrayList<Move> candidates = new ArrayList<>();

        // Search all number piles
        for (int i = 1; i <= 7; i++) {
            try {
                // Number pile variables
                CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
                int sourceTopCardIndex = sourceDeck.getBottomFaceCardIndex();
                Card sourceTopCard = sourceDeck.get(sourceTopCardIndex);

                // Check if transfering a card will free a down card
                if (sourceDeck.canFreeDownCard()) {
                    CardDeck destinationDeck;

                    // Check if top-card can be placed anywhere in the piles
                    for (int j = 1; j <= 7; i++) {
                        destinationDeck = this.board.getDeck(Integer.toString(j));
                        if (board.canMoveToNumberPile(sourceDeck, destinationDeck, sourceTopCardIndex)) {
                            candidates.add((new Move(sourceDeck, destinationDeck, sourceTopCardIndex)));
                        }
                    }
                    // Check if top-card can be placed on Ace stack
                    switch (sourceTopCard.getSuit()) {
                        case HEARTS -> destinationDeck = this.board.getDeck("heartsPile");
                        case SPADES -> destinationDeck = this.board.getDeck("spadesPile");
                        case DIAMONDS -> destinationDeck = this.board.getDeck("diamondsPile");
                        case CLUBS -> destinationDeck = this.board.getDeck("clubsPile");
                        default -> {
                            destinationDeck = board.initialDeck;
                            System.out.println("Impossible pile error");
                        }
                    }
                    if (board.canMoveToFoundation(sourceDeck, destinationDeck, sourceTopCardIndex)) {
                        // If yes we need to ensuer that the next card is protected.
                        // Initialize helper variables
                        boolean canMove = false;
                        int nextCardsPlayed = 0;

                        // Go through all the piles
                        for (int j = 1; j <= 11; j++) {
                            CardDeck deck = this.board.getDeck(Integer.toString(i));
                            int deckTopCardIndex = deck.getBottomFaceCardIndex();
                            Card deckTopCard = deck.get(deckTopCardIndex);

                            // if the same color (not suit!) + same number card is a topcard on a number pile
                            // the card can be moved
                            if (j <= 7 && sameValueSameColor(sourceTopCard, deckTopCard)) {
                                canMove = true;
                                break;
                            }
                            // Or if both not-color cards are have already been played the card can be moved
                            nextCardsPlayed += nextCardsPlayed(sourceTopCard, deck);
                            if (nextCardsPlayed == 2) {
                                canMove = true;
                                break;
                            }
                        }
                        // If the card is safe to move add it to the candidates list.
                        if (canMove) {
                            candidates.add((new Move(sourceDeck, destinationDeck, sourceTopCardIndex)));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // If there are candidates execute the move with the most downcards.
            if (!candidates.isEmpty()) {
                candidateSorter(candidates);
                // Execute best candidate move.
                // executeBestCandidate(candidates.get(candidates.size()-1)); Uncomment when function parameters are refactored.
            }
        }
        */
    }

    //TODO Person currently working: Simon
    // Search for transferable face-up card(s) that will clear a space
    // Play King if applicable
    // Author: SIMON
    private void scanForMoveType4() {
        /*
        // Initialize variables
        ArrayList<CardDeck> candidateKings = new ArrayList<>();

        ArrayList<Card> drawDeck = this.board.drawDeck.getCards();
        ArrayList<Card>  drawDiscard = this.board.drawDiscard.getCards();

        ArrayList<CardDeck> mostDownCardsCandidates = new ArrayList<>();
        CardDeck candidateDeck;
        int candidateTopCardIndex;
        Card candidateTopCard;

        // Are one or more kings playable?
        for (int k = 1; k <= 7; k++) {
            CardDeck kingDeck = this.board.getDeck(Integer.toString(k));
            mostDownCardsCandidates.add(kingDeck);
            int kingCardIndex = kingDeck.getBottomFaceCardIndex();
            Card kingCard = kingDeck.get(kingCardIndex);
            if(kingCard.getValue() == 13){
                candidateKings.add(kingDeck);
            }
        }
        if (!candidateKings.isEmpty()){
            // Search for a clearable space
            for (int c = 1; c <= 7; c++) {
                CardDeck clearableDeck = this.board.getDeck(Integer.toString(c));

            }
        }

        candidateDeckSorter(mostDownCardsCandidates);
        candidateDeck = mostDownCardsCandidates.get(mostDownCardsCandidates.size()-1);
        candidateTopCardIndex = candidateDeck.getBottomFaceCardIndex();
        candidateTopCard = candidateDeck.get(candidateTopCardIndex);

        for (Card drawCard : drawDeck){
            if(drawCard.getValue() == 13){
                candidateKings.add(drawCard);
            }
        }
        for (Card drawCard : drawDiscard){
            if(drawCard.getValue() == 13){
                candidateKings.add(drawCard);
            }
        }

        if (!candidateKings.isEmpty()){
            for (Card king : candidateKings){

                // If yes can playing the king free up a downcard? (i.e. allow transfer of a queen)
                for (int q = 1; q <= 7; q++) {
                    CardDeck queenDeck = this.board.getDeck(Integer.toString(q));
                    int queenCardIndex = queenDeck.getBottomFaceCardIndex();
                    Card queenCard = queenDeck.get(queenCardIndex);
                    if(
                            queenDeck.canFreeDownCard() &&
                            queenCard.getValue() == 12 &&
                            king.isBlack() != queenCard.isBlack()
                    ){
                        finalKings.add(king);
                    }
                    // Else will this play benefit the pile with most downcards?
                    // i.e. can it at some point be transferred to the new king pile?
                    else if (
                            king.isBlack() != candidateTopCard.isBlack() &&
                            (
                                candidateTopCard.getValue() == 10 ||
                                candidateTopCard.getValue() == 8 ||
                                candidateTopCard.getValue() == 6 ||
                                candidateTopCard.getValue() == 4 ||
                                candidateTopCard.getValue() == 2
                            )
                    ){
                        finalKings.add(king);
                    }
                    else if (
                            king.isBlack() == candidateTopCard.isBlack() &&
                                (
                                    candidateTopCard.getValue() == 11 ||
                                    candidateTopCard.getValue() == 9 ||
                                    candidateTopCard.getValue() == 7 ||
                                    candidateTopCard.getValue() == 5 ||
                                    candidateTopCard.getValue() == 3 ||
                                    candidateTopCard.getValue() == 1
                                )
                    ){
                        finalKings.add(king);
                    }
                }
            }
        }
        //
        //                IF yes play then transfer card
        //
        */
    }

    //TODO Person currently working:
    // Search for a pile that can be smoothed
    //Author: ZAINAB
    private void scanForMoveType5() {
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

    }

    //TODO Person currently working:
    //  Search for cards that can be played from the Deck then play them
    //Author: ZAINAB - IN PROGRESS
    private void scanForMoveType6() {/*

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

        }*/

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
        if (
                sourceTopCard.getValue() == deckTopCard.getValue() &&
                        sourceTopCard.getSuit() != deckTopCard.getSuit() &&
                        (
                                (sourceTopCard.isBlack() && deckTopCard.isBlack()) ||
                                        (sourceTopCard.isRed() && deckTopCard.isRed())
                        )
        ) {
            return true;
        } else return false;
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
}
