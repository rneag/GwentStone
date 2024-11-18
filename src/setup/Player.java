package setup;

import resources.Card;

import java.util.ArrayList;

public final class Player {
    private ArrayList<Card> deck;
    private ArrayList<Card> hand;
    private Card hero;
    private int mana = 0;

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(final ArrayList<Card> hand) {
        this.hand = hand;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(final ArrayList<Card> deck) {
        this.deck = deck;
    }

    public Card getHero() {
        return hero;
    }

    public void setHero(final Card hero) {
        this.hero = hero;
    }

    /**
     * Increases mana by a given value.
     * @param value
     */
    public void increaseMana(final int value) {
        mana += Math.min(value, Game.MAX_MANA);
    }

    public Player(final ArrayList<Card> deck, final Card hero) {
        this.deck = new ArrayList<Card>();
        for (Card card : deck) {
            this.deck.add(new Card(card));
        }

        this.hero = new Card(hero);
        this.hand = new ArrayList<>();
    }

    /**
     * Adds a card to hand from the deck.
     */
    public void addCardToHand() {
        if (!deck.isEmpty()) {
            hand.add(deck.removeFirst());
        }
    }

    /**
     * Plays a card from hand on the board
     * @param board the board the card is to be played on
     * @param handIdx position of the card in hand
     * @param playerIdx index of the player who places the card on board
     * @return 0 if the card could be played, OUT_OF_MANA otherwise
     */
    public int playCard(final Card[][] board, final int handIdx, final int playerIdx) {
        Card card = hand.get(handIdx);
        if (mana < card.getMana()) {
            return Game.OUT_OF_MANA;
        }

        int frontRow = 1, backRow = 0;
        int rowToBePlaced;
        boolean wasPlaced = false;

        if (playerIdx == 1) {
            frontRow = Game.BOARD_ROWS - 2;
            backRow = Game.BOARD_ROWS - 1;
        }

        if (card.getName().equals("Sentinel") || card.getName().equals("Berserker")
            || card.getName().equals("The Cursed One") || card.getName().equals("Disciple")) {
            rowToBePlaced = backRow;
        } else {
            rowToBePlaced = frontRow;
        }

        for (int j = 0; j < Game.BOARD_COLUMNS; j++) {
            if (board[rowToBePlaced][j] == null) {
                board[rowToBePlaced][j] = new Card(card);
                wasPlaced = true;
                break;
            }
        }

        if (!wasPlaced) {
            return Game.NO_ROOM;
        }

        mana -= card.getMana();
        hand.remove(handIdx);

        return 0;
    }

    /**
     * Attacker card attacks attacked card.
     * @param board the board the cards are played on
     * @param attackerX row of the attacker card
     * @param attackerY column of the attacker card
     * @param attackedX row of the attacked card
     * @param attackedY column of the attacked card
     * @param playerIdx index of the player who attacks
     * @return 0 if there were no errors when attacking the card
     */
    public int attackCard(final Card[][] board, final int attackerX, final int attackerY,
                          final int attackedX, final int attackedY, final int playerIdx) {
        Card cardAttacker = board[attackerX][attackerY];
        Card cardAttacked = board[attackedX][attackedY];
        int enemyRow = 2;

        if (playerIdx == 1) {
            enemyRow = 0;
        }

        if ((attackedX != enemyRow && attackedX != (enemyRow + 1)) || cardAttacked == null) {
            return Game.NOT_ENEMY;
        }

        if (cardAttacker.hasAttacked()) {
            return Game.ALREADY_ATTACKED;
        }
        if (cardAttacker.isFrozen()) {
            return Game.IS_FROZEN;
        }
        if (enemyHasTank(board, enemyRow) && !cardAttacked.isTank()) {
            return Game.ENEMY_HAS_TAUNT;
        }

        cardAttacker.setAttacked(true);
        cardAttacked.reduceHealth(cardAttacker.getAttackDamage());

        cardAttacked.removeIfDead(board, attackedX, attackedY);

        return 0;
    }

    /**
     * Uses the ability of the attacker card on the attacked card.
     * @param board the board the cards are played on
     * @param attackerX row of the attacker card
     * @param attackerY column of the attacker card
     * @param attackedX row of the attacked card
     * @param attackedY column of the attacked card
     * @param playerIdx index of the player who attacks
     * @return 0 if there were no errors when attacking the card
     */
    public int useCardAbility(final Card[][] board, final int attackerX, final int attackerY,
                              final int attackedX, final int attackedY, final int playerIdx) {
        Card cardAttacker = board[attackerX][attackerY];
        Card cardAttacked = board[attackedX][attackedY];
        int enemyRow = 2;

        if (playerIdx == 1) {
            enemyRow = 0;
        }

        if (cardAttacker.isFrozen()) {
            return Game.IS_FROZEN;
        }
        if (cardAttacker.hasAttacked()) {
            return Game.ALREADY_ATTACKED;
        }
        if (cardAttacker.getName().equals("Disciple")
                && (attackedX == enemyRow || attackedX == enemyRow + 1)) {
            return Game.NOT_ALLY;
        }
        if (!cardAttacker.getName().equals("Disciple")) {
            if (attackedX != enemyRow && attackedX != enemyRow + 1) {
                return Game.NOT_ENEMY;
            } else if (enemyHasTank(board, enemyRow) && !cardAttacked.isTank()) {
                return Game.ENEMY_HAS_TAUNT;
            }
        }

        switch (cardAttacker.getName()) {
            case "Disciple":
                cardAttacked.reduceHealth(-Game.HERO_ABILITY);
                break;

            case "The Ripper":
                cardAttacked.reduceAttack(Game.HERO_ABILITY);
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

            default:
                break;
        }

        cardAttacker.setAttacked(true);

        return 0;
    }

    /**
     * Attacker card attacks the enemy hero.
     * @param board the board the card is played on
     * @param attackerX row of the attacker card
     * @param attackerY column of the attacker card
     * @param enemyHero enemy hero
     * @param playerIdx index of the player that attacks
     * @return 0 if no errors occurred when attacking
     */
    public int attackHero(final Card[][] board, final int attackerX, final int attackerY,
                          final Card enemyHero, final int playerIdx) {
        Card card = board[attackerX][attackerY];
        int enemyRow = 2;

        if (playerIdx == 1) {
            enemyRow = 0;
        }

        if (card.isFrozen()) {
            return Game.IS_FROZEN;
        }
        if (card.hasAttacked()) {
            return Game.ALREADY_ATTACKED;
        }
        if (enemyHasTank(board, enemyRow)) {
            return Game.ENEMY_HAS_TAUNT;
        }

        card.setAttacked(true);
        enemyHero.reduceHealth(card.getAttackDamage());

        return 0;
    }

    /**
     * Uses the ability of the player hero.
     * @param board the board the cards are played on
     * @param affectedRow the row the player wants to affect
     * @param playerIdx index of the player that uses the ability
     * @return 0 if no errors occurred when using the ability
     */
    public int useHeroAbility(final Card[][] board, final int affectedRow, final int playerIdx) {
        int enemyRow = 2;

        if (playerIdx == 1) {
            enemyRow = 0;
        }

        if (mana < hero.getMana()) {
            return Game.OUT_OF_MANA;
        }
        if (hero.hasAttacked()) {
            return Game.ALREADY_ATTACKED;
        }
        if ((hero.getName().equals("Lord Royce") || hero.getName().equals("Empress Thorina"))
            && affectedRow != enemyRow && affectedRow != enemyRow + 1) {
            return Game.NOT_ENEMY;
        }
        if ((hero.getName().equals("General Kocioraw") || hero.getName().equals("King Mudface"))
            && (affectedRow == enemyRow || affectedRow == enemyRow + 1)) {
            return Game.NOT_ALLY;
        }

        switch (hero.getName()) {
            case "Lord Royce":
                for (int j = 0; j < Game.BOARD_COLUMNS; j++) {
                    if (board[affectedRow][j] != null) {
                        board[affectedRow][j].setFrozen(true);
                    }
                }
                break;

            case "Empress Thorina":
                int maxHealth = 0, colIdx = -1;

                for (int j = 0; j < Game.BOARD_COLUMNS; j++) {
                    if (board[affectedRow][j] != null
                            && board[affectedRow][j].getHealth() > maxHealth) {
                        maxHealth = board[affectedRow][j].getHealth();
                        colIdx = j;
                    }
                }

                board[affectedRow][colIdx].setHealth(0);
                board[affectedRow][colIdx].removeIfDead(board, affectedRow, colIdx);
                break;

            case "King Mudface":
                for (int j = 0; j < Game.BOARD_COLUMNS; j++) {
                    if (board[affectedRow][j] != null) {
                        board[affectedRow][j].reduceHealth(-1);
                    }
                }
                break;

            case "General Kocioraw":
                for (int j = 0; j < Game.BOARD_COLUMNS; j++) {
                    if (board[affectedRow][j] != null) {
                        board[affectedRow][j].reduceAttack(-1);
                    }
                }
                break;

            default:
                break;
        }

        hero.setAttacked(true);
        mana -= hero.getMana();

        return 0;
    }

    private boolean enemyHasTank(final Card[][] board, final int enemyRow) {
        for (int i = enemyRow; i < enemyRow + 2; i++) {
            for (int j = 0; j < Game.BOARD_COLUMNS; j++) {
                if (board[i][j] != null && (board[i][j].isTank())) {
                    return true;
                }
            }
        }
        return false;
    }
}
