package resources.minions.special;

import fileio.CardInput;
import resources.Placement;
import resources.minions.Minion;
import resources.minions.SpecialMinion;
import setup.Game;

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
