package setup;

import fileio.StartGameInput;
import resources.Hero;

public final class MatchInfo {
    private int playerOneDeckIdx;
    private int playerTwoDeckIdx;
    private int shuffleSeed;
    private Hero playerOneHero;
    private Hero playerTwoHero;
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

    public Hero getPlayerOneHero() {
        return playerOneHero;
    }

    public void setPlayerOneHero(final Hero playerOneHero) {
        this.playerOneHero = playerOneHero;
    }

    public Hero getPlayerTwoHero() {
        return playerTwoHero;
    }

    public void setPlayerTwoHero(final Hero playerTwoHero) {
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
        playerOneHero = Hero.createHero(info.getPlayerOneHero());
        playerTwoHero = Hero.createHero(info.getPlayerTwoHero());
        startingPlayer = info.getStartingPlayer();
    }
}
