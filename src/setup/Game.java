package setup;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.DecksInput;
import fileio.GameInput;
import resources.Decks;

import java.util.ArrayList;

public class Game {
    private Decks playerOneDecks;
    private Decks playerTwoDecks;
    private ArrayList<Match> matches;

    public static final int MAX_MANA = 10;

    public Decks getPlayerOneDecks() {
        return playerOneDecks;
    }

    public void setPlayerOneDecks(Decks playerOneDecks) {
        this.playerOneDecks = playerOneDecks;
    }

    public Decks getPlayerTwoDecks() {
        return playerTwoDecks;
    }

    public void setPlayerTwoDecks(Decks playerTwoDecks) {
        this.playerTwoDecks = playerTwoDecks;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public Game(DecksInput playerOneDecks, DecksInput playerTwoDecks, ArrayList<GameInput> games, ArrayNode output) {
        this.playerOneDecks = new Decks(playerOneDecks.getNrCardsInDeck(),
                playerOneDecks.getNrDecks(), playerOneDecks.getDecks());
        this.playerTwoDecks = new Decks(playerTwoDecks.getNrCardsInDeck(),
                playerTwoDecks.getNrDecks(), playerTwoDecks.getDecks());
        this.matches = new ArrayList<Match>();

        for (GameInput game : games) {
            this.matches.add(new Match(game, this.playerOneDecks, this.playerTwoDecks, output));
        }
    }
}
