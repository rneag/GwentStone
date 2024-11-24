package resources.minions.special;

import fileio.CardInput;
import resources.Placement;
import resources.minions.Minion;
import resources.minions.SpecialMinion;

public final class Miraj extends SpecialMinion {
    @Override
    public boolean placeOnBoard(final Minion[][] board, final int playerIdx) {
        return Placement.placeOnFrontRow(this, board, playerIdx);
    }

    public Miraj(final CardInput card) {
        super(card);
        setAffectsEnemy(true);
    }

    public Miraj(final Minion card) {
        super(card);
        setAffectsEnemy(true);
    }

    @Override
    public void useSpecialAbility(final Minion minion) {
        int auxHealth = getHealth();
        setHealth(minion.getHealth());
        minion.setHealth(auxHealth);
    }
}
