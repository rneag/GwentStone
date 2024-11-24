package org.poo.resources.minions.special;

import org.poo.fileio.CardInput;
import org.poo.resources.Placement;
import org.poo.resources.minions.Minion;
import org.poo.resources.minions.SpecialMinion;
import org.poo.setup.Game;

public final class Disciple extends SpecialMinion {
    @Override
    public boolean placeOnBoard(final Minion[][] board, final int playerIdx) {
        return Placement.placeOnBackRow(this, board, playerIdx);
    }

    public Disciple(final CardInput card) {
        super(card);
        setAffectsEnemy(false);
    }

    public Disciple(final Minion card) {
        super(card);
        setAffectsEnemy(false);
    }

    @Override
    public void useSpecialAbility(final Minion minion) {
        minion.reduceHealth(-Game.HERO_ABILITY);
    }
}
