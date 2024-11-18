package setup;

import resources.Card;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> deck;
    private ArrayList<Card> hand;
    private Card hero;
    private int mana = 0;
    private int wins = 0;

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public Card getHero() {
        return hero;
    }

    public void setHero(Card hero) {
        this.hero = hero;
    }

    public void increaseMana(int value) {
        mana += Math.min(value, Game.MAX_MANA);
    }

    public Player(ArrayList<Card> deck, Card hero) {
        this.deck = new ArrayList<Card>();
        for (Card card : deck) {
            this.deck.add(new Card(card));
        }

        this.hero = new Card(hero);
        this.hand = new ArrayList<>();
    }

    public void addCardToHand() {
        if (!deck.isEmpty()) {
            hand.add(deck.removeFirst());
        }
    }

    public int playCard(Card [][] board, int handIdx, int playerIdx) {
        Card card = hand.get(handIdx);
        if (card.getMana() > mana)
            return -1;

        int frontRow = 1, backRow = 0;
        int rowToBePlaced;
        boolean wasPlaced = false;

        if (playerIdx == 1) {
            frontRow = 2;
            backRow = 3;
        }

        if (card.getName().equals("Sentinel") || card.getName().equals("Berserker")
            || card.getName().equals("The Cursed One") || card.getName().equals("Disciple"))
            rowToBePlaced = backRow;
        else
            rowToBePlaced = frontRow;

        for (int j = 0; j < 5; j++)
            if (board[rowToBePlaced][j] == null) {
                board[rowToBePlaced][j] = new Card(card);
                wasPlaced = true;
                break;
            }

        if (!wasPlaced)
            return 1;

        mana -= card.getMana();
        hand.remove(handIdx);

        return 0;
    }

    public int attackCard(Card [][] board, int attackerX, int attackerY, int attackedX, int attackedY, int playerIdx) {
        Card cardAttacker = board[attackerX][attackerY];
        Card cardAttacked = board[attackedX][attackedY];
        int enemyRow = 2;
        boolean enemyHasTank = false;

        if (playerIdx == 1) {
            enemyRow = 0;
        }

        if ((attackedX != enemyRow && attackedX != (enemyRow + 1)) || cardAttacked == null)
            return -2;

        if (cardAttacker.hasAttacked())
            return -1;

        if (cardAttacker.isFrozen())
            return 1;

        for (int i = enemyRow; i < enemyRow + 2; i++)
            for (int j = 0; j < 5; j++) {
                if (board[i][j] != null && (board[i][j].isTank()))
                    enemyHasTank = true;
            }

        if (enemyHasTank && !cardAttacked.isTank())
            return 2;

        cardAttacker.setAttacked(true);
        cardAttacked.reduceHealth(cardAttacker.getAttackDamage());

        if (cardAttacked.getHealth() <= 0) {
            board[attackedX][attackedY] = null;

            for (int j = attackedY; j < 4; j++)
                board[attackedX][j] = board[attackedX][j + 1];

            board[attackedX][4] = null;
        }
        return 0;
    }
}
