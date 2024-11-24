package resources.minions.tanks;

import fileio.CardInput;
import resources.Placement;
import resources.minions.Minion;
import resources.minions.Tank;

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
