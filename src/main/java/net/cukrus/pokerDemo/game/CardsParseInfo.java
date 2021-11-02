package net.cukrus.pokerDemo.game;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.cukrus.pokerDemo.model.Card;
import net.cukrus.pokerDemo.model.CardSuit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@JsonIgnoreProperties({ "ordinalMap", "suitMap" })
public final class CardsParseInfo {
    private final Card[] cards;
    private final Map<Integer, List<Card>> ordinalMap = new TreeMap<>();
    private final Map<CardSuit, List<Card>> suitMap = new HashMap<>();

    public CardsParseInfo(Card[] cards) {
        this.cards = cards;
        for(Card card : cards) {
            int ordinal = card.getOrdinal();
            if(!ordinalMap.containsKey(ordinal)) {
                ordinalMap.put(ordinal, new ArrayList<>());
            }
            List<Card> ordinalList = ordinalMap.get(ordinal);
            ordinalList.add(card);

            CardSuit suit = card.getSuit();
            if(!suitMap.containsKey(suit)) {
                suitMap.put(suit, new ArrayList<>());
            }
            List<Card> suitList = suitMap.get(suit);
            suitList.add(card);
        }
    }

    public Card[] getCards() {
        return cards;
    }

    public Map<Integer, List<Card>> getOrdinalMap() {
        return ordinalMap;
    }

    public Map<CardSuit, List<Card>> getSuitMap() {
        return suitMap;
    }
}
