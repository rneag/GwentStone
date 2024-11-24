package org.poo.resources.minions;

import org.poo.fileio.CardInput;

public class SpecialMinion extends Minion {
    private boolean affectsEnemy;

    /**
     * Returns 1 if the card's ability affects enemies and 0 otherwise.
     * @return the boolean value
     */
    public boolean getAffectsEnemy() {
        return affectsEnemy;
    }

    /**
     * Sets the affectsEnemy field of the minion.
     * @param affectsEnemy the new value for affectsEnemy
     */
    public void setAffectsEnemy(final boolean affectsEnemy) {
        this.affectsEnemy = affectsEnemy;
    }

    public SpecialMinion(final CardInput card) {
        super(card);
    }

    public SpecialMinion(final Minion card) {
        super(card);
    }

    /**
     * Uses a minion's special ability.
     * @param minion the minion who uses the ability
     */
    public void useSpecialAbility(final Minion minion) {

    }
}
