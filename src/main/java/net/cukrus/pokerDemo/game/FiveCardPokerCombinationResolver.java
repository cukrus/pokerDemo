package net.cukrus.pokerDemo.game;

import net.cukrus.pokerDemo.model.Card;
import net.cukrus.pokerDemo.model.PokerCardCombo;
import net.cukrus.pokerDemo.model.PokerHandInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//TODO refactor to some default poker combo resolver?
// Maybe create a PokerRules representation with params for combo resolving?
public class FiveCardPokerCombinationResolver extends CardCombinationResolver {
    @Override
    protected boolean internalCardsValid(Card... cards) {
        return cards != null && cards.length == 5;
    }

    @Override
    protected PokerHandInfo internalResolveCombo(CardsParseInfo cardsParseInfo) {
        int ordinalSum = Arrays.stream(cardsParseInfo.getCards()).mapToInt(card -> card.getOrdinal()).sum();
        PokerHandInfo result = new PokerHandInfo(cardsParseInfo, ordinalSum);
        int ordinalMapSize = cardsParseInfo.getOrdinalMap().size();
        boolean flush = false;
        boolean straight = false;
        if (cardsParseInfo.getSuitMap().size() == 1) {
            flush = true;
            result.setCombo(PokerCardCombo.FLUSH);
            result.setComboOrdinalSum(ordinalSum);
        }
        if (ordinalMapSize == 5) {
            result.setComboOrdinalSum(ordinalSum);
            List<Integer> keyList = new ArrayList<>(cardsParseInfo.getOrdinalMap().keySet());
            if (keyList.get(0) + 4 == keyList.get(4)) {
                straight = true;
            }
            if (straight) {
                result.setCombo(PokerCardCombo.STRAIGHT);
                if (flush) {
                    if (keyList.get(4) == 14) {
                        result.setCombo(PokerCardCombo.ROYAL_FLUSH);
                    } else {
                        result.setCombo(PokerCardCombo.STRAIGHT_FLUSH);
                    }
                }
            } else if (!flush) {
                result.setCombo(PokerCardCombo.CARD_HIGH);
            }
        } else {
            List<Integer> pairs = new ArrayList<>();
            int threes = 0;
            int fours = 0;
            for (Map.Entry<Integer, List<Card>> entry : cardsParseInfo.getOrdinalMap().entrySet()) {
                int entrySize = entry.getValue().size();
                switch (entrySize) {
                    case 2:
                        pairs.add(entry.getKey());
                        break;
                    case 3:
                        threes = entry.getKey();
                        break;
                    case 4:
                        fours = entry.getKey();
                        break;
                }
            }
            if (ordinalMapSize == 4 && pairs.size() == 1) {
                result.setComboOrdinalSum(2 * pairs.get(0));
                result.setCombo(PokerCardCombo.ONE_PAIR);
            } else if (ordinalMapSize == 3) {
                if (threes != 0) {
                    result.setComboOrdinalSum(threes * 3);
                    result.setCombo(PokerCardCombo.THREE_OF_A_KIND);
                } else {
                    result.setComboOrdinalSum(pairs.stream().mapToInt(ord -> 2 * ord).sum());
                    result.setCombo(PokerCardCombo.TWO_PAIRS);
                }
            } else if (ordinalMapSize == 2) {
                if (fours != 0) {
                    result.setComboOrdinalSum(fours * 4);
                    result.setCombo(PokerCardCombo.FOUR_OF_A_KIND);
                } else {
                    result.setComboOrdinalSum((threes * 3) + (2 * pairs.get(0)));
                    result.setCombo(PokerCardCombo.FULL_HOUSE);
                }
            }
        }
        return result;
    }
}
