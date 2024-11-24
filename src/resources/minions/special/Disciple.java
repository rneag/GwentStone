package resources.minions.special;

import fileio.CardInput;
import resources.Placement;
import resources.minions.Minion;
import resources.minions.SpecialMinion;
import setup.Game;

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
