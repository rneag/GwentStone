package org.poo.setup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.GameInput;
import org.poo.resources.Decks;
import org.poo.resources.minions.Minion;

import java.util.ArrayList;

public final class Match {
    private MatchInfo information;
    private ArrayList<Action> actions;
    private Player player1;
    private Player player2;
    private final Minion[][] board = new Minion[Game.BOARD_ROWS][Game.BOARD_COLUMNS];
    private int currentRound;
    private int activePlayer;

    private final ArrayNode output;
    private final ObjectMapper mapper = new ObjectMapper();

    public MatchInfo getInformation() {
        return information;
    }

    public void setInformation(final MatchInfo information) {
        this.information = information;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(final ArrayList<Action> actions) {
        this.actions = actions;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(final Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(final Player player2) {
        this.player2 = player2;
    }

    public Match(final GameInput game, final Decks playerOneDecks, final Decks playerTwoDecks,
                 final ArrayNode output) {
        this.output = output;
        this.information = new MatchInfo(game.getStartGame());
        this.actions = new ArrayList<Action>();

        for (ActionsInput action : game.getActions()) {
            this.actions.add(new Action(action));
        }

        int playerOneDeckIdx = information.getPlayerOneDeckIdx();
        int playerTwoDeckIdx = information.getPlayerTwoDeckIdx();
        ArrayList<Minion> playerOneDeck = playerOneDecks.getDecks().get(playerOneDeckIdx);
        ArrayList<Minion> playerTwoDeck = playerTwoDecks.getDecks().get(playerTwoDeckIdx);

        this.player1 = new Player(playerOneDeck, information.getPlayerOneHero());
        this.player2 = new Player(playerTwoDeck, information.getPlayerTwoHero());

        player1.shuffleDeck(information.getShuffleSeed());
        player2.shuffleDeck(information.getShuffleSeed());

        currentRound = 0;
        startNewRound();
        this.activePlayer = information.getStartingPlayer();

        for (Action action : actions) {
            execute(action);
        }
    }

    void execute(final Action action) {
        ObjectNode objectNode = mapper.createObjectNode();

        switch (action.getCommand()) {
            case "getCardsInHand":
                objectNode.put("command", action.getCommand());
                objectNode.put("playerIdx", action.getPlayerIdx());

                if (action.getPlayerIdx() == 1) {
                    objectNode.put("output", player1.getCardsInHand());
                } else {
                    objectNode.put("output", player2.getCardsInHand());
                }

                output.add(objectNode);
                break;

            case "getPlayerDeck":
                objectNode.put("command", action.getCommand());
                objectNode.put("playerIdx", action.getPlayerIdx());

                if (action.getPlayerIdx() == 1) {
                    objectNode.put("output", player1.getPlayerDeck());
                } else {
                    objectNode.put("output", player2.getPlayerDeck());
                }

                output.add(objectNode);
                break;

            case "getCardsOnTable":
                objectNode.put("command", action.getCommand());
                objectNode.put("output", getCardsOnTable());

                output.add(objectNode);
                break;

            case "getPlayerTurn":
                objectNode.put("command", action.getCommand());
                objectNode.put("output", activePlayer);

                output.add(objectNode);
                break;

            case "getPlayerHero":
                objectNode.put("command", action.getCommand());
                objectNode.put("playerIdx", action.getPlayerIdx());

                if (action.getPlayerIdx() == 1) {
                    objectNode.put("output", player1.getHero().convertToJSON());
                } else {
                    objectNode.put("output", player2.getHero().convertToJSON());
                }

                output.add(objectNode);
                break;

            case "getCardAtPosition":
                objectNode.put("command", action.getCommand());
                objectNode.put("x", action.getX());
                objectNode.put("y", action.getY());

                Minion card = board[action.getX()][action.getY()];
                if (card == null) {
                    objectNode.put("output", "No card available at that position.");
                } else {
                    objectNode.put("output", card.convertToJSON());
                }

                output.add(objectNode);
                break;

            case "getPlayerMana":
                objectNode.put("command", action.getCommand());
                objectNode.put("playerIdx", action.getPlayerIdx());

                if (action.getPlayerIdx() == 1) {
                    objectNode.put("output", player1.getMana());
                } else {
                    objectNode.put("output", player2.getMana());
                }

                output.add(objectNode);
                break;

            case "getFrozenCardsOnTable":
                objectNode.put("command", action.getCommand());
                objectNode.put("output", getFrozenCardsOnTable());

                output.add(objectNode);
                break;

            case "endPlayerTurn":
                endPlayerTurn();
                break;

            case "placeCard":
                int placeCardReturn;

                if (activePlayer == 1) {
                    placeCardReturn = player1.playCard(board, action.getHandIdx(), 1);
                } else {
                    placeCardReturn = player2.playCard(board, action.getHandIdx(), 2);
                }

                switch (placeCardReturn) {
                    case Game.OUT_OF_MANA:
                        objectNode.put("command", action.getCommand());
                        objectNode.put("handIdx", action.getHandIdx());
                        objectNode.put("error", "Not enough mana to place card on table.");
                        output.add(objectNode);
                        break;

                    case Game.NO_ROOM:
                        objectNode.put("command", action.getCommand());
                        objectNode.put("handIdx", action.getHandIdx());
                        objectNode.put("error", "Cannot place card on table since row is full.");
                        output.add(objectNode);
                        break;

                    default:
                        break;
                }
                break;

            case "cardUsesAttack":
                int attackReturn;

                if (activePlayer == 1) {
                    attackReturn = player1.attackCard(board, action.getCardAttacker().getX(),
                            action.getCardAttacker().getY(), action.getCardAttacked().getX(),
                            action.getCardAttacked().getY(), 1);
                } else {
                    attackReturn = player2.attackCard(board, action.getCardAttacker().getX(),
                            action.getCardAttacker().getY(), action.getCardAttacked().getX(),
                            action.getCardAttacked().getY(), 2);
                }

                if (attackReturn != 0) {
                    objectNode.put("command", action.getCommand());
                    ObjectNode attackerNode = mapper.createObjectNode();
                    ObjectNode attackedNode = mapper.createObjectNode();

                    attackerNode.put("x", action.getCardAttacker().getX());
                    attackerNode.put("y", action.getCardAttacker().getY());
                    objectNode.put("cardAttacker", attackerNode);

                    attackedNode.put("x", action.getCardAttacked().getX());
                    attackedNode.put("y", action.getCardAttacked().getY());
                    objectNode.put("cardAttacked", attackedNode);
                }

                switch (attackReturn) {
                    case Game.NOT_ENEMY:
                        objectNode.put("error", "Attacked card does not belong to the enemy.");
                        output.add(objectNode);
                        break;

                    case Game.ALREADY_ATTACKED:
                        objectNode.put("error", "Attacker card has already attacked this turn.");
                        output.add(objectNode);
                        break;

                    case Game.IS_FROZEN:
                        objectNode.put("error", "Attacker card is frozen.");
                        output.add(objectNode);
                        break;

                    case Game.ENEMY_HAS_TAUNT:
                        objectNode.put("error", "Attacked card is not of type 'Tank'.");
                        output.add(objectNode);
                        break;

                    default:
                        break;
                }
                break;

            case "cardUsesAbility":
                int cardAbilityReturn;

                if (activePlayer == 1) {
                    cardAbilityReturn = player1.useCardAbility(board,
                            action.getCardAttacker().getX(), action.getCardAttacker().getY(),
                            action.getCardAttacked().getX(), action.getCardAttacked().getY(), 1);
                } else {
                    cardAbilityReturn = player2.useCardAbility(board,
                            action.getCardAttacker().getX(), action.getCardAttacker().getY(),
                            action.getCardAttacked().getX(), action.getCardAttacked().getY(), 2);
                }

                if (cardAbilityReturn != 0) {
                    objectNode.put("command", action.getCommand());
                    ObjectNode attackerNode = mapper.createObjectNode();
                    ObjectNode attackedNode = mapper.createObjectNode();

                    attackerNode.put("x", action.getCardAttacker().getX());
                    attackerNode.put("y", action.getCardAttacker().getY());
                    objectNode.put("cardAttacker", attackerNode);

                    attackedNode.put("x", action.getCardAttacked().getX());
                    attackedNode.put("y", action.getCardAttacked().getY());
                    objectNode.put("cardAttacked", attackedNode);
                }

                switch (cardAbilityReturn) {
                    case Game.IS_FROZEN:
                        objectNode.put("error", "Attacker card is frozen.");
                        output.add(objectNode);
                        break;

                    case Game.ALREADY_ATTACKED:
                        objectNode.put("error", "Attacker card has already attacked this turn.");
                        output.add(objectNode);
                        break;

                    case Game.NOT_ALLY:
                        objectNode.put("error",
                                "Attacked card does not belong to the current player.");
                        output.add(objectNode);
                        break;

                    case Game.NOT_ENEMY:
                        objectNode.put("error", "Attacked card does not belong to the enemy.");
                        output.add(objectNode);
                        break;

                    case Game.ENEMY_HAS_TAUNT:
                        objectNode.put("error", "Attacked card is not of type 'Tank'.");
                        output.add(objectNode);
                        break;

                    default:
                        break;
                }
                break;

            case "useAttackHero":
                int attackHeroReturn;

                if (activePlayer == 1) {
                    attackHeroReturn = player1.attackHero(board, action.getCardAttacker().getX(),
                            action.getCardAttacker().getY(), player2.getHero(), 1);
                } else {
                    attackHeroReturn = player2.attackHero(board, action.getCardAttacker().getX(),
                            action.getCardAttacker().getY(), player1.getHero(), 2);
                }

                if (attackHeroReturn != 0) {
                    objectNode.put("command", action.getCommand());
                    ObjectNode attackerNode = mapper.createObjectNode();

                    attackerNode.put("x", action.getCardAttacker().getX());
                    attackerNode.put("y", action.getCardAttacker().getY());
                    objectNode.put("cardAttacker", attackerNode);
                }

                switch (attackHeroReturn) {
                    case Game.IS_FROZEN:
                        objectNode.put("error", "Attacker card is frozen.");
                        output.add(objectNode);
                        break;

                    case Game.ALREADY_ATTACKED:
                        objectNode.put("error", "Attacker card has already attacked this turn.");
                        output.add(objectNode);
                        break;

                    case Game.ENEMY_HAS_TAUNT:
                        objectNode.put("error", "Attacked card is not of type 'Tank'.");
                        output.add(objectNode);
                        break;

                    default:
                        if (activePlayer == 1) {
                            if (player2.getHero().getHealth() <= 0) {
                                Game.incrementPlayerOneWins();
                                objectNode.put("gameEnded", "Player one killed the enemy hero.");
                                output.add(objectNode);
                            }
                        } else {
                            if (player1.getHero().getHealth() <= 0) {
                                Game.incrementPlayerTwoWins();
                                objectNode.put("gameEnded", "Player two killed the enemy hero.");
                                output.add(objectNode);
                            }
                        }
                        break;
                }
                break;

            case "useHeroAbility":
                int heroAbilityReturn;

                if (activePlayer == 1) {
                    heroAbilityReturn = player1.useHeroAbility(board, action.getAffectedRow(), 1);
                } else {
                    heroAbilityReturn = player2.useHeroAbility(board, action.getAffectedRow(), 2);
                }

                if (heroAbilityReturn != 0) {
                    objectNode.put("command", action.getCommand());
                    objectNode.put("affectedRow", action.getAffectedRow());
                }

                switch (heroAbilityReturn) {
                    case Game.OUT_OF_MANA:
                        objectNode.put("error", "Not enough mana to use hero's ability.");
                        output.add(objectNode);
                        break;

                    case Game.ALREADY_ATTACKED:
                        objectNode.put("error", "Hero has already attacked this turn.");
                        output.add(objectNode);
                        break;

                    case Game.NOT_ENEMY:
                        objectNode.put("error", "Selected row does not belong to the enemy.");
                        output.add(objectNode);
                        break;

                    case Game.NOT_ALLY:
                        objectNode.put("error",
                                "Selected row does not belong to the current player.");
                        output.add(objectNode);
                        break;

                    default:
                        break;
                }
                break;

            case "getTotalGamesPlayed":
                objectNode.put("command", action.getCommand());
                objectNode.put("output", Game.getPlayerOneWins() + Game.getPlayerTwoWins());

                output.add(objectNode);
                break;

            case "getPlayerOneWins":
                objectNode.put("command", action.getCommand());
                objectNode.put("output", Game.getPlayerOneWins());

                output.add(objectNode);
                break;

            case "getPlayerTwoWins":
                objectNode.put("command", action.getCommand());
                objectNode.put("output", Game.getPlayerTwoWins());

                output.add(objectNode);
                break;

            default:
                break;
        }
    }

    void startNewRound() {
        currentRound++;
        player1.increaseMana(currentRound);
        player2.increaseMana(currentRound);

        player1.addCardToHand();
        player2.addCardToHand();
    }

    void endPlayerTurn() {
        int startRow = 0, endRow = 2;

        if (activePlayer == 1) {
            player1.getHero().setAttacked(false);
            startRow = 2;
            endRow = Game.BOARD_ROWS;
        } else {
            player2.getHero().setAttacked(false);
        }

        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < Game.BOARD_COLUMNS; j++) {
                if (board[i][j] != null) {
                    board[i][j].setFrozen(false);
                    board[i][j].setAttacked(false);
                }
            }
        }

        activePlayer = activePlayer % 2 + 1;
        if (activePlayer == information.getStartingPlayer()) {
            startNewRound();
        }
    }

    ArrayNode getCardsOnTable() {
        ArrayNode arrayNode = mapper.createArrayNode();
        for (int i = 0; i < Game.BOARD_ROWS; i++) {
            ArrayNode arrayNodeRow = mapper.createArrayNode();

            for (int j = 0; j < Game.BOARD_COLUMNS; j++) {
                if (board[i][j] != null) {
                    arrayNodeRow.add((board[i][j]).convertToJSON());
                }
            }
            arrayNode.add(arrayNodeRow);
        }
        return arrayNode;
    }

    ArrayNode getFrozenCardsOnTable() {
        ArrayNode arrayNode = mapper.createArrayNode();
        for (int i = 0; i < Game.BOARD_ROWS; i++) {
            for (int j = 0; j < Game.BOARD_COLUMNS; j++) {
                if (board[i][j] != null && board[i][j].isFrozen()) {
                    arrayNode.add((board[i][j]).convertToJSON());
                }
            }
        }

        return arrayNode;
    }
}
