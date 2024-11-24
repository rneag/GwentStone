package org.poo.resources;

import org.poo.fileio.CardInput;
import org.poo.resources.minions.Minion;

import java.util.ArrayList;

public final class Decks {
    private int nrCardsInDeck;
    private int nrDecks;
    private ArrayList<ArrayList<Minion>> decks;

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public void setNrCardsInDeck(final int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    public int getNrDecks() {
        return nrDecks;
    }

    public void setNrDecks(final int nrDecks) {
        this.nrDecks = nrDecks;
    }

    public ArrayList<ArrayList<Minion>> getDecks() {
        return decks;
    }

    public void setDecks(final ArrayList<ArrayList<Minion>> decks) {
        this.decks = decks;
    }

    public Decks(final int nrCardsInDeck, final int nrDecks,
                 final ArrayList<ArrayList<CardInput>> decks) {
        this.nrCardsInDeck = nrCardsInDeck;
        this.nrDecks = nrDecks;
        this.decks = new ArrayList<ArrayList<Minion>>();

        for (int i = 0; i < decks.size(); i++) {
            this.decks.add(new ArrayList<Minion>());
            for (CardInput card : decks.get(i)) {
                this.decks.get(i).add(Minion.createMinion(card));
            }
        }
    }
}
