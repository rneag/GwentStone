package resources.minions.regular;

import fileio.CardInput;
import resources.Placement;
import resources.minions.Minion;

public final class Sentinel extends Minion {
    @Override
    public boolean placeOnBoard(final Minion[][] board, final int playerIdx) {
        return Placement.placeOnBackRow(this, board, playerIdx);
    }

    public Sentinel(final CardInput card) {
        super(card);
    }

    public Sentinel(final Minion card) {
        super(card);
    }
}
