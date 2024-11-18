package setup;

import resources.Card;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> deck;
    private ArrayList<Card> hand;
    private Card hero;
    private int mana = 0;

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
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

        if (playerIdx == 1) {
            enemyRow = 0;
        }

        if ((attackedX != enemyRow && attackedX != (enemyRow + 1)) || cardAttacked == null)
            return -2;

        if (cardAttacker.hasAttacked())
            return -1;

        if (cardAttacker.isFrozen())
            return 1;

        if (enemyHasTank(board, enemyRow) && !cardAttacked.isTank())
            return 2;

        cardAttacker.setAttacked(true);
        cardAttacked.reduceHealth(cardAttacker.getAttackDamage());

        cardAttacked.removeIfDead(board, attackedX, attackedY);

        return 0;
    }

    public int useCardAbility(Card [][] board, int attackerX, int attackerY, int attackedX, int attackedY, int playerIdx) {
        Card cardAttacker = board[attackerX][attackerY];
        Card cardAttacked = board[attackedX][attackedY];
        int enemyRow = 2;

        if (playerIdx == 1) {
            enemyRow = 0;
        }

        if (cardAttacker.isFrozen())
            return -3;

        if (cardAttacker.hasAttacked())
            return -2;

        if (cardAttacker.getName().equals("Disciple") && (attackedX == enemyRow || attackedX == enemyRow + 1))
            return -1;

        if (!cardAttacker.getName().equals("Disciple"))
            if (attackedX != enemyRow && attackedX != enemyRow + 1)
                return 1;
            else if (enemyHasTank(board, enemyRow) && !cardAttacked.isTank())
                return 2;

        switch (cardAttacker.getName()) {
            case "Disciple":
                cardAttacked.reduceHealth(-2);
                break;

            case "The Ripper":
                cardAttacked.reduceAttack(2);
                break;

            case "Miraj":
                int auxHealth = cardAttacker.getHealth();
                cardAttacker.setHealth(cardAttacked.getHealth());
                cardAttacked.setHealth(auxHealth);
                break;

            case "The Cursed One":
                int aux = cardAttacked.getHealth();
                cardAttacked.setHealth(cardAttacked.getAttackDamage());
                cardAttacked.setAttackDamage(aux);
                cardAttacked.removeIfDead(board, attackedX, attackedY);
                break;
        }

        cardAttacker.setAttacked(true);

        return 0;
    }

    public int attackHero(Card [][] board, int attackerX, int attackerY, Card enemyHero, int playerIdx) {
        Card card = board[attackerX][attackerY];
        int enemyRow = 2;

        if (playerIdx == 1) {
            enemyRow = 0;
        }

        if (card.isFrozen())
            return -2;

        if (card.hasAttacked())
            return -1;

        if(enemyHasTank(board, enemyRow))
            return 1;

        card.setAttacked(true);
        enemyHero.reduceHealth(card.getAttackDamage());

        return 0;
    }

    public int useHeroAbility(Card [][] board, int affectedRow, int playerIdx) {
        int enemyRow = 2;

        if (playerIdx == 1) {
            enemyRow = 0;
        }

        if (mana < hero.getMana())
            return -2;

        if (hero.hasAttacked())
            return -1;

        if ((hero.getName().equals("Lord Royce") || hero.getName().equals("Empress Thorina"))
            && affectedRow != enemyRow && affectedRow != enemyRow + 1)
            return 1;

        if ((hero.getName().equals("General Kocioraw") || hero.getName().equals("King Mudface"))
            && (affectedRow == enemyRow || affectedRow == enemyRow + 1))
            return 2;

        switch (hero.getName()) {
            case "Lord Royce":
                for (int j = 0; j < 5; j++)
                    if (board[affectedRow][j] != null)
                        board[affectedRow][j].setFrozen(true);
                break;

            case "Empress Thorina":
                int maxHealth = 0, colIdx = -1;

                for (int j = 0; j < 5; j++)
                    if (board[affectedRow][j] != null && board[affectedRow][j].getHealth() > maxHealth) {
                        maxHealth = board[affectedRow][j].getHealth();
                        colIdx = j;
                    }

                board[affectedRow][colIdx].setHealth(0);
                board[affectedRow][colIdx].removeIfDead(board, affectedRow, colIdx);
                break;

            case "King Mudface":
                for (int j = 0; j < 5; j++)
                    if (board[affectedRow][j] != null)
                        board[affectedRow][j].reduceHealth(-1);
                break;

            case "General Kocioraw":
                for (int j = 0; j < 5; j++)
                    if (board[affectedRow][j] != null)
                        board[affectedRow][j].reduceAttack(-1);
                break;
        }

        hero.setAttacked(true);
        mana -= hero.getMana();

        return 0;
    }

    private boolean enemyHasTank(Card [][] board, int enemyRow) {
        for (int i = enemyRow; i < enemyRow + 2; i++)
            for (int j = 0; j < 5; j++) {
                if (board[i][j] != null && (board[i][j].isTank()))
                    return true;
            }

        return false;
    }
}
