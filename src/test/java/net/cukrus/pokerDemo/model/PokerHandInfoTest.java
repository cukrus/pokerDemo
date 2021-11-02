package net.cukrus.pokerDemo.model;

import net.cukrus.pokerDemo.game.CardsParseInfo;
import net.cukrus.pokerDemo.game.FiveCardPokerCombinationResolver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PokerHandInfoTest {
    private FiveCardPokerCombinationResolver resolver = new FiveCardPokerCombinationResolver();

    @Test
    void compareTo_comboDiffers() {
        //given
        PokerHandInfo straight = resolver.resolveCombo(new CardsParseInfo(new Card[]{Card.S_2, Card.C_3, Card.S_4, Card.C_5, Card.H_6}));
        PokerHandInfo flush = resolver.resolveCombo(new CardsParseInfo(new Card[]{Card.S_2, Card.S_4, Card.S_6, Card.S_8, Card.S_10}));

        //when
        int result = straight.compareTo(flush);

        //then
        assertEquals(-1, result);
    }

    @Test
    void compareTo_comboSameComboOrdinalDiffers() {
        //given
        PokerHandInfo straight6High = resolver.resolveCombo(new CardsParseInfo(new Card[]{Card.S_2, Card.C_3, Card.S_4, Card.C_5, Card.H_6}));
        PokerHandInfo straight7High = resolver.resolveCombo(new CardsParseInfo(new Card[]{Card.C_3, Card.S_4, Card.C_5, Card.H_6, Card.S_7}));

        //when
        int result = straight6High.compareTo(straight7High);

        //then
        assertEquals(-1, result);
    }

    @Test
    void compareTo_comboSameComboOrdinalSameHandOrdinalDiffers() {
        //given
        PokerHandInfo twoPairsAndAce = resolver.resolveCombo(new CardsParseInfo(new Card[]{Card.S_2, Card.C_2, Card.S_4, Card.C_4, Card.H_A}));
        PokerHandInfo twoPairsAndKing = resolver.resolveCombo(new CardsParseInfo(new Card[]{Card.S_2, Card.C_2, Card.S_4, Card.C_4, Card.H_K}));

        //when
        int result = twoPairsAndAce.compareTo(twoPairsAndKing);

        //then
        assertEquals(1, result);
    }

    @Test
    void compareTo_equals() {
        //given
        PokerHandInfo twoPairsAndAce = resolver.resolveCombo(new CardsParseInfo(new Card[]{Card.S_2, Card.C_2, Card.S_4, Card.C_4, Card.H_A}));
        PokerHandInfo twoPairsAndAce2 = resolver.resolveCombo(new CardsParseInfo(new Card[]{Card.D_2, Card.H_2, Card.D_4, Card.H_4, Card.D_A}));

        //when
        int result = twoPairsAndAce.compareTo(twoPairsAndAce2);

        //then
        assertEquals(0, result);
    }
}