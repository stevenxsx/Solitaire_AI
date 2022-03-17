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

    public void executeTurn() {

        //needs to break the sequence if a move is executed. check between every type of move.
        scanForMoveType1();

        scanForMoveType2();

        scanForMoveType3();

        scanForMoveType4();
        validateCandidates4();
        executeBestCandidate();

        scanForMoveType5();
        validateCandidates5();
        executeBestCandidate();

        scanForMoveType6();
        validateCandidates6();
        executeBestCandidate();

        scanForMoveType7();
        validateCandidates7();
        executeBestCandidate();

        scanForMoveType8();
        validateCandidates8();
        executeBestCandidate();

        scanForMoveType9();
        validateCandidates9();
        executeBestCandidate();

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
        // 1. The function should receive all candidates
        // 2. The function should iterate through the list starting with the last index
        // 3. Before continuing the function should call clearDeckOkay(move) and ensure that the move is desirable
        // 4. If move is desirable execute and terminate iteration
        // 5. Else continue iteration to next candidate
        //Feed it one move somehow <- I disagree; see above :)
    }

    // Search for Aces in number piles and move best candidate to foundation
    private void scanForMoveType1() {
        // Initialize list of candidate card.
        ArrayList<Move> candidates = new ArrayList<>();

        // Scan number piles
        for(int i = 1; i <= 7; i++){
            try {
                // Number pile variables
                CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
                int sourceTopCardIndex = sourceDeck.getTopCardIndex();
                Card sourceTopCard = sourceDeck.get(sourceTopCardIndex);

                // Ace pile variables
                CardDeck destinationDeck;
                
                // Identify destionation Ace pile. Only runs if source card is an Ace.
                // Default is just an empty deck.
                if ( sourceTopCard.getValue() == 1){
                    switch (sourceTopCard.getSuit()){
                        case HEARTS -> destinationDeck = this.board.getDeck("heartsPile");
                        case SPADES -> destinationDeck = this.board.getDeck("spadesPile");
                        case DIAMONDS -> destinationDeck = this.board.getDeck("diamondsPile");
                        case CLUBS -> destinationDeck = this.board.getDeck("clubsPile");
                        default -> destinationDeck = new CardDeck();
                    }
                    candidates.add((new Move(sourceDeck, destinationDeck, sourceTopCardIndex)));
                }
            } catch (Exception e) {
                e.printStackTrace(); // Strings are hardcorded so this is used for bughunting
            }
        }
        // Sort candidates, if any, in ascending order
        if (!candidates.isEmpty()) {
            candidateSorter(candidates);
            // executeBestCandidate(candidates.get(candidates.size()-1)); Uncomment when function parameters are refactored.
        }
    }

    // Search for Deuces in number piles and move best candidate to foundation
    private void scanForMoveType2() {
        // Initialize list of candidate card.
        ArrayList<Move> candidates = new ArrayList<>();

        // Scan number piles
        for(int i = 1; i <= 7; i++){
            try {
                // Number pile variables
                CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
                int sourceTopCardIndex = sourceDeck.getTopCardIndex();
                Card sourceTopCard = sourceDeck.get(sourceTopCardIndex);

                // Ace pile variables
                CardDeck destinationDeck;
                Card ace = new Card(sourceTopCard.getSuit(), 1);
                int destionationTopCardIndex;

                // Identify destionation Ace pile. Only runs if source card is a deuce.
                // Default is just so deck is a non-empty, non-ace deck.
                if ( sourceTopCard.getValue() == 2){
                    switch (sourceTopCard.getSuit()){
                        case HEARTS -> destinationDeck = this.board.getDeck("heartsPile");
                        case SPADES -> destinationDeck = this.board.getDeck("spadesPile");
                        case DIAMONDS -> destinationDeck = this.board.getDeck("diamondsPile");
                        case CLUBS -> destinationDeck = this.board.getDeck("clubsPile");
                        default -> destinationDeck = sourceDeck;
                    }

                    // Check if the top card of the destionation pile is an ace, if so add candidate
                    destionationTopCardIndex = destinationDeck.getTopCardIndex();
                    if (destinationDeck.isCardValue(destionationTopCardIndex, ace)){
                        candidates.add((new Move(sourceDeck, destinationDeck, sourceTopCardIndex)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // Strings are hardcorded so this is used for bughunting
            }
        }
        // Sort candidates, if any, in ascending order
        if (!candidates.isEmpty()) {
            candidateSorter(candidates);
            // Execute best candidate move.
            // executeBestCandidate(candidates.get(candidates.size()-1)); Uncomment when function parameters are refactored.
        }
    }
    // Search for Aces in number piles and move best candidate to foundation
    private void scanForMoveType3() {


    }
    private void scanForMoveType4() {

    }
    private void scanForMoveType5() {

    }
    private void scanForMoveType6() {

    }
    private void scanForMoveType7() {

    }
    private void scanForMoveType8() {

    }
    private void scanForMoveType9() {

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
    public void validateCandidates7() {

    }
    public void validateCandidates8() {

    }
    public void validateCandidates9() {

    }

    private void candidateSorter(ArrayList<Move> candidates){
        candidates.sort(
                Comparator.comparing(
                        c -> c.getSourceDeck().getNumberOfFaceDownCards()
                )
        );
    }

    private boolean clearDeckOkay(Move move){
        return false;
    }
}
