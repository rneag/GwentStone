package org.poo.resources.minions.regular;

import org.poo.fileio.CardInput;
import org.poo.resources.Placement;
import org.poo.resources.minions.Minion;

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
