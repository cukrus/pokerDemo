package net.cukrus.pokerDemo.game;

import net.cukrus.pokerDemo.model.Card;
import net.cukrus.pokerDemo.model.PokerCardCombo;
import net.cukrus.pokerDemo.model.PokerHandInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FiveCardPokerCombinationResolverTest {
    private FiveCardPokerCombinationResolver resolver = new FiveCardPokerCombinationResolver();

    @Test
    void internalCardsValid_valid() {
        boolean result = resolver.internalCardsValid(Card.S_2, Card.S_3, Card.S_4, Card.S_5, Card.S_6);
        assertTrue(result);
    }
    @Test
    void internalCardsValid_invalid() {
        boolean result = resolver.internalCardsValid();
        assertFalse(result);
        result = resolver.internalCardsValid(Card.S_2, Card.S_3, Card.S_4, Card.S_5);
        assertFalse(result);
    }

    @Test
    void internalResolveCombo_cardHigh() {
        PokerHandInfo result = resolver.internalResolveCombo(new CardsParseInfo(new Card[]{Card.S_2, Card.C_4, Card.S_6, Card.C_8, Card.H_10}));
        assertEquals(PokerCardCombo.CARD_HIGH, result.getCombo());
        assertEquals(30, result.getHandOrdinalSum());
        assertEquals(30, result.getComboOrdinalSum());
    }

    @Test
    void internalResolveCombo_pair() {
        PokerHandInfo result = resolver.internalResolveCombo(new CardsParseInfo(new Card[]{Card.S_2, Card.C_4, Card.S_6, Card.C_8, Card.H_8}));
        assertEquals(PokerCardCombo.ONE_PAIR, result.getCombo());
        assertEquals(28, result.getHandOrdinalSum());
        assertEquals(16, result.getComboOrdinalSum());
    }

    @Test
    void internalResolveCombo_2pairs() {
        PokerHandInfo result = resolver.internalResolveCombo(new CardsParseInfo(new Card[]{Card.S_2, Card.C_2, Card.S_6, Card.C_8, Card.H_8}));
        assertEquals(PokerCardCombo.TWO_PAIRS, result.getCombo());
        assertEquals(26, result.getHandOrdinalSum());
        assertEquals(20, result.getComboOrdinalSum());
    }

    @Test
    void internalResolveCombo_3OfAKind() {
        PokerHandInfo result = resolver.internalResolveCombo(new CardsParseInfo(new Card[]{Card.S_2, Card.C_2, Card.H_2, Card.C_8, Card.H_10}));
        assertEquals(PokerCardCombo.THREE_OF_A_KIND, result.getCombo());
        assertEquals(24, result.getHandOrdinalSum());
        assertEquals(6, result.getComboOrdinalSum());
    }

    @Test
    void internalResolveCombo_straight() {
        PokerHandInfo result = resolver.internalResolveCombo(new CardsParseInfo(new Card[]{Card.S_2, Card.C_3, Card.S_4, Card.C_5, Card.H_6}));
        assertEquals(PokerCardCombo.STRAIGHT, result.getCombo());
        assertEquals(20, result.getHandOrdinalSum());
        assertEquals(20, result.getComboOrdinalSum());
    }

    @Test
    void internalResolveCombo_flush() {
        PokerHandInfo result = resolver.internalResolveCombo(new CardsParseInfo(new Card[]{Card.S_2, Card.S_4, Card.S_6, Card.S_8, Card.S_10}));
        assertEquals(PokerCardCombo.FLUSH, result.getCombo());
        assertEquals(30, result.getHandOrdinalSum());
        assertEquals(30, result.getComboOrdinalSum());
    }

    @Test
    void internalResolveCombo_fullHouse() {
        PokerHandInfo result = resolver.internalResolveCombo(new CardsParseInfo(new Card[]{Card.S_2, Card.C_2, Card.H_2, Card.C_8, Card.H_8}));
        assertEquals(PokerCardCombo.FULL_HOUSE, result.getCombo());
        assertEquals(22, result.getHandOrdinalSum());
        assertEquals(22, result.getComboOrdinalSum());
    }

    @Test
    void internalResolveCombo_4OfAKind() {
        PokerHandInfo result = resolver.internalResolveCombo(new CardsParseInfo(new Card[]{Card.S_2, Card.C_2, Card.H_2, Card.D_2, Card.H_10}));
        assertEquals(PokerCardCombo.FOUR_OF_A_KIND, result.getCombo());
        assertEquals(18, result.getHandOrdinalSum());
        assertEquals(8, result.getComboOrdinalSum());
    }

    @Test
    void internalResolveCombo_straightFlush() {
        PokerHandInfo result = resolver.internalResolveCombo(new CardsParseInfo(new Card[]{Card.S_2, Card.S_4, Card.S_3, Card.S_6, Card.S_5}));
        assertEquals(PokerCardCombo.STRAIGHT_FLUSH, result.getCombo());
        assertEquals(20, result.getHandOrdinalSum());
        assertEquals(20, result.getComboOrdinalSum());
    }

    @Test
    void internalResolveCombo_royalFlush() {
        PokerHandInfo result = resolver.internalResolveCombo(new CardsParseInfo(new Card[]{Card.S_A, Card.S_K, Card.S_Q, Card.S_J, Card.S_10}));
        assertEquals(PokerCardCombo.ROYAL_FLUSH, result.getCombo());
        assertEquals(60, result.getHandOrdinalSum());
        assertEquals(60, result.getComboOrdinalSum());
    }
}