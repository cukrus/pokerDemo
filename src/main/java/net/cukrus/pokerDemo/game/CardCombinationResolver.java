package net.cukrus.pokerDemo.game;

import net.cukrus.pokerDemo.model.Card;
import net.cukrus.pokerDemo.model.CardCombo;
import net.cukrus.pokerDemo.model.PokerHandInfo;

public abstract class CardCombinationResolver {
    protected abstract boolean internalCardsValid(Card... cards);
    protected abstract PokerHandInfo internalResolveCombo(CardsParseInfo cardsParseInfo);

    public PokerHandInfo resolveCombo(CardsParseInfo cardsParseInfo) {
        PokerHandInfo result = null;
        if (internalCardsValid(cardsParseInfo.getCards())) {
            result = internalResolveCombo(cardsParseInfo);
        }
        return result;
    }
}
