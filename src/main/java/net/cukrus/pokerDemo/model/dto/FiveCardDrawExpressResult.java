package net.cukrus.pokerDemo.model.dto;

import net.cukrus.pokerDemo.model.PokerHandInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FiveCardDrawExpressResult {
    private Date requestReceived;
    private Date requestProcessed;
    private List<PokerHandInfo> hands = new ArrayList<>();
    private PokerHandInfo winner;

    public FiveCardDrawExpressResult() {}

    public FiveCardDrawExpressResult(Date requestReceived) {
        this.requestReceived = requestReceived;
    }

    public List<PokerHandInfo> getHands() {
        return hands;
    }

    public void setHands(List<PokerHandInfo> hands) {
        this.hands = hands;
    }

    public PokerHandInfo getWinner() {
        return winner;
    }

    public void setWinner(PokerHandInfo winner) {
        this.winner = winner;
    }

    public Date getRequestReceived() {
        return requestReceived;
    }

    public void setRequestReceived(Date requestReceived) {
        this.requestReceived = requestReceived;
    }

    public Date getRequestProcessed() {
        return requestProcessed;
    }

    public void setRequestProcessed(Date requestProcessed) {
        this.requestProcessed = requestProcessed;
    }
}
