package setup;

import fileio.StartGameInput;
import resources.Card;

public class MatchInfo {
    private int playerOneDeckIdx;
    private int playerTwoDeckIdx;
    private int shuffleSeed;
    private Card playerOneHero;
    private Card playerTwoHero;
    private int startingPlayer;

    public int getPlayerOneDeckIdx() {
        return playerOneDeckIdx;
    }

    public void setPlayerOneDeckIdx(int playerOneDeckIdx) {
        this.playerOneDeckIdx = playerOneDeckIdx;
    }

    public int getPlayerTwoDeckIdx() {
        return playerTwoDeckIdx;
    }

    public void setPlayerTwoDeckIdx(int playerTwoDeckIdx) {
        this.playerTwoDeckIdx = playerTwoDeckIdx;
    }

    public int getShuffleSeed() {
        return shuffleSeed;
    }

    public void setShuffleSeed(int shuffleSeed) {
        this.shuffleSeed = shuffleSeed;
    }

    public Card getPlayerOneHero() {
        return playerOneHero;
    }

    public void setPlayerOneHero(Card playerOneHero) {
        this.playerOneHero = playerOneHero;
    }

    public Card getPlayerTwoHero() {
        return playerTwoHero;
    }

    public void setPlayerTwoHero(Card playerTwoHero) {
        this.playerTwoHero = playerTwoHero;
    }

    public int getStartingPlayer() {
        return startingPlayer;
    }

    public void setStartingPlayer(int startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    public MatchInfo(StartGameInput info) {
        playerOneDeckIdx = info.getPlayerOneDeckIdx();
        playerTwoDeckIdx = info.getPlayerTwoDeckIdx();
        shuffleSeed = info.getShuffleSeed();
        playerOneHero = new Card(info.getPlayerOneHero());
        playerOneHero.setHealth(30);
        playerTwoHero = new Card(info.getPlayerTwoHero());
        playerTwoHero.setHealth(30);
        startingPlayer = info.getStartingPlayer();
    }

    @Override
    public String toString() {
        return "MatchInfo{" +
                "playerOneDeckIdx=" + playerOneDeckIdx +
                ", playerTwoDeckIdx=" + playerTwoDeckIdx +
                ", shuffleSeed=" + shuffleSeed +
                ", playerOneHero=" + playerOneHero +
                ", playerTwoHero=" + playerTwoHero +
                ", startingPlayer=" + startingPlayer +
                '}';
    }
}
