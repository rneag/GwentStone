package org.poo.resources.heroes;

import org.poo.fileio.CardInput;
import org.poo.resources.Hero;
import org.poo.resources.minions.Minion;
import org.poo.setup.Game;

public class EmpressThorina extends Hero {
    public EmpressThorina(final CardInput card) {
        super(card);
        setAffectsEnemy(true);
    }

    public EmpressThorina(final Hero card) {
        super(card);
    }

    /**
     * Destroys the card with the lowest health on the row.
     * @param board the board the game is played on
     * @param affectedRow the row the hero uses the ability on
     */
    @Override
    public void useSpecialAbility(final Minion[][] board, final int affectedRow) {
        int maxHealth = 0, colIdx = -1;

        for (int j = 0; j < Game.BOARD_COLUMNS; j++) {
            if (board[affectedRow][j] != null
                    && board[affectedRow][j].getHealth() > maxHealth) {
                maxHealth = board[affectedRow][j].getHealth();
                colIdx = j;
            }
        }

        board[affectedRow][colIdx].setHealth(0);
        board[affectedRow][colIdx].removeIfDead(board, affectedRow, colIdx);
    }
}
