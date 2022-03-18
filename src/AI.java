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


    /** AI functions
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

    **/


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
        //Feed it one move somehow
    }

    //TODO Person currently working: Simon
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
                        case HEARTS -> destinationDeck = board.heartsPile;
                        case SPADES -> destinationDeck = board.spadesPile;
                        case DIAMONDS -> destinationDeck = board.diamondsPile;
                        case CLUBS -> destinationDeck = board.clubsPile;
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

    //TODO Person currently working: Simon
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
                        case HEARTS -> destinationDeck = board.heartsPile;
                        case SPADES -> destinationDeck = board.spadesPile;
                        case DIAMONDS -> destinationDeck = board.diamondsPile;
                        case CLUBS -> destinationDeck = board.clubsPile;
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

    //TODO Person currently working: Simon
    // Search for transferable face-up card(s) that will free a face-down card
    private void scanForMoveType3() {
        // Search all number piles
        // Check if transfering a card will free a down card (the second to last card in pile is face-down)
            // Check if top-card can be placed anywhere in the piles
                // If not check if top-card can be placed on Ace stack
                    // If yes check if the same color same number card is a topcard on a pil
                    // Or if both not-color cards are have already been played
                        // Transfer card
                // If yes transfer card
    }

    //TODO Person currently working:
    //  Transfer cards from column to column only to allow a downcard to be freed or to make the columns smoother.
    private void scanForMoveType4() {

    }

    //TODO Person currently working:
    //  Don't clear a spot unless there's a King IMMEDIATELY waiting to occupy it.
    private void scanForMoveType5() {

    }

    //TODO Person currently working:
    //  Only play a King that will benefit the column(s) with the biggest pile of downcards,
    //  unless the play of another King will at least allow a transfer that frees a downcard.
    private void scanForMoveType6() {

    }

    //TODO Person currently working:
    //  Only build your Ace stacks (with anything other than an Ace or Deuce) when the play will:
    //  Not interfere with your Next Card Protection
    //  Allow a play or transfer that frees (or allows a play that frees) a downcard
    //  Open up a space for a same-color card pile transfer that allows a downcard to be freed
    //  Clear a spot for an IMMEDIATE waiting King (it cannot be to simply clear a spot)
    private void scanForMoveType7() {

    }

    //TODO Person currently working:
    //  Don't play or transfer a 5, 6, 7 or 8 anywhere unless at least one of these situations will apply after the play:
    //  It is smooth with it's next highest even/odd partner in the column
    //  It will allow a play or transfer that will IMMEDIATELY free a downcard
    //  There have not been any other cards already played to the column
    //  You have ABSOLUTELY no other choice to continue playing
    private void scanForMoveType8() {

    }

    //TODO Person currently working:
    //  When you get to a point that you think all of your necessary cards are covered and you just can't get to them,
    //  IMMEDIATELY play any cards you can to their appropriate Ace stacks. You may have to rearrange existing piles to
    //  allow blocked cards freedom to be able to go to their Ace stack. Hopefully this will clear an existing pile up
    //  to the point that you can use an existing pile upcard to substitute for the necessary covered card.
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
        // 1. The calling function should  iterate through the list of candidates starting with the last index
        // 2. Before continuing the function should call clearDeckOkay(move) and ensure that the move is desirable
        // 3. If move is desirable call executeBestCandidate() and terminate iteration
        // 5. Else continue iteration to next candidate
        return false;
    }
}
