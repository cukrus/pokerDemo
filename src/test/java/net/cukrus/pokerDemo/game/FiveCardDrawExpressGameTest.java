package net.cukrus.pokerDemo.game;

import net.cukrus.pokerDemo.model.Card;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FiveCardDrawExpressGameTest {

    @Test
    void dealCards() {
        FiveCardDrawExpressGame game = new FiveCardDrawExpressGame();
        List<Card> cardsDealt = game.dealCards(5);
        assertEquals(5, cardsDealt.size());
        assertTrue(game.getCardsDealt().containsAll(cardsDealt));
    }
}