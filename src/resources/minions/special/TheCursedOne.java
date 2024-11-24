package resources.minions.special;

import fileio.CardInput;
import resources.Placement;
import resources.minions.Minion;
import resources.minions.SpecialMinion;

public final class TheCursedOne extends SpecialMinion {
    @Override
    public boolean placeOnBoard(final Minion[][] board, final int playerIdx) {
        return Placement.placeOnBackRow(this, board, playerIdx);
    }

    public TheCursedOne(final CardInput card) {
        super(card);
        setAffectsEnemy(true);
    }

    public TheCursedOne(final Minion card) {
        super(card);
        setAffectsEnemy(true);
    }

    @Override
    public void useSpecialAbility(final Minion minion) {
        int aux = minion.getHealth();
        minion.setHealth(minion.getAttackDamage());
        minion.setAttackDamage(aux);
    }
}
