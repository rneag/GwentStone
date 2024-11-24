package resources.minions.regular;

import fileio.CardInput;
import resources.Placement;
import resources.minions.Minion;

public final class Berserker extends Minion {
    @Override
    public boolean placeOnBoard(final Minion[][] board, final int playerIdx) {
        return Placement.placeOnBackRow(this, board, playerIdx);
    }

    public Berserker(final CardInput card) {
        super(card);
    }

    public Berserker(final Minion card) {
        super(card);
    }
}
