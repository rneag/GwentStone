package org.poo.resources;

import org.poo.resources.minions.Minion;
import org.poo.setup.Game;

public abstract class Placement {
    /**
     * Helper function that plays a card on a given row.
     * @param card the card to be played
     * @param board the board the game is played on
     * @param row the row the card is to be played on
     * @return 1 if the card was placed successfully on the board, 0 otherwise
     */
    private static boolean placeOnRow(final Minion card, final Minion[][] board, final int row) {
        boolean wasPlaced = false;

        for (int j = 0; j < Game.BOARD_COLUMNS; j++) {
            if (board[row][j] == null) {
                board[row][j] = Minion.createMinion(card);
                wasPlaced = true;
                break;
            }
        }

        return wasPlaced;
    }

    /**
     * Places a card on the back row of the given player.
     * @param card the card to be played
     * @param board the board the game is played on
     * @param playerIdx the player who plays the card
     * @return 1 if the card was placed successfully on the board, 0 otherwise
     */
    public static boolean placeOnBackRow(final Minion card,
                                         final Minion[][] board, final int playerIdx) {
        int backRow = (playerIdx == 1) ? Game.BOARD_ROWS - 1 : 0;

        return placeOnRow(card, board, backRow);
    }

    /**
     * Places a card on the front row of the given player.
     * @param card the card to be played
     * @param board the board the game is played on
     * @param playerIdx the player who plays the card
     * @return 1 if the card was placed successfully on the board, 0 otherwise
     */
    public static boolean placeOnFrontRow(final Minion card,
                                          final Minion[][] board, final int playerIdx) {
        int frontRow = (playerIdx == 1) ? Game.BOARD_ROWS - 2 : 1;

        return placeOnRow(card, board, frontRow);
    }
}
