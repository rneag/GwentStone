package resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;
import setup.Game;

import java.util.ArrayList;

public final class Card {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private boolean isFrozen = false;
    private boolean hasAttacked = false;

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(final boolean frozen) {
        isFrozen = frozen;
    }

    /**
     *
     * @return 1 if the card has already attacked and 0 otherwise
     */
    public boolean hasAttacked() {
        return hasAttacked;
    }

    public void setAttacked(final boolean newHasAttacked) {
        this.hasAttacked = newHasAttacked;
    }

    public Card(final CardInput card) {
        this.mana = card.getMana();
        this.attackDamage = card.getAttackDamage();
        this.health = card.getHealth();
        this.description = card.getDescription();
        this.colors = card.getColors();
        this.name = card.getName();
    }

    public Card(final Card card) {
        this.mana = card.mana;
        this.attackDamage = card.attackDamage;
        this.health = card.health;
        this.description = card.description;
        this.colors = card.colors;
        this.name = card.name;
    }

    public boolean isTank() {
        return name.equals("Goliath") || name.equals("Warden");
    }

    public boolean isHero() {
        return name.equals("Lord Royce") || name.equals("Empress Thorina")
                || name.equals("King Mudface") || name.equals("General Kocioraw");
    }

    /**
     * Reduces card health by a given value.
     * @param value
     */
    public void reduceHealth(final int value) {
        health -= value;
    }

    /**
     * Reduces card attackDamage by a given value.
     * @param value
     */
    public void reduceAttack(final int value) {
        attackDamage -= value;
        if (attackDamage < 0) {
            attackDamage = 0;
        }
    }

    /**
     * Converts a card to a JSON using Jackson library.
     * @return the card as an ObjectNode-type JSON
     */
    public ObjectNode convertToJSON() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();

        objectNode.put("mana", mana);
        if (!this.isHero()) {
            objectNode.put("attackDamage", attackDamage);
        }
        objectNode.put("health", health);
        objectNode.put("description", description);

        for (String color : colors) {
            arrayNode.add(color);
        }

        objectNode.put("colors", arrayNode);
        objectNode.put("name", name);

        return objectNode;
    }

    /**
     * Removes a minion off the board if it has been killed and
     * shifts all the minions to its right one position to the left.
     * @param board the board where the card is
     * @param x card row
     * @param y card column
     */
    public void removeIfDead(final Card[][] board, final int x, final int y) {
        if (health <= 0) {
            board[x][y] = null;

            for (int j = y; j < Game.BOARD_COLUMNS - 1; j++) {
                board[x][j] = board[x][j + 1];
            }

            board[x][Game.BOARD_COLUMNS - 1] = null;
        }
    }
}
