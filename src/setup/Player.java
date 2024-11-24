package setup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import resources.Hero;
import resources.minions.Minion;
import resources.minions.SpecialMinion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Player {
    private ArrayList<Minion> deck;
    private ArrayList<Minion> hand;
    private Hero hero;
    private int mana = 0;

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public ArrayList<Minion> getHand() {
        return hand;
    }

    public void setHand(final ArrayList<Minion> hand) {
        this.hand = hand;
    }

    public ArrayList<Minion> getDeck() {
        return deck;
    }

    public void setDeck(final ArrayList<Minion> deck) {
        this.deck = deck;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(final Hero hero) {
        this.hero = hero;
    }

    /**
     * Increases mana by a given value.
     * @param value the value to be increased with
     */
    public void increaseMana(final int value) {
        mana += Math.min(value, Game.MAX_MANA);
    }

    public Player(final ArrayList<Minion> deck, final Hero hero) {
        this.deck = new ArrayList<Minion>();
        for (Minion card : deck) {
            this.deck.add(new Minion(card));
        }

        this.hero = Hero.createHero(hero);
        this.hand = new ArrayList<>();
    }

    /**
     * Shuffles the player's deck based on a shuffle seed.
     * @param shuffleSeed the given seed
     */
    public void shuffleDeck(final int shuffleSeed) {
        Random random = new Random(shuffleSeed);
        Collections.shuffle(deck, random);
    }

    /**
     * Adds all cards from hand to an ArrayNode to be printed as a JSON.
     * @return the ArrayNode containing all the cards
     */
    public ArrayNode getCardsInHand() {
        ArrayNode arrayNode = new ObjectMapper().createArrayNode();
        for (Minion card : hand) {
            arrayNode.add(card.convertToJSON());
        }
        return arrayNode;
    }

    /**
     * Adds all cards from deck to an ArrayNode to be printed as a JSON.
     * @return the ArrayNode containing all the cards
     */
    public ArrayNode getPlayerDeck() {
        ArrayNode arrayNode = new ObjectMapper().createArrayNode();
        for (Minion card : deck) {
            arrayNode.add(card.convertToJSON());
        }
        return arrayNode;
    }


    /**
     * Adds a card to hand from the deck.
     */
    public void addCardToHand() {
        if (!deck.isEmpty()) {
            hand.add(Minion.createMinion(deck.removeFirst()));
        }
    }

    /**
     * Plays a card from hand on the board.
     * @param board the board the card is to be played on
     * @param handIdx position of the card in hand
     * @param playerIdx index of the player who places the card on board
     * @return 0 if the card could be played, OUT_OF_MANA otherwise
     */
    public int playCard(final Minion[][] board, final int handIdx, final int playerIdx) {
        Minion card = hand.get(handIdx);
        if (mana < card.getMana()) {
            return Game.OUT_OF_MANA;
        }

        if (!card.placeOnBoard(board, playerIdx)) {
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
    public int attackCard(final Minion[][] board, final int attackerX, final int attackerY,
                          final int attackedX, final int attackedY, final int playerIdx) {
        Minion cardAttacker = board[attackerX][attackerY];
        Minion cardAttacked = board[attackedX][attackedY];
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
    public int useCardAbility(final Minion[][] board, final int attackerX, final int attackerY,
                              final int attackedX, final int attackedY, final int playerIdx) {
        SpecialMinion cardAttacker = (SpecialMinion) board[attackerX][attackerY];
        Minion cardAttacked = board[attackedX][attackedY];
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
        if (!cardAttacker.getAffectsEnemy()
                && (attackedX == enemyRow || attackedX == enemyRow + 1)) {
            return Game.NOT_ALLY;
        }
        if (cardAttacker.getAffectsEnemy()) {
            if (attackedX != enemyRow && attackedX != enemyRow + 1) {
                return Game.NOT_ENEMY;
            } else if (enemyHasTank(board, enemyRow) && !cardAttacked.isTank()) {
                return Game.ENEMY_HAS_TAUNT;
            }
        }

        cardAttacker.useSpecialAbility(cardAttacked);
        cardAttacked.removeIfDead(board, attackedX, attackedY);
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
    public int attackHero(final Minion[][] board, final int attackerX, final int attackerY,
                          final Hero enemyHero, final int playerIdx) {
        Minion card = board[attackerX][attackerY];
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
    public int useHeroAbility(final Minion[][] board, final int affectedRow, final int playerIdx) {
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
        if (hero.getAffectsEnemy() && affectedRow != enemyRow && affectedRow != enemyRow + 1) {
            return Game.NOT_ENEMY;
        }
        if (!hero.getAffectsEnemy() && (affectedRow == enemyRow || affectedRow == enemyRow + 1)) {
            return Game.NOT_ALLY;
        }

        hero.useSpecialAbility(board, affectedRow);
        hero.setAttacked(true);
        mana -= hero.getMana();

        return 0;
    }

    private boolean enemyHasTank(final Minion[][] board, final int enemyRow) {
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
