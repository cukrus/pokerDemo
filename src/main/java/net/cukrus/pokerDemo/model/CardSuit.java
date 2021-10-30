package net.cukrus.pokerDemo.model;

import com.google.common.base.CaseFormat;

public enum CardSuit {
    DIAMONDS, CLUBS, HEARTS, SPADES, NONE;

    @Override
    public String toString() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, super.toString());
    }
}
