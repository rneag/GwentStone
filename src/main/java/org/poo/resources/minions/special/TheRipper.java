package org.poo.resources.minions.special;

import org.poo.fileio.CardInput;
import org.poo.resources.Placement;
import org.poo.resources.minions.Minion;
import org.poo.resources.minions.SpecialMinion;
import org.poo.setup.Game;

public final class TheRipper extends SpecialMinion {
    @Override
    public boolean placeOnBoard(final Minion[][] board, final int playerIdx) {
        return Placement.placeOnFrontRow(this, board, playerIdx);
    }

    public TheRipper(final CardInput card) {
        super(card);
        setAffectsEnemy(true);
    }

    public TheRipper(final Minion card) {
        super(card);
        setAffectsEnemy(true);
    }

    @Override
    public void useSpecialAbility(final Minion minion) {
        minion.reduceAttack(Game.HERO_ABILITY);
    }
}
