package org.poo.setup;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.DecksInput;
import org.poo.fileio.GameInput;
import org.poo.resources.Decks;

import java.util.ArrayList;

public final class Game {
    private Decks playerOneDecks;
    private Decks playerTwoDecks;
    private ArrayList<Match> matches;
    private static int playerOneWins;
    private static int playerTwoWins;

    public static final int HERO_HEALTH = 30;
    public static final int MAX_MANA = 10;
    public static final int BOARD_ROWS = 4;
    public static final int BOARD_COLUMNS = 5;
    public static final int ENEMY_HAS_TAUNT = -3;
    public static final int NOT_ENEMY = -2;
    public static final int ALREADY_ATTACKED = -1;
    public static final int IS_FROZEN = 1;
    public static final int NOT_ALLY = 2;
    public static final int OUT_OF_MANA = 3;
    public static final int NO_ROOM = 4;
    public static final int HERO_ABILITY = 2;

    public Decks getPlayerOneDecks() {
        return playerOneDecks;
    }

    public void setPlayerOneDecks(final Decks playerOneDecks) {
        this.playerOneDecks = playerOneDecks;
    }

    public Decks getPlayerTwoDecks() {
        return playerTwoDecks;
    }

    public void setPlayerTwoDecks(final Decks playerTwoDecks) {
        this.playerTwoDecks = playerTwoDecks;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public void setMatches(final ArrayList<Match> matches) {
        this.matches = matches;
    }

    public static int getPlayerOneWins() {
        return playerOneWins;
    }

    public void setPlayerOneWins(final int playerOneWins) {
        Game.playerOneWins = playerOneWins;
    }

    public static int getPlayerTwoWins() {
        return playerTwoWins;
    }

    public void setPlayerTwoWins(final int playerTwoWins) {
        Game.playerTwoWins = playerTwoWins;
    }

    /**
     * Increments the number of games won by player one.
     */
    public static void incrementPlayerOneWins() {
        playerOneWins++;
    }

    /**
     * Increments the number of games won by player two.
     */
    public static void incrementPlayerTwoWins() {
        playerTwoWins++;
    }

    public Game(final DecksInput playerOneDecks, final DecksInput playerTwoDecks,
                final ArrayList<GameInput> games, final ArrayNode output) {
        playerOneWins = 0;
        playerTwoWins = 0;

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
