package org.poo.resources.minions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CardInput;
import org.poo.resources.Card;
import org.poo.resources.minions.regular.Berserker;
import org.poo.resources.minions.regular.Sentinel;
import org.poo.resources.minions.special.Disciple;
import org.poo.resources.minions.special.Miraj;
import org.poo.resources.minions.special.TheCursedOne;
import org.poo.resources.minions.special.TheRipper;
import org.poo.resources.minions.tanks.Goliath;
import org.poo.resources.minions.tanks.Warden;
import org.poo.setup.Game;

public class Minion extends Card {
    private int attackDamage;
    private boolean isFrozen;
    private boolean isTank;

    /**
     * Getter for the minion's attack damage.
     * @return the attack damage
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Setter for the minion's attack damage.
     * @param attackDamage the new attack damage value
     */
    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Getter for the minion's frozen status.
     * @return 1 if the minion is frozen, 0 otherwise
     */
    public boolean isFrozen() {
        return isFrozen;
    }

    /**
     * Setter for the minion's frozen status.
     * @param frozen the new attack damage value
     */
    public void setFrozen(final boolean frozen) {
        isFrozen = frozen;
    }

    /**
     * Getter for isTank.
     * @return the boolean value of isTank
     */
    public boolean isTank() {
        return isTank;
    }

    /**
     * Setter for isTank
     * @param newIsTank the new value of isTank
     */
    public void setTank(final boolean newIsTank) {
        this.isTank = newIsTank;
    }

    public Minion(final CardInput card) {
        setMana(card.getMana());
        setHealth(card.getHealth());
        setDescription(card.getDescription());
        setColors(card.getColors());
        setName(card.getName());
        setAttackDamage(card.getAttackDamage());
    }

    public Minion(final Minion card) {
        setMana(card.getMana());
        setHealth(card.getHealth());
        setDescription(card.getDescription());
        setColors(card.getColors());
        setName(card.getName());
        setAttackDamage(card.getAttackDamage());
    }

    /**
     * Factory-style constructor for a minion.
     * @param card the card to make a copy of
     * @return the new minion
     */
    public static Minion createMinion(final CardInput card) {
        return switch (card.getName()) {
            case "Disciple" -> new Disciple(card);
            case "Miraj" -> new Miraj(card);
            case "The Cursed One" -> new TheCursedOne(card);
            case "The Ripper" -> new TheRipper(card);
            case "Berserker" -> new Berserker(card);
            case "Sentinel" -> new Sentinel(card);
            case "Goliath" -> new Goliath(card);
            case "Warden" -> new Warden(card);

            default -> null;
        };
    }

    /**
     * Factory-style constructor for a minion.
     * @param card the minion to make a copy of
     * @return the new minion
     */
    public static Minion createMinion(final Minion card) {
        return switch (card.getName()) {
            case "Disciple" -> new Disciple(card);
            case "Miraj" -> new Miraj(card);
            case "The Cursed One" -> new TheCursedOne(card);
            case "The Ripper" -> new TheRipper(card);
            case "Berserker" -> new Berserker(card);
            case "Sentinel" -> new Sentinel(card);
            case "Goliath" -> new Goliath(card);
            case "Warden" -> new Warden(card);

            default -> null;
        };
    }

    /**
     * Reduces card health by a given value.
     * @param value the value to be reduced by
     */
    public void reduceHealth(final int value) {
        setHealth(getHealth() - value);
    }

    /**
     * Reduces card attackDamage by a given value.
     * @param value the value to be increased by
     */
    public void reduceAttack(final int value) {
        attackDamage -= value;
        if (attackDamage < 0) {
            attackDamage = 0;
        }
    }

    /**
     * Converts a minion to a JSON using Jackson library.
     * @return the minion as an ObjectNode-type JSON
     */
    @Override
    public ObjectNode convertToJSON() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();

        objectNode.put("mana", getMana());
        objectNode.put("attackDamage", attackDamage);
        objectNode.put("health", getHealth());
        objectNode.put("description", getDescription());

        for (String color : getColors()) {
            arrayNode.add(color);
        }

        objectNode.put("colors", arrayNode);
        objectNode.put("name", getName());

        return objectNode;
    }

    /**
     * Removes this minion off the board if it has been killed and
     * shifts all the minions to its right one position to the left.
     * @param board the board where the card is
     * @param x card row
     * @param y card column
     */
    public void removeIfDead(final Minion[][] board, final int x, final int y) {
        if (getHealth() <= 0) {
            board[x][y] = null;

            for (int j = y; j < Game.BOARD_COLUMNS - 1; j++) {
                board[x][j] = board[x][j + 1];
            }

            board[x][Game.BOARD_COLUMNS - 1] = null;
        }
    }

    /**
     * Places a minion on the row specific to it.
     * @param board the board to be played on
     * @param playerIdx the player who plays the minion
     * @return 1 if the minion could be placed on the board and 0 otherwise
     */
    public boolean placeOnBoard(final Minion[][] board, final int playerIdx) {
        return false;
    }
}
