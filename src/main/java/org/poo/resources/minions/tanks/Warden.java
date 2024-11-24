package org.poo.resources.minions.tanks;

import org.poo.fileio.CardInput;
import org.poo.resources.Placement;
import org.poo.resources.minions.Minion;
import org.poo.resources.minions.Tank;

public final class Warden extends Tank {
    @Override
    public boolean placeOnBoard(final Minion[][] board, final int playerIdx) {
        return Placement.placeOnFrontRow(this, board, playerIdx);
    }

    public Warden(final CardInput card) {
        super(card);
    }

    public Warden(final Minion card) {
        super(card);
    }
}
