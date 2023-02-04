package com.qsspy.watmerchbackend.controller.rest;

import com.qsspy.watmerchbackend.entity.CreditCard;
import com.qsspy.watmerchbackend.service.CreditCardService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CreditCardController {

    private CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @ApiOperation(value = "Dodaj kartę użytkownikowi",
            notes = "Dodaje kartę użytkownikowi, który jest identyfikowany przez nagłowek 'Authorization'")
    @PostMapping("/creditCards")
    public CreditCard postCreditCard(
            @ApiParam(name = "Dane karty kredytowej") @RequestBody CreditCard creditCard,
            @RequestHeader(name = "Authorization") String authString
    ) {
        return creditCardService.postCreditCard(creditCard, authString);
    }

    @ApiOperation(value = "Pobierz karty kredytowe użytkownika",
            notes = "Pobiera karty kredytowe użytkownika, który jest identyfikowany przez nagłowek 'Authorization'")
    @GetMapping("/creditCards")
    public List<CreditCard> getCreditCards(
            @RequestHeader(name = "Authorization") String authString
    ) {
        return creditCardService.getCreditCards(authString);
    }

    @ApiOperation(value = "Usuń kartę użytkownikowi",
            notes = "Usuwa kartę użytkownikowi, który jest identyfikowany przez nagłowek 'Authorization'")
    @DeleteMapping("/creditCards")
    public String deleteCreditCard(
            @ApiParam(name = "Id karty kredytowej") @RequestParam Long cardId
    ) {
        creditCardService.deleteCreditCard(cardId);
        return "Card deleted successfully";
    }
}
