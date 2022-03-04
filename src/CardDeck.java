import java.io.*;
import java.util.ArrayList;

public class CardDeck {

    ArrayList<Card> deck = new ArrayList<>(52);

    public void initialPopulate() throws IOException {
        File file = new File(
                "C:\\Users\\Jhinstalock\\IdeaProjects\\Solitaire_AI\\resources\\cards.txt"
        );

        BufferedReader br = new BufferedReader(new FileReader(file));

        String str;

        while ((str = br.readLine()) != null) {
            //handle each line of input
            deck.add(new Card(parseInputSuit(str),parseInputValue(str)));
        }
    }

    public Suit parseInputSuit(String s) throws IOException {
        return switch (s) {
            case "H" -> Suit.HEARTS;
            case "S" -> Suit.SPADES;
            case "D" -> Suit.DIAMONDS;
            case "C" -> Suit.CLUBS;
            default -> throw new IOException("Input text format error");
        };
    }
    public int parseInputValue(String s) throws IOException {
        if (s.length() > 2) {
            throw new IOException("Input text format error");
        }
        return Integer.parseInt(s.substring(1,2));
    }
}
