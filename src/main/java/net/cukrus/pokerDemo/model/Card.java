package net.cukrus.pokerDemo.model;

import static net.cukrus.pokerDemo.model.CardSuit.*;

public enum Card {
    JOKER(1, NONE, "Joker"),
    D_2(2, DIAMONDS, "2ofD"),
    D_3(3, DIAMONDS, "3ofD"),
    D_4(4, DIAMONDS, "4ofD"),
    D_5(5, DIAMONDS, "5ofD"),
    D_6(6, DIAMONDS, "6ofD"),
    D_7(7, DIAMONDS, "7ofD"),
    D_8(8, DIAMONDS, "8ofD"),
    D_9(9, DIAMONDS, "9ofD"),
    D_10(10, DIAMONDS, "10ofD"),
    D_J(11, DIAMONDS, "JofD"),
    D_Q(12, DIAMONDS, "QofD"),
    D_K(13, DIAMONDS, "KofD"),
    D_A(14, DIAMONDS, "AofD"),
    C_2(2, CLUBS, "2ofC"),
    C_3(3, CLUBS, "3ofC"),
    C_4(4, CLUBS, "4ofC"),
    C_5(5, CLUBS, "5ofC"),
    C_6(6, CLUBS, "6ofC"),
    C_7(7, CLUBS, "7ofC"),
    C_8(8, CLUBS, "8ofC"),
    C_9(9, CLUBS, "9ofC"),
    C_10(10, CLUBS, "10ofC"),
    C_J(11, CLUBS, "JofC"),
    C_Q(12, CLUBS, "QofC"),
    C_K(13, CLUBS, "KofC"),
    C_A(14, CLUBS, "AofC"),
    H_2(2, HEARTS, "2ofH"),
    H_3(3, HEARTS, "3ofH"),
    H_4(4, HEARTS, "4ofH"),
    H_5(5, HEARTS, "5ofH"),
    H_6(6, HEARTS, "6ofH"),
    H_7(7, HEARTS, "7ofH"),
    H_8(8, HEARTS, "8ofH"),
    H_9(9, HEARTS, "9ofH"),
    H_10(10, HEARTS, "10ofH"),
    H_J(11, HEARTS, "JofH"),
    H_Q(12, HEARTS, "QofH"),
    H_K(13, HEARTS, "KofH"),
    H_A(14, HEARTS, "AofH"),
    S_2(2, SPADES, "2ofS"),
    S_3(3, SPADES, "3ofS"),
    S_4(4, SPADES, "4ofS"),
    S_5(5, SPADES, "5ofS"),
    S_6(6, SPADES, "6ofS"),
    S_7(7, SPADES, "7ofS"),
    S_8(8, SPADES, "8ofS"),
    S_9(9, SPADES, "9ofS"),
    S_10(10, SPADES, "10ofS"),
    S_J(11, SPADES, "JofS"),
    S_Q(12, SPADES, "QofS"),
    S_K(13, SPADES, "KofS"),
    S_A(14, SPADES, "AofS");

    private final int ordinal;
    private final CardSuit suit;
    private final String strVal;

    Card(int ordinal, CardSuit suit, String strVal) {
        this.ordinal = ordinal;
        this.suit = suit;
        this.strVal = strVal;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public CardSuit getSuit() {
        return suit;
    }

    public String getStrVal() {
        return strVal;
    }
}
