package resources;

import fileio.CardInput;

import java.util.ArrayList;

public class Decks {
    private int nrCardsInDeck;
    private int nrDecks;
    private ArrayList<ArrayList<Card>> decks;

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public void setNrCardsInDeck(int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    public int getNrDecks() {
        return nrDecks;
    }

    public void setNrDecks(int nrDecks) {
        this.nrDecks = nrDecks;
    }

    public ArrayList<ArrayList<Card>> getDecks() {
        return decks;
    }

    public void setDecks(ArrayList<ArrayList<Card>> decks) {
        this.decks = decks;
    }

    public Decks(int nrCardsInDeck, int nrDecks, ArrayList<ArrayList<CardInput>> decks) {
        this.nrCardsInDeck = nrCardsInDeck;
        this.nrDecks = nrDecks;
        this.decks = new ArrayList<ArrayList<Card>>();

        for (int i = 0; i < decks.size(); i++) {
            this.decks.add(new ArrayList<Card>());
            for (CardInput card : decks.get(i)) {
                this.decks.get(i).add(new Card(card));
            }
        }
    }
}
