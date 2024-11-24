package resources.heroes;

import fileio.CardInput;
import resources.Hero;
import resources.minions.Minion;
import setup.Game;

public class GeneralKocioraw extends Hero {
    public GeneralKocioraw(final CardInput card) {
        super(card);
        setAffectsEnemy(false);
    }

    public GeneralKocioraw(final Hero card) {
        super(card);
    }

    /**
     * Increases attack of all cards on the selected row by 1.
     * @param board the board the game is played on
     * @param affectedRow the row the hero uses the ability on
     */
    @Override
    public void useSpecialAbility(final Minion[][] board, final int affectedRow) {
        for (int j = 0; j < Game.BOARD_COLUMNS; j++) {
            if (board[affectedRow][j] != null) {
                board[affectedRow][j].reduceAttack(-1);
            }
        }
    }
}
