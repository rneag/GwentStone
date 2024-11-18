package resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

import java.util.ArrayList;

public class Card {
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

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    public boolean hasAttacked() {
        return hasAttacked;
    }

    public void setAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public Card(CardInput card) {
        this.mana = card.getMana();
        this.attackDamage = card.getAttackDamage();
        this.health = card.getHealth();
        this.description = card.getDescription();
        this.colors = card.getColors();
        this.name = card.getName();
    }

    public Card(Card card) {
        this.mana = card.mana;
        this.attackDamage = card.attackDamage;
        this.health = card.health;
        this.description = card.description;
        this.colors = card.colors;
        this.name = card.name;
    }

    boolean isHero() {
        return name.equals("Lord Royce") || name.equals("Empress Thorina")
                || name.equals("King Mudface") || name.equals("General Koncioraw");
    }

    public ObjectNode convertToJSON() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();

        objectNode.put("mana", mana);
        if (!this.isHero())
            objectNode.put("attackDamage", attackDamage);
        objectNode.put("health", health);
        objectNode.put("description", description);

        for (String color : colors)
            arrayNode.add(color);

        objectNode.put("colors", arrayNode);
        objectNode.put("name", name);

        return objectNode;
    }

    @Override
    public String toString() {
        return "Card{" +
                "mana=" + mana +
                ", attackDamage=" + attackDamage +
                ", health=" + health +
                ", description='" + description + '\'' +
                ", colors=" + colors +
                ", name='" + name + '\'' +
                ", isFrozen=" + isFrozen +
                ", hasAttacked=" + hasAttacked +
                '}';
    }
}
