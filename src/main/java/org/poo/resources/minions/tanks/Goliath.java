package org.poo.resources.minions.tanks;

import org.poo.fileio.CardInput;
import org.poo.resources.Placement;
import org.poo.resources.minions.Minion;
import org.poo.resources.minions.Tank;

public final class Goliath extends Tank {
    @Override
    public boolean placeOnBoard(final Minion[][] board, final int playerIdx) {
        return Placement.placeOnFrontRow(this, board, playerIdx);
    }

    public Goliath(final CardInput card) {
        super(card);
    }

    public Goliath(final Minion card) {
        super(card);
    }
}
