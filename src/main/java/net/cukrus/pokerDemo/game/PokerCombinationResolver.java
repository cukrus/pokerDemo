package net.cukrus.pokerDemo.game;

import net.cukrus.pokerDemo.model.Card;
import net.cukrus.pokerDemo.model.CardSuit;
import net.cukrus.pokerDemo.model.PokerCardCombo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO refactor to some default poker combo resolver?
// Maybe create a PokerRules representation with params for combo resolving?
public class PokerCombinationResolver extends CardCombinationResolver {
    @Override
    protected boolean internalCardsValid(Card... cards) {
        return cards != null && cards.length == 5;
    }

    @Override
    protected PokerCardCombo internalResolveCombo(Card... cards) {
        PokerCardCombo result = null;
        Map<Integer, List<Card>> ordinalMap = new HashMap<>();
        Map<CardSuit, List<Card>> suitMap = new HashMap<>();
        for(Card card : cards) {
            int ordinal = card.getOrdinal();
            CardSuit suit = card.getSuit();
        }
        //TODO finish implementation
        return result;
    }
}
