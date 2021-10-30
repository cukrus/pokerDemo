package net.cukrus.pokerDemo.game;

import net.cukrus.pokerDemo.model.Card;
import net.cukrus.pokerDemo.model.CardCombo;

public abstract class CardCombinationResolver {
    protected abstract boolean internalCardsValid(Card... cards);
    protected abstract CardCombo internalResolveCombo(Card... cards);

    public CardCombo resolveCombo(Card... cards) {
        CardCombo result = null;
        if (internalCardsValid(cards)) {
            result = internalResolveCombo(cards);
        }
        return result;
    }
}
