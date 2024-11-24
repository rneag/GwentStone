package org.poo.resources.minions;

import org.poo.fileio.CardInput;

public class Tank extends Minion {
    public Tank(final CardInput card) {
        super(card);
        setTank(true);
    }

    public Tank(final Minion card) {
        super(card);
        setTank(true);
    }
}
