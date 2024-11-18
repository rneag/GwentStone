package setup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.GameInput;
import resources.Card;
import resources.Decks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Match {
    private MatchInfo information;
    private ArrayList<Action> actions;
    private Player player1;
    private Player player2;
    private Card[][] board = new Card[4][5];
    private int currentRound;
    private int activePlayer = 0;

    private ArrayNode output;
    private ObjectMapper mapper = new ObjectMapper();

    public MatchInfo getInformation() {
        return information;
    }

    public void setInformation(MatchInfo information) {
        this.information = information;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Match(GameInput game, Decks playerOneDecks, Decks playerTwoDecks, ArrayNode output) {
        this.output = output;
        this.information = new MatchInfo(game.getStartGame());
        this.actions = new ArrayList<Action>();

        for (ActionsInput action : game.getActions()) {
            this.actions.add(new Action(action));
        }

        int playerOneDeckIdx = information.getPlayerOneDeckIdx();
        int playerTwoDeckIdx = information.getPlayerTwoDeckIdx();
        ArrayList<Card> playerOneDeck = playerOneDecks.getDecks().get(playerOneDeckIdx);
        ArrayList<Card> playerTwoDeck = playerTwoDecks.getDecks().get(playerTwoDeckIdx);

        this.player1 = new Player(playerOneDeck, information.getPlayerOneHero());
        this.player2 = new Player(playerTwoDeck, information.getPlayerTwoHero());

        Random random = new Random(information.getShuffleSeed());
        Collections.shuffle(player1.getDeck(), random);
        random = new Random(information.getShuffleSeed());
        Collections.shuffle(player2.getDeck(), random);

        currentRound = 0;
        startNewRound();
        this.activePlayer = information.getStartingPlayer();

        for (Action action : actions) {
            execute(this, action);
        }

    }

    void execute(Match match, Action action) {
        ObjectNode objectNode = mapper.createObjectNode();

        switch (action.getCommand()) {
            case "getCardsInHand":
                objectNode.put("command", action.getCommand());
                objectNode.put("playerIdx", action.getPlayerIdx());

                if (action.getPlayerIdx() == 1)
                    objectNode.put("output", getCardsInHand(player1));
                else
                    objectNode.put("output", getCardsInHand(player2));

                output.add(objectNode);
                break;

            case "getPlayerDeck":
                objectNode.put("command", action.getCommand());
                objectNode.put("playerIdx", action.getPlayerIdx());

                if (action.getPlayerIdx() == 1)
                    objectNode.put("output", getPlayerDeck(player1));
                else
                    objectNode.put("output", getPlayerDeck(player2));

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

                if (action.getPlayerIdx() == 1)
                    objectNode.put("output", player1.getHero().convertToJSON());
                else
                    objectNode.put("output", player2.getHero().convertToJSON());

                output.add(objectNode);
                break;

            case "getCardAtPosition":
                objectNode.put("command", action.getCommand());
                objectNode.put("x", action.getX());
                objectNode.put("y", action.getY());

                Card card = board[action.getX()][action.getY()];
                if (card == null)
                    objectNode.put("output", "No card available at that position.");
                else
                    objectNode.put("output", card.convertToJSON());

                output.add(objectNode);
                break;

            case "getPlayerMana":
                objectNode.put("command", action.getCommand());
                objectNode.put("playerIdx", action.getPlayerIdx());

                if (action.getPlayerIdx() == 1)
                    objectNode.put("output", player1.getMana());
                else
                    objectNode.put("output", player2.getMana());

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

                if (activePlayer == 1)
                    placeCardReturn = player1.playCard(board, action.getHandIdx(), 1);
                else
                    placeCardReturn = player2.playCard(board, action.getHandIdx(), 2);

                switch (placeCardReturn) {
                    case -1:
                        objectNode.put("command", action.getCommand());
                        objectNode.put("handIdx", action.getHandIdx());
                        objectNode.put("error", "Not enough mana to place card on table.");
                        output.add(objectNode);
                        break;

                    case 1:
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

                if (activePlayer == 1)
                    attackReturn = player1.attackCard(board, action.getCardAttacker().getX(), action.getCardAttacker().getY(),
                                            action.getCardAttacked().getX(), action.getCardAttacked().getY(), 1);
                else
                    attackReturn = player2.attackCard(board, action.getCardAttacker().getX(), action.getCardAttacker().getY(),
                            action.getCardAttacked().getX(), action.getCardAttacked().getY(), 2);

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
                    case -2:
                        objectNode.put("error", "Attacked card does not belong to the enemy.");
                        output.add(objectNode);
                        break;

                    case -1:
                        objectNode.put("error", "Attacker card has already attacked this turn.");
                        output.add(objectNode);
                        break;

                    case 1:
                        objectNode.put("error", "Attacker card is frozen.");
                        output.add(objectNode);
                        break;

                    case 2:
                        objectNode.put("error", "Attacked card is not of type 'Tank’.");
                        output.add(objectNode);
                        break;

                    default:
                        break;
                }
                break;

            case "cardUsesAbility":
                int cardAbilityReturn;

                if (activePlayer == 1)
                    cardAbilityReturn = player1.useCardAbility(board, action.getCardAttacker().getX(), action.getCardAttacker().getY(),
                            action.getCardAttacked().getX(), action.getCardAttacked().getY(), 1);
                else
                    cardAbilityReturn = player2.useCardAbility(board, action.getCardAttacker().getX(), action.getCardAttacker().getY(),
                            action.getCardAttacked().getX(), action.getCardAttacked().getY(), 2);

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
                    case -3:
                        objectNode.put("error", "Attacker card is frozen.");
                        output.add(objectNode);
                        break;

                    case -2:
                        objectNode.put("error", "Attacker card has already attacked this turn.");
                        output.add(objectNode);
                        break;

                    case -1:
                        objectNode.put("error", "Attacked card does not belong to the current player.");
                        output.add(objectNode);
                        break;

                    case 1:
                        objectNode.put("error", "Attacked card does not belong to the enemy.");
                        output.add(objectNode);
                        break;

                    case 2:
                        objectNode.put("error", "Attacked card is not of type 'Tank’.");
                        output.add(objectNode);
                        break;

                    default:
                        break;
                }
                break;

            case "useAttackHero":
                int attackHeroReturn;

                if (activePlayer == 1)
                    attackHeroReturn = player1.attackHero(board, action.getCardAttacker().getX(), action.getCardAttacker().getY(),
                            player2.getHero(), 1);
                else
                    attackHeroReturn = player2.attackHero(board, action.getCardAttacker().getX(), action.getCardAttacker().getY(),
                            player1.getHero(), 2);

                if (attackHeroReturn != 0) {
                    objectNode.put("command", action.getCommand());
                    ObjectNode attackerNode = mapper.createObjectNode();

                    attackerNode.put("x", action.getCardAttacker().getX());
                    attackerNode.put("y", action.getCardAttacker().getY());
                    objectNode.put("cardAttacker", attackerNode);
                }

                switch (attackHeroReturn) {
                    case -2:
                        objectNode.put("error", "Attacker card is frozen.");
                        output.add(objectNode);
                        break;

                    case -1:
                        objectNode.put("error", "Attacker card has already attacked this turn.");
                        output.add(objectNode);
                        break;

                    case 1:
                        objectNode.put("error", "Attacked card is not of type 'Tank'.");
                        output.add(objectNode);
                        break;

                    default:
                        if (activePlayer == 1) {
                            if (player2.getHero().getHealth() <= 0) {
                                Game.playerOneWins++;
                                objectNode.put("gameEnded", "Player one killed the enemy hero.");
                                output.add(objectNode);
                            }
                        } else {
                            if (player1.getHero().getHealth() <= 0) {
                                Game.playerTwoWins++;
                                objectNode.put("gameEnded", "Player two killed the enemy hero.");
                                output.add(objectNode);
                            }
                        }
                        break;
                }
                break;

            case "useHeroAbility":
                int heroAbilityReturn;

                if (activePlayer == 1)
                    heroAbilityReturn = player1.useHeroAbility(board, action.getAffectedRow(), 1);
                else
                    heroAbilityReturn = player2.useHeroAbility(board, action.getAffectedRow(), 2);

                if (heroAbilityReturn != 0) {
                    objectNode.put("command", action.getCommand());
                    objectNode.put("affectedRow", action.getAffectedRow());
                }

                switch (heroAbilityReturn) {
                    case -2:
                        objectNode.put("error", "Not enough mana to use hero's ability.");
                        output.add(objectNode);
                        break;

                    case -1:
                        objectNode.put("error", "Hero has already attacked this turn.");
                        output.add(objectNode);
                        break;

                    case 1:
                        objectNode.put("error", "Selected row does not belong to the enemy.");
                        output.add(objectNode);
                        break;

                    case 2:
                        objectNode.put("error", "Selected row does not belong to the current player.");
                        output.add(objectNode);
                        break;

                    default:
                        break;
                }
                break;

            case "getTotalGamesPlayed":
                objectNode.put("command", action.getCommand());
                objectNode.put("output", Game.playerOneWins + Game.playerTwoWins);

                output.add(objectNode);
                break;

            case "getPlayerOneWins":
                objectNode.put("command", action.getCommand());
                objectNode.put("output", Game.playerOneWins);

                output.add(objectNode);
                break;

            case "getPlayerTwoWins":
                objectNode.put("command", action.getCommand());
                objectNode.put("output", Game.playerTwoWins);

                output.add(objectNode);
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
            endRow = 4;
        }
        else
            player2.getHero().setAttacked(false);

        for (int i = startRow; i < endRow; i++)
            for (int j = 0; j < 5; j++)
                if (board[i][j] != null) {
                    board[i][j].setFrozen(false);
                    board[i][j].setAttacked(false);
                }

        activePlayer = activePlayer % 2 + 1;
        if (activePlayer == information.getStartingPlayer())
            startNewRound();
    }

    ArrayNode getCardsInHand(Player player) {
        ArrayNode arrayNode = mapper.createArrayNode();
        for (Card card : player.getHand())
            arrayNode.add(card.convertToJSON());

        return arrayNode;
    }

    ArrayNode getPlayerDeck(Player player) {
        ArrayNode arrayNode = mapper.createArrayNode();
        for (Card card : player.getDeck())
            arrayNode.add(card.convertToJSON());

        return arrayNode;
    }

    ArrayNode getCardsOnTable() {
        ArrayNode arrayNode = mapper.createArrayNode();
        for (int i = 0; i < 4; i++) {
            ArrayNode arrayNodeRow = mapper.createArrayNode();

            for (int j = 0; j < 5; j++)
                if (board[i][j] != null)
                    arrayNodeRow.add((board[i][j]).convertToJSON());

            arrayNode.add(arrayNodeRow);
        }
        return arrayNode;
    }

    ArrayNode getFrozenCardsOnTable() {
        ArrayNode arrayNode = mapper.createArrayNode();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 5; j++)
                if (board[i][j] != null && board[i][j].isFrozen())
                    arrayNode.add((board[i][j]).convertToJSON());

        return arrayNode;
    }
}
