package net.cukrus.pokerDemo.model;

import net.cukrus.pokerDemo.game.CardsParseInfo;

public class PokerHandInfo implements Comparable<PokerHandInfo> {
    private PokerCardCombo combo;
    private CardsParseInfo parseInfo;
    private int comboOrdinalSum;
    private int handOrdinalSum;

    public PokerHandInfo(CardsParseInfo parseInfo, int handOrdinalSum) {
        this.parseInfo = parseInfo;
        this.handOrdinalSum = handOrdinalSum;
    }

    public PokerHandInfo(PokerCardCombo combo, CardsParseInfo parseInfo, int comboOrdinalSum, int handOrdinalSum) {
        this.combo = combo;
        this.parseInfo = parseInfo;
        this.comboOrdinalSum = comboOrdinalSum;
        this.handOrdinalSum = handOrdinalSum;
    }

    @Override
    public int compareTo(PokerHandInfo o) {
        int tOrdinal = combo.ordinal();
        int oOrdinal = o.getCombo().ordinal();
        if (combo == o.getCombo()) {
            if (comboOrdinalSum == o.getComboOrdinalSum()) {
                if (handOrdinalSum != o.getHandOrdinalSum()) {
                    return handOrdinalSum > o.getHandOrdinalSum() ? 1 : -1;
                }
            } else {
                return comboOrdinalSum > o.getComboOrdinalSum() ? 1 : -1;
            }
        } else {
            return tOrdinal > oOrdinal ? 1 : -1;
        }
        return 0;
    }

    public PokerCardCombo getCombo() {
        return combo;
    }

    public void setCombo(PokerCardCombo combo) {
        this.combo = combo;
    }

    public CardsParseInfo getParseInfo() {
        return parseInfo;
    }

    public void setParseInfo(CardsParseInfo parseInfo) {
        this.parseInfo = parseInfo;
    }

    public int getComboOrdinalSum() {
        return comboOrdinalSum;
    }

    public void setComboOrdinalSum(int comboOrdinalSum) {
        this.comboOrdinalSum = comboOrdinalSum;
    }

    public int getHandOrdinalSum() {
        return handOrdinalSum;
    }

    public void setHandOrdinalSum(int handOrdinalSum) {
        this.handOrdinalSum = handOrdinalSum;
    }
}
