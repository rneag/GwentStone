package org.poo.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CardInput;
import org.poo.resources.heroes.EmpressThorina;
import org.poo.resources.heroes.GeneralKocioraw;
import org.poo.resources.heroes.KingMudface;
import org.poo.resources.heroes.LordRoyce;
import org.poo.resources.minions.Minion;
import org.poo.setup.Game;

public class Hero extends Card {
    private boolean affectsEnemy;

    /**
     * Returns 1 if the hero's ability affects enemies and 0 otherwise.
     * @return the boolean value
     */
    public boolean getAffectsEnemy() {
        return affectsEnemy;
    }

    /**
     * Sets the affectsEnemy field of the hero.
     * @param affectsEnemy the new value for affectsEnemy
     */
    public void setAffectsEnemy(final boolean affectsEnemy) {
        this.affectsEnemy = affectsEnemy;
    }

    public Hero(final CardInput card) {
        setMana(card.getMana());
        setHealth(Game.HERO_HEALTH);
        setDescription(card.getDescription());
        setColors(card.getColors());
        setName(card.getName());
    }

    public Hero(final Hero hero) {
        setMana(hero.getMana());
        setHealth(hero.getHealth());
        setDescription(hero.getDescription());
        setColors(hero.getColors());
        setName(hero.getName());
        setAffectsEnemy(hero.getAffectsEnemy());
    }

    /**
     * Uses the hero's specific ability.
     * @param board the board the game is played on
     * @param affectedRow the row the players wants to affect
     */
    public void useSpecialAbility(final Minion[][] board, final int affectedRow) {

    }

    /**
     * Factory-style constructor for a hero.
     * @param card the card to make a copy of
     * @return the new hero
     */
    public static Hero createHero(final CardInput card) {
        return switch (card.getName()) {
            case "Empress Thorina" -> new EmpressThorina(card);
            case "General Kocioraw" -> new GeneralKocioraw(card);
            case "King Mudface" -> new KingMudface(card);
            case "Lord Royce" -> new LordRoyce(card);
            default -> null;
        };
    }

    /**
     * Factory-style constructor for a minion.
     * @param card the hero to make a copy of
     * @return the new minion
     */
    public static Hero createHero(final Hero card) {
        return switch (card.getName()) {
            case "Empress Thorina" -> new EmpressThorina(card);
            case "General Kocioraw" -> new GeneralKocioraw(card);
            case "King Mudface" -> new KingMudface(card);
            case "Lord Royce" -> new LordRoyce(card);
            default -> null;
        };
    }

    /**
     * Converts a hero to a JSON using Jackson library.
     * @return the hero as an ObjectNode-type JSON
     */
    @Override
    public ObjectNode convertToJSON() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();

        objectNode.put("mana", getMana());
        objectNode.put("description", getDescription());

        for (String color : getColors()) {
            arrayNode.add(color);
        }
        objectNode.put("colors", arrayNode);

        objectNode.put("name", getName());
        objectNode.put("health", getHealth());

        return objectNode;
    }
}
