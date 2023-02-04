package com.qsspy.watmerchbackend.controller.rest;

import com.qsspy.watmerchbackend.entity.Purchase;
import com.qsspy.watmerchbackend.exception.login.LoginException;
import com.qsspy.watmerchbackend.service.IPurchaseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PurchaseController {

    private IPurchaseService purchaseService;

    public PurchaseController(IPurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @ApiOperation(value = "Wykonanie zakupu",
                  notes = "Wykonanie zakupu. " +
                          "Jeżeli zostanie podany nagłówek 'Authorization' zakup będzie skorelowany z użytkownikiem." +
                          "Podanie pola 'user' może być całkowicie pominięte")
    @PostMapping("/buy")
    public void makePurchase(
            @RequestHeader(name = "Authorization", required = false) String authString,
            @ApiParam(name = "Obiekt zakupu") @RequestBody Purchase purchase) throws LoginException {

        purchaseService.makePurchase(purchase, authString);
    }

    @ApiOperation(value = "Pobranie strony listy zakupów",
                  notes = "Pobranie strony listy zakupów dla użytkownika podanego w 'Authorization'")
    @GetMapping("/purchases")
    public Page<Purchase> getPurchases(
            @RequestHeader(name = "Authorization") String authString,
            @ApiParam(name = "Wielkość strony") @RequestParam(defaultValue = "20") Integer size,
            @ApiParam(name = "Numer strony (0-indexed)") @RequestParam(defaultValue = "0") Integer page
    ) {

        return purchaseService.getPurchases(authString, size, page);
    }
}
