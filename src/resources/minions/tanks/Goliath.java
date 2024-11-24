package resources.minions.tanks;

import fileio.CardInput;
import resources.Placement;
import resources.minions.Minion;
import resources.minions.Tank;

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
