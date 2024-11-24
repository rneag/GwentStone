package org.poo.resources;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public abstract class Card {
    private int mana;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private boolean hasAttacked;

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
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

    /**
     * Getter for the card's hasAttacked value.
     * @return 1 if the card has already attacked and 0 otherwise
     */
    public boolean hasAttacked() {
        return hasAttacked;
    }

    /**
     * Setter for the card's hasAttacked value.
     * @param newHasAttacked the new value of hasAttacked
     */
    public void setAttacked(final boolean newHasAttacked) {
        this.hasAttacked = newHasAttacked;
    }

    /**
     * Reduces the card's health by a given value.
     * @param value the given value
     */
    public void reduceHealth(final int value) {
        setHealth(getHealth() - value);
    }

    /**
     * Converts a card to a JSON using Jackson library.
     * @return the card as an ObjectNode-type JSON
     */
    public abstract ObjectNode convertToJSON();
}
