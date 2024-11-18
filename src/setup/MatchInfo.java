package setup;

import fileio.StartGameInput;
import resources.Card;

public final class MatchInfo {
    private int playerOneDeckIdx;
    private int playerTwoDeckIdx;
    private int shuffleSeed;
    private Card playerOneHero;
    private Card playerTwoHero;
    private int startingPlayer;

    public int getPlayerOneDeckIdx() {
        return playerOneDeckIdx;
    }

    public void setPlayerOneDeckIdx(final int playerOneDeckIdx) {
        this.playerOneDeckIdx = playerOneDeckIdx;
    }

    public int getPlayerTwoDeckIdx() {
        return playerTwoDeckIdx;
    }

    public void setPlayerTwoDeckIdx(final int playerTwoDeckIdx) {
        this.playerTwoDeckIdx = playerTwoDeckIdx;
    }

    public int getShuffleSeed() {
        return shuffleSeed;
    }

    public void setShuffleSeed(final int shuffleSeed) {
        this.shuffleSeed = shuffleSeed;
    }

    public Card getPlayerOneHero() {
        return playerOneHero;
    }

    public void setPlayerOneHero(final Card playerOneHero) {
        this.playerOneHero = playerOneHero;
    }

    public Card getPlayerTwoHero() {
        return playerTwoHero;
    }

    public void setPlayerTwoHero(final Card playerTwoHero) {
        this.playerTwoHero = playerTwoHero;
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }

    public void setStartingPlayer(final int startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    public MatchInfo(final StartGameInput info) {
        playerOneDeckIdx = info.getPlayerOneDeckIdx();
        playerTwoDeckIdx = info.getPlayerTwoDeckIdx();
        shuffleSeed = info.getShuffleSeed();
        playerOneHero = new Card(info.getPlayerOneHero());
        playerOneHero.setHealth(Game.HERO_HEALTH);
        playerTwoHero = new Card(info.getPlayerTwoHero());
        playerTwoHero.setHealth(Game.HERO_HEALTH);
        startingPlayer = info.getStartingPlayer();
    }
}
