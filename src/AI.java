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
        validateCandidates2();
        executeBestCandidate();

        scanForMoveType3();
        validateCandidates3();
        executeBestCandidate();

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
        //Feed it one move somehow
    }

    // Search for Aces in number piles and move best candidate to foundation
    private void scanForMoveType1() {
        ArrayList<Move> candidates = new ArrayList<>();
        for(int i = 1; i <= 7; i++){
            try {
                CardDeck sourceDeck = this.board.getDeck(Integer.toString(i));
                int topCardIndex = sourceDeck.getTopCardIndex();
                Card topCard = sourceDeck.get(topCardIndex);
                if ( topCard.getValue() == 1){
                    CardDeck destinationDeck;
                    switch (topCard.getSuit()){
                        case HEARTS -> destinationDeck = this.board.getDeck("heartsPile");
                        case SPADES -> destinationDeck = this.board.getDeck("spadesPile");
                        case DIAMONDS -> destinationDeck = this.board.getDeck("diamondsPile");
                        case CLUBS -> destinationDeck = this.board.getDeck("clubsPile");
                        default -> destinationDeck = sourceDeck;
                    }
                    candidates.add((new Move(sourceDeck, destinationDeck, topCardIndex)));
                }
            } catch (Exception e) {
                e.printStackTrace(); // Strings are hardcorded so this is used for bughunting
            }

            candidates.sort(
                    Comparator.comparing(
                            c -> c.getSourceDeck().getNumberOfFaceDownCards()
                    )
            );

            // executeBestCandidate(candidates.get(candidates.size()-1)); Uncomment when function parameters are refactored.
        }
    }
    private void scanForMoveType2() {

    }
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




}
