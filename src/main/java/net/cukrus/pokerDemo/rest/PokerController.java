package net.cukrus.pokerDemo.rest;

import net.cukrus.pokerDemo.game.CardCombinationResolver;
import net.cukrus.pokerDemo.game.CardsParseInfo;
import net.cukrus.pokerDemo.game.FiveCardDrawExpressGame;
import net.cukrus.pokerDemo.game.FiveCardPokerCombinationResolver;
import net.cukrus.pokerDemo.model.Card;
import net.cukrus.pokerDemo.model.dto.FiveCardDrawExpressResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;

@RestController
@RequestMapping("/poker")
public class PokerController {

    //TODO could move logic to a service
    @GetMapping("/5CardDrawExpress")
    public FiveCardDrawExpressResult dealAndDecideWinner(@RequestParam(value = "players", defaultValue = "2") int players) {
        FiveCardDrawExpressResult result = new FiveCardDrawExpressResult(new Date());
        //TODO can create a better hierarchy structure for games, and maybe factory
        FiveCardDrawExpressGame game = new FiveCardDrawExpressGame();
        //TODO could make a factory for this
        CardCombinationResolver handResolver = new FiveCardPokerCombinationResolver();
        for(int i = 0; i < players; i++) {
            result.getHands().add(handResolver.resolveCombo(new CardsParseInfo(game.dealCards(5).toArray(new Card[0]))));
        }
        Collections.sort(result.getHands());
        result.setWinner(result.getHands().get(players - 1));
        result.setRequestProcessed(new Date());
        //TODO save info to DB
        return result;
    }

    //TODO create API for accepting 5 card hands for winner determination?
}
