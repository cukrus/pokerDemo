package net.cukrus.pokerDemo.game;

import net.cukrus.pokerDemo.model.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class FiveCardDrawExpressGame {
    private final List<Card> deck = Arrays.stream(Card.values()).filter(card -> card.getOrdinal() != 1).collect(Collectors.toList());
    private final List<Card> cardsDealt = new ArrayList<>();

    public List<Card> dealCards(int nrCards) {
        List<Card> result = new ArrayList<>();
        for(int i = 0; i < nrCards; i++) {
            int random = ThreadLocalRandom.current().nextInt(0, deck.size());
            Card card = deck.remove(random);
            cardsDealt.add(card);
            result.add(card);
        }
        return result;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public List<Card> getCardsDealt() {
        return cardsDealt;
    }
}
